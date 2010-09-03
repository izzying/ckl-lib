package de.ckl.testing.domain.generator.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.ckl.testing.domain.FieldCreationEnvironment;

/**
 * Creates a unique random object
 * 
 * @author ckl
 * 
 */
public class UniqueDataGenerationStrategy implements ValueCreationStrategy
{
	/**
	 * map contains field name as key and a list with already generated values
	 * as value
	 */
	private HashMap<String, List<Object>> mapGeneratedValues = new HashMap<String, List<Object>>();

	public void setMapGeneratedValues(
			HashMap<String, List<Object>> mapGeneratedValues)
	{
		this.mapGeneratedValues = mapGeneratedValues;
	}

	public HashMap<String, List<Object>> getMapGeneratedValues()
	{
		return mapGeneratedValues;
	}

	public Object generate(FieldCreationEnvironment _fieldCreationEnvironment)
	{
		Object r;
		String fieldName = _fieldCreationEnvironment.getField().getName();
		ValueCreationStrategy strategy = new StupidDataGenerationStrategy();

		if (!getMapGeneratedValues().containsKey(fieldName))
		{
			getMapGeneratedValues().put(fieldName, new ArrayList<Object>());
		}

		// unique data is needed...
		do
		{
			r = strategy.generate(_fieldCreationEnvironment);
		}
		while (getMapGeneratedValues().get(fieldName).contains(r));

		getMapGeneratedValues().get(fieldName).add(r);

		return r;
	}
}
