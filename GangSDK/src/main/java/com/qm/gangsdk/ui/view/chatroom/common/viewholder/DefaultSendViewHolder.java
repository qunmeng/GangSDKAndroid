package com.qm.gangsdk.ui.view.chatroom.common.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.entity.XLMessageBean;
import com.qm.gangsdk.core.outer.common.entity.XLMessageBody;
import com.qm.gangsdk.core.outer.receiver.GangPosterReceiver;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.event.XLChatSingleEvent;
import com.qm.gangsdk.ui.utils.ImageLoadUtil;
import com.qm.gangsdk.ui.view.common.GangChatPopWindow;

/**
 * Author: mengbo
 * Time: 2017/12/15 16:03
 * Description:  未找到viewtype时对应的ViewHolder
 */

public class DefaultSendViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageUserPic;
    private ImageView imageGangIcon;
    private TextView textUserName;

    public DefaultSendViewHolder(View view){
        super(view);
        imageUserPic = (ImageView) view.findViewById(R.id.imageUserPic);
        imageGangIcon = (ImageView) view.findViewById(R.id.imageGangIcon);
        textUserName = (TextView) view.findViewById(R.id.textUserName);
    }

    public static DefaultSendViewHolder newViewHolder(Context context, ViewGroup parent) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_gangchat_default_send, parent, false);
        return new DefaultSendViewHolder(view);
    }

    public void bindData(final Context context, final XLMessageBody messageBody){
        ImageLoadUtil.loadCircleImage(imageUserPic, messageBody.getIconurl());
        if(messageBody.getChanneltype() != null) {
            if (XLMessageBean.ChannelType.RECRUIT.value() == messageBody.getChanneltype().intValue()) {
                imageGangIcon.setVisibility(View.VISIBLE);
                ImageLoadUtil.loadCircleImage(imageGangIcon, messageBody.getConsortiaiconurl());
                textUserName.setText(messageBody.getNickname());
            } else if (XLMessageBean.ChannelType.GANG.value() == messageBody.getChanneltype().intValue()){
                imageGangIcon.setVisibility(View.GONE);
                if(messageBody.getRolename() != null) {
                    textUserName.setText(messageBody.getNickname() + " <" + messageBody.getRolename() + ">");
                }else {
                    textUserName.setText(messageBody.getNickname());
                }
            }else {
                imageGangIcon.setVisibility(View.GONE);
                textUserName.setText(messageBody.getNickname());
            }
        }else {
            imageGangIcon.setVisibility(View.GONE);
            textUserName.setText(messageBody.getNickname());
        }
        imageUserPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GangChatPopWindow popWindowUtils = new GangChatPopWindow(new GangChatPopWindow.ClickCallBack() {
                    @Override
                    public void chatSingle() {
                        XLChatSingleEvent chatSingleEvent = new XLChatSingleEvent(messageBody);
                        GangPosterReceiver.post(chatSingleEvent);
                    }
                });
                popWindowUtils.showGangChatPopWindow(context, imageUserPic, messageBody.getConsortiaid(), messageBody.getUserid());
            }
        });
    }
}
