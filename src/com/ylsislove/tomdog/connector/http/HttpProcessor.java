package com.ylsislove.tomdog.connector.http;

import com.ylsislove.tomdog.JspProcessor;
import com.ylsislove.tomdog.ServletProcessor;
import com.ylsislove.tomdog.StaticResourceProcessor;

import java.net.Socket;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import com.ylsislove.tomdog.connector.http.enumeration.Method;
import com.ylsislove.tomdog.log.Logger;
import com.ylsislove.tomdog.utils.RequestUtil;

public class HttpProcessor implements Runnable {

	private static Logger log = Logger.getLogger(HttpProcessor.class);

	public HttpProcessor(Socket client) {
		this.client = client;
	}

	private Socket client;
	private HttpRequest request;
	private HttpResponse response;

	private Method method;
	private String uri;
	private String protocol;

	@Override
	public void run() {
		process(client);
	}

	public void process(Socket socket) {
		BufferedReader reader = null;
		OutputStream output = null;
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = socket.getOutputStream();

			request = new HttpRequest(socket.getInputStream());
			response = new HttpResponse(output);
			response.setRequest(request);

			// 解析request请求
			String str = reader.readLine();
			parseRequestLine(str);
			while (str != null && !str.equals("")) {
				str = reader.readLine();
				parseRequestHeader(str);
			}
			
			// 创建Session
			if (request.getRequestedSessionId() != null) {
				Manager.setSession(request.getRequestedSessionId());
			} else {
				// 如果没有JsessionId的话，就把cookie设置为sessionId
				String name = request.getCookie().get(0).getName();
				String value = request.getCookie().get(0).getValue();
				if (!name.equals("") && !value.equals("")) {
					Manager.setSession(value);
					request.setRequestedSessionId(value);
				} else {
					log.error("do not found cookie!");
				}
			}

			// 解析Body
			StringBuffer sb = new StringBuffer();
			if ("POST".equals(request.getMethod()) && (request.getContentLength() > 0)
					&& "application/x-www-form-urlencoded".equals(request.getContentType())) {

				int i = 0;
				while (reader.ready() && (i = reader.read()) != -1) {
					sb.append((char) i);
				}
				log.info("BODY: " + sb.toString());
				request.parseParameters(sb.toString());
			}

			// 如果Filter要求过滤该请求
			if (!uri.endsWith(".ico")) {
				Manager.getFilter().process(request, response, "course.AccessFilter");
			}

			// 这是一个jsp请求
			if (uri.endsWith(".jsp")) {
				JspProcessor processor = new JspProcessor();
				processor.process(request, response);
			}
			// 这是一个servlet请求
			else if (Constants.isServletUrl(request.getRequestURI())) {
				ServletProcessor processor = new ServletProcessor();
				processor.process(request, response);
			}
			// 这是一个静态资源的请求
			else {
				StaticResourceProcessor processor = new StaticResourceProcessor();
				processor.process(request, response);
			}

			// 关闭套接字
			socket.close();

		} catch (Exception e) {
			;
		}
	}

	private void parseRequestHeader(String str) throws IOException, ServletException {
		int pos = str.indexOf(":");
		if (pos != -1) {
			String name = str.substring(0, pos).trim();
			String value = str.substring(pos + 1).trim();
			request.addHeader(name, value);

			if (name.equals("Cookie")) {
				Cookie cookies[] = RequestUtil.parseCookieHeader(value);
				for (int i = 0; i < cookies.length; i++) {
					if (cookies[i].getName().equals("JSESSIONID")) {
						// 覆盖在URL里得到的SessionID
						if (!request.isRequestedSessionIdFromCookie()) {
							request.setRequestedSessionId(cookies[i].getValue());
							request.setRequestedSessionCookie(true);
							request.setRequestedSessionURL(false);
						}
					}
					request.addCookie(cookies[i]);
				}
			} else if (name.equals("Content-Length")) {
				int n = -1;
				try {
					n = Integer.parseInt(value);
				} catch (Exception e) {
					log.error("httpProcessor.parseHeaders.contentLength parse error!");
				}
				request.setContentLength(n);

			} else if (name.equals("Content-Type")) {
				request.setContentType(value);
			}
		}
	}

	private void parseRequestLine(String line) throws IOException, ServletException {
		if (line != null) {
			String[] split = line.split(" ");
			try {
				method = Method.valueOf(split[0]);
			} catch (Exception e) {
				method = Method.UNRECOGNIZED;
			}
			uri = split[1];
			protocol = split[2];
		}

		if (method == null)
			return;

		// 进行检验
		if (method.equals(Method.UNRECOGNIZED)) {
			log.error("Missing HTTP request method!");
		} else if (uri.length() < 1) {
			log.error("Missing HTTP request URI");
		}

		// 提取GET请求参数
		int question = uri.indexOf("?");
		if (question >= 0) {
			log.info("URLQUERY: " + uri.substring(question + 1));
			request.setQueryString(uri.substring(question + 1));
			uri = new String(uri.substring(0, question));

		} else {
			request.setQueryString(null);
		}

		// 检查绝对路径的URI（带HTTP协议头）
		if (!uri.startsWith("/")) {
			int pos = uri.indexOf("://");
			// 去除掉协议头和主机名
			if (pos != -1) {
				pos = uri.indexOf('/', pos + 3);
				if (pos == -1) {
					uri = "";
				} else {
					uri = uri.substring(pos);
				}
			}
		}

		// 解析url中的sessionId（如果有的话）
		String match = ";jsessionid=";
	    int semicolon = uri.indexOf(match);
	    if (semicolon >= 0) {
	    	String rest = uri.substring(semicolon + match.length());
	    	int semicolon2 = rest.indexOf(';');
	    	if (semicolon2 >= 0) {
	    		request.setRequestedSessionId(rest.substring(0, semicolon2));
	    		rest = rest.substring(semicolon2);
	    	}
	    	else {
	    		request.setRequestedSessionId(rest);
	    		rest = "";
	    	}
	    	request.setRequestedSessionURL(true);
	    	uri = uri.substring(0, semicolon) + rest;
	    }
	    else {
	    	request.setRequestedSessionId(null);
	    	request.setRequestedSessionURL(false);
	    }
				
		// uri正常化
		String normalizedUri = normalize(uri);

		// 填充request
		((HttpRequest) request).setMethod(method.name());
		request.setProtocol(protocol);

		if (normalizedUri != null) {
			((HttpRequest) request).setRequestURI(normalizedUri);
		} else {
			log.error("Invalid URI: " + uri);
		}
		
		log.info(method.name() + " " + uri + " " + protocol);
	}

	protected String normalize(String path) {
		if (path == null)
			return null;
		// Create a place for the normalized path
		String normalized = path;

		// Normalize "/%7E" and "/%7e" at the beginning to "/~"
		if (normalized.startsWith("/%7E") || normalized.startsWith("/%7e"))
			normalized = "/~" + normalized.substring(4);

		// Prevent encoding '%', '/', '.' and '\', which are special reserved
		// characters
		if ((normalized.indexOf("%25") >= 0) || (normalized.indexOf("%2F") >= 0) || (normalized.indexOf("%2E") >= 0)
				|| (normalized.indexOf("%5C") >= 0) || (normalized.indexOf("%2f") >= 0)
				|| (normalized.indexOf("%2e") >= 0) || (normalized.indexOf("%5c") >= 0)) {
			return null;
		}

		if (normalized.equals("/."))
			return "/";

		// Normalize the slashes and add leading slash if necessary
		if (normalized.indexOf('\\') >= 0)
			normalized = normalized.replace('\\', '/');
		if (!normalized.startsWith("/"))
			normalized = "/" + normalized;

		// Resolve occurrences of "//" in the normalized path
		while (true) {
			int index = normalized.indexOf("//");
			if (index < 0)
				break;
			normalized = normalized.substring(0, index) + normalized.substring(index + 1);
		}

		// Resolve occurrences of "/./" in the normalized path
		while (true) {
			int index = normalized.indexOf("/./");
			if (index < 0)
				break;
			normalized = normalized.substring(0, index) + normalized.substring(index + 2);
		}

		// Resolve occurrences of "/../" in the normalized path
		while (true) {
			int index = normalized.indexOf("/../");
			if (index < 0)
				break;
			if (index == 0)
				return (null); // Trying to go outside our context
			int index2 = normalized.lastIndexOf('/', index - 1);
			normalized = normalized.substring(0, index2) + normalized.substring(index + 3);
		}

		// Declare occurrences of "/..." (three or more dots) to be invalid
		// (on some Windows platforms this walks the directory tree!!!)
		if (normalized.indexOf("/...") >= 0)
			return (null);

		// Return the normalized path that we have completed
		return (normalized);

	}

}
