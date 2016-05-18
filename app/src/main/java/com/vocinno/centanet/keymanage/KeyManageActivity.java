package com.vocinno.centanet.keymanage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.cst.CST_JS;
import com.vocinno.centanet.apputils.dialog.ModelDialog;
import com.vocinno.centanet.apputils.selfdefineview.MyHorizontalScrollView;
import com.vocinno.centanet.baseactivity.OtherBaseActivity;
import com.vocinno.centanet.housemanage.HouseDetailActivity;
import com.vocinno.centanet.housemanage.HouseManageActivity2;
import com.vocinno.centanet.housemanage.HouseType;
import com.vocinno.centanet.keymanage.adapter.KeyListAdapter;
import com.vocinno.centanet.keymanage.adapter.ViewHolderGiveKey;
import com.vocinno.centanet.keymanage.adapter.ViewHolderGiveKey.KeyItemState;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.model.KeyItem;
import com.vocinno.centanet.model.KeyList;
import com.vocinno.centanet.model.KeyReceiverInfo;
import com.vocinno.centanet.myinterface.HttpInterface;
import com.vocinno.centanet.tools.constant.MyConstant;
import com.vocinno.utils.MethodsData;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsFile;
import com.vocinno.utils.MethodsJni;
import com.vocinno.utils.MethodsJson;
import com.vocinno.utils.view.refreshablelistview.XListView;
import com.vocinno.utils.view.refreshablelistview.XListView.IXListViewListener;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class KeyManageActivity extends OtherBaseActivity implements
		IXListViewListener {
	private View mBack, mAddBorrowKey;
	private XListView mListViewKeys;
	private KeyListAdapter mListAdapter;
	private List<KeyItem> mKeyListData;
	// 归还钥匙或借用钥匙
	private ViewHolderGiveKey mHolderGiveKey = null;
	private View mHolderViewItem = null;
	public String mStrtrKeyNo = null;
	private int mHolderRightWidth = 0;
	private int mHolderLeftWidth = 0;
	public static final int UPDATE_TIMER_TEXT = 1001;
	public static final int UPDATE_KEY_GET_IMAGE_HEADER = 1002;
	public static final int REMOVE_KEY_ITEM_VIEW_FROM_RIGHT = 1003;
	public static final int REMOVE_KEY_ITEM_VIEW_FROM_LEFT = 1004;
	// 倒计时
	public static final int REFRESH_TIMER = 10001;
	public static final int RESET_TIMER = 10002;
	public static final int SET_PINCODE = 10003;
	public int TOTAL_TIMER = 300;
	public int NOW_TIMER = TOTAL_TIMER;
	public Timer mTimer = null;
	// 归还钥匙的KeyNo
	private String mStrReturnKeyNo = null;

	@Override
	public Handler setHandler() {
		return new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case R.id.FINISH_LOAD_MORE:
					mListViewKeys.stopLoadMore();
					break;
				case R.id.FINISH_REFRESH:
					mListViewKeys.stopRefresh();
					break;
				case UPDATE_KEY_GET_IMAGE_HEADER:
					if (mHolderGiveKey == null) {
						return;
					}
					ImageView img = mHolderGiveKey.mImageViewGiveKey1;
					cancelTimer();
					mHolderGiveKey.mTvGiveKeyPassWord.setText("");
					Bitmap bp = BitmapFactory.decodeFile((String) msg.obj);
					if (bp == null) {
						return;
					}
					int newWidth = MethodsData.dip2px(mContext, 60);
					Bitmap bp1 = MethodsFile.getRoundBitmap(bp, newWidth,
							newWidth, R.color.white);
					bp.recycle();
					img.setImageBitmap(bp1);
					break;
				case UPDATE_TIMER_TEXT:
					if (mHolderGiveKey == null) {
						return;
					}
					TextView tvTimer = mHolderGiveKey.mTvTime;
					TextView tvTishi = mHolderGiveKey.mTvTishi;
					int mLeftSconds = (int) msg.obj;
					if (mLeftSconds == -1) {
						tvTimer.setText("");
						tvTishi.setText("请拖动钥匙\n到头像处");
					} else {
						String str = (mLeftSconds / 60) + ":"
								+ (mLeftSconds % 60);
						tvTimer.setText(str);
						tvTishi.setText("接收方请输入\n右图PIN码");
					}
					break;
				case REMOVE_KEY_ITEM_VIEW_FROM_RIGHT:
					// 调用数据
					getInitData();
					// 还原item状态
					((HorizontalScrollView) mHolderViewItem).smoothScrollTo(
							mHolderRightWidth, 0);
					mHolderGiveKey.keyItemState = KeyItemState.NORMAL;
					mHolderGiveKey.mImageViewGiveKey1
							.setImageResource(R.drawable.k_manage_button_number);
					mHolderGiveKey.mImgViewRtnKey.layout(
							mHolderGiveKey.mImgViewGiveKey0Bg.getLeft(),
							mHolderGiveKey.mImgViewGiveKey0Bg.getTop(),
							mHolderGiveKey.mImgViewGiveKey0Bg.getRight(),
							mHolderGiveKey.mImgViewGiveKey0Bg.getBottom());

					break;
				case REMOVE_KEY_ITEM_VIEW_FROM_LEFT:
					mHolderGiveKey.keyItemState = KeyItemState.NORMAL;
					((HorizontalScrollView) mHolderViewItem).smoothScrollTo(
							mHolderLeftWidth, 0);
					for (int i = 0; i < mKeyListData.size(); i++) {
						if (mKeyListData.get(i).getKeyNum()
								.equals(mStrReturnKeyNo)) {
							mKeyListData.remove(i);
							break;
						}
					}
					mListAdapter.setListDatas(mKeyListData);
					break;
				case REFRESH_TIMER:
					int second = NOW_TIMER % 60;
					String strTime = NOW_TIMER / 60 + ":"
							+ (second >= 10 ? second + "" : "0" + second);
					mHolderGiveKey.mTvTime.setText(strTime);
					if (--NOW_TIMER == 0) {
						NOW_TIMER = TOTAL_TIMER;
					}
					break;
				case RESET_TIMER:
					if (mHolderGiveKey != null) {
						mHolderGiveKey.mTvTime.setText("5:00");
					}
					break;
				case SET_PINCODE:
					setGiveKeyPinCode((String) msg.obj);
					break;
				default:
					break;
				}
			}
		};
	}

	private void getInitData() {
		if(modelDialog==null){
			modelDialog= ModelDialog.getModelDialog(this);
		}
		modelDialog.show();
		MethodsJni.callProxyFun(CST_JS.JS_ProxyName_KeyProxy,
				CST_JS.JS_Function_KeyProxy_getMyKeyList,
				CST_JS.getJsonStringForGetMyKeyList());
	}

	@Override
	public int setContentLayoutId() {
		return R.layout.activity_key_manage;
	}

	@Override
	public void initView() {
		MethodsExtra.findHeadTitle1(mContext, baseView, R.string.keymanage,
				null);
		mBack = MethodsExtra.findHeadLeftView1(mContext, baseView, 0, 0);
		mAddBorrowKey = MethodsExtra.findHeadRightView1(mContext, baseView, 0,
				R.drawable.universal_button_add);
		mListViewKeys = (XListView) findViewById(R.id.xlistview_key_manage_activity);

		setListener();
	}

	public void setListener() {
		mBack.setOnClickListener(this);
		mAddBorrowKey.setOnClickListener(this);

		mListViewKeys.setXListViewListener(this);

		mListViewKeys.setOnTouchListener(new OnTouchListener() {
			float x, y, thisDistantX, thisDistantY;
			int position = -1, lastPostion = -1, maxDistantRight,
					maxDistantLeft, mScreenWidth;
			MyHorizontalScrollView scrollView = null;
			View rootView;
			// 本次按下时的状态(开启还是关闭)
			boolean isFromOpenRight = false;
			boolean isFromOpenLeft = false;
			boolean isNeedOpenWellRight = false;
			boolean isNeedOpenWellLeft = false;

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						Log.d("slide", "slidelistview down isFromOpenLeft:"
								+ isFromOpenLeft + " isFromOpenRight:"
								+ isFromOpenRight + " x:" + event.getRawX());
						x = event.getRawX();
						y = event.getRawY();
						if (x < 60) {
							return false;
						}
						position = ((ListView) view).pointToPosition((int) x,
								(int) event.getY())
								- ((ListView) view).getFirstVisiblePosition();
						if ((isFromOpenRight || isFromOpenLeft) && lastPostion >= 0
								&& position != lastPostion) {
							// 上次未关闭，本次不能选择新行
							position = lastPostion;
						}
						rootView = ((ListView) view).getChildAt(position);
						if (rootView != null) {
							mHolderGiveKey = (ViewHolderGiveKey) rootView.getTag();
							scrollView = (MyHorizontalScrollView) rootView
									.findViewById(R.id.scrollView_itemKeyManageListView);
							mHolderViewItem = scrollView;
							mStrtrKeyNo = mHolderGiveKey.mStrKeyNum;
						}
						// 如果该行还未加载完毕或该行为footer
						if (rootView == null || scrollView == null) {
							return false;
						}
						lastPostion = position;
						if (mScreenWidth <= 0) {
							mScreenWidth = getWindowManager().getDefaultDisplay()
									.getWidth();
							maxDistantLeft = scrollView.findViewById(
									R.id.llyt_leftContainer_itemKeyManageListView)
									.getMeasuredWidth();
							maxDistantRight = scrollView.findViewById(
									R.id.touchFlyt_giveKey_itemKeyManageListView)
									.getMeasuredWidth();
							mHolderRightWidth = maxDistantLeft;
							mHolderLeftWidth = maxDistantLeft;
						}
						if (!isFromOpenLeft && !isFromOpenRight) {
							return false;
						}
						break;
					case MotionEvent.ACTION_UP:
						// 如果该行还未加载完毕或该行为footer
						if (rootView == null || scrollView == null || x < 60) {
							return false;
						} else if (Math.abs(event.getRawX() - x) <= 5
								&& Math.abs(event.getRawY() - y) <= 5) {
							Log.d("wanggsx", "wanggsxclick");

							// MethodsDeliverData.mKeyType = -1;
							// MethodsDeliverData.mDelCode = mKeyListData.get(
							// position - 1).getDelCode();
							// MethodsExtra.startActivity(mContext,
							// HouseDetailActivity.class);

							onItemClickListener(position);

							return false;
						}
						if (scrollView.getScrollX() < maxDistantLeft) {
							Log.d("wanggsx", "scrollview up 左");
							// 左侧操作
							if (isNeedOpenWellLeft) {
								// Log.d("wanggsx", "scrollview up 开");
								scrollView.smoothScrollTo(0, 0);
								isFromOpenLeft = true;
								mHolderGiveKey.mImgViewRtnKey
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View arg0) {
												setReturnKeyFinish(mHolderGiveKey.mStrKeyNum);
												scrollView.post(new Runnable() {
													@Override
													public void run() {
														scrollView.smoothScrollTo(
																maxDistantLeft, 0);
														isFromOpenLeft = false;
													}
												});
											}
										});
							} else {
								// Log.d("wanggsx", "scrollview up 关");
								scrollView.smoothScrollTo(maxDistantLeft, 0);
								isFromOpenLeft = false;
							}
						} else if (scrollView.getScrollX() > maxDistantLeft) {
							Log.d("wanggsx", "scrollview up 右");
							// 右侧操作
							if (isNeedOpenWellRight) {
								// Log.d("wanggsx", "scrollview up 开");
								scrollView.smoothScrollTo(maxDistantRight
										+ maxDistantLeft, 0);
								isFromOpenRight = true;
								setGiveKeyStart();
							} else {
								// Log.d("wanggsx", "scrollview up 关");
								scrollView.smoothScrollTo(maxDistantLeft, 0);
								isFromOpenRight = false;
								setCancelGiveKey();
							}
						} else {
							Log.d("wanggsx", "scrollview up 中");
							isFromOpenLeft = false;
							isFromOpenRight = false;
							isNeedOpenWellLeft = false;
							isNeedOpenWellRight = false;
						}
						if (!isFromOpenLeft && !isFromOpenRight) {
							return false;
						}
						break;
					case MotionEvent.ACTION_MOVE:
						// 如果该行还未加载完毕或该行为footer
						if (rootView == null || scrollView == null || x < 60) {
							return false;
						}
						thisDistantX = event.getRawX() - x;
						thisDistantY = event.getRawY() - y;
						if (Math.abs(thisDistantY / thisDistantX) > 2
								&& (!isFromOpenLeft && !isFromOpenRight)) {
							// 竖直方向滚动，事件传给listview
							return false;
						} else if (Math.abs(thisDistantX) < 5
								&& Math.abs(thisDistantY) < 5) {
							// 避免跟点击事件冲突
							return true;
						}

						if (thisDistantX < 0) {
							// 向左滑动
							if (isFromOpenLeft) {
								// 关闭左侧
								if (thisDistantX < -maxDistantLeft) {
									thisDistantX = -maxDistantLeft;
								}
								if (thisDistantX < -maxDistantLeft / 3) {
									isNeedOpenWellLeft = false;
								} else {
									isNeedOpenWellLeft = true;
								}
								scrollView.smoothScrollTo(-(int) thisDistantX, 0);
							} else if (!isFromOpenRight) {
								// 打开右侧
								if (thisDistantX < -maxDistantRight) {
									// 左滑必须有个距离限制
									thisDistantX = -maxDistantRight;
								}
								if (thisDistantX < -maxDistantRight / 5) {
									isNeedOpenWellRight = true;
								} else {
									isNeedOpenWellRight = false;
								}
								scrollView.smoothScrollTo(-(int) thisDistantX
										+ maxDistantLeft, 0);
							} else if (isFromOpenRight) {
								return true;
							} else {
								return false;
							}

						} else {
							// 向右滑动
							if (isFromOpenRight) {
								// 关闭右侧
								if (thisDistantX > maxDistantRight) {
									thisDistantX = maxDistantRight;
								}
								if (thisDistantX > maxDistantRight / 5) {
									isNeedOpenWellRight = false;
								} else {
									isNeedOpenWellRight = true;
								}
								scrollView.smoothScrollTo(-(int) thisDistantX
										+ maxDistantLeft + maxDistantRight, 0);
							} else if (!isFromOpenLeft) {
								// 打开左侧
								if (thisDistantX > maxDistantLeft) {
									// 左滑必须有个距离限制
									thisDistantX = maxDistantLeft;
								}
								if (thisDistantX > maxDistantLeft / 3) {
									isNeedOpenWellLeft = true;
								} else {
									isNeedOpenWellLeft = false;
								}
								scrollView.smoothScrollTo(-(int) thisDistantX
										+ maxDistantLeft, 0);
							} else if (isFromOpenLeft) {
								return true;
							} else {
								return false;
							}
						}
						break;
				}
				return true;
			}

		});

	}

	@Override
	public void initData() {
		methodsJni=new MethodsJni();
		methodsJni.setMethodsJni((HttpInterface)this);
		mListAdapter = new KeyListAdapter(KeyManageActivity.this);
		mListViewKeys.setAdapter(mListAdapter);
		mListViewKeys.setPullLoadEnable(false);
		// 添加通知
		MethodsJni.addNotificationObserver(
				CST_JS.NOTIFY_NATIVE_KEY_LIST_RESULT, TAG);
		MethodsJni.addNotificationObserver(
				CST_JS.NOTIFY_NATIVE_SET_PINCODE_RESULT, TAG);
		MethodsJni
				.addNotificationObserver(CST_JS.NOTIFY_NATIVE_PASS_CHECK, TAG);
		MethodsJni.addNotificationObserver(
				CST_JS.NOTIFY_NATIVE_PASS_CHECK_RECEIVER, TAG);
		MethodsJni.addNotificationObserver(
				CST_JS.NOTIFY_NATIVE_CONFIRM_KEY_TO_RECEIVER_RESULT, TAG);
		MethodsJni.addNotificationObserver(
				CST_JS.NOTIFY_NATIVE_CONFIRM_PINCODE, TAG);
		MethodsJni.addNotificationObserver(
				CST_JS.NOTIFY_NATIVE_RETURN_KEY_RESULT, TAG);
		// 调用数据
		getInitData();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.img_left_mhead1:
			finish();
			break;
		case R.id.img_right_mhead1:
			MethodsDeliverData.mIntHouseType = HouseType.YAO_SHI;
			Intent intent=new Intent(mContext,HouseManageActivity2.class);
			intent.putExtra(MyConstant.isKeyHouse,true);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MethodsDeliverData.mKeyType = -1;
		MethodsJni.callProxyFun(CST_JS.JS_ProxyName_KeyProxy,
				CST_JS.JS_Function_KeyPproxy_cancelAll,
				CST_JS.getJsonStringForGetMyKeyList());
		MethodsJni.removeNotificationObserver(
				CST_JS.NOTIFY_NATIVE_KEY_LIST_RESULT, TAG);
		MethodsJni.removeNotificationObserver(
				CST_JS.NOTIFY_NATIVE_SET_PINCODE_RESULT, TAG);
		MethodsJni.removeNotificationObserver(CST_JS.NOTIFY_NATIVE_PASS_CHECK,
				TAG);
		MethodsJni.removeNotificationObserver(
				CST_JS.NOTIFY_NATIVE_PASS_CHECK_RECEIVER, TAG);
		MethodsJni.removeNotificationObserver(
				CST_JS.NOTIFY_NATIVE_CONFIRM_KEY_TO_RECEIVER_RESULT, TAG);
		MethodsJni.removeNotificationObserver(
				CST_JS.NOTIFY_NATIVE_CONFIRM_PINCODE, TAG);
		MethodsJni.removeNotificationObserver(
				CST_JS.NOTIFY_NATIVE_RETURN_KEY_RESULT, TAG);
	}

	// listView item点击事件
	private void onItemClickListener(int position) {
		MethodsDeliverData.mKeyType = -1;
		MethodsDeliverData.mDelCode = mKeyListData.get(position - 1)
				.getDelCode();
		Intent intent=new Intent(this, HouseDetailActivity.class);
		intent.putExtra("key",true);
		startActivity(intent);
//		MethodsExtra.startActivity(mContext, HouseDetailActivity.class);
	}

	// 开启借用钥匙模式，此时需要设置钥匙的密码
	private void setGiveKeyStart() {
		mHolderGiveKey.keyItemState = KeyItemState.GIVE_ONE_REQUEST_PIN;
		// 此处需要设置钥匙的密码
		Log.d("test", "左边滑动");
		setGiveKeyPinCode("");
		MethodsJni
				.callProxyFun(
						CST_JS.JS_ProxyName_KeyProxy,
						CST_JS.JS_Function_KeyProxy_getPinCodeByLocal,
						CST_JS.getJsonStringForGetPinCodeByLocal(mHolderGiveKey.mStrKeyNum));

	}

	// 设置pincode
	private void setGiveKeyPinCode(String code) {
		if (mHolderGiveKey != null) {
			mHolderGiveKey.setPinCode(code);
			mHolderGiveKey.keyItemState = KeyItemState.GIVE_TWO_REQUEST_HEADER;
			// 启动倒计时
			if (mTimer != null) {
				mTimer.cancel();
				mTimer = null;
			}
			if (code != null && code.length() >= 1) {
				mTimer = new Timer();
				TimerTask mTask = new TimerTask() {
					@Override
					public void run() {
						Message msg = mHander.obtainMessage();
						msg.what = REFRESH_TIMER;
						mHander.sendMessage(msg);
					}
				};
				mTimer.schedule(mTask, 0, 1000);
			}
		}
	}

	// 设置借钥匙者的头像
	private void setHeadImage(final String url) {
		if (mHolderGiveKey != null) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					String strPath = MethodsFile.getAutoFileDirectory()
							+ MethodsFile.getFileNameFromPath(url);
					MethodsFile.downloadImgByUrl(url, strPath);
					Message msg = new Message();
					msg.what = UPDATE_KEY_GET_IMAGE_HEADER;
					msg.obj = strPath;
					mHander.sendMessage(msg);
				}
			}).start();
			mHolderGiveKey.mTouchableImgViewGiveKey0.setIsCanTouch(true);
			mHolderGiveKey.keyItemState = KeyItemState.GIVE_THREE_REQUEST_DRAG;
		}
	}

	// 借钥匙成功的回调
	public void setGiveKeyFinish(String strCode) {
		MethodsJni.callProxyFun(CST_JS.JS_ProxyName_KeyProxy,
				CST_JS.JS_Function_KeyProxy_confirmKeyPassToReceivert,
				CST_JS.getJsonStringForconfirmKeyPassToReceiver(strCode));
	}

	// 取消钥匙传递
	private void setCancelGiveKey() {
		String keyno = null;
		if (mHolderGiveKey != null) {
			cancelTimer();
			keyno = mHolderGiveKey.mStrKeyNum;
			mHolderGiveKey.keyItemState = KeyItemState.NORMAL;
		}
		mHolderGiveKey = null;
		mHolderViewItem = null;
		Log.d("test", "测试钥匙传递的KEY:" + keyno);
		MethodsJni.callProxyFun(CST_JS.JS_ProxyName_KeyProxy,
				CST_JS.JS_Function_KeyProxy_cancelKeyPassToReceiver,
				CST_JS.getJsonStringForcancelKeyPassToReceiver(keyno));
	}

	// 归还钥匙，传入钥匙编号
	private void setReturnKeyFinish(String keyNo) {
		mStrReturnKeyNo = keyNo;
		MethodsJni.callProxyFun(CST_JS.JS_ProxyName_KeyProxy,
				CST_JS.JS_Function_KeyProxy_returnKeyToShop,
				CST_JS.getJsonStringForReturnKeyToShop(keyNo));
	}

	@Override
	public void onLoadMore() {
		MethodsJni.callProxyFun(CST_JS.JS_ProxyName_KeyProxy,
				CST_JS.JS_Function_KeyProxy_getMyKeyList,
				CST_JS.getJsonStringForGetMyKeyList());
	}

	@Override
	public void onRefresh() {
		MethodsJni.callProxyFun(CST_JS.JS_ProxyName_KeyProxy,
				CST_JS.JS_Function_KeyProxy_getMyKeyList,
				CST_JS.getJsonStringForGetMyKeyList());
	}

	public void notifCallBack(String name, String className, Object data) {

	}

	private void cancelTimer() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		NOW_TIMER = TOTAL_TIMER;
		Message msg = mHander.obtainMessage();
		msg.what = RESET_TIMER;
		mHander.sendMessage(msg);
	}

	@Override
	public void netWorkResult(String name, String className, Object data) {
		dismissDialog();
		String strJson = (String) data;
		JSReturn jsReturn;
		Log.d("notifCallBack", "notif name=" + name + " data=" + data);
		if (name.equals(CST_JS.NOTIFY_NATIVE_KEY_LIST_RESULT)) {
			jsReturn = MethodsJson.jsonToJsReturn(strJson, KeyList.class);
			if (jsReturn.isSuccess()) {
				mHander.sendEmptyMessage(R.id.FINISH_REFRESH);
				mKeyListData = jsReturn.getListDatas();
				mListAdapter.setListDatas(mKeyListData);
			}
		} else if (name.equals(CST_JS.NOTIFY_NATIVE_SET_PINCODE_RESULT)) {
			jsReturn = MethodsJson.jsonToJsReturn(strJson, null);
			if (jsReturn.isSuccess()) {
				// 拥有者设置pin码
				Object obj = jsReturn.getContent();

				Message msg = mHander.obtainMessage();
				msg.what = SET_PINCODE;
				msg.obj = jsReturn.getParams().getData();
				mHander.sendMessage(msg);
			} else {

			}
		} else if (name.equals(CST_JS.NOTIFY_NATIVE_PASS_CHECK)) {
			jsReturn = MethodsJson.jsonToJsReturn(strJson,
					KeyReceiverInfo.class);
			if (jsReturn.isSuccess()) {
				List<KeyReceiverInfo> listKinfo = jsReturn.getListDatas();
				for (int i = 0; i < listKinfo.size(); i++) {
					KeyReceiverInfo kInfo = listKinfo.get(i);
					if (kInfo.getKeyNum().equals(mHolderGiveKey.mStrKeyNum)) {
						// 拥有者设置接收者头像
						String URL = kInfo.getReceiverInfo().gethImg();
						setHeadImage(URL);
						break;
					}
				}
			}
		} else if (name.equals(CST_JS.NOTIFY_NATIVE_PASS_CHECK_RECEIVER)) {
			Log.d("test ", "wanggsx 接收者等待状态更新");
			jsReturn = MethodsJson.jsonToJsReturn(strJson,
					KeyReceiverInfo.class);
			if (jsReturn.isSuccess()) {
				List<KeyReceiverInfo> listKinfo = jsReturn.getListDatas();
				for (int i = 0; i < listKinfo.size(); i++) {
					KeyReceiverInfo kInfo = listKinfo.get(i);
					if (kInfo.getKeyNum().equals(mStrtrKeyNo)
							&& kInfo.getType().equals("receive")
							&& kInfo.getResult().equals("0")) {
						// 接收者成功借到钥匙
						MethodsExtra.toast(mContext, "恭喜，借钥匙完成");
						MethodsJni.callProxyFun(CST_JS.JS_ProxyName_KeyProxy,
								CST_JS.JS_Function_KeyProxy_getMyKeyList,
								CST_JS.getJsonStringForGetMyKeyList());
						break;
					}
				}
			} else {
				MethodsExtra.toast(mContext, "借钥匙失败");
			}
		} else if (name.equals(CST_JS.NOTIFY_NATIVE_CONFIRM_PINCODE)) {
			Log.d("test", "收到通知显示等待接收钥匙条目");
		} else if (name
				.equals(CST_JS.NOTIFY_NATIVE_CONFIRM_KEY_TO_RECEIVER_RESULT)) {
			jsReturn = MethodsJson.jsonToJsReturn(strJson, null);
			// 确认要是借用
			if (jsReturn.isSuccess()) {
				mHander.sendEmptyMessageDelayed(
						REMOVE_KEY_ITEM_VIEW_FROM_RIGHT, 50);
				MethodsExtra.toast(mContext, "钥匙已借出");
			} else {
				MethodsExtra.toast(mContext, "钥匙未能成功借出");
			}
		} else if (name.equals(CST_JS.NOTIFY_NATIVE_RETURN_KEY_RESULT)) {
			jsReturn = MethodsJson.jsonToJsReturn(strJson, null);
			if (jsReturn.isSuccess()) {
				// 钥匙成功归还到店
				mHander.sendEmptyMessageDelayed(REMOVE_KEY_ITEM_VIEW_FROM_LEFT,
						50);
				MethodsExtra.toast(mContext, "钥匙已归还");
			} else {
				if (jsReturn != null) {
					MethodsExtra.toast(mContext, jsReturn.getMsg());
				}
			}
		}
	}
}
