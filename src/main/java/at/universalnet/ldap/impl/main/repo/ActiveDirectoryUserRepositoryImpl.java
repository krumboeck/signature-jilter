package at.universalnet.ldap.impl.main.repo;

import org.springframework.stereotype.Repository;

import at.universalnet.ldap.api.main.entry.ActiveDirectoryUser;
import at.universalnet.ldap.api.main.repo.ActiveDirectoryUserRepository;

@Repository
public class ActiveDirectoryUserRepositoryImpl extends ReadWriteRepositoryImpl<ActiveDirectoryUser> implements ActiveDirectoryUserRepository {

	@Override
	public ActiveDirectoryUser findByMail(String mail) {
		return findOne(getQueryBuilder().where(ActiveDirectoryUser.ATTR_PROXY_ADDRESSES).is("smtp:" + mail).or(ActiveDirectoryUser.ATTR_MAIL).is(mail));
	}

}
