package com.vocinno.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.vocinno.centanet.model.BorrowKey;
import com.vocinno.centanet.model.ContactDetail;
import com.vocinno.centanet.model.CustomerDetail;
import com.vocinno.centanet.model.CustomerList;
import com.vocinno.centanet.model.EstateSearchItem;
import com.vocinno.centanet.model.HouseDetail;
import com.vocinno.centanet.model.HouseList;
import com.vocinno.centanet.model.HouseMapList;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.model.KeyItem;
import com.vocinno.centanet.model.KeyList;
import com.vocinno.centanet.model.KeyReceiverInfo;
import com.vocinno.centanet.model.MessageItem;
import com.vocinno.centanet.model.Params;
import com.vocinno.centanet.model.PianQu;
import com.vocinno.centanet.model.UploadImageResult;

public final class MethodsJson {

	/**
	 * 解析通知过来的json字符串到JSReturn对象
	 * 
	 * @param <T>
	 * 
	 * @param <T>
	 * 
	 * @param <clazz>
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static JSReturn jsonToJsReturn(String strJson, Class clazz) {
		Log.d("tag", "jsonToJsReturn json:" + strJson);
		JSReturn jsReturn = new JSReturn();
		if (clazz == UploadImageResult.class) {
			List<UploadImageResult> listResults = new ArrayList<UploadImageResult>();
			List<Map<String, String>> listMaps = new Gson().fromJson(strJson,
					List.class);
			for (int i = 0; i < listMaps.size(); i++) {
				JSONObject jObject = new JSONObject(listMaps.get(i));
				UploadImageResult imageResult = new Gson().fromJson(
						jObject.toString(), UploadImageResult.class);
				listResults.add(imageResult);
			}
			jsReturn.setSuccess(true);
			jsReturn.setListDatas(listResults);
			return jsReturn;
		}

		try {
			JSONObject jsonObject = new JSONObject(strJson);
			jsReturn.setSuccess(jsonObject.getBoolean("isSuccess"));
			if (jsonObject.has("msg")) {
				jsReturn.setMsg(jsonObject.getString("msg"));
			}
			if (jsonObject.has("token")) {
				jsReturn.setToken(jsonObject.getString("token"));
			}
			if (jsonObject.has("empId")) {
				jsReturn.setEmpId(jsonObject.getString("empId"));
			}
			if (jsonObject.has("params")) {
				Params param = new Gson().fromJson(
						jsonObject.getString("params"), Params.class);
				jsReturn.setParams(param);
			}

			if (clazz == null || clazz == Object.class) {
				return jsReturn;
			}
			if (clazz == MessageItem.class) {
				List<Object> listTmp;
				List<MessageItem> list;
				listTmp = new Gson().fromJson(jsonObject.get("content")
						.toString(), List.class);
				list = new ArrayList<MessageItem>();
				for (int i = 0; i < listTmp.size(); i++) {
					LinkedTreeMap<String, ?> map = (LinkedTreeMap<String, ?>) listTmp
							.get(i);
					JSONObject jObject = new JSONObject(map);
					MessageItem item = new Gson().fromJson(jObject.toString(),
							MessageItem.class);
					list.add(item);
				}
				jsReturn.setListDatas(list);
			} else if (clazz == KeyList.class) {
				List<Object> listTmp;
				List<KeyItem> list;
				listTmp = new Gson().fromJson(jsonObject.get("content")
						.toString(), List.class);
				list = new ArrayList<KeyItem>();
				for (int i = 0; i < listTmp.size(); i++) {
					LinkedTreeMap<String, ?> map = (LinkedTreeMap<String, ?>) listTmp
							.get(i);
					JSONObject jObject = new JSONObject(map);
					KeyItem item = new Gson().fromJson(jObject.toString(),
							KeyItem.class);
					list.add(item);
				}
				jsReturn.setListDatas(list);
			} else if (clazz == PianQu.class) {
				List<Object> listTmp;
				List<PianQu> list;
				listTmp = new Gson().fromJson(jsonObject.get("content")
						.toString(), List.class);
				list = new ArrayList<PianQu>();
				for (int i = 0; i < listTmp.size(); i++) {
					LinkedTreeMap<String, ?> map = (LinkedTreeMap<String, ?>) listTmp
							.get(i);
					JSONObject jObject = new JSONObject(map);
					PianQu item = new Gson().fromJson(jObject.toString(),
							PianQu.class);
					list.add(item);
				}
				jsReturn.setListDatas(list);
			} else if (clazz == ContactDetail.class) {
				ContactDetail contactDetail = new Gson().fromJson(jsonObject
						.get("content").toString(), ContactDetail.class);
				jsReturn.setObject(contactDetail);
			} else if (clazz == EstateSearchItem.class) {
				List<EstateSearchItem> listSearchItems = new ArrayList<EstateSearchItem>();
				List<Map<String, String>> listMaps = new Gson().fromJson(
						jsonObject.get("content").toString(), List.class);
				for (int i = 0; i < listMaps.size(); i++) {
					JSONObject jObject = new JSONObject(listMaps.get(i));
					EstateSearchItem estateItem = new Gson().fromJson(
							jObject.toString(), EstateSearchItem.class);
					listSearchItems.add(estateItem);
				}
				jsReturn.setListDatas(listSearchItems);
			} else if (clazz == CustomerDetail.class) {
				CustomerDetail customerDetail = new Gson().fromJson(jsonObject
						.get("content").toString(), CustomerDetail.class);
				jsReturn.setObject(customerDetail);
			} else if (clazz == HouseDetail.class) {
				HouseDetail houseDetail = new Gson()
						.fromJson(jsonObject.get("content").toString(),
								HouseDetail.class);
				jsReturn.setObject(houseDetail);
			} else if (clazz == HouseMapList.class) {
				HouseMapList houseMapList = new Gson().fromJson(
						jsonObject.get("content").toString(),
						HouseMapList.class);
				jsReturn.setObject(houseMapList);
			} else if (clazz == BorrowKey.class) {
				BorrowKey borrowKey = new Gson().fromJson(
						jsonObject.get("content").toString(), BorrowKey.class);
				jsReturn.setObject(borrowKey);
			} else if (clazz == KeyReceiverInfo.class) {
				JSONArray jArray = jsonObject.getJSONArray("content");
				List<KeyReceiverInfo> list = new ArrayList<KeyReceiverInfo>();
				for (int i = 0; i < jArray.length(); i++) {
					KeyReceiverInfo kInfo = new Gson().fromJson(
							jArray.getString(i), KeyReceiverInfo.class);
					list.add(kInfo);
				}
				jsReturn.setListDatas(list);
			} else {
				JSONObject jsonObjectContent = (JSONObject) jsonObject
						.get("content");
				if (jsonObjectContent.has("page")) {
					jsReturn.setPage(jsonObjectContent.getString("page"));
				}
				if (jsonObjectContent.has("pageSize")) {
					jsReturn.setPageSize(jsonObjectContent
							.getString("pageSize"));
				}
				if (jsonObjectContent.has("total")) {
					jsReturn.setTotal(Integer.parseInt(jsonObjectContent
							.getString("total")));
				}
				if (jsonObjectContent.has("records")) {
					jsReturn.setRecords(Integer.parseInt(jsonObjectContent
							.getString("records")));
				}
				if (jsonObjectContent.has("listType")) {
					jsReturn.setListType(jsonObjectContent
							.getString("listType"));
				}
				if (jsonObjectContent.has("type")) {
					jsReturn.setType(jsonObjectContent.getString("type"));
				}
				if (jsonObjectContent.has("rows")) {
					Gson gson = new Gson();
					if (clazz == HouseList.class) {
						HouseList dataList = gson.fromJson(
								jsonObjectContent.toString(), clazz);
						jsReturn.setListDatas(dataList.getRows());
					} else if (clazz == CustomerList.class) {
						CustomerList dataList = gson.fromJson(
								jsonObjectContent.toString(), clazz);
						jsReturn.setListDatas(dataList.getRows());
					}
				}
			}
		} catch (Exception e) {
			try {
				System.err.println("cast 异常："
						+ new JSONObject(strJson).get("content"));
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
		return jsReturn;
	}

	/**
	 * 将json字符串转object
	 * 
	 * @param strJson
	 * @param clazz
	 * @return
	 */
	public static <T> T convJSONtoObject(String strJson, Class<T> clazz) {
		T tt0 = null;
		JSONObject jObject = null;
		try {
			jObject = new JSONObject(strJson);
			tt0 = clazz.newInstance();
			Field[] fds = clazz.getDeclaredFields();
			Method[] mds = clazz.getDeclaredMethods();
			for (int j = 0; j < mds.length; j++) {
				Method md = mds[j];
				if (md.getName().startsWith("set")) {
					// 调用每一个set方法
					String ignoCaseName = md.getName().substring(3)
							.toUpperCase();
					String realName = null;
					for (int k = 0; k < fds.length; k++) {
						String thisFdName = fds[k].getName();
						if (thisFdName.toUpperCase().equals(ignoCaseName)) {
							// 找到对应的参数
							if (jObject.has(thisFdName)) {
								md.invoke(tt0, jObject.get(thisFdName));
							}
							break;
						}
					}
				}
			}
		} catch (Exception e) {
		}
		return tt0;
	}

	/**
	 * 将JSONArray转List对象
	 * 
	 * @param jsonArray
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> convJSONArraytoList(JSONArray jsonArray,
			Class<T> clazz) {
		ArrayList<T> listDatas = new ArrayList<T>();
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				listDatas.add(convJSONtoObject(jsonArray.get(i).toString(),
						clazz));
			}
		} catch (Exception e) {
			return null;
		}
		return listDatas;
	}

}
