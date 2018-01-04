package com.qm.gangsdk.ui.view.gangout.invite;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qm.gangsdk.core.outer.common.callback.DataCallBack;
import com.qm.gangsdk.core.outer.receiver.GangPosterReceiver;
import com.qm.gangsdk.core.outer.receiver.listener.GangReceiverListener;
import com.qm.gangsdk.core.outer.receiver.base.OnGangReceiverListener;
import com.qm.gangsdk.core.outer.common.entity.XLGangInviteListBean;
import com.qm.gangsdk.ui.GangSDK;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseFragment;
import com.qm.gangsdk.ui.event.XLInviteRefuseEvent;
import com.qm.gangsdk.ui.utils.XLToastUtil;
import com.xl.views.ptr.PtrClassicFrameLayout;
import com.xl.views.ptr.PtrDefaultHandler;
import com.xl.views.ptr.PtrFrameLayout;
import com.xl.views.ptr.PtrHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijiyuan on 2017/8/4.
 * 社群邀请
 */

public class GangInviteFragment extends XLBaseFragment {

    private RecyclerView recyclerViewInvite;
    private InviteAdapter adapter;
    private List<XLGangInviteListBean> inviteList = new ArrayList<>();
    private PtrClassicFrameLayout ptrFrameLayout;

    private GangReceiverListener memberInviteListener;
    private GangReceiverListener inviteRefuseListener;

    @Override
    protected int getContentView() {
        return R.layout.fragment_invite;
    }

    @Override
    protected void initData() {
        GangSDK.getInstance().userManager().getGangInviteList(new DataCallBack<List<XLGangInviteListBean>>() {

            @Override
            public void onSuccess(int status, String message, List<XLGangInviteListBean> data) {
                ptrFrameLayout.refreshComplete();
                if(data != null && !data.isEmpty()) {
                    inviteList.clear();
                    inviteList.addAll(data);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFail(String message) {
                ptrFrameLayout.refreshComplete();
                XLToastUtil.showToastShort(message);
            }
        });
    }

    @Override
    protected void initView(View view) {
        ptrFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptrFrameLayout);
        recyclerViewInvite = (RecyclerView) view.findViewById(R.id.recyclerViewInvite);
        bindRecyclerView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ptrFrameLayout.setLastUpdateTimeRelateObject(this);
        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                initData();
            }
        });

        ptrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrFrameLayout.autoRefresh(true);
            }
        }, 150);

        inviteRefuseListener = GangPosterReceiver.addReceiverListener(this, XLInviteRefuseEvent.class, new OnGangReceiverListener() {
            @Override
            public void onReceived(Object data) {
                initData();
            }
        });

        memberInviteListener = GangSDK.getInstance().receiverManager().addMemberInviteListener(this, new OnGangReceiverListener<XLGangInviteListBean>() {
            @Override
            public void onReceived(XLGangInviteListBean data) {
                initData();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GangSDK.getInstance().receiverManager().removeReceiverListener(memberInviteListener);
        GangSDK.getInstance().receiverManager().removeReceiverListener(inviteRefuseListener);
    }

    private void bindRecyclerView() {
        adapter = new InviteAdapter(aContext, inviteList);
        recyclerViewInvite.setHasFixedSize(false);
        recyclerViewInvite.setLayoutManager( new LinearLayoutManager(aContext));
        recyclerViewInvite.setAdapter(adapter);
    }
}
