package de.ckl.springframework.i18n;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;

public interface IMessageSourceContext
{

	/**
	 * Delegates to
	 * {@link MessageSource#getMessage(MessageSourceResolvable, Locale)}.
	 * 
	 * @param _resolvable
	 * @return
	 */
	public abstract String getMessage(MessageSourceResolvable _resolvable);

	/**
	 * Delegates to
	 * {@link MessageSource#getMessage(String, Object[], String, Locale)}
	 * 
	 * @param _messageKey
	 * @param params
	 * @return
	 */
	public abstract String getMessage(String _messageKey, Object... params);

	/**
	 * Delegates to {@link #getMessage(String, Object...)}. Object... is an
	 * empty array: new Object[] {}
	 * 
	 * @param _messageKey
	 * @return
	 */
	public abstract String getMessage(String _messageKey);

	/**
	 * Delegates to
	 * {@link MessageSource#getMessage(String, Object[], String, Locale)}
	 * 
	 * @param _messageKey
	 * @param _defaultMessage
	 * @param param
	 * @return
	 */
	public abstract String getMessage(String _messageKey,
			String _defaultMessage, Object... param);

}