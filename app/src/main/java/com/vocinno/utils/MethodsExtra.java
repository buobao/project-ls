package com.vocinno.utils;

import java.io.File;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.Vector;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.vocinno.centanet.R;
import com.vocinno.centanet.StartActivity;
import com.vocinno.centanet.apputils.AppInstance;
import com.vocinno.centanet.apputils.AppOffOn;
import com.vocinno.centanet.apputils.dialog.ModelDialog;
import com.vocinno.utils.file.FileExplore;
import com.vocinno.utils.input.edittextdialog.InputEditTextDialog;
import com.vocinno.utils.input.edittextdialog.InputEditTextListener;
import com.vocinno.utils.media.audio.SoundPoolUtil;
import com.vocinno.utils.media.sms.SmsManager;
import com.vocinno.utils.media.sms.SmsManager.SmsType;
import com.vocinno.utils.media.sms.SmsManager.SmsTypeURI;
import com.vocinno.utils.timer.TimerCallback;
import com.vocinno.utils.timer.TimerManager;
import com.vocinno.utils.view.span.MyClickableSpan;

public final class MethodsExtra {

	/**
	 * 开启计时器执行任务
	 * 
	 * @param delay
	 *            多少毫秒后开始执行任务
	 * @param isRepeat
	 *            是否需要重复执行任务
	 * @param times
	 *            重复执行次数，默认无限次(0或-1均代表无限次)
	 * @param period
	 *            如果需要重复执行，则间隔多少毫秒执行一次任务
	 * 
	 * @param callback
	 *            执行任务需触发的回调
	 * @return
	 */
	public static Timer startTimer(long delay, final boolean isRepeat,
			final int times, long period, final TimerCallback timerCallback) {
		return TimerManager.startTimer(delay, isRepeat, times, period,
				timerCallback);
	}

	/**
	 * 添加通知
	 * 
	 * @param context
	 * @param title
	 * @param content
	 * @param activityClass
	 */
	public static void addNotification(Activity context, String title,
			String content, Class activityClass) {
		Intent intent = new Intent(context, activityClass);
		intent.putExtra("notification", 1);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		Notification notification = new NotificationCompat.Builder(context)
				.setContentTitle(title)
				.setContentText(content)
				.setSmallIcon(R.drawable.ic_launcher)
				.setLargeIcon(
						BitmapFactory.decodeResource(context.getResources(),
								R.drawable.ic_launcher))
				.setContentIntent(pendingIntent).setAutoCancel(true)
				.getNotification();
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(new Random().nextInt(), notification);
	}

	public static SpannableString getClickableSpanner(String strKeyWords,
			OnClickListener listener) {
		SpannableString span = new SpannableString(strKeyWords);
		span.setSpan(new MyClickableSpan(listener), 0, strKeyWords.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return span;
	}

	/**
	 * 打开qq与指定号码临时聊天的界面
	 * 
	 * @param act
	 * @param strQQ
	 */
	public static void openQQChat(Activity act, String strQQ) {
		String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + strQQ;
		act.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
	}
	

//	/**
//	 * 打开weixin与指定号码临时聊天的界面
//	 * 
//	 * @param act
//	 * @param strWx
//	 */
//	public static void openwechatChat(Activity act, String strWx) {
//		String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + strWx;
//		act.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
//	}

	/**
	 * 根据item来设置listview的高度
	 * 
	 * @param listView
	 */
	public static void resetListHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	/**
	 * 重新设置listView的高度(用于解决listView嵌套问题,此方法适用于每一行高度相等的情况)
	 * 
	 * @param context
	 * @param listView
	 * @param itemHeightDip
	 *            每一行的高度(固定值)
	 * @param itemCount
	 *            总共有多少行
	 */
	public static void resetListHeightWithFixItemHeight(Context context,
			ListView listView, int itemHeightDip, int itemCount) {
		int itemHeightPx = MethodsData.dip2px(context, itemHeightDip);
		LinearLayout.LayoutParams params = (LayoutParams) listView
				.getLayoutParams();
		if (params == null) {
			params = new LinearLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					itemHeightPx * itemCount);
		} else {
			params.height = itemHeightPx * itemCount;
		}
		listView.setLayoutParams(params);
	}

