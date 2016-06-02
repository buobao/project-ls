package com.zbar.lib.unify.http;

import android.graphics.Bitmap;

public interface IImageDownloader {
	public void getBitmap(int statusCode, Bitmap bitmap, String msg);
}
