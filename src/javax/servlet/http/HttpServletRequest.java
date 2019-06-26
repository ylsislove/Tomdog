package javax.servlet.http;

import java.util.Enumeration;

import javax.servlet.ServletRequest;


public interface HttpServletRequest extends ServletRequest {
	
    public String getMethod();

    public String getQueryString();

    public String getRequestURI();

	public String getServletPath();
	
	public void setProtocol(String protocol);
	
	public String getHeader(String name);
	
	public Enumeration<String> getHeaderNames();
	
	public Enumeration<String> getHeaders(String name); 
	
    public boolean isRequestedSessionIdFromCookie();
    
    public boolean isRequestedSessionIdFromURL();
    
    public boolean isRequestedSessionIdValid();
    
    public String getRequestedSessionId();
    
    public HttpSession getSession(boolean create);

    public HttpSession getSession();
}
