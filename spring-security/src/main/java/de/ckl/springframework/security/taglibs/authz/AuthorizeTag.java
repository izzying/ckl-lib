package de.ckl.springframework.security.taglibs.authz;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.springframework.context.ApplicationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ParseException;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.taglibs.authz.LegacyAuthorizeTag;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.WebInvocationPrivilegeEvaluator;
import org.springframework.security.web.access.expression.WebSecurityExpressionHandler;
import org.springframework.web.context.support.WebApplicationContextUtils;

import de.ckl.springframework.security.web.expression.WebSecurityPermissionEvaluatorExpressionHandler;

/**
 * Access control tag which evaluates its body based either on
 * <ul>
 * <li>an access expression (the "access" attribute), or</li>
 * <li>by evaluating the current user's right to access a particular URL (set
 * using the "url" attribute).</li>
 * </ul>
 * In difference to Spring Securites
 * {@link org.springframework.security.taglibs.authz.AuthorizeTag} authorize
 * tag, this tag handler can evaluate JSP variables. <br />
 * 
 * Every JSP-variable in current context will be injected by name into the SpEl
 * context.
 * 
 * The following JSP code will work in <strong>this</strong> tag handler but not
 * in Spring Securities.
 * 
 * <pre>
 * 	<c:forEach var="item" items="${items}">
 *    <sec-ckl authorize access="hasPermission(#item, 'any_permission')" />
 *  </c:forEach>
 * </pre>
 * 
 * @author Christopher Klein
 * @author Luke Taylor
 * @since 3.0
 */
public class AuthorizeTag extends LegacyAuthorizeTag
{
	private static final long serialVersionUID = -4898201584180807446L;

	private String access;

	private String url;

	private String method;

	private String var;

	// If access expression evaluates to "true" return
	public int doStartTag() throws JspException
	{
		Authentication currentUser = SecurityContextHolder.getContext()
				.getAuthentication();

		if (currentUser == null)
		{
			return SKIP_BODY;
		}

		int result;

		if (access != null && access.length() > 0)
		{
			result = authorizeUsingAccessExpression(currentUser);
		}
		else if (url != null && url.length() > 0)
		{
			result = authorizeUsingUrlCheck(currentUser);
		}
		else
		{
			result = super.doStartTag();
		}

		if (var != null)
		{
			pageContext.setAttribute(var,
					Boolean.valueOf(result == EVAL_BODY_INCLUDE),
					PageContext.PAGE_SCOPE);
		}

		return result;
	}

	public void setVar(String var)
	{
		this.var = var;
	}

	@SuppressWarnings("unchecked")
	private int authorizeUsingAccessExpression(Authentication currentUser)
			throws JspException
	{
		// Get web expression
		WebSecurityExpressionHandler handler = getExpressionHandler();

		Expression accessExpression;

		try
		{
			accessExpression = handler.getExpressionParser().parseExpression(
					access);

		}
		catch (ParseException e)
		{
			throw new JspException(e);
		}

		FilterInvocation f = new FilterInvocation(pageContext.getRequest(),
				pageContext.getResponse(), DUMMY_CHAIN);
		EvaluationContext ec = handler.createEvaluationContext(currentUser, f);

		String varName = "";
		boolean inVarName = false;
		char c = '0';

		// iterate over every defined var reference.
		// Expression "#varName ... #varName2" will be evaluated so that
		// pageContext.varName and pageContext.varName2 are injected into Spring
		// Expression Language context.
		for (int i = 0, m = access.length(); i < m; i++)
		{
			c = access.charAt(i);

			if (inVarName)
			{
				if ((c >= 65 && c <= 90) || (c >= 97 && c <= 122)
						|| (c >= 48 && c <= 57) || (c == 45) || (c == 95))
				{
					varName += c;
				}
				else
				{
					// lookup object and inject
					Object pageObject = pageContext.getAttribute(varName);
					
					if (pageObject != null)
					{
						ec.setVariable(varName, pageObject);
					}
					
					varName = "";
					inVarName = false;
				}
			}

			if (c == '#')
			{
				inVarName = true;
			}
		}

		if (ExpressionUtils.evaluateAsBoolean(accessExpression, ec))
		{
			return EVAL_BODY_INCLUDE;
		}

		return SKIP_BODY;
	}

	private int authorizeUsingUrlCheck(Authentication currentUser)
			throws JspException
	{
		return getPrivilegeEvaluator()
				.isAllowed(
						((HttpServletRequest) pageContext.getRequest())
								.getContextPath(),
						url, method, currentUser) ? EVAL_BODY_INCLUDE
				: SKIP_BODY;
	}

	public void setAccess(String access)
	{
		this.access = access;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public void setMethod(String method)
	{
		this.method = method;
	}

	WebSecurityExpressionHandler getExpressionHandler() throws JspException
	{
		ServletContext servletContext = pageContext.getServletContext();
		ApplicationContext ctx = WebApplicationContextUtils
				.getRequiredWebApplicationContext(servletContext);
		Map<String, WebSecurityPermissionEvaluatorExpressionHandler> expressionHdlrs = ctx
				.getBeansOfType(WebSecurityPermissionEvaluatorExpressionHandler.class);

		if (expressionHdlrs.size() == 0)
		{
			throw new JspException(
					"No visible WebSecurityExpressionHandler instance could be found in the application "
							+ "context. There must be at least one in order to support expressions in JSP 'authorize' tags.");
		}

		return (WebSecurityExpressionHandler) expressionHdlrs.values()
				.toArray()[0];
	}

	WebInvocationPrivilegeEvaluator getPrivilegeEvaluator() throws JspException
	{
		ServletContext servletContext = pageContext.getServletContext();
		ApplicationContext ctx = WebApplicationContextUtils
				.getRequiredWebApplicationContext(servletContext);
		Map<String, WebInvocationPrivilegeEvaluator> wipes = ctx
				.getBeansOfType(WebInvocationPrivilegeEvaluator.class);

		if (wipes.size() == 0)
		{
			throw new JspException(
					"No visible WebInvocationPrivilegeEvaluator instance could be found in the application "
							+ "context. There must be at least one in order to support the use of URL access checks in 'authorize' tags.");
		}

		return (WebInvocationPrivilegeEvaluator) wipes.values().toArray()[0];
	}

	private static final FilterChain DUMMY_CHAIN = new FilterChain() {
		public void doFilter(ServletRequest request, ServletResponse response)
				throws IOException, ServletException
		{
			throw new UnsupportedOperationException();
		}
	};
}
