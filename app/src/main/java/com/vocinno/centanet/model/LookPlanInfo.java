package com.vocinno.centanet.model;

/**
 * Created by Administrator on 2016/5/5.
 */
public class LookPlanInfo{
    private String CUST_CODE;
    private String HOUSE_ID;
    private String PLAN_DIRECTION;
    private String endtime;
    private String plan_date;
    private String starttime;

    private String addr;
    private String delCode;
    private String floor = "";// 楼层
    private boolean ishidden;
    private String building_name;
    public String getCUST_CODE() {
        return CUST_CODE;
    }

    public String getAddr() {
        return addr;
    }

    public String getBuilding_name() {
        return building_name;
    }

    public void setBuilding_name(String building_name) {
        this.building_name = building_name;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getDelCode() {
        return delCode;
    }

    public void setDelCode(String delCode) {
        this.delCode = delCode;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public boolean ishidden() {
        return ishidden;
    }

    public void setIshidden(boolean ishidden) {
        this.ishidden = ishidden;
    }

    public void setCUST_CODE(String CUST_CODE) {
        this.CUST_CODE = CUST_CODE;
    }

    public String getHOUSE_ID() {
        return HOUSE_ID;
    }

    public void setHOUSE_ID(String HOUSE_ID) {
        this.HOUSE_ID = HOUSE_ID;
    }

    public String getPLAN_DIRECTION() {
        return PLAN_DIRECTION;
    }

    public void setPLAN_DIRECTION(String PLAN_DIRECTION) {
        this.PLAN_DIRECTION = PLAN_DIRECTION;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getPlan_date() {
        return plan_date;
    }

    public void setPlan_date(String plan_date) {
        this.plan_date = plan_date;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }
}
	/* "CUST_CODE": "DM23052",
		"HOUSE_ID": 112246,
		"PLAN_DIRECTION": "周女士",
		"endtime": "15:30:00",
		"plan_date": "2016-05-06",
		"starttime": "11:40:00"*/
