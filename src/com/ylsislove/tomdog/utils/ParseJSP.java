package com.ylsislove.tomdog.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServlet;
import com.ylsislove.tomdog.connector.http.Constants;
import com.ylsislove.tomdog.log.Logger;

public class ParseJSP extends HttpServlet {

	private static Logger log = Logger.getLogger(ParseJSP.class);
	
	private static StringBuffer htmlcode = new StringBuffer();
	private static StringBuffer javacode = new StringBuffer();
	private static List<String> html_list = new ArrayList<String>();
	private static List<String> java_list = new ArrayList<String>();
	
	private static String getClassName(String uri) {
		int pos = uri.lastIndexOf("/");
		int pos1 = uri.lastIndexOf(".jsp");
		String name = uri.substring(pos+1, pos1);
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}
	
	public static Class<?> parse(String uri) {	
		String className = getClassName(uri);
		
		File file = new File(Constants.WEB_SC_ROOT, uri);
		StringBuffer original = new StringBuffer();
		String str = "";
		
		BufferedReader br = null;
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
			br = new BufferedReader(isr);
			while (br.ready()) {
				str = br.readLine();
				original.append(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 生成List集合
		generateList(original.toString());
		clearInvalidListItem();
		//showList();

		StringBuffer codes = new StringBuffer();
		codes.append("package jsp;" +
				"import javax.servlet.http.*;"+
				"import java.io.*;"+
				"public class " + className + " extends HttpServlet {"+
				"public void doGet (HttpServletRequest request, HttpServletResponse response) throws IOException {" + 
				"PrintWriter out = response.getWriter();");
		
		
		for (int i = 0; i < html_list.size(); i++) {
			// 字符转中的双引号进行转义
			str = translate(html_list.get(i));
			// 写入
			if (str != null && !str.equals("")) {
				codes.append("out.println(\"" + str + "\");");
			}
			// 跳过空
			str = java_list.get(i);
			if (str.equals(""))
				continue;
			// 写入
			if (str.startsWith("=")) {
				// 移除等号
				str = str.replace("=", "");
				codes.append("out.println(" + str + ");");
			} else {
				codes.append(java_list.get(i));
			}
		}
		
		codes.append("out.close(); }" + 
				"public void doPost (HttpServletRequest request, HttpServletResponse response) throws IOException {" + 
				"doGet(request,response); } }");
		
		writeFile(codes.toString(), className);
		
		// 这是根据字符串自动创建类并编译成.class文件
		//Class<?> clazz = CompilerJSP.createServlet("com.ylsislove.tomdog.myjsp."+className, codes.toString());
		
		// 这是直接编译.java文件并编译成.class文件，参数一：.java输入路径，参数二：.class输出路径，类名
		Class<?> clazz = Compiler.getServlet(Constants.CLASSPATH, Constants.CLASSPATH, "jsp." + className);
		
		// 清尾工作
		clear();
		
		return clazz;
	}
	
	private static void writeFile(String codes, String className) {
		File file = new File(System.getProperty("user.dir") + "/bin/jsp/" + className + ".java");
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdir();
		}
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter(file));
		} catch (IOException e) {
			log.error("CompilerJSP write file error!");
			log.error(e.getMessage());
		}
		pw.println(codes);
		pw.close();
	}
	
	private static String translate(String str) {
		StringBuffer sb = new StringBuffer();
		if (str.equals(""))
			return null;
		
		// 字符转中的双引号进行转义
		int pos = str.indexOf("\"");
		if (pos == -1) 
			return str;
		
		while (pos != -1) {
			sb.append(str.substring(0, pos) + "\\\"");
			str = str.substring(pos+1);
			pos = str.indexOf("\"");
		}
		sb.append(str);
		return sb.toString();
	}
	
	private static void clear() {
		htmlcode.delete(0, htmlcode.length());
		javacode.delete(0, javacode.length());
		html_list.clear();
		java_list.clear();
	}
	
	private static void generateList(String original) {
		Pattern pattern = Pattern.compile("(<%@(.*?)%>)");
		Matcher matcher = pattern.matcher(original);
		String str = "";
		
		// 先去除jsp头部
		if (matcher.find()) {
			str = original.replace(matcher.group(1), "");
		} else {
			str = original;
		}
		
		while (!str.equals("")) {
			// 如果以JSP输出表达式开头
			matcher = Pattern.compile("^(<%(.*?)%>)").matcher(str);
			if (matcher.find()) {
				javacode.append(matcher.group(2).trim());
				str = str.replace(matcher.group(1), "").trim();
				
			}
			// 以HTML代码开头
			else {
				matcher = Pattern.compile("(.*?)<%(.*?)%>").matcher(str);
				if (matcher.find()) {
					// 保存，注意位置，一段html代码，对应一段java代码
					sava2List();
					
					htmlcode.append(matcher.group(1).trim());
					str = str.replace(matcher.group(1), "").trim();
				}
				// 到达文件尾
				else {
					// 保存之前的数据
					sava2List();
					
					// 加入新数据
					htmlcode.append(str.trim());
					html_list.add(htmlcode.toString());
					java_list.add(javacode.toString());
					break;
				}
				
			}
		}
	}
	
	@SuppressWarnings("unused")
	private static void showList() {
		for (int i = 0; i < html_list.size(); i++) {
			System.out.println("------------html----------");
			System.out.println(html_list.get(i));
			System.out.println("------------java----------");
			System.out.println(java_list.get(i));
		}
	}
	
	// 清除列表项都是空的项
	private static void clearInvalidListItem() {
		for (int i = 0; i < html_list.size(); i++) {
			if (html_list.get(i).equals("") && java_list.get(i).equals("")) {
				html_list.remove(i);
				java_list.remove(i);
			}
		}
	}
	
	private static void sava2List() {
		// 保存，注意位置，一段html代码，对应一段java代码
		html_list.add(htmlcode.toString());
		java_list.add(javacode.toString());
		htmlcode.delete(0, htmlcode.length());
		javacode.delete(0, javacode.length());
	}

}