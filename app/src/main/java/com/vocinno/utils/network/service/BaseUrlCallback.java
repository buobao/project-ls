package com.vocinno.utils.network.service;

/**
 * 网络请求的回调基类
 * 
 * @author Jason
 * 
 */
public class BaseUrlCallback implements IObserverCallback {

	@Override
	public void resultCallback(boolean isSuccess, Object rtnObj,
			String strErrorMsg, int errorCode) {

	}

	/**
	 * 有任何错误，需要自己处理
	 * 
	 * @param vo
	 */
	@Override
	public void handleErrorResult(ServiceHandleResultVO vo) {

	}

}
