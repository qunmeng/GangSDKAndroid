package com.qm.gangsdk.ui.view.chatroom.common.viewholder;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.entity.XLMessageBean;
import com.qm.gangsdk.core.outer.common.entity.XLMessageBody;
import com.qm.gangsdk.core.outer.common.utils.StringUtils;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.utils.DensityUtil;
import com.qm.gangsdk.ui.utils.ImageLoadUtil;
import com.xl.xlaudio.XLAudioClient;
import com.xl.xlaudio.XLAudioPlayerListener;

/**
 * Author: mengbo
 * Time: 2017/12/15 17:37
 * Description: 发送语音消息ViewHolder
 */

public class VoiceSendViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageSendVoiceUserPic;
    private ImageView imageGangIcon;
    private TextView textSendVoiceUserName;
    private TextView textSendVoiceTime;
    private LinearLayout linearSendVoice;
    private View imageSendVoice;

    public VoiceSendViewHolder(View view) {
        super(view);
        imageSendVoiceUserPic = (ImageView) view.findViewById(R.id.imageSendVoiceUserPic);
        imageGangIcon = (ImageView) view.findViewById(R.id.imageGangIcon);
        textSendVoiceUserName = (TextView) view.findViewById(R.id.textSendVoiceUserName);
        textSendVoiceTime = (TextView) view.findViewById(R.id.textSendVoiceTime);
        linearSendVoice = (LinearLayout) view.findViewById(R.id.linearSendVoice);
        imageSendVoice = view.findViewById(R.id.imageSendVoice);
    }

    public static VoiceSendViewHolder newViewHolder(Context context, ViewGroup parent) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_gangchat_voice_send, parent, false);
        return new VoiceSendViewHolder(view);
    }

    public void bindData(final Context context, final XLMessageBody messageBody){
        ImageLoadUtil.loadCircleImage(imageSendVoiceUserPic, messageBody.getIconurl());
        if(messageBody.getChanneltype() != null) {
            if (XLMessageBean.ChannelType.RECRUIT.value() == messageBody.getChanneltype().intValue()) {
                imageGangIcon.setVisibility(View.VISIBLE);
                ImageLoadUtil.loadCircleImage(imageGangIcon, messageBody.getIconurl());
                textSendVoiceUserName.setText(messageBody.getNickname());
            } else if(XLMessageBean.ChannelType.GANG.value() == messageBody.getChanneltype().intValue()){
                imageGangIcon.setVisibility(View.GONE);
                textSendVoiceUserName.setText(messageBody.getNickname() + " <" + messageBody.getRolename() + ">");
            }else {
                imageGangIcon.setVisibility(View.GONE);
                textSendVoiceUserName.setText(messageBody.getNickname());
            }
        }else {
            imageGangIcon.setVisibility(View.GONE);
            textSendVoiceUserName.setText(messageBody.getNickname());
        }
        textSendVoiceTime.setText(StringUtils.getString(messageBody.getVoicetime(), "") + "″");
        LinearLayout.LayoutParams layoutParams = null;
        if(messageBody.getVoicetime() > 10){
            layoutParams = new LinearLayout.LayoutParams(DensityUtil.dip2px(context, 140), DensityUtil.dip2px(context, 20));
        }else if(messageBody.getVoicetime() > 5){
            layoutParams = new LinearLayout.LayoutParams(DensityUtil.dip2px(context, 120), DensityUtil.dip2px(context, 20));
        }else {
            layoutParams = new LinearLayout.LayoutParams(DensityUtil.dip2px(context, 100), DensityUtil.dip2px(context, 20));
        }
        linearSendVoice.setLayoutParams(layoutParams);
        linearSendVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSendVoice.setBackgroundResource(R.drawable.qm_play_send_voice_anim);
                AnimationDrawable drawable = (AnimationDrawable) imageSendVoice.getBackground();
                drawable.start();
                XLAudioClient.sharedInstance().stopAll();
                XLAudioClient.sharedInstance().play(messageBody.getMessage(), new XLAudioPlayerListener() {
                    @Override
                    public void onFinished(String url) {
                        imageSendVoice.setBackgroundResource(R.mipmap.qm_record_volume_right3);
                    }
                });
            }
        });
    }
}
