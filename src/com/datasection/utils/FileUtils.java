package com.datasection.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

public class FileUtils {

	
	public static LinkedHashMap<String, List<String>> getDataFromFolder(String folderName) throws IOException {

		File folder = new File(folderName);
		File[] files = folder.listFiles();
		
		LinkedHashMap<String, List<String>> datas = new LinkedHashMap<String, List<String>>();
		int c = 0;
		for (File file : files) {
			List<String> listSentence = org.apache.commons.io.FileUtils.readLines(file);
			datas.put(file.getName(), listSentence);
			System.out.println(file.getName());
			c++;
			if(c > 600)
				break;
		}

		return datas;
	}
	
	public static Properties getProperties(){
		
		Properties pros = new Properties();
		InputStream input = null;
		
		try {
			
			input = new FileInputStream("config.properties");
			pros.load(input);
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		return pros;
	}
	
}
