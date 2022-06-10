package at.universalnet.ad.impl.main.repo;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.naming.Name;
import javax.naming.directory.SearchControls;

import org.springframework.ldap.query.ContainerCriteria;
import org.springframework.ldap.query.LdapQueryBuilder;

import at.universalnet.ad.api.main.repo.ReadOnlyRepository;
import at.universalnet.signature.util.Utils;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

public class ReadOnlyRepositoryImpl<E> extends RepositoryImpl implements ReadOnlyRepository<E> {

	private Type actualTypeArgument;

	public ReadOnlyRepositoryImpl() {
		actualTypeArgument = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<E> findAll() {
		return (List<E>) getLdapTemplate().findAll(Utils.getClass(actualTypeArgument));
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<E> findAll(Name base, SearchControls searchControls) {
		return (List<E>) getLdapTemplate().findAll(base, searchControls, Utils.getClass(actualTypeArgument));
	}

	protected LdapQueryBuilder getQueryBuilder() {
		return query();
	}

	@SuppressWarnings("unchecked")
	protected List<E> find(ContainerCriteria query) {
		return (List<E>) getLdapTemplate().find(query, Utils.getClass(actualTypeArgument));
	}

	@SuppressWarnings("unchecked")
	protected E findOne(ContainerCriteria query) {
		return (E) getLdapTemplate().findOne(query, Utils.getClass(actualTypeArgument));
	}

}
