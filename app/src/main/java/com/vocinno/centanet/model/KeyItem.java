package com.vocinno.centanet.model;

import com.vocinno.utils.MethodsData;

public class KeyItem {

	private String keyNum = ""; // 钥匙编号
	private String addr = ""; // 地址
	private String delCode = ""; // 房源编号
	private long borrowTime = 0;// 借用时间
	private String store = ""; // 店面
	private String houId = "";
	private String isWaitingConfirm = "";
	

	public String getKeyNum() {
		return keyNum;
	}

	public void setKeyNum(String KeyNum) {
		keyNum = KeyNum;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getDelCode() {
		return delCode;
	}

	public void setDelCode(String delCode) {
		this.delCode = delCode;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public long getBorrowTime() {
//		return MethodsData.getFriendDateTime(borrowTime);
		return this.borrowTime;
	}

	public void setBorrowTime(long borrowTime) {
		this.borrowTime = borrowTime;
	}

	public String getHouId() {
		return houId;
	}

	public void setHouId(String houId) {
		this.houId = houId;
	}

	public String getIsWaitingConfirm() {
		return isWaitingConfirm;
	}

	public void setIsWaitingConfirm(String isWaitingConfirm) {
		this.isWaitingConfirm = isWaitingConfirm;
	}

}
