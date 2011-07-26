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
import de.ckl.testing.ObjectRandomizer;
import de.ckl.testing.ObjectRandomizerTemplate;
import de.ckl.testing.handler.jpa.DenySpecialJpaAnnotatedFields;
import de.ckl.testing.handler.jpa.StringLengthHandler;

/**
 * Generic template for testing entity which are persisted by
 * {@link GenericCrudJpaDao}
 * 
 * @author ckl
 * 
 * @param <E>
 */
public class GenericCrudJpaDaoTestTemplate<E> {
	private GenericCrudJpaDao<E> dao;

	private ObjectRandomizer<E> objectRandomizer;

	private IdResolver idResolver = new ReflectionIdResolver();

	public GenericCrudJpaDaoTestTemplate(GenericCrudJpaDao<E> _dao) {
		init(_dao);
	}

	public GenericCrudJpaDaoTestTemplate(GenericCrudJpaDao<E> _dao,
			IdResolver _idResolver) {
		init(_dao);
		idResolver = _idResolver;
	}

	private void init(GenericCrudJpaDao<E> _dao) {
		setDao(_dao);

		objectRandomizer = new ObjectRandomizerTemplate(dao.factory()
				.getClass());

		// register additional field handlers
		objectRandomizer.getFieldHandlers().add(
				new DenySpecialJpaAnnotatedFields());
		objectRandomizer.getFieldHandlers().add(new StringLengthHandler());
	}

	/**
	 * Returns table name for entity
	 * 
	 * @param _instance
	 * @return
	 * @throws InvalidParameterException
	 */
	public String getEntityTable(E _instance) throws InvalidParameterException {
		String entityName = null;
		String tableName = null;

		if (_instance.getClass().isAnnotationPresent(Entity.class)) {
			entityName = _instance.getClass().getAnnotation(Entity.class)
					.name();
		}

		if (_instance.getClass().isAnnotationPresent(Table.class)) {
			tableName = _instance.getClass().getAnnotation(Table.class).name();
		}

		if (entityName == null && tableName == null) {
			throw new InvalidParameterException(
					"Could not resolve database table name of entity. Entity must be annotated by @Table or @Entity");
		}

		if (tableName != null) {
			return tableName;
		}

		return entityName;
	}

	@Test
	public void create() {
		E instance = getObjectRandomizer().createNewInstance();

		int entriesBeforeCreating = getDao().findAll().size();

		E persistedEntry = getDao().persist(instance);
		int entriesAfterCreating = getDao().findAll().size();
		assertEquals(++entriesBeforeCreating, entriesAfterCreating);

		assertNotNull(persistedEntry);
		assertEquals(persistedEntry.hashCode(), instance.hashCode());
		assertTrue(idResolver.resolveId(instance) > 0);
	}

	@Test
	public void delete() {
		E instance = getObjectRandomizer().createNewInstance();
		int entriesBeforeCreating = getDao().findAll().size();

		getDao().persist(instance);
		getDao().delete(instance);

		int entriesAfterDelete = getDao().findAll().size();
		assertEquals(entriesBeforeCreating, entriesAfterDelete);
	}

	@Test
	public void findAll() {
		List<E> r = getDao().findAll();
		int entriesBefore = (r == null) ? (0) : (r.size());

		E instance = getObjectRandomizer().createNewInstance();
		E persistedInstance = getDao().persist(instance);

		r = getDao().findAll();

		assertNotNull(persistedInstance);
		assertEquals(++entriesBefore, r.size());

		assertTrue(r.contains(persistedInstance));
	}

	@Test
	public void findById() {
		E instance = getObjectRandomizer().createNewInstance();
		E persistedEntry = getDao().persist(instance);

		// Execute findAll() so JPA detects changes
		getDao().findAll();

		long id = idResolver.resolveId(instance);
		assertTrue((id > 0));
		E r = getDao().findById(id);
		assertEquals(instance, r);
	}

	@Test
	public void update() {
		E instance = getObjectRandomizer().createNewInstance();
		E persistedEntry = getDao().persist(instance);

		int entriesBeforeUpdate = getDao().findAll().size();

		getObjectRandomizer().update(persistedEntry);
		E updated = getDao().update(persistedEntry);

		int entriesAfterUpdate = getDao().findAll().size();

		assertEquals(entriesBeforeUpdate, entriesAfterUpdate);
		assertEquals(persistedEntry, updated);
	}

	public void setDao(GenericCrudJpaDao<E> dao) {
		this.dao = dao;
	}

	public GenericCrudJpaDao<E> getDao() {
		return dao;
	}

	public void setObjectRandomizer(ObjectRandomizer<E> domainPropertyRandomizer) {
		this.objectRandomizer = domainPropertyRandomizer;
	}

	public ObjectRandomizer<E> getObjectRandomizer() {
		return objectRandomizer;
	}

	public void setIdResolver(IdResolver idResolver) {
		this.idResolver = idResolver;
	}

	public IdResolver getIdResolver() {
		return idResolver;
	}
}
