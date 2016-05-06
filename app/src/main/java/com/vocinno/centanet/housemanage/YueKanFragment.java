package com.vocinno.centanet.housemanage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.MyUtils;
import com.vocinno.centanet.apputils.cst.CST_JS;
import com.vocinno.centanet.baseactivity.HouseListBaseFragment;
import com.vocinno.centanet.customermanage.MyCustomerDetailActivity;
import com.vocinno.centanet.housemanage.adapter.MyHouseListAdapter;
import com.vocinno.centanet.model.HouseItem;
import com.vocinno.centanet.model.HouseList;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.model.LookPlanInfo;
import com.vocinno.centanet.myinterface.GetDataInterface;
import com.vocinno.centanet.myinterface.HttpInterface;
import com.vocinno.utils.MethodsJson;

import org.unify.helper.CELibHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
@SuppressLint("ValidFragment")
public class YueKanFragment extends HouseListBaseFragment implements HttpInterface {
    private List<HouseItem> listHouses;
    private boolean firstLoading = true;
    private LinearLayout ll_yuekan_layout;
    private Map<Integer, List> map;
    private Map<String, List> otherMap;
    private LayoutInflater inflater;

    @Override
    public int setContentLayoutId() {
        return R.layout.activity_yue_kan;
    }

    CELibHelper ceLibHelper;

    @Override
    public void initView() {
        ll_yuekan_layout = (LinearLayout) baseView.findViewById(R.id.ll_yuekan_layout);

        if (HouseListBaseFragment.YUE_KAN == viewPosition) {
            initData();
        }
    }

