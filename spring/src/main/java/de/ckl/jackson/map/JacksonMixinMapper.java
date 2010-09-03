package de.ckl.jackson.map;

import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * This bean can be used by Spring for auto injecting mixins for Jacksons
 * {@link ObjectMapper}.
 * 
 * <pre>
 * 	<bean id="jacksonObjectMapper" name="jacksonObjectMapper"
 * 		class="org.codehaus.jackson.map.ObjectMapper">
 * 	</bean>
 * 
 * 	<bean id="jacksonAutoInjector" class="de.ckl.jackson.map.JacksonMixinMapper"
 * 		init-method="init">
 * 		<property name="objectMapper" ref="jacksonObjectMapper" />
 * 		<property name="mixInAnnotations">
 * 				<entry key="your.class">
 * 					<value>your.mixing.class</value>
 * 				</entry>
 * 		</property>
 * </bean>
 * </pre>
 * 
 * @author ckl
 * 
 */
public class JacksonMixinMapper
{
	/**
	 * Jacksons {@link ObjectMapper}
	 */
	private ObjectMapper objectMapper;

	/**
	 * Mapping between main class (key) and mixin (value)
	 */
	private Map<Class<?>, Class<?>> sourceMixins;

	public void setMixInAnnotations(Map<Class<?>, Class<?>> sourceMixins)
	{
		this.sourceMixins = sourceMixins;
	}

	/**
	 * Set {@link ObjectMapper}
	 * 
	 * @param objectMapper
	 */
	public void setObjectMapper(ObjectMapper objectMapper)
	{
		this.objectMapper = objectMapper;
	}

	/**
	 * Get {@link ObjectMapper}
	 * 
	 * @return
	 */
	public ObjectMapper getObjectMapper()
	{
		return objectMapper;
	}

	/**
	 * Initializes the mixin annotations.<br />
	 * Sets the {@link #sourceMixins} to
	 * {@link ObjectMapper#getSerializationConfig()#setMixInAnnotations(Map)}
	 */
	public void init()
	{
		getObjectMapper().getSerializationConfig().setMixInAnnotations(
				sourceMixins);
	}
}
