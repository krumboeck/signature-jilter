package at.universalnet.ldap.api.main.entry;

import java.util.List;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Attribute.Type;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

@Entry(objectClasses = { "top", "person", "inetOrgPerson", "mailAccount" })
public final class LdapMailAccount {

	public static final String ATTR_MAIL = "mail";
	public static final String ATTR_MAIL_ADDRESSES = "mailAddresses";
	public static final String ATTR_UID = "uid";

	@Id
	private Name distinguishedName;

	@Attribute(name = "departmentNumber")
	private String departmentNumber;

	@Attribute(name = "uid")
	@DnAttribute(value = "uid", index = 1)
	private String uid;

	@Attribute(name = "cn")
	private String cn;

	@Attribute(name = "displayName")
	private String displayName;

	@Attribute(name = "employeeNumber")
	private String employeeNumber;

	@Attribute(name = "employeeType")
	private String employeeType;

	@Attribute(name = "givenName")
	private String givenName;

	@Attribute(name = "preferredLanguage")
	private String preferredLanguage;

	@Attribute(name = "mail")
	private String mail;

	@Attribute(name = "homePhone")
	private String homePhone;

	@Attribute(name = "homePostalAddress")
	private String homePostalAddress;

	@Attribute(name = "initials")
	private String initials;

	@Attribute(name = "mobile")
	private String mobile;

	@Attribute(name = "labeledURI")
	private String labeledURI;

	@Attribute(name = "pager")
	private String pager;

	@Attribute(name = "roomNumber")
	private String roomNumber;

	@Attribute(name = "mailAddresses")
	private List<String> mailAddresses;

	@Attribute(name = "sn")
	private String sn;

	@Attribute(name = "description")
	private String description;

	@Attribute(name = "facsimileTelephoneNumber")
	private String facsimileTelephoneNumber;

	@Attribute(name = "title")
	private String title;

	@Attribute(name = "o")
	private String o;

	@Attribute(name = "ou")
	private String ou;

	@Attribute(name = "street")
	private String street;

	@Attribute(name = "postalCode")
	private String postalCode;

	@Attribute(name = "l")
	private String l;

	@Attribute(name = "internationaliSDNNumber")
	private String internationaliSDNNumber;

	@Attribute(name = "telephoneNumber")
	private String telephoneNumber;

	@Attribute(name = "physicalDeliveryOfficeName")
	private String physicalDeliveryOfficeName;

	@Attribute(name = "jpegPhoto", type = Type.BINARY)
	private byte[] jpegPhoto;

	@Attribute(name = "photo", type = Type.BINARY)
	private byte[] photo;

	@Attribute(name = "postalAddress")
	private String postalAddress;

	@Attribute(name = "postOfficeBox")
	private String postOfficeBox;

	@Attribute(name = "registeredAddress")
	private String registeredAddress;

	@Attribute(name = "st")
	private String st;

	@Attribute(name = "teletexTerminalIdentifier")
	private String teletexTerminalIdentifier;

	@Attribute(name = "telexNumber")
	private String telexNumber;

	public LdapMailAccount() {
		super();
	}

	public Name getDistinguishedName() {
		return distinguishedName;
	}

	public void setDistinguishedName(Name distinguishedName) {
		this.distinguishedName = distinguishedName;
	}

	public String getDepartmentNumber() {
		return departmentNumber;
	}

	public void setDepartmentNumber(String departmentNumber) {
		this.departmentNumber = departmentNumber;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getCn() {
		return cn;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public String getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getPreferredLanguage() {
		return preferredLanguage;
	}

	public void setPreferredLanguage(String preferredLanguage) {
		this.preferredLanguage = preferredLanguage;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getHomePostalAddress() {
		return homePostalAddress;
	}

	public void setHomePostalAddress(String homePostalAddress) {
		this.homePostalAddress = homePostalAddress;
	}

	public String getInitials() {
		return initials;
	}

	public void setInitials(String initials) {
		this.initials = initials;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getLabeledURI() {
		return labeledURI;
	}

	public void setLabeledURI(String labeledURI) {
		this.labeledURI = labeledURI;
	}

	public String getPager() {
		return pager;
	}

	public void setPager(String pager) {
		this.pager = pager;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public List<String> getMailAddresses() {
		return mailAddresses;
	}

	public void setMailAddresses(List<String> mailAddresses) {
		this.mailAddresses = mailAddresses;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFacsimileTelephoneNumber() {
		return facsimileTelephoneNumber;
	}

	public void setFacsimileTelephoneNumber(String facsimileTelephoneNumber) {
		this.facsimileTelephoneNumber = facsimileTelephoneNumber;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getO() {
		return o;
	}

	public void setO(String o) {
		this.o = o;
	}

	public String getOu() {
		return ou;
	}

	public void setOu(String ou) {
		this.ou = ou;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getL() {
		return l;
	}

	public void setL(String l) {
		this.l = l;
	}

	public String getInternationaliSDNNumber() {
		return internationaliSDNNumber;
	}

	public void setInternationaliSDNNumber(String internationaliSDNNumber) {
		this.internationaliSDNNumber = internationaliSDNNumber;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public String getPhysicalDeliveryOfficeName() {
		return physicalDeliveryOfficeName;
	}

	public void setPhysicalDeliveryOfficeName(String physicalDeliveryOfficeName) {
		this.physicalDeliveryOfficeName = physicalDeliveryOfficeName;
	}

	public byte[] getJpegPhoto() {
		return jpegPhoto;
	}

	public void setJpegPhoto(byte[] jpegPhoto) {
		this.jpegPhoto = jpegPhoto;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public String getPostalAddress() {
		return postalAddress;
	}

	public void setPostalAddress(String postalAddress) {
		this.postalAddress = postalAddress;
	}

	public String getPostOfficeBox() {
		return postOfficeBox;
	}

	public void setPostOfficeBox(String postOfficeBox) {
		this.postOfficeBox = postOfficeBox;
	}

	public String getRegisteredAddress() {
		return registeredAddress;
	}

	public void setRegisteredAddress(String registeredAddress) {
		this.registeredAddress = registeredAddress;
	}

	public String getSt() {
		return st;
	}

	public void setSt(String st) {
		this.st = st;
	}

	public String getTeletexTerminalIdentifier() {
		return teletexTerminalIdentifier;
	}

	public void setTeletexTerminalIdentifier(String teletexTerminalIdentifier) {
		this.teletexTerminalIdentifier = teletexTerminalIdentifier;
	}

	public String getTelexNumber() {
		return telexNumber;
	}

	public void setTelexNumber(String telexNumber) {
		this.telexNumber = telexNumber;
	}

}
