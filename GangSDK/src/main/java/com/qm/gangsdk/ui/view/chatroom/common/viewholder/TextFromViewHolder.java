package com.qm.gangsdk.ui.view.chatroom.common.viewholder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.entity.XLMessageBean;
import com.qm.gangsdk.core.outer.common.entity.XLMessageBody;
import com.qm.gangsdk.core.outer.receiver.GangPosterReceiver;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.custom.style.SpannableStringStyle;
import com.qm.gangsdk.ui.entity.SpannableStringBean;
import com.qm.gangsdk.ui.event.XLChatSingleEvent;
import com.qm.gangsdk.ui.utils.ImageLoadUtil;
import com.qm.gangsdk.ui.view.common.GangChatPopWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: mengbo
 * Time: 2017/12/15 16:03
 * Description: 接受文本消息ViewHolder
 */

public class TextFromViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageFromTextUserPic;
    private ImageView imageGangIcon;
    private TextView textFromTextUserName;
    private TextView textFromTextMessage;

    public TextFromViewHolder(View view){
        super(view);
        imageFromTextUserPic = (ImageView) view.findViewById(R.id.imageFromTextUserPic);
        imageGangIcon = (ImageView) view.findViewById(R.id.imageGangIcon);
        textFromTextUserName = (TextView) view.findViewById(R.id.textFromTextUserName);
        textFromTextMessage = (TextView) view.findViewById(R.id.textFromTextMessage);
    }

    public static TextFromViewHolder newViewHolder(Context context, ViewGroup parent) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_gangchat_text_from, parent, false);
        return new TextFromViewHolder(view);
    }

    public void bindData(final Context context, final XLMessageBody messageBody){
        ImageLoadUtil.loadCircleImage(imageFromTextUserPic, messageBody.getIconurl());
        if(messageBody.getChanneltype() != null){
            if(XLMessageBean.ChannelType.RECRUIT.value() == messageBody.getChanneltype().intValue()){
                imageGangIcon.setVisibility(View.VISIBLE);
                ImageLoadUtil.loadCircleImage(imageGangIcon, messageBody.getConsortiaiconurl());
                textFromTextUserName.setText(messageBody.getNickname());
            }else if(XLMessageBean.ChannelType.GANG.value() == messageBody.getChanneltype().intValue()){
                imageGangIcon.setVisibility(View.GONE);
                if(messageBody.getRolename() != null) {
                    textFromTextUserName.setText(messageBody.getNickname() + " <" + messageBody.getRolename() + ">");
                }else {
                    textFromTextUserName.setText(messageBody.getNickname());
                }
            }else if(XLMessageBean.ChannelType.CHATSINGLE.value() == messageBody.getChanneltype().intValue()){
                imageGangIcon.setVisibility(View.GONE);
                List<SpannableStringBean> list = new ArrayList<>();
                list.add(new SpannableStringBean(messageBody.getNickname(), ContextCompat.getColor(context, R.color.xlchatsingle_item_nickname_color)));
                list.add(new SpannableStringBean(" 对你说：", 0));
                SpannableStringBuilder spannableStringBuilder = SpannableStringStyle.buildStyle(list, ContextCompat.getColor(context, R.color.xlchatsingle_item_common_text_color));
                textFromTextUserName.setText(spannableStringBuilder);
            }else {
                imageGangIcon.setVisibility(View.GONE);
                textFromTextUserName.setText(messageBody.getNickname());
            }
        }else {
            imageGangIcon.setVisibility(View.GONE);
            textFromTextUserName.setText(messageBody.getNickname());
        }
        textFromTextMessage.setText(messageBody.getMessage());
        imageFromTextUserPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GangChatPopWindow popWindowUtils = new GangChatPopWindow(new GangChatPopWindow.ClickCallBack() {
                    @Override
                    public void chatSingle() {
                        XLChatSingleEvent chatSingleEvent = new XLChatSingleEvent(messageBody);
                        GangPosterReceiver.post(chatSingleEvent);
                    }
                });
                popWindowUtils.showGangChatPopWindow(context, imageFromTextUserPic, messageBody.getConsortiaid(), messageBody.getUserid());
            }
        });
    }
}
