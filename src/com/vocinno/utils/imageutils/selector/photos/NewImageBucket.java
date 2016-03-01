package com.vocinno.utils.imageutils.selector.photos;

import java.util.List;

/**
 * 一个目录的相册对象
 * 
 * @author Administrator
 * 
 */
public class NewImageBucket {
	public int count = 0;
	public String bucketName;
	public List<NewImageItem> imageList;
	
	public Long lastModified ;
	
	private String mName;

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

}
