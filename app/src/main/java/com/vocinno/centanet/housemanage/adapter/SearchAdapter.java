package com.vocinno.centanet.housemanage.adapter;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vocinno.centanet.R;
import com.vocinno.centanet.model.EstateSearchItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/29.
 */
public class SearchAdapter extends BaseAdapter {

    private Activity mContext;
    private List<EstateSearchItem> mListTexts=new ArrayList<EstateSearchItem>();
    private String colorText;
    public SearchAdapter(Activity context, List<EstateSearchItem> listTexts) {
        mContext = context;
        mListTexts = listTexts;
    }
    public void setList(List<EstateSearchItem> listTexts){
        mListTexts = listTexts;
    }
    @Override
    public int getCount() {
        return mListTexts.size();
    }
    public void setColorText(String str){
        colorText=str;
    }
    @Override
    public Object getItem(int position) {
        return mListTexts.get(position);
    }

    public List<EstateSearchItem> getListData() {
        return mListTexts;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.listitem_search_house_manage_dialog, null);
            holder = new SearchHolder();
            holder.mTvSearchText = (TextView) convertView
                    .findViewById(R.id.tv_text_listitemSearchHouseManageDialog);
            holder.tv_tel_cust = (TextView) convertView
                    .findViewById(R.id.tv_tel_cust);
            convertView.setTag(holder);
        } else {
            holder = (SearchHolder) convertView.getTag();
        }
        if(colorText!=null){
            String searchName="<font color='red'>"+colorText+"</font>";
            String name=mListTexts.get(position).getSearchName();
            StringBuffer sb=new StringBuffer(name);
            int nameIndex=name.indexOf(colorText);
            sb=sb.replace(nameIndex,nameIndex+colorText.length(),searchName);
            holder.mTvSearchText.setText(Html.fromHtml(sb.toString()));
        }else{
            holder.mTvSearchText.setText(mListTexts.get(position).getSearchName());
        }
        if(mListTexts.get(position).getMpNo()!=null&&mListTexts.get(position).getMpNo().toString().trim().length()>0){
            holder.tv_tel_cust.setText(mListTexts.get(position).getMpNo());
        }else{
            holder.tv_tel_cust.setText("");
        }
        return convertView;
    }

    class SearchHolder {
        TextView mTvSearchText;
        TextView tv_tel_cust;
    }
}
