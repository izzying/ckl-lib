package de.ckl.springframework.orm.jpa.support;

import java.util.List;

import javax.persistence.Query;

/**
 * Returns a subset defined by a {@link PaginationBean} from a JPA {@link Query}
 * 
 * @author ckl
 * 
 */
public class PaginationEntities
{
	private PaginationBean paginationBean;

	public void setPaginationBean(PaginationBean paginationBean)
	{
		this.paginationBean = paginationBean;
	}

	public PaginationBean getPaginationBean()
	{
		return paginationBean;
	}

	@SuppressWarnings("rawtypes")
	public List getEntities(Query _query)
	{
		_query.setMaxResults(getPaginationBean().getSize());
		_query.setFirstResult(getPaginationBean().getOffset());

		return _query.getResultList();
	}
}
