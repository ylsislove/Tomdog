package javax.servlet.http;

import javax.servlet.ServletResponse;


public interface HttpServletResponse extends ServletResponse {

	public void sendRedirect(String path);

}
