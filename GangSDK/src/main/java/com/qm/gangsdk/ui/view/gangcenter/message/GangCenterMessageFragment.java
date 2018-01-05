package com.qm.gangsdk.ui.view.gangcenter.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.callback.DataCallBack;
import com.qm.gangsdk.core.outer.common.entity.XLGangCenterMessageBean;
import com.qm.gangsdk.ui.GangSDK;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseFragment;
import com.qm.gangsdk.ui.utils.XLToastUtil;
import com.xl.views.ptr.PtrClassicFrameLayout;
import com.xl.views.ptr.PtrDefaultHandler;
import com.xl.views.ptr.PtrFrameLayout;
import com.xl.views.ptr.PtrHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息中心页面
 */

public class GangCenterMessageFragment extends XLBaseFragment {

    private ImageButton btnMenuLeft;
    private TextView tvTitle;

    private RecyclerView recyclerView;
    private GangCenterMessageAdapter adapter;
    private List<XLGangCenterMessageBean> messageList = new ArrayList<>();
    private PtrClassicFrameLayout ptrFrameLayout;

    @Override
    protected int getContentView() {
        return R.layout.fragment_gangcenter_message;
    }

    @Override
    protected void initData() {
        GangSDK.getInstance().userManager().getMessageNotificationList(new DataCallBack<List<XLGangCenterMessageBean>>() {

            @Override
            public void onSuccess(int status, String message, List<XLGangCenterMessageBean> data) {
                ptrFrameLayout.refreshComplete();
                messageList.clear();
                messageList.addAll(data);
                adapter.notifyDataSetChanged();
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
        btnMenuLeft = (ImageButton) view.findViewById(R.id.btnMenuLeft);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvTitle.setText(aContext.getResources().getString(R.string.gang_message_center));

        ptrFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptrFrameLayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        bindRecyclerView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnMenuLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aContext.finish();
            }
        });

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
    }

    private void bindRecyclerView() {
        adapter = new GangCenterMessageAdapter(aContext, messageList);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager( new LinearLayoutManager(aContext));
        recyclerView.setAdapter(adapter);
    }
}
