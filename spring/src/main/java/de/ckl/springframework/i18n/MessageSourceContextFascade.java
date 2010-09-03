package de.ckl.springframework.i18n;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * Returns localized Strings which are located by HTTP requested locale.
 * {@link MessageSource} is used as resolve strategy for i18n keys.
 * 
 * @author ckl
 * 
 */
public class MessageSourceContextFascade implements IMessageSourceContext
{
	private MessageSource messageSource;

	public MessageSourceContextFascade(MessageSource _messageSource)
	{
		setMessageSource(_messageSource);
	}

	public void setMessageSource(MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}

	public MessageSource getMessageSource()
	{
		return messageSource;
	}

	/**
	 * Resolves current locale from {@link HttpServletRequest}
	 * 
	 * @return
	 */
	protected Locale resolveLocale()
	{
		RequestAttributes requestAttributes = RequestContextHolder
				.getRequestAttributes();
		HttpServletRequest request = null;

		if (requestAttributes instanceof ServletRequestAttributes)
		{
			request = ((ServletRequestAttributes) requestAttributes)
					.getRequest();
		}

		return RequestContextUtils.getLocale(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ckl.springframework.i18n.IMessageSourceContext#getMessage(org.
	 * springframework.context.MessageSourceResolvable)
	 */
	@Override
	public String getMessage(MessageSourceResolvable _resolvable)
	{
		return getMessageSource().getMessage(_resolvable, resolveLocale());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.ckl.springframework.i18n.IMessageSourceContext#getMessage(java.lang
	 * .String, java.lang.Object)
	 */
	@Override
	public String getMessage(String _messageKey, Object... params)
	{
		return getMessageSource().getMessage(_messageKey, params,
				resolveLocale());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.ckl.springframework.i18n.IMessageSourceContext#getMessage(java.lang
	 * .String)
	 */
	@Override
	public String getMessage(String _messageKey)
	{
		return getMessage(_messageKey, new Object[] {});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.ckl.springframework.i18n.IMessageSourceContext#getMessage(java.lang
	 * .String, java.lang.String, java.lang.Object)
	 */
	@Override
	public String getMessage(String _messageKey, String _defaultMessage,
			Object... param)
	{
		return getMessageSource().getMessage(_messageKey, param,
				_defaultMessage, resolveLocale());
	}
}
