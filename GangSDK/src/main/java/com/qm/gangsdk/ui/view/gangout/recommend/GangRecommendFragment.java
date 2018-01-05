package com.qm.gangsdk.ui.view.gangout.recommend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qm.gangsdk.core.outer.common.callback.DataCallBack;
import com.qm.gangsdk.ui.utils.XLToastUtil;
import com.qm.gangsdk.core.outer.common.entity.XLGangRecommendBean;
import com.qm.gangsdk.ui.GangSDK;;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseFragment;
import com.xl.views.ptr.PtrClassicFrameLayout;
import com.xl.views.ptr.PtrDefaultHandler;
import com.xl.views.ptr.PtrFrameLayout;
import com.xl.views.ptr.PtrHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijiyuan on 2017/8/4.
 * 社群推荐
 */

public class GangRecommendFragment extends XLBaseFragment {

    private RecyclerView recyclerViewRecommend;
    private RecommendAdapter adapter;
    private List<XLGangRecommendBean> recommendList = new ArrayList<>();
    private PtrClassicFrameLayout ptrFrameLayout;

    @Override
    protected int getContentView() {
        return R.layout.fragment_recommend;
    }

    @Override
    protected void initData() {
        GangSDK.getInstance().groupManager().getGangRecommendList(new DataCallBack<List<XLGangRecommendBean>>() {

            @Override
            public void onSuccess(int status, String message, List<XLGangRecommendBean> data) {
                ptrFrameLayout.refreshComplete();
                if(data != null && !data.isEmpty()) {
                    recommendList.clear();
                    recommendList.addAll(data);
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
        recyclerViewRecommend = (RecyclerView) view.findViewById(R.id.recyclerViewRecommend);
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
    }

    private void bindRecyclerView() {
        adapter = new RecommendAdapter(aContext, recommendList);
        recyclerViewRecommend.setHasFixedSize(false);
        recyclerViewRecommend.setLayoutManager( new LinearLayoutManager(aContext));
        recyclerViewRecommend.setAdapter(adapter);
    }
}
