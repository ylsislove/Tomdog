package com.ylsislove.tomdog.connector.http;

import java.util.HashMap;
import java.util.Map;

import com.ylsislove.tomdog.FilterProcessor;
import com.ylsislove.tomdog.JspProcessor;
import com.ylsislove.tomdog.ServletProcessor;

public class Manager {

	private static Map<String, HttpSessionImpl> HttpSessionMap = new HashMap<String, HttpSessionImpl>();
	private static Map<String, ServletProcessor> ServletProcessorMap = new HashMap<String, ServletProcessor>();
	private static Map<String, JspProcessor> JspProcessorMap = new HashMap<String, JspProcessor>();
	
	private static FilterProcessor filter = null;
	
	public static FilterProcessor getFilter() {
		if (filter == null) {
			filter = new FilterProcessor();
		}
		return filter;
	}
	
	public static JspProcessor getJspProcessor(String uri) {
		JspProcessor processor = JspProcessorMap.get(uri);
		if (processor == null) {
			processor = new JspProcessor();
			JspProcessorMap.put(uri, processor);
			return processor;
			
		} else {
			return processor;
		}
	}
	
	public static ServletProcessor getServletProcessor(String servletName) {
		ServletProcessor processor = ServletProcessorMap.get(servletName);
		if (processor == null) {
			processor = new ServletProcessor();
			ServletProcessorMap.put(servletName, processor);
			return processor;
			
		} else {
			return processor;
		}
	}
	
	public static HttpSessionImpl getSession(String id) {
		return HttpSessionMap.get(id);
	}
	
	public static void setSession(String id) {
		if (HttpSessionMap.containsKey(id)) {
			return;
		} else {
			HttpSessionMap.put(id, new HttpSessionImpl(id));
		}
	}
}
