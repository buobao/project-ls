package com.vocinno.centanet.tools;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.dialog.MyDialog;
import com.vocinno.utils.MethodsExtra;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/5/9.
 */
public class DownloadApp {
    private Context context;
    private String apkFile;
    private MyDialog alterDialog;

    public DownloadApp(Context context) {
        this.context = context;
    }
    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;
    private TextView tv_progress;
    private Dialog downloadDialog;
    private boolean interceptFlag = false;
    private Thread downLoadThread;
    private String savePath;
    private int progress;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;

    public void showDownloadDialog() {
        if(isNetworkConnected(context)){
            final LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.progress_update_client, null);
            mProgress = (ProgressBar) v.findViewById(R.id.progress);
            tv_progress = (TextView) v.findViewById(R.id.tv_progress);
            tv_progress.setText("0");
            /*****************************************/
            MyDialog.Builder myDialog=new MyDialog.Builder(context);
            myDialog.setTitle("软件版本更新");
            myDialog.setMessage(null);
            myDialog.setContentView(v);
            myDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            myDialog.setDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    interceptFlag = true;
                }
            });
            alterDialog = myDialog.create();
            alterDialog.show();
            /*****************************************/
        /*final MaterialDialog materialDialog=new MaterialDialog(context);
        materialDialog.setTitle("软件版本更新");
        materialDialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();

            }
        });
        materialDialog.setCanceledOnTouchOutside(false);
        materialDialog.setContentView(v);*/
//        materialDialog.show();
            downloadApk();
        }else{
            MethodsExtra.toast(context, "当前无网络连接,请稍后再试");
            return;
        }

    }
    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }
    public void setPath(String path){
        savePath=path;
    }
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    tv_progress.setText(progress + "");
                    break;
                case DOWN_OVER:
                    installApk();
                    break;
                default:
                    break;
            }
        };
    };
   /* public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }*/
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return info.isAvailable();
                }
            }
        }
        return false;
    }
    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(context.getString(R.string.download_url));

                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.setConnectTimeout(8000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Connection", "Keep-Alive"); // 设置为持久连接
//                conn.setReadTimeout(8000);
                conn.connect();
                int responseCode = conn.getResponseCode();
                Log.i("responseCode","responseCode+==="+responseCode);
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdir();
                }/* else {
                    file.delete();
                }*/
                apkFile = savePath+"/"+context.getString(R.string.apk_name);
                File ApkFile = new File(apkFile);
                FileOutputStream fos = new FileOutputStream(ApkFile);

                int count = 0;
                byte buf[] = new byte[1024];

                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    // 更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        alterDialog.dismiss();
                        // 下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!interceptFlag);// 点击取消就停止下载.
                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };
    private void installApk() {
        File apkfile = new File(apkFile);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");
        context.startActivity(i);

    }
}
