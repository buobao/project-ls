package com.vocinno.centanet.apputils.cst;

public class ImageForJsParams {
	public static String PIC_TYPE_HOUSE = "100531001";
	public static String PIC_TYPE_ROOM = "100531002";
	public static String PIC_TYPE_LIVINGROOMT = "100531003";
	public static String PIC_TYPE_TOILET = "100531005";
	public static String PIC_TYPE_BALCONY = "100531006";
	public static String PIC_TYPE_OTHER = "100531008";

	private String pic = "";
	private String type = "";
	private String desc = "";

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
