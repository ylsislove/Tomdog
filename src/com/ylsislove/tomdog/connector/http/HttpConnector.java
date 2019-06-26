package com.ylsislove.tomdog.connector.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.ylsislove.tomdog.log.Logger;

public class HttpConnector implements Runnable {

	private static Logger log = Logger.getLogger(HttpConnector.class);
	
	private String scheme = "http";
	public String getScheme() {
		return scheme;
	}
	
	public void run() {
		
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(Constants.WEB_PORT);
			
		} catch (IOException e) {
			log.error("ServerSocket init error");
			System.exit(1);
		}
		
		//System.out.println("Web server listening on port " + Constants.WEB_PORT);
		log.info("Tomdog listening on port " + Constants.WEB_PORT);
		
		while (!Thread.currentThread().isInterrupted()) {	
			Socket socket = null;
			try {
				socket = serverSocket.accept();
				if (Thread.currentThread().isInterrupted()) {
					break;
				}
			} catch (Exception e) {
				continue;
			}
			
			HttpProcessor processor = new HttpProcessor(socket);
			Thread cThread = new Thread(processor); 
			cThread.setDaemon(true);
			cThread.start();
		}
	}

	public void start() {
		Thread thread = new Thread(this);
		thread.start();
	}
}