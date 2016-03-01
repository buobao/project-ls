package com.vocinno.utils.media.camera;

import com.vocinno.centanet.R;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.imageutils.editor.ImageEditorActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

public class PreviewActivity extends Activity {
	private ImageView imageView;
	private ImageButton imageViewBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_preview);
		initView();
	}

	private void initView() {
		imageView = (ImageView) findViewById(R.id.previewImage);
		imageViewBtn = (ImageButton) findViewById(R.id.submitCameraBtn);
		imageViewBtn.setOnClickListener(new subMitCameraOnClick());
		imageView.setImageBitmap(MethodsDeliverData.mBitmap);
	}

	// 提交照片事件(提供给以后的集成)
	class subMitCameraOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			MethodsExtra.startActivity(PreviewActivity.this,
					ImageEditorActivity.class);
			finish();
		}
	}

	@Override
	public void onBackPressed() {
		MethodsDeliverData.mBitmap = null;
		MethodsExtra.startActivity(PreviewActivity.this, CameraActivity.class);
		finish();
	}
}