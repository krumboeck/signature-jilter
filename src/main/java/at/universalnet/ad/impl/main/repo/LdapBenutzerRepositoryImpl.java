package at.universalnet.ad.impl.main.repo;

import org.springframework.stereotype.Repository;

import at.universalnet.ad.api.main.entry.LdapBenutzer;
import at.universalnet.ad.api.main.repo.LdapBenutzerRepository;

@Repository
public class LdapBenutzerRepositoryImpl extends ReadWriteRepositoryImpl<LdapBenutzer> implements LdapBenutzerRepository {

	@Override
	public LdapBenutzer findByMail(String mail) {
		return findOne(getQueryBuilder().where(LdapBenutzer.ATTR_PROXY_ADDRESSES).is("smtp:" + mail).or(LdapBenutzer.ATTR_MAIL).is(mail));
	}

}
