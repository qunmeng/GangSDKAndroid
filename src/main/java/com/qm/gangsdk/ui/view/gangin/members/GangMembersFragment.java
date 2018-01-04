package com.qm.gangsdk.ui.view.gangin.members;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.callback.DataCallBack;
import com.qm.gangsdk.core.outer.common.entity.XLGangMemberInfoBean;
import com.qm.gangsdk.core.outer.common.entity.XLGangMemberSortBean;
import com.qm.gangsdk.core.outer.common.utils.StringUtils;
import com.qm.gangsdk.core.outer.receiver.GangPosterReceiver;
import com.qm.gangsdk.core.outer.receiver.base.OnGangReceiverListener;
import com.qm.gangsdk.core.outer.receiver.listener.GangReceiverListener;
import com.qm.gangsdk.ui.GangSDK;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseFragment;
import com.qm.gangsdk.ui.custom.headerfooter.EndlessRecyclerOnScrollListener;
import com.qm.gangsdk.ui.custom.headerfooter.HeaderAndFooterRecyclerViewAdapter;
import com.qm.gangsdk.ui.custom.loadingfooter.LoadingFooter;
import com.qm.gangsdk.ui.custom.loadingfooter.RecyclerViewStateUtils;
import com.qm.gangsdk.ui.event.XLKickUserSuccessEvent;
import com.qm.gangsdk.ui.utils.XLKeyBoardUtil;
import com.qm.gangsdk.ui.utils.XLToastUtil;
import com.qm.gangsdk.ui.view.common.GangModuleManage;
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
 */
public class GangMembersFragment extends XLBaseFragment {

    public static final int pagesize = 10;
    private static final int TYPE_GET_MEMBER = 1;     //获取成员数据
    private static final int TYPE_GET_SEARCH = 2;      //获取查询成员数据
    private int type = -1;
    private int pageindex = 1;

    private Button btnNoTalk;
    private Button btnActive;
    private Button btnContribute;
    private Button btnAddMember;
    private Button btnSearch;
    private EditText editMemberName;
    private TextView tvNums;
    private ImageView imageSearchBack;
    private ImageView imageSearchDelete;

    private RecyclerView recyclerView;
    private XLMemberAdapter adapter;
    private PtrClassicFrameLayout ptrFrameLayout;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;

    private List dataList = new ArrayList();

    private GangReceiverListener kickUserListener;

    @Override
    protected int getContentView() {
        return R.layout.fragment_gang_members;
    }

    @Override
    protected void initData() {
        type = TYPE_GET_MEMBER;
    }

    @Override
    protected void initView(View view) {
        ptrFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptrFrameLayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        tvNums = (TextView) view.findViewById(R.id.tvNums);
        btnActive = (Button) view.findViewById(R.id.btnActive);
        btnNoTalk = (Button) view.findViewById(R.id.btnNoTalk);
        btnContribute = (Button) view.findViewById(R.id.btnContribute);
        btnAddMember = (Button) view.findViewById(R.id.btnAddMember);
        btnSearch = (Button) view.findViewById(R.id.btnSearch);
        editMemberName = (EditText) view.findViewById(R.id.editMemberName);
        imageSearchBack = (ImageView) view.findViewById(R.id.imageSearchBack);
        imageSearchDelete = (ImageView) view.findViewById(R.id.imageSearchDelete);

        editMemberName.setFocusable(false);
        editMemberName.clearFocus();
        initRecyclerView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        kickUserListener = GangPosterReceiver.addReceiverListener(this, XLKickUserSuccessEvent.class, new OnGangReceiverListener() {
            @Override
            public void onReceived(Object data) {
                getData(1, pagesize);
            }
        });

        editMemberName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                XLKeyBoardUtil.showKeyBoard(aContext, editMemberName);
                dataList.clear();
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        editMemberName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    imageSearchBack.setImageResource(R.mipmap.qm_icon_gangmembers_search_back);
                    imageSearchDelete.setVisibility(View.VISIBLE);
                }else {
                    imageSearchBack.setImageResource(R.mipmap.qm_icon_gangmembers_magnifier);
                    imageSearchDelete.setVisibility(View.GONE);
                }
            }
        });

        imageSearchDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editMemberName.setText("");
            }
        });

        imageSearchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = TYPE_GET_MEMBER;
                getData(1, pagesize);
                editMemberName.setText("");
                editMemberName.setFocusable(false);
                editMemberName.setFocusableInTouchMode(false);
                XLKeyBoardUtil.hideKeyBoard(aContext, editMemberName);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = TYPE_GET_SEARCH;
                getData(1, pagesize);
                XLKeyBoardUtil.hideKeyBoard(aContext, editMemberName);

            }
        });

        btnNoTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GangModuleManage.toMemberNotalkActivity(aContext);
            }
        });
        btnContribute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GangModuleManage.toMemberContributeActivity(aContext);
            }
        });
        btnActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GangModuleManage.toMemberActiveActivity(aContext);
            }
        });
        btnAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogAddMemberFragment().show(aContext.getFragmentManager());
            }
        });

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
        adapter = new XLMemberAdapter(aContext, this.dataList);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        recyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
    }

    private void getData(final int page, final int pagesize) {
        switch (type){
            case TYPE_GET_MEMBER:
                GangSDK.getInstance().groupManager().getGangMembers(page, pagesize, new DataCallBack<XLGangMemberSortBean>() {
                    @Override
                    public void onSuccess(int status, String message, XLGangMemberSortBean data) {
                        ptrFrameLayout.refreshComplete();
                        pageindex = page + 1;
                        if (page == 1) {
                            dataList.clear();
                        }

                        if(data != null){
                            tvNums.setText("在线：" + data.getOnlinenum() + "/" + data.getTotalnum());
                            dataList.addAll(data.getSortedlist());
                        }
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
                break;
            case TYPE_GET_SEARCH:
                String nickname = editMemberName.getText().toString().trim();
                if (StringUtils.isEmpty(nickname)) {
                    XLToastUtil.showToastShort(aContext.getResources().getString(R.string.message_gang_member_search));
                }else {
                    GangSDK.getInstance().groupManager().searchGangMember(nickname, page, pagesize, new DataCallBack<List<XLGangMemberInfoBean>>() {

                        @Override
                        public void onSuccess(int status, String message, List<XLGangMemberInfoBean> data) {
                            ptrFrameLayout.refreshComplete();
                            pageindex = page + 1;
                            if (page == 1) {
                                dataList.clear();
                            }

                            if(data != null && !data.isEmpty()){
                                dataList.addAll(data);
                            }
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
                break;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GangSDK.getInstance().receiverManager().removeReceiverListener(kickUserListener);
    }
}
