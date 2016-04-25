package com.vocinno.centanet.housemanage;

import android.os.Handler;
import android.os.Message;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.cst.CST_JS;
import com.vocinno.centanet.baseactivity.HouseListBaseFragment;
import com.vocinno.centanet.housemanage.adapter.MyHouseListAdapter;
import com.vocinno.centanet.model.HouseItem;
import com.vocinno.centanet.model.HouseList;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.myinterface.GetDataInterface;
import com.vocinno.centanet.myinterface.HttpInterface;
import com.vocinno.utils.MethodsJson;

import java.util.List;


public class NearSellFragment extends HouseListBaseFragment implements HttpInterface {
//    private static String TAG = null;
    private List<HouseItem> listHouses;
    private boolean firstLoading=true;
//    private MethodsJni methodsJni;
    @Override
    public int setContentLayoutId() {
        return R.layout.activity_near_sell;
    }

    public NearSellFragment(GetDataInterface getData,int position) {
        getDataInterface=getData;
        this.viewPosition=position;
    }

    @Override
    public void initView() {
        if(HouseListBaseFragment.NEAR_SELL==viewPosition){
            initData();
        }
    }
    @Override
    public void addNotification() {
        TAG=this.getClass().getName();
    }

    @Override
    public void initData() {
        if(firstLoading){
            houseListAdapter = new MyHouseListAdapter(mContext, HouseType.CHU_SHOU);
            houseListAdapter.setDataList(null);
            XHouseListView.setAdapter(houseListAdapter);
            type = HouseType.CHU_SHOU;
            getData(1, false);
        }
    }
    public void searchForList(int tagIndex,String param){
        switch (tagIndex){
            case 0:
                price=param;
                break;
            case 1:
                square=param;
                break;
            case 2:
                frame=param;
                break;
            case 3:
                tag=param;
                break;
            case 4:
                usageType=param;
                break;
        }
        resetSearchOtherTag(tagIndex);
        getData(1, false);
    }
    public void getData(int page,boolean isXListViewLoad){
//        methodsJni.setMethodsJni((HttpInterface) this);
        if(!isXListViewLoad){
            showDialog();
        }
        getDataInterface.getListData("" + type, price, square, frame, tag, usageType, page, pageSize, sidx, sord, searchId, searchType);
       }
    @Override
    public Handler setHandler() {
        return new Handler() {
            @Override
            public void handleMessage(Message msg) {
                dismissDialog();
                switch (msg.what) {
                    case R.id.doSuccess:
                        break;
                }
            }
        };
    }

    @Override
    public void setListData(int dataType,List list) {
        dismissDialog();
        firstLoading=false;
        switch (dataType){
            case LIST_REFRESH:
                dataRefresh(list);
                break;
            case LIST_LOADMORE:
                dataLoadMore(list);
                break;
        }
    }




    //重置搜索条件
    public void resetSearch(){
        page = 1;
        pageSize = 20;
        delType = "s";
        type = HouseType.CHU_SHOU;
        price = "0-不限";
        square = "0-不限";
        frame = "不限-不限-不限-不限";
        tag = "";
        usageType = "";
        sidx = "";
        sord = "";
        searchId = "";
        searchType = "";
    };
    @Override
    public void onRefresh() {
        resetSearch();
        getData(1,true);
    }

    @Override
    public void onLoadMore() {
        getData(page+1,true);
    }

    @Override
    public void netWorkResult(String name, String className, Object data) {
//        methodsJni.setMethodsJni(null);
        //页面刷新
        if(name.equals(CST_JS.NOTIFY_NATIVE_HOU_LIST_RESULT)
                || name.equals(CST_JS.NOTIFY_NATIVE_HOU_LIST_SEARCH_RESULT)){
            JSReturn jsReturn = MethodsJson.jsonToJsReturn((String) data,
                    HouseList.class);
            int type = Integer.parseInt(jsReturn.getParams().getType());
            Message msg = new Message();
            if (jsReturn.isSuccess()) {
                if (jsReturn.getParams().getIsAppend()) {
                    if(jsReturn.getListDatas()==null||jsReturn.getListDatas().size()<=0){
                        XHouseListView.stopLoadMore();
                        XHouseListView.setPullLoadEnable(false);
                    }else{
                        houseListAdapter.addDataList(jsReturn.getListDatas());
                        houseListAdapter.notifyDataSetChanged();
                        XHouseListView.stopLoadMore();
                        page++;
                    }
                } else {
                    if(jsReturn.getListDatas()==null||jsReturn.getListDatas().size()<=0){
//                        XHouseListView.setEmptyFooter();
//                        XHouseListView.setPullLoadEnable(false);
                    }else{
                        firstLoading=false;
                        houseListAdapter.setDataList(jsReturn.getListDatas());
                        XHouseListView.setAdapter(houseListAdapter);
                        page=1;
                        XHouseListView.setPullLoadEnable(true);
                        XHouseListView.stopRefresh();
                    }

                }
            }
        }else if(false){
        }
        dismissDialog();
    }
}
