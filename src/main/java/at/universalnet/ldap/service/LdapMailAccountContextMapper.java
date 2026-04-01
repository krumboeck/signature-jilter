package at.universalnet.ldap.service;

import java.util.Arrays;
import java.util.Base64;

import javax.naming.Name;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.stereotype.Service;

import at.universalnet.ldap.api.main.entry.LdapMailAccount;

@Service
public class LdapMailAccountContextMapper extends AbstractContextMapper<LdapMailAccount> {

	private static final Logger LOG = LoggerFactory.getLogger(LdapMailAccountContextMapper.class);

	@Value("${ldap.map.attribute.uid:uid}")
	private String attrUid;

	@Value("${ldap.map.attribute.departmentNumber:departmentNumber}")
	private String attrDepartmentNumber;

	@Value("${ldap.map.attribute.cn:cn}")
	private String attrCn;

	@Value("${ldap.map.attribute.displayName:displayName}")
	private String attrDisplayName;

	@Value("${ldap.map.attribute.employeeNumber:employeeNumber}")
	private String attrEmployeeNumber;

	@Value("${ldap.map.attribute.employeeType:employeeType}")
	private String attrEmployeeType;

	@Value("${ldap.map.attribute.givenName:givenName}")
	private String attrGivenName;

	@Value("${ldap.map.attribute.preferredLanguage:preferredLanguage}")
	private String attrPreferredLanguage;

	@Value("${ldap.map.attribute.mail:mail}")
	private String attrMail;

	@Value("${ldap.map.attribute.homePhone:homePhone}")
	private String attrHomePhone;

	@Value("${ldap.map.attribute.homePostalAddress:homePostalAddress}")
	private String attrHomePostalAddress;

	@Value("${ldap.map.attribute.initials:initials}")
	private String attrInitials;

	@Value("${ldap.map.attribute.mobile:mobile}")
	private String attrMobile;

	@Value("${ldap.map.attribute.labeledURI:labeledURI}")
	private String attrLabeledURI;

	@Value("${ldap.map.attribute.pager:pager}")
	private String attrPager;

	@Value("${ldap.map.attribute.roomNumber:roomNumber}")
	private String attrRoomNumber;

	@Value("${ldap.map.attribute.mailAddresses:mailAddresses}")
	private String attrMailAddresses;

	@Value("${ldap.map.attribute.sn:sn}")
	private String attrSn;

	@Value("${ldap.map.attribute.description:description}")
	private String attrDescription;

	@Value("${ldap.map.attribute.facsimileTelephoneNumber:facsimileTelephoneNumber}")
	private String attrFacsimileTelephoneNumber;

	@Value("${ldap.map.attribute.title:title}")
	private String attrTitle;

	@Value("${ldap.map.attribute.o:o}")
	private String attrO;

	@Value("${ldap.map.attribute.ou:ou}")
	private String attrOu;

	@Value("${ldap.map.attribute.street:street}")
	private String attrStreet;

	@Value("${ldap.map.attribute.postalCode:postalCode}")
	private String attrPostalCode;

	@Value("${ldap.map.attribute.l:l}")
	private String attrL;

	@Value("${ldap.map.attribute.internationaliSDNNumber:internationaliSDNNumber}")
	private String attrInternationaliSDNNumber;

	@Value("${ldap.map.attribute.telephoneNumber:telephoneNumber}")
	private String attrTelephoneNumber;

	@Value("${ldap.map.attribute.physicalDeliveryOfficeName:physicalDeliveryOfficeName}")
	private String attrPhysicalDeliveryOfficeName;

	@Value("${ldap.map.attribute.jpegPhoto:jpegPhoto}")
	private String attrJpegPhoto;

	@Value("${ldap.map.attribute.photo:photo}")
	private String attrPhoto;

	@Value("${ldap.map.attribute.postalAddress:postalAddress}")
	private String attrPostalAddress;

	@Value("${ldap.map.attribute.postOfficeBox:postOfficeBox}")
	private String attrPostOfficeBox;

	@Value("${ldap.map.attribute.registeredAddress:RegisteredAddress}")
	private String attrRegisteredAddress;

	@Value("${ldap.map.attribute.st:st}")
	private String attrSt;

