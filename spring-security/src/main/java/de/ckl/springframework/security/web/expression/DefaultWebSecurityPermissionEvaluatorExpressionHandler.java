package de.ckl.springframework.security.web.expression;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

/**
 * Make the {@link PermissionEvaluator} available in JSP files
 * 
 * @author Christopher Klein
 * @since 3.0
 */
public class DefaultWebSecurityPermissionEvaluatorExpressionHandler implements
		WebSecurityPermissionEvaluatorExpressionHandler
{

	protected final Log logger = LogFactory.getLog(getClass());

	private PermissionEvaluator permissionEvaluator;

	private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

	private ExpressionParser expressionParser = new SpelExpressionParser();

	private RoleHierarchy roleHierarchy;

	public ExpressionParser getExpressionParser()
	{
		return expressionParser;
	}

	public EvaluationContext createEvaluationContext(
			Authentication authentication, FilterInvocation fi)
	{
		if (permissionEvaluator == null)
		{
			logger.warn("No permissionEvaluator given!");
		}

		StandardEvaluationContext ctx = new StandardEvaluationContext();
		SecurityExpressionRoot root = new PermissionEvaluatorExpressionRoot(
				authentication, fi, permissionEvaluator);
		root.setTrustResolver(trustResolver);
		root.setRoleHierarchy(roleHierarchy);
		ctx.setRootObject(root);

		return ctx;
	}

	public DefaultWebSecurityPermissionEvaluatorExpressionHandler()
	{
	}

	public void setPermissionEvaluator(PermissionEvaluator permissionEvaluator)
	{
		this.permissionEvaluator = permissionEvaluator;
	}

	public void setTrustResolver(AuthenticationTrustResolver trustResolver)
	{
		this.trustResolver = trustResolver;
	}

	public void setRoleHierarchy(RoleHierarchy roleHierarchy)
	{
		this.roleHierarchy = roleHierarchy;
	}

}
