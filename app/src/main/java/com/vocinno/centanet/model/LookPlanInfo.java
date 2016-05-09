package com.vocinno.centanet.model;

/**
 * Created by Administrator on 2016/5/5.
 */
public class LookPlanInfo{
    private String planDirection;//   客户名字
    private String startDate;//   开始时间
    private String endDate;//  结束时间
    private String planDate;//约看日期
    private String rmdCustTime;//约看提醒时间
    private String custCode;//   客户编号

    private String BUILDING_NAME;
    private String ESTATE_NAME;
    private String HOUSE_DEL_CODE;
    private String HOUSE_ID;
    private String LOOKPLAN_ID;
    private String ROOM_NO;
    private String floor;
    private String totalFloor;



    public String getBUILDING_NAME() {
        return BUILDING_NAME;
    }

    public void setBUILDING_NAME(String BUILDING_NAME) {
        this.BUILDING_NAME = BUILDING_NAME;
    }

    public String getESTATE_NAME() {
        return ESTATE_NAME;
    }

    public void setESTATE_NAME(String ESTATE_NAME) {
        this.ESTATE_NAME = ESTATE_NAME;
    }

    public String getHOUSE_DEL_CODE() {
        return HOUSE_DEL_CODE;
    }

    public void setHOUSE_DEL_CODE(String HOUSE_DEL_CODE) {
        this.HOUSE_DEL_CODE = HOUSE_DEL_CODE;
    }

    public String getHOUSE_ID() {
        return HOUSE_ID;
    }

    public void setHOUSE_ID(String HOUSE_ID) {
        this.HOUSE_ID = HOUSE_ID;
    }

    public String getLOOKPLAN_ID() {
        return LOOKPLAN_ID;
    }

    public void setLOOKPLAN_ID(String LOOKPLAN_ID) {
        this.LOOKPLAN_ID = LOOKPLAN_ID;
    }

    public String getROOM_NO() {
        return ROOM_NO;
    }

    public void setROOM_NO(String ROOM_NO) {
        this.ROOM_NO = ROOM_NO;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getTotalFloor() {
        return totalFloor;
    }

    public void setTotalFloor(String totalFloor) {
        this.totalFloor = totalFloor;
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

    public String getRmdCustTime() {
        return rmdCustTime;
    }

    public void setRmdCustTime(String rmdCustTime) {
        this.rmdCustTime = rmdCustTime;
    }

    public String getCustCode() {
        return custCode;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }
/*"BUILDING_NAME": "1",
            "ESTATE_NAME": "新陆花园(四平路480弄)",
            "HOUSE_DEL_CODE": "SHHKS000007521",
            "HOUSE_ID": 112234,
            "LOOKPLAN_ID": 2238,
            "ROOM_NO": "0101",
            "floor": 1,
            "totalFloor": 24*/
}
	/* "CUST_CODE": "DM23052",
		"HOUSE_ID": 112246,
		"PLAN_DIRECTION": "周女士",
		"endtime": "15:30:00",
		"plan_date": "2016-05-06",
		"starttime": "11:40:00"*/
