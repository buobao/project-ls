package com.vocinno.centanet.model;

import java.util.List;

public class FirstHouse {
	private String address;
	private String peiKan;
	private List<String> imgPath;
	private String commitment;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPeiKan() {
		return peiKan;
	}

	public void setPeiKan(String peiKan) {
		this.peiKan = peiKan;
	}

	public List<String> getImgPath() {
		return imgPath;
	}

	public void setImgPath(List<String> imgPath) {
		this.imgPath = imgPath;
	}

	public String getCommitment() {
		return commitment;
	}

	public void setCommitment(String commitment) {
		this.commitment = commitment;
	}
}
