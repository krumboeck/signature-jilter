package at.universalnet.ldap.impl.main.repo;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.ldap.query.ContainerCriteria;
import org.springframework.stereotype.Repository;

import at.universalnet.ldap.api.main.entry.LdapMailAccount;
import at.universalnet.ldap.api.main.repo.LdapMailAccountRepository;
import at.universalnet.ldap.service.LdapMailAccountContextMapper;

@Repository
public class LdapMailAccountRepositoryImpl extends ReadWriteRepositoryImpl<LdapMailAccount> implements LdapMailAccountRepository {

	@Inject
	private LdapMailAccountContextMapper ldapMailAccountContextMapper;

	@Value("${ldap.filter.objectclass:inetOrgPerson}")
	private String objectclass;

	@Value("${ldap.filter.mailAttributes:mail}")
	private String paramMailAttributes;

	@Override
	public LdapMailAccount findByMail(String mail) {

		List<String> mailAttributes = Arrays.stream(paramMailAttributes.split("[,;]+"))
			    .map(String::trim)
			    .filter(s -> !s.isEmpty())
			    .distinct()
			    .toList();

	    List<ContainerCriteria> orParts = new ArrayList<>();
	    if (mailAttributes.isEmpty()) {
	    	// Fallback
	        orParts.add(query().where("mail").is(mail));
	    } else {
	    	for (String attr : mailAttributes) {
	    		orParts.add(query().where(attr).is(mail));
	    	}
	    }

	    // Erste Bedingung als Basis, Rest per or() anhängen
	    ContainerCriteria orCriteria = orParts.get(0);
	    for (int i = 1; i < orParts.size(); i++) {
	        orCriteria = orCriteria.or(orParts.get(i));
	    }

	    ContainerCriteria finalQuery = query()
	            .where("objectclass").is(objectclass)
	            .and(orCriteria);

		List<LdapMailAccount> results = getLdapTemplate().search(
				finalQuery,
				ldapMailAccountContextMapper
			);

		if (results != null && results.size() == 1) {
			return results.get(0);
		} else {
			throw new IncorrectResultSizeDataAccessException(1, results != null ? results.size() : 0);
		}
	}

}
