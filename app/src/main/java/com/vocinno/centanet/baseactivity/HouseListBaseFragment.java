package com.vocinno.centanet.baseactivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.dialog.ModelDialog;
import com.vocinno.centanet.housemanage.HouseType;
import com.vocinno.centanet.housemanage.adapter.KeyHouseListAdapter;
import com.vocinno.centanet.housemanage.adapter.MyHouseListAdapter;
import com.vocinno.centanet.myinterface.GetDataInterface;
import com.vocinno.centanet.tools.constant.NetWorkConstant;
import com.vocinno.centanet.tools.customview.ProgressLayout;
import com.vocinno.utils.MethodsJni;
import com.vocinno.utils.view.refreshablelistview.XListView;

import java.util.Arrays;
import java.util.List;

public abstract class HouseListBaseFragment extends Fragment implements  XListView.IXListViewListener {
    public ProgressLayout pl_progress;
    public static final int NEAR_SELL=0;
    public static final int NEAR_RENT=1;
    public static final int YUE_KAN=2;
    public static final int MY_SELL=3;
    public static final int MY_RENT=4;
    public static final int ROB_GONG_SHOU=0;
    public static final int ROB_GONG_ZU=1;
    public MethodsJni methodsJni;
    public String URL;
    public Activity mContext = null;
    public Handler mHander = null;
    public ModelDialog modelDialog;
    public XListView XHouseListView;
    public MyHouseListAdapter houseListAdapter;
    public KeyHouseListAdapter keyHouseListAdapter;
    public boolean isBackDismiss=true;
    public GetDataInterface getDataInterface;
    public final int LIST_REFRESH=0;
    public final int LIST_LOADMORE=1;
    public boolean firstLoading=true;
    public int viewPosition;
    public ImageView iv_scroll_top;
    /******************数据查询条件************************/
    public int page=1;
    public int pageSize=20;
    public String delType="s";
    public int type= HouseType.CHU_SHOU;
    public String price="";
    public String square="";
    public String frame="";
    public String tag="";
    public String usageType="";
    public String sidx="";          //排序字段
    public String sord="";          //排序顺序
    public String searchId="";
    public String searchType="";
    public String searchBuildingName="";
    public String searchRoomNo="";
    /*************************************************/
    /*******************View***************************/
    public View baseView=null;
    /*******************抽象方法***************************/
    public abstract int setContentLayoutId();
    public abstract void initView();
    public abstract void addNotification();
    public abstract void initData();
    public abstract Handler setHandler();
    public abstract void setFirstLoading();
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
        price=getText(R.string.house_price).toString();
        square=getText(R.string.house_square).toString();
        frame=getText(R.string.house_frame).toString();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int layoutId=setContentLayoutId();
        baseView=inflater.inflate(layoutId, null);
        setXListView();
        Arrays.asList(R.id.scrollView1);
        mHander = setHandler();
        if(getUserVisibleHint()){
            setUserVisibleHint(true);
        }else{
            setUserVisibleHint(false);
        }
        initView();
        listScrollTop();
        return baseView;
    }

    private void listScrollTop() {
        if(baseView.findViewById(R.id.iv_scroll_top)!=null){
            iv_scroll_top= (ImageView)baseView.findViewById(R.id.iv_scroll_top);
            iv_scroll_top.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    XHouseListView.smoothScrollToPosition(0);
                }
            });
            XHouseListView.setOnScrollListener(new XListView.OnXScrollListener() {
                @Override
                public void onXScrolling(View view) {

                }

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    switch (scrollState) {
                        case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                            if(XHouseListView.getFirstVisiblePosition() == 0){
                                iv_scroll_top.setVisibility(View.GONE);
                            }else{
                                iv_scroll_top.setVisibility(View.VISIBLE);
                            }
                            break;
                }
            }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });
        }

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
//            XHouseListView.setPullLoadEnable(false);
            XHouseListView.setDataEmpty();
