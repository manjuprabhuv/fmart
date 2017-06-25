package com.fmart.ui.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class FmartProperties {
	
	static Properties props;

	
	private static void init() throws IOException{
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream is =classLoader.getResourceAsStream("fmart.properties");
		props = new Properties();
		props.load(is);	
	}
	
	public static String getValue(String key){
		if(props==null){
			try {
				init();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return (String)props.get(key);
	}

}
