package com.datasection.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.datasection.entities.Keyword;
import com.datasection.utils.ArgsConstant;
import com.datasection.utils.KeywordUtil;

public class TrendDetection extends Thread {

	private ArrayList<Keyword> burstyKeywords;
	private HashMap<String, Float> listPopularKeyword;
	private LinkedHashMap<String, LinkedHashMap<String, Keyword>> keywordData;
	private LinkedHashMap<String, List<String>> datas;

	public TrendDetection() {
		ArgsConstant.newInstance();
		burstyKeywords = new ArrayList<Keyword>();
		listPopularKeyword = new HashMap<String, Float>();
		keywordData = new LinkedHashMap<String, LinkedHashMap<String, Keyword>>();
		datas = new LinkedHashMap<String, List<String>>();
	}

	public ArrayList<Keyword> getBurstyKeywords() {
		return burstyKeywords;
	}

	public void setBurstyKeywords(ArrayList<Keyword> burstyKeywords) {
		this.burstyKeywords = burstyKeywords;
	}

	public HashMap<String, Float> getListPopularKeyword() {
		return listPopularKeyword;
	}

	public void setFrequenceKeyword(HashMap<String, Float> listPopularKeyword) {
		this.listPopularKeyword = listPopularKeyword;
	}

	/*
	 * public void detectTreding() {
	 * 
	 * try {
	 * 
	 * for (String fileName : datas.keySet()) { List<String> listData =
	 * datas.get(fileName); LinkedHashMap<String, Keyword> keywords =
	 * KeywordUtil.getKeyword(this, fileName, listData, keywordData); if
	 * (keywords.size() < 0) continue; keywordData.put(fileName, keywords); }
	 * 
	 * } catch (IOException e) { e.printStackTrace(); }
	 * 
	 * // for(String key : keywordData.keySet()){ // // LinkedHashMap<String,
	 * Keyword> listKeyword = keywordData.get(key); // for(String key2 :
	 * listKeyword.keySet()){ // // Keyword keyword = listKeyword.get(key2); //
	 * if(keyword.getQuantity() > 20){ // ArrayList<Keyword> list =
	 * stopwords.get(keyword.getKeyword()); // if(list == null){ // list = new
	 * ArrayList<Keyword>(); // list.add(keyword); //
	 * stopwords.put(keyword.getKeyword(), list); // } else { //
	 * list.add(keyword); // } // } // // } // listKeyword.clear(); // }
	 * 
	 * /* int k = 0; for(String key : stopwords.keySet()){ //
	 * if(stopwords.get(key).size() > 20){ // System.out.println(key); // k++;
	 * // } if(stopwords.get(key).size() > 30){ int m =
	 * numOfDataAppearance(stopwords.get(key)); if(m >= 6){ //int m =
	 * countDiffirenceDate() float z = 0; for(Keyword ke : stopwords.get(key)){
	 * z += ke.getQuantity(); } System.out.println(key + "\t" + (z /
	 * stopwords.get(key).size())); try {
	 * org.apache.commons.io.FileUtils.write(new File("model.txt"), key + "\t" +
	 * (z / stopwords.get(key).size()), true); } catch (IOException e) {
	 * e.printStackTrace(); } k++; } } } System.out.println(k);
	 */

	/*
	 * for (Keyword key : burstyKeywords) { try { FileOutputStream fos = new
	 * FileOutputStream( ArgsConstant.OUTPUT +
	 * "\\" + key.getKeyword() + key.getTime() + ".txt"); PrintWriter pw = new
	 * PrintWriter(fos); for (String str : key.getStatus()) { pw.println(str);
	 * pw.flush(); } } catch (FileNotFoundException e) { e.printStackTrace(); }
	 * 
	 * }
	 * 
	 * }
	 */

	public static int numOfDataAppearance(ArrayList<Keyword> listKeyword) {

		HashMap<String, String> hm = new HashMap<String, String>();
		for (Keyword keyword : listKeyword) {
			hm.put(getDate(keyword.getTime()), "1");
		}

		return hm.size();
	}

	public static String getDate(String str) {

		String time = str.substring(0, str.lastIndexOf("_"));

		return time;

	}

	public static String getNextDate(String date) {

		String day = date.substring(date.lastIndexOf("_") + 1, date.length());

		return date.substring(0, date.lastIndexOf("_")) + "_" + (Integer.parseInt(day) + 1);
	}

	private File getFileToDetect() {

		File folder = new File(ArgsConstant.newInstance().DATA_FOLDER);
		File[] files = folder.listFiles();

		for (File file : files) {
			if (!datas.containsKey(file.getName()))
				return file;
		}

		return null;

	}

	@Override
	public void run() {
		try {
			List<String> models = org.apache.commons.io.FileUtils.readLines(new File("model"));
			for (String str : models) {
				String[] tokens = str.split("\t");
				if (tokens.length != 2)
					continue;
				listPopularKeyword.put(tokens[0], new Float(Float.parseFloat(tokens[1])));
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		while (true) {

			// get file data to detect trend
			File file = getFileToDetect();
			for(String key : datas.keySet()){
				datas.get(key).clear();
			}
			if (file == null) {
				sleep();
				continue;
			}
			// push data into datas and get keyword
			try {
				List<String> dataInFile = org.apache.commons.io.FileUtils.readLines(file);
				datas.put(file.getName(), dataInFile);
				// get keyword and detect bursty keyword
				LinkedHashMap<String, Keyword> keywords = KeywordUtil.getKeyword(this, file.getName(), dataInFile,
						keywordData);
				if (keywords.size() < 0)
					continue;
				keywordData.put(file.getName(), keywords);
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			File folder = new File(
					System.getProperty("user.dir") + "\\" + ArgsConstant.newInstance().OUTPUT + "\\" + getDate(file.getName()));
			if (!folder.exists())
				folder.mkdir();
			for (Keyword key : burstyKeywords) {
				try {

					FileOutputStream fos = new FileOutputStream(
							System.getProperty("user.dir") + "\\" + ArgsConstant.newInstance().OUTPUT
									+ "\\" + getDate(file.getName()) + "\\" + key.getKeyword() + "-" + key.getTime()
											.substring(key.getTime().lastIndexOf("_") + 1, key.getTime().length())
							+ ".txt");
					@SuppressWarnings("resource")
					PrintWriter pw = new PrintWriter(fos);

					for (String str : key.getStatus()) {
						pw.println(str);
						pw.flush();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

			}
			System.out.println("Bursty Keyword " + file.getName() + " : " + burstyKeywords.size());
			burstyKeywords.clear();

			sleep();
		}
	}

	public void sleep() {
		try {
			Thread.sleep(1000 * ArgsConstant.newInstance().TIME_SLEEP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