//            setDataEmptyView(true);
        }else{
            page=1;
            if(list.size()< NetWorkConstant.pageSize){
                XHouseListView.setPullLoadEnable(false);
            }else{
                XHouseListView.setPullLoadEnable(true);
            }
//            setDataEmptyView(false);

        }
        XHouseListView.stopRefresh();
        houseListAdapter.setDataList(list);
        XHouseListView.setAdapter(houseListAdapter);
    }
    public void dataLoadMore(List list) {
        if(list==null||list.size()<=0){
            XHouseListView.setPullLoadEnable(false);
        }else{
            if(list.size()< NetWorkConstant.pageSize){
                XHouseListView.setPullLoadEnable(false);
            }
            houseListAdapter.addDataList(list);
            houseListAdapter.notifyDataSetChanged();
            page++;
        }
        XHouseListView.stopLoadMore();
    }
    public void keyHouseDataRefresh(List list) {
        if(list==null||list.size()<=0){
            XHouseListView.stopLoadMore();
            XHouseListView.setDataEmpty();
        }else{
            page=1;
            if(list.size()< NetWorkConstant.pageSize){
                XHouseListView.setPullLoadEnable(false);
            }else{
                XHouseListView.setPullLoadEnable(true);
            }

        }
        XHouseListView.stopRefresh();
        keyHouseListAdapter.setDataList(list);
        XHouseListView.setAdapter(keyHouseListAdapter);
    }
    public void keyHouseDtaLoadMore(List list) {
        if(list==null||list.size()<=0){
            XHouseListView.setPullLoadEnable(false);
        }else{
            if(list.size()< NetWorkConstant.pageSize){
                XHouseListView.setPullLoadEnable(false);
            }
            keyHouseListAdapter.addDataList(list);
            keyHouseListAdapter.notifyDataSetChanged();
            page++;
        }
        XHouseListView.stopLoadMore();
    }
    private void setXListView() {
        XHouseListView =(XListView)baseView.findViewById(R.id.xlistview_sell_activity);
        XHouseListView.setXListViewListener(this);
        XHouseListView.setPullLoadEnable(true);
        XHouseListView.setPullRefreshEnable(true);
    }

    public void showDialog(){
        if(this.modelDialog==null){
            this.modelDialog=ModelDialog.getModelDialog(getActivity());
        }
        if(ModelDialog.showTag==0){
            ModelDialog.showTag=1;
            this.modelDialog.show();
        }
    }
    public void dismissDialog(){
        isBackDismiss=false;
        if(this.modelDialog!=null&&this.modelDialog.isShowing()){
            ModelDialog.showTag=0;
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
    /**************************设置排序字段和排序顺序****************************/
    public void searchByOrderForList(String param,String order){
        sidx = param;
        sord = order;
    }
    /*public void searchByKeyWord(String sId,String sType){
        searchId = sId;
        searchType = sType;
    }*/
    public void searchByKeyWord(String sId,String sType,String buildingName,String roomNo){
        searchId = sId;
        searchType = sType;
        searchBuildingName=buildingName;
        searchRoomNo=roomNo;
    }
    public void resetSearchOtherTag(int tagIndex){
        switch (tagIndex){
            case 0:
                Log.i("price","=="+price);
                square =getText(R.string.house_square).toString();
                frame = getText(R.string.house_frame).toString();
                tag = "";
                usageType = "";
                break;
            case 1:
                Log.i("square","=="+square);
                price = getText(R.string.house_price).toString();
                frame =getText(R.string.house_frame).toString();
                tag = "";
                usageType = "";
                break;
            case 2:
                Log.i("frame","=="+frame);
                price =getText(R.string.house_price).toString();
                square = getText(R.string.house_square).toString();
                tag = "";
                usageType = "";
                break;
            case 3:
                Log.i("tag","=="+tag);
                price = getText(R.string.house_price).toString();
                square =getText(R.string.house_square).toString();
                frame = getText(R.string.house_frame).toString();
                usageType = "";
                break;
            case 4:
                Log.i("usageType","=="+usageType);
                price =getText(R.string.house_price).toString();
                square = getText(R.string.house_square).toString();
                frame = getText(R.string.house_frame).toString();
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
