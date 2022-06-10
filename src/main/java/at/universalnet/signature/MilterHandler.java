package at.universalnet.signature;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sendmail.jilter.JilterEOMActions;
import com.sendmail.jilter.JilterHandler;
import com.sendmail.jilter.JilterHandlerAdapter;
import com.sendmail.jilter.JilterStatus;

import at.universalnet.ad.api.main.entry.LdapBenutzer;
import at.universalnet.ad.api.main.repo.LdapBenutzerRepository;
import at.universalnet.signature.service.BLOBService;
import jakarta.mail.BodyPart;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;

@Component
@Scope("prototype")
public class MilterHandler extends JilterHandlerAdapter {

	private static final Logger LOG = LoggerFactory.getLogger(MilterHandler.class);

	private static final String MIMETYPE_TEXT_PLAIN = "text/plain";
	private static final String MIMETYPE_TEXT_HTML = "text/html";

	private static final String HEADER_CONTENT_TYPE = "Content-Type";

	@Value("${template.mark.pattern:sig#(?<template>[^#]*)(#(?<mailAddress>[^#]*))?#sig}")
	private String markPatternStr;

	@Value("${template.var.pattern:\\$\\{([^\\}]*)\\}}")
	private String varPatternStr;

	@Value("${template.html.body.start.pattern:<body[^>]*>\\s*}")
	private String bodyStartPatternStr;

	@Value("${template.html.body.end.pattern:\\s*</body>}")
	private String bodyEndPatternStr;

	@Value("${template.dir}")
	private String templateDir;

	@Value("${template.domains:#null}")
	private String templateDomains;

	@Value("${trace.message.body:false}")
	private boolean traceBody;

	@Inject
	private BLOBService blobService;

	@Inject
	private LdapBenutzerRepository ldapBenutzerRepository;

	private MimeMessage mimeMessage = null;
	private Pattern markPattern;
	private Pattern varPattern;
	private Pattern bodyStartPattern;
	private Pattern bodyEndPattern;
	private Pattern fromPattern;
	private String contentType = null;
	private String fromAddress = null;
	private Set<String> domainList = new HashSet<>();

	public MilterHandler() {
		super();
	}

	@PostConstruct
	private void afterPropertiesSet() {
		LOG.debug("Prepare pattern for replace: " + markPatternStr);
		markPattern = Pattern.compile(markPatternStr);
		varPattern = Pattern.compile(varPatternStr);
		bodyStartPattern = Pattern.compile(bodyStartPatternStr);
		bodyEndPattern = Pattern.compile(bodyEndPatternStr);
		fromPattern = Pattern.compile("<([^>]*)>");
		if (templateDomains != null) {
			String[] domains = templateDomains.split(",");
			for (String domain : domains) {
				domainList.add(domain.replaceAll("\\s+", ""));
			}
		}
	}

	@Override
	public int getSupportedProcesses() {
		return JilterHandler.PROCESS_CONNECT | JilterHandler.PROCESS_HELO | JilterHandler.PROCESS_ENVFROM
				| JilterHandler.PROCESS_ENVRCPT | JilterHandler.PROCESS_HEADER | JilterHandler.PROCESS_BODY;
	}

	@Override
	public int getRequiredModifications() {
		return SMFIF_CHGBODY;
	}

    public JilterStatus envfrom(String[] argv, Properties properties) {
    	if (argv.length > 0) {
    		fromAddress = argv[0];
    		Matcher matcher = fromPattern.matcher(fromAddress);
    		if (matcher.find()) {
    			fromAddress = matcher.group(1);
    		}
    		LOG.debug("From: " + fromAddress);
    	} else {
    		LOG.warn("From address not found!");
    	}
		return JilterStatus.SMFIS_CONTINUE;
    }

	@Override
    public JilterStatus header(String headerf, String headerv) {
    	if (HEADER_CONTENT_TYPE.equals(headerf)) {
    		contentType = headerv;
    	}
		return JilterStatus.SMFIS_CONTINUE;
    }

	@Override
	public JilterStatus body(ByteBuffer bodyp) {
		mimeMessage = readEmailBody(bodyp.array());
		return JilterStatus.SMFIS_CONTINUE;
	}

