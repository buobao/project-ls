package com.vocinno.centanet.model;

import java.util.ArrayList;
import java.util.List;

public class HouseDetail {
	private String delCode = "";// 房源编号
	private String houseId = "";// 房屋id
	private String delDate = "";
	private String area = "";
	private String addr = "";// 地址
	private String frame = "";// 户型
	private String square = "";// 面积
	private String floor = "";// 楼层
	private String buiding = "";// 写理由返回的栋
	private String buildingname= "";// 房源详情栋
	private String orient = "";// 朝向
	private String price = "";// 价格
	private String unitprice = "";// 单价
	private String tag = "";// 标签
	private String activeTime = "";// 最近活动时间
	private boolean showroom =false;// 是否显示楼层号
	private String roomNO = "";// 楼层号
	public String roomNo = "";// 楼层号
	private String lat = "";// 经度
	private String att = "";// 纬度
	private boolean isPublic = false;// 是否是公房
	private boolean isRequireReason = false;// 是否需要原因
	private boolean showroomInfo   = false;// 是否显示室号
	private boolean showroomBtn   = false;// 是否显示查看室号按钮
	private String year = "";
	private String delegationType;//10015001  售  10015002  租
	public String getRoomNO() {
		return roomNO;
	}

	public String getDelegationType() {
		return delegationType;
	}

	public void setDelegationType(String delegationType) {
		this.delegationType = delegationType;
	}

	public void setRoomNO(String roomNO) {
		this.roomNO = roomNO;
	}

	public boolean getShowroom() {
		return showroom;
	}

	public boolean isShowroomInfo() {
		return showroomInfo;
	}

	public void setShowroomInfo(boolean showroomInfo) {
		this.showroomInfo = showroomInfo;
	}

	public boolean isShowroomBtn() {
		return showroomBtn;
	}

	public void setShowroomBtn(boolean showroomBtn) {
		this.showroomBtn = showroomBtn;
	}

	public void setShowroom(boolean showroom) {
		this.showroom = showroom;
	}

	private List<Exclude> exclude = new ArrayList<Exclude>();
	private List<Track> track = new ArrayList<Track>();
	private List<Image> img = new ArrayList<Image>();

	public String getHouseId() {
		return houseId;
	}

	public void setHouseId(String houseId) {
		this.houseId = houseId;
	}

	public String getBuildingname() {
		return buildingname;
	}

	public void setBuildingname(String buildingname) {
		this.buildingname = buildingname;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public boolean isShowroom() {
		return showroom;
	}

	public boolean isRequireReason() {
		return isRequireReason;
	}

	public void setIsRequireReason(boolean isRequireReason) {
		this.isRequireReason = isRequireReason;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public String getBuiding() {
		return buiding;
	}

	public void setBuiding(String buiding) {
		this.buiding = buiding;
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
