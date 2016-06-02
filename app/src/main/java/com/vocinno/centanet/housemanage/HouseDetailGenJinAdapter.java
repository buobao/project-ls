package com.vocinno.centanet.housemanage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vocinno.centanet.R;
import com.vocinno.centanet.model.Track;

import java.util.List;

public class HouseDetailGenJinAdapter extends BaseAdapter {
	private List<Track> trackList;
	private Context mContext;
	public HouseDetailGenJinAdapter(Context context) {
		mContext = context;
	}
	public void setList(List<Track> listTracks){
		trackList = listTracks;
	}
	@Override
	public int getCount() {
		if (trackList == null) {
			return 0;
		}
		return trackList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return trackList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		TrackView trackView = null;
		if (arg1 == null) {
			arg1 = LayoutInflater.from(mContext).inflate(
					R.layout.item_house_detail_genjin, null);
			trackView = new TrackView();
			trackView.tv_genjin_time = (TextView) arg1
					.findViewById(R.id.tv_genjin_time);
			trackView.tv_genjin_name = (TextView) arg1
					.findViewById(R.id.tv_genjin_name);
			trackView.tv_genjin_content = (TextView) arg1
					.findViewById(R.id.tv_genjin_content);
			arg1.setTag(trackView);
		} else {
			trackView = (TrackView) arg1.getTag();
		}
		Track track = trackList.get(arg0);
		trackView.tv_genjin_time.setText(  track.getTime());
		trackView.tv_genjin_name.setText( track.getUser());
		trackView.tv_genjin_content.setText(  track.getMsg());
		return arg1;
	}
	class TrackView {
		private TextView tv_genjin_time, tv_genjin_name, tv_genjin_content;
	}

}
