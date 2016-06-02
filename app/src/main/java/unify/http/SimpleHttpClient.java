package unify.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import cz.msebera.android.httpclient.HttpEntity;

public class SimpleHttpClient {

	private static AsyncHttpClient _client = new AsyncHttpClient();
	
	public static void get(String url, String data, AsyncHttpResponseHandler responseHandler) {
		_client.get(url, responseHandler);
	}

	public static void delete(String url, String data, AsyncHttpResponseHandler responseHandler) {
		_client.delete(url, responseHandler);
	}
	
	public static void post(String url, String postData, AsyncHttpResponseHandler responseHandler) {
		postWithForm(url, postData, responseHandler);
	}
	
	private static void postWithForm(final String url, String postData, AsyncHttpResponseHandler responseHandler) {
		RequestParams params = getParamsByJsonString(postData);
		_client.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		_client.post(url, params, responseHandler);
	}
	
	public static void put(final String url, String putData, AsyncHttpResponseHandler responseHandler) {
		putWithForm(url, putData, responseHandler);
	}
	
	private static void putWithForm(final String url, String postData, AsyncHttpResponseHandler responseHandler) {
		RequestParams params = getParamsByJsonString(postData);
		_client.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		_client.put(url, params, responseHandler);
	}
	
	private static RequestParams getParamsByJsonString(String data) {
		RequestParams params = new RequestParams();
		try {
			JSONObject jsonObject = new JSONObject(data);
			Iterator<?> it = jsonObject.keys();
			String key = "";
			String value = "";
			while (it.hasNext()) {
				key = it.next().toString();
				value = jsonObject.getString(key);
				params.add(key, value);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return params;
	}
	
	private static JSONObject getJsonByJsonString(String data) {
		JSONObject jsonObj = new JSONObject();
		try {
			JSONObject jsonObject = new JSONObject(data);
			Iterator<?> it = jsonObject.keys();
			String key = "";
			String value = "";
			while (it.hasNext()) {
				key = it.next().toString();
				value = jsonObject.getString(key);
				jsonObj.put(key, value);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObj;
	}
}
