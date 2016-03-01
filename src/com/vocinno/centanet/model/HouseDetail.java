package com.vocinno.centanet.model;

import java.util.ArrayList;
import java.util.List;

public class HouseDetail {
	private String delCode = "";// 房源编号
	private String delDate = "";
	private String area = "";
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
	private boolean isPublic = false;// 是否是公房
	private String year = "";

	private List<Exclude> exclude = new ArrayList<Exclude>();
	private List<Track> track = new ArrayList<Track>();
	private List<Image> img = new ArrayList<Image>();

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public String getDelCode() {
		return delCode;
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

	public String getUnitprice() {
		return unitprice;
	}

	public void setUnitprice(String unitprice) {
		this.unitprice = unitprice;
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

	public String getDelDate() {
		return delDate;
	}

	public void setDelDate(String delDate) {
		this.delDate = delDate;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public List<Exclude> getExclude() {
		return exclude;
	}

	public void setExclude(List<Exclude> exclude) {
		this.exclude = exclude;
	}

	public List<Track> getTrack() {
		return track;
	}

	public void setTrack(List<Track> track) {
		this.track = track;
	}

	public List<Image> getImg() {
		return img;
	}

	public void setImg(List<Image> img) {
		this.img = img;
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

}
