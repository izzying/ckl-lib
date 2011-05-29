package de.ckl.springframework.orm.jpa.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.security.InvalidParameterException;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.junit.Test;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.test.jdbc.SimpleJdbcTestUtils;

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
public class GenericCrudJpaDaoTestTemplate<E> {
	private GenericCrudJpaDao<E> dao;

	private SimpleJdbcTemplate simpleJdbcTemplate;

	private DomainPropertyRandomizer<E> domainPropertyRandomizer;

	public GenericCrudJpaDaoTestTemplate(GenericCrudJpaDao<E> _dao,
			SimpleJdbcTemplate _simpleJdbcTemplate) {
		setDao(_dao);
		setSimpleJdbcTemplate(_simpleJdbcTemplate);

		domainPropertyRandomizer = new DomainPropertyRandomizerTemplate(dao
				.factory().getClass());
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

	/**
	 * Resolves entity Id
	 * 
	 * @param _instance
	 * @return
	 * @throws InvalidParameterException
	 */
	public long resolveId(E _instance) throws InvalidParameterException {
		if (!(_instance.getClass().isAnnotationPresent(Entity.class))) {
			throw new InvalidParameterException(
					"Entity must be annotated by @Entity to resolve @Id parameter");
		}

		Field[] fields = _instance.getClass().getDeclaredFields();

		for (Field f : fields) {
			if (f.isAnnotationPresent(Id.class)) {
				long r = 0;

				try {
					boolean access = f.isAccessible();
					f.setAccessible(true);
					r = f.getLong(_instance);
					f.setAccessible(access);
				} catch (Exception e) {
					throw new InvalidParameterException("@Id annotated field ["
							+ f.getName()
							+ "] is not accesible or of type long");
				}

				return r;
			}
		}

		throw new InvalidParameterException(
				"No field of entity is annotated by @Id");
	}

	@Test
	public void create() {
		E instance = getDomainPropertyRandomizer().factory();

		int entriesBeforeCreating = SimpleJdbcTestUtils.countRowsInTable(
				getSimpleJdbcTemplate(), getEntityTable(instance));

		E persistedEntry = getDao().persist(instance);
		int entriesAfterCreating = SimpleJdbcTestUtils.countRowsInTable(
				getSimpleJdbcTemplate(), getEntityTable(instance));
		assertEquals(++entriesBeforeCreating, entriesAfterCreating);

		assertNotNull(persistedEntry);
		assertEquals(persistedEntry.hashCode(), instance.hashCode());
		assertTrue(resolveId(instance) > 0);
	}

	@Test
	public void delete() {
		E instance = getDomainPropertyRandomizer().factory();
		int entriesBeforeCreating = SimpleJdbcTestUtils.countRowsInTable(
				getSimpleJdbcTemplate(), getEntityTable(instance));

		getDao().persist(instance);
		getDao().delete(instance);

		int entriesAfterDelete = SimpleJdbcTestUtils.countRowsInTable(
				getSimpleJdbcTemplate(), getEntityTable(instance));
		assertEquals(entriesBeforeCreating, entriesAfterDelete);
	}

	@Test
	public void findAll() {
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
	public void findById() {
		E instance = getDomainPropertyRandomizer().factory();
		E persistedEntry = getDao().persist(instance);
		E r = getDao().findById(resolveId(instance));
		assertEquals(r, persistedEntry);
	}

	@Test
	public void update() {
		E instance = getDomainPropertyRandomizer().factory();
		E persistedEntry = getDao().persist(instance);

		int entriesBeforeUpdate = SimpleJdbcTestUtils.countRowsInTable(
				getSimpleJdbcTemplate(), getEntityTable(instance));

		getDomainPropertyRandomizer().update(persistedEntry);
		E updated = getDao().update(persistedEntry);

		int entriesAfterUpdate = SimpleJdbcTestUtils.countRowsInTable(
				getSimpleJdbcTemplate(), getEntityTable(instance));

		assertEquals(entriesBeforeUpdate, entriesAfterUpdate);
		assertEquals(persistedEntry, updated);
	}

	public void setDao(GenericCrudJpaDao<E> dao) {
		this.dao = dao;
	}

	public GenericCrudJpaDao<E> getDao() {
		return dao;
	}

	public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
		this.simpleJdbcTemplate = simpleJdbcTemplate;
	}

	public SimpleJdbcTemplate getSimpleJdbcTemplate() {
		return simpleJdbcTemplate;
	}

	public void setDomainPropertyRandomizer(
			DomainPropertyRandomizer<E> domainPropertyRandomizer) {
		this.domainPropertyRandomizer = domainPropertyRandomizer;
	}

	public DomainPropertyRandomizer<E> getDomainPropertyRandomizer() {
		return domainPropertyRandomizer;
	}
}
