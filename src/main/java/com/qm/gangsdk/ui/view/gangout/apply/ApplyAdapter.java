package com.qm.gangsdk.ui.view.gangout.apply;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.callback.DataCallBack;
import com.qm.gangsdk.core.outer.receiver.GangPosterReceiver;
import com.qm.gangsdk.core.outer.common.entity.XLGangApplyListBean;
import com.qm.gangsdk.ui.GangSDK;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.custom.dialog.ViewTools;
import com.qm.gangsdk.ui.event.XLApplyCancelEvent;
import com.qm.gangsdk.ui.utils.ImageLoadUtil;
import com.qm.gangsdk.ui.utils.XLToastUtil;
import com.qm.gangsdk.ui.view.common.DialogGangInfoFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lijiyuan on 2017/9/12.
 * 申请adapter
 */

public class ApplyAdapter extends RecyclerView.Adapter<ApplyAdapter.InviteViewHolder> {

    private Activity context;
    private List<XLGangApplyListBean> list = new ArrayList<>();
    public ApplyAdapter(Activity context, List list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public InviteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_apply, parent, false);
        return new InviteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InviteViewHolder holder, int position) {
        final XLGangApplyListBean applyBean = list.get(position);

        ImageLoadUtil.loadRoundImage(holder.imageGangIcon, applyBean.getIconurl());
        holder.textGangLevel.setText(String.valueOf(applyBean.getBuildlevel()) + "级");
        holder.textGangName.setText(applyBean.getConsortianame());
        ImageLoadUtil.loadRoundImage(holder.imageGangLabel, applyBean.getLabelurl());
        holder.textGangDescribe.setText(applyBean.getDeclaration());
        holder.textGangPeopleNumber.setText(String.valueOf(applyBean.getNownum()));
        holder.relateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                new DialogGangInfoFragment()
                        .setGangId(applyBean.getConsortiaid().intValue())
                        .show(context.getFragmentManager());
            }
        });

        holder.btnGangApplyCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (applyBean.getApplicationid() != null) {
                    cancelGangApplyJoin(applyBean.getApplicationid());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class InviteViewHolder extends RecyclerView.ViewHolder {
        private View relateView;
        private ImageView imageGangIcon;
        private TextView textGangLevel;
        private TextView textGangName;
        private ImageView imageGangLabel;
        private TextView textGangDescribe;
        private TextView textGangPeopleNumber;
        private Button btnGangApplyCancel;

        public InviteViewHolder(View view) {
            super(view);
            relateView = view.findViewById(R.id.relateSortView);
            imageGangIcon = (ImageView) view.findViewById(R.id.imageGangIcon);
            textGangLevel = (TextView) view.findViewById(R.id.textGangLevel);
            textGangName = (TextView) view.findViewById(R.id.textGangName);
            imageGangLabel = (ImageView) view.findViewById(R.id.imageGangLabel);
            textGangDescribe = (TextView) view.findViewById(R.id.textGangDescribe);
            textGangPeopleNumber = (TextView) view.findViewById(R.id.textGangPeopleNumber);
            btnGangApplyCancel = (Button) view.findViewById(R.id.btnGangApplyCancel);
        }
    }

    /**
     * 取消加入
     * @param applyid
     */
    private void cancelGangApplyJoin(int applyid) {
        final Dialog loading = ViewTools.createLoadingDialog(context,"正在提交数据...", false);
        loading.show();
        GangSDK.getInstance().userManager().cancelApplyJoinGang(applyid, new DataCallBack() {

            @Override
            public void onSuccess(int status, String message, Object data) {
                loading.dismiss();
                GangPosterReceiver.post(new XLApplyCancelEvent());
            }

            @Override
            public void onFail(String message) {
                loading.dismiss();
                XLToastUtil.showToastShort(message);
            }
        });
    }
}
