package com.vocinno.centanet.customermanage;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.squareup.okhttp.Request;
import com.vocinno.centanet.R;
import com.vocinno.centanet.baseactivity.OtherBaseActivity;
import com.vocinno.centanet.database.DBManager;
import com.vocinno.centanet.housemanage.adapter.ChoosePeopleAdapter;
import com.vocinno.centanet.model.ChoosePeople;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.tools.Loading;
import com.vocinno.centanet.tools.OkHttpClientManager;
import com.vocinno.centanet.tools.constant.MyConstant;
import com.vocinno.centanet.tools.constant.NetWorkConstant;
import com.vocinno.centanet.tools.constant.NetWorkMethod;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsJson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客源管理
 *
 * @author Administrator
 */
public class ChoosePeopleActivity extends OtherBaseActivity {
    private Dialog mMenuDialog, mSearchDialog;
    private View mBack;
    private ChoosePeopleAdapter peopleAdapter,choosePeopleAdapter;
    private List<ChoosePeople> peopleList;
    private LinearLayout ll_choose_people_search;
    private ListView lv_choose_people;
    @Override
    public Handler setHandler() {
        return null;
    }

    @Override
    public int setContentLayoutId() {
        return R.layout.activity_choose_people;
    }

    @Override
    public void initView() {
        intent = getIntent();
        MethodsExtra.findHeadTitle1(mContext, baseView,
                R.string.choose_people, null);
        mBack = MethodsExtra.findHeadLeftView1(mContext, baseView, 0, 0);
        mBack.setOnClickListener(this);
        lv_choose_people = (ListView) findViewById(R.id.lv_choose_people);
        lv_choose_people.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChoosePeople choosePeople = peopleAdapter.getItem(position);
                try {
                    saveData(choosePeople);
                }catch (Exception e){
                    goBack(choosePeople);
                }
                goBack(choosePeople);
            }
        });
        ll_choose_people_search = (LinearLayout) findViewById(R.id.ll_choose_people_search);
        ll_choose_people_search.setOnClickListener(this);

//        et_choose_people_search = (EditText) findViewById(R.id.et_choose_people_search);
//        et_choose_people_search.setOnClickListener(this);
    }

    @Override
    public void initData() {
        choosePeopleAdapter = new ChoosePeopleAdapter(this);
        queryData();
    }

    private void queryData() {
        peopleAdapter = new ChoosePeopleAdapter(this);
        peopleAdapter.setList(DBManager.getDBManager(this).queryAll());
        lv_choose_people.setAdapter(peopleAdapter);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_choose_people_search:
                showSearchDialog();
                break;
            case R.id.img_left_mhead1:        //标题右侧按钮
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void onLoadMore() {
    }

    @Override
    public void netWorkResult(String name, String className, Object data) {
    }


    private ListView lv_choose_search;
    public static EditText et_choose_search;

    private void showSearchDialog() {
        mSearchDialog = new Dialog(mContext, R.style.Theme_dialog);
        mSearchDialog.setContentView(R.layout.dialog_choose_search);
        Window win = mSearchDialog.getWindow();
        win.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        win.setGravity(Gravity.TOP);
        mSearchDialog.setCanceledOnTouchOutside(true);
        lv_choose_search = (ListView) mSearchDialog.findViewById(R.id.lv_choose_search);
        choosePeopleAdapter.setList(null);
        lv_choose_search.setAdapter(choosePeopleAdapter);
        et_choose_search = (EditText) mSearchDialog.findViewById(R.id.et_choose_search);
        et_choose_search.setHint(getText(R.string.search_choose_hit));
        et_choose_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                searchKeYuan(et_choose_search.getText().toString().trim());
            }
        });

        lv_choose_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
                ChoosePeople choosePeople = choosePeopleAdapter.getItem(position);
                mSearchDialog.dismiss();
                try {
                    saveData(choosePeople);
                }catch (Exception e){
                    goBack(choosePeople);
                }
                goBack(choosePeople);
            }
        });
        mSearchDialog.show();
        showKeyBoard();
    }

    private void saveData(ChoosePeople choosePeople) {
        int count = DBManager.getDBManager(this).queryPeople(choosePeople);
        if(count>0){
            DBManager.getDBManager(this).updatePeople(choosePeople);
        }else{
            DBManager.getDBManager(this).addPeople(choosePeople);
        }
        if(DBManager.getDBManager(this).queryCount()>5){
            DBManager.getDBManager(this).deletePeople();
        }
    }

    private void goBack(ChoosePeople choosePeople) {
        intent.putExtra(MyConstant.choose_people,choosePeople);
        setResult(MyConstant.CHOOSE_PEOPLE,intent);
        finish();
    }

    private void showKeyBoard() {
        et_choose_search.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputManager =
                        (InputMethodManager) et_choose_search.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(et_choose_search, 0);
            }
        }, 100);
    }

    private void searchKeYuan(String editString) {
        if (editString == null || editString.length() <= 0) {
                choosePeopleAdapter.setList(null);
                lv_choose_search.setVisibility(View.GONE);
            } else {
//           Loading.show(this);
                URL = NetWorkConstant.PORT_URL + NetWorkMethod.custlookUser;
                Map<String, String> map = new HashMap<String, String>();
                map.put(NetWorkMethod.accompanyPerson, editString);
                OkHttpClientManager.getAsyn(URL, map, new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Loading.dismissLoading();
                    }

                    @Override
                    public void onResponse(String response) {
                        Loading.dismissLoading();
                        JSReturn jReturn = MethodsJson.jsonToJsReturn(response, ChoosePeople.class);
                        if (jReturn.getListDatas() != null && jReturn.getListDatas().size() > 0) {
                            choosePeopleAdapter.setList(jReturn.getListDatas());
                            lv_choose_search.setAdapter(choosePeopleAdapter);
                            lv_choose_search.setVisibility(View.VISIBLE);
                        } else {
                            lv_choose_search.setVisibility(View.GONE);
                        }
                    }
                });
            }
    }
}
