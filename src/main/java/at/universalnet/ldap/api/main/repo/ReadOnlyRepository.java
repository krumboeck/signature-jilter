package at.universalnet.ldap.api.main.repo;

import java.util.List;

import javax.naming.Name;
import javax.naming.directory.SearchControls;

public interface ReadOnlyRepository<E> extends Repository {

	List<E> findAll();

	List<E> findAll(Name base, SearchControls searchControls);

}
