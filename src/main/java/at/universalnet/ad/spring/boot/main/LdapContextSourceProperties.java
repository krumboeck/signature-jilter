package at.universalnet.ad.spring.boot.main;

import org.springframework.ldap.pool2.factory.PoolConfig;

public class LdapContextSourceProperties {

	private String url;
	private String base;
	private String userDN;
	private String password;
	private PoolConfig pool;

	public LdapContextSourceProperties() {
		super();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getUserDN() {
		return userDN;
	}

	public void setUserDN(String userDN) {
		this.userDN = userDN;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public PoolConfig getPool() {
		return pool;
	}

	public void setPool(PoolConfig pool) {
		this.pool = pool;
	}

}
