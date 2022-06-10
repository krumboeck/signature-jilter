package at.universalnet.ad.api.main.repo;

import at.universalnet.ad.api.main.entry.LdapBenutzer;

public interface LdapBenutzerRepository extends ReadWriteRepository<LdapBenutzer> {

	LdapBenutzer findByMail(String mail);

}
