package de.ckl.lang.util;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;

/**
 * Easy access to Javas enumeration type
 * 
 * @author ckl
 */
@SuppressWarnings("rawtypes")
public class EnumerationDao implements InitializingBean
{
	private Class enumReflection;

	private boolean ignoreCase = true;

	/**
	 * Key is name of enum field, value is the index of enumeration field
	 */
	private Map<String, Integer> mapEnumLookup = new HashMap<String, Integer>();

	/**
	 * Constructor
	 * 
	 * @param enumReflection
	 *            must be of type enumeration
	 */
	public EnumerationDao(Class enumReflection)
	{
		setEnumReflection(enumReflection);
	}

	public void setEnumReflection(Class enumReflection)
	{
		this.enumReflection = enumReflection;
	}

	public Class getEnumReflection()
	{
		return enumReflection;
	}

	/**
	 * Returns true if enumeration contains key
	 * 
	 * @param _key
	 * @return
	 */
	public boolean isKey(String _key)
	{
		return mapEnumLookup.containsKey(getKeyInternal(_key));
	}

	/**
	 * Returns the internal key
	 * 
	 * @param _key
	 *            if {@link #isIgnoreCase()} is true, _key is converted to lower
	 *            case
	 * @return
	 */
	private String getKeyInternal(String _key)
	{
		if (isIgnoreCase())
		{
			_key = _key.toLowerCase();
		}

		return _key;
	}

	/**
	 * Returns the value/index of enumeration field
	 * 
	 * @param _key
	 * @return
	 */
	public int getValue(String _key)
	{
		_key = getKeyInternal(_key);

		if (mapEnumLookup.containsKey(_key))
		{
			return mapEnumLookup.get(_key);
		}

		return -1;
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		if (null == enumReflection)
		{
			throw new InvalidParameterException("No enumeration class set");
		}

		if (!enumReflection.isEnum())
		{
			throw new InvalidParameterException("Given class is no enumeration");
		}

		Object[] constants = getEnumReflection().getEnumConstants();

		for (int i = 0, m = constants.length; i < m; i++)
		{
			Object constant = constants[i];

			String key = constant.toString();

			if (isIgnoreCase())
			{
				key = key.toLowerCase();
			}

			mapEnumLookup.put(key, i);
		}
	}

	/**
	 * If true, case of enumeration fields will be ignored
	 * 
	 * @param ignoreCase
	 */
	public void setIgnoreCase(boolean ignoreCase)
	{
		this.ignoreCase = ignoreCase;
	}

	/**
	 * Returns true if enumeration field case is ignored
	 * 
	 * @return
	 */
	public boolean isIgnoreCase()
	{
		return ignoreCase;
	}
}
