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
import com.qm.gangsdk.core.outer.receiver.GangPosterReceiver;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.event.XLChatSingleEvent;
import com.qm.gangsdk.ui.utils.DensityUtil;
import com.qm.gangsdk.ui.utils.ImageLoadUtil;
import com.qm.gangsdk.ui.view.common.GangChatPopWindow;
import com.xl.xlaudio.XLAudioClient;
import com.xl.xlaudio.XLAudioPlayerListener;

import static com.qm.gangsdk.ui.R.id.textSendTextUserName;

/**
 * Author: mengbo
 * Time: 2017/12/15 17:34
 * Description: 接受语音消息ViewHolder
 */

public class VoiceFromViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageFromVoiceUserPic;
    private ImageView imageGangIcon;
    private TextView textFromVoiceUserName;
    private TextView textFromVoiceTime;
    private LinearLayout linearFromVoice;
    private View imageFromVoice;

    public VoiceFromViewHolder(View view) {
        super(view);
        imageFromVoiceUserPic = (ImageView) view.findViewById(R.id.imageFromVoiceUserPic);
        imageGangIcon = (ImageView) view.findViewById(R.id.imageGangIcon);
        textFromVoiceUserName = (TextView) view.findViewById(R.id.textFromVoiceUserName);
        textFromVoiceTime = (TextView) view.findViewById(R.id.textFromVoiceTime);
        linearFromVoice = (LinearLayout) view.findViewById(R.id.linearFromVoice);
        imageFromVoice = view.findViewById(R.id.imageFromVoice);
    }

    public static VoiceFromViewHolder newViewHolder(Context context, ViewGroup parent) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_gangchat_voice_from, parent, false);
        return new VoiceFromViewHolder(view);
    }

    public void bindData(final Context context, final XLMessageBody messageBody){
        ImageLoadUtil.loadCircleImage(imageFromVoiceUserPic, messageBody.getIconurl());
        if(messageBody.getChanneltype() != null) {
            if (XLMessageBean.ChannelType.RECRUIT.value() == messageBody.getChanneltype().intValue()) {
                imageGangIcon.setVisibility(View.VISIBLE);
                ImageLoadUtil.loadCircleImage(imageGangIcon, messageBody.getConsortiaiconurl());
                textFromVoiceUserName.setText(messageBody.getNickname());
            }else if (XLMessageBean.ChannelType.GANG.value() == messageBody.getChanneltype().intValue()){
                imageGangIcon.setVisibility(View.GONE);
                if(messageBody.getRolename() != null) {
                    textFromVoiceUserName.setText(messageBody.getNickname() + " <" + messageBody.getRolename() + ">");
                }else {
                    textFromVoiceUserName.setText(messageBody.getNickname());
                }
            }else {
                imageGangIcon.setVisibility(View.GONE);
                textFromVoiceUserName.setText(messageBody.getNickname());
            }
        }else {
            imageGangIcon.setVisibility(View.GONE);
            textFromVoiceUserName.setText(messageBody.getNickname());
        }
        textFromVoiceTime.setText(StringUtils.getString(messageBody.getVoicetime(), "") + "″");
        imageFromVoiceUserPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GangChatPopWindow popWindowUtils = new GangChatPopWindow(new GangChatPopWindow.ClickCallBack() {
                    @Override
                    public void chatSingle() {
                        XLChatSingleEvent chatSingleEvent = new XLChatSingleEvent(messageBody);
                        GangPosterReceiver.post(chatSingleEvent);
                    }
                });
                popWindowUtils.showGangChatPopWindow(context, imageFromVoiceUserPic, messageBody.getConsortiaid(), messageBody.getUserid());
            }
        });
        LinearLayout.LayoutParams layoutParams = null;
        if(messageBody.getVoicetime() > 10){
            layoutParams = new LinearLayout.LayoutParams(DensityUtil.dip2px(context, 140), DensityUtil.dip2px(context, 20));
        }else if(messageBody.getVoicetime() > 5){
            layoutParams = new LinearLayout.LayoutParams(DensityUtil.dip2px(context, 120), DensityUtil.dip2px(context, 20));
        }else {
            layoutParams = new LinearLayout.LayoutParams(DensityUtil.dip2px(context, 100), DensityUtil.dip2px(context, 20));
        }
        linearFromVoice.setLayoutParams(layoutParams);
        linearFromVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageFromVoice.setBackgroundResource(R.drawable.qm_play_receiver_voice_anim);
                AnimationDrawable drawable = (AnimationDrawable) imageFromVoice.getBackground();
                drawable.start();
                XLAudioClient.sharedInstance().stopAll();
                XLAudioClient.sharedInstance().play(messageBody.getMessage(), new XLAudioPlayerListener() {
                    @Override
                    public void onFinished(String url) {
                        imageFromVoice.setBackgroundResource(R.mipmap.qm_record_volume_left3);
                    }
                });
            }
        });
    }
}
