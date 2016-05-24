package com.vocinno.centanet.housemanage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.vocinno.centanet.R;
import com.vocinno.centanet.baseactivity.HouseListBaseFragment;
import com.vocinno.centanet.customermanage.MyCustomerDetailActivity;
import com.vocinno.centanet.housemanage.adapter.MyHouseListAdapter;
import com.vocinno.centanet.model.HouseItem;
import com.vocinno.centanet.model.HouseList;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.model.LookPlanInfo;
import com.vocinno.centanet.tools.Loading;
import com.vocinno.centanet.tools.MyToast;
import com.vocinno.centanet.tools.MyUtils;
import com.vocinno.centanet.tools.OkHttpClientManager;
import com.vocinno.centanet.tools.constant.NetWorkConstant;
import com.vocinno.centanet.tools.constant.NetWorkMethod;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsJson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
@SuppressLint("ValidFragment")
public class YueKanFragment extends HouseListBaseFragment{
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
    @Override
    public void initView() {
        ll_yuekan_layout = (LinearLayout) baseView.findViewById(R.id.ll_yuekan_layout);

        if (HouseListBaseFragment.YUE_KAN == viewPosition) {
//            initData();
        }
    }

    public YueKanFragment( int position) {
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
        getData(1, false,true);
    }

    @Override
    public void initData() {
        if (firstLoading) {
            houseListAdapter = new MyHouseListAdapter(mContext, HouseType.YUE_KAN);
            houseListAdapter.setDataList(null);
            XHouseListView.setAdapter(houseListAdapter);
            type = HouseType.YUE_KAN;
            getData(1, false,true);
        }
    }

    public void getData(int pageNo,boolean isXListViewLoad, final boolean isRefresh){
        if(!isXListViewLoad){
            Loading.show(getActivity());
        }
        URL= NetWorkConstant.PORT_URL+ NetWorkMethod.houLookPlanListMobile;
        Map<String,String> map=new HashMap<String,String>();
        map.put(NetWorkMethod.type,type+"");
        map.put(NetWorkMethod.listType,NetWorkMethod.APPO_HOULIST);
        map.put(NetWorkMethod.price,price);
        map.put(NetWorkMethod.square,square);
        map.put(NetWorkMethod.frame,frame);
        map.put(NetWorkMethod.tag,tag);
        map.put(NetWorkMethod.usageType,usageType);
        map.put(NetWorkMethod.page,pageNo+"");
        map.put(NetWorkMethod.pageSize,pageSize+"");
        map.put(NetWorkMethod.sidx,sidx);
        map.put(NetWorkMethod.sord,sord);
        map.put(NetWorkMethod.searchId,searchId);
        map.put(NetWorkMethod.searchType,searchType);
        OkHttpClientManager.getAsyn(URL, map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                XHouseListView.stopRefresh();
                XHouseListView.stopLoadMore();
                Loading.dismissLoading();
            }

            @Override
            public void onResponse(String response) {
                XHouseListView.stopRefresh();
                XHouseListView.stopLoadMore();
                Loading.dismissLoading();
                JSReturn jsReturn = MethodsJson.jsonToJsReturn(response, HouseList.class);
                if (jsReturn.isSuccess()) {
                    int dataType = 0;
                    if (!isRefresh) {
                        dataType = 1;
                    }
                    setListData(dataType, jsReturn.getListDatas());
                }else{
                    MyToast.showToast(jsReturn.getMsg());
                }
            }
        });
    }


    View viewItem;
    TextView tv_yuekan_date;
    LinearLayout ll_yuekan_item;
    ImageButton ib_yuekan_img;

    @Override
    public void setListData(int dataType, List lookList) {
        dismissDialog();
        firstLoading = false;
        switch (dataType) {
            case LIST_REFRESH:
                ll_yuekan_layout.removeAllViews();
                map = new HashMap<Integer, List>();
                map = mapSort(lookList);
                inflater = mContext.getLayoutInflater();
                int viewItemIndex=0;
                if(map.get(0)!=null&map.get(0).size()>0){
                    viewItem = inflater.inflate(R.layout.item_yuekan_layout, null);
                    tv_yuekan_date = (TextView) viewItem.findViewById(R.id.tv_yuekan_date);
                    ll_yuekan_item = (LinearLayout) viewItem.findViewById(R.id.ll_yuekan_item);
                    tv_yuekan_date.setText("今天");
                    tv_yuekan_date.setTextColor(Color.RED);
                    ll_yuekan_layout.addView(viewItem, viewItemIndex);
                    viewItemIndex++;

                    for (int i = 0; i < map.get(0).size(); i++) {
                        View viewEntry = addItemView((HouseItem)map.get(0).get(i));
                        ll_yuekan_item.addView(viewEntry, i);
                    }
                }
                if(map.get(1)!=null&&map.get(1).size()>0){
                    viewItem = inflater.inflate(R.layout.item_yuekan_layout, null);
                    tv_yuekan_date = (TextView) viewItem.findViewById(R.id.tv_yuekan_date);
                    ll_yuekan_item = (LinearLayout) viewItem.findViewById(R.id.ll_yuekan_item);
                    tv_yuekan_date.setText("明天");
                    ib_yuekan_img = (ImageButton) viewItem.findViewById(R.id.ib_yuekan_img);
                    ib_yuekan_img.setImageResource(R.drawable.calendar_unnormal);
                    ll_yuekan_layout.addView(viewItem, viewItemIndex);
                    viewItemIndex++;

                    for (int i = 0; i < map.get(1).size(); i++) {
                        View viewEntry = addItemView((HouseItem)map.get(1).get(i));
                        ll_yuekan_item.addView(viewEntry, i);
                    }
                }
                if(otherMap!=null&&otherMap.size()>0){
                    for (String key : otherMap.keySet()) {
                        viewItem = inflater.inflate(R.layout.item_yuekan_layout, null);
                        tv_yuekan_date = (TextView) viewItem.findViewById(R.id.tv_yuekan_date);
                        ll_yuekan_item = (LinearLayout) viewItem.findViewById(R.id.ll_yuekan_item);
                        tv_yuekan_date.setText(MyUtils.dateFormat(key,"MM/dd"));
                        ib_yuekan_img = (ImageButton) viewItem.findViewById(R.id.ib_yuekan_img);
                        ib_yuekan_img.setImageResource(R.drawable.calendar_unnormal);
                        ll_yuekan_layout.addView(viewItem,viewItemIndex);
                        viewItemIndex++;
//                        setTimeSort(otherMap.get(key));
                        for (int i = 0; i <otherMap.get(key).size(); i++) {
                            View viewEntry = addItemView((HouseItem)otherMap.get(key).get(i));
                            ll_yuekan_item.addView(viewEntry, i);
                        }
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
    private View addItemView(final HouseItem item) {
        List<LookPlanInfo> lookInfo = item.getLookInfo();
        View viewEntry = inflater.inflate(R.layout.item_yuekan_entry, null);
        LinearLayout ll_yuekan_kehu= (LinearLayout) viewEntry.findViewById(R.id.ll_yuekan_kehu);
        final TextView tv_yuekan_custcode = (TextView) viewEntry.findViewById(R.id.tv_yuekan_custcode);
        tv_yuekan_custcode.setText(item.getCustCode());
        ll_yuekan_kehu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MyCustomerDetailActivity.class);
                intent.putExtra("custCode", item.getCustCode());
                startActivity(intent);
            }
        });
        TextView tv_yuekan_name = (TextView) viewEntry.findViewById(R.id.tv_yuekan_name);
        tv_yuekan_name.setText(item.getPlanDirection());
        TextView tv_yuekan_time = (TextView) viewEntry.findViewById(R.id.tv_yuekan_time);
        tv_yuekan_time.setText(MyUtils.dateFormat(item.getStartDate()) + "--" + MyUtils.dateFormat(item.getEndDate()));
        TextPaint tp = tv_yuekan_time.getPaint();
        tp.setFakeBoldText(true);
        /***********************************************************************************************/
        for (int i = 0; i <lookInfo.size(); i++) {
            final LookPlanInfo info = lookInfo.get(i);
            View viewHouseItem = inflater.inflate(R.layout.item_yuekan_houseitem, null);
            LinearLayout ll_yuekan_fangyuan= (LinearLayout) viewHouseItem.findViewById(R.id.ll_yuekan_fangyuan);
            ll_yuekan_fangyuan.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent=new Intent(mContext, HouseDetailActivity.class);
                    MethodsDeliverData.mDelCode=info.getHOUSE_DEL_CODE();
                    intent.putExtra("houseCode", info.getHOUSE_DEL_CODE());
                    intent.putExtra(MyUtils.INTO_FROM_LIST,true);
                    startActivityForResult(intent,10);
                }
            });
            TextView tv_yuekan_loupan = (TextView) viewHouseItem.findViewById(R.id.tv_yuekan_loupan);
            TextView tv_yuekan_dong = (TextView) viewHouseItem.findViewById(R.id.tv_yuekan_dong);
            TextView tv_yuekan_gaodi = (TextView) viewHouseItem.findViewById(R.id.tv_yuekan_gaodi);
            tv_yuekan_loupan.setText(info.getESTATE_NAME());
            tv_yuekan_dong.setText(info.getBUILDING_NAME()+"栋");
        /*if (item.ishidden()) {
            tv_yuekan_gaodi.setVisibility(View.GONE);
        } else {*/
            tv_yuekan_gaodi.setVisibility(View.VISIBLE);
            tv_yuekan_gaodi.setText(info.getFloor()+"层");
