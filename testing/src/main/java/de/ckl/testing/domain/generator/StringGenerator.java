package de.ckl.testing.domain.generator;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.apache.commons.lang.RandomStringUtils;
import org.springmodules.validation.bean.conf.loader.annotation.handler.Email;

import de.ckl.testing.domain.generator.interfaces.UniqueValueGenerator;
import de.ckl.testing.domain.handlers.SpringValidationAnnotatedDomain;

/**
 * Creates a random string.
 * 
 * If options contains
 * {@link SpringValidationAnnotatedDomain#ENABLE_SPRING_VALIDATION_ANNOTATION},
 * the String is generated by allowed validation values. Currently only
 * validation of {@link Email} annotation is available.
 * 
 * @author ckl
 * 
 */
public class StringGenerator implements UniqueValueGenerator
{
	@Override
	public Object generate(Field _field, HashMap<String, Object> _options,
			long minLength, long maxLength)
	{
		if (_options
				.containsKey(SpringValidationAnnotatedDomain.ENABLE_SPRING_VALIDATION_ANNOTATION))
		{
			if (_field.isAnnotationPresent(Email.class))
			{
				StringBuffer sb = new StringBuffer();
				sb.append(RandomStringUtils.random(10, true, true));
				sb.append("@");
				sb.append(RandomStringUtils.random(20, true, true));
				sb.append(".");
				sb.append(RandomStringUtils.random(3, true, true));

				return sb.toString();
			}
		}

		long length = minLength
				+ (long) (Math.random() * (maxLength - minLength) + 0.5);

		return RandomStringUtils.randomAlphanumeric((int) length);
	}
}