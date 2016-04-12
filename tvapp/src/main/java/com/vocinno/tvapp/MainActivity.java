package com.vocinno.tvapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    private WebView wv_mainpage;
//    private String pageUrl = "http://trade.sh.centaline.com.cn/trade-web/mobile/dashboard/box/select";
//    private String pageUrl = "http://10.4.19.205:8083/trade-web//mobile/dashboard/box/showRLightList?orgId=FF5BC56E0E4B45289DAA5721A494C7C5";
    private String pageUrl = "http://trade.sh.centaline.com.cn/trade-web/mobile/dashboard/box/showRLightList?orgId=FF5BC56E0E4B45289DAA5721A494C7C5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        wv_mainpage = (WebView) findViewById(R.id.wv_mainpage);
        wv_mainpage.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        wv_mainpage.loadUrl(pageUrl);
        WebSettings s = wv_mainpage.getSettings();
      /*  int screenDensity = getResources().getDisplayMetrics().densityDpi ;
        WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.MEDIUM ;
        switch (screenDensity){
            case DisplayMetrics.DENSITY_LOW :
                zoomDensity = WebSettings.ZoomDensity.CLOSE;
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                zoomDensity = WebSettings.ZoomDensity.MEDIUM;
                break;
            case DisplayMetrics.DENSITY_HIGH:
                zoomDensity = WebSettings.ZoomDensity.FAR;
                break ;
        }
        s.setDefaultZoom(zoomDensity);*/
//        s.setBuiltInZoomControls(true);
//        s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        // 获取当前界面的高度
        //int width = dm.widthPixels;
        //int height = dm.heightPixels;
        int scale = dm.densityDpi;
        if (scale == 240) {
            s.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (scale == 160) {
           s.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else {
            s.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        }

        s.setUseWideViewPort(true);
        s.setLoadWithOverviewMode(true);
        s.setSavePassword(true);
        s.setSaveFormData(true);
        s.setJavaScriptEnabled(true);
        s.setGeolocationEnabled(true);
        s.setDomStorageEnabled(true);
        wv_mainpage.requestFocus();
        wv_mainpage.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

    }

}
