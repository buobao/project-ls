package com.vocinno.centanet.model;

import java.util.ArrayList;
import java.util.List;

public class CustomerDetail {
	private String custCode = ""; // 客户编号
	private String name = ""; // 姓名
	private String phone = ""; // 联系电话
	private String qq = ""; // 联系qq
	private String wechat = ""; // 微信
	private List<Content> content = new ArrayList<Content>();
	private List<Requets> requets = new ArrayList<Requets>();
	private List<Track> tracks = new ArrayList<Track>();

	public List<Content> getContent() {
		return content;
	}

	public void setContent(List<Content> content) {
		this.content = content;
	}

	private String paymentType = ""; // 付款方式

	private boolean isPay = false; // 是否付佣金
	private boolean isPublic = false; // 是否是公客

	public String getCustCode() {
		return custCode;
	}

	public void setCustCode(String custCode) {
		this.custCode = custCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}


	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public boolean isPay() {
		return isPay;
	}

	public void setPay(boolean isPay) {
		this.isPay = isPay;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public List<Requets> getRequets() {
		return requets;
	}

	public void setRequets(List<Requets> requets) {
		this.requets = requets;
	}

	public List<Track> getTracks() {
		return tracks;
	}

	public void setTracks(List<Track> tracks) {
		this.tracks = tracks;
	}

	public class Content{
		private String name;
		private String phone;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}
	}
}
