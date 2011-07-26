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

	public Object generate(Field _field, IFieldHandler _fieldHandler) {
		Object r;
		String fieldName = _field.getName();

		if (!getMapGeneratedValues().containsKey(fieldName)) {
			getMapGeneratedValues().put(fieldName, new ArrayList<Object>());
		}

		boolean fieldHandlerCanGenerateUniqueData = _fieldHandler.getClass()
				.isAnnotationPresent(CreatesUniqueValues.class);

		do {
			r = _fieldHandler.createValue(_field);
		} while (getMapGeneratedValues().get(fieldName).contains(r)
				&& fieldHandlerCanGenerateUniqueData);

		getMapGeneratedValues().get(fieldName).add(r);

		return r;
	}
}