	@Value("${ldap.map.attribute.teletexTerminalIdentifier:teletexTerminalIdentifier}")
	private String attrTeletexTerminalIdentifier;

	@Value("${ldap.map.attribute.telexNumber:telexNumber}")
	private String attrTelexNumber;

	public LdapMailAccountContextMapper() {
		super();
	}

	@Override
	protected LdapMailAccount doMapFromContext(DirContextOperations ctx) {
        LdapMailAccount account = new LdapMailAccount();
        Name dn = ctx.getDn();
        account.setDistinguishedName(dn);
        account.setUid(ctx.getStringAttribute(attrUid));
        account.setDepartmentNumber(ctx.getStringAttribute(attrDepartmentNumber));
        account.setCn(ctx.getStringAttribute(attrCn));
        account.setDisplayName(ctx.getStringAttribute(attrDisplayName));
        account.setEmployeeNumber(ctx.getStringAttribute(attrEmployeeNumber));
        account.setEmployeeType(ctx.getStringAttribute(attrEmployeeType));
        account.setGivenName(ctx.getStringAttribute(attrGivenName));
        account.setPreferredLanguage(ctx.getStringAttribute(attrPreferredLanguage));
        account.setMail(ctx.getStringAttribute(attrMail));
        account.setHomePhone(ctx.getStringAttribute(attrHomePhone));
        account.setHomePostalAddress(ctx.getStringAttribute(attrHomePostalAddress));
        account.setInitials(ctx.getStringAttribute(attrInitials));
        account.setMobile(ctx.getStringAttribute(attrMobile));
        account.setLabeledURI(ctx.getStringAttribute(attrLabeledURI));
        account.setPager(ctx.getStringAttribute(attrPager));
        account.setRoomNumber(ctx.getStringAttribute(attrRoomNumber));
        account.setSn(ctx.getStringAttribute(attrSn));
        account.setDescription(ctx.getStringAttribute(attrDescription));
        account.setFacsimileTelephoneNumber(ctx.getStringAttribute(attrFacsimileTelephoneNumber));
        account.setTitle(ctx.getStringAttribute(attrTitle));
        account.setO(ctx.getStringAttribute(attrO));
        account.setOu(ctx.getStringAttribute(attrOu));
        account.setStreet(ctx.getStringAttribute(attrStreet));
        account.setPostalCode(ctx.getStringAttribute(attrPostalCode));
        account.setL(ctx.getStringAttribute(attrL));
        account.setInternationaliSDNNumber(ctx.getStringAttribute(attrInternationaliSDNNumber));
        account.setTelephoneNumber(ctx.getStringAttribute(attrTelephoneNumber));
        account.setPhysicalDeliveryOfficeName(ctx.getStringAttribute(attrPhysicalDeliveryOfficeName));
        account.setPostalAddress(ctx.getStringAttribute(attrPostalAddress));
        account.setPostOfficeBox(ctx.getStringAttribute(attrPostOfficeBox));
        account.setRegisteredAddress(ctx.getStringAttribute(attrRegisteredAddress));
        account.setSt(ctx.getStringAttribute(attrSt));
        account.setTeletexTerminalIdentifier(ctx.getStringAttribute(attrTeletexTerminalIdentifier));
        account.setTelexNumber(ctx.getStringAttribute(attrTelexNumber));

        // Multi-value Attribute
        String[] mailAddresses = ctx.getStringAttributes(attrMailAddresses);
        if (mailAddresses != null) {
            account.setMailAddresses(Arrays.asList(mailAddresses));
        }

        Object jpegPhoto = ctx.getObjectAttribute(attrJpegPhoto);
        if (jpegPhoto instanceof byte[] jpegPhotoBytes) {
            account.setJpegPhoto(jpegPhotoBytes);
        } else if (jpegPhoto instanceof String jpegPhotoStr) {
            // Fallback: Base64-dekodieren
            account.setJpegPhoto(Base64.getDecoder().decode(jpegPhotoStr));
        }

        Object photo = ctx.getObjectAttribute(attrPhoto);
        if (photo instanceof byte[] photoBytes) {
            account.setJpegPhoto(photoBytes);
        } else if (photo instanceof String photoStr) {
            // Fallback: Base64-dekodieren
            account.setPhoto(Base64.getDecoder().decode(photoStr));
        }

        return account;		
	}

}
