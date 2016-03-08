package com.vocinno.centanet.apputils.selfdefineview;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;

public class DoublePictureDrawable extends Drawable {
	// 图片资源
	private Bitmap mBitmap = null;
	// 画笔
	private Paint mPaint = null;
	// 宽度和高度
	private int mWidth, mHeight;

	public DoublePictureDrawable(Bitmap bitmap, int screenW, int screenH) {
		mBitmap = bitmap;
		BitmapShader bitmapShader = new BitmapShader(bitmap, TileMode.REPEAT,
				TileMode.MIRROR);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setShader(bitmapShader);
		mWidth = screenW;
		mHeight = screenH;
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawRect(0, 0, mWidth, mHeight, mPaint);
	}

	@Override
	public int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}

	@Override
	public void setAlpha(int alpha) {
		mPaint.setAlpha(alpha);
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		mPaint.setColorFilter(cf);
	}

}
