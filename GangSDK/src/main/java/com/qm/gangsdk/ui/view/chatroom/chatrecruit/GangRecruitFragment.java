package com.qm.gangsdk.ui.view.chatroom.chatrecruit;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;

import com.qm.gangsdk.core.outer.common.callback.DataCallBack;
import com.qm.gangsdk.core.outer.common.entity.XLMessageBean;
import com.qm.gangsdk.core.outer.common.entity.XLMessageBody;
import com.qm.gangsdk.core.outer.common.utils.ListUtils;
import com.qm.gangsdk.core.outer.common.utils.StringUtils;
import com.qm.gangsdk.core.outer.receiver.GangPosterReceiver;
import com.qm.gangsdk.core.outer.receiver.base.OnGangReceiverListener;
import com.qm.gangsdk.core.outer.receiver.listener.GangReceiverListener;
import com.qm.gangsdk.ui.GangSDK;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseFragment;
import com.qm.gangsdk.ui.custom.button.XLAudioRecordButton;
import com.qm.gangsdk.ui.custom.headerfooter.HeaderAndFooterRecyclerViewAdapter;
import com.qm.gangsdk.ui.custom.headerfooter.RecyclerViewUtils;
import com.qm.gangsdk.ui.custom.loadingfooter.RecyclerViewStateUtils;
import com.qm.gangsdk.ui.event.XLChatSingleEvent;
import com.qm.gangsdk.ui.utils.DensityUtil;
import com.qm.gangsdk.ui.utils.TimeUtils;
import com.qm.gangsdk.ui.utils.XLKeyBoardUtil;
import com.qm.gangsdk.ui.utils.XLToastUtil;
import com.qm.gangsdk.ui.utils.XLVoiceRecorderManage;
import com.xl.undercover.mp3recorder.MP3Recorder;
import com.xl.views.ptr.PtrClassicFrameLayout;
import com.xl.views.ptr.PtrDefaultHandler;
import com.xl.views.ptr.PtrFrameLayout;
import com.xl.views.ptr.PtrHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by lijiyuan on 2017/8/4.
 * 社群招募
 */

public class GangRecruitFragment extends XLBaseFragment {

    private final static int pagesize = 10;
    private String endTime = null;

    private MP3Recorder mp3Recorder;

    private RecyclerView recyclerViewRecruit;
    private EditText editContent;
    private XLAudioRecordButton btnRecruitAudioRecord;
    private Button btnCommit;
    private ImageView imageSwitchVoice;
    private XLVoiceRecorderManage voiceRecorderManage;

    private RecruitChatAdapter adapter;
    private List<XLMessageBody> list = new ArrayList<>();
    private PtrClassicFrameLayout ptrFrameLayout;

    private GangReceiverListener receiveRecruitMessageListener;         //招募消息
    private GangReceiverListener sendChatSingleListener;                //私聊监听

    private boolean sendSuccessFlag = true;    //发送消息是否成功
    private int hintLength = 0;         //私聊提示字符串长度
    private String hintString = null;   //私聊提示字符串
    private boolean isChatSingle = false;   //是否是私聊
    private Integer toUserId = null;        //私聊玩家id

    @Override
    protected int getContentView() {
        return R.layout.fragment_recruit;
    }

    @Override
    protected void initData() {
        List messagesRecruit = GangSDK.getInstance().chatManager().getMessagesRecruitCache();
        if(messagesRecruit != null || !messagesRecruit.isEmpty()){
            list.clear();
            list.addAll(messagesRecruit);
            adapter.notifyDataSetChanged();
            int bottomPosition = adapter.getItemCount();
            if (bottomPosition > 0) {
                recyclerViewRecruit.scrollToPosition(bottomPosition);
            }
        }

        if(list.size() > 0){
            endTime =  String.valueOf(list.get(0).getCreatetime());
        }
    }

    @Override
    protected void initView(View view) {
        recyclerViewRecruit = (RecyclerView) view.findViewById(R.id.recyclerViewRecruit);
        ptrFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptrFrameLayout);

        editContent = (EditText) view.findViewById(R.id.editContent);
        btnRecruitAudioRecord = (XLAudioRecordButton) view.findViewById(R.id.btnRecruitAudioRecord);
        btnCommit = (Button) view.findViewById(R.id.btnCommit);
        imageSwitchVoice = (ImageView) view.findViewById(R.id.imageSwitchVoice);

        bindRecyclerView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GangSDK.getInstance().receiverManager().removeReceiverListener(receiveRecruitMessageListener);
        GangSDK.getInstance().receiverManager().removeReceiverListener(sendChatSingleListener);
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        voiceRecorderManage = new XLVoiceRecorderManage();
        mp3Recorder = voiceRecorderManage.getRecorder();

