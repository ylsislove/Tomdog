package javax.servlet.http;

//import java.util.Enumeration;

public interface HttpSession {

	public void setAttribute(String name, Object value);
	
	public Object getAttribute(String name);
	
	public String getId();
	
	//public long getCreationTime();

	//public long getLastAccessedTime();

	//public void setMaxInactiveInterval(int interval);

	//public int getMaxInactiveInterval();

	//public Object getValue(String name);

	//public Enumeration<String> getAttributeNames();

	//public String[] getValueNames();

	//public void putValue(String name, Object value);

	//public void removeAttribute(String name);

	//public void removeValue(String name);

	//public void invalidate();

	//public boolean isNew();

}
