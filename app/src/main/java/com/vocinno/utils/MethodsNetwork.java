package com.vocinno.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;

import com.vocinno.utils.media.video.VideoPlayerActivity;
import com.vocinno.utils.network.service.AnsynHttpRequest;
import com.vocinno.utils.network.service.IObserverCallback;
import com.vocinno.utils.web.webview.WebviewActivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;

public final class MethodsNetwork {
	// 网络状态
	public static int netWorkType = 1; // 0=无网络 1=wifi 2=3G 3=2G

	/**
	 * 采用线程更新网络状态(网络状态使用广播机制进行自动监听)
	 * 
	 * @param context
	 */
	public static Thread refreshAPNType(final Context context) {
		Thread mThread = new Thread(new Runnable() {
			@Override
			public void run() {
				netWorkType = getAPNType(context);
			}
		});
		mThread.start();
		return mThread;
	}

	/**
	 * 初次启动的时候调用(用于初始化网络状态)
	 * 
	 * @param context
	 */
	public static void refreshAPNTypeInMainThread(Context context) {
		netWorkType = getAPNType(context);
	}

	/**
	 * 获取当前的网络状态 ：没有网络0：WIFI网络1：3G网络2：2G网络3
	 * 
	 * @param context
	 * @return
	 */
	private static int getAPNType(Context context) {
		int netType = 0;
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = 1;// wifi
		} else if (nType == ConnectivityManager.TYPE_MOBILE) {
			int nSubType = networkInfo.getSubtype();
			TelephonyManager mTelephony = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
					&& !mTelephony.isNetworkRoaming()) {
				netType = 2;// 3G
			} else {
				netType = 3;// 2G
			}
		}
		return netType;
	}

	/**
	 * 打开系统网络设置
	 * 
	 * @param activity
	 */
	public static void startSystemSetting(Activity act) {
		Intent intent = null;
		try {
			String sdkVersion = android.os.Build.VERSION.SDK;
			if (Integer.valueOf(sdkVersion) > 10) {
				intent = new Intent(
						android.provider.Settings.ACTION_WIRELESS_SETTINGS);
			} else {
				intent = new Intent();
				ComponentName comp = new ComponentName("com.android.settings",
						"com.android.settings.WirelessSettings");
				intent.setComponent(comp);
				intent.setAction("android.intent.action.VIEW");
			}
			act.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 请求服务器接口，通过回调接收json格式数据并转换为clazz对象
	 * 
	 * @param context
	 * @param callback
	 *            基于BaseUrlCallback类的回调对象，里面封装有回调方法getResult()
	 * @param strUrl
	 *            接口url 注意此url需预先定义在CST_URL类中
	 * @param map
	 *            用于传递的参数
	 * @param clazz
	 *            接收的对象model
	 * @return
	 */
	public static boolean postHttpService(Context context,
			IObserverCallback callback, String strUrl, Map<String, String> map,
			Class<?> clazz) {
		if (MethodsNetwork.netWorkType == 0) {
			MethodsExtra.toast(context, null);
			return false;
		}
		AnsynHttpRequest.requestByPost(context, callback, strUrl, map, clazz);
		return true;
	}

	/**
	 * 获取经纬度：[经度，纬度] 首先要开启经纬度标记类：LocationUtil.startGetLocation(context);
	 * 
	 * @return
	 */
	public static double[] getLocationJingWeiDu() {
		double[] jingWeiDu = new double[2];
		jingWeiDu[0] = LocationUtil.getLongitude();
		jingWeiDu[1] = LocationUtil.getLatitude();
		return jingWeiDu;
	}

	/**
	 * 调用系统浏览器打开网页
	 * 
	 * @param activity
	 * @param str
	 */
	public static void openWeb(Activity activity, String str) {
		Uri uri = Uri.parse(str);
		activity.startActivity(new Intent(Intent.ACTION_VIEW, uri));
	}

	/**
	 * 应用内打开网页
	 * 
	 * @param activity
	 * @param strUrl
	 */
	public static void openWebView(Activity activity, String strUrl) {
		MethodsDeliverData.currentWebViewURL = strUrl;
		activity.startActivity(new Intent(activity, WebviewActivity.class));
	}

	/**
	 * 应用内打开视频
	 * 
	 * @param activity
	 * @param strUrl
	 */
	public static void openVideo(Activity activity, String strUrl) {
		MethodsDeliverData.currentVideoURL = strUrl;
		activity.startActivity(new Intent(activity, VideoPlayerActivity.class));
	}

	/**
	 * 获取指定URL页面的HTML格式代码
	 * 
	 * @param strUrl
	 * @return
	 */
	public static String getHtmlFromUrl(String strUrl) {
		try {
			// 根据http协议，要访问服务器上的某个网页，必须先建立一个连接，连接的参数为一个URL
			URL url = new URL(strUrl);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			// 然后, //设置请求方式为GET方式，就是相当于浏览器打开百度网页
			connection.setRequestMethod("GET");
			// 接着设置超时时间为5秒，5秒内若连接不上，则通知用户网络连接有问题

			connection.setReadTimeout(3500);
			// 若连接上之后，得到网络的输入流，内容就是网页源码的字节码
			InputStream inStream = connection.getInputStream();
			// 必须将其转换为字符串才能够正确的显示给用户
			ByteArrayOutputStream data = new ByteArrayOutputStream();// 新建一字节数组输出流
			byte[] buffer = new byte[1024];// 在内存中开辟一段缓冲区，接受网络输入流
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				data.write(buffer, 0, len);// 缓冲区满了之后将缓冲区的内容写到输出流
			}
			inStream.close();
			return new String(data.toByteArray(), "utf-8");// 最后可以将得到的输出流转成utf-8编码的字符串，便可进一步处理
		} catch (Exception e) {
			return "";
		}
	}

	public static void sendEmail(Activity activity, String strSubject,
			String strContent, File file) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.putExtra("subject", strSubject); //
		intent.putExtra("body", strContent); // 正文
		intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file)); // 添加附件，附件为file对象
		if (file.getName().endsWith(".gz")) {
			intent.setType("application/x-gzip"); // 如果是gz使用gzip的mime
		} else if (file.getName().endsWith(".txt")) {
			intent.setType("text/plain"); // 纯文本则用text/plain的mime
		} else {
			intent.setType("application/octet-stream"); // 其他的均使用流当做二进制数据来发送
		}
		activity.startActivity(intent); // 调用系统的mail客户端进行发送
	}

	public static void sendEMailNoFile(Activity activity, String toName,
			String subject, String content) {
		Uri uri = Uri.parse("mailto:" + toName);
		Intent it = new Intent(Intent.ACTION_SENDTO, uri);
		it.putExtra(Intent.EXTRA_SUBJECT, subject);
		it.putExtra(Intent.EXTRA_TEXT, content);
		activity.startActivity(Intent.createChooser(it, "Choose Email Client"));
	}

	public static String getIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (Exception e) {
		}
		return "127.0.0.1";
	}

}
