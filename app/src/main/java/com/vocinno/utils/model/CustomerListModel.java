package com.vocinno.utils.model;

public class CustomerListModel {
	private int mCustomerId;
	private String mCustomerName;
	private String mCustomerBelong;
	private String mCustomerHouseType;
	private String mCustomerHouseArea;
	private String mCustomerRequire;
	private String mCustomerType;
	private int mCustomerLowPrice;
	private int mCustomerHighPrice;
	private String mCustomerPublishTime;
	
	public void setCustomerId(int id){
		this.mCustomerId = id;
	}
	
	public int getCustomerId(){
		return this.mCustomerId;
	}
	
	public void setCustomerBelong(String customerBelong){
		this.mCustomerBelong = customerBelong;
	}
	
	public String getCustomerBelong(){
		return this.mCustomerBelong;
	}
}
