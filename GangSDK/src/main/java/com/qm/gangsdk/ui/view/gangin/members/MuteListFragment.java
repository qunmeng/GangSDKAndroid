package com.qm.gangsdk.ui.view.gangin.members;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.callback.DataCallBack;
import com.qm.gangsdk.core.outer.common.utils.StringUtils;
import com.qm.gangsdk.ui.utils.XLToastUtil;
import com.qm.gangsdk.core.outer.common.entity.XLGangMemberInfoBean;
import com.qm.gangsdk.ui.GangSDK;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseFragment;
import com.qm.gangsdk.ui.custom.headerfooter.EndlessRecyclerOnScrollListener;
import com.qm.gangsdk.ui.custom.headerfooter.HeaderAndFooterRecyclerViewAdapter;
import com.qm.gangsdk.ui.custom.loadingfooter.LoadingFooter;
import com.qm.gangsdk.ui.custom.loadingfooter.RecyclerViewStateUtils;
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
public class MuteListFragment extends XLBaseFragment {
    public static final int pagesize = 10;
    private String endTime = null;
    private ImageButton btnMenuLeft;
    private RecyclerView recyclerView;
    XLMemberMuteAdapter adapter;
    TextView tvTitle;
    private PtrClassicFrameLayout ptrFrameLayout;

    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;

    private List<XLGangMemberInfoBean> dataList = new ArrayList();

    @Override
    protected int getContentView() {
        return R.layout.fragment_members_notalk;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        btnMenuLeft = (ImageButton) view.findViewById(R.id.btnMenuLeft);
        ptrFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptrFrameLayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewContent);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvTitle.setText(aContext.getResources().getString(R.string.member_notalk));
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
        initRecyclerView();

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
                    }
                });
                getData(endTime, pagesize);

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
                endTime = "";
                getData(endTime, pagesize);
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
        adapter = new XLMemberMuteAdapter(aContext, dataList);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        recyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
    }

    private void getData(final String time, int pagesize) {
        GangSDK.getInstance().groupManager().getGangMuteMembersList(time, pagesize, new DataCallBack<List<XLGangMemberInfoBean>>() {

            @Override
            public void onSuccess(int status, String message, List<XLGangMemberInfoBean> data) {
                ptrFrameLayout.refreshComplete();
                if (StringUtils.isEmpty(time)) {
                    dataList.clear();
                }

                if(data != null && !data.isEmpty()) {
                    endTime = String.valueOf(data.get(data.size() - 1).getCreatetime());
                }else {
                    RecyclerViewStateUtils.setFooterViewState(recyclerView, LoadingFooter.State.Normal);
                    return;
                }
                dataList.addAll(data);
                adapter.notifyDataSetChanged();
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
