package com.vocinno.centanet.model;

public class Requets {
	private String acreage = "";// 区域
	private String TenancyTime = "";// 租期
	private String price = "";// 价格
	private String reqType = "";// 类型
	private String selfDescription ; // 需求自述
	private String fromToRoom;
	public Requets() {
	}

	public String getTenancyTime() {
		return TenancyTime;
	}

	public void setTenancyTime(String TenancyTime) {
		this.TenancyTime = TenancyTime;
	}

	public String getAcreage() {
		return acreage;
	}

	public void setAcreage(String acreage) {
		this.acreage = acreage;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getSelfDescription() {
		return selfDescription;
	}

	public void setSelfDescription(String selfDescription) {
		this.selfDescription = selfDescription;
	}

	public String getReqType() {
		return reqType;
	}

	public void setReqType(String reqType) {
		this.reqType = reqType;
	}

	public String getFromToRoom() {
		return fromToRoom;
	}

	public void setFromToRoom(String fromToRoom) {
		this.fromToRoom = fromToRoom;
	}
}
