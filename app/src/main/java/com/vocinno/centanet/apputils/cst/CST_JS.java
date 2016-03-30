package com.vocinno.centanet.apputils.cst;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vocinno.utils.MethodsData;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsNetwork;

public final class CST_JS {
	// notifications
	public static String NOTIFY_NATIVE_LOGIN_RESULT = "natify_native_login_result";
	// 搜索框的下拉选项
	public static String NOTIFY_NATIVE_SEARCH_ITEM_RESULT = "notify_native_search_estate_result";

	public static String NOTIFY_NATIVE_HOU_LIST_RESULT = "notify_native_get_houstlist_result";
	// 地图找房
	public static String NOTIFY_NATIVE_HOU_LIST_INMAP_RESULT = "notify_native_get_hou_InMap_result";
	// 搜索框下拉选项点击
	public static String NOTIFY_NATIVE_HOU_LIST_SEARCH_RESULT = "notify_native_get_hou_search_result";
	// 点击地图的楼盘
	public static String NOTIFY_NATIVE_HOU_LIST_CLICK_MAP_RESULT = "notify_native_get_hou_click_map_result";
	public static String NOTIFY_NATIVE_HOU_DETAIL_RESULT = "notify_native_house_detail_result";
	// 获取联系人列表
	public static String NOTIFY_NATIVE_CONTACT_LIST_RESULT = "notify_native_contact_list_result";
	public static String NOTIFY_NATIVE_TRACK_LIST_RESULT = "notify_native_track_list_result";
	// 抢公房
	public static String NOTIFY_NATIVE_CLAIM_HOUSE_RESULT = "notify_native_claim_house_result";
	// 获取分享链接
	public static String NOTIFY_NATIVE_GET_SHARE_LINK_RESULT = "notify_native_share_link_result";

	public static String NOTIFY_NATIVE_GET_CUSTOMER_LIST_RESULT = "notify_native_customerlist_result";
	public static String NOTIFY_NATIVE_GET_CUSTOMER_DETAIL_RESULT = "notify_native_customerinfo_result";
	public static String NOTIFY_NATIVE_ADD_CUSTOMER_RESULT = "notify_native_add_customer_result";
	public static String NOTIFY_NATIVE_CUST_TRACK_RESULT = "notify_native_cust_track_result";
	public static String NOTIFY_NATIVE_HOU_ADD_TRACK_RESULT = "notify_native_hou_add_track_result";
	// 抢公客
	public static String NOTIFY_NATIVE_CLAIM_CUSTOMER_RESULT = "notify_native_claim_customer_result";

	// 接收者：监听输入pin码的结果(返回结果成功，则跳转)
	public static String NOTIFY_NATIVE_CONFIRM_PINCODE = "notify_native_confirm_pincode_result";
	// 拥有者：等待更改头像
	public static String NOTIFY_NATIVE_PASS_CHECK = "notify_native_pass_check_result";
	// 接收者：在钥匙列表中等待更改状态为已完成
	public static String NOTIFY_NATIVE_PASS_CHECK_RECEIVER = "notify_native_pass_check_by_receiver_result";
	// 钥匙列表
	public static String NOTIFY_NATIVE_KEY_LIST_RESULT = "notify_native_key_list_result";
	// 拥有者：传递钥匙成功，需要关闭页面并删除钥匙列表中的当前行
	public static String NOTIFY_NATIVE_CONFIRM_KEY_TO_RECEIVER_RESULT = "notify_native_confirm_key_2_receiver";
	// 拥有者取消传递钥匙成功
	public static String NOTIFY_NATIVE_CALCEL_KEY_TO_RECEIVER = "notify_native_cancel_key_2_receiver";
	// 钥匙房源列表借钥匙的结果
	public static String NOTIFY_NATIVE_BORROW_KEY_FROM_SHOP_RESULT = "notify_native_borrow_key_from_shop_result";
	// 归还钥匙结果
	public static String NOTIFY_NATIVE_RETURN_KEY_RESULT = "notify_native_return_key_result";
	// 创建pin码
	public static String NOTIFY_NATIVE_SET_PINCODE_RESULT = "notify_native_set_pincode_result";

