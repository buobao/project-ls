package com.vocinno.centanet.apputils.utils.media.audio;

import java.util.HashMap;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;

public class SoundPoolUtil {

	private static SoundPool soundPool;
	@SuppressLint("UseSparseArrays")
	private static int stopRes;
	private static Context _context;
	private static HashMap<Integer, Integer> mSoundPoolKeyRawIdMap = null;
	public static HashMap<Integer, Integer> mSoundPoolMap = new HashMap<Integer, Integer>();

	/**
	 * 播放音效
	 * 
	 * @param context
	 * @param soundRes
	 *            音效资源文件
	 * @param number
	 *            循环次数，0是不循环，-1是永远循环
	 */
	public static void playSound(Context context, final int soundRes,
			final int number) {
		_context = context;
		AudioManager am = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		// 最大音量
		float audioMaxVolumn = am
				.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
		// 当前音量
		float audioCurrentVolumn = am
				.getStreamVolume(AudioManager.STREAM_SYSTEM);
		final float volumnRatio = audioCurrentVolumn / (2 * audioMaxVolumn);

		// 獲取當前媒體音量
		int voiceMedia = am.getStreamVolume(AudioManager.STREAM_MUSIC);

		// 如果系統音量為0則不播放音效
		if ((int) audioCurrentVolumn == 0 || voiceMedia == 0) {
			return;
		}

		// 获取本地设置，是否设置了关闭音效
		SharedPreferences share = context.getSharedPreferences("jiujie",
				Context.MODE_PRIVATE);

		// 播放
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				stopRes = soundPool.play(getVoiceRes(soundRes), // 声音资源
						volumnRatio, // 左声道
						volumnRatio, // 右声道
						1, // 优先级，0最低
						number, // 循环次数，0是不循环，-1是永远循环
						1); // 回放速度，0.5-2.0之间。1为正常速度

			}
		}, 100);

	}

	/**
	 * 初始化声音文件加载
	 * 
	 * @param context
	 */
	public static void loadVoice(Context context,
			HashMap<Integer, Integer> soundPoolKeyRawIdMap) {
		soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
		if (soundPoolKeyRawIdMap != null) {
			mSoundPoolKeyRawIdMap = soundPoolKeyRawIdMap;
		}
		if (mSoundPoolKeyRawIdMap != null) {
			Integer[] keys = (Integer[]) mSoundPoolKeyRawIdMap.keySet()
					.toArray();
			for (int k = 0; k < keys.length; k++) {
				mSoundPoolMap.put(
						keys[k],
						soundPool.load(context,
								mSoundPoolKeyRawIdMap.get(keys[k]), 1));
			}
		}
	}

	/**
	 * 获取音效
	 * 
	 * @param Res
	 * @return
	 */
	private static int getVoiceRes(int Res) {
		return mSoundPoolMap.get(Res);
	}

	/**
	 * 暂停播放
	 * 
	 * @param res
	 */
	public static void pauseSoundPool() {
		try {
			soundPool.stop(stopRes);
			soundPool.release();
			loadVoice(_context, null);
		} catch (Exception e) {

		}
	}

}
