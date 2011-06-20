package de.ckl.springframework.orm.jpa.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.security.InvalidParameterException;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.junit.Test;

import de.ckl.springframework.orm.jpa.GenericCrudJpaDao;
import de.ckl.testing.domain.DomainPropertyRandomizer;
import de.ckl.testing.domain.DomainPropertyRandomizerTemplate;

/**
 * Generic template for testing entity which are persisted by
 * {@link GenericCrudJpaDao}
 * 
 * @author ckl
 * 
 * @param <E>
 */
public class GenericCrudJpaDaoTestTemplate<E>
{
	private GenericCrudJpaDao<E> dao;

	private DomainPropertyRandomizer<E> domainPropertyRandomizer;

	private IdResolver idResolver = new ReflectionIdResolver();

	public GenericCrudJpaDaoTestTemplate(GenericCrudJpaDao<E> _dao)
	{
		setDao(_dao);

		domainPropertyRandomizer = new DomainPropertyRandomizerTemplate(dao
				.factory().getClass());
	}

	public GenericCrudJpaDaoTestTemplate(GenericCrudJpaDao<E> _dao,
			IdResolver _idResolver)
	{
		setDao(_dao);

		domainPropertyRandomizer = new DomainPropertyRandomizerTemplate(dao
				.factory().getClass());

		idResolver = _idResolver;
	}

	/**
	 * Returns table name for entity
	 * 
	 * @param _instance
	 * @return
	 * @throws InvalidParameterException
	 */
	public String getEntityTable(E _instance) throws InvalidParameterException
	{
		String entityName = null;
		String tableName = null;

		if (_instance.getClass().isAnnotationPresent(Entity.class))
		{
			entityName = _instance.getClass().getAnnotation(Entity.class)
					.name();
		}

		if (_instance.getClass().isAnnotationPresent(Table.class))
		{
			tableName = _instance.getClass().getAnnotation(Table.class).name();
		}

		if (entityName == null && tableName == null)
		{
			throw new InvalidParameterException(
					"Could not resolve database table name of entity. Entity must be annotated by @Table or @Entity");
		}

		if (tableName != null)
		{
			return tableName;
		}

		return entityName;
	}

	@Test
	public void create()
	{
		E instance = getDomainPropertyRandomizer().factory();

		int entriesBeforeCreating = getDao().findAll().size();

		E persistedEntry = getDao().persist(instance);
		int entriesAfterCreating = getDao().findAll().size();
		assertEquals(++entriesBeforeCreating, entriesAfterCreating);

		assertNotNull(persistedEntry);
		assertEquals(persistedEntry.hashCode(), instance.hashCode());
		assertTrue(idResolver.resolveId(instance) > 0);
	}

	@Test
	public void delete()
	{
		E instance = getDomainPropertyRandomizer().factory();
		int entriesBeforeCreating = getDao().findAll().size();

		getDao().persist(instance);
		getDao().delete(instance);

		int entriesAfterDelete = getDao().findAll().size();
		assertEquals(entriesBeforeCreating, entriesAfterDelete);
	}

	@Test
	public void findAll()
	{
		List<E> r = getDao().findAll();
		int entriesBefore = (r == null) ? (0) : (r.size());

		E instance = getDomainPropertyRandomizer().factory();
		E persistedInstance = getDao().persist(instance);

		r = getDao().findAll();

		assertNotNull(persistedInstance);
		assertEquals(++entriesBefore, r.size());

		assertTrue(r.contains(persistedInstance));
	}

	@Test
	public void findById()
	{
		E instance = getDomainPropertyRandomizer().factory();
		E persistedEntry = getDao().persist(instance);
		
		// Execute findAll() so JPA detects changes
		getDao().findAll();
		
		long id = idResolver.resolveId(instance);
		assertTrue((id > 0));
		E r = getDao().findById(id);
		assertEquals(instance, r);
	}

	@Test
	public void update()
	{
		E instance = getDomainPropertyRandomizer().factory();
		E persistedEntry = getDao().persist(instance);

		int entriesBeforeUpdate = getDao().findAll().size();

		getDomainPropertyRandomizer().update(persistedEntry);
		E updated = getDao().update(persistedEntry);

		int entriesAfterUpdate = getDao().findAll().size();

		assertEquals(entriesBeforeUpdate, entriesAfterUpdate);
		assertEquals(persistedEntry, updated);
	}

	public void setDao(GenericCrudJpaDao<E> dao)
	{
		this.dao = dao;
	}

	public GenericCrudJpaDao<E> getDao()
	{
		return dao;
	}

	public void setDomainPropertyRandomizer(
			DomainPropertyRandomizer<E> domainPropertyRandomizer)
	{
		this.domainPropertyRandomizer = domainPropertyRandomizer;
	}

	public DomainPropertyRandomizer<E> getDomainPropertyRandomizer()
	{
		return domainPropertyRandomizer;
	}

	public void setIdResolver(IdResolver idResolver)
	{
		this.idResolver = idResolver;
	}

	public IdResolver getIdResolver()
	{
		return idResolver;
	}
}
