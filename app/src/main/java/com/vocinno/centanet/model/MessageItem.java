package com.vocinno.centanet.model;

import com.vocinno.utils.MethodsData;

public class MessageItem {
	
	private String msgId="";		//消息Id
	private String msgType="";		//消息类型
	private String msgContent="";	//消息内容
	private long msgTime=0;		//消息时间
	
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getMsgContent() {
		return msgContent;
	}
	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
	public String getMsgTime() {
		
		return MethodsData.getFriendDateTime(msgTime);
	}
	public void setMsgTime(long msgTime) {
		this.msgTime = msgTime;
	}
	

}
