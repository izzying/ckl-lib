package de.ckl.testing.domain.generator;

import java.lang.reflect.Field;
import java.util.HashMap;

import de.ckl.testing.domain.generator.interfaces.UniqueValueGenerator;

/**
 * Returns a random integer
 * 
 * @author ckl
 */
public class IntGenerator implements UniqueValueGenerator
{

	@Override
	public Object generate(Field _field, HashMap<String, Object> _options,
			long minLength, long maxLength)
	{
		return minLength
				+ (long) (Math.random() * (maxLength - minLength) + 0.5);
	}
}
