package com.vocinno.centanet.baseactivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.dialog.ModelDialog;
import com.vocinno.centanet.housemanage.HouseType;
import com.vocinno.centanet.housemanage.adapter.MyHouseListAdapter;
import com.vocinno.centanet.myinterface.GetDataInterface;
import com.vocinno.utils.MethodsJni;
import com.vocinno.utils.view.refreshablelistview.XListView;

import java.util.List;

public abstract class HouseListBaseFragment extends Fragment implements  XListView.IXListViewListener {
    public MethodsJni methodsJni;
    public Activity mContext = null;
    public Handler mHander = null;
    public ModelDialog modelDialog;
    public XListView XHouseListView;
    public MyHouseListAdapter houseListAdapter;
    public boolean isBackDismiss=true;
    public GetDataInterface getDataInterface;
    public final int LIST_REFRESH=0;
    public final int LIST_LOADMORE=1;
    public boolean firstLoading=true;
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
        mHander = setHandler();
        if(getUserVisibleHint()){
            setUserVisibleHint(true);
        }else{
            setUserVisibleHint(false);
        }
        return baseView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if(baseView!=null){
                initData();
            }
        }
    }
    public void dataRefresh(List list) {
        if(list==null||list.size()<=0){
            XHouseListView.stopLoadMore();
            XHouseListView.setPullLoadEnable(false);
            setDataEmptyView(true);
        }else{
            setDataEmptyView(false);
            page=1;
            XHouseListView.setPullLoadEnable(true);
        }
        XHouseListView.stopRefresh();
        houseListAdapter.setDataList(list);
        XHouseListView.setAdapter(houseListAdapter);
    }
    public void dataLoadMore(List list) {
        if(list==null||list.size()<=0){
            XHouseListView.setPullLoadEnable(false);
        }else{
            houseListAdapter.addDataList(list);
            houseListAdapter.notifyDataSetChanged();
            page++;
        }
        XHouseListView.stopLoadMore();
    }
    private void setXListView() {
        XHouseListView =(XListView)baseView.findViewById(R.id.xlistview_sell_activity);
        XHouseListView.setXListViewListener(this);
        XHouseListView.setPullLoadEnable(true);
        XHouseListView.setPullRefreshEnable(true);
        tv_empty_listview=(TextView)baseView.findViewById(R.id.tv_empty_listview);
    }

    public void setDataEmptyView(boolean isShow) {
        if(isShow){
            tv_empty_listview.setVisibility(View.VISIBLE);
        }else{
            tv_empty_listview.setVisibility(View.GONE);
        }
    }
    public void showDialog(){
        if(this.modelDialog==null){
            this.modelDialog=ModelDialog.getModelDialog(getActivity());
        }
        this.modelDialog.show();
    }
    public void dismissDialog(){
        isBackDismiss=false;
        if(this.modelDialog!=null&&this.modelDialog.isShowing()){
            this.modelDialog.dismiss();
        }
    }
    public void dismissDialog(boolean backDismiss){
        if(backDismiss){
            isBackDismiss=true;
            if(this.modelDialog!=null&&this.modelDialog.isShowing()){
                this.modelDialog.dismiss();
            }
        }
    }
    public void searchByOrderForList(String param,String order){
        sidx = param;
        sord = order;
    }
    public void searchByKeyWord(String sId,String sType){
        searchId = sId;
        searchType = sType;
    }
    public void resetSearchOtherTag(int tagIndex){
        switch (tagIndex){
            case 0:
                Log.i("price","=="+price);
                square = "0-不限";
                frame = "不限-不限-不限-不限";
                tag = "";
                usageType = "";
                break;
            case 1:
                Log.i("square","=="+square);
                price = "0-不限";
                frame = "不限-不限-不限-不限";
                tag = "";
                usageType = "";
                break;
            case 2:
                Log.i("frame","=="+frame);
                price = "0-不限";
                square = "0-不限";
                tag = "";
                usageType = "";
                break;
            case 3:
                Log.i("tag","=="+tag);
                price = "0-不限";
                square = "0-不限";
                frame = "不限-不限-不限-不限";
                usageType = "";
                break;
            case 4:
                Log.i("usageType","=="+usageType);
                price = "0-不限";
                square = "0-不限";
                frame = "不限-不限-不限-不限";
                tag = "";
                break;
        }
    }
    public abstract void setListData(int dataType,List list);
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