	/**
	 * 显示文本输入对话框，完成后回调
	 * 
	 * @param context
	 * @param preContent
	 * @param sendDialogTextListener实现回调接口
	 */
	public static void showEditTextInputDialog(Context context,
			String preContent, InputEditTextListener sendDialogTextListener) {
		InputEditTextDialog dialog = new InputEditTextDialog(context,
				preContent, sendDialogTextListener);
		dialog.show();
	}

	/**
	 * 打印debug类型的日志
	 * 
	 * @param tag
	 * @param message
	 */
	public static void logDebug(String tag, String message) {
		if (AppOffOn.mIsOnLog) {
			Log.d(tag, message);
		}
	}

	public static void logError(String tag, String message) {
		if (AppOffOn.mIsOnLog) {
			Log.e(tag, message);
		}
	}

	public static void logInfo(String tag, String message) {
		if (AppOffOn.mIsOnLog) {
			Log.i(tag, message);
		}
	}

	public static void startActivity(Context mContext, Class clazz) {
		StartActivityUtil.startActivity(mContext, clazz);
	}

	public static void startActivity(Context mContext, Class clazz, int flag) {
		StartActivityUtil.startActivity(mContext, clazz, flag);
	}

	public static void startActivityForResult(Activity activity,
			int requestCode, Intent intent) {
		StartActivityUtil.startActivityForResult(activity, requestCode, intent);
	}

	public static void startActivity(Context mContext, Intent intent) {
		StartActivityUtil.startActivity(mContext, intent);
	}

