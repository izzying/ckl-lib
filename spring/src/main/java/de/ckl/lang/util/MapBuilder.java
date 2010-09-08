package de.ckl.lang.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple fluent interface for generating maps.
 * 
 * @author ckl
 * @param <K>
 * @param <Object>
 */
public class MapBuilder<K>
{
	private Map<K, Object> map = new HashMap<K, Object>();

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
	public MapBuilder(Map<K, Object> _mapInstance)
	{
		map = _mapInstance;
	}

	/**
	 * Sets _mapInstance as internal {@link #map} and
	 * {@link #add(Object, Object)} the Objectalues
	 * 
	 * @param _mapInstance
	 * @param _key
	 * @param _Objectalue
	 */
	public MapBuilder(Map<K, Object> _mapInstance, K _key, Object _Objectalue)
	{
		map = _mapInstance;
		add(_key, _Objectalue);
	}

	/**
	 * Uses a {@link HashMap} as internal {@link #map} and pushes giObjecten
	 * Objectalues to {@link #map}
	 * 
	 * @param _key
	 * @param _Objectalue
	 */
	public MapBuilder(K _key, Object _Objectalue)
	{
		add(_key, _Objectalue);
	}

	/**
	 * Return a new {@link MapBuilder} instance
	 * 
	 * @param <K>
	 * @param <Object>
	 * @param _key
	 * @param _Object
	 * @return
	 */
	public static <K> MapBuilder<K> newInstance(K _key, Object _Object)
	{
		return new MapBuilder<K>().add(_key, _Object);
	}

	/**
	 * Add _key and Objectalue to {@link #map}
	 * 
	 * @param _key
	 * @param _Objectalue
	 * @return
	 */
	public MapBuilder<K> add(K _key, Object _Objectalue)
	{
		map.put(_key, _Objectalue);

		return this;
	}

	/**
	 * Returns internal {@link #map}
	 * 
	 * @return
	 */
	public Map<K, Object> map()
	{
		return map;
	}
}