    public JilterStatus eom(JilterEOMActions eomActions, Properties properties) {
    	if (fromAddress == null) {
    		LOG.warn("Filter didn't receive From address! TEMPFAIL");
    		return JilterStatus.SMFIS_TEMPFAIL;
    	}

    	if (mimeMessage == null) {
    		LOG.warn("Filter couldn't read message body! Continue without processing!");
    		return JilterStatus.SMFIS_CONTINUE;
    	}

    	String domain = fromAddress.replaceFirst("^.*@", "");
    	if (domainList.contains(domain)) {

    		boolean applied = false;
    		try {
    			LOG.info("Apply template to message from " + fromAddress);
    			applyTemplate(mimeMessage);
    			applied = true;
    		} catch (MarkNotFoundException e) {
    			LOG.info("Mail contained no mark to replace");
    		} catch (IOException | MessagingException | TemplateException e) {
    			LOG.error("Unable to apply template", e);
    		} catch (Exception e) {
    			LOG.error("Caught unexpected exception", e);
			}

    		if (applied) {
    			LOG.info("Write message back");
	    		byte[] rawBody = writeEmailBody(mimeMessage);
				try {
					eomActions.replacebody(ByteBuffer.wrap(rawBody));
				} catch (IOException e) {
					LOG.error("Failed to replace body", e);
				}

				String bodypString = null;
				try {
					bodypString = new String(rawBody, "UTF-8");
					if (traceBody) {
						LOG.debug("Body:\n" + bodypString);
					}
				} catch (UnsupportedEncodingException e) {
					LOG.error("UnsupportedEncodingException", e);
				}
    		}
    	} else {
    		LOG.debug("We don't process, because the domain is not in our list: " + domain);
    	}
        return JilterStatus.SMFIS_CONTINUE;
    }

	@Override
	public JilterStatus data() {
		return JilterStatus.SMFIS_CONTINUE;
	}

	private MimeMessage readEmailBody(byte[] rawBody) {
		String fakeHeader = "Return-Path: <no-reply@example.invalid>\r\n";
		if (contentType != null) {
			fakeHeader += HEADER_CONTENT_TYPE + ": " + contentType + "\r\n";
		}
		fakeHeader += "\r\n";

		byte[] head = fakeHeader.getBytes();
		byte[] body = new byte[head.length + rawBody.length];
		System.arraycopy(head, 0, body, 0, head.length);
		System.arraycopy(rawBody, 0, body, head.length, rawBody.length);

		try {
			Session s = Session.getInstance(new Properties());
			InputStream is = new ByteArrayInputStream(body);
			return new MimeMessage(s, is);
		} catch (MessagingException e) {
			LOG.error("Couldn't parse email body", e);
			return null;
		}
	}

