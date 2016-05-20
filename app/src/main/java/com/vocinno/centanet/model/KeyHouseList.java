package com.vocinno.centanet.model;

import java.util.ArrayList;
import java.util.List;

public class KeyHouseList {
	private String listType = "";// {附近房源列表，我的房源列表，约看房源列表}
	private String houseListType;// 房源列表类型{出售，出租，约看，我的，公房}
	private String page = ""; // 当前页码
	private int total = 0; // 一共多少页
	private int records = 0; // 共多少条记录
	private String deltype = ""; // 原值返回

	public String getHouseListType() {
		return houseListType;
	}

	public void setHouseListType(String houseListType) {
		this.houseListType = houseListType;
	}

	public String getDeltype() {
		return deltype;
	}

	public void setDeltype(String deltype) {
		this.deltype = deltype;
	}

	private List<KeyHouseItem> rows = new ArrayList<KeyHouseItem>();

	public void setRows(List<KeyHouseItem> rows) {
		this.rows = rows;
	}

	public List<KeyHouseItem> getRows() {
		return rows;
	}

	public void setListType(String listType) {
		this.listType = listType;
	}

	public String getListType() {
		return listType;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getRecords() {
		return records;
	}

	public void setRecords(int records) {
		this.records = records;
	}

}
