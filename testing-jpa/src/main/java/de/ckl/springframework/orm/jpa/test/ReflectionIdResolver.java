package de.ckl.springframework.orm.jpa.test;

import java.lang.reflect.Field;
import java.security.InvalidParameterException;

import javax.persistence.Entity;
import javax.persistence.Id;

public class ReflectionIdResolver implements IdResolver
{
	public long resolveId(Object _instance) throws InvalidParameterException
	{
		if (!(_instance.getClass().isAnnotationPresent(Entity.class)))
		{
			throw new InvalidParameterException(
					"Entity must be annotated by @Entity to resolve @Id parameter");
		}

		Field[] fields = _instance.getClass().getDeclaredFields();

		for (Field f : fields)
		{
			if (f.isAnnotationPresent(Id.class))
			{
				long r = 0;

				try
				{
					boolean access = f.isAccessible();
					f.setAccessible(true);
					r = f.getLong(_instance);
					f.setAccessible(access);
				}
				catch (Exception e)
				{
					throw new InvalidParameterException("@Id annotated field ["
							+ f.getName()
							+ "] is not accesible or of type long");
				}

				return r;
			}
		}

		throw new InvalidParameterException(
				"No field of entity is annotated by @Id");
	}

}