	/**
	 * 创建桌面快捷方式
	 * 
	 * @param activity
	 */
	public static void CreateShortCut(Activity activity) {
		// intent进行隐式跳转,到桌面创建快捷方式
		Intent addIntent = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		// 不允许重建
		addIntent.putExtra("duplicate", false);
		// 得到应用的名称
		String title = activity.getResources().getString(R.string.app_name);
		// 将应用的图标设置为Parceable类型
		Parcelable icon = Intent.ShortcutIconResource.fromContext(activity,
				R.drawable.ic_launcher);
		// 点击图标之后的意图操作
		Intent myIntent = new Intent(activity, StartActivity.class);
		// 设置快捷方式的名称
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
		// 设置快捷方式的图标
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
		// 设置快捷方式的意图
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, myIntent);
		// 发送广播
		activity.sendBroadcast(addIntent);
	}

	/**
	 * 退出app的确认对话框
	 * 
	 * @param context
	 */
	public static void backdailog(final Context mcontext) {
		final ModelDialog dialog = new ModelDialog(mcontext,
				R.layout.dialog_back, R.style.Theme_dialog);
		dialog.show();
		Button btnok = (Button) dialog.findViewById(R.id.btn_ok_MethodsExtra);
		btnok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				exitApp(mcontext);
			}
		});
		Button btnCancel = (Button) dialog
				.findViewById(R.id.btn_cancel_MethodsExtra);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	/**
	 * 退出app
	 * 
	 * @param ctx
	 */
	public static void exitApp(Context ctx) {

		if (AppInstance.mListActivitys != null
				&& AppInstance.mListActivitys.size() >= 1) {
			for (int i = 0; i < AppInstance.mListActivitys.size(); i++) {
				Activity activity = AppInstance.mListActivitys.get(i);
				if (activity != null) {
					activity.finish();
				}
			}
		}
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
        ActivityManager activityMgr = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        activityMgr.killBackgroundProcesses(ctx.getPackageName());
	}

	/**
	 * 设置EditText不能输入空格
	 * 
	 * @param editText
	 */
	public static void editTextFilterSpace(final EditText editText,
			int maxLength) {
		editText.setFilters(new InputFilter[] {
				new InputFilter.LengthFilter(maxLength), new InputFilter() {
					@Override
					public CharSequence filter(CharSequence src, int start,
							int end, Spanned dst, int dstart, int dend) {
						if (src.length() < 1) {
							return null;
						} else {
							char temp[] = (src.toString()).toCharArray();
							char result[] = new char[temp.length];
							for (int i = 0, j = 0; i < temp.length; i++) {
								if (temp[i] == ' ') {
									continue;
								} else {
									result[j++] = temp[i];
								}
							}
							return String.valueOf(result).trim();
						}

					}

				} });
	}

	/**
	 * 设置输入框字数限制
	 * 
	 * @param editText
	 * @param maxLength
	 */
	public static void setTextLimitCount(final EditText editText,
			final int maxLength, final IInputLimitListener InputLimitListener) {

		editText.addTextChangedListener(new TextWatcher() {
			boolean isChange = false;
			String currentContent = "";

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				int inputMaxLength = 0;
				for (int i = 0; i < s.length(); i++) {
					if (s.subSequence(i, i + 1).charAt(0) >= 19968
							&& s.subSequence(i, i + 1).charAt(0) <= 171941) {
						inputMaxLength = inputMaxLength + 2;
					} else {
						inputMaxLength = inputMaxLength + 1;
					}
				}

				if (inputMaxLength >= 2 * maxLength + 2
						|| inputMaxLength >= 2 * maxLength + 1) {

					if (isChange) {
						return;
					}
					isChange = true;
					editText.setText(currentContent);
					editText.setSelection(currentContent.length());
					if (InputLimitListener != null) {
						InputLimitListener.onLimit();
					}
					isChange = false;
				} else {
					String content = editText.getText().toString();
					currentContent = content;
					if (InputLimitListener != null) {
						InputLimitListener.withinLimit();
					}
				}

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	/**
	 * 判断当前app是否在前台运行
	 * 
	 */
	public static boolean isRunningForeground(Context context) {
		try {
			ActivityManager activityManager = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningAppProcessInfo> appProcesses = activityManager
					.getRunningAppProcesses();
			for (RunningAppProcessInfo appProcess : appProcesses) {
				if (appProcess.processName.equals(context.getPackageName())) {
					if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
						return true;
					} else {
						KeyguardManager mKeyguardManager = (KeyguardManager) context
								.getSystemService(Context.KEYGUARD_SERVICE);
						if (mKeyguardManager.inKeyguardRestrictedInputMode()) {
							Log.i("MethodsExtra",
									"App is running,but screen is locked");
							return true;
						}
						return false;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	/**
	 * 根据包名判断应用是否安装
	 * 
	 * @param context
	 * @param platform
	 * @return
	 */
	public static boolean isInstall(Context context, String platform) {
		PackageManager pm = context.getPackageManager();
		try {
			pm.getPackageInfo(platform, 0);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}

	/**
	 * 获取手机版本号
	 * 
	 * @return
	 */
	public static String getPhoneSystemVersion() {
		return Build.VERSION.RELEASE;
	}

	/**
	 * 获取手机型号
	 * 
	 * @return
	 */
	public static String getPhoneModel() {
		return Build.MODEL;
	}

	/**
	 * 获取本机MAC地址
	 * 
	 * @param context
	 * @return
	 */
	public static String getLocalMACAddress(Context context) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

	/**
	 * 获取当前手机android sdk版本号
	 * 
	 * @return
	 */
	public static int getPhoneSDKVersionLevel() {
		return android.os.Build.VERSION.SDK_INT;
	}

	/**
	 * 获取当前应用程序的版本号。
	 */
	public static int getAppVersion(Context context) {
		if (context == null)
			return 0;

		return Integer.parseInt(context.getResources().getString(
				R.string.appVersionCode));
	}

	/**
	 * 设置TextView的Typeface字体
	 * 
	 * @param context
	 * @param root
	 * @param fontName
	 */
	public static void textViewSetTypeface(final Context context,
			final View root, final String fontName) {
		try {
			if (root instanceof ViewGroup) {
				ViewGroup viewGroup = (ViewGroup) root;
				for (int i = 0; i < viewGroup.getChildCount(); i++)
					textViewSetTypeface(context, viewGroup.getChildAt(i),
							fontName);
			} else if (root instanceof TextView)
				((TextView) root).setTypeface(Typeface.createFromAsset(
						context.getAssets(), fontName));
		} catch (Exception e) {
			Log.e("MethodsExtra", String.format(
					"Error occured when trying to apply %s font for %s view",
					fontName, root));
			e.printStackTrace();
		}
	}

	/**
	 * 播放声音 注意：如果需要调用此方法，请先在AppInit中调用SoundPoolUtil.loadVoice方法进行声音文件的初始化
	 * 其中声音文件放到raw目录下面
	 * 
	 * @param ctx
	 * @param soundId
	 *            此id定义在CST类中
	 * @param number
	 *            循环次数：0表示不循环，-1表示无限循环
	 * 
	 */
	public static void playSound(Context ctx, int soundId, int number) {
		SoundPoolUtil.playSound(ctx, soundId, 0);
	}

	/**
	 * 安装指定路径的apk文件
	 * 
	 * @param strAPKurl
	 */
	public static void installAPK(String strAPKurl) {
		if (strAPKurl == null || !strAPKurl.endsWith(".apk")) {
			return;
		}
		try {
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec("adb install -r " + strAPKurl);
			proc.getInputStream();
		} catch (Exception e) {
		}
	}

	/**
	 * 封装toast 默认提示无网路
	 * 
	 * @param cxt
	 * @param content
	 */
	public static void toast(Context cxt, String content) {
		if (cxt == null) {
		} else if (null == content) {
			content = "sorry,网络掉了";
		} else {
			Toast.makeText(cxt, content, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 隐藏软键盘
	 * 
	 * @param view
	 */
	public static void hideSoftInput(Activity activity) {
		Log.d("hidesoft", "hidesoft");
		activity.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	public static void hideSoftInput1(Activity activity, View view) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	/**
	 * 显示并赋值给标题左边按钮
	 * 
	 * @param activity
	 * @param view
	 * @param intString
	 * @return
	 */
	public static View findHeadLeftView1(Activity activity, View view,
			int intString, int backgroudImgId) {
		RelativeLayout mLayout = (RelativeLayout) view
				.findViewById(R.id.rlyt_left_mhead1);
		mLayout.setVisibility(View.VISIBLE);
		TextView tView = (TextView) mLayout.findViewById(R.id.tv_left_mhead1);
		ImageView bgImageView = (ImageView) view
				.findViewById(R.id.img_left_mhead1);
		if (intString != 0) {
			tView.setText(activity.getResources().getString(intString));
		} else {
			tView.setVisibility(View.GONE);
		}
		if (backgroudImgId != 0) {
			bgImageView.setImageDrawable(activity.getResources().getDrawable(
					backgroudImgId));
		}
		return bgImageView;
	}

	/**
	 * 显示并赋值给标题左边按钮
	 * 
	 * @param activity
	 * @param view
	 * @param intString
	 * @return
	 */
	public static View findHeadLeftViewNew(Activity activity, View view,
			int intString) {
		FrameLayout mLayout = (FrameLayout) view
				.findViewById(R.id.flyt_left_mhead);
		mLayout.setVisibility(View.VISIBLE);
		if (intString != 0) {
			TextView tView = (TextView) mLayout
					.findViewById(R.id.tv_left_mhead1);
			tView.setText(activity.getResources().getString(intString));
		}
		return view.findViewById(R.id.img_left_mhead1);
	}

	/**
	 * 显示并赋值给标题右边按钮
	 * 
	 * @param activity
	 * @param intString
	 * @return
	 */
	public static View findHeadRightView1(Activity activity, View view,
			int intString, int backgroudImgId) {
		RelativeLayout mLayout = (RelativeLayout) view
				.findViewById(R.id.rlyt_right_mhread1);
		mLayout.setVisibility(View.VISIBLE);
		ImageView bgImageView = (ImageView) view
				.findViewById(R.id.img_right_mhead1);
		TextView tView = (TextView) mLayout.findViewById(R.id.tv_right_mhead1);
		if (intString != 0) {
			tView.setText(activity.getResources().getString(intString));
		} else {
			tView.setVisibility(View.GONE);
		}
		if (backgroudImgId != 0) {
			bgImageView.setImageDrawable(activity.getResources().getDrawable(
					backgroudImgId));
		}
		return bgImageView;
	}

	/**
	 * 显示并赋值给标题右边按钮
	 * 
	 * @param activity
	 * @param intString
	 * @return
	 */
	public static View findHeadRightViewNew(Activity activity, View view,
			int intString) {
		RelativeLayout mLayout = (RelativeLayout) view
				.findViewById(R.id.rlyt_right_mhread1);
		mLayout.setVisibility(View.VISIBLE);
		if (intString != 0) {
			TextView tView = (TextView) mLayout
					.findViewById(R.id.tv_right_mhead1);
			tView.setText(activity.getResources().getString(intString));
		}
		return view.findViewById(R.id.img_right_mhead1);
	}

	/**
	 * 给标题框添加标题
	 * 
	 * @param activity
	 * @param stringTitle
	 * @return
	 */
	public static TextView findHeadTitleNew(Activity activity, View view,
			int stringTitle) {
		TextView textView = (TextView) view.findViewById(R.id.tv_title_mhead1);
		if (stringTitle != 0) {
			textView.setText(activity.getResources().getString(stringTitle));
		}
		return textView;
	}

	/**
	 * 给标题框添加标题
	 * 
	 * @param activity
	 * @param stringTitle
	 * @return
	 */
	public static TextView findHeadTitle1(Activity activity, View view,
			int stringTitle, String strTitle) {
		TextView textView = (TextView) view.findViewById(R.id.tv_title_mhead1);
		if (stringTitle != 0) {
			textView.setText(activity.getResources().getString(stringTitle));
		} else if (strTitle != null) {
			textView.setText(strTitle);
		}
		return textView;
	}

	/**
	 * 调用短信功能发送短信
	 * 
	 * @param context
	 * @param phone
	 * @param content
	 */
	public static void sendSms(Context context, String phone, String content) {
		Uri uri = Uri.parse("smsto:" + phone);
		Intent it = new Intent(Intent.ACTION_SENDTO, uri);
		it.putExtra("sms_body", content);
		context.startActivity(it);
	}

	/**
	 * 调出拨号功能打电话
	 * 
	 * @param context
	 * @param phone
	 */
	public static void tel(Context context, String phone) {
		Uri uri = Uri.parse("tel:" + phone);
		Intent it = new Intent(Intent.ACTION_DIAL, uri);
		context.startActivity(it);
	}

	/**
	 * 返回一个打开视频文件的Intent
	 * 
	 * @param param
	 * @return
	 */
	public static Intent getVideoFileIntent(String param) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.parse(param);
		intent.setDataAndType(uri, "video/*");
		return intent;
	}

	/**
	 * 实现文本复制功能
	 * 
	 * @param content
	 */
	public static void copy(String content, Context context) {
		// 得到剪贴板管理器
		ClipboardManager cmb = (ClipboardManager) context
				.getSystemService(Context.CLIPBOARD_SERVICE);
		cmb.setText(content.trim());
	}

	/**
	 * 实现粘贴功能
	 * 
	 * @param context
	 * @return
	 */
	public static String paste(Context context) {
		// 得到剪贴板管理器
		ClipboardManager cmb = (ClipboardManager) context
				.getSystemService(Context.CLIPBOARD_SERVICE);
		return cmb.getText().toString().trim();
	}

	public static void destroyThread0(Thread mThread) {
		if (mThread != null) {
			try {
				Log.i("destroyThread", "destroyThread开始");
				mThread.interrupt();
				System.gc();
				System.runFinalization();
				Log.i("destroyThread", "destroyThread结束");
			} catch (Exception e) {
				Log.i("destroyThread", "destroyThread异常");
			}
		}
	}

	/**
	 * 用字符串生成二维码图片
	 * 
	 * @param str
	 * @author zhouzhe@lenovo-cw.com
	 * @return
	 * @throws WriterException
	 */
	public Bitmap createBarcodeBitmap(String str) throws WriterException {
		// 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		BitMatrix matrix = new MultiFormatWriter().encode(str,
				BarcodeFormat.QR_CODE, 300, 300, hints);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		// 二维矩阵转为一维像素数组,也就是一直横着排了
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = 0xff000000;
				}

			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		// 通过像素数组生成bitmap,具体参考api
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	/**
	 * 从图片解码二维码数据
	 * 
	 * @param strPath
	 * @return
	 */
	public static Result decodeFromBitmap(Bitmap bitmap, String strPath) {
		MultiFormatReader multiFormatReader = new MultiFormatReader();

		// 解码的参数
		Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>(
				2);
		// 可以解析的编码类型
		Vector<BarcodeFormat> decodeFormats = new Vector<BarcodeFormat>();
		if (decodeFormats == null || decodeFormats.isEmpty()) {
			decodeFormats = new Vector<BarcodeFormat>();

			// 这里设置可扫描的类型，我这里选择了都支持
			decodeFormats.add(BarcodeFormat.UPC_A);
			decodeFormats.add(BarcodeFormat.UPC_E);
			decodeFormats.add(BarcodeFormat.EAN_13);
			decodeFormats.add(BarcodeFormat.EAN_8);
			decodeFormats.add(BarcodeFormat.CODABAR);
			decodeFormats.add(BarcodeFormat.CODE_39);
			decodeFormats.add(BarcodeFormat.CODE_93);
			decodeFormats.add(BarcodeFormat.CODE_128);
			decodeFormats.add(BarcodeFormat.ITF);
			decodeFormats.add(BarcodeFormat.RSS_14);
			decodeFormats.add(BarcodeFormat.RSS_EXPANDED);
			decodeFormats.add(BarcodeFormat.QR_CODE);
			decodeFormats.add(BarcodeFormat.DATA_MATRIX);
			decodeFormats.add(BarcodeFormat.MAXICODE);
			decodeFormats.add(BarcodeFormat.AZTEC);
			decodeFormats.add(BarcodeFormat.PDF_417);
		}
		hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

		// 设置继续的字符编码格式为UTF8
		// hints.put(DecodeHintType.CHARACTER_SET, "UTF8");

		// 设置解析配置参数
		multiFormatReader.setHints(hints);
		// 将图片的像素数据，转成ZXing定义的位图BinaryBitmap对象
		if (bitmap == null) {
			bitmap = BitmapFactory.decodeFile(strPath);
		}
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int[] pixels = new int[width * height];
		bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
		BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
				new RGBLuminanceSource(width, height, pixels)));
		MultiFormatReader mReader = new MultiFormatReader();
		Result result = null;
		try {
			result = mReader.decode(binaryBitmap, hints);
			System.out.println("res" + result.getText());
		} catch (Exception e) {
		}
		return result;
	}

	public static boolean checkBarcodeFormat(String strFormat) {
		if ("EAN_13".equals(strFormat) || "UPC_E".equals(strFormat)
				|| "UPC_A".equals(strFormat) || "EAN_8".equals(strFormat)
				|| "RSS_14".equals(strFormat) || "CODE_39".equals(strFormat)
				|| "CODE_93".equals(strFormat) || "CODE_128".equals(strFormat)
				|| "ITF".equals(strFormat) || "CODABAR".equals(strFormat)) {
			return true;
		}
		return false;
	}

	/**
	 * 打开文件
	 * 
	 * @param file
	 */
	public static void openFileWithExtra(Activity activity, Uri file) {
		// Uri uri = Uri.parse("file://"+file.getAbsolutePath());
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// 设置intent的Action属性
		intent.setAction(Intent.ACTION_VIEW);
		// 获取文件file的MIME类型
		String type = getMIMEType("" + file);
		// 设置intent的data和Type属性。
		intent.setDataAndType(file, type);
		// 跳转
		activity.startActivity(intent);
	}

	// 建立一个MIME类型与文件后缀名的匹配表
	public static final String[][] MIME_MapTable = {
			// {后缀名， MIME类型}
			{ ".3gp", "video/3gpp" },
			{ ".apk", "application/vnd.android.package-archive" },
			{ ".asf", "video/x-ms-asf" }, { ".avi", "video/x-msvideo" },
			{ ".bin", "application/octet-stream" }, { ".bmp", "image/bmp" },
			{ ".c", "text/plain" }, { ".class", "application/octet-stream" },
			{ ".conf", "text/plain" }, { ".cpp", "text/plain" },
			{ ".doc", "application/msword" },
			{ ".exe", "application/octet-stream" }, { ".gif", "image/gif" },
			{ ".gtar", "application/x-gtar" }, { ".gz", "application/x-gzip" },
			{ ".h", "text/plain" }, { ".htm", "text/html" },
			{ ".html", "text/html" }, { ".jar", "application/java-archive" },
			{ ".java", "text/plain" }, { ".jpeg", "image/jpeg" },
			{ ".jpg", "image/jpeg" }, { ".js", "application/x-javascript" },
			{ ".log", "text/plain" }, { ".m3u", "audio/x-mpegurl" },
			{ ".m4a", "audio/mp4a-latm" }, { ".m4b", "audio/mp4a-latm" },
			{ ".m4p", "audio/mp4a-latm" }, { ".m4u", "video/vnd.mpegurl" },
			{ ".m4v", "video/x-m4v" }, { ".mov", "video/quicktime" },
			{ ".mp2", "audio/x-mpeg" }, { ".mp3", "audio/x-mpeg" },
			{ ".mp4", "video/mp4" },
			{ ".mpc", "application/vnd.mpohun.certificate" },
			{ ".mpe", "video/mpeg" }, { ".mpeg", "video/mpeg" },
			{ ".mpg", "video/mpeg" }, { ".mpg4", "video/mp4" },
			{ ".mpga", "audio/mpeg" },
			{ ".msg", "application/vnd.ms-outlook" }, { ".ogg", "audio/ogg" },
			{ ".pdf", "application/pdf" }, { ".png", "image/png" },
			{ ".pps", "application/vnd.ms-powerpoint" },
			{ ".ppt", "application/vnd.ms-powerpoint" },
			{ ".prop", "text/plain" },
			{ ".rar", "application/x-rar-compressed" },
			{ ".rc", "text/plain" }, { ".rmvb", "audio/x-pn-realaudio" },
			{ ".rtf", "application/rtf" }, { ".sh", "text/plain" },
			{ ".tar", "application/x-tar" },
			{ ".tgz", "application/x-compressed" }, { ".txt", "text/plain" },
			{ ".wav", "audio/x-wav" }, { ".wma", "audio/x-ms-wma" },
			{ ".wmv", "audio/x-ms-wmv" },
			{ ".wps", "application/vnd.ms-works" },
			// {".xml", "text/xml"},
			{ ".xml", "text/plain" }, { ".z", "application/x-compress" },
			{ ".zip", "application/zip" }, { "", "*/*" } };

	/**
	 * 根据文件后缀名获得对应的MIME类型。
	 * 
	 * @param file
	 */
	public static String getMIMEType(String fName) {
		String type = "*/*";
		// 获取后缀名前的分隔符"."在fName中的位置。
		int dotIndex = fName.lastIndexOf(".");
		if (dotIndex < 0) {
			return type;
		}
		/* 获取文件的后缀名 */
		String end = fName.substring(dotIndex, fName.length()).toLowerCase();
		if (end == "")
			return type;
		// 在MIME和文件类型的匹配表中找到对应的MIME类型。
		for (int i = 0; i < MIME_MapTable.length; i++) {
			if (end.equals(MIME_MapTable[i][0]))
				type = MIME_MapTable[i][1];
		}
		return type;
	}

	/**
	 * 将两个Bitmap合并成一个
	 * 
	 * @param first
	 * @param second
	 * @param fromPoint
	 *            第二个Bitmap开始绘制的起始位置（相对于第一个Bitmap）
	 * @return
	 */
	protected static Bitmap mixtureBitmap(Bitmap first, Bitmap second,
			PointF fromPoint) {
		if (first == null || second == null || fromPoint == null) {
			return null;
		}
		int marginW = 20;
		Bitmap newBitmap = Bitmap.createBitmap(
				first.getWidth() + second.getWidth() + marginW,
				first.getHeight() + second.getHeight(), Config.ARGB_4444);
		Canvas cv = new Canvas(newBitmap);
		cv.drawBitmap(first, marginW, 0, null);
		cv.drawBitmap(second, fromPoint.x, fromPoint.y, null);
		cv.save(Canvas.ALL_SAVE_FLAG);
		cv.restore();

		return newBitmap;
	}

	// 打开文件浏览对话框
	public static void openFileExplore(Activity activity, int ResultCode) {
		Intent intent = new Intent();
		intent.putExtra("explorer_title", R.string.choose);
		intent.setDataAndType(Uri.fromFile(new File("/sdcard")), "*/*");
		intent.setClass(activity, FileExplore.class);
		activity.startActivityForResult(intent, ResultCode);
	}

	/**
	 * 在本机创建短信(此创建短信没有经过运营商，无资费消耗)
	 * 
	 * @param ctxt
	 * @param phone
	 * @param content
	 * @param type
	 * @param typeUri
	 * @param isRead
	 * @param date
	 */
	public static void createSmsLocal(Context ctxt, String phone,
			String content, SmsType type, SmsTypeURI typeUri, boolean isRead,
			Date date) {
		SmsManager.writeMessageInbox(ctxt, phone, content, type, typeUri,
				isRead, date);
	}
}