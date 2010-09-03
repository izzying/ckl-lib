package de.ckl.testing.domain.generator.strategy;

import de.ckl.testing.domain.FieldCreationEnvironment;
import de.ckl.testing.domain.generator.interfaces.UniqueValueGenerator;
import de.ckl.testing.domain.generator.interfaces.ValueGenerator;

/**
 * Delegates to available generators
 * 
 * @author ckl
 * 
 */
public class ValueCreationDelegator implements ValueCreationStrategy
{

	@Override
	public Object generate(FieldCreationEnvironment _fieldCreationEnvironment)
	{
		ValueGenerator generator = _fieldCreationEnvironment.getGenerator();
		ValueCreationStrategy strategy = null;

		if (generator instanceof UniqueValueGenerator)
		{
			strategy = new UniqueDataGenerationStrategy();
		}
		else
		{
			strategy = new StupidDataGenerationStrategy();
		}

		return strategy.generate(_fieldCreationEnvironment);
	}
}
