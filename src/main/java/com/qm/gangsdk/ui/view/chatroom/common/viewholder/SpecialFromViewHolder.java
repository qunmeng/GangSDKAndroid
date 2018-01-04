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
 * Time: 2017/12/15 18:24
 * Description: 接受特殊消息ViewHolder
 */

public class SpecialFromViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageFromUserPic;
    private ImageView imageGangIcon;
    private ImageView imageViewIcon;
    private TextView textFromUserName;
    private TextView textViewTitle;
    private TextView textViewContent;
    private RelativeLayout RlClickableView;

    public SpecialFromViewHolder(View view) {
        super(view);
        imageFromUserPic = (ImageView) view.findViewById(R.id.imageFromUserPic);
        imageGangIcon = (ImageView) view.findViewById(R.id.imageGangIcon);
        imageViewIcon = (ImageView) view.findViewById(R.id.imageViewIcon);
        textFromUserName = (TextView) view.findViewById(R.id.textFromUserName);
        textViewTitle = (TextView) view.findViewById(R.id.textViewTitle);
        textViewContent = (TextView) view.findViewById(R.id.textViewContent);
        RlClickableView = (RelativeLayout) view.findViewById(R.id.RlClickableView);
    }

    public static SpecialFromViewHolder newViewHolder(Context context, ViewGroup parent) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_gangchat_special_from, parent, false);
        return new SpecialFromViewHolder(view);
    }

    public void bindData(final Context context, final XLMessageBody messageBody){

        String message = messageBody.getMessage();
        ImageLoadUtil.loadCircleImage(imageFromUserPic, messageBody.getIconurl());
        if(messageBody.getChanneltype() != null) {
            if (XLMessageBean.ChannelType.RECRUIT.value() == messageBody.getChanneltype().intValue()) {
                imageGangIcon.setVisibility(View.VISIBLE);
                ImageLoadUtil.loadCircleImage(imageGangIcon, messageBody.getIconurl());
                textFromUserName.setText(messageBody.getNickname());
            } else if (XLMessageBean.ChannelType.GANG.value() == messageBody.getChanneltype().intValue()){
                imageGangIcon.setVisibility(View.GONE);
                textFromUserName.setText(messageBody.getNickname() + " <" + messageBody.getRolename() + ">");
            }else {
                imageGangIcon.setVisibility(View.GONE);
                textFromUserName.setText(messageBody.getNickname());
            }
        }else {
            imageGangIcon.setVisibility(View.GONE);
            textFromUserName.setText(messageBody.getNickname());
        }
        imageFromUserPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GangChatPopWindow popWindowUtils = new GangChatPopWindow(new GangChatPopWindow.ClickCallBack() {
                    @Override
                    public void chatSingle() {
                        XLChatSingleEvent chatSingleEvent = new XLChatSingleEvent(messageBody);
                        GangPosterReceiver.post(chatSingleEvent);
                    }
                });
                popWindowUtils.showGangChatPopWindow(context, imageFromUserPic, messageBody.getConsortiaid(), messageBody.getUserid());
            }
        });
        try{
            final XLMessageSpecialBean xlMessageSpecialBean = BeanUtils.convertToBean(new JSONObject(message), XLMessageSpecialBean.class);
//                ImageLoadUtil.loadCircleImage(viewHolder.imageViewIcon, xlMessageSpecialBean.getIcon());
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
