package de.ckl.springframework.orm.jpa.support;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

/**
 * Helper class for creating {@link Query} objects and injecting parameters
 * 
 * @author ckl
 * 
 */
public class QueryHelper
{
    Logger log = Logger.getLogger(QueryHelper.class);

    private EntityManager entityManager;

    public enum ParameterType
    {
        INDEX, NAME
    }

    public enum QueryType
    {
        JPQL, NATIVE
    }

    /**
     * @param entityManager
     */
    public QueryHelper(EntityManager entityManager)
    {
        setEntityManager(entityManager);
    }

    /**
     * Delegates to {@link #updateParametersByIndex(Query, Object...)} or
     * {@link #updateParametersByName(Query, Object...)} depends on parameter
     * {@link ParameterType}
     * 
     * @param parameterType
     * @param query
     * @param parameters
     * @return
     */
    protected Query delegateParameterUpdateForQuery(
                    ParameterType parameterType, Query query,
                    Object... parameters)
    {
        if (parameters == null || parameters.length == 0)
        {
            log.error("Could not update parameters for query [" + query
                            + "]: no parameters given");

            return query;
        }

        if (parameterType == ParameterType.NAME)
        {
            return updateParametersByName(query, parameters);
        }

        return updateParametersByIndex(query, parameters);
    }

    /**
     * Creates a new {@link Query} object. The {@link Query} object could be a
     * native query ({@link QueryType} = {@link QueryType#NATIVE}) or a JPQL
     * query ({@link QueryType} == {@link QueryType#JPQL})
     * 
     * @param _sql
     * @param queryType
     * @return
     */
    protected Query createQueryObject(String _sql, QueryType queryType)
    {
        if (queryType == QueryType.JPQL)
        {
            return getEntityManager().createQuery(_sql);
        }

        return getEntityManager().createNativeQuery(_sql);
    }

    /**
     * Delegates to {@link #createQueryObject(String, QueryType)} and
     * {@link #delegateParameterUpdateForQuery(ParameterType, Query, Object...)}
     * 
     * @param _sql
     * @param queryType
     * @param parameterType
     * @param parameters
     * @return
     */
    protected Query createQuery(String _sql, QueryType queryType,
                    ParameterType parameterType, Object... parameters)
    {
        Query q = createQueryObject(_sql, queryType);
        delegateParameterUpdateForQuery(parameterType, q, parameters);
        return q;
    }

    /**
     * Creates a new {@link Query} object, parameters will be injected by name.
     * The count of parameters must be an even number. parameter[0] is the name
     * of named parameter (":param_name"), parameter[1] is the value of
     * parameter[0].
     * 
     * @param _sql
     * @param parameters
     * @return
     */
    public Query createQueryByName(String _sql, Object... parameters)
    {
        return createQuery(_sql, QueryType.JPQL, ParameterType.NAME, parameters);
    }

    /**
     * Creates a JPQL query, parameters will be injected by index.<br />
     * parameters[0] will be assigned to SQL parameter "?1" and so on.
     * 
     * @param _sql
     * @param parameters
     * @return
     */
    public Query createQueryByIndex(String _sql, Object... parameters)
    {
        return createQuery(_sql, QueryType.JPQL, ParameterType.INDEX,
                        parameters);
    }

    /**
     * Creates a JPQL query
     * 
     * @param _sql
     * @return
     */
    public Query createQuery(String _sql)
    {
        return createQueryObject(_sql, QueryType.JPQL);
    }

    /**
     * Crates a native query, parameters will be injected by name.<br />
     * The count of parameters must be an even number. parameter[0] is the name
     * of named parameter (":param_name"), parameter[1] is the value of
     * parameter[0].
     * 
     * @param _sql
     * @param parameters
     * @return
     */
    public Query createNativeQueryByName(String _sql, Object... parameters)
    {
        return createQuery(_sql, QueryType.NATIVE, ParameterType.NAME,
                        parameters);
    }

    /**
     * Creates a native {@link Query} object, parameters will be injected by
     * index.<br />
     * parameters[0] will be assigned to SQL parameter "?1" and so on.
     * 
     * @param _sql
     * @param parameters
     * @return
     */
    public Query createNativeQueryByIndex(String _sql, Object... parameters)
    {
        return createQuery(_sql, QueryType.NATIVE, ParameterType.INDEX,
                        parameters);
    }

    /**
     * Creates a native query
     * 
     * @param _sql
     * @return
     */
    public Query createNativeQuery(String _sql)
    {
        return createQueryObject(_sql, QueryType.NATIVE);
    }

    /**
     * Updates all parameters by name.<br />
     * The count of parameters must be an even number. parameter[0] is the name
     * of named parameter (":param_name"), parameter[1] is the value of
     * parameter[0].
     * 
     * @param q
     * @param parameters
     * @return
     */
    protected Query updateParametersByName(Query q, Object... parameters)
    {
        if (parameters.length % 2 == 1)
        {
            log
                            .error("Could not update parameters by name for query ["
                                            + q
                                            + "]: you have to use an even number of parameters");
        }

        for (int i = 0, m = parameters.length; i < m; i = i + 2)
        {
            String parameterName = parameters[i].toString();
            log.debug("Setting parameter [" + parameterName + "]");
            q.setParameter(parameterName, parameters[(i + 1)]);
        }

        return q;

    }

    /**
     * Updates all parameters by index
     * 
     * @param q
     * @param parameters
     * @return
     */
    protected Query updateParametersByIndex(Query q, Object... parameters)
    {
        for (int i = 0, m = parameters.length; i < m; i++)
        {
            q.setParameter((i + 1), parameters[i]);
        }

        return q;
    }

    /**
     * Set entity manager
     * 
     * @param entityManager
     */
    public void setEntityManager(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    /**
     * Get entity manager
     * 
     * @return
     */
    public EntityManager getEntityManager()
    {
        return entityManager;
    }
}
