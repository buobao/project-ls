package com.vocinno.centanet.model;

import java.io.Serializable;

public class Track implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String content = "";
	private String tracktime = "";

	private String msg = "";
	private String time = "";
	private String user = "";

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTracktime() {
		return tracktime;
	}

	public void setTracktime(String tracktime) {
		this.tracktime = tracktime;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

}
