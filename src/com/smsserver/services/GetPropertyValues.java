package com.smsserver.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Startup
@Singleton(name="properties")
public class GetPropertyValues {
	
	private static Properties prop ;
	
	static String result = "";
	static InputStream inputStream;


	@PostConstruct
	void loadConfig() {

		try {
			prop = new Properties();
			String propFileName = "config.properties";
 
			inputStream = GetPropertyValues.class.getClassLoader().getResourceAsStream(propFileName);
 
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
 
			System.out.println(prop);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
}



	public static Properties getProperties() {
		return prop;
	}
}
