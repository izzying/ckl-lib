package de.ckl.springframework.orm.jpa.impl;

import java.util.List;

import org.springframework.orm.jpa.support.JpaDaoSupport;

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
	public List<Domain> update(List<Domain> _domains)
	{
		for (Domain domain : _domains)
		{
			update(domain);
		}

		return _domains;
	}

	public Domain update(Domain _domain)
	{
		getEntityManager().merge(_domain);

		return _domain;
	}

	public Domain persist(Domain domain)
	{
		getEntityManager().persist(domain);

		return domain;
	}

	public List<Domain> persist(List<Domain> domains)
	{
		for (Domain domain : domains)
		{
			persist(domain);
		}

		return domains;
	}

	public void delete(Domain domain)
	{
		domain = getEntityManager().merge(domain);
		getEntityManager().remove(domain);
		domain = null;
	}
}
