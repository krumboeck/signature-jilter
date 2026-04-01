package at.universalnet.signature.service;

import java.util.Base64;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

@Service
public class ToDataUrlMethod implements TemplateMethodModelEx {

	@Inject
	private BLOBService blobService;

	public ToDataUrlMethod() {
		super();
	}

	@Override
	public Object exec(List arguments) throws TemplateModelException {
        if (arguments.size() < 1 || arguments.size() > 2) {
            throw new TemplateModelException("Only one or two arguments");
        }

        Object value = arguments.get(0);
        
        if (!(value instanceof byte[])) {
            throw new TemplateModelException("First argument has to be a byte array");
        }

        String filename = "binary.dat";

        if (arguments.size() > 1) {
        	Object argFilename = arguments.get(1);
        	if (!(argFilename instanceof String)) {
                throw new TemplateModelException("Second argument has to be a String");
        	}
        	filename = (String) argFilename;
        }
        
		byte[] byteArray = (byte[]) value;
		String mimeType = blobService.detectMimeType(byteArray);
		String valueBase64 = Base64.getEncoder().encodeToString(byteArray);
		return "data:" + mimeType + ";filename=" + filename + ";base64," + valueBase64;
	}

}
