package com.ylsislove.tomdog.connector.http;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

public class HttpSessionImpl implements HttpSession {

	private Map<String, Object> map = new HashMap<String, Object>();
	
	private String sessionId;
	
	public HttpSessionImpl(String id) {
		this.sessionId = id;
	}
	
	@Override
	public void setAttribute(String name, Object value) {
		map.put(name, value);
	}

	@Override
	public Object getAttribute(String name) {
		return map.get(name);
	}

	@Override
	public String getId() {
		return sessionId;
	}

}
