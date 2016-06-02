package com.vocinno.centanet.apputils.utils.media.sms;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

public class SmsManager {

	public enum SmsType {
		ALL, // 所有类型
		Inbox, // 收件箱
		Sent, // 已发送
		Draft, // 草稿
		Outbox, // 发件箱
		Failed, // 发送失败
		Queued; // 待发送列表
	};

	public enum SmsTypeURI {
		Inbox("content://sms/inbox"), // 收件箱
		Sent("content://sms/sent"), // 已发送
		Draft("content://sms/draft"), // 草稿
		Outbox("content://sms/outbox"), // 发件箱
		Failed("content://sms/failed"), // 发送失败
		Queued("content://sms/queued"); // 待发送列表
		String value;

		SmsTypeURI(String value) {
			this.value = value;
		}
	};

	public static void writeMessageInbox(Context ctxt, String phone,
			String content, SmsType type, SmsTypeURI typeUri, boolean isRead,
			Date date) {
		ContentValues values = new ContentValues();
		values.put("address", phone);// 发件人手机号码
		values.put("type", type + ""); // ALL=0,INBOX=1,SENT=2,DRAFT=3,OUTBOX=4,FAILED=5,QUEUED=6
		if (isRead) {
			values.put("read", "1"); // 0=未读，1=已读
		} else {
			values.put("read", "0"); // 0=未读，1=已读
		}
		values.put("body", content);// 短信内容
		values.put("date", date.getTime());// 发件日期，单位是milliseconds
		values.put("person", "");// 联系人列表里的序号，陌生人为null
		Uri uri = ctxt.getApplicationContext().getContentResolver()
				.insert(Uri.parse(typeUri.value), values);
	}

}
