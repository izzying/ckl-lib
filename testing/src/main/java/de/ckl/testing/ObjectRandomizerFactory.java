package de.ckl.testing;

/**
 * Interface for creating new {@link ObjectRandomizer} objects
 * 
 * @author ckl
 * 
 */
public interface ObjectRandomizerFactory {
	/**
	 * Create a new {@link ObjectRandomizer}.
	 * 
	 * @param _clazz
	 * @return Must not be null
	 */
	public ObjectRandomizer create(Class _clazz);
}
