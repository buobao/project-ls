package com.vocinno.centanet.model;

import java.util.ArrayList;
import java.util.List;

public class HouseItem {
	private String delCode = "";// 房源编号
	private List<Image> img = new ArrayList<Image>();// 图片
	private String addr = "";// 地址
	private String frame = "";// 户型
	private String square = "";// 面积
	private String floor = "";// 楼层
	private String orient = "";// 朝向
	private String price = "";// 价格
	private String unitprice = "";// 单价
	private String tag = "";// 标签
	private String activeTime = "";// 最近活动时间
	private String lat = "";// 经度
	private String att = "";// 纬度
	private String keyStatus = "";// 钥匙在店状态
	private String keyUser = "";// 当前借用人姓名
	private String keyUserPhone = "";// 当前借用人电话
	private String keyCount = "-1";

	// private Image prieviewImg = new Image();

	public String getDelCode() {
		return delCode;
	}

	public String getUnitprice() {
		return unitprice;
	}

	public void setUnitprice(String unitprice) {
		this.unitprice = unitprice;
	}

	public String getKeyStatus() {
		return keyStatus;
	}

	public void setKeyStatus(String keyStatus) {
		this.keyStatus = keyStatus;
	}

	public String getKeyUser() {
		return keyUser;
	}

	public void setKeyUser(String keyUser) {
		this.keyUser = keyUser;
	}

	public String getKeyUserPhone() {
		return keyUserPhone;
	}

	public void setKeyUserPhone(String keyUserPhone) {
		this.keyUserPhone = keyUserPhone;
	}

	public void setDelCode(String delCode) {
		this.delCode = delCode;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getFrame() {
		return frame;
	}

	public void setFrame(String frame) {
		this.frame = frame;
	}

	public String getSquare() {
		return square;
	}

	public void setSquare(String square) {
		this.square = square;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getOrient() {
		return orient;
	}

	public void setOrient(String orient) {
		this.orient = orient;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(String activeTime) {
		this.activeTime = activeTime;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getAtt() {
		return att;
	}

	public void setAtt(String att) {
		this.att = att;
	}

	public List<Image> getImg() {
		return img;
	}

	public void setImg(List<Image> img) {
		this.img = img;
	}

	public String getKeyCount() {
		return keyCount;
	}

	public void setKeyCount(String keyCount) {
		this.keyCount = keyCount;
	}

}
