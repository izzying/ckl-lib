package de.ckl.testing.stub;

public class ObjectStub {
	private String string;
	private int integer;
	private final String finalString = "isFinal";
	private static String staticString = "isStatic";

	public String getFinalString() {
		return finalString;
	}

	public int getInteger() {
		return integer;
	}

	public void setInteger(int integer) {
		this.integer = integer;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public static String getStaticString() {
		return staticString;
	}

	public static void setStaticString(String staticString) {
		ObjectStub.staticString = staticString;
	}
}