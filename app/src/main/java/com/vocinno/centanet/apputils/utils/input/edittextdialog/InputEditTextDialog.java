package com.vocinno.centanet.apputils.utils.input.edittextdialog;

import java.util.Timer;
import java.util.TimerTask;

import com.vocinno.centanet.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class InputEditTextDialog extends Dialog {

	private static final String TAG = "InputEditTextDialog";
	private LinearLayout wholelayout;
	private EditText inputEditText;
	private Button confirmBtn;
	private InputEditTextListener mSendDialogTextListener;

	private int currentHight = 1;
	private String preContent = "";
	private boolean isShow = false;

	public InputEditTextDialog(Context context, String preContent,
			InputEditTextListener sendDialogTextListener) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		this.preContent = preContent;
		mSendDialogTextListener = sendDialogTextListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_input_edit_text);
		initView();
		initListener();

		final Window window = this.getWindow();

		// 取消全屏，显示标题栏
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		// 监听屏幕的高度，当键盘隐藏时，Dialog也隐藏
		window.getDecorView()
				.getViewTreeObserver()
				.addOnGlobalLayoutListener(
						new ViewTreeObserver.OnGlobalLayoutListener() {
							@Override
							public void onGlobalLayout() {
								Rect r = new Rect();
								window.getDecorView()
										.getWindowVisibleDisplayFrame(r);
								int screenHeight = getWindow().getDecorView()
										.getRootView().getHeight();
								currentHight = screenHeight - r.bottom;
								if (isShow == true && currentHight < 1) {
									InputEditTextDialog.this.dismiss();
								}
							}
						});

		Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				InputMethodManager inputManager =

				(InputMethodManager) inputEditText.getContext()
						.getSystemService(Context.INPUT_METHOD_SERVICE);

				inputManager.showSoftInput(inputEditText, 0);
				isShow = true;
			}
		};

		timer.schedule(task, 500);

	}

	private void initView() {
		wholelayout = (LinearLayout) findViewById(R.id.input_text_dialog_layout);
		inputEditText = (EditText) findViewById(R.id.input_text_dialog_edit_text);
		inputEditText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE
				| InputType.TYPE_CLASS_TEXT);
		inputEditText.setSingleLine(false);
		inputEditText.setHorizontallyScrolling(false);
		inputEditText.requestFocus();
		confirmBtn = (Button) findViewById(R.id.input_text_dialog_confirm_btn);
		if (!preContent.equals("")) {
			inputEditText.setText(preContent);
		}

	}

	private void initListener() {
		wholelayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				InputEditTextDialog.this.dismiss();
			}
		});

		confirmBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String content = inputEditText.getText().toString().trim();
				mSendDialogTextListener.onSend(content);
				inputEditText.setText("");
				InputEditTextDialog.this.dismiss();
			}
		});
	}

}
