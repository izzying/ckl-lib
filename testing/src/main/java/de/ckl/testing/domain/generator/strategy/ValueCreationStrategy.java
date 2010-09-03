package de.ckl.testing.domain.generator.strategy;

import de.ckl.testing.domain.FieldCreationEnvironment;

/**
 * Generates a random object of given {@link FieldCreationEnvironment}
 * 
 * @author ckl
 * 
 */
public interface ValueCreationStrategy
{
	/**
	 * Generates a random object of given {@link FieldCreationEnvironment}
	 * 
	 * @param _fieldCreationEnvironment
	 * @return
	 */
	public abstract Object generate(
			FieldCreationEnvironment _fieldCreationEnvironment);
}