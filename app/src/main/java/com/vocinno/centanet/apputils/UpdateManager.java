package com.vocinno.centanet.apputils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vocinno.centanet.R;

/*import org.ksoap2.serialization.SoapObject;*/


/**
 * APP更新
 * 
 * @author 
 * 
 */
@SuppressLint({ "HandlerLeak", "SdCardPath" })
public class UpdateManager{
	private Context context;
	private String versionName;
	Map<String,String> parameters;
	ProgressDialog dialog;

	// 返回的安装包url是
	private String apkUrl =context.getString(R.string.download_url);

	private Dialog downloadDialog;
	/* 下载包安装路径 */
	private static final String savePath = "/sdcard/updatedemo/";

	private static final String saveFileName = savePath + "centanet_Release.apk";

	/* 进度条与通知ui刷新的handler和msg常量 */
	private ProgressBar mProgress;
	private TextView tv_progress;

	private static final int DOWN_UPDATE = 1;

	private static final int DOWN_OVER = 2;

	private int progress;

	private Thread downLoadThread;

	private boolean interceptFlag = false;

	private String json;
	private String sign;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				tv_progress.setText(progress + "");
				break;
			case DOWN_OVER:
				installApk();
				break;
			default:
				break;
			}
		};
	};

	public UpdateManager(Context context, String versionName) {
		this.context = context;
		this.versionName = versionName;

	}
	public UpdateManager(Context context) {
		this.context = context;
	}
	/*// 外部接口让主Activity调用
	public void checkUpdateInfo() {
		parameters=new HashMap<String,String>();
		parameters.put("itype", Constant.QUERRYVERSONNAME);
		parameters.put("version", "android");
		dialog=new ProgressDialog(context);
		dialog.show();
		new Thread(new Runnable() {			
			@Override
			public void run() {
				String result=HttpUtil.sendHttpPost("", parameters, 119, context);
				Message msg = handler.obtainMessage();
				msg.what = MessageCode.UPDATECLIENT;
				if(result==null||"".equals(result)){
					return;
				}else{
					JSONObject obj=null;
					try {
						obj=new JSONObject(result);
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(null!=obj){
					msg.obj = obj.optString("object");
					}
					msg.sendToTarget();
				}
			}
		}).start();
		
	}*/


	/*
	 * 获取服务器返回数据
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			String result = (String) msg.obj;
			Log.i("TEST", "更新JSON---" + result);
			if(result==null||"".equals(result)){
				return;
			}
			JSONObject object = null;
			if (!"".equals(object)) {
				try {
					object = new JSONObject(result);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			dialog.dismiss();
			switch (msg.what) {
			case MessageCode.UPDATECLIENT:
				if (object != null) {
						apkUrl=object.optString("url");
						Log.i("TEST", "新版本名---------->"+" "+object.optString("version")+"老版本名字-----》"+versionName);
						if (!versionName.equals(object.optString("version"))) {
							showNoticeDialog();
						}
				}
			}
		};
	};

	/**
	 * 更新APP
	 */
	/*private void showNoticeDialog() {
		MyDialog dialog = null;
		MyDialog.Builder builder = new MyDialog.Builder(context);
		builder.setTitle("到途生活馆版本更新");
		builder.setMessage("有最新版本哦，亲赶快下载吧~");
		builder.setPositiveButton("立即下载",
				new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Utils.getbitBitmapUtils(context).clearCache();
						Utils.getbitBitmapUtils(context).clearDiskCache();
						Utils.getbitBitmapUtils(context).clearMemoryCache();
						showDownloadDialog();
					}
				});

		builder.setNegativeButton("以后再说",
				new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		if (dialog == null) {
			dialog = builder.create();
		}
		if (!dialog.isShowing()) {
			dialog.show();
		}
	}*/
	/**
	 * 下载进度条
	 */
	public void showDownloadDialog() {
		Builder builder = new Builder(context);
		builder.setTitle("软件版本更新");
		final LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.progress_update_client, null);
		mProgress = (ProgressBar) v.findViewById(R.id.progress);
		tv_progress = (TextView) v.findViewById(R.id.tv_progress);
		tv_progress.setText("0");
		builder.setView(v);
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				interceptFlag = true;
			}
		});
		downloadDialog = builder.create();
		downloadDialog.setCancelable(false);
		downloadDialog.show();
		downloadApk();
	}

	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				URL url = new URL(apkUrl);

				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdir();
				} else {
					file.delete();
				}
				String apkFile = saveFileName;
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);

				int count = 0;
				byte buf[] = new byte[1024];

				do {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					// 更新进度
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						downloadDialog.dismiss();
						// 下载完成通知安装
						mHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);// 点击取消就停止下载.
				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};
	/**
	 * 下载apk
	 * 
	 * @param url	 */

	private void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}


	/**
	 * 安装apk
	 * 
	 * @param url
	 */
	private void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");

		context.startActivity(i);

	}

}
