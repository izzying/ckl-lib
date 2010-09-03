package de.ckl.testing.domain.generator.strategy;

import de.ckl.testing.domain.FieldCreationEnvironment;

/**
 * Simple strategy of which creates random data. No more, no less.
 * 
 * @author ckl
 */
public class StupidDataGenerationStrategy implements ValueCreationStrategy
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.ckl.domain.test.generator.strategy.IValueCreator#generate(de.ckl.domain
	 * .test.generator.FieldCreationEnvironment)
	 */
	@Override
	public Object generate(FieldCreationEnvironment _fieldCreationEnvironment)
	{
		return _fieldCreationEnvironment.getGenerator().generate(
				_fieldCreationEnvironment.getField(),
				_fieldCreationEnvironment.getMapOptions(),
				_fieldCreationEnvironment.getMinLength(),
				_fieldCreationEnvironment.getMaxLength());
	}

}
