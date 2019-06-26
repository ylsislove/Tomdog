package com.ylsislove.tomdog.log;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ylsislove.tomdog.gui.MainUI;

public class Logger {

	private static Map<String, Logger> manager = new HashMap<String, Logger>();
	
	private Logger() {
	}
	
	@SuppressWarnings("rawtypes")
	public static Logger getLogger(Class cl) {
		Logger log = manager.get(cl.getName());
		if (log == null) {
			log = new Logger();
			manager.put(cl.getName(), log);
		}
		return log;
	} 
	
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public void error(String msg) {
		MainUI.setMsg("时间：" + format.format(new Date()), new Color(243, 24, 73));
		MainUI.setMsg("信息：" + msg, new Color(243, 24, 73));
	}

	public void info(String msg) {
		MainUI.setMsg("时间：" + format.format(new Date()), new Color(20,123,17));
		MainUI.setMsg("信息：" + msg, Color.BLACK);
	}
}
