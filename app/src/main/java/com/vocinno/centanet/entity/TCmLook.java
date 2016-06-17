package com.vocinno.centanet.entity;
/**
 * 带看信息表
 * T_CM_LOOK
 * @author yumin1  字段添加备注
 */
public class TCmLook {
	
	/**
	 * 主键
	 */
    private Long   pkid; 
    
    /**
	 * 客户编码
	 */
    private String custCode;    
    
    /**
	 * 带看确认书编号
	 */
    private String confirmationNumber; 
    
    /**
	 * 带看开始时间
	 */
    private String   startTime;
    
    /**
	 * 带看结束时间
	 */
    private String   endTime;
    
    /**
	 * 描述
	 */
    private String remark;   
    
    /**
	 * 带看人
	 */
    private String leadlookBy;   
    
    /**
	 * 带看人姓名
	 */
    private String leadlookName;  
    
    /**
	 * 带看店组
	 */
    private String groupId;     
    
    /**
	 * 店组名称
	 */
    private String storeGroupName; 
   
    /**
	 * 录入人
	 */
    private String createdBy;     
    
    /**
	 * 录入时间
	 */
    private String   createdTime;
    
    /**
	 * 服务过程ID
	 */
    private Long   srvactId; 
    
    private String lookType;
    
    public Long getPkid() {
        return pkid;
    }

    public void setPkid(Long pkid) {
        this.pkid = pkid;
    }

    public String getCustCode() {
        return custCode;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode == null ? null : custCode.trim();
    }

    public String getConfirmationNumber() {
        return confirmationNumber;
    }

    public void setConfirmationNumber(String confirmationNumber) {
        this.confirmationNumber = confirmationNumber == null ? null : confirmationNumber.trim();
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getLeadlookBy() {
        return leadlookBy;
    }

    public void setLeadlookBy(String leadlookBy) {
        this.leadlookBy = leadlookBy == null ? null : leadlookBy.trim();
    }

    public String getLeadlookName() {
        return leadlookName;
    }

    public void setLeadlookName(String leadlookName) {
        this.leadlookName = leadlookName == null ? null : leadlookName.trim();
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId == null ? null : groupId.trim();
    }

    public String getStoreGroupName() {
        return storeGroupName;
    }

    public void setStoreGroupName(String storeGroupName) {
        this.storeGroupName = storeGroupName == null ? null : storeGroupName.trim();
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

    public Long getSrvactId() {
        return srvactId;
    }

    public void setSrvactId(Long srvactId) {
        this.srvactId = srvactId;
    }

	public String getLookType() {
		return lookType;
	}

	public void setLookType(String lookType) {
		this.lookType = lookType;
	}
    
}