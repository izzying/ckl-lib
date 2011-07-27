package de.ckl.testing;

import java.util.List;

import de.ckl.testing.registry.ObjectRandomizerRegistry;

/**
 * Class with static methods which can be used in unit test cases for fast
 * creation of random objects
 * 
 * @author ckl
 * 
 */
public class ObjectRandomizerUtils {
	private ObjectRandomizerRegistry objectRandomizerRegistry = new ObjectRandomizerRegistry();

	/**
	 * Singleton
	 */
	private ObjectRandomizerUtils() {
	}

	private static ObjectRandomizerUtils instance = null;

	public static ObjectRandomizerUtils getInstance() {
		if (instance == null) {
			instance = new ObjectRandomizerUtils();
		}

		return instance;
	}

	/**
	 * Creates a new random instance
	 * 
	 * @param <E>
	 * @param _clazz
	 * @return
	 */
	public static <E> E factory(Class<E> _clazz) {
		return (E) getInstance().getObjectRandomizerRegistry()
				.getRandomizer(_clazz).createNewInstance();
	}

	/**
	 * Creates a number of new random instances
	 * 
	 * @param <E>
	 * @param _clazz
	 * @param _count
	 * @return
	 */
	public static <E> List<E> factory(Class<E> _clazz, int _count) {
		return (List<E>) getInstance().getObjectRandomizerRegistry()
				.getRandomizer(_clazz).factory(_count);
	}

	/**
	 * Updates a given instance with new values
	 * 
	 * @param <E>
	 * @param _object
	 * @return
	 */
	public static <E> E update(E _object) {
		return (E) getInstance().getObjectRandomizerRegistry()
				.getRandomizer(_object.getClass()).update(_object);
	}

	public ObjectRandomizerRegistry getObjectRandomizerRegistry() {
		return objectRandomizerRegistry;
	}

	public void setObjectRandomizerRegistry(
			ObjectRandomizerRegistry objectRandomizerRegistry) {
		this.objectRandomizerRegistry = objectRandomizerRegistry;
	}

}
