package com.datasection.entities;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Keyword {

	private String keyword;
	private int quantity;
	private int amount;
	private String time;
	private ArrayList<String> status;
	private LinkedHashMap<String, ArrayList<Integer>> status2;

	public Keyword() {
		status = new ArrayList<String>();
	}

	public Keyword(String keyword, int quantity, int amount, String time) {
		super();
		this.keyword = keyword;
		this.quantity = quantity;
		this.amount = amount;
		this.time = time;
		status = new ArrayList<String>();
	}
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public ArrayList<String> getStatus() {
		return status;
	}

	public void addStatus(String date,String isKeyword) {
		status.add(date + ":" + quantity + ":" + amount + ":" + isKeyword);
		if(status.size() > 48)
			status.remove(0);
	}
	
	public void setStatus(ArrayList<String> status){
		this.status = status;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

}
