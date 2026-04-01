package at.universalnet.ldap.api.main.repo;

import at.universalnet.ldap.api.main.entry.ActiveDirectoryUser;

public interface ActiveDirectoryUserRepository extends ReadWriteRepository<ActiveDirectoryUser> {

	ActiveDirectoryUser findByMail(String mail);

}
