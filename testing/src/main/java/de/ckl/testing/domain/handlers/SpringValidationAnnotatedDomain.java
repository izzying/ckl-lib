package de.ckl.testing.domain.handlers;

import java.lang.reflect.Field;

import org.springmodules.validation.bean.conf.loader.annotation.handler.Length;
import org.springmodules.validation.bean.conf.loader.annotation.handler.MaxLength;
import org.springmodules.validation.bean.conf.loader.annotation.handler.MinLength;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;

/**
 * Checks {@link MaxLength}, {@link MinLength}, {@link Length} and
 * {@link NotBlank} annotation
 * 
 * @author ckl
 * 
 */
public class SpringValidationAnnotatedDomain implements IRangeDeterminator
{
	/**
	 * static option for enabling checks of Springs validation annotation
	 */
	public final static String ENABLE_SPRING_VALIDATION_ANNOTATION = "enableSpringValidationAnnotation";

	public long getMaxLength(Field _f)
	{
		if (_f.isAnnotationPresent(MaxLength.class))
		{
			return _f.getAnnotation(MaxLength.class).value();
		}

		if (_f.isAnnotationPresent(Length.class))
		{
			return _f.getAnnotation(Length.class).max();
		}

		return 0;
	}

	public long getMinLength(Field _f)
	{
		if (_f.isAnnotationPresent(MinLength.class))
		{
			return _f.getAnnotation(MinLength.class).value();
		}

		if (_f.isAnnotationPresent(Length.class))
		{
			return _f.getAnnotation(Length.class).min();
		}

		if (_f.isAnnotationPresent(NotBlank.class))
		{
			return 1;
		}

		return 0;
	}
}
