package de.ckl.testing.domain.handlers;

import java.lang.reflect.Field;

import javax.persistence.Column;
import javax.persistence.Transient;

/**
 * Support for JPA annotated fields
 * 
 * @author ckl
 */
public class JpaAnnotatedDomain implements IRangeDeterminator
{
	/**
	 * static option for enabling JPA support
	 */
	public final static String ENABLE_JPA = "enableJpa";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.ckl.domain.test.requires.IRangeDeterminator#getMaxLength(java.lang
	 * .reflect.Field)
	 */
	@Override
	public long getMaxLength(Field _f)
	{
		if (_f.isAnnotationPresent(Transient.class))
		{
			return 0;
		}

		if (_f.isAnnotationPresent(Column.class))
		{
			long maxValue = _f.getAnnotation(Column.class).length();

			if (_f.getType().equals(Integer.class)
					|| _f.getType().equals(Long.class))
			{
				maxValue = ((10 ^ maxValue) - 1);
			}

			return maxValue;
		}

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.ckl.domain.test.requires.IRangeDeterminator#getMinLength(java.lang
	 * .reflect.Field)
	 */
	@Override
	public long getMinLength(Field _f)
	{
		if (_f.isAnnotationPresent(Transient.class))
		{
			return 0;
		}

		if (_f.isAnnotationPresent(Column.class)
				&& _f.getAnnotation(Column.class).nullable())
		{
			return 0;
		}

		return 1;
	}
}
