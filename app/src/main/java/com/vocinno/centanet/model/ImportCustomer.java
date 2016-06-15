package com.vocinno.centanet.model;

public class ImportCustomer {
	private String pkid;//id
	private String importSrc;//数据来源
	private String phone;//手机号码
	private long importTime;//导入时间

	public String getPkid() {
		return pkid;
	}

	public void setPkid(String pkid) {
		this.pkid = pkid;
	}

	public String getImportSrc() {
		return importSrc;
	}

	public void setImportSrc(String importSrc) {
		this.importSrc = importSrc;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public long getImportTime() {
		return importTime;
	}

	public void setImportTime(long importTime) {
		this.importTime = importTime;
	}
}
