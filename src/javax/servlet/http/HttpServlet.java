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
			System.out.println("û�ж�Ӧ�����󷽷�");
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
		System.out.println("����HttpServlet�ĳ�ʼ���������ұ�������");
	}
	
	public void destroy() {
		System.out.println("����HttpServlet�����ٷ������ұ�������");
	}
	
}
