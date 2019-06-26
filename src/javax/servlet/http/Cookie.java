package javax.servlet.http;

public class Cookie implements Cloneable {

	private String name;
	private String value;

	public Cookie(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setValue(String newValue) {
		value = newValue;
	}


	public String getValue() {
		return value;
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
