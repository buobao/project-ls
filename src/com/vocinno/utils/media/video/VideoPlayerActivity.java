package com.vocinno.utils.media.video;

import java.util.Timer;
import java.util.TimerTask;

import com.vocinno.centanet.R;
import com.vocinno.utils.MethodsDeliverData;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class VideoPlayerActivity extends Activity implements
		View.OnClickListener {
	private static final String TAG = "VideoActivity";
	// 多媒体文件路径(含文件名)
	private String videoFile;
	// 播放控件
	private SurfaceView surfaceView;
	// 播放控制类
	private MediaPlayer mediaPlayer;
	// assets目录下的文件管理器
	private AssetFileDescriptor assetFileDescriptor;
	// 多媒体文件是否来自于项目包下的assets目录
	private boolean IsFileInApk = true;
	// 控制播放进度的按钮栏
	private LinearLayout llytMenu;
	// 是否是第一次打开
	private boolean firstStart = true;
	// 是否正在播放
	private boolean isPlaying = true;
	// 用于接收消息来更新UI
	private Handler mHandler;
	// 用于延迟1s自动播放视频
	private Timer mTimer;
	// 用于显示播放进度控件
	private ProgressBar pBar;
	// 用于提示播放时间
	private TextView tvPastTime, tvTotalTime, tvTotalTime0;
	// 当前播放进度
	private int currentSeconds = 0;
	// 总播放时间
	private int totalSeconds = 0;
	// 更新播放进度的线程，1s发一个消息给Handle
	private Thread refreshThread;
	// 播放按钮
	private ImageButton playButton, pauseButton, resetButton, stopButton,
			closeButton, openFileButton;
	// 请求结果码
	public static final int resultCode = 1001;
	public static final int doPlay = 1002;
	public static final int doPlayUpdate = 1003;
	public static final int doUpdateSuccess = 1007;
	public static final int doFinish = 1004;
	public static final int doStop = 1005;
	public static final int doReplay = 1006;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("mmm调用onCreate()");
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			setContentView(R.layout.activity_video_player_land);
		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			setContentView(R.layout.activity_video_player_port);
		}
		if (firstStart) {
			String strPath = MethodsDeliverData.currentVideoURL;
			System.out.println("获取播放文件路径：" + strPath);
			if (strPath.startsWith("http")) {
				videoFile = strPath;
				IsFileInApk = false;
			} else {
				videoFile = strPath.substring(22);
				IsFileInApk = true;
			}
		}
		System.out.println("mmmvideoFile=" + videoFile);

		llytMenu = (LinearLayout) this.findViewById(R.id.llyt_menu_videoPlayer);

		playButton = (ImageButton) this
				.findViewById(R.id.ibtn_play_videoPlayer);
		playButton.setOnClickListener(this);

		pauseButton = (ImageButton) this
				.findViewById(R.id.ibtn_pause_videoPlayer);
		pauseButton.setOnClickListener(this);

		resetButton = (ImageButton) this
				.findViewById(R.id.ibtn_reset_videoPlayer);
		resetButton.setOnClickListener(this);

		stopButton = (ImageButton) this
				.findViewById(R.id.ibtn_stop_videoPlayer);
		stopButton.setOnClickListener(this);

		openFileButton = (ImageButton) this
				.findViewById(R.id.ibtn_openFile_videoPlayer);
		openFileButton.setOnClickListener(this);

		closeButton = (ImageButton) this
				.findViewById(R.id.ibtn_close_videoPlayer);
		closeButton.setOnClickListener(this);

		pBar = (ProgressBar) this.findViewById(R.id.pb_videoPlayer);
		tvPastTime = (TextView) this.findViewById(R.id.tv_pastTime_videoPlayer);
		tvTotalTime = (TextView) this
				.findViewById(R.id.tv_totalTime_videoPlayer);
		tvTotalTime0 = (TextView) this
				.findViewById(R.id.tv_totalTime0_videoPlayer);

		surfaceView = (SurfaceView) this
				.findViewById(R.id.surfaceView_videoPlayer);
		/* 下面设置Surface不维护自己的缓冲区，而是等待屏幕的渲染引擎将内容推送到用户面前 */
		SurfaceHolder holder = surfaceView.getHolder();
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		holder.setFixedSize(320, 240); // 设置分辨率
		/* 设置不自动屏保 */
		holder.setKeepScreenOn(true);
		surfaceView.setOnClickListener(this);
		mediaPlayer = new MediaPlayer();
		MyHandle();
		mTimer = new Timer(true);
		llytMenu.setVisibility(View.VISIBLE);
	}

	private void MyHandle() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case R.id.doSuccess:
					llytMenu.setVisibility(View.INVISIBLE);
					break;
				case doPlayUpdate:
					// 每隔1秒更新进度
					int currentSeconds = msg.arg1;
					tvPastTime.setText(currentSeconds
							/ 60
							+ ":"
							+ (currentSeconds % 60 >= 10 ? currentSeconds % 60
									: "0" + currentSeconds % 60));
					pBar.setProgress(currentSeconds);
					if (tvTotalTime0.getText().toString()
							.equals(tvPastTime.getText().toString())) {
						mHandler.removeMessages(doPlayUpdate);
						new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									Thread.sleep(800);
									Message msg = mHandler.obtainMessage();
									msg.what = doUpdateSuccess;
									mHandler.sendMessage(msg);
									System.out.println("mmm播放时间到达终点");
									Thread.sleep(500);
									Message msg1 = mHandler.obtainMessage();
									msg1.what = doFinish;
									mHandler.sendMessage(msg1);
									System.out.println("mmm结束播放");
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}).start();
					}
					break;
				case doPlay:
					// 开始播放时显示总时间
					try {
						totalSeconds = mediaPlayer.getDuration() / 1000;
						pBar.setMax(totalSeconds);
						tvPastTime.setText("0:00");
						System.out.println("mmm总时间为：" + totalSeconds);
						tvTotalTime0.setText(intToTimeString(totalSeconds - 1));
						tvTotalTime.setText(intToTimeString(totalSeconds));
						// 启动更新进度的线程
						startRefreshThread();
					} catch (Exception e) {
					}
					break;
				case doReplay:
					playWithLastPosition();
					break;
				case doFinish:
					// 播放结束
					currentSeconds = 0;
					tvPastTime.setText("0:00");
					isPlaying = false;
					refreshThread = null;
					clickpauseButtons();
					System.out.println("mmm您点击了播放结束的暂停按钮");
					pBar.setProgress(0);
					break;
				case doUpdateSuccess:
					tvPastTime.setText(tvTotalTime.getText().toString());
					break;
				default:
					break;
				}
			}
		};
	}

	private String intToTimeString(int seconds) {
		int thisSeconds = seconds % 60;
		return seconds / 60 + ":"
				+ (thisSeconds >= 10 ? thisSeconds : "0" + thisSeconds);
	}

	private class MyTimeTask extends TimerTask {
		@Override
		public void run() {
			Message msg = mHandler.obtainMessage();
			msg.what = R.id.doSuccess;
			mHandler.sendMessage(msg);
		}
	}

	@Override
	protected void onDestroy() {
		System.out.println("mmm调用onDestroy（）");
		if (mediaPlayer.isPlaying())
			mediaPlayer.stop();
		isPlaying = false;
		mediaPlayer.release();
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		System.out.println("mmm调用onPause（）");
		if (mediaPlayer.isPlaying())
			mediaPlayer.pause();
		if (refreshThread != null) {
			isPlaying = false;
			refreshThread = null;
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 延迟100ms自动播放
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (isPlaying && currentSeconds >= 1) {
					// 通知主线程接着上次的播放
					Message msg = mHandler.obtainMessage();
					msg.what = doReplay;
					mHandler.sendMessage(msg);
				} else if (isPlaying) {
					play();
					firstStart = false;
				}
			}
		}).start();
	}

	void play() {
		try {
			mediaPlayer.reset();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			if (IsFileInApk) {
				assetFileDescriptor = getAssets().openFd(videoFile);
				mediaPlayer.setDataSource(
						assetFileDescriptor.getFileDescriptor(),
						assetFileDescriptor.getStartOffset(),
						assetFileDescriptor.getLength());
				assetFileDescriptor.close();
			} else {
				mediaPlayer.setDataSource(videoFile);
			}
			mediaPlayer.setDisplay(surfaceView.getHolder());// 把视频画面输出到SurfaceView
			mediaPlayer.prepare();
			mediaPlayer.start();
			mTimer.schedule(new MyTimeTask(), 3000);
			// 通知更新总播放时间
			Message msg = mHandler.obtainMessage();
			msg.what = doPlay;
			mHandler.sendMessage(msg);
		} catch (Exception e) {
			System.out.println("mmm播放出问题");
		}
	}

	private void stopAndPrepare() {
		try {
			mediaPlayer.reset();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			if (IsFileInApk) {
				assetFileDescriptor = getAssets().openFd(videoFile);
				mediaPlayer.setDataSource(
						assetFileDescriptor.getFileDescriptor(),
						assetFileDescriptor.getStartOffset(),
						assetFileDescriptor.getLength());
				assetFileDescriptor.close();
			} else {
				mediaPlayer.setDataSource(videoFile);
			}
			mediaPlayer.setDisplay(surfaceView.getHolder());// 把视频画面输出到SurfaceView
			mediaPlayer.prepare();
		} catch (Exception e) {
			System.out.println("mmm播放出问题");
		}
	}

	private void startRefreshThread() {
		if (null == refreshThread) {
			System.out.println("mmm重启更新线程");
			refreshThread = new Thread(new Runnable() {
				@Override
				public void run() {
					while (isPlaying) {
						int thiscurrentSeconds = 0;
						try {
							if (mediaPlayer.isPlaying()
									&& totalSeconds >= currentSeconds + 1) {
								thiscurrentSeconds = mediaPlayer
										.getCurrentPosition() / 1000;
								currentSeconds = thiscurrentSeconds;
								System.out.println("mmm更新播放器进度："
										+ currentSeconds + ":线程："
										+ Thread.currentThread().getName());
								Message msg = mHandler.obtainMessage();
								msg.what = doPlayUpdate;
								msg.arg1 = currentSeconds;
								mHandler.sendMessage(msg);
								Thread.sleep(980);
							}
						} catch (Exception e) {
							System.out.println("mmm更新播错误进度："
									+ thiscurrentSeconds);
							currentSeconds = 0;
						}
					}
				}
			});
			refreshThread.start();
		}
	}

	@Override
	public void onClick(View v) {
		try {
			switch (v.getId()) {
			case R.id.ibtn_play_videoPlayer:// 来自播放按钮
				clickplayButtons();
				isPlaying = true;
				if (firstStart || tvPastTime.getText().equals("0:00")) {
					play();
				} else {
					playWithLastPosition();
				}
				break;
			case R.id.ibtn_pause_videoPlayer:// 来自暂停按钮
				clickpauseButtons();
				if (isPlaying) {
					isPlaying = false;
					mediaPlayer.pause();
					refreshThread = null;
				}
				break;
			case R.id.ibtn_reset_videoPlayer:// 来自重新播放按钮
				isPlaying = true;
				if (!mediaPlayer.isPlaying()) {
					play();
				}
				mediaPlayer.seekTo(0);
				startRefreshThread();
				clickplayButtons();
				break;
			case R.id.ibtn_stop_videoPlayer:// 来自停止按钮
				isPlaying = false;
				refreshThread = null;
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.stop();
				}
				clickpauseButtons();
				tvPastTime.setText("0:00");
				pBar.setProgress(0);
				stopAndPrepare();
				break;
			case R.id.ibtn_openFile_videoPlayer:
				// Methods.openFileExplore(VideoPlayerActivity.this,
				// resultCode);
				break;
			case R.id.surfaceView_videoPlayer:
				llytMenu.setVisibility(View.VISIBLE);
				break;
			case R.id.ibtn_close_videoPlayer:
				isPlaying = false;
				backToMain();
				break;
			}
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

	void playWithLastPosition() {
		isPlaying = true;
		mediaPlayer.start();
		startRefreshThread();
	}

	void clickplayButtons() {
		System.out.println("mmm您点击了播放按钮");
		playButton.setVisibility(View.GONE);
		pauseButton.setVisibility(View.VISIBLE);
	}

	void clickpauseButtons() {
		System.out.println("mmm您点击了暂停按钮");
		playButton.setVisibility(View.VISIBLE);
		pauseButton.setVisibility(View.GONE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == VideoPlayerActivity.resultCode) {
				Uri uri = data.getData();
				videoFile = "" + uri;
				System.out.println("mmm选择文件路径为：" + videoFile);
				IsFileInApk = false;
			}
		}
	}

	@Override
	public void onBackPressed() {
		backToMain();
	}

	void backToMain() {
		finish();
	}

}