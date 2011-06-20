package de.ckl.springframework.orm.jpa.test;

import java.security.InvalidParameterException;

public interface IdResolver
{

	/**
	 * Resolves entity Id
	 * 
	 * @param _instance
	 * @return
	 * @throws InvalidParameterException
	 */
	public long resolveId(Object _instance) throws InvalidParameterException;
}