	public static String NOTIFY_NATIVE_MESSAGE_READ_RESULT = "notify_native_message_read_result";
	public static String NOTIFY_NATIVE_MESSAGE_LIST_RESULT = "notify_native_message_list_result";
	// 带看跟进
	public static String NOTIFY_NATIVE_ADD_HOU_CUST_TRACK_RESULT = "notify_native_hou_cust_track_result";
	// 片区
	public static String NOTIFY_NATIVE_GET_AREA_RESULT = "notify_native_get_area_result";
	//看房理由
	public static String NOTIFY_NATIVE_DOROOMVIEW_RESULT = "notify_native_doroomview_result";
	// 电话号码是否重复
	public static String NOTIFY_NATIVE_CHECK_PNONENO = "notify_native_check_pnoneno";
	public static String NOTIFY_NATIVE_UPLOAD_IMGS_RESULT = "notify_native_upload_imgs_result";

	// -----------------------------------公共参数------------------------------------------
	// 参数赋值(默认值)
	public static int JS_CommonValue_PageSize = 3;
	public static String JS_CommonParam_Page = "page";
	public static String JS_CommonParam_Pagesize = "pageSize";
	public static String JS_CommonParam_Type = "type";
	public static String jS_CommonParam_Name = "name";
	public static String jS_CommonParam_Content = "content";
	public static String jS_CommonParam_Att = "att";
	public static String jS_CommonParam_Lat = "lat";

	public static void addJingWeiDegree(JsonObject jsonObject) {
		jsonObject.addProperty(jS_CommonParam_Att,
				MethodsNetwork.getLocationJingWeiDu()[0]);
		jsonObject.addProperty(jS_CommonParam_Lat,
				MethodsNetwork.getLocationJingWeiDu()[1]);
	}

	// -----------------------------------公共参数------------------------------------------

	// -----------------------------------登录------------------------------------------
	// Proxy
	public static String JS_ProxyName_Login = "LoginProxy";

	// 参数定义
	public static String JS_Login_username = "username";
	public static String JS_Login_password = "password";

	// Function >>>>>>>>>>>>>>>>>>>>>>
	public static String JS_Function_Login_login = "login";

