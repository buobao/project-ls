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
import com.vocinno.centanet.myinterface.HttpInterFace;
import com.vocinno.utils.MethodsJni;
import com.vocinno.utils.MethodsJson;

import org.unify.helper.CELibHelper;

import java.util.List;

public class MyRentFragment extends HouseListBaseFragment implements HttpInterFace {

    private List<HouseItem> listHouses;
    private boolean firstLoading=true;
    @Override
    public int setContentLayoutId() {
        return R.layout.activity_near_sell;
    }
    CELibHelper ceLibHelper;
    @Override
    public void initView() {

    }

    @Override
    public void addNotification() {
        TAG=this.getClass().getName();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if(firstLoading){
                type = HouseType.WO_DEZU2;
                getData(1,false);
            }
        }else{
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
    @Override
    public void initData() {
        houseListAdapter = new MyHouseListAdapter(mContext, HouseType.WO_DEZU2);
        XHouseListView.setPullLoadEnable(true);
    }
    public void getData(int page,boolean isXListViewLoad){
        methodsJni.setMethodsJni((HttpInterFace)this);
        if(!isXListViewLoad){
            showDialog();
        }
        MethodsJni.callProxyFun(CST_JS.JS_ProxyName_HouseResource,
                CST_JS.JS_Function_HouseResource_getList, CST_JS
                        .getJsonStringForHouseListGetList("" + type, price, square, frame, tag, usageType, page,
                                pageSize, sidx, sord, searchId, searchType));
    }
    @Override
    public void netWorkResult(String name, String className, Object data) {
        methodsJni.setMethodsJni(null);
        dismissDialog();
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
                    firstLoading=false;
                    houseListAdapter.setDataList(jsReturn.getListDatas());
                    XHouseListView.setAdapter(houseListAdapter);
                    page=1;
                    XHouseListView.setPullLoadEnable(true);
                    XHouseListView.stopRefresh();
                }
            }
        }else if(false){

        }
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
    //重置搜索条件
    public void resetSearch(){
        page = 1;
        pageSize = 20;
        delType = "r";
        type = HouseType.WO_DEZU2;
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
}
