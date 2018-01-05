package com.qm.gangsdk.ui.view.gangout.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.qm.gangsdk.core.outer.common.callback.DataCallBack;
import com.qm.gangsdk.core.outer.common.utils.StringUtils;
import com.qm.gangsdk.ui.utils.XLKeyBoardUtil;
import com.qm.gangsdk.ui.utils.XLToastUtil;
import com.qm.gangsdk.core.outer.common.utils.logger.Logger;
import com.qm.gangsdk.core.outer.common.entity.XLGangListBean;
import com.qm.gangsdk.ui.GangSDK;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseFragment;
import com.qm.gangsdk.ui.custom.headerfooter.EndlessRecyclerOnScrollListener;
import com.qm.gangsdk.ui.custom.headerfooter.HeaderAndFooterRecyclerViewAdapter;
import com.qm.gangsdk.ui.custom.loadingfooter.LoadingFooter;
import com.qm.gangsdk.ui.custom.loadingfooter.RecyclerViewStateUtils;
import com.qm.gangsdk.ui.view.common.GangConfigureUtils;
import com.xl.views.ptr.PtrClassicFrameLayout;
import com.xl.views.ptr.PtrDefaultHandler;
import com.xl.views.ptr.PtrFrameLayout;
import com.xl.views.ptr.PtrHandler;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lijiyuan on 2017/8/4.
 * 社群排行
 */

public class GangListFragment extends XLBaseFragment{

    private static final int TYPE_SORT_LEVEL = 1;   //等级榜
    private static final int TYPE_GET_SORT = 2;     //获取排行榜数据
    private static final int TYPE_GET_SEARCH = 3;       //获取查询数据
    private int type = -1;
    private int pageindex = 1;
    private int pagesize = 10;
    private EditText editGangName;
    private Button btnGangSearch;
    private ImageView imageSearchBack;
    private ImageView imageSearchDelete;
    private RecyclerView recyclerViewSort;
    private ListAdapter adapter;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;
    private List<XLGangListBean> mSortList = new ArrayList<>();
    private PtrClassicFrameLayout ptrFrameLayout;

    @Override
    protected int getContentView() {
        return R.layout.fragment_ganglist;
    }

    @Override
    protected void initData() {
        type = TYPE_GET_SORT;
    }

    @Override
    protected void initView(View view) {
        editGangName = (EditText) view.findViewById(R.id.editGangName);
        imageSearchBack = (ImageView) view.findViewById(R.id.imageSearchBack);
        imageSearchDelete = (ImageView) view.findViewById(R.id.imageSearchDelete);
        btnGangSearch = (Button) view.findViewById(R.id.btnGangSearch);
        recyclerViewSort = (RecyclerView) view.findViewById(R.id.recyclerViewSort);
        ptrFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptrFrameLayout);

        editGangName.setFocusable(false);
        editGangName.clearFocus();
        editGangName.setHint("请输入要查询的" + GangConfigureUtils.getGangName() + "名称或者ID");