	private byte[] writeEmailBody(MimeMessage message){
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			Object content = message.getContent();
			if (content instanceof String) {
				return ((String) content).getBytes();
			} else if (content instanceof Multipart) {
				Multipart multipart = (Multipart) content;
				multipart.writeTo(baos);
				baos.flush();
				return baos.toByteArray();
			} else {
				LOG.error("Unknown body type");
			}

		} catch (IOException | MessagingException e) {
			LOG.error("Generating the body failed with error", e);
		}
		return null;
	}

	private void applyTemplate(MimeMessage message) throws IOException, MessagingException, TemplateException, MarkNotFoundException {
		Object content = message.getContent();

		if (content instanceof String) {
			LOG.debug("Multipart: no");
			String contentType = message.getContentType();
			LOG.debug("Content type: " + contentType);
			if (contentType.startsWith(MIMETYPE_TEXT_PLAIN) || contentType.startsWith(MIMETYPE_TEXT_HTML)) {
				String newContent = applyTemplate((String) content, contentType);
				message.setContent(newContent, contentType);
				message.saveChanges();
			} else {
				throw new TemplateException("Unknown content type: " + contentType);
			}
		} else if (content instanceof Multipart) {
			LOG.debug("Multipart: yes");
			Multipart multipart = (Multipart) content;
			for (int i = 0; i < multipart.getCount(); i++) {
				BodyPart part = multipart.getBodyPart(i);
				Object partContent = part.getContent();
				if (!(partContent instanceof String)) {
					continue;
				}

				String contentType = part.getContentType();
				String disp = part.getDisposition();
				boolean changes = false;
				if (disp == null || disp.equalsIgnoreCase(Part.INLINE)) {
					if (contentType.startsWith(MIMETYPE_TEXT_PLAIN) || contentType.startsWith(MIMETYPE_TEXT_HTML)) {
						try {
							String newContent = applyTemplate((String) partContent, contentType);
							part.setContent(newContent, contentType);
							message.saveChanges();
							changes = true;
						} catch (MarkNotFoundException e) {
							// Do nothing.
						}
					} else {
						throw new TemplateException("Unknown content type: " + contentType);
					}
				}
				if (!changes) {
					throw new MarkNotFoundException("No part has any mark");
				}
			}
		} else {
			throw new TemplateException("Unknown content object: " + content.getClass().getName());
		}
	}

	private String applyTemplate(String content, String contentType) throws MarkNotFoundException, TemplateException {
		String temp = content;
		int count = 0;

		if (traceBody) {
			LOG.debug("Body:\n" + content);
		}
		LOG.debug("Try to find mark");
		Matcher matcher = markPattern.matcher(temp);
		if (matcher.find()) {
			LOG.debug("Mark found");
			count++;
			String templateName = matcher.group("template");
			String mailAddress = matcher.group("mailAddress");

			LOG.debug("Template: " + templateName);
			LOG.debug("Mail address: " + mailAddress);
			LOG.debug("Mark starts: " + matcher.start());
			LOG.debug("Mark stops: " + matcher.end());
			
			if (mailAddress == null) {
				mailAddress = fromAddress;
				LOG.debug("Use \"From\" address because \"Mail address\" value not found: " + mailAddress);
			}

			String template = getTemplate(templateName, contentType);
			LdapBenutzer ldapBenutzer = getBenutzerByMail(mailAddress);
			template = processTemplate(template, ldapBenutzer, templateName, mailAddress);


			temp = temp.substring(0, matcher.start()) + template + temp.substring(matcher.end());
		}

		if (count == 0) {
			throw new MarkNotFoundException();
		}

		return temp;
	}

	private String processTemplate(String template, LdapBenutzer benutzer, String templateName, String mailAddress) throws TemplateException {

		String content = template;
		content = getBody(content);
		int count = 0;
		while (true) {
			Matcher matcher = varPattern.matcher(content);
			if (matcher.find()) {
				String expression = matcher.group(1);
				LOG.debug("Expression: " + expression);
				Object value;
				if (expression.startsWith("ldap:")) {
					value = callGetter(benutzer, expression.replaceFirst("ldap:", ""));
					if (value instanceof byte[]) {
						byte[] byteArray = (byte[]) value;
						String mimeType = blobService.detectMimeType(byteArray);
						String valueBase64 = Base64.getEncoder().encodeToString(byteArray);
						value = "data:" + mimeType + ";filename=binary.dat;base64," + valueBase64;
					}
				} else if (expression.equals("mailAddress")) {
					value = mailAddress;
				} else if (expression.equals("templateName")) {
					value = templateName;
				} else {
					value = null;
				}
				logValue(value);
				if (value == null) {
					value = "";
				}
				content = content.substring(0, matcher.start()) + value + content.substring(matcher.end());
			} else {
				break;
			}

			count++;
			if (count >= 30) {
				throw new TemplateException("Endless loop detected! Please check your configuration and template values.");
			}
		}
		return content;
	}
	
	private void logValue(Object value) {
		if (value == null) {
			LOG.debug("Value: (not found)");
		} else if (value instanceof String) {
			String strValue = (String) value;
			if (strValue.length() > 70) {
				strValue = strValue.substring(0, 70) + "...";
			}
			LOG.debug("Value: " + strValue);
		} else {
			LOG.debug("Value: " + value);
		}
	}

	private Object callGetter(Object obj, String fieldName) throws TemplateException {
		PropertyDescriptor pd;
		try {
			pd = new PropertyDescriptor(fieldName, obj.getClass());
			return pd.getReadMethod().invoke(obj);
		} catch (IntrospectionException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new TemplateException("Problem calling getter for property: " + fieldName);
		}
	}

	private String getBody(String content) {
		Matcher matcherStart = bodyStartPattern.matcher(content);
		Matcher matcherEnd = bodyEndPattern.matcher(content);
		if (matcherStart.find()) {
			if (matcherEnd.find()) {
				return content.substring(matcherStart.end(), matcherEnd.start());
			}
		}
		return content;
	}

	private String getTemplate(String name, String contentType) throws TemplateException {
		String extension = getExtension(contentType);
		Path templatePath = Paths.get(templateDir + "/" + name + "." + extension);
		if (Files.notExists(templatePath)) {
			throw new TemplateException("Template does not exist: " + templatePath);
		}
		if (!Files.isReadable(templatePath)) {
			throw new TemplateException("Template is not readable: " + templatePath);
		}

		try {
		    byte[] bytes = Files.readAllBytes(templatePath);
		    return new String(bytes);
		} catch (IOException e) {
			throw new TemplateException("Error reading template", e);
		}
	}

	private String getExtension(String contentType) {
		if (contentType.startsWith(MIMETYPE_TEXT_PLAIN)) {
			return "txt";
		} else if (contentType.startsWith(MIMETYPE_TEXT_HTML)) {
			return "html";
		}
		LOG.warn("Content-type: " + contentType + " unknown. Fallback to text/plain.");
		return "txt";
	}

	private LdapBenutzer getBenutzerByMail(String mailAddress) throws TemplateException {
		LdapBenutzer benutzer = ldapBenutzerRepository.findByMail(mailAddress);
		if (benutzer == null) {
			throw new TemplateException("User could not be found with mail address: " + mailAddress);
		}
		return benutzer;
	}

}
