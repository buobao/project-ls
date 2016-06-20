package com.vocinno.centanet.entity;

import java.math.BigDecimal;
import java.util.List;

/**
 * 带看房源表
 * T_CM_LOOK_HOUSE
 *
 * @author yumin1  字段添加备注
 */

public class TCmLookHouse {

	/**
	 * PKID
	 */
	private Long pkid;

	/**
	 * 房源委托编号
	 */
	private String housedelCode;

	/**
	 * 房屋ID
	 */
	private Long houseId;

	/**
	 * 带看ID
	 */
	private Long lookId;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 客户反馈
	 */
	private String feedback;

	/**
	 * 录入人
	 */
	private String createdBy;

	/**
	 * 录入时间
	 */
	private String createdTime;

	/**
	 * 客户报价
	 */
	private BigDecimal price;

	/**
	 * 客户底价
	 */
	private BigDecimal priceFloor;

	/**
	 * 委托类型
	 */
	private String delegationType;

	/**
	 * 带看详情页显示 房源地址
	 */
	private String houAddr;

	/**
	 * 带看房源详情页 陪看人姓名
	 */
	private String accompanyName;
	/**
	 * 图片列表id
	 */
	private String filesId;
	/***
	 * 图片路径
	 */
	private List<String> imgList;

	public Long getPkid() {
		return pkid;
	}

	public void setPkid(Long pkid) {
		this.pkid = pkid;
	}

	public String getHousedelCode() {
		return housedelCode;
	}

	public void setHousedelCode(String housedelCode) {
		this.housedelCode = housedelCode == null ? null : housedelCode.trim();
	}

	public Long getHouseId() {
		return houseId;
	}

	public void setHouseId(Long houseId) {
		this.houseId = houseId;
	}

	public Long getLookId() {
		return lookId;
	}

	public void setLookId(Long lookId) {
		this.lookId = lookId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public List<String> getImgList() {
		return imgList;
	}

	public void setImgList(List<String> imgList) {
		this.imgList = imgList;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback == null ? null : feedback.trim();
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy == null ? null : createdBy.trim();
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getPriceFloor() {
		return priceFloor;
	}

	public void setPriceFloor(BigDecimal priceFloor) {
		this.priceFloor = priceFloor;
	}

	public String getDelegationType() {
		return delegationType;
	}

	public void setDelegationType(String delegationType) {
		this.delegationType = delegationType;
	}

	public String getHouAddr() {
		return houAddr;
	}

	public void setHouAddr(String houAddr) {
		this.houAddr = houAddr;
	}

	public String getAccompanyName() {
		return accompanyName;
	}

	public void setAccompanyName(String accompanyName) {
		this.accompanyName = accompanyName;
	}

	public String getFilesId() {
		return filesId;
	}

	public void setFilesId(String filesId) {
		this.filesId = filesId;
	}

}