//        }
            ll_yuekan_kehu.addView(viewHouseItem);
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
        HouseItem item1;
        setDateSort(list);
        for (int i = 0; i < list.size(); i++) {
            item1 = (HouseItem) list.get(i);
            if (MyUtils.compareNowDate(item1.getPlanDate()) == 0) {
                itemList.add(item1);
            } else if (MyUtils.compareNowDate(item1.getPlanDate()) == 1) {
                itemList1.add(item1);
            } else {
//                itemList2.add(item1);
                if(otherMap==null){
                    otherMap=new LinkedHashMap<String,List>();
                }
                if(otherMap.containsKey(item1.getPlanDate())){
                    otherMap.get(item1.getPlanDate()).add(item1);
                }else{
                    List<HouseItem>iList=new ArrayList<HouseItem>();
                    iList.add(item1);
                    otherMap.put(item1.getPlanDate(),iList);
                }
            }
        }
        map.put(0, itemList);//今天
        map.put(1, itemList1);//明天
//        map.put(2, itemList2);//其他日期
        return map;
    }

    private void setDateSort(List list) {
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object lhs, Object rhs) {
                HouseItem item1 = (HouseItem) lhs;
                HouseItem item2 = (HouseItem) rhs;
                int i=item1.getPlanDate().compareTo(item2.getPlanDate());
                if(i==0){
                    int j=item1.getStartDate().compareTo(item2.getStartDate());
                    if(j==0){
                        int k=item1.getEndDate().compareTo(item2.getEndDate());
                        if(k==0){
                            return item1.getRmdCustTime().compareTo(item2.getRmdCustTime());
                        }
                        return k;
                    }
                    return j;
                }
                return i;
            }
        });
    }



    @Override
    public Handler setHandler() {
        return null;
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
        getData(1, true,true);
    }

    @Override
    public void onLoadMore() {
        getData(page + 1, true,false);
    }


}
