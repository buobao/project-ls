package com.vocinno.centanet.model;

public class ContactItem {
	
	private String partiInfoId="";	//联系人ID
	private String contactType="";	//联系人类型
	private String name="";		//联系人姓名
	private String tel="";			//联系人电话
	private String frequency="";	//最近一周联系次数
	private String customerIcon=""; //
	public String getPartiInfoId() {
		return partiInfoId;
	}
	public void setPartiInfoId(String partiInfoId) {
		this.partiInfoId = partiInfoId;
	}
	public String getContactType() {
		return contactType;
	}
	public void setContactType(String contactType) {
		this.contactType = contactType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	
	public void setCustomerIcon(String path){
		this.customerIcon = path;
	}
	public String getCustomerIcon(){
		return this.customerIcon;
	}

}
