package at.universalnet.ldap.api.main.repo;

import at.universalnet.ldap.api.main.entry.LdapMailAccount;

public interface LdapMailAccountRepository extends ReadWriteRepository<LdapMailAccount> {

	LdapMailAccount findByMail(String mail);

}
