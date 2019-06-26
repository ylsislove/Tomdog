package com.ylsislove.tomdog;

import javax.servlet.Servlet;
import com.ylsislove.tomdog.connector.http.HttpRequest;
import com.ylsislove.tomdog.connector.http.HttpRequestFacade;
import com.ylsislove.tomdog.connector.http.HttpResponse;
import com.ylsislove.tomdog.connector.http.HttpResponseFacade;
import com.ylsislove.tomdog.log.Logger;
import com.ylsislove.tomdog.utils.ParseJSP;

public class JspProcessor {

	private static Logger log = Logger.getLogger(JspProcessor.class);
	
	public void process(HttpRequest request, HttpResponse response) {

		Class<?> myClass = ParseJSP.parse(request.getRequestURI());
		Servlet servlet = null;
		try {
			servlet = (Servlet) myClass.newInstance();
			HttpRequestFacade requestFacade = new HttpRequestFacade(request);
			HttpResponseFacade responseFacade = new HttpResponseFacade(response);
			servlet.service(requestFacade, responseFacade);
			
			((HttpResponse) response).finishResponse();
			
		} catch (Exception e) {
			log.error("JspProcessor servlet service error!");
		} catch (Throwable e) {
			log.error("JspProcessor servlet service error!");
		}
	}
	
}
