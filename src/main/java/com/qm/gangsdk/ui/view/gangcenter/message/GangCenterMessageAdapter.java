package com.qm.gangsdk.ui.view.gangcenter.message;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.callback.DataCallBack;
import com.qm.gangsdk.core.outer.common.entity.XLGangCenterMessageBean;
import com.qm.gangsdk.core.outer.common.entity.XLGangCenterMessageCommonBean;
import com.qm.gangsdk.core.outer.common.entity.XLGangCenterMessageInviteBean;
import com.qm.gangsdk.core.outer.common.entity.XLGangInfoBean;
import com.qm.gangsdk.core.outer.receiver.GangPosterReceiver;
import com.qm.gangsdk.ui.GangSDK;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.custom.dialog.ViewTools;
import com.qm.gangsdk.ui.event.XLInviteRefuseEvent;
import com.qm.gangsdk.ui.view.common.GangConfigureUtils;
import com.qm.gangsdk.ui.utils.ImageLoadUtil;
import com.qm.gangsdk.ui.utils.TimeUtils;
import com.qm.gangsdk.ui.utils.XLToastUtil;
import com.qm.gangsdk.ui.view.common.GangModuleManage;

import java.util.ArrayList;
import java.util.List;


/**
 * 消息中心适配器
 * 消息类型包括：1-邀请加入；2-踢出公会；3-申请拒绝；4-系统消息
 */

public class GangCenterMessageAdapter extends RecyclerView.Adapter{

    private Activity context;
    private List<XLGangCenterMessageBean> list = new ArrayList<>();

    public GangCenterMessageAdapter(Activity context, List list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case XLGangCenterMessageBean.MSG_TYPE_INVITE_JOIN_GANG:
                view = LayoutInflater.from(context).inflate(R.layout.item_gangcenter_messgae_invite, parent, false);
                return new InviteViewHolder(view);
            case XLGangCenterMessageBean.MSG_TYPE_KICK_OUT:
            case XLGangCenterMessageBean.MSG_TYPE_REFUSE_APPLICATION:
            case XLGangCenterMessageBean.MSG_TYPE_SYSTEM_MESSAGE:
            default:
                view = LayoutInflater.from(context).inflate(R.layout.item_gangcenter_messgae_common, parent, false);
                return new CommonViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final XLGangCenterMessageBean gangCenterMessageBean = list.get(position);
        if(holder instanceof CommonViewHolder){
            final XLGangCenterMessageCommonBean messageBean = (XLGangCenterMessageCommonBean)gangCenterMessageBean.getMessageBean();
            CommonViewHolder viewHolder = (CommonViewHolder) holder;
            ImageLoadUtil.loadRoundImage(viewHolder.imageViewIcon, messageBean.getIconurl());
            viewHolder.textViewTitle.setText(messageBean.getTitle());
            viewHolder.textViewContent.setText(messageBean.getContent());
            viewHolder.textViewDateTime.setText(TimeUtils.getTime(messageBean.getCreatetime(), TimeUtils.DATE_FORMAT_DATE));

            if(!gangCenterMessageBean.hasRead()){
                viewHolder.RlBackground.setBackgroundResource(R.drawable.qm_bg_item);
                viewHolder.textViewTitle.setTextColor(ContextCompat.getColor(context,R.color.xlmessagecenter_item_common_unread_title_color));
                viewHolder.textViewContent.setTextColor(ContextCompat.getColor(context,R.color.xlmessagecenter_item_common_unread_content_color));
                viewHolder.textViewDateTime.setTextColor(ContextCompat.getColor(context,R.color.xlmessagecenter_item_common_unread_datetime_color));
            }else{
                viewHolder.RlBackground.setBackgroundResource(R.drawable.qm_bg_message_notify_hasread);
                viewHolder.textViewTitle.setTextColor(ContextCompat.getColor(context,R.color.xlmessagecenter_item_common_read_title_color));
                viewHolder.textViewContent.setTextColor(ContextCompat.getColor(context,R.color.xlmessagecenter_item_common_read_content_color));
                viewHolder.textViewDateTime.setTextColor(ContextCompat.getColor(context,R.color.xlmessagecenter_item_common_read_datetime_color));
            }

            viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    GangSDK.getInstance().userManager().deleteMessageNotification(gangCenterMessageBean.getMessageid(), new DataCallBack() {
                        @Override
                        public void onSuccess(int status, String message, Object data) {
                            list.remove(position);
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onFail(String message) {
                            XLToastUtil.showToastShort("删除失败");
                        }
                    });
                }
            });
            viewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    DialogMessageNotificationFragment dialog = new DialogMessageNotificationFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("message",messageBean);
                    dialog.setArguments(bundle);
                    dialog.show(context.getFragmentManager());

