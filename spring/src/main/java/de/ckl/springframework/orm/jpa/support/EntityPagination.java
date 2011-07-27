package de.ckl.springframework.orm.jpa.support;

import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Returns a subset defined by a {@link PaginationBean} from a JPA {@link Query}
 * 
 * @author ckl
 * 
 */
public class EntityPagination<E> implements ApplicationContextAware {
	private ApplicationContext applicationContext;

	@SuppressWarnings("unchecked")
	public List<E> getEntities(Query _query) {
		PaginationBean paginationBean = applicationContext
				.getBean(PaginationBean.class);

		_query.setMaxResults(paginationBean.getSize());
		_query.setFirstResult(paginationBean.getOffset());

		return (List<E>) _query.getResultList();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
}
