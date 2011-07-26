package de.ckl.springframework.orm.jpa;

import java.util.List;

import javax.persistence.EntityManager;

public interface GenericCrudJpaDao<Domain>
{
	/**
	 * Returns entity manager
	 * 
	 * @return
	 */
	public EntityManager getEntityManager();

	/**
	 * Flushes the entity cache
	 */
	public void flush();

	/**
	 * Creates a new domain object
	 * 
	 * @param _domain
	 * @return
	 */
	public Domain persist(Domain _domain);

	/**
	 * Persists multiple domain objects
	 * 
	 * @param _domains
	 * @return
	 */
	public List<Domain> persist(List<Domain> _domains);

	/**
	 * Updates a list of domain objects
	 * 
	 * @param _domains
	 * @return
	 */
	public List<Domain> update(List<Domain> _domains);

	/**
	 * Updates a domain object
	 * 
	 * @param _domain
	 * @return
	 */
	public Domain update(Domain _domain);

	/**
	 * Finds domain object by id
	 * 
	 * @param id
	 * @return
	 */
	public Domain findById(long id);

	/**
	 * Finds all domain objects
	 * 
	 * @return
	 */
	public List<Domain> findAll();

	/**
	 * Deletes domain object
	 * 
	 * @param _VO
	 */
	public void delete(Domain _domain);

	/**
	 * Creates a new object instance
	 * 
	 * @return
	 */
	public Domain factory();
}
