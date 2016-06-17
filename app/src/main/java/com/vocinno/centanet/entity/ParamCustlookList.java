/**
 * AISThink.com Inc.
 * Copyright (c) 2015-2015 All Rights Reserved.
 */
package com.vocinno.centanet.entity;

import java.util.List;


/**
 * 约看带看 
 * @author yumin1
 */
public class ParamCustlookList {

    /**
     * 带看
     */
    private TCmLook                tCmLook;

    /**
     * 带看房屋信息
     */
    private List<TCmLookHouse>     tCmLookHouseList;

    /**
     * 带看陪看记录
     */
    private List<TCmLookAccompany> tCmLookAccompanyList;

    /**
     * 房源价格
    */
//    private List<THmPrice>         tHmPriceList;

    /**
     * 委托类型
     */
    private String                 delegationType;

    /**
     * 跟进类别
     * 选中checkbox=1时，插入房源跟进，反之插入带看跟进
     * @return
     */
    private String                 custlookTrackType;

    /**
     * 约看编号
     */
    private String                 custLookPlanId;

    /**
     * Getter method for property <tt>custLookPlanId</tt>.
     * 
     * @return property value of custLookPlanId
     */
    public String getCustLookPlanId() {
        return custLookPlanId;
    }

    /**
     * Setter method for property <tt>custLookPlanId</tt>.
     * 
     * @param custLookPlanId value to be assigned to property custLookPlanId
     */
    public void setCustLookPlanId(String custLookPlanId) {
        this.custLookPlanId = custLookPlanId;
    }

    public TCmLook gettCmLook() {
        return tCmLook;
    }

    public void settCmLook(TCmLook tCmLook) {
        this.tCmLook = tCmLook;
    }

    public List<TCmLookHouse> gettCmLookHouseList() {
        return tCmLookHouseList;
    }

    public void settCmLookHouseList(List<TCmLookHouse> tCmLookHouseList) {
        this.tCmLookHouseList = tCmLookHouseList;
    }

    public List<TCmLookAccompany> gettCmLookAccompanyList() {
        return tCmLookAccompanyList;
    }

    public void settCmLookAccompanyList(List<TCmLookAccompany> tCmLookAccompanyList) {
        this.tCmLookAccompanyList = tCmLookAccompanyList;
    }

//    public List<THmPrice> gettHmPriceList() {
//        return tHmPriceList;
//    }

//    public void settHmPriceList(List<THmPrice> tHmPriceList) {
//        this.tHmPriceList = tHmPriceList;
//    }

    public String getDelegationType() {
        return delegationType;
    }

    public void setDelegationType(String delegationType) {
        this.delegationType = delegationType;
    }

    public String getCustlookTrackType() {
        return custlookTrackType;
    }

    public void setCustlookTrackType(String custlookTrackType) {
        this.custlookTrackType = custlookTrackType;
    }

}
