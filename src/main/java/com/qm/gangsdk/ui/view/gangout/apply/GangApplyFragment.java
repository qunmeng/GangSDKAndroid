package com.qm.gangsdk.ui.view.gangout.apply;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qm.gangsdk.core.outer.common.callback.DataCallBack;
import com.qm.gangsdk.core.outer.receiver.GangPosterReceiver;
import com.qm.gangsdk.core.outer.receiver.listener.GangReceiverListener;
import com.qm.gangsdk.core.outer.receiver.base.OnGangReceiverListener;
import com.qm.gangsdk.core.outer.common.entity.XLGangApplyListBean;
import com.qm.gangsdk.ui.GangSDK;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseFragment;
import com.qm.gangsdk.ui.event.XLApplyCancelEvent;
import com.qm.gangsdk.ui.utils.XLToastUtil;
import com.xl.views.ptr.PtrClassicFrameLayout;
import com.xl.views.ptr.PtrDefaultHandler;
import com.xl.views.ptr.PtrFrameLayout;
import com.xl.views.ptr.PtrHandler;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lijiyuan on 2017/8/4.
 * 社群申请页面
 */

public class GangApplyFragment extends XLBaseFragment {

    private RecyclerView recyclerViewApply;
    private ApplyAdapter adapter;
    private List<XLGangApplyListBean> applyList = new ArrayList<>();
    private PtrClassicFrameLayout ptrFrameLayout;

    private GangReceiverListener applyCancelListener;
    @Override
    protected int getContentView() {
        return R.layout.fragment_apply;
    }

    @Override
    protected void initData() {
        GangSDK.getInstance().userManager().getGangApplyList(new DataCallBack<List<XLGangApplyListBean>>() {

            @Override
            public void onSuccess(int status, String message, List<XLGangApplyListBean> data) {
                ptrFrameLayout.refreshComplete();
                if(data != null && !data.isEmpty()) {
                    applyList.clear();
                    applyList.addAll(data);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFail(String message) {
                XLToastUtil.showToastShort(message);
            }
        });
    }

    @Override
    protected void initView(View view) {
        ptrFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptrFrameLayout);
        recyclerViewApply = (RecyclerView) view.findViewById(R.id.recyclerViewApply);
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

        applyCancelListener = GangPosterReceiver.addReceiverListener(this, XLApplyCancelEvent.class, new OnGangReceiverListener() {
            @Override
            public void onReceived(Object data) {
                initData();
            }
        });
    }

    private void bindRecyclerView() {
        adapter = new ApplyAdapter(aContext, applyList);
        recyclerViewApply.setHasFixedSize(false);
        recyclerViewApply.setLayoutManager( new LinearLayoutManager(aContext));
        recyclerViewApply.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GangSDK.getInstance().receiverManager().removeReceiverListener(applyCancelListener);
    }
}
