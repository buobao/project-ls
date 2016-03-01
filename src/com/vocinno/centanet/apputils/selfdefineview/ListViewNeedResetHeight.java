package com.vocinno.centanet.apputils.selfdefineview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ListViewNeedResetHeight extends ListView {
	
	public ListViewNeedResetHeight(Context context) {
		super(context);
	}
	
	public ListViewNeedResetHeight(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ListViewNeedResetHeight(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override  
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
	    int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  
	            MeasureSpec.AT_MOST);  
	    super.onMeasure(widthMeasureSpec, expandSpec);  
	} 

}
