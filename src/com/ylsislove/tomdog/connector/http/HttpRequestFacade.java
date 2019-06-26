package com.ylsislove.tomdog.connector.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;

public class HttpRequestFacade implements HttpServletRequest {

	private HttpServletRequest request;

	public HttpRequestFacade(HttpRequest request) {
		this.request = request;
	}

	/* implementation of the HttpServletRequest */
	public Object getAttribute(String name) {
		return request.getAttribute(name);
	}

	public Enumeration<String> getAttributeNames() {
		return request.getAttributeNames();
	}

	public String getCharacterEncoding() {
		return request.getCharacterEncoding();
	}

	public int getContentLength() {
		return request.getContentLength();
	}

	public String getContentType() {
		return request.getContentType();
	}

	public ServletInputStream getInputStream() throws IOException {
		return request.getInputStream();
	}

	public String getMethod() {
		return request.getMethod();
	}

	public String getParameter(String name) {
		return request.getParameter(name);
	}

	public Map<String, String[]> getParameterMap() {
		return request.getParameterMap();
	}

	public Enumeration<String> getParameterNames() {
		return request.getParameterNames();
	}

	public String[] getParameterValues(String name) {
		return request.getParameterValues(name);
	}

	public String getProtocol() {
		return request.getProtocol();
	}

	public String getQueryString() {
		return request.getQueryString();
	}

	public BufferedReader getReader() throws IOException {
		return request.getReader();
	}

	public RequestDispatcher getRequestDispatcher(String path) {
		return request.getRequestDispatcher(path);
	}

	public String getRequestURI() {
		return request.getRequestURI();
	}

	public void removeAttribute(String attribute) {
		request.removeAttribute(attribute);
	}

	public void setAttribute(String key, Object value) {
		request.setAttribute(key, value);
	}

	public void setCharacterEncoding(String encoding) {
		request.setCharacterEncoding(encoding);
	}

	public void setProtocol(String protocol) {
		request.setProtocol(protocol);
	}

	public String getServletPath() {
		return request.getServletPath();
	}


	@Override
	public String getHeader(String name) {
		return request.getHeader(name);
	}

	@Override
	public Enumeration<String> getHeaderNames() {
		return request.getHeaderNames();
	}

	@Override
	public Enumeration<String> getHeaders(String name) {
		return request.getHeaders(name);
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		return request.isRequestedSessionIdFromCookie();
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		return request.isRequestedSessionIdFromURL();
	}


	@Override
	public boolean isRequestedSessionIdValid() {
		return request.isRequestedSessionIdValid();
	}

	@Override
	public String getRequestedSessionId() {
		return request.getRequestedSessionId();
	}

	@Override
	public HttpSession getSession(boolean create) {
		return request.getSession(create);
	}

	@Override
	public HttpSession getSession() {
		return request.getSession();
	}

}