package at.universalnet.ldap.service;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import at.universalnet.ldap.api.main.repo.ActiveDirectoryUserRepository;
import at.universalnet.ldap.api.main.repo.LdapMailAccountRepository;

@Service
public class PersonAttributeResolverServiceImpl implements PersonAttributeResolverService {

	private static final Logger LOG = LoggerFactory.getLogger(PersonAttributeResolverServiceImpl.class);

	private static final String LDAP_OBJECT_TYPE_ACTIVE_DIRECTORY = "ad";
	private static final String LDAP_OBJECT_TYPE_LDAP = "ldap";

	@Value("${ldap.object.type:default}")
	private String ldapObjectType;

	@Inject
	private ActiveDirectoryUserRepository activeDirectoryUserRepository;

	@Inject
	private LdapMailAccountRepository ldapMailAccountRepository;

	public PersonAttributeResolverServiceImpl() {
		super();
	}

	@Override
	public Object getPersonAttributesByMail(String mailAddress) {
		if (ldapObjectType.equals(LDAP_OBJECT_TYPE_LDAP)) {
			return ldapMailAccountRepository.findByMail(mailAddress);
		} else if (ldapObjectType.equals(LDAP_OBJECT_TYPE_ACTIVE_DIRECTORY)) {
			return activeDirectoryUserRepository.findByMail(mailAddress);
		} else {
			LOG.debug("ldap.object.type not set -> Fallback to \"ad\"");
			return activeDirectoryUserRepository.findByMail(mailAddress);
		}
	}

}
