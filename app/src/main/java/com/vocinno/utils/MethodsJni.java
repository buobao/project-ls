package com.vocinno.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.vocinno.centanet.apputils.AppInstance;
import com.vocinno.centanet.myinterface.HttpInterFace;

import org.unify.helper.CELibHelper;
import org.unify.helper.JsHelper;

import java.lang.reflect.Method;
import java.util.List;

public final class MethodsJni {
	private static HttpInterFace httpInterFace=null;
	/*public MethodsJni(HttpInterFace httpInterFace) {
		this.httpInterFace = httpInterFace;
	}*/
	public void setMethodsJni(HttpInterFace httpInterFace) {
		this.httpInterFace = httpInterFace;
	}
	/**
	 * 在使用jni之前调用，用于初始化jni
	 * 
	 * @param act
	 */
	public static void initJniLibrary(Context act) {
		System.loadLibrary("unifylib");
		CELibHelper.init(act);
		JsHelper.executeJsString("require('main');");
	}

	/**
	 * 调用JS的全局方法
	 * 
	 * @param functionName
	 *            函数名
	 * @param args
	 *            参数(对象数组)
	 */
	public static String callJSGlobalFun(final String functionName,
			final Object... args) {
		return (String) JsHelper.callGlobalFunction(functionName, args);
	}

	/**
	 * 调用方法
	 * 
	 * @param proxyName
	 * @param functionName
	 * @param args
	 */
	public static Object callProxyFun(String proxyName, String functionName,
			Object... args) {
		Log.d("wan", "wanggsx callProxyFun params:" + (String) args[0]);
		return JsHelper.callProxy(proxyName, functionName, args);
	}
	/**
	 * 通知回调
	 * 
	 * @param name
	 * @param className
	 * @param data
	 */
	public static void notificationCallBack(final String name,
			final String className, final Object data) {
		if(httpInterFace!=null){
			httpInterFace.netWorkResult(name,className,data);
			httpInterFace=null;
		}else{
			Log.d("tag", "tagwanggsx data:" + data + " name:" + name
					+ " className:" + className);
			if (data != null) {
				try {
					List<Activity> listActs = AppInstance.mListActivitys;
					Activity mActivity = listActs.get(listActs.size() - 1);
					if (mActivity.getClass().getName().equals(className)) {
						// 必须是当前的activity在显示状态才可以接收通知
						Method[] methods = mActivity.getClass()
								.getDeclaredMethods();
						for (Method md : methods) {
							if (md.getName().equals("notifCallBack")) {
								try {
									md.invoke(mActivity, name, className, data);
									break;
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 执行JS代码，参数为JS代码
	 * 
	 * @param source
	 * @return
	 */
	public static int executeJsString(final String source) {
		return JsHelper.executeJsString(source);
	}

	/**
	 * 执行js文件，参数为js的完整路径(apk和项目里的文件不能调用)
	 * 
	 * @param jsFile
	 * @return
	 */
	public static int executeJsFile(final String jsFile) {
		return executeJsFile(jsFile);
	}

	/**
	 * 添加通知
	 * 
	 * @param name
	 * @param observerClassName
	 */
	public static void addNotificationObserver(final String name,
			final String observerClassName) {
		JsHelper.addNotificationObserver(name, observerClassName);
	}

	// /**
	// * 添加多个通知
	// *
	// * @param names
	// * @param observerClassName
	// */
	// public static void addNotificationObservers(final String[] names,
	// final String observerClassName) {
	// JsHelper.addNotificationObservers(names, observerClassName);
	// }

	/**
	 * 移除通知
	 * 
	 * @param name
	 * @param observerClassName
	 */
	public static void removeNotificationObserver(final String name,
			final String observerClassName) {
		JsHelper.removeNotificationObserver(name, observerClassName);
	}

	/**
	 * 移除所有通知
	 * 
	 * @param observerClassName
	 */
	public static void removeAllNotifications(final String observerClassName) {
		JsHelper.removeAllNotifications(observerClassName);
	}

}
