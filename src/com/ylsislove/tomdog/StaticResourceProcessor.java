package com.ylsislove.tomdog;

import com.ylsislove.tomdog.connector.http.HttpRequest;
import com.ylsislove.tomdog.connector.http.HttpResponse;
import java.io.IOException;

public class StaticResourceProcessor {

	public void process(HttpRequest request, HttpResponse response) {
		try {
			response.sendStaticResource();
		} catch (IOException e) {
			;
		}
	}

}
