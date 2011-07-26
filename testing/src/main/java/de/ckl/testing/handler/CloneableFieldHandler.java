package de.ckl.testing.handler;

/**
 * Yeah... Java clone hell.
 * 
 * @author ckl
 * 
 */
public class CloneableFieldHandler implements Cloneable {
	public Object clone() throws CloneNotSupportedException {
		CloneableFieldHandler copy = (CloneableFieldHandler) super.clone();

		return copy;
	}
}
