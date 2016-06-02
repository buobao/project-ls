package com.vocinno.centanet.apputils.utils.web.webview;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.SuperActivity;
import com.vocinno.centanet.apputils.utils.MethodsDeliverData;
import com.vocinno.centanet.apputils.utils.MethodsThirdParty;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.http.SslError;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class WebviewActivity extends SuperActivity implements OnClickListener {
	private Animation animation;
	private WebView mWebView;
	private ProgressBar mProgressBarLoading;
	private ImageView mImageViewBack, mImageViewShare, mImageViewRefresh,
			mImageViewRefreshing, mImageViewClose;

	@Override
	public Handler setHandler() {
		return null;
	}

	@Override
	public int setContentLayoutId() {
		return R.layout.activity_webview;
	}

	@Override
	public void initView() {
		mWebView = (WebView) findViewById(R.id.webview_webviewActivity);
		mImageViewBack = (ImageView) findViewById(R.id.img_back_footerWebview);
		mImageViewShare = (ImageView) findViewById(R.id.img_share_footerWebview);
		mImageViewRefresh = (ImageView) findViewById(R.id.img_refresh_footerWebview);
		mImageViewRefreshing = (ImageView) findViewById(R.id.img_refreshRotating_footerWebview);
		mImageViewClose = (ImageView) findViewById(R.id.img_close_footerWebview);
		mProgressBarLoading = (ProgressBar) findViewById(R.id.pb_loading_webviewActivity);
		animation = AnimationUtils.loadAnimation(WebviewActivity.this,
				R.anim.rotate_refresh);
	}

	@Override
	public void setListener() {
		mImageViewBack.setOnClickListener(this);
		mImageViewShare.setOnClickListener(this);
		mImageViewRefresh.setOnClickListener(this);
		mImageViewClose.setOnClickListener(this);
	}

	@Override
	public void initData() {
		initWebView();
		mWebView.loadUrl(MethodsDeliverData.currentWebViewURL);
	}

	@Override
	public void onBack() {
		mWebView.goBack();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back_footerWebview:
			mWebView.goBack();
			break;
		case R.id.img_close_footerWebview:
			finish();
			break;
		case R.id.img_share_footerWebview:
			// 分享
			// MethodsExtra.toast(this, "分享");
			MethodsThirdParty
					.shareOut(this, "分享内容",
							"http://y1.ifengimg.com/a/2015_27/968e7e5d559fcf8_size64_w880_h587.jpg");
			break;
		case R.id.img_refresh_footerWebview:
			mWebView.loadUrl(mWebView.getUrl());
			break;
		default:
			break;
		}
	}

	/*
	 * 初始化WebView
	 */
	private void initWebView() {
		mWebView.clearCache(true);
		mWebView.clearHistory();
		mProgressBarLoading.setVisibility(View.VISIBLE);
		// 为WebView设置WebViewClient处理某些操作
		WebSettings settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(true);// 支持javascript
		settings.setLoadWithOverviewMode(true);
		settings.setUseWideViewPort(true);
		settings.setPluginState(PluginState.ON);
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		settings.setAllowFileAccess(true);
		settings.setAppCacheEnabled(true);
		settings.setDefaultTextEncodingName("UTF-8");
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		mWebView.setBackgroundColor(0);
		// 隐藏滚动条
		mWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		mWebView.setWebViewClient(new webViewClient());
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onShowCustomView(View view, CustomViewCallback callback) {
				super.onShowCustomView(view, callback);
				view.draw(new Canvas());
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				if (newProgress == 100) {
					mProgressBarLoading.setVisibility(View.GONE);
				} else {
					// 横向进度条
					mProgressBarLoading.setProgress(newProgress);
					mProgressBarLoading.postInvalidate();
					if (mProgressBarLoading.getVisibility() == View.GONE) {
						mProgressBarLoading.setVisibility(View.VISIBLE);
					}
					System.out.println("+++加载中..." + newProgress);
				}
			}
		});
	}

	class webViewClient extends WebViewClient {
		// 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			System.out.println("shouldOverrideUrlLoading:" + url);
			return super.shouldOverrideUrlLoading(view, url);
		}

		// 开始加载网页时要做的工作
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			mImageViewRefresh.setVisibility(View.GONE);
			mImageViewRefreshing.startAnimation(animation);
			mImageViewRefreshing.setVisibility(View.VISIBLE);
			super.onPageStarted(view, url, favicon);
		}

		// 加载完成时要做的工作
		@Override
		public void onPageFinished(WebView view, String url) {
			mImageViewRefresh.setVisibility(View.VISIBLE);
			mImageViewRefreshing.setVisibility(View.GONE);
			mImageViewRefreshing.clearAnimation();
			super.onPageFinished(view, url);
		}

		// 重写此方法可以让webview处理https请求
		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {
			handler.proceed();
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
		}
	}

	@Override
	public void notifCallBack(String name, String className, Object data) {

	}

}
