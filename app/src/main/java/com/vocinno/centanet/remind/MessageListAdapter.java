package com.vocinno.centanet.remind;


import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vocinno.centanet.R;
import com.vocinno.centanet.customermanage.MyCustomerDetailActivity;
import com.vocinno.centanet.housemanage.HouseDetailActivity;
import com.vocinno.centanet.model.MessageItem;
import com.vocinno.centanet.apputils.utils.MethodsDeliverData;
import com.vocinno.centanet.apputils.utils.MethodsExtra;

import java.util.List;

public class MessageListAdapter extends BaseAdapter {

	private static Context mContext;
	private List<MessageItem> mListMessages;

	public MessageListAdapter() {
	}

	public MessageListAdapter(Context mContext) {
		MessageListAdapter.mContext = mContext;
	}

	public void setListDatas(List<MessageItem> listItems) {
		this.mListMessages = listItems;
		notifyDataSetChanged();
	}

	public MessageItem getItemData(int position) {
		return mListMessages.get(position);
	}

	@Override
	public int getCount() {
		if (mListMessages == null) {
			return 0;
		}
		return mListMessages.size();
	}

	@Override
	public Object getItem(int index) {
		return mListMessages.get(index);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_my_remind_list_my_remind_activity, null);
			holder.tvTitle = (TextView) convertView
					.findViewById(R.id.tv_title_MessageListAdapter);
			holder.tvTime = (TextView) convertView
					.findViewById(R.id.tv_time_MessageListAdapter);
			holder.tvContent = (TextView) convertView
					.findViewById(R.id.tv_content_MessageListAdapter);
			holder.ivHeader = (ImageView) convertView
					.findViewById(R.id.img_remindPic_MessageListAdapter);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String type = mListMessages.get(position).getMsgType();
		if (type.equals("10064001")) {
			holder.tvTitle.setText(R.string.remind_zhisun);
			holder.ivHeader.setImageDrawable(mContext.getResources()
					.getDrawable(R.drawable.remind_icon_remind));
		} else if (type.equals("10064002")) {
			holder.tvTitle.setText(R.string.remind_chengjkiao);
			holder.ivHeader.setImageDrawable(mContext.getResources()
					.getDrawable(R.drawable.remind_icon_deal));
		} else {
			holder.tvTitle.setText(R.string.remind_dongtai);
			holder.ivHeader.setImageDrawable(mContext.getResources()
					.getDrawable(R.drawable.remind_icon_dynamic));
		}

		setTextViewWithKeyWords(holder.tvContent, mListMessages.get(position)
				.getMsgContent());
		holder.tvTime.setText(mListMessages.get(position).getMsgTime());
		return convertView;
	}

	private void setTextViewWithKeyWords(TextView textView, String strContent) {
		textView.setMovementMethod(LinkMovementMethod.getInstance());
		String strTagDelCodeStart = "<delCode>";
		String strTagDelCodeEnd = "</delCode>";
		int intDelCodeStartLen = 9;
		int intDelCodeEndLen = 10;
		String strTagCustCodeStart = "<custCode>";
		String strTagCustCodeEnd = "</custCode>";
		int intCustCodeStartLen = 10;
		int intCustCodeEndLen = 11;
		textView.setText("");
		while (strContent.contains(strTagDelCodeStart)
				|| strContent.contains(strTagCustCodeStart)) {
			
			int indexDelCode = strContent.indexOf(strTagDelCodeStart);
			int indexCustCode = strContent.indexOf(strTagCustCodeStart);
			final String strCodeWithTag, strCode;
			if (indexDelCode <= -1
					|| (indexCustCode >= 0 && indexDelCode > indexCustCode)) {
				// custCode
				textView.append(strContent.substring(0, indexCustCode));
				strCodeWithTag = strContent.substring(indexCustCode,
						strContent.indexOf(strTagCustCodeEnd)
								+ intCustCodeEndLen);
				strContent = strContent.substring(
						strContent.indexOf(strTagCustCodeEnd)
								+ intCustCodeEndLen, strContent.length());
				strCode = strCodeWithTag.substring(intCustCodeStartLen,
						strCodeWithTag.length() - intCustCodeEndLen);
				SpannableString spanString = MethodsExtra.getClickableSpanner(
						strCode, new OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent=new Intent(mContext,MyCustomerDetailActivity.class);
								intent.putExtra("custCode",strCode);
								mContext.startActivity(intent);

							}
						});
				textView.append(spanString);
			} else {
				// delCode
				textView.append(strContent.substring(0, indexDelCode));
				strCodeWithTag = strContent
						.substring(indexDelCode,
								strContent.indexOf(strTagDelCodeEnd)
										+ intDelCodeEndLen);
				strContent = strContent
						.substring(strContent.indexOf(strTagDelCodeEnd)
								+ intDelCodeEndLen, strContent.length());
				strCode = strCodeWithTag.substring(intDelCodeStartLen,
						strCodeWithTag.length() - intDelCodeEndLen);
				SpannableString spanString = MethodsExtra.getClickableSpanner(
						strCode, new OnClickListener() {
							@Override
							public void onClick(View v) {
								MethodsDeliverData.mDelCode = strCode;
								MethodsExtra.startActivity(mContext,
										HouseDetailActivity.class);
							}
						});
				
				textView.append(spanString);
			}
		}
		if (strContent.length() >= 1) {
			textView.append(strContent);
		}
	}

	public void setmContext(Context mContext) {
		MessageListAdapter.mContext = mContext;
	}

	public class ViewHolder {
		public TextView tvTitle;
		public TextView tvTime;
		public TextView tvContent;
		public ImageView ivHeader;
	}
}
