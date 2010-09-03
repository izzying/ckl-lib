package de.ckl.testing.domain;

import java.lang.reflect.Field;
import java.util.HashMap;

import de.ckl.testing.domain.generator.interfaces.ValueGenerator;

/**
 * Encapsulates the process of generating random data for a specific object
 * 
 * @author ckl
 * 
 */
public class FieldCreationEnvironment
{
	/**
	 * reflected field
	 */
	private Field field;

	/**
	 * maximum length of returned object
	 */
	private long maxLength;

	/**
	 * minimum length of returned object
	 */
	private long minLength;

	private HashMap<String, Object> mapOptions;

	private ValueGenerator generator;

	public FieldCreationEnvironment(Field _field, ValueGenerator _generator,
			HashMap<String, Object> _mapOptions, long _minLength,
			long _maxLength)
	{
		setField(_field);
		setGenerator(_generator);
		setMapOptions(_mapOptions);
		setMinLength(_minLength);
		setMaxLength(_maxLength);
	}

	public void setField(Field field)
	{
		this.field = field;
	}

	public Field getField()
	{
		return field;
	}

	public void setMaxLength(long maxLength)
	{
		this.maxLength = maxLength;
	}

	public long getMaxLength()
	{
		return maxLength;
	}

	public void setMinLength(long minLength)
	{
		this.minLength = minLength;
	}

	public long getMinLength()
	{
		return minLength;
	}

	public void setMapOptions(HashMap<String, Object> mapOptions)
	{
		this.mapOptions = mapOptions;
	}

	public HashMap<String, Object> getMapOptions()
	{
		return mapOptions;
	}

	public void setGenerator(ValueGenerator generator)
	{
		this.generator = generator;
	}

	public ValueGenerator getGenerator()
	{
		return generator;
	}
}
