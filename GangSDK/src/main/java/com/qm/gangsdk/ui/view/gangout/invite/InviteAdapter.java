package com.qm.gangsdk.ui.view.gangout.invite;

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
import com.qm.gangsdk.core.outer.common.entity.XLGangInfoBean;
import com.qm.gangsdk.core.outer.common.entity.XLGangInviteListBean;
import com.qm.gangsdk.ui.GangSDK;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.custom.dialog.ViewTools;
import com.qm.gangsdk.ui.event.XLInviteRefuseEvent;
import com.qm.gangsdk.ui.view.common.GangConfigureUtils;
import com.qm.gangsdk.ui.utils.ImageLoadUtil;
import com.qm.gangsdk.ui.utils.XLToastUtil;
import com.qm.gangsdk.ui.view.common.GangModuleManage;
import com.qm.gangsdk.ui.view.common.DialogGangInfoFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijiyuan on 2017/9/12.
 */

public class InviteAdapter extends RecyclerView.Adapter<InviteAdapter.InviteViewHolder> {

    private Activity context;
    private List<XLGangInviteListBean> list = new ArrayList<>();
    public InviteAdapter(Activity context, List list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public InviteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_invite, parent, false);
        return new InviteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InviteViewHolder holder, int position) {
        final XLGangInviteListBean inviteBean = list.get(position);

        ImageLoadUtil.loadRoundImage(holder.imageGangIcon, inviteBean.getIconurl());
        holder.textGangLevel.setText(String.valueOf(inviteBean.getBuildlevel()) + "级");
        holder.textGangName.setText(inviteBean.getConsortianame());
        ImageLoadUtil.loadRoundImage(holder.imageGangLabel, inviteBean.getLabelurl());
        holder.textGangDescribe.setText(inviteBean.getDeclaration());
        holder.textGangPeopleNumber.setText(String.valueOf(inviteBean.getNownum()));
        holder.relateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                new DialogGangInfoFragment()
                        .setGangId(inviteBean.getConsortiaid().intValue())
                        .show(context.getFragmentManager());
            }
        });

        holder.btnGangInviteCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelGangApplyJoin(inviteBean.getVisitid(), 2);
            }
        });

        holder.btnGangInviteConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelGangApplyJoin(inviteBean.getVisitid(), 1);
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
        private Button btnGangInviteConfirm;
        private Button btnGangInviteCancel;

        public InviteViewHolder(View view) {
            super(view);
            relateView = view.findViewById(R.id.relateSortView);
            imageGangIcon = (ImageView) view.findViewById(R.id.imageGangIcon);
            textGangLevel = (TextView) view.findViewById(R.id.textGangLevel);
            textGangName = (TextView) view.findViewById(R.id.textGangName);
            imageGangLabel = (ImageView) view.findViewById(R.id.imageGangLabel);
            textGangDescribe = (TextView) view.findViewById(R.id.textGangDescribe);
            textGangPeopleNumber = (TextView) view.findViewById(R.id.textGangPeopleNumber);
            btnGangInviteConfirm = (Button) view.findViewById(R.id.btnGangInviteConfirm);
            btnGangInviteCancel = (Button) view.findViewById(R.id.btnGangInviteCancel);
        }
    }

    /**
     * 取消加入
     * @param inviteid
     */
    private void cancelGangApplyJoin(int inviteid, final int state) {
        final Dialog loading = ViewTools.createLoadingDialog(context,"正在提交数据...", false);
        loading.show();
        GangSDK.getInstance().userManager().dealGangInvitation(inviteid, state, -1, new DataCallBack<XLGangInfoBean>() {
            @Override
            public void onSuccess(int status, String message, XLGangInfoBean data) {
                loading.dismiss();
                if (data != null) {
                    context.finish();
                    GangModuleManage.toInGangTabActivity(context);
                    XLToastUtil.showToastShort("加入" + GangConfigureUtils.getGangName() + "成功");
                } else {
                    XLToastUtil.showToastShort(context.getResources().getString(R.string.message_gang_invite_against));
                    GangPosterReceiver.post(new XLInviteRefuseEvent());
                }
            }

            @Override
            public void onFail(String message) {
                loading.dismiss();
                XLToastUtil.showToastShort(message);
            }

        });
    }
}