	// 请求参数
	public static String getJsonStringForLogin(String username, String password) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(JS_Login_username, username);
		jsonObject.addProperty(JS_Login_password, password);
		addJingWeiDegree(jsonObject);
		return jsonObject.toString();
	}

	// -----------------------------------登录------------------------------------------

	// -----------------------------------房源列表------------------------------------------
	// Proxy
	public static String JS_ProxyName_HouseResource = "HouseResourceProxy";
	// 参数定义
	// "type"; 1=出租，2=出售，3=预约，4=我的，5=公房
	public static String JS_HouseResource_Price = "price";
	public static String JS_HouseResource_Square = "square";
	public static String JS_HouseResource_Frame = "frame";
	public static String JS_HouseResource_Tag = "tag";
	public static String JS_HouseResource_UsageType = "usageType";
	public static String JS_HouseResource_Sidx = "sidx"; // acre/price
	public static String JS_HouseResource_Sord = "sord"; // asc/desc

	public static String JS_HouseResource_DelType = "delType";
	public static String JS_HouseResource_LatMin = "latMin";
	public static String JS_HouseResource_LatMax = "latMax";
	public static String JS_HouseResource_AttMin = "attMin";
	public static String JS_HouseResource_AttMax = "attMax";

	public static String JS_HouseResource_searchId = "searchId";
	public static String jS_HouseResource_searchType = "searchType";

	public static String JS_HouseResource_custCode = "custCode";
	public static String JS_HouseResource_lookCode = "lookCode";
	public static String JS_HouseResource_remark = "remark";

	public static String JS_HouseResource_url = "url";
	// Function >>>>>>>>>>>>>>>>>>>>>>
	public static String JS_Function_HouseResource_getList = "getHouseList";
	public static String JS_Function_HouseResource_getShiHao = "doRoomview";
	public static String JS_CommonParam_Heason = "reason";
	public static String JS_CommonParam_HelCode = "delCode";
	public static String JS_CommonParam_HouseId = "houseId";
	// 请求参数 发送查看房间原因
	public static String getJsonStringForLookShiHao(String reason,
													String delCode,
													String houseId,
													String type) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(JS_CommonParam_Heason, reason);
		jsonObject.addProperty(JS_CommonParam_HelCode, delCode);
		jsonObject.addProperty(JS_CommonParam_HouseId, houseId);
		jsonObject.addProperty(JS_CommonParam_Type, type);
		addJingWeiDegree(jsonObject);
		return jsonObject.toString();
	}
	// 请求参数 type:1 price square frame tag page pageSize sidx(acre/price)
	// sord(asc/desc)
	private static String zOrS="";
	public static void setZOrS(String param){
		zOrS=param;
	}
	public static String getJsonStringForHouseListGetList(String type,
														  String price, String square, String frame, String tag,
														  String userType, int page, int pagesize, String sidx, String sord,
														  String searchId, String searchType) {
		JsonObject jsonObject = new JsonObject();
		if(!"3".equals(type)){//预约不传delType
			jsonObject.addProperty(JS_HouseResource_DelType, zOrS);
		}
		jsonObject.addProperty(JS_CommonParam_Type, type);

		jsonObject.addProperty(JS_HouseResource_Price, price);
		jsonObject.addProperty(JS_HouseResource_Square, square);
		jsonObject.addProperty(JS_HouseResource_Frame, frame);
		jsonObject.addProperty(JS_HouseResource_Tag, tag);
		jsonObject.addProperty(JS_HouseResource_UsageType, userType);
		jsonObject.addProperty(JS_CommonParam_Page, page);
		jsonObject.addProperty(JS_CommonParam_Pagesize,
				pagesize >= 1 ? pagesize : JS_CommonValue_PageSize);
		jsonObject.addProperty(JS_HouseResource_Sidx, sidx);
		jsonObject.addProperty(JS_HouseResource_Sord, sord);
		jsonObject.addProperty(JS_HouseResource_searchId, searchId);
		jsonObject.addProperty(jS_HouseResource_searchType, searchType);
		addJingWeiDegree(jsonObject);
		return jsonObject.toString();
	}

	// Function >>>>>>>>>>>>>>>>>>>>>>
	public static String JS_Function_HouseResource_searchEstateName = "searchEstateName";

	// 请求参数 name page pageSize
	public static String getJsonStringForHouseListSearchEstateName(String name,
																   int page, int pagesize) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(jS_CommonParam_Name, name);
		jsonObject.addProperty(JS_CommonParam_Page, page);
		jsonObject.addProperty(JS_CommonParam_Pagesize,
				pagesize >= 1 ? pagesize : JS_CommonValue_PageSize);
		addJingWeiDegree(jsonObject);
		return jsonObject.toString();
	}

	// Function >>>>>>>>>>>>>>>>>>>>>>
	public static String JS_Function_HouseResource_getHouseInMap = "getHouseInMap";

	// 请求参数:delType latMin latMax attMin attMax
	public static String getJsonStringForHouseListGetHouseInMap(String delType,
																double latMin, double latMax, double attMin, double attMax) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(JS_HouseResource_DelType, delType);
		jsonObject.addProperty(JS_HouseResource_LatMin, latMin);
		jsonObject.addProperty(JS_HouseResource_LatMax, latMax);
		jsonObject.addProperty(JS_HouseResource_AttMin, attMin);
		jsonObject.addProperty(JS_HouseResource_AttMax, attMax);
		addJingWeiDegree(jsonObject);
		return jsonObject.toString();
	}

	// Function >>>>>>>>>>>>>>>>>>>>>>
	// public static String JS_Function_HouseResource_searchHouList =
	// "searchHouList";
	//
	// // 请求参数:page pageSize searchId searchType sidx sord
	// public static String getJsonStringForHouseListSearchHouList(int page,
	// int pagesize, int searchId, String searchType, String sidx,
	// String sord) {
	// JsonObject jsonObject = new JsonObject();
	// jsonObject.addProperty(JS_CommonParam_Page, page);
	// jsonObject.addProperty(JS_CommonParam_Pagesize,
	// pagesize >= 1 ? pagesize : JS_CommonValue_PageSize);
	// jsonObject.addProperty(JS_HouseResource_searchId, searchId);
	// jsonObject.addProperty(jS_HouseResource_searchType, searchType);
	// jsonObject.addProperty(JS_HouseResource_Sidx, sidx);
	// jsonObject.addProperty(JS_HouseResource_Sord, sord);
	// return jsonObject.toString();
	// }

	// Function >>>>>>>>>>>>>>>>>>>>>>
	public static String JS_Function_HouseResource_getMapHouseList = "getMapHouseList";

	// 请求参数:page pageSize searchId searchType
	public static String getJsonStringForHouseListGetMapHouseList(int page,
																  int pagesize, int searchId, String searchType) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(JS_CommonParam_Page, page);
		jsonObject.addProperty(JS_CommonParam_Pagesize,
				pagesize >= 1 ? pagesize : JS_CommonValue_PageSize);
		jsonObject.addProperty(JS_HouseResource_searchId, searchId);
		jsonObject.addProperty(jS_HouseResource_searchType, searchType);
		addJingWeiDegree(jsonObject);
		return jsonObject.toString();
	}

	// Function >>>>>>>>>>>>>>>>>>>>>>
	public static String JS_Function_HouseResource_getHouseDetail = "getHouseDetail";

	// 请求参数: delCode
	public static String getJsonStringForHouseListGetHouseDetail(String delCode) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(JS_KeyProxy_DelCode, delCode);
		addJingWeiDegree(jsonObject);
		return jsonObject.toString();
	}

	// Function >>>>>>>>>>>>>>>>>>>>>>
	public static String JS_Function_HouseResource_getContactList = "getContactList";

	// 请求参数:delCode
	public static String getJsonStringForGetContactList(String delCode) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(JS_KeyProxy_DelCode, delCode);
		addJingWeiDegree(jsonObject);
		return jsonObject.toString();
	}

	// Function >>>>>>>>>>>>>>>>>>>>>>
	public static String JS_Function_HouseResource_getTrackList = "getTrackList";

	// 请求参数:delCode page pageSize
	public static String getJsonStringForGetTrackList(String delCode, int page,
													  int pagesize) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(JS_KeyProxy_DelCode, delCode);
		jsonObject.addProperty(JS_CommonParam_Page, page);
		jsonObject.addProperty(JS_CommonParam_Pagesize,
				pagesize >= 1 ? pagesize : JS_CommonValue_PageSize);
		addJingWeiDegree(jsonObject);
		return jsonObject.toString();
	}

	// Function >>>>>>>>>>>>>>>>>>>>>>
	public static String JS_Function_HouseResource_claimHouse = "claimHouse";

	// 请求参数:delCode
	public static String getJsonStringForClaimHouse(String delCode) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(JS_KeyProxy_DelCode, delCode);
		addJingWeiDegree(jsonObject);
		return jsonObject.toString();
	}

	// Function >>>>>>>>>>>>>>>>>>>>>>
	public static String JS_Function_HouseResource_getShareLink = "getShareLink";

	// 请求参数:delCode
	public static String getJsonStringForGetShareLink(String delCode) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(JS_KeyProxy_DelCode, delCode);
		addJingWeiDegree(jsonObject);
		return jsonObject.toString();
	}

	// Function >>>>>>>>>>>>>>>>>>>>>>
	public static String JS_Function_HouseResource_addHouCustomerTrack = "addHouCustomerTrack";

	// 请求参数:delCode custCode lookCode remark
	public static String getJsonStringForAddHouCustomerTrack(String delCode,
															 String custCode, String lookCode, String remark) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(JS_KeyProxy_DelCode, delCode);
		jsonObject.addProperty(JS_HouseResource_custCode, custCode);
		jsonObject.addProperty(JS_HouseResource_lookCode, lookCode);
		jsonObject.addProperty(JS_HouseResource_remark, remark);
		addJingWeiDegree(jsonObject);
		return jsonObject.toString();
	}

	// Function >>>>>>>>>>>>>>>>>>>>>>
	public static String JS_Function_HouseResource_addHouTrack = "addHouTrack";

	//
	public static String getJsonStringForAddHouTrack(String delCode,
													 String content) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(JS_KeyProxy_DelCode, delCode);
		jsonObject.addProperty(jS_CommonParam_Content, content);
		addJingWeiDegree(jsonObject);
		return jsonObject.toString();
	}

	// Function >>>>>>>>>>>>>>>>>>>>>>
	public static String JS_Function_HouseResource_getHouseInfoByQRCode = "getHouseInfoByQRCode";

	// 请求参数:url
	public static String getJsonStringForGetHouseInfoByQRCode(String url) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(JS_HouseResource_url, url);
		addJingWeiDegree(jsonObject);
		return jsonObject.toString();
	}

	// Function >>>>>>>>>>>>>>>>>>>>>>
	public static String JS_Function_HouseResource_uploadImages = "uploadImages";

	// 请求参数: delCode, pics:[{pic, type, desc},{pic, type, desc}]
	public static String getJsonStringForUploadImages(String delCode,
													  ArrayList<ImageForJsParams> listImages) {
		return "{\"delCode\":\"" + delCode + "\",\"pics\":"
				+ new Gson().toJson(listImages) + "}";
	}

	// 片区
	// Function >>>>>>>>>>>>>>>>>>>>>>
	public static String JS_Function_HouseResource_getDistrictArray = "getAreaArray";// getAreaArray

	// 请求参数:districtCode
	public static String getJsonStringForGetAreaArray(String districtCode) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(JS_CustomerList_districtCode, districtCode);
		addJingWeiDegree(jsonObject);
		return jsonObject.toString();
	}

	// -----------------------------------房源列表------------------------------------------

	// -----------------------------------客源列表------------------------------------------
	// Proxy
	public static String JS_ProxyName_CustomerList = "CustomerProxy";

	// 参数定义

	public static String JS_CustomerList_Type_Public = "public";// 公客
	public static String JS_CustomerList_Type_My = "my";// 我的客源
	public static String JS_CustomerList_CustCode = "custCode";
	public static String JS_CustomerList_Phone = "phone";
	public static String JS_CustomerList_QQ = "qq";
	public static String JS_CustomerList_Wechat = "wechat";
	public static String JS_CustomerList_ReqType = "reqType";
	public static String JS_CustomerList_Acreage = "acreage";
	public static String JS_CustomerList_Area = "area";
	public static String JS_CustomerList_Other = "other";
	public static String JS_CustomerList_districtCode = "districtCode";

	// Function >>>>>>>>>>>>>>>>>>>>>>
	public static String JS_Function_CustomerList_getList = "getCustomerList";

	// 请求参数type page pageSize
	public static String getJsonStringForCustomerList(String type, int page,
													  int pagesize) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(JS_CommonParam_Type, type);
		jsonObject.addProperty(JS_CommonParam_Page, page);
		jsonObject.addProperty(JS_CommonParam_Pagesize,
				pagesize >= 1 ? pagesize : JS_CommonValue_PageSize);
		addJingWeiDegree(jsonObject);
		return jsonObject.toString();
	}

	// Function >>>>>>>>>>>>>>>>>>>>>>
	public static String JS_Function_CustomerList_getCustomerInfo = "getCustomerInfo";

	// 请求参数custCode
	public static String getJsonStringForGetCustomerInfo(String custCode) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(JS_CustomerList_CustCode, custCode);
		addJingWeiDegree(jsonObject);
		return jsonObject.toString();
	}

	// Function >>>>>>>>>>>>>>>>>>>>>>
	public static String JS_Function_CustomerList_claimCustomer = "claimCustomer";

	// 请求参数custCode
	public static String getJsonStringForClaimCustomer(String custCode) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(JS_CustomerList_CustCode, custCode);
		addJingWeiDegree(jsonObject);
		return jsonObject.toString();
	}

	// Function >>>>>>>>>>>>>>>>>>>>>>
	public static String JS_Function_CustomerList_addCustomer = "addCustomer";

	// 请求参数name phone qq wechat reqType(rent or buy) area acreage price other
	public static String getJsonStringForAddCustomer(String name, String phone,
													 String qq, String wechat, String reqType, String area,
													 String acreage, String price, String other) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(jS_CommonParam_Name, name);
		jsonObject.addProperty(JS_CustomerList_Phone, phone);
		jsonObject.addProperty(JS_CustomerList_QQ, qq);
		jsonObject.addProperty(JS_CustomerList_Wechat, wechat);
		jsonObject.addProperty(JS_CustomerList_ReqType, reqType);
		jsonObject.addProperty(JS_CustomerList_Acreage, acreage);
		jsonObject.addProperty(JS_CustomerList_Area, area);
		jsonObject.addProperty(JS_HouseResource_Price, price);
		jsonObject.addProperty(JS_CustomerList_Other, other);
		addJingWeiDegree(jsonObject);
		return jsonObject.toString();
	}

	public static String JS_Function_CustomerList_addCustomer_checkPhoneNORepeated = "checkPhoneNORepeated";

	// 请求参数name phone qq wechat reqType(rent or buy) area acreage price other
	public static String getJsonStringForCheckPhoneNORepeated(String phoneNO) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(JS_CustomerList_Phone, phoneNO);
		return jsonObject.toString();
	}


	// Function >>>>>>>>>>>>>>>>>>>>>>
	public static String JS_Function_CustomerList_addTrackInfo = "addTrackInfo";

	// 请求参数 custCode content
	public static String getJsonStringForAddTrackInfo(String custCode,
													  String content) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(JS_HouseResource_custCode, custCode);
		jsonObject.addProperty(jS_CommonParam_Content, content);
		addJingWeiDegree(jsonObject);
		return jsonObject.toString();
	}

	// -----------------------------------客源列表------------------------------------------

	// -----------------------------------钥匙列表------------------------------------------
	// Proxy
	public static String JS_ProxyName_KeyProxy = "KeyProxy";

	// 参数定义
	public static String JS_KeyProxy_PinCode = "pinCode";
	public static String JS_KeyProxy_KeyNum = "keyNum";
	public static String JS_KeyProxy_DelCode = "delCode";

	// Function >>>>>>>>>>>>>>>>>>>>>>
	public static String JS_Function_KeyProxy_confirmPincode = "confirmPincode";

	// 请求参数 pinCode
	public static String getJsonStringForConfirmPincode(String pinCode) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(JS_KeyProxy_PinCode, pinCode);
		addJingWeiDegree(jsonObject);
		return jsonObject.toString();
	}

	// Function >>>>>>>>>>>>>>>>>>>>>>
	public static String JS_Function_KeyProxy_checkKeyPass = "checkKeyPass";

	// 请求参数 pinCode type
	public static String getJsonStringForCheckKeyPass(String pinCode,
													  String type) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(JS_KeyProxy_PinCode, pinCode);
		jsonObject.addProperty(JS_CommonParam_Type, type);
		addJingWeiDegree(jsonObject);
		return jsonObject.toString();
	}

	// Function >>>>>>>>>>>>>>>>>>>>>>
	// 拥有者取消钥匙传递
	// public static String JS_Function_KeyProxy_cancelKeyPass =
	// "cancelKeyPass";
	//
	// // 请求参数 pinCode type
	// public static String getJsonStringForCancelKeyPass() {
	// JsonObject jsonObject = new JsonObject();
	// return jsonObject.toString();
	// }

	// Function >>>>>>>>>>>>>>>>>>>>>>
	public static String JS_Function_KeyProxy_getMyKeyList = "getMyKeyList";

	// 请求参数 pinCode type
	public static String getJsonStringForGetMyKeyList() {
		JsonObject jsonObject = new JsonObject();
		addJingWeiDegree(jsonObject);
		return jsonObject.toString();
	}

	// Function >>>>>>>>>>>>>>>>>>>>>>
	// 拥有者作传递钥匙操作
	public static String JS_Function_KeyProxy_confirmKeyPassToReceivert = "confirmKeyPassToReceiver";

	// 请求参数 keyNum
	public static String getJsonStringForconfirmKeyPassToReceiver(String keyNum) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(JS_KeyProxy_KeyNum, keyNum);
		addJingWeiDegree(jsonObject);
		return jsonObject.toString();
	}

	// Function >>>>>>>>>>>>>>>>>>>>>>
	// 拥有者取消传递钥匙操作
	public static String JS_Function_KeyProxy_cancelKeyPassToReceiver = "cancelKeyPassToReceiver";
	public static String JS_Function_KeyPproxy_cancelAll = "cancelKeyPass";

	// 请求参数 keyNum
	public static String getJsonStringForcancelKeyPassToReceiver(String keyNum) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(JS_KeyProxy_KeyNum, keyNum);
		addJingWeiDegree(jsonObject);
		return jsonObject.toString();
	}

	// Function >>>>>>>>>>>>>>>>>>>>>>
	// 从店里借用钥匙
	public static String JS_Function_KeyProxy_borrowKeyFromShop = "borrowKeyFromShop";

	// 请求参数 delCode
	public static String getJsonStringForborrowKeyFromShop(String delCode) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(JS_KeyProxy_DelCode, delCode);
		addJingWeiDegree(jsonObject);
		return jsonObject.toString();
	}

	// Function >>>>>>>>>>>>>>>>>>>>>>
	// 归还钥匙到店里
	public static String JS_Function_KeyProxy_returnKeyToShop = "returnKeyToShop";

	// 请求参数 keyNum
	public static String getJsonStringForReturnKeyToShop(String keyNum) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(JS_KeyProxy_KeyNum, keyNum);
		addJingWeiDegree(jsonObject);
		return jsonObject.toString();
	}

	// Function >>>>>>>>>>>>>>>>>>>>>>
	// 拥有者获取pin码
	public static String JS_Function_KeyProxy_getPinCodeByLocal = "getPinCode";

	// 请求参数 keyNum
	public static String getJsonStringForGetPinCodeByLocal(String keyNum) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(JS_KeyProxy_KeyNum, keyNum);
		addJingWeiDegree(jsonObject);
		return jsonObject.toString();
	}

	// Function >>>>>>>>>>>>>>>>>>>>>>
	public static String JS_Function_KeyProxy_setPinCode = "setPinCode";

	// 请求参数 keyNum pinCode
	public static String getJsonStringForSetPinCode(String keyNum,
													String pinCode) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(JS_KeyProxy_KeyNum, keyNum);
		jsonObject.addProperty(JS_KeyProxy_PinCode, pinCode);
		addJingWeiDegree(jsonObject);
		return jsonObject.toString();
	}

	// -----------------------------------钥匙列表------------------------------------------

	// -----------------------------------消息列表------------------------------------------
	// Proxy
	public static String JS_ProxyName_Message = "MessageProxy";

	// 参数定义
	public static String JS_Message_msgId = "msgId";
	public static String JS_Message_Type_old = "old";
	public static String JS_Message_Type_new = "new";

	// Function >>>>>>>>>>>>>>>>>>>>>>
	public static String JS_Function_MessageProxy_getMessageList = "getMessageList";

	// 请求参数 type(new or old) page pageSize
	public static String getJsonStringForGetMessageList(String type, int page,
														int pagesize) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(JS_CommonParam_Type, type);
		jsonObject.addProperty(JS_CommonParam_Page, page);
		jsonObject.addProperty(JS_CommonParam_Pagesize,
				pagesize >= 1 ? pagesize : JS_CommonValue_PageSize);
		addJingWeiDegree(jsonObject);
		return jsonObject.toString();
	}

	// Function >>>>>>>>>>>>>>>>>>>>>>
	public static String JS_Function_MessageProxy_setMessageRead = "setMessageRead";

	// 请求参数 msgId
	public static String getJsonStringForsetMessageRead(String msgId) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(JS_Message_msgId, msgId);
		addJingWeiDegree(jsonObject);
		return jsonObject.toString();
	}

	// --------------------------------消息列表------------------------------------------
}
