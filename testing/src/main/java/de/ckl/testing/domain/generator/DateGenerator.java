package de.ckl.testing.domain.generator;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import de.ckl.testing.domain.generator.interfaces.UniqueValueGenerator;

/**
 * Returns a new date object
 * 
 * @author ckl
 */
public class DateGenerator implements UniqueValueGenerator
{
	/**
	 * constants for setting specific options for this generator
	 */
	public final static String OPTION_DATE = "dateGeneratorOption";

	/**
	 * Which date type should be generated. Only {@link DateOption#CURRENT}
	 * currently available.
	 * 
	 */
	public static enum DateOption
	{
		CURRENT
	}

	@Override
	public Object generate(Field _field, HashMap<String, Object> _options,
			long minLength, long maxLength)
	{
		Date r = Calendar.getInstance().getTime();

		return r;
	}
}
