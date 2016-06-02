package com.vocinno.centanet.apputils.utils.timer;

import java.util.Timer;
import java.util.TimerTask;

public final class TimerManager {
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
		final Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			int leftTimes = times;

			@Override
			public void run() {
				timerCallback.timerCallback();
			}
		};
		if (isRepeat) {
			timer.schedule(task, delay, period);
		} else {
			timer.schedule(task, delay);
		}
		return timer;
	}

	public static void cancelTimer(Timer timer) {
		if (timer != null) {
			timer.cancel();
		}
	}
}
