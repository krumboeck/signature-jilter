package at.universalnet.ad.impl.main.repo;

import at.universalnet.ad.api.main.repo.ReadWriteRepository;

public class ReadWriteRepositoryImpl<E> extends ReadOnlyRepositoryImpl<E> implements ReadWriteRepository<E> {

	@Override
	public E create(E entry) {
		getLdapTemplate().create(entry);
		return entry;
	}

	@Override
	public E update(E entry) {
		getLdapTemplate().update(entry);
		return entry;
	}

	@Override
	public void delete(E entry) {
		getLdapTemplate().delete(entry);
	}

}
