package com.ylsislove.tomdog.connector.http;

import java.io.OutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.ylsislove.tomdog.connector.ResponseStream;
import com.ylsislove.tomdog.connector.ResponseWriter;
import com.ylsislove.tomdog.connector.http.Constants;
import com.ylsislove.tomdog.connector.http.HttpResponse;
import com.ylsislove.tomdog.connector.http.enumeration.ContentType;
import com.ylsislove.tomdog.connector.http.enumeration.Status;
import com.ylsislove.tomdog.log.Logger;

public class HttpResponse implements HttpServletResponse {

	private static Logger log = Logger.getLogger(HttpResponse.class);
	
	private static final int BUFFER_SIZE = 1024;
	HttpRequest request;
	OutputStream output;
	PrintWriter writer;
	protected byte[] buffer = new byte[BUFFER_SIZE];
	protected int bufferCount = 0;
	
	protected boolean committed = false;
	protected int contentCount = 0;
	protected int contentLength = -1;
	protected String contentType = null;
	protected String encoding = null;
	protected Locale locale;
	protected Map<String, ArrayList<String>> headers = new HashMap<String, ArrayList<String>>();
	protected ArrayList<Cookie> cookies = new ArrayList<Cookie>();
	protected String responseLine = "";
	byte[] body;

	public HttpResponse(OutputStream output) {
		this.output = output;
	}

	public void setRequest(HttpRequest request) {
		this.request = request;
	}
	
	public void finishResponse() {
		if (writer != null) {
			writer.flush();
			writer.close();
		}
	}

	public int getContentLength() {
		return this.contentLength;
	}

	public String getContentType() {
		return this.contentType;
	}

	protected String getProtocol() {
		return request.getProtocol();
	}

	public OutputStream getStream() {
		return this.output;
	}

	public void sendHeaders() throws IOException {
		if (isCommitted())
			return;

		setResponseLine(Status._200);
		setContentType(ContentType.HTML.toString());
		fillHeader();
		
		DataOutputStream os = new DataOutputStream(getStream());
		os.writeBytes(responseLine + "\r\n");
		
	    if (getContentType() != null) {
	    	os.writeBytes("Content-Type: " + getContentType() + "\r\n");
	    }
	    if (getContentLength() >= 0) {
	    	os.writeBytes("Content-Length: " + getContentLength() + "\r\n");
	    }
		
		synchronized (headers) {
			Iterator<String> names = headers.keySet().iterator();
			while (names.hasNext()) {
				String name = (String) names.next();
		        ArrayList<String> values = (ArrayList<String>) headers.get(name);
		        Iterator<String> items = values.iterator();
		        while (items.hasNext()) {
		        	String value = (String) items.next();
		        	os.writeBytes(name + ": " + value + "\r\n");
	        	}
			}
	    }
		
		synchronized (cookies) {
			Iterator<Cookie> items = cookies.iterator();
			while (items.hasNext()) {
				Cookie cookie = (Cookie) items.next();
				os.writeBytes(cookie.getName() + ": " + cookie.getValue() + "\r\n");
			}
	    }
		
		os.writeBytes("\r\n");
		os.flush();
		
		committed = true;
	}
	
	public void addHeader(String name, String value) {
		name = name.toLowerCase();
		synchronized (headers) {
			ArrayList<String> values = (ArrayList<String>) headers.get(name);
			if (values == null) {
				values = new ArrayList<String>();
				headers.put(name, values);
			}
			values.add(value);
		}
	}
	
	private void fillHeader() {
		addHeader("Connection", "close");
		addHeader("Server", "Tomdog");
		addHeader("Set-Cookie", request.getSession().getId());
	}
	
	private void fillResponse(String response) {
		body = response.getBytes();
	}

	private void fillResponse(byte[] response) {
		body = response;
	}
	
	private byte[] getBytes(File file) throws IOException {
		int length = (int) file.length();
		byte[] array = new byte[length];
		
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		in.readFully(array);
		
		in.close();
		return array;
	}
	
	public void write(OutputStream os) throws IOException {
		DataOutputStream output = new DataOutputStream(os);
		output.writeBytes(responseLine + "\r\n");
		fillHeader();
		
	    if (getContentType() != null) {
	    	output.writeBytes("Content-Type: " + getContentType() + "\r\n");
	    }
	    if (getContentLength() >= 0) {
	    	output.writeBytes("Content-Length: " + getContentLength() + "\r\n");
	    }
		
		synchronized (headers) {
			Iterator<String> names = headers.keySet().iterator();
			while (names.hasNext()) {
				String name = (String) names.next();
		        ArrayList<String> values = (ArrayList<String>) headers.get(name);
		        Iterator<String> items = values.iterator();
		        while (items.hasNext()) {
		        	String value = (String) items.next();
		        	output.writeBytes(name + ": " + value + "\r\n");
	        	}
			}
	    }
		
		synchronized (cookies) {
			Iterator<Cookie> items = cookies.iterator();
			while (items.hasNext()) {
				Cookie cookie = (Cookie) items.next();
				output.writeBytes(cookie.getName() + ": " + cookie.getValue() + "\r\n");
			}
	    }
		
		output.writeBytes("\r\n");
		if (body != null) {
			output.write(body);
		}
		output.writeBytes("\r\n");
		output.flush();
	}
	
	private void setResponseLine(Status status) {
		this.responseLine = request.getProtocol() + " " + status.toString();
	}
	
