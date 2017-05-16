package com.resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Set;
/**
 * 
 * @author rajendra.beelagi
 *
 */

public class PropertiesCache
{
	private final Properties configProp = new Properties();
	private static PropertiesCache  propertiesCache= null;

	private PropertiesCache(){
		try {
			InputStream in= Files.newInputStream(Paths.get("src/com/resource/data.properties").toAbsolutePath());
			configProp.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static PropertiesCache getInstance() 
	{
		if (propertiesCache == null) {
			propertiesCache = new PropertiesCache();
		}
		return propertiesCache;
	}

	public String getProperty(String key){
		return configProp.getProperty(key);
	}

	public Set<String> getAllPropertyNames(){
		return configProp.stringPropertyNames();
	}

	public boolean containsKey(String key){
		return configProp.containsKey(key);
	}
	

}
