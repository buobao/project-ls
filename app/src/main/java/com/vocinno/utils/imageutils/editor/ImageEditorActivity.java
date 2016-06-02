package com.vocinno.utils.imageutils.editor;

import com.vocinno.centanet.R;
import com.vocinno.centanet.housemanage.AddHousePictureDescriptionActivity;
import com.vocinno.utils.MethodsData;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsFile;

import android.R.integer;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class ImageEditorActivity extends Activity implements OnClickListener {
	private View mRootView;
	private ImageView mTouchImageView;
	private View viewSelectArea;
	private Button mBtnSave, mBtnRotate;
	private float rateHeightByWidth = 1.0f;
	private int SCREEN_HEIGHT = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mRootView = LayoutInflater.from(this).inflate(
				R.layout.activity_image_editor, null);
		setContentView(mRootView);
		SCREEN_HEIGHT = getWindowManager().getDefaultDisplay().getHeight();
		mTouchImageView = (ImageView) mRootView
				.findViewById(R.id.imgView_imageEditorActivity);
		viewSelectArea = findViewById(R.id.view_selectContainer_imageEditorActivity);
		mBtnSave = (Button) findViewById(R.id.btn_ok_imageEditorActivity);
		mBtnRotate = (Button) findViewById(R.id.btn_rotate_imageEditorActivity);
		mBtnSave.setOnClickListener(this);
		mBtnRotate.setOnClickListener(this);
		mTouchImageView.setImageBitmap(MethodsDeliverData.mBitmap);
		int[] xy = MethodsData.getScreenWidthHeight(ImageEditorActivity.this);
		float scx = xy[0] / MethodsDeliverData.mBitmap.getWidth();
		float scy = xy[1] / MethodsDeliverData.mBitmap.getHeight();
		final float sc = scx > scy ? scx : scy;
		mTouchImageView.post(new Runnable() {
			@Override
			public void run() {
				Matrix mtr = mTouchImageView.getImageMatrix();
				mtr.postScale(sc, sc, 0, 0);
				mTouchImageView.setImageMatrix(mtr);
			}
		});
		mTouchImageView.setOnTouchListener(new OnTouchListener() {
			// ImageView触摸事件
			private PointF startPoint = new PointF();
			private Matrix matrix = new Matrix();
			private Matrix currentMaritx = new Matrix();
			private int mode = 0;// 用于标记模式
			private static final int DRAG = 1;// 拖动
			private static final int ZOOM = 2;// 放大
			private float startDis = 0;
			private PointF startMidPoint;// 中心点

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:
					mode = DRAG;
					currentMaritx.set(mTouchImageView.getImageMatrix());// 记录ImageView当期的移动位置
					startPoint.set(event.getRawX(), event.getRawY());// 开始点
					break;
				case MotionEvent.ACTION_MOVE:// 移动事件
					float dx = event.getRawX() - startPoint.x;// x轴移动距离
					float dy = event.getRawY() - startPoint.y;
					if (mode == DRAG) {// 图片拖动事件
						matrix.set(currentMaritx);// 在当前的位置基础上移动
						matrix.postTranslate(dx, dy);

					} else if (mode == ZOOM) {// 图片放大事件
						float endDis = distance(event);// 结束距离
						if (endDis > 10f) {
							float scale = endDis / startDis;// 放大倍数
							// Log.d("touch", "touch move scale:" + scale);
							matrix.set(currentMaritx);
							matrix.postScale(scale, scale, startMidPoint.x,
									startMidPoint.y);
						}
					}
					break;
				case MotionEvent.ACTION_UP:
					mode = 0;
					break;
				// 有手指离开屏幕，但屏幕还有触点(手指)
				case MotionEvent.ACTION_POINTER_UP:
					mode = 0;
					break;
				// 当屏幕上已经有触点（手指）,再有一个手指压下屏幕
				case MotionEvent.ACTION_POINTER_DOWN:
					mode = ZOOM;
					Log.d("touch", "touch down");
					startDis = distance(event);
					if (startDis > 10f) {// 避免手指上有两个茧
						startMidPoint = mid(event);
						currentMaritx.set(mTouchImageView.getImageMatrix());// 记录当前的缩放倍数
					}
					break;
				}
				mTouchImageView.setImageMatrix(matrix);
				return true;
			}

		});
	}

	/**
	 * 两点之间的距离
	 * 
	 * @param event
	 * @return
	 */
	private static float distance(MotionEvent event) {
		// 两根线的距离
		float dx = event.getX(1) - event.getX(0);
		float dy = event.getY(1) - event.getY(0);
		return FloatMath.sqrt(dx * dx + dy * dy);
	}

	/**
	 * 两点之间连线的倾斜角
	 * 
	 * @param event
	 * @return
	 */
	private static double lineDegree(MotionEvent event) {
		float dx = event.getX(1) - event.getX(0);
		float dy = event.getY(1) - event.getY(0);
		return dy / dx;
	}

	/**
	 * 计算两点之间中心点的距离
	 * 
	 * @param event
	 * @return
	 */
	private static PointF mid(MotionEvent event) {
		float midx = event.getX(1) + event.getX(0);
		float midy = event.getY(1) + event.getY(0);

		return new PointF(midx / 2, midy / 2);
	}

	public void rotate(float degree) {
		Matrix matrix = new Matrix();
		matrix.set(mTouchImageView.getImageMatrix());// 在当前的位置基础上移动
		matrix.postRotate(degree,
				mTouchImageView.getLeft() + mTouchImageView.getMeasuredWidth()
						/ 2,
				mTouchImageView.getTop() + mTouchImageView.getMeasuredHeight()
						/ 2);
		mTouchImageView.setImageMatrix(matrix);
	}

	/**
	 * 设置截图高宽比
	 * 
	 * @param rateHeightByWidth
	 */
	public void setRateHeightByWidth(float rateHeightByWidth) {
		this.rateHeightByWidth = rateHeightByWidth;
		LayoutParams params = (LayoutParams) viewSelectArea
				.getLayoutParams();
		if (params == null) {
			params = new LayoutParams(
					(int) (SCREEN_HEIGHT / (this.rateHeightByWidth * 2)),
					SCREEN_HEIGHT / 2);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok_imageEditorActivity:
			Log.d("image x y:",
					viewSelectArea.getLeft() + ";" + viewSelectArea.getTop());
			// 截屏并保存图片
			Bitmap bitmap = MethodsFile.takeScreenShot(
					ImageEditorActivity.this, viewSelectArea.getLeft(),
					viewSelectArea.getTop(), viewSelectArea.getMeasuredWidth(),
					viewSelectArea.getMeasuredHeight());
			String strFileName = MethodsFile.saveBitmapToFile(bitmap);
			if (strFileName != null) {
				MethodsExtra.toast(ImageEditorActivity.this, "图片保存成功");
				MethodsDeliverData.mListImagePath.add(strFileName);
				bitmap.recycle();
				MethodsExtra.startActivity(ImageEditorActivity.this,
						AddHousePictureDescriptionActivity.class);
				finish();
			}
			break;
		case R.id.btn_rotate_imageEditorActivity:
			rotate(15);
			break;
		default:
			break;
		}

	}
}
