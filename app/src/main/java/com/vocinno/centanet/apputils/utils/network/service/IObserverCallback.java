package com.vocinno.centanet.apputils.utils.network.service;

public interface IObserverCallback {

	public void resultCallback(boolean isSuccess, Object rtnObj,
			String strErrorMsg, int errorCode);

	public void handleErrorResult(ServiceHandleResultVO vo);
}
