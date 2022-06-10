package at.universalnet.ad.api.main.repo;

public interface ReadWriteRepository<E> extends ReadOnlyRepository<E> {

	E create(E entry);

	E update(E entry);

	void delete(E entry);

}
