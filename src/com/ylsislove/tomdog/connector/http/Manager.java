package com.ylsislove.tomdog.connector.http;

import java.util.HashMap;
import java.util.Map;

import com.ylsislove.tomdog.FilterProcessor;

public class Manager {

	private static Map<String, HttpSessionImpl> map = new HashMap<String, HttpSessionImpl>();
	
	private static FilterProcessor filter = null;
	
	public static FilterProcessor getFilter() {
		if (filter == null) {
			filter = new FilterProcessor();
		}
		return filter;
	}
	
	public static HttpSessionImpl getSession(String id) {
		return map.get(id);
	}
	
	public static void setSession(String id) {
		if (map.containsKey(id)) {
			return;
		} else {
			map.put(id, new HttpSessionImpl(id));
		}
	}
}
