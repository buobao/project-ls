package com.vocinno.centanet.apputils.selfdefineview;

import java.util.ArrayList;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.vocinno.centanet.R;

/**
 * WheelView滚轮
 * 
 * @author JiangPing
 */
public class WheelView extends View {
	/**
	 * 控件宽度
	 */
	private float mControlWidth;
	/**
	 * 控件高度
	 */
	private float mControlHeight;
	/**
	 * 是否滑动中
	 */
	private boolean mIsScrolling = false;
	/**
	 * 选择的内容
	 */
	private ArrayList<ItemObject> mListItem = new ArrayList<ItemObject>();
	/**
	 * 设置数据
	 */
	private ArrayList<String> mListDate = new ArrayList<String>();
	/**
	 * 按下的坐标
	 */
	private int mDownY;
	/**
	 * 按下的时间
	 */
	private long mDownTime = 0;
	/**
	 * 短促移动
	 */
	private long mGoonTime = 200;
	/**
	 * 短促移动距离
	 */
	private int mGoonDistence = 100;
	/**
	 * 画线画笔
	 */
	private Paint mLinePaint;
	/**
	 * 线的默认颜色
	 */
	private int mLineColor = 0xff000000;
	/**
	 * 线的默认宽度
	 */
	private float mLineWidth = 2f;
	/**
	 * 默认字体
	 */
	private float mNormalFont = 14.0f;
	/**
	 * 选中的时候字体
	 */
	private float mSelectedFont = 18.0f;
	/**
	 * 单元格高度
	 */
	private int mUnitHeight = 50;
	/**
	 * 显示多少个内容
	 */
	private int mItemNumber = 7;
	/**
	 * 默认字体颜色
	 */
	private int mNormalColor = 0xff000000;
	/**
	 * 默认二号字体的颜色
	 */
	private int mSecondColor = 0xff9e9e9e;
	/**
	 * 选中时候的字体颜色
	 */
	private int mSelectedColor = 0xffff0000;
	/**
	 * 蒙板高度
	 */
	private float mMaskHight = 48.0f;
	/**
	 * 选择监听
	 */
	private OnSelectListener mOnSelectListener;
	/**
	 * 是否可用
	 */
	private boolean mIsEnable = true;
	/**
	 * 刷新界面
	 */
	private static final int REFRESH_VIEW = 0x001;
	/**
	 * 移动距离
	 */
	private static final int MOVE_NUMBER = 5;
	/**
	 * 是否允许选空
	 */
	private boolean mNoEmpty = true;

	/**
	 * 正在修改数据，避免ConcurrentModificationException异常
	 */
	private boolean mIsClearing = false;

