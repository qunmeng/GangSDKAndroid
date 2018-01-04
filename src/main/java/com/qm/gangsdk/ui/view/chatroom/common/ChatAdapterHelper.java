package com.qm.gangsdk.ui.view.chatroom.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.qm.gangsdk.core.outer.common.entity.XLMessageBean;
import com.qm.gangsdk.core.outer.common.entity.XLMessageBody;
import com.qm.gangsdk.ui.GangSDK;
import com.qm.gangsdk.ui.view.chatroom.common.viewholder.DefaultFromViewHolder;
import com.qm.gangsdk.ui.view.chatroom.common.viewholder.DefaultSendViewHolder;
import com.qm.gangsdk.ui.view.chatroom.common.viewholder.SpecialFromViewHolder;
import com.qm.gangsdk.ui.view.chatroom.common.viewholder.SpecialSendViewHolder;
import com.qm.gangsdk.ui.view.chatroom.common.viewholder.SystemViewHolder;
import com.qm.gangsdk.ui.view.chatroom.common.viewholder.TextFromViewHolder;
import com.qm.gangsdk.ui.view.chatroom.common.viewholder.TextSendViewHolder;
import com.qm.gangsdk.ui.view.chatroom.common.viewholder.VoiceFromViewHolder;
import com.qm.gangsdk.ui.view.chatroom.common.viewholder.VoiceSendViewHolder;

import java.util.List;

/**
 * Author: mengbo
 * Time: 2017/12/15 18:01
 * Description: ChatAdapterHelper
 */

public class ChatAdapterHelper {

    public static final int MSG_SYSTEM_RECEIVE = 0;
    public static final int MSG_DEFAULT_FROM = 1;
    public static final int MSG_DEFAULT_SEND = 2;
    public static final int MSG_TEXT_FROM = 3;
    public static final int MSG_TEXT_SEND = 4;
    public static final int MSG_VOICE_FROM = 5;
    public static final int MSG_VOICE_SEND = 6;
    public static final int MSG_SPECIAL_FROM = 7;
    public static final int MSG_SPECIAL_SEND = 8;

    private Context context;
    private List<XLMessageBody> list;

    public ChatAdapterHelper(Context context, List<XLMessageBody> list) {
        this.context = context;
        this.list = list;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case MSG_SYSTEM_RECEIVE:
                return SystemViewHolder.newViewHolder(context, parent);
            case MSG_DEFAULT_FROM:
                return DefaultFromViewHolder.newViewHolder(context, parent);
            case MSG_DEFAULT_SEND:
                return DefaultSendViewHolder.newViewHolder(context, parent);
            case MSG_TEXT_FROM:
                return TextFromViewHolder.newViewHolder(context, parent);
            case MSG_TEXT_SEND:
                return TextSendViewHolder.newViewHolder(context, parent);
            case MSG_VOICE_FROM:
                return VoiceFromViewHolder.newViewHolder(context, parent);
            case MSG_VOICE_SEND:
                return VoiceSendViewHolder.newViewHolder(context, parent);
            case MSG_SPECIAL_FROM:
                return SpecialFromViewHolder.newViewHolder(context, parent);
            case MSG_SPECIAL_SEND:
                return SpecialSendViewHolder.newViewHolder(context, parent);
        }
        return null;
    }

    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position, final int viewType) {
        final XLMessageBody messageBody = list.get(position);
        switch (viewType) {
            case MSG_SYSTEM_RECEIVE:
                ((SystemViewHolder) holder).bindData(context, messageBody);
                break;
            case MSG_DEFAULT_FROM:
                ((DefaultFromViewHolder) holder).bindData(context, messageBody);
                break;
            case MSG_DEFAULT_SEND:
                ((DefaultSendViewHolder) holder).bindData(context, messageBody);
                break;
            case MSG_TEXT_FROM:
                ((TextFromViewHolder) holder).bindData(context, messageBody);
                break;
            case MSG_TEXT_SEND:
                ((TextSendViewHolder) holder).bindData(context, messageBody);
                break;
            case MSG_VOICE_FROM:
                ((VoiceFromViewHolder) holder).bindData(context, messageBody);
                break;
            case MSG_VOICE_SEND:
                ((VoiceSendViewHolder) holder).bindData(context, messageBody);
                break;
            case MSG_SPECIAL_FROM:
                ((SpecialFromViewHolder) holder).bindData(context, messageBody);
                break;
            case MSG_SPECIAL_SEND:
                ((SpecialSendViewHolder) holder).bindData(context, messageBody);
                break;
        }
    }

    public int getItemViewType(final int position) {
        final XLMessageBody messageBody = list.get(position);
        if (MSG_SYSTEM_RECEIVE == messageBody.getUserid().intValue()) {
            return MSG_SYSTEM_RECEIVE;
        } else if (XLMessageBean.MessageType.TEXT.value() == messageBody.getMessagetype().intValue()) {
            if (GangSDK.getInstance().userManager().getXlUserBean().getUserid() != null) {
                if (GangSDK.getInstance().userManager().getXlUserBean().getUserid().intValue() == messageBody.getUserid().intValue()) {
                    return MSG_TEXT_SEND;
                } else {
                    return MSG_TEXT_FROM;
                }
            }
        } else if (XLMessageBean.MessageType.VOICE.value() == messageBody.getMessagetype().intValue()) {
            if (GangSDK.getInstance().userManager().getXlUserBean().getUserid() != null) {
                if (GangSDK.getInstance().userManager().getXlUserBean().getUserid().intValue() == messageBody.getUserid().intValue()) {
                    return MSG_VOICE_SEND;
                } else {
                    return MSG_VOICE_FROM;
                }
            }
        } else if (XLMessageBean.MessageType.SPECIAL.value() == messageBody.getMessagetype().intValue()) {
            if (GangSDK.getInstance().userManager().getXlUserBean().getUserid() != null) {
                if (GangSDK.getInstance().userManager().getXlUserBean().getUserid().intValue() == messageBody.getUserid().intValue()) {
                    return MSG_SPECIAL_SEND;
                } else {
                    return MSG_SPECIAL_FROM;
                }
            }
        } else {
            if (GangSDK.getInstance().userManager().getXlUserBean().getUserid() != null) {
                if (GangSDK.getInstance().userManager().getXlUserBean().getUserid().intValue() == messageBody.getUserid().intValue()) {
                    return MSG_DEFAULT_SEND;
                } else {
                    return MSG_DEFAULT_FROM;
                }
            }
        }
        return MSG_DEFAULT_FROM;
    }
}
