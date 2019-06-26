package javax.servlet;

public class ServletException extends Exception {

	private static final long serialVersionUID = 1L;
	private Throwable rootCause;

	public ServletException() {
		super();
	}

	public ServletException(String message) {
		super(message);
	}



	public ServletException(String message, Throwable rootCause) {
		super(message);
		this.rootCause = rootCause;
	}


	public ServletException(Throwable rootCause) {
		super(rootCause.getLocalizedMessage());
		this.rootCause = rootCause;
	}


	public Throwable getRootCause() {
		return rootCause;
	}
}
