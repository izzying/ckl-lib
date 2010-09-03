package de.ckl.testing.domain.handlers;

import java.lang.reflect.Field;

/**
 * Determines the range of a field
 * 
 * @author ckl
 * 
 */
public interface IRangeDeterminator
{
	/**
	 * Return maximum length of field
	 * 
	 * @param _f
	 * @return
	 */
	public abstract long getMaxLength(Field _f);

	/**
	 * Return minimum length of field
	 * 
	 * @param _f
	 * @return
	 */
	public abstract long getMinLength(Field _f);
}