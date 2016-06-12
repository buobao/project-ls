package com.vocinno.centanet.apputils.cst;

public class ImageForJsParams {
	public static String PIC_TYPE_HOUSE = "100531001";//户型图
	public static String PIC_TYPE_ROOM = "100531002";//室
	public static String PIC_TYPE_OFFICE = "100531003";//厅
	public static String PIC_TYPE_KITCHEN = "100531004";//厨
	public static String PIC_TYPE_TOILET = "100531005";//卫
	public static String PIC_TYPE_YANGTAI = "100531006";//阳台
	public static String PIC_TYPE_WAIJING = "100531007";//外景图
	public static String PIC_TYPE_OTHER = "100531008";//其它

	private String pic = "";
	private String type = "";
//	private String desc = "";

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

/*	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}*/

}
