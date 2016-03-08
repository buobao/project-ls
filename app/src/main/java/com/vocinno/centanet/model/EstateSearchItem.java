package com.vocinno.centanet.model;

public class EstateSearchItem {
	private int id = 0;
	private String name = "";
	private String type = "";
	
	public int getSearchId(){
		return id;
	}
	
	public String getSearchType(){
		return type;
	}
	
	public String getSearchName(){
		return name;
	}
	
	public void setSearchId(int id){
		this.id = id;
	}
	
	public void setSearchType(String type){
		this.type = type;
	}
	
	public void setSearchName(String name){
		this.name = name;
	}
}
