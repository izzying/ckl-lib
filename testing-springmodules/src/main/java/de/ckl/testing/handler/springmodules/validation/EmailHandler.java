package de.ckl.testing.handler.springmodules.validation;

import java.lang.reflect.Field;

import org.apache.commons.lang.RandomStringUtils;
import org.springmodules.validation.bean.conf.loader.annotation.handler.Email;

import de.ckl.testing.handler.FieldHandlerAdapter;

public class EmailHandler extends FieldHandlerAdapter {
	@Override
	public boolean supports(Field _field) {
		if (_field.isAnnotationPresent(Email.class)
				&& _field.getType().isAssignableFrom(String.class)) {
			return true;
		}

		return false;
	}

	@Override
	public Object createValue(Field _field) {
		StringBuffer sb = new StringBuffer();
		sb.append(RandomStringUtils.random(10, true, true));
		sb.append("@");
		sb.append(RandomStringUtils.random(20, true, true));
		sb.append(".");
		sb.append(RandomStringUtils.random(3, true, true));

		return sb.toString();
	}
}
