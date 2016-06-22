package com.vocinno.centanet.customermanage.adapter;

import com.vocinno.centanet.R;
import com.vocinno.centanet.customermanage.AddAccompanyActivity;
import com.vocinno.centanet.housemanage.adapter.CustomGridView;
import com.vocinno.centanet.housemanage.adapter.ImgAdapter;
import com.vocinno.centanet.model.FirstHouse;
import com.vocinno.centanet.tools.CommonAdapter;
import com.vocinno.centanet.tools.ViewHolder;

import java.util.List;

/**
 * 	我的客源  条目适配器
 */
public class FirstHouseAdapter extends CommonAdapter<FirstHouse> {

	private ImgAdapter imgAdapter;
	private AddAccompanyActivity activity;

	public FirstHouseAdapter(AddAccompanyActivity context, List<FirstHouse> mDatas, int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		this.activity = context;
	}

	@Override
	public void convert(ViewHolder helper, FirstHouse item) {

		String commitment="否";
		if("1".equals(item.getCommitment())){
			commitment="是";
		}

		imgAdapter=new ImgAdapter(activity);
		imgAdapter.setData(item.getImgPath());
		int num=0;
		if(item.getImgPath()!=null&&item.getImgPath().size()>0){
			num=item.getImgPath().size();
		}

		helper.setText(R.id.tv_accompany_people, item.getPeiKan())
				.setText(R.id.tv_accompany_promise, commitment)
				.setText(R.id.tv_accompany_address, item.getAddress())
				.setText(R.id.tv_first_house_num, "(共"+num+"张)");
		CustomGridView gv_first_house_item = helper.getView(R.id.gv_first_house_item);
		gv_first_house_item.setAdapter(imgAdapter);


	}
}