        receiveRecruitMessageListener = GangSDK.getInstance().receiverManager().addReceiveRecruitMessageListener(this, new OnGangReceiverListener<XLMessageBody>() {
            @Override
            public void onReceived(XLMessageBody data) {
                if(data != null){
                    list.add(data);
                    adapter.notifyDataSetChanged();
                    if(RecyclerViewStateUtils.isScrolledBottom(recyclerViewRecruit)){
                        int bottomPosition = adapter.getItemCount();
                        if (bottomPosition > 0) {
                            recyclerViewRecruit.scrollToPosition(bottomPosition);
                        }
                    }
                }
            }
        });

        /**
         * 私聊监听
         */
        sendChatSingleListener = GangPosterReceiver.addReceiverListener(this, XLChatSingleEvent.class, new OnGangReceiverListener<XLMessageBody>() {

            @Override
            public void onReceived(XLMessageBody data) {
                if(data.getChanneltype() != null) {
                    if (XLMessageBean.ChannelType.RECRUIT.value() == data.getChanneltype().intValue()) {
                        isChatSingle = true;
                        toUserId = data.getUserid();
                        hintString = "@" + data.getNickname() + " ";
                        hintLength = hintString.length();
                        editContent.setText(hintString);
                        editContent.setSelection(hintString.length());
                        Handler mainHandler = new Handler(Looper.getMainLooper());
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                XLKeyBoardUtil.showKeyBoard(aContext, editContent);
                            }
                        });
                    }
                }
            }
        });

        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessager();
            }
        });


        editContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if(isChatSingle){
                    if(charSequence.length() >= hintLength){
                        isChatSingle = true;
                    }else {
                        isChatSingle = false;
                        editContent.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imageSwitchVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editContent.getVisibility() == View.VISIBLE){
                    imageSwitchVoice.setImageResource(R.mipmap.qm_btn_gangchat_keyboard);
                    editContent.setVisibility(View.GONE);
                    btnCommit.setVisibility(View.INVISIBLE);
                    btnRecruitAudioRecord.setVisibility(View.VISIBLE);
                }else {
                    imageSwitchVoice.setImageResource(R.mipmap.qm_btn_gangchat_voice);
                    editContent.setVisibility(View.VISIBLE);
                    btnRecruitAudioRecord.setVisibility(View.GONE);
                    btnCommit.setVisibility(View.VISIBLE);
                }
            }
        });

        btnRecruitAudioRecord.setAudioFinishRecorderListener(new XLAudioRecordButton.AudioFinishRecorderListener() {

            @Override
            public void onStart() {
                btnRecruitAudioRecord.getParent().requestDisallowInterceptTouchEvent(true);
                try {
                    mp3Recorder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancel() {
                btnRecruitAudioRecord.getParent().requestDisallowInterceptTouchEvent(false);
                mp3Recorder.stop();
            }

            @Override
            public void onFinished(float seconds) {
                btnRecruitAudioRecord.getParent().requestDisallowInterceptTouchEvent(false);
                mp3Recorder.stop();
                sendVoiceMessager(seconds);
            }
        });

        btnRecruitAudioRecord.setOnVolumeInterface(new XLAudioRecordButton.VolumeInterface() {
            @Override
            public int getVolumeLevel() {
                int volume = mp3Recorder.getVolume();
                int maxVolume = mp3Recorder.getMaxVolume();
                if (volume == 0 || maxVolume == 0) return 0;
                if(volume < 250)  return 1;
                else if (volume >= 250 && volume < 500) return 2;
                else if (volume >= 500 && volume < 750) return 3;
                else if (volume >= 750 && volume < 1000) return 4;
                else if (volume >= 1000 && volume < 1250) return 5;
                else if (volume >= 1250 && volume < 1500) return 6;
                else if (volume >= 1500 && volume < 1750) return 7;
                else return 8;
            }
        });

        ptrFrameLayout.setLastUpdateTimeRelateObject(this);
        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getData(endTime, pagesize);
            }
        });

        ptrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(ListUtils.isEmpty(list)){
                    ptrFrameLayout.autoRefresh(true);
                }
            }
        }, 150);
    }

    /**
     * 发送消息
     */
    private void sendMessager() {
        String content = editContent.getText().toString().trim();
        if(StringUtils.isEmpty(content)){
            XLToastUtil.showToastShort(aContext.getResources().getString(R.string.message_gang_recruit_edit_null));
            return;
        }
        if(hintString != null) {
            content = content.replace(hintString, "");
        }
        if(StringUtils.getChineseLength(content) > 100){
            XLToastUtil.showToastShort("内容不能超过五十个汉字");
            return;
        }
        if (sendSuccessFlag) {
            sendSuccessFlag = false;
            if(isChatSingle){
                sendChatSingleMessager(content, toUserId);
            }else {
                sendChatRecruitMessager(content);
            }
        }else {
            if(TimeUtils.isFastDoubleClick()){
                sendSuccessFlag = false;
            }else {
                sendSuccessFlag = true;
            }
        }
    }

    /**
     * 发送招募频道消息
     * @param content       消息内容
     */
    private void sendChatRecruitMessager(String content) {
        if(StringUtils.isEmpty(content)){
            return;
        }
        GangSDK.getInstance().chatManager().sendTextMessage(content, XLMessageBean.ChannelType.RECRUIT.value(), new DataCallBack() {

            @Override
            public void onSuccess(int status, String message, Object data) {
                sendSuccessFlag = true;
                editContent.setText("");
            }

            @Override
            public void onFail(String message) {
                sendSuccessFlag = true;
                XLToastUtil.showToastShort(message);
            }
        });
    }

    /**
     * 发送私聊消息
     * @param content       消息内容
     * @param toUserId      私聊玩家ID
     */
    private void sendChatSingleMessager(String content, Integer toUserId) {
        if(StringUtils.isEmpty(content)){
            return;
        }
        if(toUserId == null){
            return;
        }
        GangSDK.getInstance().chatManager().sendSingleChatTextMessage(content, toUserId.intValue(), new DataCallBack<XLMessageBody>() {
            @Override
            public void onSuccess(int status, String message, XLMessageBody data) {
                sendSuccessFlag = true;
                editContent.setText("");
            }

            @Override
            public void onFail(String message) {
                sendSuccessFlag = true;
                XLToastUtil.showToastShort(message);
            }
        });
    }

    /**
     * 发送语音消息
     * @param seconds     消息时长
     */
    private void sendVoiceMessager(float seconds) {
        String voiceFilePath = voiceRecorderManage.getVoicePath();
        if(StringUtils.isEmpty(voiceFilePath)){
            return;
        }
        GangSDK.getInstance().chatManager().sendVoiceMessage(voiceFilePath, XLMessageBean.ChannelType.RECRUIT.value(), (int) Math.ceil(seconds), new DataCallBack() {

            @Override
            public void onSuccess(int status, String message, Object data) {
            }

            @Override
            public void onFail(String message) {
                XLToastUtil.showToastShort(message);
            }
        });
    }

    private void bindRecyclerView() {
        recyclerViewRecruit.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(aContext);
        linearLayoutManager.setStackFromEnd(true);
        recyclerViewRecruit.setLayoutManager(linearLayoutManager);
        adapter = new RecruitChatAdapter(aContext, list);
        HeaderAndFooterRecyclerViewAdapter headerAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        recyclerViewRecruit.setAdapter(headerAndFooterRecyclerViewAdapter);

        recyclerViewRecruit.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.top = DensityUtil.dip2px(aContext, DensityUtil.dip2px(aContext, 5));
            }

        });

        Space footspace = new Space(aContext);
        footspace.setLayoutParams(new LinearLayout.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(aContext, 20)));
        RecyclerViewUtils.setFooterView(recyclerViewRecruit, footspace);
    }

    /**
     * 获取聊天历史记录
     * @param time
     * @param size
     */
    private void getData(final String time, int size) {
        Integer consortiaid = GangSDK.getInstance().userManager().getXlUserBean().getConsortiaid();
        GangSDK.getInstance().chatManager().getChatHistory(consortiaid, XLMessageBean.ChannelType.RECRUIT.value(), size, time, new DataCallBack<List<XLMessageBody>>() {
            @Override
            public void onSuccess(int status, String message, List<XLMessageBody> data) {
                ptrFrameLayout.refreshComplete();
                if (StringUtils.isEmpty(time)) {
                    list.clear();
                    GangSDK.getInstance().chatManager().clearMessagesRecruitCache();
                }

                if (data != null && !data.isEmpty()) {
                    endTime = String.valueOf(data.get(0).getCreatetime());
                } else {
                    return;
                }

                if (list.size() == 0) {
                    list.addAll(0, data);
                    adapter.notifyDataSetChanged();
                    int bottomPosition = adapter.getItemCount();
                    if (bottomPosition > 0) {
                        recyclerViewRecruit.scrollToPosition(bottomPosition);
                    }
                } else {
                    list.addAll(0, data);
                    adapter.notifyDataSetChanged();
                }
                GangSDK.getInstance().chatManager().addAllMessagesRecruitToCache(0, data);
            }

            @Override
            public void onFail(String message) {
                ptrFrameLayout.refreshComplete();
                XLToastUtil.showToastShort(message);
            }
        });
    }
}
