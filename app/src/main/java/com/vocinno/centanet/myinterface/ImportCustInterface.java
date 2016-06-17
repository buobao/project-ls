package com.vocinno.centanet.myinterface;

import com.daimajia.swipe.SwipeLayout;

/**
 * Created by Administrator on 2016/6/15.
 */
public interface ImportCustInterface {
    void importCustInvalid(int position,String id,SwipeLayout swipeLayout);
    void importCustAccept(int position,String id,SwipeLayout swipeLayout);
}
