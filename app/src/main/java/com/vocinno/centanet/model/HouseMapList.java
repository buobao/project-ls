package com.vocinno.centanet.model;

import java.util.ArrayList;

public class HouseMapList {
	private ArrayList<HouseInMap> mapPoint = new ArrayList<HouseInMap>();

	private String type = "";

	public ArrayList<HouseInMap> getMapPoint() {
		return mapPoint;
	}

	public void setMapPoint(ArrayList<HouseInMap> mapPoint) {
		this.mapPoint = mapPoint;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
