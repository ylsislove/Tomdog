package com.ylsislove.tomdog.connector.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

public class HttpResponseFacade implements HttpServletResponse {
	private HttpServletResponse response;

	public HttpResponseFacade(HttpResponse response) {
		this.response = response;
	}

	public void flushBuffer() throws IOException {
		response.flushBuffer();
	}

	public int getBufferSize() {
		return response.getBufferSize();
	}

	public String getCharacterEncoding() {
		return response.getCharacterEncoding();
	}

	public Locale getLocale() {
		return response.getLocale();
	}

	public ServletOutputStream getOutputStream() throws IOException {
		return response.getOutputStream();
	}

	public PrintWriter getWriter() throws IOException {
		return response.getWriter();
	}

	public boolean isCommitted() {
		return response.isCommitted();
	}

	public void reset() {
		response.reset();
	}

	public void resetBuffer() {
		response.resetBuffer();
	}

	public void setBufferSize(int size) {
		response.setBufferSize(size);
	}

	public void setContentLength(int length) {
		response.setContentLength(length);
	}

	public void setContentType(String type) {
		response.setContentType(type);
	}

	public void setLocale(Locale locale) {
		response.setLocale(locale);
	}

	public String getContentType() {
		return response.getContentType();
	}

	public void setCharacterEncoding(String charset) {
		response.setCharacterEncoding(charset);
	}
	
	public void sendRedirect(String path) {
		response.sendRedirect(path);
	}
}