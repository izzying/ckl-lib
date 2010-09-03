package de.ckl.springframework.orm.jpa.impl;

import java.util.List;

import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import de.ckl.springframework.orm.jpa.GenericCrudJpaDao;

/**
 * Generic adapter with create, update and delete methods for
 * {@link JpaDaoSupport}
 * 
 * @author ckl
 * 
 * @param <Domain>
 */
abstract public class GenericCrudJpaDaoTemplate<Domain> extends
		GenericJpaDaoTemplate<Domain> implements GenericCrudJpaDao<Domain>
{
	@Transactional
	public List<Domain> update(List<Domain> _domains)
	{
		for (Domain domain : _domains)
		{
			update(domain);
		}

		return _domains;
	}

	@Transactional
	public Domain update(Domain _domain)
	{
		getEntityManager().merge(_domain);

		return _domain;
	}

	@Transactional
	public Domain persist(Domain domain)
	{
		getEntityManager().persist(domain);

		return domain;
	}

	@Transactional
	public List<Domain> persist(List<Domain> domains)
	{
		for (Domain domain : domains)
		{
			persist(domain);
		}

		return domains;
	}

	@Transactional
	public void delete(Domain domain)
	{
		domain = getEntityManager().merge(domain);
		getEntityManager().remove(domain);
		domain = null;
	}
}
