package com.qm.gangsdk.ui.view.gangrank;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qm.gangsdk.core.outer.common.callback.DataCallBack;
import com.qm.gangsdk.core.outer.common.entity.XLGangListBean;
import com.qm.gangsdk.ui.GangSDK;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseFragment;
import com.qm.gangsdk.ui.custom.headerfooter.EndlessRecyclerOnScrollListener;
import com.qm.gangsdk.ui.custom.headerfooter.HeaderAndFooterRecyclerViewAdapter;
import com.qm.gangsdk.ui.custom.loadingfooter.LoadingFooter;
import com.qm.gangsdk.ui.custom.loadingfooter.RecyclerViewStateUtils;
import com.qm.gangsdk.ui.utils.XLToastUtil;
import com.xl.views.ptr.PtrClassicFrameLayout;
import com.xl.views.ptr.PtrDefaultHandler;
import com.xl.views.ptr.PtrFrameLayout;
import com.xl.views.ptr.PtrHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：shuzhou on 2017/8/17.
 * 邮箱：810421209@qq.com
 * 版本：v1.0
 */
public class RankFragment extends XLBaseFragment {

    public static final int TYPE_LEVEL = 1;   //等级榜
    public static final int TYPE_RENQI = 2;   //人气榜
    public static final int TYPE_CAIFU = 3;   //财富榜

    public static final int pagesize = 10;
    private int pageindex = 1;
    private int type = -1;
    private RecyclerView recyclerView;
    private PtrClassicFrameLayout ptrFrameLayout;

    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;

    private List dataList = new ArrayList();

    @Override
    protected int getContentView() {
        return R.layout.fragment_members_sort;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        ptrFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptrFrameLayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewContent);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            type = bundle.getInt("type", -1);
        }

        initRecyclerView();
        ptrFrameLayout.setLastUpdateTimeRelateObject(this);
        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View view, View view1) {
                return PtrDefaultHandler.checkContentCanBePulledDown(ptrFrameLayout, view, view1);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                getData(1, pagesize);
            }
        });
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {

            @Override
            public void onLoadNextPage(View view) {
                super.onLoadNextPage(view);
                LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(recyclerView);
                if (state == LoadingFooter.State.Loading) {
                    return;
                }
                // loading more
                RecyclerViewStateUtils.setFooterViewState(aContext, recyclerView, pagesize, LoadingFooter.State.Loading, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecyclerViewStateUtils.setFooterViewState(aContext, recyclerView, LoadingFooter.State.Loading, null);
                        getData(pageindex, pagesize);
                    }
                });
                getData(pageindex, pagesize);

            }
        });
        ptrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrFrameLayout.autoRefresh(true);
            }
        }, 150);
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(aContext));
        RankAdapter adapter = new RankAdapter(aContext, this.dataList, type);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        recyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
    }

    private void getData(final int page, int pagesize) {
        GangSDK.getInstance().groupManager().getGangList(page, pagesize, type, new DataCallBack<List<XLGangListBean>>() {

            @Override
            public void onSuccess(int status, String message, List<XLGangListBean> data) {
                ptrFrameLayout.refreshComplete();
                pageindex = page + 1;
                if (page == 1) {
                    dataList.clear();
                }
                if(data == null || data.isEmpty()){
                    RecyclerViewStateUtils.setFooterViewState(recyclerView, LoadingFooter.State.Normal);
                    return;
                }
                dataList.addAll(data);
                mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
                RecyclerViewStateUtils.setFooterViewState(recyclerView, LoadingFooter.State.Normal);
            }

            @Override
            public void onFail(String message) {
                ptrFrameLayout.refreshComplete();
                XLToastUtil.showToastShort(message);
                RecyclerViewStateUtils.setFooterViewState(recyclerView, LoadingFooter.State.TheEnd);
            }
        });
    }
}
