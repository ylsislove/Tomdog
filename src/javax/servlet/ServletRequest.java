package javax.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

public interface ServletRequest {

	
    public Object getAttribute(String name);
    
    public Enumeration<String> getAttributeNames();
    
    public void setAttribute(String name, Object o);

    public void removeAttribute(String name);
    
    
    
    public String getParameter(String name);
    
    public Enumeration<String> getParameterNames();
    
    public String[] getParameterValues(String name);
    
    public Map<String, String[]> getParameterMap();
    
    
    
    public int getContentLength();
    
    public String getContentType();
    
    public String getProtocol();
    
    
    public String getCharacterEncoding();

    public void setCharacterEncoding(String env);

    public ServletInputStream getInputStream() throws IOException;     

    public BufferedReader getReader() throws IOException;

    public RequestDispatcher getRequestDispatcher(String path);
}