        bindRecyclerView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnGangSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                XLKeyBoardUtil.hideKeyBoard(GangListFragment.this.getContext(), editGangName);
                type = TYPE_GET_SEARCH;
                getData(1, pagesize);
            }
        });

        editGangName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                XLKeyBoardUtil.showKeyBoard(aContext, editGangName);
                mSortList.clear();
                adapter.notifyDataSetChanged();
                return true;
            }
        });


        editGangName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    imageSearchBack.setImageResource(R.mipmap.qm_icon_ganglist_search_back);
                    imageSearchDelete.setVisibility(View.VISIBLE);
                }else {
                    imageSearchBack.setImageResource(R.mipmap.qm_icon_ganglist_magnifier);
                    imageSearchDelete.setVisibility(View.GONE);
                }
            }
        });

        imageSearchDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editGangName.setText("");
            }
        });


        imageSearchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = TYPE_GET_SORT;
                getData(1, pagesize);
                editGangName.setText("");
                editGangName.setFocusable(false);
                editGangName.setFocusableInTouchMode(false);
                XLKeyBoardUtil.hideKeyBoard(aContext, editGangName);
            }
        });
        recyclerViewSort.addOnScrollListener(new EndlessRecyclerOnScrollListener() {

            @Override
            public void onLoadNextPage(View view) {
                super.onLoadNextPage(view);
                LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(recyclerViewSort);
                if (state == LoadingFooter.State.Loading) {
                    return;
                }
                // loading more
                RecyclerViewStateUtils.setFooterViewState(aContext, recyclerViewSort, pagesize, LoadingFooter.State.Loading, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecyclerViewStateUtils.setFooterViewState(aContext, recyclerViewSort, LoadingFooter.State.Loading, null);
                    }
                });
                Logger.d("pageindex = " + pageindex);
                getData(pageindex, pagesize);

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
                pageindex = 1;
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


    /**
     * 获取排行榜数据
     * @param page
     * @param size
     */
    private void getData(final int page, int size) {
        switch (type){
            case TYPE_GET_SORT:
                GangSDK.getInstance().groupManager().getGangList(page,
                        size, TYPE_SORT_LEVEL, new DataCallBack<List<XLGangListBean>>() {
                            @Override
                            public void onSuccess(int status, String message, List<XLGangListBean> data) {
                                ptrFrameLayout.refreshComplete();
                                pageindex = page + 1;
                                if (page == 1) {
                                    mSortList.clear();
                                }

                                if(data == null || data.isEmpty()){
                                    RecyclerViewStateUtils.setFooterViewState(recyclerViewSort, LoadingFooter.State.Normal);
                                    return;
                                }
                                mSortList.addAll(data);
                                adapter.notifyDataSetChanged();
                                RecyclerViewStateUtils.setFooterViewState(recyclerViewSort, LoadingFooter.State.Normal);
                            }

                            @Override
                            public void onFail(String message) {
                                ptrFrameLayout.refreshComplete();
                                XLToastUtil.showToastShort(message);
                                RecyclerViewStateUtils.setFooterViewState(recyclerViewSort, LoadingFooter.State.TheEnd);
                            }
                        });
                break;
            case TYPE_GET_SEARCH:
                String gangname = editGangName.getText().toString().trim();
                if(StringUtils.isEmpty(gangname)){
                    XLToastUtil.showToastLong(GangConfigureUtils.getGangName() + "的名称或者ID不能为空");
                }else {
                    GangSDK.getInstance().groupManager().searchGang(gangname, page, size, new DataCallBack<List<XLGangListBean>>() {

                        @Override
                        public void onSuccess(int status, String message, List<XLGangListBean> data) {
                            ptrFrameLayout.refreshComplete();
                            pageindex = page + 1;
                            if (page == 1) {
                                mSortList.clear();
                            }
                            if(data == null && data.isEmpty()){
                                RecyclerViewStateUtils.setFooterViewState(recyclerViewSort, LoadingFooter.State.Normal);
                                return;
                            }
                            mSortList.addAll(data);
                            adapter.notifyDataSetChanged();
                            RecyclerViewStateUtils.setFooterViewState(recyclerViewSort, LoadingFooter.State.Normal);
                        }

                        @Override
                        public void onFail(String message) {
                            ptrFrameLayout.refreshComplete();
                            XLToastUtil.showToastShort(message);
                            RecyclerViewStateUtils.setFooterViewState(recyclerViewSort, LoadingFooter.State.TheEnd);
                        }
                    });
                }
                break;
        }

    }

    private void bindRecyclerView() {
        adapter = new ListAdapter(aContext, mSortList);
        recyclerViewSort.setHasFixedSize(false);
        recyclerViewSort.setLayoutManager( new LinearLayoutManager(aContext));
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        recyclerViewSort.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
    }
}