	public WheelView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
		initData();
		Log.d("wheelview", "wheelview init 3");
	}

	public WheelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
		initData();
		Log.d("wheelview", "wheelview init 2");
	}

	public WheelView(Context context) {
		super(context);
		initData();
		Log.d("wheelview", "wheelview init 1");
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		getParent().requestDisallowInterceptTouchEvent(true);
		return super.dispatchTouchEvent(event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!mIsEnable) {
			Log.d("wheel", "wheel touch : 终止");
			return true;
		}
		int y = (int) event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.d("wheel", "wheel touch : down");
			mIsScrolling = true;
			mDownY = (int) event.getY();
			mDownTime = System.currentTimeMillis();
			break;
		case MotionEvent.ACTION_MOVE:
			Log.d("wheel", "wheel touch : move");
			actionMove(y - mDownY);
			onSelectListener();
			break;
		case MotionEvent.ACTION_UP:
			Log.d("wheel", "wheel touch : up");
			int move = Math.abs(y - mDownY);
			// 判断段时间移动的距离
			if (System.currentTimeMillis() - mDownTime < mGoonTime
					&& move > mGoonDistence) {
				goonMove(y - mDownY);
			} else {
				actionUp(y - mDownY);
			}
			noEmpty();
			mIsScrolling = false;
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawLine(canvas);
		drawList(canvas);
		drawMask(canvas);
	}

	private synchronized void drawList(Canvas canvas) {
		if (mIsClearing)
			return;
		try {
			for (ItemObject itemObject : mListItem) {
				itemObject.drawSelf(canvas);
			}
		} catch (Exception e) {
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		mControlWidth = getWidth();
		if (mControlWidth != 0) {
			setMeasuredDimension((int) mControlWidth, mItemNumber * mUnitHeight);
		}
		invalidate();
	}

	/**
	 * 继续移动一定距离
	 */
	private synchronized void goonMove(final int move) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				int distence = 0;
				while (distence < mUnitHeight * MOVE_NUMBER) {
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					actionThreadMove(move > 0 ? distence : distence * (-1));
					distence += 10;
				}
				actionUp(move > 0 ? distence - 10 : distence * (-1) + 10);
				noEmpty();
			}
		}).start();
	}

	/**
	 * 不能为空，必须有选项
	 */
	private void noEmpty() {
		if (!mNoEmpty)
			return;
		for (ItemObject item : mListItem) {
			if (item.isSelected())
				return;
		}
		int move = (int) mListItem.get(0).moveToSelected();
		if (move < 0) {
			defaultMove(move);
		} else {
			defaultMove((int) mListItem.get(mListItem.size() - 1)
					.moveToSelected());
		}
		for (ItemObject item : mListItem) {
			if (item.isSelected()) {
				if (mOnSelectListener != null)
					mOnSelectListener.endSelect(item.mId, item.mItemText);
				break;
			}
		}
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mIsClearing = true;
		mListItem.clear();
		for (int i = 0; i < mListDate.size(); i++) {
			ItemObject itmItemObject = new ItemObject();
			itmItemObject.mId = i;
			itmItemObject.mItemText = mListDate.get(i);
			itmItemObject.x = 0;
			itmItemObject.y = i * mUnitHeight;
			mListItem.add(itmItemObject);
		}
		mIsClearing = false;
	}

	/**
	 * 移动的时候
	 * 
	 * @param move
	 */
	private void actionMove(int move) {
		for (ItemObject item : mListItem) {
			item.move(move);
		}
		invalidate();
	}

	/**
	 * 移动，线程中调用
	 * 
	 * @param move
	 */
	private void actionThreadMove(int move) {
		for (ItemObject item : mListItem) {
			item.move(move);
		}
		Message rMessage = new Message();
		rMessage.what = REFRESH_VIEW;
		handler.sendMessage(rMessage);
	}

	/**
	 * 松开的时候
	 * 
	 * @param move
	 */
	private void actionUp(int move) {
		int newMove = 0;
		if (move > 0) {
			for (int i = 0; i < mListItem.size(); i++) {
				if (mListItem.get(i).isSelected()) {
					newMove = (int) mListItem.get(i).moveToSelected();
					if (mOnSelectListener != null)
						mOnSelectListener.endSelect(mListItem.get(i).mId,
								mListItem.get(i).mItemText);
					break;
				}
			}
		} else {
			for (int i = mListItem.size() - 1; i >= 0; i--) {
				if (mListItem.get(i).isSelected()) {
					newMove = (int) mListItem.get(i).moveToSelected();
					if (mOnSelectListener != null)
						mOnSelectListener.endSelect(mListItem.get(i).mId,
								mListItem.get(i).mItemText);
					break;
				}
			}
		}
		for (ItemObject item : mListItem) {
			item.newY(move + 0);
		}
		slowMove(newMove);
		Message rMessage = new Message();
		rMessage.what = REFRESH_VIEW;
		handler.sendMessage(rMessage);

	}

	/**
	 * 缓慢移动
	 * 
	 * @param move
	 */
	private synchronized void slowMove(final int move) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// 判断政府
				int m = move > 0 ? move : move * (-1);
				int i = move > 0 ? 1 : (-1);
				// 移动速度
				int speed = 1;
				while (true) {
					m = m - speed;
					if (m <= 0) {
						for (ItemObject item : mListItem) {
							item.newY(m * i);
						}
						Message rMessage = new Message();
						rMessage.what = REFRESH_VIEW;
						handler.sendMessage(rMessage);
						try {
							Thread.sleep(2);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						break;
					}
					for (ItemObject item : mListItem) {
						item.newY(speed * i);
					}
					Message rMessage = new Message();
					rMessage.what = REFRESH_VIEW;
					handler.sendMessage(rMessage);
					try {
						Thread.sleep(2);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				for (ItemObject item : mListItem) {
					if (item.isSelected()) {
						if (mOnSelectListener != null)
							mOnSelectListener.endSelect(item.mId,
									item.mItemText);
						break;
					}
				}

			}
		}).start();
	}

	/**
	 * 移动到默认位置
	 * 
	 * @param move
	 */
	private void defaultMove(int move) {
		for (ItemObject item : mListItem) {
			item.newY(move);
		}
		Message rMessage = new Message();
		rMessage.what = REFRESH_VIEW;
		handler.sendMessage(rMessage);
	}

	/**
	 * 滑动监听
	 */
	private void onSelectListener() {
		if (mOnSelectListener == null)
			return;
		for (ItemObject item : mListItem) {
			if (item.isSelected()) {
				mOnSelectListener.selecting(item.mId, item.mItemText);
			}
		}
	}

	/**
	 * 绘制线条
	 * 
	 * @param canvas
	 */
	private void drawLine(Canvas canvas) {

		if (mLinePaint == null) {
			mLinePaint = new Paint();
			mLinePaint.setColor(mLineColor);
			mLinePaint.setAntiAlias(true);
			mLinePaint.setStrokeWidth(mLineWidth);
		}

		canvas.drawLine(0, mControlHeight / 2 - mUnitHeight / 2 + 2,
				mControlWidth, mControlHeight / 2 - mUnitHeight / 2 + 2,
				mLinePaint);
		canvas.drawLine(0, mControlHeight / 2 + mUnitHeight / 2 - 2,
				mControlWidth, mControlHeight / 2 + mUnitHeight / 2 - 2,
				mLinePaint);
	}

	/**
	 * 绘制遮盖板
	 * 
	 * @param canvas
	 */
	private void drawMask(Canvas canvas) {
		LinearGradient lg = new LinearGradient(0, 0, 0, mMaskHight, 0x00f2f2f2,
				0x00f2f2f2, TileMode.MIRROR);
		Paint paint = new Paint();
		paint.setShader(lg);
		canvas.drawRect(0, 0, mControlWidth, mMaskHight, paint);
		LinearGradient lg2 = new LinearGradient(0, mControlHeight - mMaskHight,
				0, mControlHeight, 0x00f2f2f2, 0x00f2f2f2, TileMode.MIRROR);
		Paint paint2 = new Paint();
		paint2.setShader(lg2);
		canvas.drawRect(0, mControlHeight - mMaskHight, mControlWidth,
				mControlHeight, paint2);

	}

	/**
	 * 初始化，获取设置的属性
	 * 
	 * @param context
	 * @param attrs
	 */
	private void init(Context context, AttributeSet attrs) {

		TypedArray attribute = context.obtainStyledAttributes(attrs,
				R.styleable.WheelView);
		mUnitHeight = (int) attribute.getDimension(
				R.styleable.WheelView_unitHight, 32);
		mNormalFont = attribute.getDimension(
				R.styleable.WheelView_normalTextSize, 14.0f);
		mSelectedFont = attribute.getDimension(
				R.styleable.WheelView_selectedTextSize, 22.0f);
		mItemNumber = attribute.getInt(R.styleable.WheelView_itemNumber, 7);
		mNormalColor = attribute.getColor(
				R.styleable.WheelView_normalTextColor, 0xff000000);
		// mSecondColor =
		// attribute.getColor(R.styleable.WheelView_normalTextColor,
		// 0xff000000);
		mSelectedColor = attribute.getColor(
				R.styleable.WheelView_selectedTextColor, 0xffff0000);
		mLineColor = attribute.getColor(R.styleable.WheelView_lineColor,
				0xff000000);
		mLineWidth = attribute.getDimension(R.styleable.WheelView_lineHeight,
				2f);
		mMaskHight = attribute.getDimension(R.styleable.WheelView_maskHight,
				48.0f);
		mNoEmpty = attribute.getBoolean(R.styleable.WheelView_noEmpty, true);
		mIsEnable = attribute.getBoolean(R.styleable.WheelView_isEnable, true);
		attribute.recycle();

		mControlHeight = mItemNumber * mUnitHeight;

	}

	/**
	 * 设置数据 （第一次）
	 * 
	 * @param data
	 */
	public void setData(ArrayList<String> data) {
		if (data != null) {
			this.mListDate = data;
			initData();
		}
	}
	public void setData(ArrayList<String> data,int centerX) {
		this.mControlWidth=centerX;
		if (data != null) {
			this.mListDate = data;
			initData();
		}
	}
	/**
	 * 重置数据
	 * 
	 * @param data
	 */
	public void resetData(ArrayList<String> data) {
		setData(data);
		invalidate();
	}

	/**
	 * 获取返回项 id
	 * 
	 * @return
	 */
	public int getSelected() {
		for (ItemObject item : mListItem) {
			if (item.isSelected())
				return item.mId;
		}
		return -1;
	}

	/**
	 * 获取返回的内容
	 * 
	 * @return
	 */
	public String getSelectedText() {
		for (ItemObject item : mListItem) {
			if (item.isSelected())
				return item.mItemText;
		}
		return "";
	}

	/**
	 * 是否正在滑动
	 * 
	 * @return
	 */
	public boolean isScrolling() {
		return mIsScrolling;
	}

	/**
	 * 是否可用
	 * 
	 * @return
	 */
	public boolean isEnable() {
		return mIsEnable;
	}

	/**
	 * 设置是否可用
	 * 
	 * @param isEnable
	 */
	public void setEnable(boolean isEnable) {
		this.mIsEnable = isEnable;
	}

	/**
	 * 设置选中的位置
	 * 
	 * @param index
	 */
	public void setSelectItem(int index) {
		if (index > mListItem.size() - 1 || index < 0)
			return;
		float move = mListItem.get(index).moveToSelected();
		defaultMove((int) move);
	}

	/**
	 * 设置选中的内容项
	 * 
	 * @param text
	 */
	public void setSelectText(String text, int defaultIndex) {
		if (text == null) {
			return;
		}
		int index = defaultIndex;
		for (int i = 0; i < mListDate.size(); i++) {
			if (text.equals(mListDate.get(i))) {
				index = i;
			}
		}
		setSelectItem(index);
	}

	/**
	 * 获取列表大小
	 * 
	 * @return
	 */
	public int getListSize() {
		if (mListItem == null)
			return 0;
		return mListItem.size();
	}

	/**
	 * 获取某项的内容
	 * 
	 * @param index
	 * @return
	 */
	public String getItemText(int index) {
		if (mListItem == null)
			return "";
		return mListItem.get(index).mItemText;
	}

	/**
	 * 监听
	 * 
	 * @param onSelectListener
	 */
	public void setOnSelectListener(OnSelectListener onSelectListener) {
		this.mOnSelectListener = onSelectListener;
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REFRESH_VIEW:
				invalidate();
				break;
			default:
				break;
			}
		}

	};

	/**
	 * 单条内容
	 * 
	 * @author JiangPing
	 */
	private class ItemObject {
		/**
		 * id
		 */
		public int mId = 0;
		/**
		 * 内容
		 */
		public String mItemText = "";
		/**
		 * x坐标
		 */
		public int x = 0;
		/**
		 * y坐标
		 */
		public int y = 0;
		/**
		 * 移动距离
		 */
		public int mMove = 0;
		/**
		 * 字体画笔
		 */
		private Paint mTextPaint;
		/**
		 * 字体范围矩形
		 */
		private Rect mTextRect;

		public ItemObject() {
			super();
		}

		/**
		 * 绘制自身
		 * 
		 * @param canvas
		 */
		public void drawSelf(Canvas canvas) {

			if (mTextPaint == null) {
				mTextPaint = new Paint();
				mTextPaint.setAntiAlias(true);
			}

			if (mTextRect == null)
				mTextRect = new Rect();

			// 判断是否被选择
			if (isSelected()) {
				mTextPaint.setColor(mSelectedColor);
				// 获取距离标准位置的距离
				float moveToSelect = moveToSelected();
				moveToSelect = moveToSelect > 0 ? moveToSelect : moveToSelect
						* (-1);
				// 计算当前字体大小
				float textSize = mNormalFont
						+ ((mSelectedFont - mNormalFont) * (1.0f - moveToSelect
								/ mUnitHeight));
				mTextPaint.setTextSize(textSize);
			} else {
				if (isSecondSelected()) {
					mTextPaint.setColor(mSecondColor);
					mTextPaint.setTextSize(mNormalFont);
				} else {
					mTextPaint.setColor(mNormalColor);
					mTextPaint.setTextSize(mNormalFont);
				}
			}

			// 返回包围整个字符串的最小的一个Rect区域
			mTextPaint.getTextBounds(mItemText, 0, mItemText.length(),
					mTextRect);
			int textRectWidth= mTextRect.width();
			// 判断是否可视
			if (!isInView())
				return;

			// 绘制内容
			canvas.drawText(mItemText,
					x + mControlWidth / 2 - mTextRect.width() / 2, y + mMove
							+ mUnitHeight / 2 + mTextRect.height() / 2,
					mTextPaint);

		}

		/**
		 * 是否在可视界面内
		 * 
		 * @return
		 */
		public boolean isInView() {
			if (y + mMove > mControlHeight
					|| (y + mMove + mUnitHeight / 2 + mTextRect.height() / 2) < 0)
				return false;
			return true;
		}

		/**
		 * 移动距离
		 * 
		 * @param _move
		 */
		public void move(int _move) {
			this.mMove = _move;
		}

		/**
		 * 设置新的坐标
		 * 
		 * @param _move
		 */
		public void newY(int _move) {
			this.mMove = 0;
			this.y = y + _move;
		}

		public Boolean isSecondSelected() {
			if ((y + mMove) >= mControlHeight / 2 - mUnitHeight / 2
					+ mUnitHeight + 2
					&& (y + mMove) <= mControlHeight / 2 + mUnitHeight / 2
							+ mUnitHeight - 2)
				return true;
			if ((y + mMove + mUnitHeight) >= mControlHeight / 2 - mUnitHeight
					/ 2 + 2 + mUnitHeight
					&& (y + mMove + mUnitHeight) <= mControlHeight / 2
							+ mUnitHeight / 2 - 2 + mUnitHeight)
				return true;
			if ((y + mMove) <= mControlHeight / 2 - mUnitHeight / 2
					+ mUnitHeight + 2
					&& (y + mMove + mUnitHeight) >= mControlHeight / 2
							+ mUnitHeight / 2 + mUnitHeight - 2)
				return true;
			if ((y + mMove) >= mControlHeight / 2 - mUnitHeight / 2
					- mUnitHeight + 2
					&& (y + mMove) <= mControlHeight / 2 + mUnitHeight / 2
							- mUnitHeight - 2)
				return true;
			if ((y + mMove + mUnitHeight) >= mControlHeight / 2 - mUnitHeight
					/ 2 + 2 - mUnitHeight
					&& (y + mMove + mUnitHeight) <= mControlHeight / 2
							+ mUnitHeight / 2 - 2 - mUnitHeight)
				return true;
			if ((y + mMove) <= mControlHeight / 2 - mUnitHeight / 2
					- mUnitHeight + 2
					&& (y + mMove + mUnitHeight) >= mControlHeight / 2
							+ mUnitHeight / 2 - mUnitHeight - 2)
				return true;

			return false;
		}

		/**
		 * 判断是否在选择区域内
		 * 
		 * @return
		 */
		public boolean isSelected() {
			if ((y + mMove) >= mControlHeight / 2 - mUnitHeight / 2 + 2
					&& (y + mMove) <= mControlHeight / 2 + mUnitHeight / 2 - 2)
				return true;
			if ((y + mMove + mUnitHeight) >= mControlHeight / 2 - mUnitHeight
					/ 2 + 2
					&& (y + mMove + mUnitHeight) <= mControlHeight / 2
							+ mUnitHeight / 2 - 2)
				return true;
			if ((y + mMove) <= mControlHeight / 2 - mUnitHeight / 2 + 2
					&& (y + mMove + mUnitHeight) >= mControlHeight / 2
							+ mUnitHeight / 2 - 2)
				return true;
			return false;
		}

		/**
		 * 获取移动到标准位置需要的距离
		 */
		public float moveToSelected() {
			return (mControlHeight / 2 - mUnitHeight / 2) - (y + mMove);
		}
	}

	/**
	 * 选择监听
	 * 
	 * @author JiangPing
	 */
	public interface OnSelectListener {
		/**
		 * 结束选择
		 * 
		 * @param id
		 * @param text
		 */
		public void endSelect(int id, String text);

		/**
		 * 选中的内容
		 * 
		 * @param id
		 * @param text
		 */
		public void selecting(int id, String text);

	}
}
