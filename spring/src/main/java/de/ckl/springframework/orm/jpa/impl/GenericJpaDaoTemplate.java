package de.ckl.springframework.orm.jpa.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import de.ckl.springframework.orm.jpa.support.QueryHelper;

/**
 * Generic adapter class for retrieving database entries
 * 
 * @author ckl
 * 
 * @param <Domain>
 */
@Repository
public class GenericJpaDaoTemplate<Domain>
{
	private EntityManager em;

	private JpaTemplate jpaTemplate;

	protected QueryHelper queryHelper;

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	public void setEntityManager(EntityManager _em)
	{
		em = _em;
		setJpaTemplate(new JpaTemplate(em));
		setQueryHelper(new QueryHelper(em));
	}

	/**
	 * Returns {@link EntityManager}
	 * 
	 * @return
	 */
	public EntityManager getEntityManager()
	{
		return em;
	}

	/**
	 * Delegates to {@link JpaTemplate#find(String)}
	 * 
	 * @param _s
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Domain> find(String _s)
	{
		return getQueryHelper().createQuery(_s).getResultList();
	}

	/**
	 * Delegates to {@link JpaTemplate#find(String, Object...)} but returns
	 * value in generic manner
	 * 
	 * @param _s
	 * @param values
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Domain> find(String _s, Object... values)
	{
		return getQueryHelper().createQueryByIndex(_s, values).getResultList();
	}

	/**
	 * Find exactly one domain object for given query. If no result set is
	 * loaded from database, null will be returned.
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public Domain findOne(Query query)
	{
		Object r = query.getSingleResult();

		if (r != null)
		{
			return (Domain) r;
		}

		return null;
	}

	/**
	 * Returns a list of Domain objects
	 * 
	 * @param query
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Domain> find(Query query)
	{
		return query.getResultList();
	}

	/**
	 * Find exactly one domain object or returns nullF
	 * 
	 * @param entityClass
	 * @param id
	 * @return
	 */
	@Transactional
	public Domain find(Class<Domain> entityClass, Object id)
	{
		return getEntityManager().find(entityClass, id);
	}

	/**
	 * Returns exactly one element or null
	 * 
	 * @param _s
	 * @param values
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public Domain findOne(String _s, Object... values)
	{
		Object r = null;

		try
		{
			r = getQueryHelper().createQueryByIndex(_s, values)
					.getSingleResult();
		}
		catch (Exception e)
		{
			return null;
		}

		return (Domain) r;
	}

	/**
	 * Executes query and returns result as long
	 * 
	 * @param query
	 * @return
	 */
	@Transactional
	public long count(Query query)
	{
		Long r = (Long) query.getSingleResult();

		return r;
	}

	/**
	 * Führt die Abfrage durch und liefert true zurück, wenn in der
	 * Ergebnisspalte mehr als 0 zurückgeliefert wird.
	 * 
	 * @param query
	 * @return
	 */
	@Transactional
	public boolean hasMoreThanNullResults(Query query)
	{
		long r = count(query);

		return (r > 0);
	}

	/**
	 * Sets the {@link QueryHelper} object
	 * 
	 * @param queryHelper
	 */
	public void setQueryHelper(QueryHelper queryHelper)
	{
		this.queryHelper = queryHelper;
	}

	/**
	 * Returns the {@link QueryHelper} object
	 * 
	 * @return
	 */
	public QueryHelper getQueryHelper()
	{
		return queryHelper;
	}

	public void setJpaTemplate(JpaTemplate jpaTemplate)
	{
		this.jpaTemplate = jpaTemplate;
	}

	public JpaTemplate getJpaTemplate()
	{
		return jpaTemplate;
	}
}
