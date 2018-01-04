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
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.custom.style.SpannableStringStyle;
import com.qm.gangsdk.ui.entity.SpannableStringBean;
import com.qm.gangsdk.ui.utils.ImageLoadUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: mengbo
 * Time: 2017/12/15 17:02
 * Description: 发送文本消息ViewHolder
 */

public class TextSendViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageSendTextUserPic;
    private ImageView imageGangIcon;
    private TextView textSendTextUserName;
    private TextView textSendTextMessage;

    public TextSendViewHolder(View view) {
        super(view);
        imageSendTextUserPic = (ImageView) view.findViewById(R.id.imageSendTextUserPic);
        imageGangIcon = (ImageView) view.findViewById(R.id.imageGangIcon);
        textSendTextUserName = (TextView) view.findViewById(R.id.textSendTextUserName);
        textSendTextMessage = (TextView) view.findViewById(R.id.textSendTextMessage);
    }

    public static TextSendViewHolder newViewHolder(Context context, ViewGroup parent) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_gangchat_text_send, parent, false);
        return new TextSendViewHolder(view);
    }

    public void bindData(final Context context, final XLMessageBody messageBody){
        if(messageBody.getChanneltype() != null){
            if(XLMessageBean.ChannelType.CHATSINGLE.value() == messageBody.getChanneltype().intValue()){
                imageGangIcon.setVisibility(View.GONE);
                List<SpannableStringBean> list = new ArrayList<>();
                list.add(new SpannableStringBean("我对 ", 0));
                list.add(new SpannableStringBean(messageBody.getTonickname(), ContextCompat.getColor(context, R.color.xlchatsingle_item_nickname_color)));
                list.add(new SpannableStringBean(" 说：", 0));
                SpannableStringBuilder spannableStringBuilder = SpannableStringStyle.buildStyle(list, ContextCompat.getColor(context, R.color.xlchatsingle_item_common_text_color));
                textSendTextUserName.setText(spannableStringBuilder);
            }else if(XLMessageBean.ChannelType.RECRUIT.value() == messageBody.getChanneltype().intValue()){
                imageGangIcon.setVisibility(View.VISIBLE);
                ImageLoadUtil.loadCircleImage(imageGangIcon, messageBody.getIconurl());
                textSendTextUserName.setText(messageBody.getNickname());
            }else if(XLMessageBean.ChannelType.GANG.value() == messageBody.getChanneltype().intValue()){
                imageGangIcon.setVisibility(View.GONE);
                textSendTextUserName.setText(messageBody.getNickname() + " <" + messageBody.getRolename() + ">");
            }else {
                imageGangIcon.setVisibility(View.GONE);
                textSendTextUserName.setText(messageBody.getNickname());
            }
        }else {
            imageGangIcon.setVisibility(View.GONE);
            textSendTextUserName.setText(messageBody.getNickname());
        }
        ImageLoadUtil.loadCircleImage(imageSendTextUserPic, messageBody.getIconurl());
        textSendTextMessage.setText(messageBody.getMessage());
    }
}
