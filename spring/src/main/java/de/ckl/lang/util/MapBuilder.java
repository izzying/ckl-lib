package de.ckl.lang.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple fluent interface for generating maps.
 * 
 * @author ckl
 * @param <K>
 * @param <V>
 */
public class MapBuilder<K, V>
{
	private Map<K, V> map = new HashMap<K, V>();

	/**
	 * Uses a {@link HashMap} as internal {@link #map}
	 */
	public MapBuilder()
	{
	}

	/**
	 * Uses {@link Map} as internal {@link #map}
	 * 
	 * @param _mapInstance
	 */
	public MapBuilder(Map<K, V> _mapInstance)
	{
		map = _mapInstance;
	}

	/**
	 * Sets _mapInstance as internal {@link #map} and
	 * {@link #add(Object, Object)} the values
	 * 
	 * @param _mapInstance
	 * @param _key
	 * @param _value
	 */
	public MapBuilder(Map<K, V> _mapInstance, K _key, V _value)
	{
		map = _mapInstance;
		add(_key, _value);
	}

	/**
	 * Uses a {@link HashMap} as internal {@link #map} and pushes given values
	 * to {@link #map}
	 * 
	 * @param _key
	 * @param _value
	 */
	public MapBuilder(K _key, V _value)
	{
		add(_key, _value);
	}

	/**
	 * Return a new {@link MapBuilder} instance
	 * 
	 * @param <K>
	 * @param <V>
	 * @param _key
	 * @param _v
	 * @return
	 */
	public static <K, V> MapBuilder<K, V> newInstance(K _key, V _v)
	{
		return new MapBuilder<K, V>().add(_key, _v);
	}

	/**
	 * Add _key and value to {@link #map}
	 * 
	 * @param _key
	 * @param _value
	 * @return
	 */
	public MapBuilder<K, V> add(K _key, V _value)
	{
		map.put(_key, _value);

		return this;
	}

	/**
	 * Returns internal {@link #map}
	 * 
	 * @return
	 */
	public Map<K, V> map()
	{
		return map;
	}
}
