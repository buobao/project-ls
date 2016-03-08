package com.vocinno.centanet.model;

public class HouseInMap {
	private String name = "";
	private String att = "";
	private String lat = "";
	private String count = "";
	private String entityId = "";

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public String getAtt() {
		return att;
	}

	public void setAtt(String att) {
		this.att = att;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

}
