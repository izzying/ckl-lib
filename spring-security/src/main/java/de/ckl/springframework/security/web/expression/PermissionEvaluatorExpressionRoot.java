package de.ckl.springframework.security.web.expression;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.util.IpAddressMatcher;

/**
 * Expression root for {@link PermissionEvaluator}.
 * 
 * @author ckl
 * 
 */
public class PermissionEvaluatorExpressionRoot extends SecurityExpressionRoot
{
	private PermissionEvaluator permissionEvaluator;

	public final HttpServletRequest request;

	public PermissionEvaluatorExpressionRoot(Authentication a,
			FilterInvocation fi, PermissionEvaluator permissionEvaluator)
	{
		super(a);
		this.request = fi.getRequest();
		this.permissionEvaluator = permissionEvaluator;
	}

	public boolean hasPermission(Object target, Object permission)
	{
		return permissionEvaluator.hasPermission(authentication, target,
				permission);
	}

	public boolean hasPermission(Object targetId, String targetType,
			Object permission)
	{
		return permissionEvaluator.hasPermission(authentication,
				(Serializable) targetId, targetType, permission);
	}

	public void setPermissionEvaluator(PermissionEvaluator permissionEvaluator)
	{
		this.permissionEvaluator = permissionEvaluator;
	}

	/**
	 * Takes a specific IP address or a range using the IP/Netmask (e.g.
	 * 192.168.1.0/24 or 202.24.0.0/14).
	 * 
	 * @param ipAddress
	 *            the address or range of addresses from which the request must
	 *            come.
	 * @return true if the IP address of the current request is in the required
	 *         range.
	 */
	public boolean hasIpAddress(String ipAddress)
	{
		return (new IpAddressMatcher(ipAddress).matches(request));
	}

}
