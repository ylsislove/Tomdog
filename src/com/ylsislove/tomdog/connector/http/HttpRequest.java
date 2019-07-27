package com.ylsislove.tomdog.connector.http;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import com.ylsislove.tomdog.connector.RequestStream;
import com.ylsislove.tomdog.log.Logger;
import com.ylsislove.tomdog.utils.Enumerator;

public class HttpRequest implements HttpServletRequest {

	private static Logger log = Logger.getLogger(HttpRequest.class);
	
	private String characterEncoding;
	private String contentType;
	private int contentLength;
	private InputStream input;
	private String method;
	private String protocol;
	private String queryString;
	private String requestURI;
	
	private boolean requestedSessionCookie;
	private String requestedSessionId;
	private boolean requestedSessionURL;

	protected ArrayList<Cookie> cookies = new ArrayList<Cookie>();
	protected Map<String, String[]> parameters = new HashMap<String, String[]>();
	protected Map<String, ArrayList<String>> headers = new HashMap<String, ArrayList<String>>();
	protected Map<String, Object> attributes = new HashMap<String, Object>();
	protected String contextPath = "";
	protected boolean parsed = false;

	protected BufferedReader reader = null;
	protected ServletInputStream stream = null;

	
	public HttpRequest(InputStream input) {
		this.input = input;
	}

	public void addHeader(String name, String value) {
		name = name.toLowerCase();
		synchronized (headers) {
			ArrayList<String> values = (ArrayList<String>) headers.get(name);
			if (values == null) {
				values = new ArrayList<String>();
				headers.put(name, values);
			}
			values.add(value);
		}
	}


	protected void parseParameters() {
		if (parsed)
			return;

		String queryString = getQueryString();
		if (queryString != null) {
			parseParameters(queryString);
		}
		
		parsed = true;
	}

	public void parseParameters(String str) {
		String[] splits = null;
		int pos= str.indexOf("&");
		// 如果有多个请求参数
		if (pos != -1) {
			splits = str.split("&");
			
		} else {
			splits = new String[] {str};
		}
		
		for (int i = 0; i < splits.length; i++) {
			pos = splits[i].indexOf("=");
			String name = splits[i].substring(0, pos);
			String value = splits[i].substring(pos+1);
			
			if (parameters.containsKey(name)) {
				String[] values = parameters.get(name);
				values[0] = value;
				
			} else {
				String[] values = new String[] {value};
				parameters.put(name, values);
			}
		}
	}
	
	public void addCookie(Cookie cookie) {
	    synchronized (cookies) {
	    	cookies.add(cookie);
	    }
	}
	
	public ArrayList<Cookie> getCookie() {
		return cookies;
	}

	public ServletInputStream createInputStream() throws IOException {
		return (new RequestStream(this));
	}

	public InputStream getStream() {
		return input;
	}

	public void setContentLength(int length) {
		this.contentLength = length;
	}

	public void setContentType(String type) {
		this.contentType = type;
	}


	public void setContextPath(String path) {
		if (path == null)
			this.contextPath = "";
		else
			this.contextPath = path;
	}

	public void setMethod(String method) {
		this.method = method;
	}


	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public void setRequestURI(String requestURI) {
		this.requestURI = requestURI;
	}

	public Object getAttribute(String name) {
		synchronized (attributes) {
			return (attributes.get(name));
		}
	}

	public Enumeration<String> getAttributeNames() {
		return null;
	}


	public String getCharacterEncoding() {
		return characterEncoding;
	}

	public int getContentLength() {
		return contentLength;
	}

	public String getContentType() {
		return contentType;
	}

	public String getContextPath() {
		return contextPath;
	}

	public String getHeader(String name) {
		name = name.toLowerCase();
		synchronized (headers) {
			ArrayList<String> values = (ArrayList<String>) headers.get(name);
			if (values != null)
				return ((String) values.get(0));
			else
				return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Enumeration<String> getHeaderNames() {
	    synchronized (headers) {
	    	return (new Enumerator(headers.keySet()));
	    }
	}
	
	@SuppressWarnings("unchecked")
	public Enumeration<String> getHeaders(String name) {
	    name = name.toLowerCase();
	    synchronized (headers) {
	      ArrayList<String> values = (ArrayList<String>) headers.get(name);
	      if (values != null)
	        return (new Enumerator(values));
	      else
	        return (new Enumerator(new ArrayList<String>()));
	    }
	}

	public String getMethod() {
		return method;
	}

	public String getParameter(String name) {
		parseParameters();
		String values[] = (String[]) parameters.get(name);
		if (values != null)
			return (values[0]);
		else
			return (null);
	}

	public Map<String, String[]> getParameterMap() {
		parseParameters();
		return (this.parameters);
	}

	public Enumeration<String> getParameterNames() {
		parseParameters();
		return null;
	}

	public String[] getParameterValues(String name) {
		parseParameters();
		String values[] = (String[]) parameters.get(name);
		if (values != null)
			return (values);
		else
			return null;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getQueryString() {
		return queryString;
	}

	public RequestDispatcher getRequestDispatcher(String path) {
		return new HttpRequestDispatcher(path);
	}

	public String getRequestURI() {
		return requestURI;
	}
	
	public void removeAttribute(String attribute) {
	}

	public void setAttribute(String key, Object value) {
		attributes.put(key, value);
	}

	public void setCharacterEncoding(String encoding) {
		characterEncoding = encoding;
	}

	public ServletInputStream getInputStream() throws IOException {
		if (reader != null)
			log.error("getInputStream has been called");

		if (stream == null)
			stream = createInputStream();
		return (stream);
	}

	public BufferedReader getReader() throws IOException {
		if (stream != null)
			log.error("getInputStream has been called");
		
		if (reader == null) {
			String encoding = getCharacterEncoding();
			if (encoding == null)
				encoding = "UTF-8";
			InputStreamReader isr = new InputStreamReader(createInputStream(), encoding);
			reader = new BufferedReader(isr);
		}
		return (reader);
	}

	public String getServletPath() {
		return null;
	}

	public boolean isRequestedSessionIdFromCookie() {
		return requestedSessionCookie;
	}

	public void setRequestedSessionId(String value) {
		requestedSessionId = value;
	}

	public void setRequestedSessionCookie(boolean b) {
		requestedSessionCookie = b;
	}

	public void setRequestedSessionURL(boolean b) {
		requestedSessionURL = b;
	}

	public boolean isRequestedSessionIdFromURL() {
		return requestedSessionURL;
	}

	public boolean isRequestedSessionIdValid() {
		return false;
	}

	public String getRequestedSessionId() {
		return requestedSessionId;
	}

	public HttpSession getSession(boolean create) {
		return null;
	}

	public HttpSession getSession() {
		return Manager.getSession(requestedSessionId);
	}

}
