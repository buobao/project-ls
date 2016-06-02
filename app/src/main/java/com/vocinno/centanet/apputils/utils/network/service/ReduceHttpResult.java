package com.vocinno.centanet.apputils.utils.network.service;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;

public class ReduceHttpResult {

	/**
	 * 对服务器返回数据进行处理
	 * 
	 * @param data
	 * @param url
	 * @param t
	 */
	public static <T> void preduceBefore(IObserverCallback callback,
			String data, String url, Class<T> t) {
		Gson gson = new Gson();
		ServiceHandleResultVO resultVO = null;
		String TAG = "BaseUrlCallback";
		Log.d(TAG, "result data is: \n" + data);
		Object result = null;
		try {
			resultVO = gson.fromJson(data, ServiceHandleResultVO.class);
			if (resultVO != null
					&& resultVO.getStatus() == ErrorCode.ERROR_CODE_CORRECT) {
				// 如果返回是正确的
				JSONObject json = new JSONObject(data);
				JSONObject obj = new JSONObject();
				if (resultVO.getResultData() != null) {
					obj = (JSONObject) json.get("resultData");
					result = gson.fromJson(obj.toString(), t);
				}
				resultVO.setResultData(result);
			} else {
				// handleErrorResult(resultVO);
			}
		} catch (JSONException e) {
			Log.d(TAG, "json error");
			e.printStackTrace();
		} catch (Exception e) {
			Log.d(TAG, "uncatched exception");
			Log.d(TAG, "error message is: " + e.getMessage());
		}

		callback.resultCallback(
				resultVO.getStatus() == ErrorCode.ERROR_CODE_CORRECT,
				resultVO.getResultData(), (String) resultVO.getErrorMessage(),
				resultVO.getStatus());
		return;
	}
}
