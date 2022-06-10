package at.universalnet.ad.impl.main.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ldap.core.LdapTemplate;

import at.universalnet.ad.api.main.repo.Repository;

public class RepositoryImpl implements Repository {

	@Autowired
	@Qualifier("mainLdapTemplate")
	private LdapTemplate ldapTemplate;

	public RepositoryImpl() {
		super();
	}

	protected LdapTemplate getLdapTemplate() {
		return ldapTemplate;
	}

}
