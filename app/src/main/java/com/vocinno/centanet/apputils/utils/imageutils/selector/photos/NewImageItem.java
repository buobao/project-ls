package com.vocinno.centanet.apputils.utils.imageutils.selector.photos;

import java.io.Serializable;

/**
 * 一个图片对象
 * 
 * @author Administrator
 * 
 */
public class NewImageItem implements Serializable {
	public String imageId;
	public String thumbnailPath;
	public String imagePath;
	public boolean isSelected = false;
	public   Long lastModified;
	private String time;
//	public String getTime() {
//		return time;
//	}
//	public void setTime(String time) {
//		this.time = time;
//	}
	public Long getLastModified() {
		return lastModified;
	}
	public void setLastModified(Long lastModified) {
		this.lastModified = lastModified;
	}
}
