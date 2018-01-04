package com.qm.gangsdk.ui.view.gangin.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.callback.DataCallBack;
import com.qm.gangsdk.core.outer.receiver.GangPosterReceiver;
import com.qm.gangsdk.core.outer.receiver.listener.GangReceiverListener;
import com.qm.gangsdk.core.outer.receiver.base.OnGangReceiverListener;
import com.qm.gangsdk.core.outer.common.utils.StringUtils;
import com.qm.gangsdk.core.outer.common.entity.XLGangInfoBean;
import com.qm.gangsdk.core.outer.common.entity.XLGangLogBean;
import com.qm.gangsdk.ui.GangSDK;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseFragment;
import com.qm.gangsdk.ui.custom.headerfooter.EndlessRecyclerOnScrollListener;
import com.qm.gangsdk.ui.custom.headerfooter.HeaderAndFooterRecyclerViewAdapter;
import com.qm.gangsdk.ui.custom.loadingfooter.LoadingFooter;
import com.qm.gangsdk.ui.custom.loadingfooter.RecyclerViewStateUtils;
import com.qm.gangsdk.ui.event.XLDonateSuccessEvent;
import com.qm.gangsdk.ui.view.common.GangConfigureUtils;
import com.qm.gangsdk.ui.utils.TimeUtils;
import com.qm.gangsdk.ui.utils.XLToastUtil;
import com.xl.views.ptr.PtrClassicFrameLayout;
import com.xl.views.ptr.PtrDefaultHandler;
import com.xl.views.ptr.PtrFrameLayout;
import com.xl.views.ptr.PtrHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：shuzhou on 2017/8/8.
 * 邮箱：810421209@qq.com
 * 版本：v1.0
 * 社群信息页面
 */
public class GangInfoFragment extends XLBaseFragment {

    private String endtime = null;
    private int pagesize = 10;
    private TextView textGangAnnouncements;
    private TextView textGangInfo;

    private Button btnDonate;
    private TextView textGangDeclaration;
    private RecyclerView recyclerViewGangLog;
    private PtrClassicFrameLayout ptrFrameLayout;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;
    private GangLogAdapter adapter;
    private List<XLGangLogBean> loglist = new ArrayList<>();

    private GangReceiverListener donateSuccessListener;

    @Override
    protected int getContentView() {
        return R.layout.fragment_gang_info;
    }

