package com.vocinno.centanet.entity;

/**
 * 带看陪看记录表
 * T_CM_LOOK_ACCOMPANY
 * @author yumin1  字段添加备注
 */
public class TCmLookAccompany {

    /**
     * PKID
     */
    private Long   pkid;

    /**
     * 带看房源ID
     */
    private Long   lookHouseId;

    /**
     * 陪看人角色
     */
    private String accompanyRole;

    /**
     * 陪看信息
     */
    private String accompanyInfo;

    /**
     * 店经理评价
     */
    private String appraisal;

    /**
     * 陪看人
     */
    private String accompanyUser;

    /**
     * 陪看人姓名
     */
    private String accompanyName;

    /**
     * 陪看人店组
     */
    private String accompanyGroup;

    /**
     * 承诺证书
     */
    private String accompanyPromise;

    public Long getPkid() {
        return pkid;
    }

    public void setPkid(Long pkid) {
        this.pkid = pkid;
    }

    public Long getLookHouseId() {
        return lookHouseId;
    }

    public void setLookHouseId(Long lookHouseId) {
        this.lookHouseId = lookHouseId;
    }

    public String getAccompanyRole() {
        return accompanyRole;
    }

    public void setAccompanyRole(String accompanyRole) {
        this.accompanyRole = accompanyRole == null ? null : accompanyRole.trim();
    }

    public String getAccompanyInfo() {
        return accompanyInfo;
    }

    public void setAccompanyInfo(String accompanyInfo) {
        this.accompanyInfo = accompanyInfo == null ? null : accompanyInfo.trim();
    }

    public String getAppraisal() {
        return appraisal;
    }

    public void setAppraisal(String appraisal) {
        this.appraisal = appraisal == null ? null : appraisal.trim();
    }

    public String getAccompanyUser() {
        return accompanyUser;
    }

    public void setAccompanyUser(String accompanyUser) {
        this.accompanyUser = accompanyUser == null ? null : accompanyUser.trim();
    }

    public String getAccompanyName() {
        return accompanyName;
    }

    public void setAccompanyName(String accompanyName) {
        this.accompanyName = accompanyName == null ? null : accompanyName.trim();
    }

    public String getAccompanyGroup() {
        return accompanyGroup;
    }

    public void setAccompanyGroup(String accompanyGroup) {
        this.accompanyGroup = accompanyGroup == null ? null : accompanyGroup.trim();
    }

    /**
     * Getter method for property <tt>accompanyPromise</tt>.
     * 
     * @return property value of accompanyPromise
     */
    public String getAccompanyPromise() {
        return accompanyPromise;
    }

    /**
     * Setter method for property <tt>accompanyPromise</tt>.
     * 
     * @param accompanyPromise value to be assigned to property accompanyPromise
     */
    public void setAccompanyPromise(String accompanyPromise) {
        this.accompanyPromise = accompanyPromise;
    }

}