                    if(!gangCenterMessageBean.hasRead()){
                        GangSDK.getInstance().userManager().updateMessageNotificationStateRead(gangCenterMessageBean.getMessageid(), new DataCallBack() {
                            @Override
                            public void onSuccess(int status, String message, Object data) {
                                gangCenterMessageBean.setIsread(1);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onFail(String message) {
                                XLToastUtil.showToastShort("删除失败");
                            }
                        });
                    }
                }
            });
        }else if(holder instanceof InviteViewHolder){
            final XLGangCenterMessageInviteBean messageBean = (XLGangCenterMessageInviteBean)gangCenterMessageBean.getMessageBean();
            InviteViewHolder viewHolder = (InviteViewHolder) holder;
            ImageLoadUtil.loadRoundImage(viewHolder.imageViewGangIcon, messageBean.getConsortiaicon());
            viewHolder.textViewGangLevel.setText(messageBean.getConsortialevel().toString() + "级");
            viewHolder.textViewGangName.setText(messageBean.getConsortianame());
            viewHolder.textViewGangPeopleNumber.setText(messageBean.getConsortianownum().toString());
            viewHolder.textViewGangDescribe.setText(messageBean.getConsortiadeclaration());
            viewHolder.btnRefuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    dealGangInvitation(messageBean.getVisitid(), 2, gangCenterMessageBean.getMessageid(), position);
                }
            });
            viewHolder.btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    dealGangInvitation(messageBean.getVisitid(), 1, gangCenterMessageBean.getMessageid(), position);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        final XLGangCenterMessageBean messageBean = list.get(position);
        return messageBean.getMsgtype().intValue();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * 通用的ViewHolder
     */
    class CommonViewHolder extends ViewHolder{

        private RelativeLayout RlBackground;
        private ImageView imageViewIcon;
        private TextView textViewTitle;
        private TextView textViewContent;
        private TextView textViewDateTime;
        private Button btnDelete;
        private Button btnDetail;

        public CommonViewHolder(View view) {
            super(view);
            RlBackground = (RelativeLayout) view.findViewById(R.id.RlBackground);
            imageViewIcon = (ImageView) view.findViewById(R.id.imageViewIcon);
            textViewTitle = (TextView) view.findViewById(R.id.textViewTitle);
            textViewContent = (TextView) view.findViewById(R.id.textViewContent);
            textViewDateTime = (TextView) view.findViewById(R.id.textViewDateTime);
            btnDelete = (Button) view.findViewById(R.id.btnDelete);
            btnDetail = (Button) view.findViewById(R.id.btnDetail);
        }
    }

    /**
     * 接收文本消息ViewHolder
     */
    class InviteViewHolder extends ViewHolder {
        private ImageView imageViewGangIcon;
        private TextView textViewGangLevel;
        private TextView textViewGangName;
        private TextView textViewGangPeopleNumber;
        private TextView textViewGangDescribe;
        private Button btnRefuse;
        private Button btnAccept;

        public InviteViewHolder(View view) {
            super(view);
            imageViewGangIcon = (ImageView) view.findViewById(R.id.imageViewGangIcon);
            textViewGangLevel = (TextView) view.findViewById(R.id.textViewGangLevel);
            textViewGangName = (TextView) view.findViewById(R.id.textViewGangName);
            textViewGangPeopleNumber = (TextView) view.findViewById(R.id.textViewGangPeopleNumber);
            textViewGangDescribe = (TextView) view.findViewById(R.id.textViewGangDescribe);
            btnRefuse = (Button) view.findViewById(R.id.btnRefuse);
            btnAccept = (Button) view.findViewById(R.id.btnAccept);
        }
    }

    /**
     * 申请加入
     * @param inviteid       邀请ID
     * @param state          状态：1--接受；2--拒绝
     * @param messageid      消息ID
     */
    private void dealGangInvitation(int inviteid, final int state, int messageid, final int position) {
        final Dialog loading = ViewTools.createLoadingDialog(context,"正在提交数据...", false);
        loading.show();
        GangSDK.getInstance().userManager().dealGangInvitation(inviteid, state, messageid, new DataCallBack<XLGangInfoBean>() {
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
                    list.remove(position);
                    notifyDataSetChanged();
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