    public YueKanFragment(GetDataInterface getData, int position) {
        getDataInterface = getData;
        this.viewPosition = position;
    }
    public YueKanFragment() {
    }
    @Override
    public void addNotification() {
        TAG = this.getClass().getName();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (firstLoading) {
                /*type = HouseType.YUE_KAN;
                getData(1,false);*/
            }
        } else {
        }
    }

    public void searchForList(int tagIndex, String param) {
        switch (tagIndex) {
            case 0:
                price = param;
                break;
            case 1:
                square = param;
                break;
            case 2:
                frame = param;
                break;
            case 3:
                tag = param;
                break;
            case 4:
                usageType = param;
                break;
        }
        resetSearchOtherTag(tagIndex);
        getData(1, false);
    }

    @Override
    public void initData() {
        if (firstLoading) {
            houseListAdapter = new MyHouseListAdapter(mContext, HouseType.YUE_KAN);
            houseListAdapter.setDataList(null);
            XHouseListView.setAdapter(houseListAdapter);
            type = HouseType.YUE_KAN;
            getData(1, false);
        }
    }

    public void getData(int page, boolean isXListViewLoad) {
//        methodsJni.setMethodsJni((HttpInterface)this);
        if (!isXListViewLoad) {
            showDialog();
        }
        getDataInterface.getListData("" + type, price, square, frame, tag, usageType, page,
                pageSize, sidx, sord, searchId, searchType);
        /*MethodsJni.callProxyFun(CST_JS.JS_ProxyName_HouseResource,
                CST_JS.JS_Function_HouseResource_getList, CST_JS
                        .getJsonStringForHouseListGetList("" + type, price, square, frame, tag, usageType, page,
                                pageSize, sidx, sord, searchId, searchType));*/
    }


    View viewItem;
    TextView tv_yuekan_date;
    LinearLayout ll_yuekan_item;
    ImageButton ib_yuekan_img;

    @Override
    public void setListData(int dataType, List lookList) {
        List<LookPlanInfo>list=new ArrayList<LookPlanInfo>();
        LookPlanInfo info;
        for(int i=0;i<lookList.size();i++){
            for (int j = 0; j < ((HouseItem)lookList.get(i)).getLookInfo().size(); j++) {
                LookPlanInfo  lookInfo = ((HouseItem) lookList.get(i)).getLookInfo().get(j);
                info=new LookPlanInfo();
                info.setAddr(((HouseItem) lookList.get(i)).getAddr());
                info.setDelCode(((HouseItem) lookList.get(i)).getDelCode());
                info.setIshidden(((HouseItem) lookList.get(i)).ishidden());
                info.setFloor(((HouseItem) lookList.get(i)).getFloor());
                info.setBuilding_name(((HouseItem)lookList.get(i)).getBuilding_name());
                info.setCUST_CODE(lookInfo.getCUST_CODE());
                info.setEndtime(lookInfo.getEndtime());
                info.setStarttime(lookInfo.getStarttime());
                info.setPlan_date(lookInfo.getPlan_date());
                info.setPLAN_DIRECTION(lookInfo.getPLAN_DIRECTION());
                Log.i("i=" + i, j + "=j" + lookInfo.getPLAN_DIRECTION());
                info.setHOUSE_ID(lookInfo.getHOUSE_ID());
                list.add(info);
                Log.i("info" + i, j + "info" + info.getPLAN_DIRECTION());
            }
        }
        dismissDialog();
        firstLoading = false;
        switch (dataType) {
            case LIST_REFRESH:
                ll_yuekan_layout.removeAllViews();
                map = new HashMap<Integer, List>();
                map = mapSort(list);
                inflater = mContext.getLayoutInflater();
                int viewItemIndex=0;
                if(map.get(0).size()>0){
                    viewItem = inflater.inflate(R.layout.item_yuekan_layout, null);
                    tv_yuekan_date = (TextView) viewItem.findViewById(R.id.tv_yuekan_date);
                    ll_yuekan_item = (LinearLayout) viewItem.findViewById(R.id.ll_yuekan_item);
                    tv_yuekan_date.setText("今天");
                    tv_yuekan_date.setTextColor(Color.RED);
                    ll_yuekan_layout.addView(viewItem, viewItemIndex);
                    viewItemIndex++;
                }

                for (int i = 0; i < map.get(0).size(); i++) {
                    View viewEntry = addItemView((LookPlanInfo)map.get(0).get(i));
                    ll_yuekan_item.addView(viewEntry, i);
                }

                if(map.get(1).size()>0){
                    viewItem = inflater.inflate(R.layout.item_yuekan_layout, null);
                    tv_yuekan_date = (TextView) viewItem.findViewById(R.id.tv_yuekan_date);
                    ll_yuekan_item = (LinearLayout) viewItem.findViewById(R.id.ll_yuekan_item);
                    tv_yuekan_date.setText("明天");
                    ib_yuekan_img = (ImageButton) viewItem.findViewById(R.id.ib_yuekan_img);
                    ib_yuekan_img.setImageResource(R.drawable.calendar_unnormal);
                    ll_yuekan_layout.addView(viewItem, viewItemIndex);
                    viewItemIndex++;
                }
                for (int i = 0; i < map.get(1).size(); i++) {
                    View viewEntry = addItemView((LookPlanInfo)map.get(1).get(i));
                    ll_yuekan_item.addView(viewEntry, i);
                }

                for (String key : otherMap.keySet()) {
                    viewItem = inflater.inflate(R.layout.item_yuekan_layout, null);
                    tv_yuekan_date = (TextView) viewItem.findViewById(R.id.tv_yuekan_date);
                    ll_yuekan_item = (LinearLayout) viewItem.findViewById(R.id.ll_yuekan_item);
                    tv_yuekan_date.setText(MyUtils.dateFormat(key,"MM/dd"));
                    ib_yuekan_img = (ImageButton) viewItem.findViewById(R.id.ib_yuekan_img);
                    ib_yuekan_img.setImageResource(R.drawable.calendar_unnormal);
                    ll_yuekan_layout.addView(viewItem,viewItemIndex);
                    viewItemIndex++;
                    setTimeSort(otherMap.get(key));
                    for (int i = 0; i <otherMap.get(key).size(); i++) {
                        View viewEntry = addItemView((LookPlanInfo)otherMap.get(key).get(i));
                        ll_yuekan_item.addView(viewEntry, i);
                    }
                }
//                dataRefresh(list);
                break;
            case LIST_LOADMORE:
                //*/ dataLoadMore(list);
                break;
        }
    }

    @NonNull
    private View addItemView(final LookPlanInfo item) {
        View viewEntry = inflater.inflate(R.layout.item_yuekan_entry, null);
        LinearLayout ll_yuekan_kehu= (LinearLayout) viewEntry.findViewById(R.id.ll_yuekan_kehu);
        LinearLayout ll_yuekan_fangyuan= (LinearLayout) viewEntry.findViewById(R.id.ll_yuekan_fangyuan);
        TextView tv_yuekan_time = (TextView) viewEntry.findViewById(R.id.tv_yuekan_time);
        TextView tv_yuekan_name = (TextView) viewEntry.findViewById(R.id.tv_yuekan_name);
        final TextView tv_yuekan_custcode = (TextView) viewEntry.findViewById(R.id.tv_yuekan_custcode);
        TextView tv_yuekan_loupan = (TextView) viewEntry.findViewById(R.id.tv_yuekan_loupan);
        TextView tv_yuekan_dong = (TextView) viewEntry.findViewById(R.id.tv_yuekan_dong);
        TextView tv_yuekan_gaodi = (TextView) viewEntry.findViewById(R.id.tv_yuekan_gaodi);
        tv_yuekan_time.setText(MyUtils.dateFormat(item.getStarttime()) + "--" + MyUtils.dateFormat(item.getEndtime()));
        TextPaint tp = tv_yuekan_time.getPaint();
        tp.setFakeBoldText(true);
        tv_yuekan_name.setText(item.getPLAN_DIRECTION());
        tv_yuekan_custcode.setText(item.getCUST_CODE());
        ll_yuekan_kehu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MyCustomerDetailActivity.class);
                intent.putExtra("custCode", item.getCUST_CODE());
                startActivity(intent);
