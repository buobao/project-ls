package com.vocinno.utils.network.service;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import com.vocinno.utils.MethodsData;
import com.vocinno.utils.MethodsNetwork;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AnsynHttpRequest {

	public static final String HttpRequest_Intent_Action = "com.bengjiujie.vocinno.httprequest.doComplete";

	public static final long REQUEST_TIMEOUT = 30 * MethodsData.Time_One_Second;
	public static final long SO_TIMEOUT = 30 * MethodsData.Time_One_Second;
	static final int POST = 1; // post请求
	static final int GET = 2; // get请求

	static String tag = AnsynHttpRequest.class.getSimpleName();

	public static DefaultHttpClient mHttpClient;

	public static <T> void requestByGet(Context context,
			final IObserverCallback callBack, String httpUrl, int urlId,
			Map<String, String> map, Class<T> t) {

		StringBuffer buffer = new StringBuffer();
		buffer.append(httpUrl).append("?");
		if (map != null && map.size() > 0) {
			String value = "";
			if (map.containsKey("start")) {
				if (map.get("start").equals("0")) {

				}
				value = "start=" + map.get("start") + "&";
			}
			for (String key : map.keySet()) {
				if (key.equals("start")) {
					continue;
				}
				try {
					buffer.append(key)
							.append("=")
							.append(URLEncoder.encode(map.get(key), HTTP.UTF_8))
							.append("&");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			buffer.append(value);
		}
		String requestUrl = buffer.toString();
		requestUrl = requestUrl.substring(0, requestUrl.length() - 1);
		Log.i("AnsynHttpRequest - httpurl, requestByGet, requestUrl:",
				requestUrl);

		doAsynRequest(GET, null, context, callBack, requestUrl, t);
	}

	private static <T> void doAsynRequest(final int sendType,
			final Map<String, String> map, final Context context,
			final IObserverCallback callBack, final String url, Class<T> t) {

		try {
			ThreadPoolUtils.execute(new MyRunnable(sendType, map, context,
					callBack, url, t));
			// new MyRunnable(sendType,
			// map, context,
			// callBack, url,
			// intUrl, t).run();ThreadPoolUtils.java
		} catch (Exception e) {
			Log.d(HttpRequest_Intent_Action, e.toString());
		}

	}

	public static <T> void requestByPost(Context context,
			final IObserverCallback callBack, String httpUrl,
			Map<String, String> map, Class<T> t) {

		StringBuffer buffer = new StringBuffer();
		buffer.append(httpUrl);

		String requestUrl = buffer.toString();
		Log.i("AnsynHttpRequest - requestByPost, httpurl: ", requestUrl);
		doAsynRequest(POST, map, context, callBack, requestUrl, t);
	}

	/**
	 * 上传文件到服务器
	 * 
	 * @Title: uploadByGet
	 * @param @param context
	 * @param @param callBack
	 * @param @param httpUrl
	 * @param @param urlId
	 * @param @param map
	 * @param @param t
	 * @param @param filePath
	 * @return void 返回类型
	 * @throws
	 * @date 2015-3-23 下午3:08:57
	 * @version V2.0
	 */
	public static <T> void uploadByGet(Context context,
			final IObserverCallback callBack, String httpUrl, int urlId,
			Map<String, String> map, Class<T> t, String filePath,
			String fileName) {
		ThreadPoolUtils.execute(new UploadRunnable<T>(map, context, callBack,
				httpUrl, urlId, t, filePath, fileName));
		// new UploadRunnable<T>(map, context, callBack, httpUrl, urlId, t,
		// filePath).run();
	}

	public static <T> void requestByPost(Context context, String http_head,
			final IObserverCallback callBack, String httpUrl, int urlId,
			Map<String, String> map, Class<T> t) {

		StringBuffer buffer = new StringBuffer();
		if (http_head != null)
			buffer.append(http_head);
		else
			buffer.append(httpUrl);

		String requestUrl = buffer.toString();
		Log.i("AnsynHttpRequest - requestByPost, httpurl:",
				requestUrl + map.toString());

		if (MethodsNetwork.netWorkType == 0) {
			// sendBroadcastReceiverMessage(context,
			// R.string.network_show_connectionless);
			ReduceHttpResult.preduceBefore(callBack, null, httpUrl, t);
			return;
		}
		doAsynRequest(POST, map, context, callBack, requestUrl, t);
	}

	/**
	 * @param msg
	 */
	public static void sendBroadcastReceiverMessage(Context context, int msg) {
		Intent intent = new Intent(HttpRequest_Intent_Action);
		intent.putExtra("msg", msg);
		context.sendBroadcast(intent);
	}
}

/**
 * @author Jason
 * @param <T>
 */
class MyRunnable<T> implements Runnable {

	final Class<T> t;
	final int sendType;
	final Map<String, String> map;
	final Context context;
	final IObserverCallback callBack;
	final String url;

	public MyRunnable(final int sendType, final Map<String, String> map,
			final Context context, final IObserverCallback callBack,
			final String url, Class<T> t) {
		this.sendType = sendType;
		this.map = map;
		this.context = context;
		this.callBack = callBack;
		this.url = url;
		this.t = t;

	}

	@Override
	public void run() {
		String data = null;
		try {
			BasicHttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					(int) AnsynHttpRequest.REQUEST_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParams,
					(int) AnsynHttpRequest.SO_TIMEOUT);
			if (AnsynHttpRequest.mHttpClient == null) {
				DefaultHttpClient client = new DefaultHttpClient(httpParams);
				ClientConnectionManager mgr = client.getConnectionManager();
				HttpParams params = client.getParams();
				client = new DefaultHttpClient(new ThreadSafeClientConnManager(
						params, mgr.getSchemeRegistry()), params);
				AnsynHttpRequest.mHttpClient = client;
			}

			HttpResponse response = null;
			switch (sendType) {
			case AnsynHttpRequest.GET: // get ��ʽ�ύ
				HttpGet get = new HttpGet(url);
				response = AnsynHttpRequest.mHttpClient.execute(get);
				break;
			case AnsynHttpRequest.POST: // post ��ʽ�ύ
				HttpPost post = new HttpPost(url);
				Log.i("httpurl", url + map.toString());
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				if (map != null && map.size() > 0)
					for (String key : map.keySet()) {
						params.add(new BasicNameValuePair(key, map.get(key)));
					}
				HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
				post.setEntity(entity);
				response = AnsynHttpRequest.mHttpClient.execute(post);
				break;
			default:
				break;
			}

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				data = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
			} else {
				data = null;
			}
		} catch (Exception e) {
			Log.e("AsyncHttpRequest", e.toString());
			data = null;
		}

		if (callBack != null) {
			ReduceHttpResult.preduceBefore(callBack, data, url, t);
		}
	}
}