    @Override
    protected void initData() {
        if(GangSDK.getInstance().userManager().getXlUserBean().getConsortiaid() == null) {
            return;
        }
        Integer gangid = GangSDK.getInstance().userManager().getXlUserBean().getConsortiaid();
        GangSDK.getInstance().groupManager().getGangInfo(gangid, new DataCallBack<XLGangInfoBean>() {

            @Override
            public void onSuccess(int status, String message, XLGangInfoBean data) {
                updateViewData(data);
            }

            @Override
            public void onFail(String message) {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogGangDonateFragment().show(aContext.getFragmentManager());
            }
        });

        donateSuccessListener = GangPosterReceiver.addReceiverListener(this, XLDonateSuccessEvent.class, new OnGangReceiverListener<Object>() {
            @Override
            public void onReceived(Object data) {
                initData();
            }
        });

        recyclerViewGangLog.addOnScrollListener(new EndlessRecyclerOnScrollListener() {

            @Override
            public void onLoadNextPage(View view) {
                super.onLoadNextPage(view);
                LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(recyclerViewGangLog);
                if (state == LoadingFooter.State.Loading) {
                    return;
                }
                // loading more
                RecyclerViewStateUtils.setFooterViewState(aContext, recyclerViewGangLog, pagesize, LoadingFooter.State.Loading, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecyclerViewStateUtils.setFooterViewState(aContext, recyclerViewGangLog, LoadingFooter.State.Loading, null);
                    }
                });
                getGangLogData(pagesize, endtime);

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
                endtime = "";
                getGangLogData(pagesize, endtime);
            }
        });

        ptrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrFrameLayout.autoRefresh(true);
            }
        }, 150);
    }


    public void getGangLogData(int pagesize, final String time){
        GangSDK.getInstance().groupManager().getGangLogList(pagesize, time, new DataCallBack<List<XLGangLogBean>>() {

            @Override
            public void onSuccess(int status, String message, List<XLGangLogBean> data) {
                ptrFrameLayout.refreshComplete();
                if (StringUtils.isEmpty(time)) {
                    loglist.clear();
                }

                if(data != null && !data.isEmpty()) {
                    endtime = String.valueOf(data.get(data.size() - 1).getCreatetime());
                }else {
                    RecyclerViewStateUtils.setFooterViewState(recyclerViewGangLog, LoadingFooter.State.Normal);
                    return;
                }
                loglist.addAll(data);
                adapter.notifyDataSetChanged();
                RecyclerViewStateUtils.setFooterViewState(recyclerViewGangLog, LoadingFooter.State.Normal);
            }

            @Override
            public void onFail(String message) {
                ptrFrameLayout.refreshComplete();
                XLToastUtil.showToastShort(message);
                RecyclerViewStateUtils.setFooterViewState(recyclerViewGangLog, LoadingFooter.State.TheEnd);
            }
        });
    }

    /**
     * 绑定界面数据
     * @param data
     */
    private void updateViewData(XLGangInfoBean data) {
        if(isAdded()) {
            if (data != null) {
                loglist.clear();
                String infos = GangConfigureUtils.getGangName() + ": " + data.getConsortianame() + "\n" +
                        "ID: " + data.getConsortiaid() + "\n" +
                        aContext.getResources().getString(R.string.gang_leader_text) + ": " + data.getChairman() + "\n" +
                        aContext.getResources().getString(R.string.gang_level_text) + ": " + data.getBuildlevel() + "级\n" +
                        aContext.getResources().getString(R.string.gang_build_text) + ": " + data.getBuildnum() + "\n" +
                        aContext.getResources().getString(R.string.gang_active_text) + ": " + "LV" + data.getActivelevel() + "\n" +
                        aContext.getResources().getString(R.string.gang_member_text) + ": " + data.getNownum() + "/" + data.getMaxnum() + "\n" +
                        aContext.getResources().getString(R.string.gang_caifu_text) + ": " + data.getMoneynum();
                textGangInfo.setText(infos);
                textGangDeclaration.setText(data.getDeclaration());
                if(!data.getAnnouncements().isEmpty()) {
                    textGangAnnouncements.setText(StringUtils.getString(data.getAnnouncements().get((data.getAnnouncements().size() - 1)).getContent(), ""));
                }
            }
        }else {
            return;
        }
    }

    @Override
    protected void initView(View view) {
        btnDonate  = (Button) view.findViewById(R.id.btnDonate);
        textGangAnnouncements = (TextView) view.findViewById(R.id.textGangAnnouncements);
        textGangInfo = (TextView) view.findViewById(R.id.textGangInfo);
        textGangDeclaration = (TextView) view.findViewById(R.id.textGangDeclaration);
        recyclerViewGangLog = (RecyclerView) view.findViewById(R.id.recyclerViewGangLog);
        ptrFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptrFrameLayout);
        bindRecyclerView();
    }

    /**
     * 绑定Adapter
     */
    private void bindRecyclerView() {
        recyclerViewGangLog.setHasFixedSize(false);
        recyclerViewGangLog.setFocusable(false);
        recyclerViewGangLog.setLayoutManager(new LinearLayoutManager(GangInfoFragment.this.getContext()));
        recyclerViewGangLog.setNestedScrollingEnabled(true);
        adapter = new GangLogAdapter();
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        recyclerViewGangLog.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
    }


    /**
     * 社群日志adapter
     */
    class GangLogAdapter extends RecyclerView.Adapter<GangLogViewHolder>{

        @Override
        public GangLogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new GangLogViewHolder(LayoutInflater.from(GangInfoFragment.this.getContext()).inflate(
                    R.layout.item_recyclerview_ganglog, parent, false));
        }

        @Override
        public void onBindViewHolder(GangLogViewHolder holder, int position) {
            XLGangLogBean logBean = loglist.get(position);
            holder.textGanglogContent.setText(TimeUtils.getTime(logBean.getCreatetime()) + "  " + logBean.getContent());
        }

        @Override
        public int getItemCount() {
            return loglist.size();
        }
    }

    /**
     * 社群日志viewHolder
     */
    class GangLogViewHolder extends RecyclerView.ViewHolder{
        private TextView textGanglogContent;

        public GangLogViewHolder(View view) {
            super(view);
            textGanglogContent = (TextView) view.findViewById(R.id.textGanglogContent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GangPosterReceiver.removeReceiverListener(donateSuccessListener);
    }
}
