package com.vocinno.centanet.apputils.utils.model;

import java.util.ArrayList;
//房源列表数据模型
public class HouseListDetailModel {
	private int mTotalPrice;			//房屋总价格
	private String mHouseAdress;		//房屋地址
	private String mHouseInfo;			//房屋具体信息
	private int mHousePerPrice;			//每平米价格
	private String mImagePath;			//房源列表item的图片
	private String mUpdateTime;			//更新日期
	private Boolean mIsKeyInStore;		//钥匙是否在店中
	private ArrayList<String> mTags;
	
	public void setTotalPrice(int price){
		this.mTotalPrice = price;
	}
	
	public int getTotalPrice(){
		return this.mTotalPrice;
	}
	
	public void setHouseAdress(String adress){
		this.mHouseAdress = adress;
	}
	
	public String getHouseAdress(){
		return this.mHouseAdress;
	}
	
	public void setHouseInfo(String houseInfo){
		this.mHouseInfo = houseInfo;
	}
	
	public String getHouseInfo(){
		return this.mHouseInfo;
	}
	
	public void setHousePerPrice(int perPrice){
		this.mHousePerPrice = perPrice;
	}
	
	public int getHousePerPrice(){
		return this.mHousePerPrice;
	}
	
	public void setUpdateTime(String time){
		this.mUpdateTime = time;
	}
	
	public String getUpdateTime(){
		return this.mUpdateTime;
	}
	
	public void setImagePath(String imagePath){
		this.mImagePath = imagePath;
	}
	
	public String getImagePath(){
		return this.mImagePath;
	}
	
	public void setTags(ArrayList<String> array){
		this.mTags = array;
	}
	
	public ArrayList<String> getTags(){
		return this.mTags;
	}
	
	public void setIsKey(Boolean isKey){
		this.mIsKeyInStore = isKey;
	}
	
	public Boolean getIsKey(){
		return this.mIsKeyInStore;
	}
}
