package com.qm.gangsdk.ui.view.gangin.manage;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.callback.DataCallBack;
import com.qm.gangsdk.core.outer.common.entity.XLMemberApplyListBean;
import com.qm.gangsdk.ui.GangSDK;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseFragment;
import com.qm.gangsdk.ui.utils.ImageLoadUtil;
import com.qm.gangsdk.ui.utils.XLToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijiyuan on 2017/8/21.
 * 社群成员申请列表
 */

public class ManageApplyFragment extends XLBaseFragment {

    private static final int STATUS_PASS = 1;       //通过
    private static final int STATUS_AGAINST = 2;    //拒绝

    private TextView tvTitle;
    private ImageButton btnMenuLeft;
    private ImageButton btnMenuRight;
    private RecyclerView recyclerViewApplyList;
    private ApplyListAdapter adapter;
    private List<XLMemberApplyListBean> applylist = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.fragment_applylist;
    }

    @Override
    protected void initData() {
        applylist.clear();
        GangSDK.getInstance().groupManager().getMemberApplicationList(new DataCallBack<List<XLMemberApplyListBean>>() {
            @Override
            public void onSuccess(int status, String message, List<XLMemberApplyListBean> data) {
                if(data != null && !data.isEmpty()) {
                    applylist.addAll(data);
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
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        btnMenuLeft = (ImageButton) view.findViewById(R.id.btnMenuLeft);
        btnMenuRight = (ImageButton) view.findViewById(R.id.btnMenuRight);
        recyclerViewApplyList = (RecyclerView) view.findViewById(R.id.recyclerViewApplyList);

        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(aContext.getResources().getString(R.string.manage_invite_list_title));
        btnMenuRight.setVisibility(View.GONE);

        initRecyclerView();

        btnMenuLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aContext.finish();
            }
        });
    }

    private void initRecyclerView() {
        recyclerViewApplyList.setHasFixedSize(false);
        recyclerViewApplyList.setLayoutManager(new LinearLayoutManager(aContext));
        adapter = new ApplyListAdapter();
        recyclerViewApplyList.setAdapter(adapter);
    }

    /**
     * 成员申请adapter
     */
    class ApplyListAdapter extends RecyclerView.Adapter<ApplyListViewHolder> {

        @Override
        public ApplyListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ApplyListViewHolder holder = new ApplyListViewHolder(LayoutInflater.from(aContext).inflate(
                    R.layout.item_recyclerview_applylist, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(ApplyListViewHolder holder, int position) {
            final XLMemberApplyListBean applyListBean = applylist.get(position);
            if(applyListBean.getGamelevel() == null || 0 == applyListBean.getGamelevel()){
                holder.textUserLevel.setVisibility(View.GONE);
            }else {
                holder.textUserLevel.setText("Lv." + applyListBean.getGamelevel());
            }
            ImageLoadUtil.loadRoundImage(holder.imageUserPicture, applyListBean.getIconurl());
            holder.textUserName.setText(applyListBean.getNickname());
            holder.textUserProfession.setText(applyListBean.getGamerole());

            holder.btnAgainst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    confirmMemberApply(applyListBean.getApplicationid(), STATUS_AGAINST);
                }
            });

            holder.btnPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    confirmMemberApply(applyListBean.getApplicationid(), STATUS_PASS);
                }
            });
        }

        @Override
        public int getItemCount() {
            return applylist.size();
        }
    }

    /**
     * 申请加入审批
     * @param applyid
     * @param status
     */
    private void confirmMemberApply(int applyid, int status) {
        loading.show();
        GangSDK.getInstance().groupManager().acceptAppilcationOfUser(applyid, status, new DataCallBack() {
                    @Override
                    public void onSuccess(int status, String message, Object data) {
                        loading.dismiss();
                        initData();
                    }

                    @Override
                    public void onFail(String message) {
                        loading.dismiss();
                        XLToastUtil.showToastShort(message);
                    }
                });
    }

    /**
     * 成员申请ViewHolder
     */
    class ApplyListViewHolder extends RecyclerView.ViewHolder{

        private TextView textUserLevel;
        private ImageView imageUserPicture;
        private TextView textUserName;
        private TextView textUserProfession;
        private Button btnAgainst;
        private Button btnPass;

        public ApplyListViewHolder(View view) {
            super(view);
            textUserLevel = (TextView) view.findViewById(R.id.textUserLevel);
            imageUserPicture = (ImageView) view.findViewById(R.id.imageUserPicture);
            textUserName = (TextView) view.findViewById(R.id.textUserName);
            textUserProfession = (TextView) view.findViewById(R.id.textUserProfession);
            btnAgainst = (Button) view.findViewById(R.id.btnAgainst);
            btnPass = (Button) view.findViewById(R.id.btnPass);
        }
    }
}
