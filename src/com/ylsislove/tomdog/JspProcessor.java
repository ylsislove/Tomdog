package com.ylsislove.tomdog;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import com.ylsislove.tomdog.connector.http.HttpRequest;
import com.ylsislove.tomdog.connector.http.HttpRequestFacade;
import com.ylsislove.tomdog.connector.http.HttpResponse;
import com.ylsislove.tomdog.connector.http.HttpResponseFacade;
import com.ylsislove.tomdog.log.Logger;
import com.ylsislove.tomdog.utils.ParseJSP;

public class JspProcessor {

	private static Logger log = Logger.getLogger(JspProcessor.class);
	
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
		
		Class<?> myClass = ParseJSP.parse(request.getRequestURI());
		try {
			servlet = (Servlet) myClass.newInstance();
			HttpRequestFacade requestFacade = new HttpRequestFacade(request);
			HttpResponseFacade responseFacade = new HttpResponseFacade(response);
			servlet.init();
			servlet.service(requestFacade, responseFacade);
			((HttpResponse) response).finishResponse();
			
		} catch (Exception e) {
			log.error("JspProcessor servlet service error!");
		} catch (Throwable e) {
			log.error("JspProcessor servlet service error!");
		}
		
		parsed = true;
		
	}
}