	public void sendStaticResource() throws IOException {
		
		if (request.getRequestURI() == null) return;
		
		FileInputStream fis = null;
		try {
			
			File file = new File(Constants.WEB_SC_ROOT, request.getRequestURI());
			
			if (file.isDirectory()) {
				setResponseLine(Status._200);
				setContentType(ContentType.HTML.toString());
				
			    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				StringBuilder result = new StringBuilder("<html><head><title>Index of ");
				result.append(request.getRequestURI());
				result.append("</title></head><body><h1>Index of ");
				result.append(request.getRequestURI());
				result.append("</h1><hr><pre>");

				File[] files = file.listFiles();
				for (File subfile : files) {
					result.append(format.format(new Date(subfile.lastModified()))+"\t");
					
					if (subfile.isDirectory()) result.append("Dir\t\t");
					else result.append(subfile.length()+"\t\t");
					
					if (request.getRequestURI().endsWith("/"))
						result.append(" <a href=\"" + request.getRequestURI() + subfile.getName() + "\">" + subfile.getName() + "</a>\n");
					else
						result.append(" <a href=\"" + request.getRequestURI() +"/"+ subfile.getName() + "\">" + subfile.getName() + "</a>\n");
				}
				result.append("<hr></pre></body></html>");
				fillResponse(result.toString());
				
			} else if (file.exists()) {
				setResponseLine(Status._200);
				parseContentType(request.getRequestURI());
				fillResponse(getBytes(file));
			} 
			
		} catch (FileNotFoundException e) {
			setResponseLine(Status._404);
			String errorMessage = "HTTP/1.1 404 File Not Found\r\n" + "Content-Type: text/html\r\n"
					+ "Content-Length: 23\r\n" + "\r\n" + "<h1>File Not Found</h1>";
			fillResponse(errorMessage);
			
		} finally {
			if (fis != null)
				fis.close();
		}
		
		write(output);
	}
	
	private void parseContentType(String requestURI) {
		try {
			String uri = request.getRequestURI();
			String ext = uri.substring(uri.lastIndexOf(".") + 1);
			this.contentType = ContentType.valueOf(ext.toUpperCase()).toString();
		} catch (Exception e) {
			this.contentType = ContentType.HTML.toString();
		}
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void write(int b) throws IOException {
		if (bufferCount >= buffer.length)
			flushBuffer();
		buffer[bufferCount++] = (byte) b;
		contentCount++;
	}

	public void write(byte b[]) throws IOException {
		write(b, 0, b.length);
	}

	public void write(byte b[], int off, int len) throws IOException {
		// If the whole thing fits in the buffer, just put it there
		if (len == 0)
			return;
		if (len <= (buffer.length - bufferCount)) {
			System.arraycopy(b, off, buffer, bufferCount, len);
			bufferCount += len;
			contentCount += len;
			return;
		}

		// Flush the buffer and start writing full-buffer-size chunks
		flushBuffer();
		int iterations = len / buffer.length;
		int leftoverStart = iterations * buffer.length;
		int leftoverLen = len - leftoverStart;
		for (int i = 0; i < iterations; i++)
			write(b, off + (i * buffer.length), buffer.length);

		// Write the remainder (guaranteed to fit in the buffer)
		if (leftoverLen > 0)
			write(b, off + leftoverStart, leftoverLen);
	}


	public void addCookie(Cookie cookie) {
	    if (isCommitted())
	    	return;
	    synchronized (cookies) {
	    	cookies.add(cookie);
	    }
	}
	
	public String encodeRedirectURL(String url) {
		return null;
	}

	public String encodeRedirectUrl(String url) {
		return encodeRedirectURL(url);
	}

	public String encodeUrl(String url) {
		return encodeURL(url);
	}

	public String encodeURL(String url) {
		return null;
	}

	public void flushBuffer() throws IOException {
		// committed = true;
		if (bufferCount > 0) {
			try {
				output.write(buffer, 0, bufferCount);
			} finally {
				bufferCount = 0;
			}
		}
	}

	public int getBufferSize() {
		return 0;
	}

	public String getCharacterEncoding() {
		if (encoding == null)
			return ("UTF-8");
		else
			return (encoding);
	}

	public Locale getLocale() {
		return null;
	}

	public ServletOutputStream getOutputStream() throws IOException {
		return (ServletOutputStream) output;
	}

	public PrintWriter getWriter() throws IOException {
		// ∑¢ÀÕœÏ”¶Õ∑
		sendHeaders();
		
		ResponseStream newStream = new ResponseStream(this);
		newStream.setCommit(false);
		OutputStreamWriter osr = new OutputStreamWriter(newStream, getCharacterEncoding());
		writer = new ResponseWriter(osr);
		return writer;
	}

	/**
	 * Has the output of this response already been committed?
	 */
	public boolean isCommitted() {
		return (committed);
	}

	public void reset() {
	}

	public void resetBuffer() {
	}

	public void setBufferSize(int size) {
	}

	public void setContentLength(int length) {
		this.contentLength = length;
	}

	public void setCharacterEncoding(String charset) {
		this.encoding = charset;
	}

	public void setLocale(Locale loc) {
		this.locale = loc;
	}
	
	public void sendRedirect(String path) {	

		DataOutputStream out = new DataOutputStream(getStream());
		try {
			out.writeBytes("HTTP-1.0 302 OK\r\n");
			out.writeBytes("Location:/"+path+"\r\n\r\n");
			out.flush();
			out.close();
		} catch (IOException e1) {
			log.error("sendRedirect error!");
		}
		
	}
}
