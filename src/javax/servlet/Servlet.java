package javax.servlet;

import java.io.IOException;

public interface Servlet {

    public void init() throws ServletException;

    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException;

    public void destroy();
}