class UploadRunnable<T> implements Runnable {

	final Class<T> t;
	final Map<String, String> map;
	final Context context;
	final IObserverCallback callBack;
	final String url;
	final int intUrl;
	final String filePath;
	final String fileName;

	public UploadRunnable(final Map<String, String> map, final Context context,
			final IObserverCallback callBack, final String url,
			final int intUrl, Class<T> t, final String filePath,
			final String fileName) {
		this.map = map;
		this.context = context;
		this.callBack = callBack;
		this.url = url;
		this.intUrl = intUrl;
		this.t = t;
		this.filePath = filePath;
		this.fileName = fileName;
	}

	@Override
	public void run() {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		String response = "";
		String params = "";

		try {

			Iterator<String> iter = map.keySet().iterator();
			int iterCount = 0;
			while (iter.hasNext()) {
				String key = iter.next();
				String value = map.get(key);
				if (iterCount == 0)
					params = URLEncoder.encode(key, HTTP.UTF_8) + "="
							+ URLEncoder.encode(value, HTTP.UTF_8);
				else
					params = params + "&" + URLEncoder.encode(key, HTTP.UTF_8)
							+ "=" + URLEncoder.encode(value, HTTP.UTF_8);
				iterCount++;
			}

			// 添加后台需要的标记
			params = params + "&webPost=1";

			URL realUrl = new URL(url + "?" + params);
			Log.i("httpurl", realUrl.toString());
			HttpURLConnection con = (HttpURLConnection) realUrl
					.openConnection();

			// 设置不实用cache
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);

			con.setRequestMethod("GET");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; "
					+ "name=\"1\";filename=\"" + fileName + "\"" + end);
			ds.writeBytes(end);

			FileInputStream fStream = new FileInputStream(filePath);
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			/* 从文件读取数据至缓冲区 */
			while ((length = fStream.read(buffer)) != -1) {
				/* 将资料写入DataOutputStream中 */
				ds.write(buffer, 0, length);
			}
			ds.writeBytes(end);
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			/* close streams */
			fStream.close();
			ds.flush();

			InputStream is = con.getInputStream();
			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}

			ds.close();

			// 上传成功
			response = new String(b.toString().getBytes(), "UTF-8");
			if (callBack != null) {
				ReduceHttpResult.preduceBefore(callBack, response, url, t);
			}
		} catch (Exception e) {
			Log.d("AsyncHttpClient", "upload failed. " + " failed message is:"
					+ e.toString() + "\n\n" + "file is " + filePath + "\n\n"
					+ "url is " + url + "?" + params);
		}
	}

}
