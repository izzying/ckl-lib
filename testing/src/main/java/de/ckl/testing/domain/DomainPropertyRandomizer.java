package de.ckl.testing.domain;

import java.lang.reflect.Field;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

/**
 * Erzeugt Zufallsdaten für die Eigenschaften von Domain-Klassen
 * 
 * @author ckl
 * 
 * @param <E>
 */
public class DomainPropertyRandomizer<E>
{
	Logger log = Logger.getLogger(DomainPropertyRandomizer.class);

	private HashMap<String, Object> mapOptions = new HashMap<String, Object>();

	private HashMap<String, Object> defaultValuesByFieldName = new HashMap<String, Object>();

	private DataGenerationDelegator<E> dataGenerationDelegator;

	private Class<E> assignedClass;

	/**
	 * @param _class
	 *            Class <strong>must</strong> have an empty constructor.
	 * @param _dataGenerationDelegator
	 *            generation delegator
	 */
	public DomainPropertyRandomizer(Class<E> _class,
			DataGenerationDelegator<E> _dataGenerationDelegator)
	{
		setAssignedClass(_class);
		setDataGenerationDelegator(_dataGenerationDelegator);
	}

	/**
	 * Creates a new list with _count random instances.
	 * 
	 * @param _clazz
	 * @param _count
	 * @return
	 */
	public List<E> factory(int _count)
	{
		List<E> r = new ArrayList<E>();

		log.info("Creating [" + _count + "] new instances of class ["
				+ getAssignedClass().getName() + "]");

		for (int i = 0; i < _count; i++)
		{
			r.add(factory());
		}

		return r;
	}

	/**
	 * Creates a new instance. If {@link Class#newInstance()} throws any
	 * execption, the exception is packed and will be thrown as
	 * {@link InvalidParameterException}
	 * 
	 * @return
	 */
	public E factory()
	{
		E instance = null;

		try
		{
			instance = getAssignedClass().newInstance();
		}
		catch (Exception e)
		{
			throw new InvalidParameterException(
					"Could not instantiate new object of class ["
							+ getAssignedClass().getName()
							+ "]. Check class for constructor without arguments");
		}

		return update(instance);

	}

	/**
	 * Creates new random data for all declared fields of class.
	 * 
	 * @param _clazz
	 * @return
	 */
	public E update(E instance)
	{
		Field[] fields = instance.getClass().getDeclaredFields();

		for (Field field : fields)
		{
			handleField(field, instance);
		}

		return instance;
	}

	/**
	 * Won't handle the field if
	 * <ul>
	 * <li>annotation {@link Id} is available</li>
	 * <li>annotation {@link Transient} is available</li>
	 * </ul>
	 * 
	 * If field name is defined in {@link #defaultValuesByFieldName}, the value
	 * from {@link #defaultValuesByFieldName} is used. Otherwise it creates a
	 * new {@link FieldCreationEnvironment} and calls
	 * {@link DataGenerationDelegator#generate(FieldCreationEnvironment)}.
	 * 
	 * @param field
	 *            field to update
	 * @param instance
	 *            instance which contains the field to update
	 */
	protected void handleField(Field field, E instance)
	{
		String fieldName = field.getName();
		Object valueToSet = null;

		if (field.isAnnotationPresent(Id.class))
		{
			log.info("Not creating data for field [" + fieldName
					+ "] because it is annotated by @Id");
			return;
		}

		if (field.isAnnotationPresent(Transient.class))
		{
			log.info("Not creating data for field [" + fieldName
					+ "] because it is annotated by @Transient");
			return;
		}

		if (getDefaultValuesByFieldName().containsKey(fieldName))
		{
			valueToSet = getDefaultValuesByFieldName().get(fieldName);
			log.info("Field [" + fieldName + "] has registered default value ["
					+ valueToSet + "]");
		}
		else
		{
			FieldCreationEnvironment fce = new FieldCreationEnvironment(field,
					null, mapOptions, 0, 0);
			valueToSet = getDataGenerationDelegator().generate(fce);
		}

		if (valueToSet == null)
		{
			log.warn("Value for [" + fieldName + "] is null");
		}
		else
		{
			try
			{
				boolean access = field.isAccessible();
				field.setAccessible(true);
				field.set(instance, valueToSet);
				field.setAccessible(access);
			}
			catch (Exception e)
			{
				log.error("Failed to set value for field [" + fieldName + "]",
						e);
			}
		}
	}

	/**
	 * Set default values for specific field
	 * 
	 * @param defaultValuesByFieldName
	 */
	public void setDefaultValuesByFieldName(
			HashMap<String, Object> defaultValuesByFieldName)
	{
		this.defaultValuesByFieldName = defaultValuesByFieldName;
	}

	/**
	 * Returns the default values for specific field
	 * 
	 * @return
	 */
	public HashMap<String, Object> getDefaultValuesByFieldName()
	{
		return defaultValuesByFieldName;
	}

	/**
	 * Sets {@link DataGenerationDelegator}
	 * 
	 * @param dataGenerationDelegator
	 */
	public void setDataGenerationDelegator(
			DataGenerationDelegator<E> dataGenerationDelegator)
	{
		this.dataGenerationDelegator = dataGenerationDelegator;
	}

	/**
	 * Get {@link DataGenerationDelegator}
	 * 
	 * @return
	 */
	public DataGenerationDelegator<E> getDataGenerationDelegator()
	{
		return dataGenerationDelegator;
	}

	/**
	 * Set assigned class
	 * 
	 * @param assignedClass
	 *            Class for which random data has to be generated
	 */
	public void setAssignedClass(Class<E> assignedClass)
	{
		this.assignedClass = assignedClass;
	}

	/**
	 * Get assigned class
	 * 
	 * @return
	 */
	public Class<E> getAssignedClass()
	{
		return assignedClass;
	}
}
