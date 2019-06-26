package javax.servlet.http;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public abstract class HttpServlet implements Servlet {

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	}

	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = req.getMethod();

		if (method.equals("GET")) {
			doGet(req, resp);

		} else if (method.equals("POST")) {
			doPost(req, resp);

		} else {
			System.out.println("没有对应的请求方法");
		}
	}

	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		HttpServletRequest request;
		HttpServletResponse response;

		try {
			request = (HttpServletRequest) req;
			response = (HttpServletResponse) res;
			
		} catch (ClassCastException e) {
			throw new ServletException("non-HTTP request or response");
		}
		service(request, response);
	}
	
	public void init() throws ServletException {
		System.out.println("我是HttpServlet的初始化方法，我被调用啦");
	}
	
	public void destroy() {
		System.out.println("我是HttpServlet的销毁方法，我被调用啦");
	}
	
}
