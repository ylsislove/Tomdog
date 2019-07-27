package com.ylsislove.tomdog;

import com.ylsislove.tomdog.connector.http.Constants;
import com.ylsislove.tomdog.connector.http.HttpRequest;
import com.ylsislove.tomdog.connector.http.HttpResponse;
import com.ylsislove.tomdog.log.Logger;
import com.ylsislove.tomdog.utils.Compiler;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

public class ServletProcessor {

	private static Logger log = Logger.getLogger(ServletProcessor.class);
	
	private Servlet servlet = null;
	private boolean parsed = false;
	
	public void process(HttpRequest request, HttpResponse response) {
		
		if (parsed) {
			try {
				servlet.service(request, response);
				((HttpResponse) response).finishResponse();
				return;
				
			} catch (ServletException | IOException e) {
				log.error("ServletProcessor parsed error!");
			}
		}
		
		String uri = request.getRequestURI();
		String servletClass = Constants.getServletClass(uri);
		
		Class<?> myClass = Compiler.getServlet(Constants.WEB_ROOT + "/src", Constants.CLASSPATH, servletClass);

		try {
			servlet = (Servlet) myClass.newInstance();
			servlet.init();
			servlet.service(request, response);
			((HttpResponse) response).finishResponse();

		} catch (Exception e) {
			log.error("ServletProcessor servlet service error!");
		} catch (Throwable e) {
			log.error("ServletProcessor servlet service error!");
		}
		
		parsed = true;
	}
}