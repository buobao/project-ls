package com.vocinno.utils;

import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.os.Bundle;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.utils.Log;

public final class MethodsThirdParty {
	// QQ QQ空间
	public static String appidQQ = "1104627611";
	public static String secretQQ = "RyPvbWgRprTZAIDT";
	// 微信
	public static String appidWX = "wx22bdefa5fa95a73a";
	public static String secretWX = "99ca62f664f6958a3548013e2030d516";
	// 新浪微博
	public static String appidSina = "";
	public static String secretSina = "";

	public static String strToken = null;
	public static String strUid = null;
	public static String strUserName = null;
	public static String strSex = null;
	public static int intAge = 0;

	/**
	 * 分享：友盟一键分享
	 * 
	 * @param act
	 * @param strContent
	 *            分享内容
	 * @param strImageUrl
	 *            图片网址
	 */
	public static void shareOut(Activity act, String strContent,
			String strImageUrl) {
		// final UMSocialService mController = UMServiceFactory
		// .getUMSocialService("com.umeng.share");
		// UMWXHandler wxHandler = new UMWXHandler(act, appidWX, secretWX);
		// wxHandler.setToCircle(true);
		// wxHandler.addToSocialSDK();
		//
		// QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(act, appidQQ,
		// secretQQ);
		// qZoneSsoHandler.addToSocialSDK();
		//
		// UMQQSsoHandler qHandler = new UMQQSsoHandler(act, appidQQ, secretQQ);
		// qHandler.addToSocialSDK();
		// // 设置分享内容
		// mController.setShareContent(strContent);
		// // 设置分享图片, 参数2为图片的url地址
		// mController.setShareMedia(new UMImage(act, strImageUrl));
		// mController.getConfig().removePlatform(SHARE_MEDIA.RENREN,
		// SHARE_MEDIA.DOUBAN);
		// mController.openShare(act, false);
	}

	/**
	 * 第三方登录：QQ登录
	 * 
	 * @param act
	 */
	public static void loginInFromQQ(final Activity act) {
		final UMSocialService mController = UMServiceFactory
				.getUMSocialService("com.umeng.login");
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(act, appidQQ, secretQQ);
		qqSsoHandler.addToSocialSDK();
		mController.doOauthVerify(act, SHARE_MEDIA.QQ, new UMAuthListener() {
			@Override
			public void onError(SocializeException e, SHARE_MEDIA platform) {
			}

			@Override
			public void onComplete(Bundle value, SHARE_MEDIA platform) {
				mController.getPlatformInfo(act, SHARE_MEDIA.QQ,
						new UMDataListener() {
							@Override
							public void onStart() {
							}

							@Override
							public void onComplete(int status,
									Map<String, Object> info) {
								if (status == 200 && info != null) {
									StringBuilder sb = new StringBuilder();
									Set<String> keys = info.keySet();
									for (String key : keys) {
										sb.append(key + "="
												+ info.get(key).toString()
												+ "\r\n");
									}
									Log.d("TestData", sb.toString());
								} else {
									Log.d("TestData", "发生错误：" + status);
								}
							}
						});
			}

			@Override
			public void onCancel(SHARE_MEDIA platform) {
			}

			@Override
			public void onStart(SHARE_MEDIA platform) {
			}
		});
	}

	/**
	 * 第三方登录：微信登录
	 * 
	 * @param act
	 */
	public static void loginInFromWX(final Activity act) {
		// final UMSocialService mController = UMServiceFactory
		// .getUMSocialService("com.umeng.login");
		// UMWXHandler qqSsoHandler = new UMWXHandler(act, appidWX, secretWX);
		// qqSsoHandler.addToSocialSDK();
		// mController.doOauthVerify(act, SHARE_MEDIA.WEIXIN,
		// new UMAuthListener() {
		// @Override
		// public void onError(SocializeException e,
		// SHARE_MEDIA platform) {
		// }
		//
		// @Override
		// public void onComplete(Bundle value, SHARE_MEDIA platform) {
		// mController.getPlatformInfo(act, SHARE_MEDIA.QQ,
		// new UMDataListener() {
		// @Override
		// public void onStart() {
		// }
		//
		// @Override
		// public void onComplete(int status,
		// Map<String, Object> info) {
		// if (status == 200 && info != null) {
		// StringBuilder sb = new StringBuilder();
		// Set<String> keys = info.keySet();
		// for (String key : keys) {
		// sb.append(key
		// + "="
		// + info.get(key)
		// .toString()
		// + "\r\n");
		// }
		// Log.d("TestData", sb.toString());
		// } else {
		// Log.d("TestData", "发生错误：" + status);
		// }
		// }
		// });
		// }
		//
		// @Override
		// public void onCancel(SHARE_MEDIA platform) {
		// }
		//
		// @Override
		// public void onStart(SHARE_MEDIA platform) {
		// }
		// });
	}

}
