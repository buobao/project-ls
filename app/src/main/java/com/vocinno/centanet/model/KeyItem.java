package com.vocinno.centanet.model;

import java.io.Serializable;

public class KeyItem implements Serializable{

	private String keyNum = ""; // 钥匙编号
	private String addr = ""; // 地址
	private String delCode = ""; // 房源编号
	private String borrowTime;// 借用时间
	private String borrowBy;// 借钥人
	private String holder;//收钥匙人
	private String createTime ;//收钥世间
	private boolean keyStatus ;//钥匙状态(true=在店，false=借用)
	private String store = ""; // 店面
	private String houId = "";
	private String img = "";
	private String returnComfirm;

	public String getKeyNum() {
		return keyNum;
	}

	public String getHolder() {
		return holder;
	}

	public void setHolder(String holder) {
		this.holder = holder;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public boolean isKeyStatus() {
		return keyStatus;
	}

	public void setKeyStatus(boolean keyStatus) {
		this.keyStatus = keyStatus;
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

	public String getBorrowBy() {
		return borrowBy;
	}

	public void setBorrowBy(String borrowBy) {
		this.borrowBy = borrowBy;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getBorrowTime() {
//		return MethodsData.getFriendDateTime(borrowTime);
		return this.borrowTime;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public void setBorrowTime(String borrowTime) {
		this.borrowTime = borrowTime;
	}

	public String getHouId() {
		return houId;
	}

	public void setHouId(String houId) {
		this.houId = houId;
	}

	public String getReturnComfirm() {
		return returnComfirm;
	}

	public void setReturnComfirm(String returnComfirm) {
		this.returnComfirm = returnComfirm;
	}
}
