package com.ylsislove.tomdog.utils;

import javax.tools.*;

import com.ylsislove.tomdog.connector.http.Constants;
import com.ylsislove.tomdog.log.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

public class Compiler {
	
	private static Logger log = Logger.getLogger(Compiler.class);
	
	public static Class<?> getServlet(String inputPath, String outPath, String classname) {		
		// 预处理
		if (!inputPath.endsWith("/")) {
			inputPath = inputPath + "/";
		}
		String name = classname.replace(".", "/");
		
		JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
		//String classPath = Constants.WEB_CLASS_ROOT + ";" + System.getProperty("user.dir") + "/bin";
		int status = javac.run(null, null, null, "-d", outPath, inputPath + name + ".java");
		if(status != 0) { 
			log.error("Compiler getServlet run error!");
		}
		
		URLClassLoader loader = null;
		try {
			URL url = new URL("file:" + Constants.CLASSPATH + File.separator);
			loader = new URLClassLoader(new URL[] { url });
		} catch (IOException e) {
			log.error("Compiler getServlet URLClassLoader init error!");
		}
		Class<?> myClass = null;
		try {
			myClass = loader.loadClass(classname);
		} catch (ClassNotFoundException e) {
			log.error("Compiler getServlet loadClass error!");
		}	
		return myClass;
	}
	
	
    /**
     	* 装载字符串成为java可执行文件
     * @param className className
     * @param javaCodes javaCodes
     * @return Class
     */
	
	public static Class<?> createServlet(String className, String codes) {
        Class<?> clazz = compile(className, codes);
        return clazz;
	}

    private static Class<?> compile(String className, String javaCodes) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        StrSrcJavaObject srcObject = new StrSrcJavaObject(className, javaCodes);
        Iterable<? extends JavaFileObject> fileObjects = Arrays.asList(srcObject);
        String flag = "-d";
        String outDir = "";
        
        try {
            File classPath = new File(Thread.currentThread().getContextClassLoader().getResource("").toURI());
            outDir = classPath.getAbsolutePath() + File.separator;
            
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
        
        Iterable<String> options = Arrays.asList(flag, outDir);
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, options, null, fileObjects);
        
        boolean result = task.call();
        
        if (result == true) {
            try {
                return Class.forName(className);
                
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    private static class StrSrcJavaObject extends SimpleJavaFileObject {
        private String content;
        StrSrcJavaObject(String name, String content) {
            super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.content = content;
        }
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return content;
        }
    }

}