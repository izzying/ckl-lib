package de.ckl.testing.generator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.ckl.testing.handler.IFieldHandler;
import de.ckl.testing.handler.annotation.CreatesUniqueValues;

/**
 * Creates a field value
 * 
 * @author ckl
 * 
 */
public class DataGeneratorDelegator {
	/**
	 * map contains field name as key and a list with already generated values
	 * as value
	 */
	private HashMap<String, List<Object>> mapGeneratedValues = new HashMap<String, List<Object>>();

	public void setMapGeneratedValues(
			HashMap<String, List<Object>> mapGeneratedValues) {
		this.mapGeneratedValues = mapGeneratedValues;
	}

	public HashMap<String, List<Object>> getMapGeneratedValues() {
		return mapGeneratedValues;
	}

	/**
	 * Generates a new value for given field. If _fieldHandler is annotated by
	 * {@link CreatesUniqueValues} a new value is created till it is unique.
	 * 
	 * @param _field
	 * @param _fieldHandler
	 * @return
	 */
	public Object generate(Field _field, IFieldHandler _fieldHandler) {
		Object r;
		String fieldName = _field.getName();

		// unique cache
		if (!getMapGeneratedValues().containsKey(fieldName)) {
			getMapGeneratedValues().put(fieldName, new ArrayList<Object>());
		}

		// support for unique values - a BooleanHandler should not have a CreateUniqueValues annotation ;-)
		boolean fieldHandlerCanGenerateUniqueData = _fieldHandler.getClass()
				.isAnnotationPresent(CreatesUniqueValues.class);

		// create a real unique value
		do {
			r = _fieldHandler.createValue(_field);
		} while (getMapGeneratedValues().get(fieldName).contains(r)
				&& fieldHandlerCanGenerateUniqueData);

		// add to cache
		getMapGeneratedValues().get(fieldName).add(r);

		return r;
	}
}
