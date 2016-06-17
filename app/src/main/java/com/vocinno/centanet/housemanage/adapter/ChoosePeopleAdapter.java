package com.vocinno.centanet.housemanage.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vocinno.centanet.R;
import com.vocinno.centanet.model.ChoosePeople;

import java.util.List;

/**
 * Created by Administrator on 2016/4/29.
 */
public class ChoosePeopleAdapter extends BaseAdapter {

    private Activity mContext;
    private List<ChoosePeople> peopleList;
    public ChoosePeopleAdapter(Activity context) {
        mContext = context;
    }
    public void setList(List<ChoosePeople> list){
        peopleList = list;
        notifyDataSetChanged();
    }
    public void addList(List<ChoosePeople> list){
        if(peopleList==null||peopleList.size()==0){
            peopleList=list;
        }else{
            peopleList.addAll(list);
        }
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return peopleList==null?0:peopleList.size();
    }
    @Override
    public ChoosePeople getItem(int position) {
        return peopleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_choose_people, null);
            holder = new ViewHolder();
            holder.tv_people_name = (TextView) convertView
                    .findViewById(R.id.tv_people_name);
            holder.tv_people_jobname = (TextView) convertView
                    .findViewById(R.id.tv_people_jobname);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ChoosePeople item=peopleList.get(position);
        holder.tv_people_name.setText(item.getRealName());
        holder.tv_people_jobname.setText("("+item.getOrgName()+"/"+item.getJobName()+")");
        return convertView;
    }

    private class ViewHolder {
        TextView tv_people_name,tv_people_jobname;
    }
}
