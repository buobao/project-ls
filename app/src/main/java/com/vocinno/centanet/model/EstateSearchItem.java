package com.vocinno.centanet.model;

public class EstateSearchItem {
	private int id = 0;
	private String name = "";
	private String type = "";
	private String custCode;

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

	public String getCustCode() {
		return custCode;
	}

	public void setCustCode(String custCode) {
		this.custCode = custCode;
	}
}
