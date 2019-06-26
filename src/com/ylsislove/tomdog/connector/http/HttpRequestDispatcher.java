package com.ylsislove.tomdog.connector.http;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.ylsislove.tomdog.JspProcessor;

public class HttpRequestDispatcher implements RequestDispatcher {

	private String path;
	
	public HttpRequestDispatcher(String path) {
		this.path = path;
	}
	
	@Override
	public void forward(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		HttpRequest req = (HttpRequest)request;
		HttpResponse res = (HttpResponse)response;
		// 如果要转发到jsp页面
		if (path.endsWith(".jsp")) {
			if (!path.startsWith("/")) {
				path = "/"+path;
			}
			req.setRequestURI(path);
			
			JspProcessor processor = new JspProcessor();
			processor.process(req, res);
		}
	}
	
}
