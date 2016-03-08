package com.vocinno.centanet.housemanage;

import java.util.List;

import com.vocinno.centanet.R;
import com.vocinno.centanet.model.Track;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HouseDetailTrackAdapter extends BaseAdapter {
	private List<Track> mListTracks;
	private Context mContext;

	public HouseDetailTrackAdapter(Context context, List<Track> listTracks) {
		mContext = context;
		mListTracks = listTracks;
	}

	@Override
	public int getCount() {
		if (mListTracks == null) {
			return 0;
		}
		return mListTracks.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mListTracks.get(arg0);
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
					R.layout.item_track_house_detail_listview, null);
			trackView = new TrackView();
			trackView.tvTitle = (TextView) arg1
					.findViewById(R.id.tv_title_itemTrackHouseDetailListView);
			trackView.tvTime = (TextView) arg1
					.findViewById(R.id.tv_time_itemTrackHouseDetailListView);
			trackView.tvUser = (TextView) arg1
					.findViewById(R.id.tv_user_itemTrackHouseDetailListView);
			arg1.setTag(trackView);
		} else {
			trackView = (TrackView) arg1.getTag();
		}
		trackView.tvTime.setText("时间：" + mListTracks.get(arg0).getTime());
		trackView.tvTitle.setText("信息：" + mListTracks.get(arg0).getMsg());
		trackView.tvUser.setText("跟进人：" + mListTracks.get(arg0).getUser());
		return arg1;
	}

	class TrackView {
		private TextView tvTitle, tvTime, tvUser;
	}

}
