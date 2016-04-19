package com.vocinno.centanet.baseactivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.dialog.ModelDialog;
import com.vocinno.centanet.housemanage.HouseType;
import com.vocinno.centanet.housemanage.adapter.MyHouseListAdapter;
import com.vocinno.utils.MethodsJni;
import com.vocinno.utils.view.refreshablelistview.XListView;

public abstract class HouseListBaseFragment extends Fragment implements  XListView.IXListViewListener {
    public MethodsJni methodsJni;
    public Activity mContext = null;
    public Handler mHander = null;
    public ModelDialog modelDialog;
    public XListView XHouseListView;
    public MyHouseListAdapter houseListAdapter;
    /******************数据查询条件************************/
    public int page=1;
    public int pageSize=20;
    public String delType="s";
    public int type= HouseType.CHU_SHOU;
    public String price="0-不限";
    public String square="0-不限";
    public String frame="不限-不限-不限-不限";
    public String tag="";
    public String usageType="";
    public String sidx="";
    public String sord="";
    public String searchId="";
    public String searchType="";
    /*************************************************/
    /*******************View***************************/
    public View baseView=null;
    public TextView tv_empty_listview;
    /*******************抽象方法***************************/
    public abstract int setContentLayoutId();
    public abstract void initView();
    public abstract void addNotification();
    public abstract void initData();
    public abstract Handler setHandler();
    public String TAG;
//    public abstract void notifCallBack(final String name,final String className, final Object data);
    /********************************************************/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        TAG=this.getClass().getName();
        methodsJni=new MethodsJni();
        addNotification();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int layoutId=setContentLayoutId();
        baseView=inflater.inflate(layoutId, null);
        setXListView();
        initView();
        mHander = setHandler();
        setClickListener();
        initData();
        return baseView;
    }

    private void setXListView() {
        XHouseListView =(XListView)baseView.findViewById(R.id.xlistview_sell_activity);
        XHouseListView.setXListViewListener(this);

        tv_empty_listview=(TextView)baseView.findViewById(R.id.tv_empty_listview);
    }

    private void setClickListener() {

    }
    public void showDialog(){
        if(this.modelDialog==null){
            this.modelDialog=ModelDialog.getModelDialog(getActivity());
        }
        this.modelDialog.show();
    }
    public void dismissDialog(){
        if(this.modelDialog!=null&&this.modelDialog.isShowing()){
            this.modelDialog.dismiss();
        }
    }

   /* @Override
    public void onResume() {
        super.onResume();
        JPushInterface.onResume(mContext);
    }
    @Override
    public void onPause() {
        super.onPause();
        JPushInterface.onPause(mContext);
    }*/
}
