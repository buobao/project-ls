package com.vocinno.utils.media.camera;

import java.io.IOException;

import com.vocinno.centanet.R;
import com.vocinno.utils.MethodsData;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsFile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

/**
 * 照相机
 */
public class CameraActivity extends Activity {

	private static final String TAG = "MainActivity";
	private Camera camera;
	private ImageButton startCaemraBtn;
	private ImageButton flashModeBtn;

	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;

	// Popwindow显示
	private PopupWindow tipPopupWindow;
	private RelativeLayout layout;
	private ListView listView;
	private String title[] = { "自动", "开启", "关闭" };
	private AudioManager mAudioManager;
	private int ringMode;
	public static Bitmap prviewImageBitMap;
	private boolean hasSurface;// 是否有预览

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window window = getWindow();
		hasSurface = false;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_camera);
		initView();

		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		setRingMode(mAudioManager.getRingerMode());
	}

	// 初始化界面的UI
	private void initView() {
		startCaemraBtn = (ImageButton) findViewById(R.id.startCameraBtn);
		flashModeBtn = (ImageButton) findViewById(R.id.flashModeBtn);

		startCaemraBtn.setOnClickListener(new startCameraOnClick());
		flashModeBtn.setOnClickListener(new flashModeCameraOnClick());

		SharedPreferences sharedPreferences = getSharedPreferences(
				"issetValueXml", Context.MODE_PRIVATE);
		int i = sharedPreferences.getInt("onoff", 0);
		if (i == 0) {
			flashModeBtn
					.setBackgroundResource(R.drawable.camera_flashmode_auto);
		} else if (i == 1) {
			flashModeBtn
					.setBackgroundResource(R.drawable.camera_flashmode_open);
		} else if (i == 2) {
			flashModeBtn.setBackgroundResource(R.drawable.camera_flashmode_off);
		}
		surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(new SurfaceCallback());
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		// 初始化 CameraManager
		CameraManager.init(CameraActivity.this);
	}

	// 回调接口
	private final class SurfaceCallback implements SurfaceHolder.Callback {

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			System.out.println("surfaceCreated+++hasSurface:" + hasSurface);
			if (!hasSurface) {
				hasSurface = true;
				initCamera(holder);
			}

		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			System.out.println("surfaceDestroyed+++");
			hasSurface = false;
		}
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.init(CameraActivity.this);
			CameraManager.get().openDriver(surfaceHolder);
			camera = CameraManager.get().getCamera();
			CameraManager.get().startPreview();// 此句很重要，用于开启预览
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
	}

	// 回调接口
	private final class TakePictureCallback implements PictureCallback {
		// 该方法用于处理拍摄后的照片数据
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.v("--------", "执行");
			mAudioManager.setRingerMode(getRingMode());
			// data参数值就是照片数据，将这些数据以key-value形式保存，以便其他调用该Activity的程序可以获得照片数据
			try {
				Matrix matrix = new Matrix();
				matrix.reset();
				matrix.setRotate(90);
				Options opt = new Options();
				opt.inSampleSize = 4;
				Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
						data.length, opt);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
						bitmap.getHeight(), matrix, true);
				MethodsDeliverData.mBitmap = bitmap;
				Intent intent = new Intent(CameraActivity.this,
						PreviewActivity.class);
				startActivity(intent);
				// 停止照片拍摄
				camera.stopPreview();
				finish();
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
		}
	}

	// 无声处理
	ShutterCallback sc = new ShutterCallback() {
		@Override
		public void onShutter() {
			Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(100);
		}
	};

	@SuppressWarnings("deprecation")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN)
			// 结束拍照
			camera.autoFocus(new AutoFocusCallback() {
				@Override
				public void onAutoFocus(boolean success, Camera camera) {
					// success为true表示对焦成功
					if (success) {
						SharedPreferences sharedPreferences = getSharedPreferences(
								"issetValueXml", Context.MODE_PRIVATE);
						int i = sharedPreferences.getInt("onoff", 2);
						if (i == 0) {
							Parameters parameters = camera
									.getParameters();
							parameters
									.setFlashMode(Parameters.FLASH_MODE_OFF);
							camera.setParameters(parameters);
						} else if (i == 1) {
							Parameters parameters = camera
									.getParameters();
							parameters
									.setFlashMode(Parameters.FLASH_MODE_OFF);
							camera.setParameters(parameters);

						} else if (i == 2) {
							Parameters parameters = camera
									.getParameters();
							parameters
									.setFlashMode(Parameters.FLASH_MODE_OFF);
							camera.setParameters(parameters);
						}
					}
				}
			});
		return super.onTouchEvent(event);
	}

	// 快门事件
	class startCameraOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			try {
				if (camera == null) {
					MethodsExtra.toast(CameraActivity.this, "未打开摄像头");
				}
				SharedPreferences sharedPreferences = getSharedPreferences(
						"issetValueXml", Context.MODE_PRIVATE);
				int i = sharedPreferences.getInt("onoff", 2);
				if (i == 0) {
					// Camera.Parameters parameters = camera.getParameters();
					// parameters.setFlashMode(Camera.Parameters.FOCUS_MODE_AUTO);
					// camera.setParameters(parameters);
					Parameters parameters = camera.getParameters();
					parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
					camera.setParameters(parameters);
					camera.takePicture(sc, null, new TakePictureCallback());
				} else if (i == 1) {
					// Camera.Parameters parameters = camera.getParameters();
					// parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
					// camera.setParameters(parameters);
					Parameters parameters = camera.getParameters();
					parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
					camera.setParameters(parameters);
					camera.takePicture(sc, null, new TakePictureCallback());
				} else if (i == 2) {
					Parameters parameters = camera.getParameters();
					parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
					camera.setParameters(parameters);
					camera.takePicture(sc, null, new TakePictureCallback());
				}
			} catch (Exception e) {
				MethodsExtra.toast(CameraActivity.this, "未打开摄像头");
			}
		}
	}

	// 显示pop窗口
	public void showTipPopup() {
		layout = (RelativeLayout) LayoutInflater.from(CameraActivity.this)
				.inflate(R.layout.bubble_dialog, null);
		listView = (ListView) layout.findViewById(R.id.lv_dialog);
		listView.setAdapter(new ArrayAdapter<String>(CameraActivity.this,
				R.layout.bubble_text, R.id.tv_text, title));
		tipPopupWindow = new PopupWindow(CameraActivity.this);
		tipPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		tipPopupWindow.setWidth(80);
		tipPopupWindow.setHeight(250);
		tipPopupWindow.setOutsideTouchable(true);
		tipPopupWindow.setFocusable(true);
		tipPopupWindow.setContentView(layout);
		// showAsDropDown会把里面的view作为参照物，所以要那满屏幕parent
		tipPopupWindow.showAtLocation(findViewById(R.id.flashModeBtn),
				Gravity.LEFT | Gravity.TOP, 80, 30);
		// 需要指定Gravity，默认情况是center.
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				SharedPreferences sharedPreferences = getSharedPreferences(
						"issetValueXml", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedPreferences.edit();
				if (arg2 == 0) {
					editor.putInt("onoff", 0);
					editor.commit();
					flashModeBtn
							.setBackgroundResource(R.drawable.camera_flashmode_auto);
					tipPopupWindow.dismiss();
				} else if (arg2 == 1) {
					editor.putInt("onoff", 1);
					editor.commit();
					flashModeBtn
							.setBackgroundResource(R.drawable.camera_flashmode_open);
					tipPopupWindow.dismiss();
				} else if (arg2 == 2) {
					editor.putInt("onoff", 2);
					editor.commit();
					flashModeBtn
							.setBackgroundResource(R.drawable.camera_flashmode_off);
					tipPopupWindow.dismiss();
				}
			}
		});
	}

	// 闪光灯事件
	class flashModeCameraOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			showTipPopup();
		}
	}

	public int getRingMode() {
		return ringMode;
	}

	public void setRingMode(int ringMode) {
		this.ringMode = ringMode;
	}

	public static Bitmap getPrviewImageBitMap() {
		return prviewImageBitMap;
	}

	@Override
	protected void onResume() {
		System.out.println("capture+++onResume++++hasSurface:" + hasSurface);
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(new SurfaceCallback());
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
	}

	@Override
	protected void onStop() {
		System.out.println("capture+++onStop");
		super.onStop();
	}

	@Override
	protected void onPause() {
		System.out.println("capture+++onPause");
		super.onPause();
		CameraManager.get().stopPreview();
		CameraManager.get().closeDriver();
		hasSurface = false;
	}

	@Override
	protected void onDestroy() {
		System.out.println("capture+++onDestroy");
		super.onDestroy();
	}

}