//                MethodsExtra.toast(mContext,tv_yuekan_custcode.getText()+"");
            }
        });
        ll_yuekan_fangyuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, HouseDetailActivity.class);
                intent.putExtra("custCode", item.getDelCode());
                intent.putExtra(MyUtils.INTO_FROM_LIST,true);
                startActivityForResult(intent,10);
            }
        });
        tv_yuekan_loupan.setText(item.getAddr());
        tv_yuekan_dong.setText(item.getBuilding_name());
        if (item.ishidden()) {
            tv_yuekan_gaodi.setVisibility(View.GONE);
        } else {
            tv_yuekan_gaodi.setVisibility(View.VISIBLE);
            tv_yuekan_gaodi.setText(item.getFloor());
        }
        return viewEntry;
    }

    private Map mapSort(List list) {
        if(map!=null){
            map.clear();
        }
        if(otherMap!=null){
            otherMap.clear();
        }
        List itemList = new ArrayList();
        List itemList1 = new ArrayList();
        List itemList2 = new ArrayList();
        LookPlanInfo item1;
        setDateSort(list);
        for (int i = 0; i < list.size(); i++) {
            item1 = (LookPlanInfo) list.get(i);
            if (MyUtils.compareNowDate(item1.getPlan_date()) == 0) {
                itemList.add(item1);
            } else if (MyUtils.compareNowDate(item1.getPlan_date()) == 1) {
                itemList1.add(item1);
            } else {
//                itemList2.add(item1);
                if(otherMap==null){
                    otherMap=new LinkedHashMap<String,List>();
                }
                if(otherMap.containsKey(item1.getPlan_date())){
                    otherMap.get(item1.getPlan_date()).add(item1);
                }else{
                    List<LookPlanInfo>iList=new ArrayList<LookPlanInfo>();
                    iList.add(item1);
                    otherMap.put(item1.getPlan_date(),iList);
                }
            }
        }
        setTimeSort(itemList);
        setTimeSort(itemList1);
        setTimeSort(itemList2);
        map.put(0, itemList);//今天
        map.put(1, itemList1);//明天
//        map.put(2, itemList2);//其他日期
        return map;
    }

    private void setTimeSort(List list) {
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object lhs, Object rhs) {
                LookPlanInfo item1 = (LookPlanInfo) lhs;
                LookPlanInfo item2 = (LookPlanInfo) rhs;
                int i=item1.getStarttime().compareTo(item2.getStarttime());
                if(i==0){
                    return item1.getEndtime().compareTo(item2.getEndtime());
                }
                return i;
            }
        });
    }
    private void setDateSort(List list) {
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object lhs, Object rhs) {
                LookPlanInfo item1 = (LookPlanInfo) lhs;
                LookPlanInfo item2 = (LookPlanInfo) rhs;
                return item1.getPlan_date().compareTo(item2.getPlan_date());
            }
        });
    }

    @Override
    public void netWorkResult(String name, String className, Object data) {
        dismissDialog();
        //页面刷新
        if (name.equals(CST_JS.NOTIFY_NATIVE_HOU_LIST_RESULT)
                || name.equals(CST_JS.NOTIFY_NATIVE_HOU_LIST_SEARCH_RESULT)) {
            JSReturn jsReturn = MethodsJson.jsonToJsReturn((String) data,
                    HouseList.class);
            int type = Integer.parseInt(jsReturn.getParams().getType());
            Message msg = new Message();
            if (jsReturn.isSuccess()) {
                if (jsReturn.getParams().getIsAppend()) {
                    if (jsReturn.getListDatas() == null || jsReturn.getListDatas().size() <= 0) {
                        XHouseListView.stopLoadMore();
                        XHouseListView.setPullLoadEnable(false);
                    } else {
                        houseListAdapter.addDataList(jsReturn.getListDatas());
                        houseListAdapter.notifyDataSetChanged();
                        XHouseListView.stopLoadMore();
                        page++;
                    }
                } else {
                    firstLoading = false;
                    page = 1;
                    XHouseListView.stopRefresh();
                    XHouseListView.setPullLoadEnable(true);
                    houseListAdapter.setDataList(jsReturn.getListDatas());
                    XHouseListView.setAdapter(houseListAdapter);
                    if (jsReturn.getListDatas() == null || jsReturn.getListDatas().size() <= 0) {
                        XHouseListView.setPullLoadEnable(false);
                        setDataEmptyView(true);
                    } else {
                        setDataEmptyView(false);
                    }
                }
            }
        } else if (false) {

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
    public void resetSearch() {
        page = 1;
        pageSize = 20;
        delType = "";
        type = HouseType.YUE_KAN;
        price = "0-不限";
        square = "0-不限";
        frame = "不限-不限-不限-不限";
        tag = "";
        usageType = "";
        sidx = "";
        sord = "";
        searchId = "";
        searchType = "";
    }

    @Override
    public void onRefresh() {
        resetSearch();
        getData(1, true);
    }

    @Override
    public void onLoadMore() {
        getData(page + 1, true);
    }


}
