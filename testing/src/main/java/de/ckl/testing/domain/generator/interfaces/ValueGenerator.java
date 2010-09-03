package de.ckl.testing.domain.generator.interfaces;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Interface for generating any random values<br />
 * TODO: move to generics?
 * 
 * @author ckl
 * 
 */
public interface ValueGenerator
{
	/**
	 * Generates a random object
	 * 
	 * @param _field
	 *            reflected field
	 * @param _options
	 *            options for generating random data
	 * @param minLength
	 *            minimum length of field
	 * @param maxLength
	 *            maximum length of field
	 * @return
	 */
	public Object generate(Field _field, HashMap<String, Object> _options,
			long minLength, long maxLength);
}
