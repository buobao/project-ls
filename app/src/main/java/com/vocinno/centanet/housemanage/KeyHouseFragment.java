package com.vocinno.centanet.housemanage;

import android.annotation.SuppressLint;
import android.os.Handler;

import com.squareup.okhttp.Request;
import com.vocinno.centanet.R;
import com.vocinno.centanet.baseactivity.HouseListBaseFragment;
import com.vocinno.centanet.housemanage.adapter.MyHouseListAdapter;
import com.vocinno.centanet.model.HouseItem;
import com.vocinno.centanet.model.HouseList;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.myinterface.GetDataInterface;
import com.vocinno.centanet.myinterface.HttpInterface;
import com.vocinno.centanet.tools.OkHttpClientManager;
import com.vocinno.centanet.tools.constant.NetWorkConstant;
import com.vocinno.centanet.tools.constant.NetWorkMethod;
import com.vocinno.utils.MethodsJson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("ValidFragment")
public class KeyHouseFragment extends HouseListBaseFragment implements HttpInterface {
    private List<HouseItem> listHouses;
    private boolean firstLoading=true;
    @Override
    public int setContentLayoutId() {
        return R.layout.activity_near_sell;
    }

    public KeyHouseFragment(GetDataInterface getData, int position) {
        getDataInterface=getData;
        this.viewPosition=position;
    }
    public KeyHouseFragment() {
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
            houseListAdapter = new MyHouseListAdapter(mContext, HouseType.YAO_SHI);
            houseListAdapter.setDataList(null);
            XHouseListView.setAdapter(houseListAdapter);
            type = HouseType.YAO_SHI;
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
        if(!isXListViewLoad){
            showDialog();
        }
//        getDataInterface.getListData("" + type, price, square, frame, tag, usageType, page, pageSize, sidx, sord, searchId, searchType);
        URL= NetWorkConstant.PORT_URL+ NetWorkMethod.houLookPlanListMobile;
        Map<String,String> map=new HashMap<String,String>();
        map.put(NetWorkMethod.type,type+"");
        map.put(NetWorkMethod.price,price);
        map.put(NetWorkMethod.square,square);
        map.put(NetWorkMethod.frame,frame);
        map.put(NetWorkMethod.tag,tag);
        map.put(NetWorkMethod.usageType,usageType);
        map.put(NetWorkMethod.page,page+"");
        map.put(NetWorkMethod.pageSize,pageSize+"");
        map.put(NetWorkMethod.sidx,sidx);
        map.put(NetWorkMethod.sord,sord);
        map.put(NetWorkMethod.searchId,searchId);
        map.put(NetWorkMethod.searchType,searchType);
        OkHttpClientManager.getAsyn(URL, map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                dismissDialog();
            }
            @Override
            public void onResponse(String response) {
                dismissDialog();
                JSReturn jsReturn = MethodsJson.jsonToJsReturn(response, HouseList.class);
                if (jsReturn.isSuccess()) {
                    int dataType = jsReturn.getParams().getIsAppend() ? 1 : 0;
                    setListData(dataType, jsReturn.getListDatas());
                }
            }
        });
       }
    @Override
    public Handler setHandler() {
        return null;
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
        type = HouseType.YAO_SHI;
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

    }
}
