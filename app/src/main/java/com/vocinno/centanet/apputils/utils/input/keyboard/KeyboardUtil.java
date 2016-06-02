package com.vocinno.centanet.apputils.utils.input.keyboard;

import java.util.ArrayList;

import com.vocinno.centanet.R;

import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.view.View;
import android.widget.EditText;

public class KeyboardUtil {
	private Activity mActivity;
	private View view;
	private KeyboardView keyboardView;
	private Keyboard kb_num_only;

	private ArrayList<EditText> listEd;
	private String thisPwdText = "";

	public KeyboardUtil(Activity activity, View view) {
		this.view = view;
		kb_num_only = new Keyboard(activity, R.xml.number_only);
		keyboardView = (KeyboardView) view
				.findViewById(R.id.keyboard_keyGetInActivity);
		keyboardView.setKeyboard(kb_num_only);
		keyboardView.setEnabled(true);
		keyboardView.setPreviewEnabled(false);
		keyboardView.setOnKeyboardActionListener(listener);
	}

	private OnKeyboardActionListener listener = new OnKeyboardActionListener() {
		@Override
		public void swipeUp() {
		}

		@Override
		public void swipeRight() {
		}

		@Override
		public void swipeLeft() {
		}

		@Override
		public void swipeDown() {
		}

		@Override
		public void onText(CharSequence text) {
		}

		@Override
		public void onRelease(int primaryCode) {
		}

		@Override
		public void onPress(int primaryCode) {
		}

		@Override
		public void onKey(int primaryCode, int[] keyCodes) {
			if (primaryCode == -2) {
				return;
			} else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
				// 删除按钮所做的动作
				if (thisPwdText != null && thisPwdText.length() >= 1) {
					thisPwdText = thisPwdText.substring(0,
							thisPwdText.length() - 1);
					System.out.println("thisPwdText=" + thisPwdText);
					int len = thisPwdText.length();
					if (len <= 3) {
						listEd.get(len).setText("");
					}
				}
			} else if (thisPwdText == null || thisPwdText.length() < 4) {
				thisPwdText = thisPwdText + (char) primaryCode;
				System.out.println("thisPwdText=" + thisPwdText);
				int len = thisPwdText.length();
				for (int i = 0; i < len; i++) {
					listEd.get(i).setText(thisPwdText.substring(i, i + 1));
				}
				listEd.get(4).setText(thisPwdText);
			}
		}
	};

	/**
	 * 包括四个密码输入框和一个密码保存框(按此顺序即可)
	 * 
	 * @param etList
	 */
	public void setListEditText(ArrayList<EditText> etList) {
		this.listEd = etList;
	}

	// 显示键盘
	public void showKeyboard() {
		int visibility = keyboardView.getVisibility();
		if (visibility == View.GONE || visibility == View.INVISIBLE) {
			keyboardView.setVisibility(View.VISIBLE);
		}
	}

	// 隐藏键盘
	public void hideKeyboard() {
		int visibility = keyboardView.getVisibility();
		if (visibility == View.VISIBLE) {
			keyboardView.setVisibility(View.INVISIBLE);
		}
	}
}