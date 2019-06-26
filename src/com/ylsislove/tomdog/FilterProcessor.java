package com.ylsislove.tomdog;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChainWrapper;
import javax.servlet.ServletException;

import com.ylsislove.tomdog.connector.http.Constants;
import com.ylsislove.tomdog.connector.http.HttpRequest;
import com.ylsislove.tomdog.connector.http.HttpResponse;
import com.ylsislove.tomdog.log.Logger;
import com.ylsislove.tomdog.utils.Compiler;

public class FilterProcessor {

	private static Logger log = Logger.getLogger(FilterProcessor.class);
	
	private Filter filter = null;
	private FilterChainWrapper chain = new FilterChainWrapper();
	private boolean parsed = false;
	
	public void process(HttpRequest request, HttpResponse response, String filterClass) {
		try {
			if (parsed) {
				filter.doFilter(request, response, chain);
				return;
			}
			
		} catch (IOException | ServletException e) {
			log.error("FilterProcessor parsed error!");
		}
			
		Class<?> myClass = Compiler.getServlet(Constants.WEB_ROOT + "/src", Constants.CLASSPATH, filterClass);

		try {
			filter = (Filter) myClass.newInstance();
			filter.doFilter(request, response, chain);
			
		} catch (Throwable e) {
			log.error("FilterProcessor doFilter error!");
		}
		
		parsed = true;
	}
}
