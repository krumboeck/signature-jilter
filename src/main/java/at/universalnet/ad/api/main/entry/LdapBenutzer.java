package at.universalnet.ad.api.main.entry;

import java.util.List;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Attribute.Type;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

@Entry(objectClasses = { "top", "person", "organizationalPerson", "user" })
public final class LdapBenutzer {

	public static final String ATTR_MAIL = "mail";
	public static final String ATTR_PROXY_ADDRESSES = "proxyAddresses";
	public static final String ATTR_SAM_ACCOUNT_NAME = "sAMAccountName";
	public static final String ATTR_USER_PRINCIPAL_NAME = "userPrincipalName";

	@Id
	private Name distinguishedName;

	@Attribute(name = "badPwdCount")
	private Integer badPwdCount;

	@Attribute(name = "cn")
	@DnAttribute(value = "cn", index = 1)
	private String commonName;

	@Attribute(name = "codePage")
	private Integer codePage;

	@Attribute(name = "countryCode")
	private Integer countryCode;

	@Attribute(name = "displayName")
	private String displayName;

	@Attribute(name = "givenName")
	private String givenName;

	@Attribute(name = "logonCount")
	private Integer logonCount;

	@Attribute(name = "mail")
	private String mail;

	@Attribute(name = "name")
	private String name;

	@Attribute(name = "objectCategory")
	private String objectCategory;

	@Attribute(name = "objectGUID")
	private String objectGUID;

	@Attribute(name = "objectSid")
	private String objectSid;

	@Attribute(name = "proxyAddresses")
	private List<String> proxyAddresses;

	@Attribute(name = "sAMAccountName")
	private String sAMAccountName;

	@Attribute(name = "sn")
	private String sn;

	@Attribute(name = "userAccountControl")
	private Integer userAccountControl;

	@Attribute(name = "userPrincipalName")
	private String userPrincipalName;

	@Attribute(name = "title")
	private String title;

	@Attribute(name = "company")
	private String company;

	@Attribute(name = "department")
	private String department;

	@Attribute(name = "streetAddress")
	private String streetAddress;

	@Attribute(name = "postalCode")
	private String postalCode;

	@Attribute(name = "l")
	private String l;

	@Attribute(name = "co")
	private String co;

	@Attribute(name = "telephoneNumber")
	private String telephoneNumber;

	@Attribute(name = "wWWHomePage")
	private String wWWHomePage;

	@Attribute(name = "thumbnailPhoto", type = Type.BINARY)
	private byte[] thumbnailPhoto;

	@Attribute(name = "primaryTelexNumber")
	private String primaryTelexNumber;

	@Attribute(name = "mobile")
	private String mobile;

	@Attribute(name = "facsimileTelephoneNumber")
	private String facsimileTelephoneNumber;

	@Attribute(name = "homePhone")
	private String homePhone;

	@Attribute(name = "ipPhone")
	private String ipPhone;

	public LdapBenutzer() {
		super();
	}

	public Name getDistinguishedName() {
		return distinguishedName;
	}

	public void setDistinguishedName(Name distinguishedName) {
		this.distinguishedName = distinguishedName;
	}

	public Integer getBadPwdCount() {
		return badPwdCount;
	}

	public void setBadPwdCount(Integer badPwdCount) {
		this.badPwdCount = badPwdCount;
	}

	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	public Integer getCodePage() {
		return codePage;
	}

	public void setCodePage(Integer codePage) {
		this.codePage = codePage;
	}

	public Integer getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(Integer countryCode) {
		this.countryCode = countryCode;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public Integer getLogonCount() {
		return logonCount;
	}

	public void setLogonCount(Integer logonCount) {
		this.logonCount = logonCount;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getObjectCategory() {
		return objectCategory;
	}

	public void setObjectCategory(String objectCategory) {
		this.objectCategory = objectCategory;
	}

	public String getObjectGUID() {
		return objectGUID;
	}

	public void setObjectGUID(String objectGUID) {
		this.objectGUID = objectGUID;
	}

	public String getObjectSid() {
		return objectSid;
	}

	public void setObjectSid(String objectSid) {
		this.objectSid = objectSid;
	}

	public List<String> getProxyAddresses() {
		return proxyAddresses;
	}

	public void setProxyAddresses(List<String> proxyAddresses) {
		this.proxyAddresses = proxyAddresses;
	}

	public String getsAMAccountName() {
		return sAMAccountName;
	}

	public void setsAMAccountName(String sAMAccountName) {
		this.sAMAccountName = sAMAccountName;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public Integer getUserAccountControl() {
		return userAccountControl;
	}

	public void setUserAccountControl(Integer userAccountControl) {
		this.userAccountControl = userAccountControl;
	}

	public String getUserPrincipalName() {
		return userPrincipalName;
	}

	public void setUserPrincipalName(String userPrincipalName) {
		this.userPrincipalName = userPrincipalName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
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

	public String getCo() {
		return co;
	}

	public void setCo(String co) {
		this.co = co;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public String getwWWHomePage() {
		return wWWHomePage;
	}

	public void setwWWHomePage(String wWWHomePage) {
		this.wWWHomePage = wWWHomePage;
	}

	public byte[] getThumbnailPhoto() {
		return thumbnailPhoto;
	}

	public void setThumbnailPhoto(byte[] thumbnailPhoto) {
		this.thumbnailPhoto = thumbnailPhoto;
	}

	public String getPrimaryTelexNumber() {
		return primaryTelexNumber;
	}

	public void setPrimaryTelexNumber(String primaryTelexNumber) {
		this.primaryTelexNumber = primaryTelexNumber;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getFacsimileTelephoneNumber() {
		return facsimileTelephoneNumber;
	}

	public void setFacsimileTelephoneNumber(String facsimileTelephoneNumber) {
		this.facsimileTelephoneNumber = facsimileTelephoneNumber;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getIpPhone() {
		return ipPhone;
	}

	public void setIpPhone(String ipPhone) {
		this.ipPhone = ipPhone;
	}

}
