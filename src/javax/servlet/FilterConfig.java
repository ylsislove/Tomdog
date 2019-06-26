package javax.servlet;

import java.util.Enumeration;

public interface FilterConfig {

	public String getFilterName();

    public String getInitParameter(String name);

    public Enumeration<String> getInitParameterNames();

}
