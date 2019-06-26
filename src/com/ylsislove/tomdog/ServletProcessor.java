package com.ylsislove.tomdog;

import com.ylsislove.tomdog.connector.http.Constants;
import com.ylsislove.tomdog.connector.http.HttpRequest;
import com.ylsislove.tomdog.connector.http.HttpResponse;
import com.ylsislove.tomdog.log.Logger;
import com.ylsislove.tomdog.utils.Compiler;

import javax.servlet.Servlet;

public class ServletProcessor {

	private static Logger log = Logger.getLogger(ServletProcessor.class);
	
	public void process(HttpRequest request, HttpResponse response) {
		
		String uri = request.getRequestURI();
		String servletClass = Constants.getServletClass(uri);
		
		Class<?> myClass = Compiler.getServlet(Constants.WEB_ROOT + "/src", Constants.CLASSPATH, servletClass);
		
		Servlet servlet = null;

		try {
			servlet = (Servlet) myClass.newInstance();
			servlet.service(request, response);
			((HttpResponse) response).finishResponse();

		} catch (Exception e) {
			log.error("ServletProcessor servlet service error!");

		} catch (Throwable e) {
			log.error("ServletProcessor servlet service error!");

		}
	}
}