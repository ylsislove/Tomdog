package com.ylsislove.tomdog.connector;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletOutputStream;

import com.ylsislove.tomdog.connector.http.HttpResponse;

public class ResponseStream extends ServletOutputStream {

	public ResponseStream(HttpResponse response) {

		super();
		closed = false;
		commit = false;
		count = 0;
		this.response = response;
		// this.stream = response.getStream();

	}

	protected boolean closed = false;

	protected boolean commit = false;

	protected int count = 0;

	protected int length = -1;

	protected HttpResponse response = null;

	protected OutputStream stream = null;

	public boolean getCommit() {
		return (this.commit);
	}

	public void setCommit(boolean commit) {
		this.commit = commit;
	}

	public void close() throws IOException {
		if (closed)
			throw new IOException("responseStream.close.closed");
		response.flushBuffer();
		closed = true;
	}

	public void flush() throws IOException {
		if (closed)
			throw new IOException("responseStream.flush.closed");
		if (commit)
			response.flushBuffer();
	}

	public void write(int b) throws IOException {

		if (closed)
			throw new IOException("responseStream.write.closed");

		if ((length > 0) && (count >= length))
			throw new IOException("responseStream.write.count");

		response.write(b);
		count++;

	}

	public void write(byte b[]) throws IOException {
		write(b, 0, b.length);

	}

	public void write(byte b[], int off, int len) throws IOException {
		if (closed)
			throw new IOException("responseStream.write.closed");
		int actual = len;
		if ((length > 0) && ((count + len) >= length))
			actual = length - count;
		response.write(b, off, actual);
		count += actual;
		if (actual < len)
			throw new IOException("responseStream.write.count");
	}

	// -------------------------------------------------------- Package Methods

	/**
	 * Has this response stream been closed?
	 */
	boolean closed() {
		return (this.closed);
	}

	/**
	 * Reset the count of bytes written to this stream to zero.
	 */
	void reset() {
		count = 0;
	}

}
