package de.ckl.springframework.web.view;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.ModelAndView;

import de.ckl.springframework.i18n.MessageSourceContextFascade;

public class ValidatorErrorViewResolver
{
	private MessageSourceContextFascade messageSourceContextFascade;

	public ValidatorErrorViewResolver(
			MessageSourceContextFascade messageSourceContextFascade)
	{
		setMessageSourceContextFascade(messageSourceContextFascade);
	}

	class SerializedError
	{
		private String fieldName;

		private String objectName;

		private String errorCode;

		private String message;

		public void setMessage(String message)
		{
			this.message = message;
		}

		public String getMessage()
		{
			return message;
		}

		public void setErrorCode(String errorCode)
		{
			this.errorCode = errorCode;
		}

		public String getErrorCode()
		{
			return errorCode;
		}

		public void setFieldName(String fieldName)
		{
			this.fieldName = fieldName;
		}

		public String getFieldName()
		{
			return fieldName;
		}

		public void setObjectName(String objectName)
		{
			this.objectName = objectName;
		}

		public String getObjectName()
		{
			return objectName;
		}
	}

	public ModelAndView build(String modelName, BindingResult bindingResult)
	{
		List<SerializedError> r = new ArrayList<SerializedError>();

		List<FieldError> errors = bindingResult.getFieldErrors();

		for (FieldError error : errors)
		{
			SerializedError newError = new SerializedError();
			newError.setErrorCode(error.getCode());
			newError.setFieldName(error.getField());
			newError.setObjectName(error.getObjectName());
			newError.setMessage(getMessageSourceContextFascade().getMessage(
					error.getCode(), error.getArguments()));

			r.add(newError);
		}

		return new ModelAndView("", modelName, r);
	}

	public void setMessageSourceContextFascade(
			MessageSourceContextFascade messageSourceContextFascade)
	{
		this.messageSourceContextFascade = messageSourceContextFascade;
	}

	public MessageSourceContextFascade getMessageSourceContextFascade()
	{
		return messageSourceContextFascade;
	}
}
