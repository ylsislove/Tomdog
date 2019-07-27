package com.ylsislove.tomdog.connector.http;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public final class Constants {
	
	public static String CLASSPATH = System.getProperty("user.dir") + File.separator + "bin";
	public static String WEB_ROOT = System.getProperty("user.dir");
	public static String WEB_SC_ROOT = WEB_ROOT + File.separator + "WebContent";
	public static String WEB_XML_ROOT = WEB_SC_ROOT + File.separator + "WEB-INF";

	public static int WEB_PORT = 80;

	public static void setWebRoot(String path) {
		WEB_ROOT = path;
		WEB_SC_ROOT = WEB_ROOT + File.separator + "WebContent";
		WEB_XML_ROOT = WEB_SC_ROOT + File.separator + "WEB-INF";
	}
	
	public static void setPort(int port) {
		WEB_PORT = port;
	}
	
	
	private static boolean isParse = false;
	private static final Map<String, String> url_name = new HashMap<String, String>();
	private static final Map<String, String> name_class = new HashMap<String, String>();
	
	private static void parseXML() {
		File file = new File(WEB_XML_ROOT + "/web.xml");
        SAXReader reader = new SAXReader();
        
        Document document = null;
        try {
			document = reader.read(file);
		} catch (DocumentException e) {
			System.out.println(e);
		}
		
		Element root = document.getRootElement();
		String name;
		String value;
		
		List<Element> servlet_list = root.elements("servlet");
        for (Element e : servlet_list) {
        	name = e.element("servlet-name").getTextTrim();
        	value = e.element("servlet-class").getTextTrim();
        	name_class.put(name, value);
        }
        
        List<Element> servletMapping_list = root.elements("servlet-mapping");
        for (Element e : servletMapping_list) {
        	name = e.element("servlet-name").getTextTrim();
        	value = e.element("url-pattern").getTextTrim();
        	url_name.put(value, name);
        }

	}
	
	public static String getServletClass(String url) {
		if (!isParse) {
			parseXML();
			isParse = true;
		}
		String name = url_name.get(url);
		return name_class.get(name);
	}
	
	public static boolean isServletUrl(String url) {
		if (!isParse) {
			parseXML();
			isParse = true;
		}
		parseXML();
		return url_name.containsKey(url);
	}
	
	public static boolean requireFilter(String uri) {
		if (!uri.endsWith(".ico")) {
			return true;
		}
		return false;
	}
	
	public static String getFilterClass(String uri) {
		return "course.AccessFilter";
	}
	
}
