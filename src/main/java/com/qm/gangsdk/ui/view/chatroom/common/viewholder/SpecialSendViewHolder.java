package com.qm.gangsdk.ui.view.chatroom.common.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.entity.XLMessageBean;
import com.qm.gangsdk.core.outer.common.entity.XLMessageBody;
import com.qm.gangsdk.core.outer.common.entity.XLMessageSpecialBean;
import com.qm.gangsdk.core.outer.common.utils.BeanUtils;
import com.qm.gangsdk.core.outer.manager.GangPosterManager;
import com.qm.gangsdk.core.outer.receiver.GangPosterReceiver;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.event.XLChatSingleEvent;
import com.qm.gangsdk.ui.utils.ImageLoadUtil;
import com.qm.gangsdk.ui.view.common.GangChatPopWindow;

import org.json.JSONObject;

/**
 * Author: mengbo
 * Time: 2017/12/15 18:28
 * Description: 发送特殊消息ViewHolder
 */

public class SpecialSendViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageSendUserPic;
    private ImageView imageViewIcon;
    private ImageView imageGangIcon;
    private TextView textSendUserName;
    private TextView textViewTitle;
    private TextView textViewContent;
    private RelativeLayout RlClickableView;

    public SpecialSendViewHolder(View view) {
        super(view);
        imageSendUserPic = (ImageView) view.findViewById(R.id.imageSendUserPic);
        imageViewIcon = (ImageView) view.findViewById(R.id.imageViewIcon);
        imageGangIcon = (ImageView) view.findViewById(R.id.imageGangIcon);
        textSendUserName = (TextView) view.findViewById(R.id.textSendUserName);
        textViewTitle = (TextView) view.findViewById(R.id.textViewTitle);
        textViewContent = (TextView) view.findViewById(R.id.textViewContent);
        RlClickableView = (RelativeLayout) view.findViewById(R.id.RlClickableView);
    }

    public static SpecialSendViewHolder newViewHolder(Context context, ViewGroup parent) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_gangchat_special_send, parent, false);
        return new SpecialSendViewHolder(view);
    }

    public void bindData(final Context context, final XLMessageBody messageBody){
        String message = messageBody.getMessage();
        ImageLoadUtil.loadCircleImage(imageSendUserPic, messageBody.getIconurl());
        if(messageBody.getChanneltype() != null) {
            if (XLMessageBean.ChannelType.RECRUIT.value() == messageBody.getChanneltype().intValue()) {
                imageGangIcon.setVisibility(View.VISIBLE);
                ImageLoadUtil.loadCircleImage(imageGangIcon, messageBody.getIconurl());
                textSendUserName.setText(messageBody.getNickname());
            } else if (XLMessageBean.ChannelType.GANG.value() == messageBody.getChanneltype().intValue()){
                imageGangIcon.setVisibility(View.GONE);
                textSendUserName.setText(messageBody.getNickname() + " <" + messageBody.getRolename() + ">");
            }else {
                imageGangIcon.setVisibility(View.GONE);
                textSendUserName.setText(messageBody.getNickname());
            }
        }else {
            imageGangIcon.setVisibility(View.GONE);
            textSendUserName.setText(messageBody.getNickname());
        }
        imageSendUserPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GangChatPopWindow popWindowUtils = new GangChatPopWindow(new GangChatPopWindow.ClickCallBack() {
                    @Override
                    public void chatSingle() {
                        XLChatSingleEvent chatSingleEvent = new XLChatSingleEvent(messageBody);
                        GangPosterReceiver.post(chatSingleEvent);
                    }
                });
                popWindowUtils.showGangChatPopWindow(context, imageSendUserPic, messageBody.getConsortiaid(), messageBody.getUserid());
            }
        });
        try{
            final XLMessageSpecialBean xlMessageSpecialBean = BeanUtils.convertToBean(new JSONObject(message), XLMessageSpecialBean.class);
//                ImageLoadUtil.loadCircleImage(imageViewIcon, xlMessageSpecialBean.getIcon());
            textViewTitle.setText(xlMessageSpecialBean.getTitle());
            textViewContent.setText(xlMessageSpecialBean.getContent());
            RlClickableView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GangPosterManager.postClickedSpecialMessageEvent(xlMessageSpecialBean);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
