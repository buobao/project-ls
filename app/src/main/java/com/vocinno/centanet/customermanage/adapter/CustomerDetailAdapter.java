package com.vocinno.centanet.customermanage.adapter;

import java.util.List;

import com.vocinno.centanet.R;
import com.vocinno.centanet.model.Track;
import com.vocinno.utils.MethodsData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class CustomerDetailAdapter extends BaseAdapter {
	private List<Track> mListTracks;
	private Context mContext;

	public CustomerDetailAdapter(Context context, List<Track> listTracks) {
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
					R.layout.item_track_customer_detail_listview, null);
			trackView = new TrackView();
			trackView.tvTitle = (TextView) arg1
					.findViewById(R.id.tv_title_itemTrackCustomerDetailListView);
			trackView.tvTime = (TextView) arg1
					.findViewById(R.id.tv_time_itemTrackCustomerDetailListView);
			trackView.viewLine = arg1
					.findViewById(R.id.view_line_itemTrackCustomerDetailListView);
			arg1.setTag(trackView);
		} else {
			trackView = (TrackView) arg1.getTag();
		}
		trackView.tvTime.setText("时间：" + mListTracks.get(arg0).getTracktime());
		trackView.tvTitle.setText("内容：" + mListTracks.get(arg0).getContent());
		if (arg0 == mListTracks.size() - 1) {
			LayoutParams params = (LayoutParams) trackView.viewLine
					.getLayoutParams();
			if (params == null) {
				params = new LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, 1);
			}
			params.setMargins(0, MethodsData.dip2px(mContext, 6), 0, 0);
			trackView.viewLine.setLayoutParams(params);
		}
		return arg1;
	}

	class TrackView {
		private TextView tvTitle, tvTime;
		private View viewLine;
	}

}
