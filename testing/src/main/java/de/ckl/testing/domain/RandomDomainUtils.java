package de.ckl.testing.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class with static methods which can be used in unit test cases for fast
 * creation of random objects
 * 
 * @author ckl
 * 
 */
public class RandomDomainUtils
{
	private static Map<Class, DomainPropertyRandomizer> mapDomain = new HashMap<Class, DomainPropertyRandomizer>();

	/**
	 * Returns the randomizer for given class
	 * 
	 * @param _clazz
	 * @return
	 */
	private static DomainPropertyRandomizer lookupInstance(Class _clazz)
	{
		if (!mapDomain.containsKey(_clazz))
		{
			mapDomain.put(_clazz, new DomainPropertyRandomizerTemplate(_clazz));
		}

		return mapDomain.get(_clazz);
	}

	/**
	 * Creates a new random instance
	 * 
	 * @param <E>
	 * @param _clazz
	 * @return
	 */
	public static <E> E factory(Class<E> _clazz)
	{
		return (E) lookupInstance(_clazz).factory();
	}

	/**
	 * Creates a number of new random instances
	 * 
	 * @param <E>
	 * @param _clazz
	 * @param _count
	 * @return
	 */
	public static <E> List<E> factory(Class<E> _clazz, int _count)
	{
		return (List<E>) lookupInstance(_clazz).factory(_count);
	}

	/**
	 * Updates a given instance with new values
	 * 
	 * @param <E>
	 * @param _domain
	 * @return
	 */
	public static <E> E update(E _domain)
	{
		return (E) lookupInstance(_domain.getClass()).update(_domain);
	}
}
