package com.vocinno.centanet.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class KeyHouseItem implements Serializable {
	private String delCode = "";// 房源编号
	private String img ;// 图片
	private String addr = "";// 地址
	private String frame = "";// 户型
	private String square = "";// 面积
	private String floor = "";// 楼层
	private String orient = "";// 朝向
	private String price = "";// 价格
	private String unitprice = "";// 单价
	private String tag = "";// 标签
	private String activeTime = "";// 最近活动时间
	private String delDate = "";// 挂牌时间
	private String lat = "";// 经度
	private String att = "";// 纬度
	private String keyStatus = "";// 钥匙在店状态
	private String keyUser = "";// 当前借用人姓名
	private String keyUserPhone = "";// 当前借用人电话
	private String keyCount = "-1";
	private int isHD;
	private boolean ishidden;
	private String building_name;
	private String delegationType;//10015001  售  10015002  租
	public static final String SHOU="10015001";
	public static final String ZU="10015002";

	private String planDirection;//   客户名字
	private String startDate;//   开始时间
	private String endDate;//  结束时间
	private String planDate;//约看日期
	private String rmdCustTime;//约看提醒时间
	private String custCode;//   客户编号
	private List<LookPlanInfo> lookplanHouseList = new ArrayList<LookPlanInfo>();

	// private Image prieviewImg = new Image();
	public String getDelegationType() {
		return delegationType;
	}

	public void setDelegationType(String delegationType) {
		this.delegationType = delegationType;
	}

	public int getIsHD() {
		return isHD;
	}

	public String getDelDate() {
		return delDate;
	}

	public void setDelDate(String delDate) {
		this.delDate = delDate;
	}

	public void setIsHD(int isHD) {
		this.isHD = isHD;
	}

	public List<LookPlanInfo> getLookInfo() {
		return lookplanHouseList;
	}

	public void setLookInfo(List<LookPlanInfo>lookInfo) {
		this.lookplanHouseList = lookInfo;
	}

	public String getRmdCustTime() {
		return rmdCustTime;
	}

	public void setRmdCustTime(String rmdCustTime) {
		this.rmdCustTime = rmdCustTime;
	}

	public String getDelCode() {
		return delCode;
	}

	public String getUnitprice() {
		return unitprice;
	}

	public void setUnitprice(String unitprice) {
		this.unitprice = unitprice;
	}

	public String getPlanDirection() {
		return planDirection;
	}

	public void setPlanDirection(String planDirection) {
		this.planDirection = planDirection;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getPlanDate() {
		return planDate;
	}

	public void setPlanDate(String planDate) {
		this.planDate = planDate;
	}

	public String getCustCode() {
		return custCode;
	}

	public void setCustCode(String custCode) {
		this.custCode = custCode;
	}

	public String getKeyStatus() {
		return keyStatus;
	}

	public void setKeyStatus(String keyStatus) {
		this.keyStatus = keyStatus;
	}

	public String getBuilding_name() {
		return building_name;
	}

	public void setBuilding_name(String building_name) {
		this.building_name = building_name;
	}

	public boolean ishidden() {
		return ishidden;
	}

	public void setIshidden(boolean ishidden) {
		this.ishidden = ishidden;
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

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getKeyCount() {
		return keyCount;
	}

	public void setKeyCount(String keyCount) {
		this.keyCount = keyCount;
	}
}
