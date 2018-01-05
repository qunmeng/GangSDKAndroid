package com.qm.gangsdk.ui.view.chatroom.chatgang;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.callback.DataCallBack;
import com.qm.gangsdk.core.outer.common.entity.XLGangAccidentTaskBean;
import com.qm.gangsdk.core.outer.common.entity.XLGangTipsBean;
import com.qm.gangsdk.core.outer.common.entity.XLMessageBean;
import com.qm.gangsdk.core.outer.common.entity.XLMessageBody;
import com.qm.gangsdk.core.outer.common.utils.ListUtils;
import com.qm.gangsdk.core.outer.common.utils.StringUtils;
import com.qm.gangsdk.core.outer.common.utils.logger.Logger;
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
import com.qm.gangsdk.ui.custom.style.SpannableStringStyle;
import com.qm.gangsdk.ui.event.XLAccidentTaskSuccessEvent;
import com.qm.gangsdk.ui.event.XLChatSingleEvent;
import com.qm.gangsdk.ui.utils.DensityUtil;
import com.qm.gangsdk.ui.utils.TimeUtils;
import com.qm.gangsdk.ui.utils.XLKeyBoardUtil;
import com.qm.gangsdk.ui.utils.XLToastUtil;
import com.qm.gangsdk.ui.utils.XLVoiceRecorderManage;
import com.qm.gangsdk.ui.view.common.GangConfigureUtils;
import com.qm.gangsdk.ui.view.gangin.chat.DialogMonsterAttackFragment;
import com.qm.gangsdk.ui.view.gangin.chat.DialogWaterTreeFragment;
import com.qm.gangsdk.ui.view.gangin.info.DialogGangDonateFragment;
import com.xl.undercover.mp3recorder.MP3Recorder;
import com.xl.views.ptr.PtrClassicFrameLayout;
import com.xl.views.ptr.PtrDefaultHandler;
import com.xl.views.ptr.PtrFrameLayout;
import com.xl.views.ptr.PtrHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 作者：shuzhou on 2017/8/9.
 * 邮箱：810421209@qq.com
 * 版本：v1.0
 * 社群频道
 */
public class GangMessageFragment extends XLBaseFragment {

    private final static int pagesize = 10;
    private String endTime = null;

    private MP3Recorder mp3Recorder;

    private TextView tvAnnouncement;
    private View viewNotifyAnnouncement;
    private Button btnCommit;
    private Button btnOther;
    private EditText editContent;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewAccidentTask;
    private XLAudioRecordButton btnAudioRecord;
    private ImageView imageSwitchVoice;
    private View viewOtherSetting;
    private View viewPopUnderline;
    private View viewParent;
    private Button btnContribute;
    private Button btnConvene;
    private List<XLMessageBody> dataList = new ArrayList();
    private XLMessageAdapter adapter;

    private List<XLGangAccidentTaskBean> accidentTaskList = new ArrayList<>();
    private AccidentTaskAdapter accidentTaskAdapter;
    private PtrClassicFrameLayout ptrFrameLayout;

    private GangReceiverListener receiveTipsListener = null;
    private GangReceiverListener receiveGangAttackListener = null;
    private GangReceiverListener receiveGangMessageListener = null;
    private GangReceiverListener accidentTaskSuccessListener = null;
    private GangReceiverListener sendChatSingleListener = null;      //发送私聊消息监听

    private boolean sendSuccessFlag = true;    //发送消息是否成功
    private int hintLength = 0;         //私聊提示字符串长度
    private String hintString = null;   //私聊提示字符串
    private boolean isChatSingle = false;   //是否是私聊
    private Integer toUserId = null;        //私聊玩家id

    @Override
    protected int getContentView() {
        return R.layout.fragment_gang_message;
    }

    @Override
    protected void initData() {
        dataList.clear();
        List messagesGang = GangSDK.getInstance().chatManager().getMessagesGangCache();
        if (messagesGang != null && !messagesGang.isEmpty()) {
            dataList.addAll(messagesGang);
            adapter.notifyDataSetChanged();
            int bottomPosition = adapter.getItemCount();
            if (bottomPosition > 0) {
                recyclerView.scrollToPosition(bottomPosition);
            }
        }

        if (dataList.size() > 0) {
            endTime = String.valueOf(dataList.get(0).getCreatetime());
        }

        List<XLGangTipsBean> tipsEntityList = GangSDK.getInstance().groupManager().getTipsEntityList();
        if (tipsEntityList != null && !tipsEntityList.isEmpty()) {
            for (XLGangTipsBean tipentity : tipsEntityList) {
                Logger.e("msg == " + tipentity.getMsg());
                updateNotifyAnnouncement(tipentity);
            }
            tipsEntityList.clear();
        }

        List<XLGangAccidentTaskBean> taskList = GangSDK.getInstance().taskManager().getAccidentTaskList();
        if (taskList != null && !taskList.isEmpty()) {
            for (XLGangAccidentTaskBean accidentTaskBean : taskList) {
                Logger.e("data catch timeout = " + accidentTaskBean.getTimeout());
                updateNotifyAccidentTask(accidentTaskBean);
            }
        }
    }

    @Override
    protected void initView(View view) {
        editContent = (EditText) view.findViewById(R.id.editContent);
        btnCommit = (Button) view.findViewById(R.id.btnCommit);
        btnOther = (Button) view.findViewById(R.id.btnOther);
        btnAudioRecord = (XLAudioRecordButton) view.findViewById(R.id.btnAudioRecord);
        tvAnnouncement = (TextView) view.findViewById(R.id.tvAnnouncement);

        viewNotifyAnnouncement = view.findViewById(R.id.viewNotifyAnnouncement);
        imageSwitchVoice = (ImageView) view.findViewById(R.id.imageSwitchVoice);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerViewAccidentTask = (RecyclerView) view.findViewById(R.id.recyclerViewAccidentTask);
        ptrFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptrFrameLayout);
        btnContribute = (Button) view.findViewById(R.id.btnContribute);
        btnConvene = (Button) view.findViewById(R.id.btnConvene);
        viewOtherSetting = view.findViewById(R.id.viewOtherSetting);
        viewPopUnderline = view.findViewById(R.id.viewPopUnderline);
        viewParent = view.findViewById(R.id.viewParent);

        isAllowedUseCallup();
        initRecyclerView();
        initRecyclerViewAccidentTask();
    }

    /**
     * 是否使用召集令
     */
    private void isAllowedUseCallup() {
        if (GangConfigureUtils.isAllowUseCallup()) {
            btnConvene.setVisibility(View.VISIBLE);
            viewPopUnderline.setVisibility(View.VISIBLE);
        } else {
            btnConvene.setVisibility(View.GONE);
            viewPopUnderline.setVisibility(View.GONE);
        }
    }

    /**
     * 绑定突发任务
     */
    private void initRecyclerViewAccidentTask() {
        recyclerViewAccidentTask.setHasFixedSize(false);
        recyclerViewAccidentTask.setLayoutManager(new LinearLayoutManager(aContext));
        accidentTaskAdapter = new AccidentTaskAdapter();
        recyclerViewAccidentTask.setAdapter(accidentTaskAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mp3Recorder = new XLVoiceRecorderManage().getRecorder();

        //任务完成更新界面
        accidentTaskSuccessListener = GangPosterReceiver.addReceiverListener(this, XLAccidentTaskSuccessEvent.class, new OnGangReceiverListener<String>() {
            @Override
            public void onReceived(String data) {
                if(data == null){
                    return;
                }
                switch (data) {
                    case XLGangAccidentTaskBean.TYPE_MONSTER_ACCIDENT:
                        for (XLGangAccidentTaskBean accidentBean : accidentTaskList) {
                            if (accidentBean.getType() == XLGangAccidentTaskBean.TYPE_MONSTER_ACCIDENT) {
                                accidentTaskList.remove(accidentBean);
                                GangSDK.getInstance().taskManager().getAccidentTaskList().remove(accidentBean);
                                accidentTaskAdapter.notifyDataSetChanged();
                                break;
                            }
                        }
                        break;
                    case XLGangAccidentTaskBean.TYPE_WATRE_TREE:
                        for (XLGangAccidentTaskBean accidentBean : accidentTaskList) {
                            if (accidentBean.getType() == XLGangAccidentTaskBean.TYPE_WATRE_TREE) {
                                accidentTaskList.remove(accidentBean);
                                GangSDK.getInstance().taskManager().getAccidentTaskList().remove(accidentBean);
                                accidentTaskAdapter.notifyDataSetChanged();
                                break;
                            }
                        }
                }
            }
        });

        //公告监听
        receiveTipsListener = GangSDK.getInstance().receiverManager().addReceiveTipsListener(this, new OnGangReceiverListener<XLGangTipsBean>() {
            @Override
            public void onReceived(XLGangTipsBean data) {
                if(data != null){
                    updateNotifyAnnouncement(data);
                }
            }
        });

        //突发任务
        receiveGangAttackListener = GangSDK.getInstance().receiverManager().addReceiveGangAttackListener(this, new OnGangReceiverListener<XLGangAccidentTaskBean>() {
            @Override
            public void onReceived(XLGangAccidentTaskBean data) {
                if(data != null){
                    updateNotifyAccidentTask(data);
                }
            }
        });

        //消息监听
        receiveGangMessageListener = GangSDK.getInstance().receiverManager().addReceiveGangMessageListener(this, new OnGangReceiverListener<XLMessageBody>() {
            @Override
            public void onReceived(XLMessageBody data) {
                if(data != null){
                    Logger.d("-----------社群消息---------------" + data.toString());
                    dataList.add(data);
                    adapter.notifyDataSetChanged();
                    if(RecyclerViewStateUtils.isScrolledBottom(recyclerView)){
                        int bottomPosition = adapter.getItemCount();
                        if (bottomPosition > 0) {
                            recyclerView.scrollToPosition(bottomPosition);
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
                    if (XLMessageBean.ChannelType.GANG.value() == data.getChanneltype().intValue()) {
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

        btnOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewOtherSetting.getVisibility() == View.GONE) {
                    viewOtherSetting.setVisibility(View.VISIBLE);
                }else {
                    viewOtherSetting.setVisibility(View.GONE);
                }
            }
        });

        btnContribute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewOtherSetting.setVisibility(View.GONE);
                new DialogGangDonateFragment().show(aContext.getFragmentManager());
            }
        });

        btnConvene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewOtherSetting.setVisibility(View.GONE);
            }
        });

        viewParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewOtherSetting.setVisibility(View.GONE);
            }
        });

        imageSwitchVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editContent.getVisibility() == View.VISIBLE){
                    imageSwitchVoice.setImageResource(R.mipmap.qm_btn_gangchat_keyboard);
                    editContent.setVisibility(View.GONE);
                    btnAudioRecord.setVisibility(View.VISIBLE);
                    btnCommit.setVisibility(View.GONE);
                }else {
                    imageSwitchVoice.setImageResource(R.mipmap.qm_btn_gangchat_voice);
                    editContent.setVisibility(View.VISIBLE);
                    btnAudioRecord.setVisibility(View.GONE);
                    btnCommit.setVisibility(View.VISIBLE);
                }
            }
        });

        btnAudioRecord.setAudioFinishRecorderListener(new XLAudioRecordButton.AudioFinishRecorderListener() {

            @Override
            public void onStart() {
                btnAudioRecord.getParent().requestDisallowInterceptTouchEvent(true);
                try {
                    mp3Recorder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancel() {
                btnAudioRecord.getParent().requestDisallowInterceptTouchEvent(false);
                mp3Recorder.stop();
            }

            @Override
            public void onFinished(float seconds) {
                btnAudioRecord.getParent().requestDisallowInterceptTouchEvent(false);
                mp3Recorder.stop();
                sendVoiceMessager(seconds);
            }
        });

        btnAudioRecord.setOnVolumeInterface(new XLAudioRecordButton.VolumeInterface() {
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
                if(ListUtils.isEmpty(dataList)){
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
        if (StringUtils.isEmpty(content)) {
            XLToastUtil.showToastShort(aContext.getResources().getString(R.string.message_gang_recruit_edit_null));
            return;
        }
        if(hintString != null) {
            content = content.replace(hintString, "");
        }
        if (StringUtils.getChineseLength(content) > 100) {
            XLToastUtil.showToastShort("内容不能超过五十个汉字");
            return;
        }
        if (sendSuccessFlag) {
            sendSuccessFlag = false;
            if(isChatSingle){
                sendChatSingleMessager(content, toUserId);
            }else {
                sendChatGangMessager(content);
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
     * 发送社群频道消息
     * @param content  消息内容
     */
    private void sendChatGangMessager(String content) {
        if(StringUtils.isEmpty(content)){
            return;
        }
        GangSDK.getInstance().chatManager().sendTextMessage(content, XLMessageBean.ChannelType.GANG.value(), new DataCallBack() {

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
     * 发送私聊频道消息
     * @param content  消息内容
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
     * @param seconds
     */
    private void sendVoiceMessager(float seconds) {
        String voiceFilePath = new XLVoiceRecorderManage().getVoicePath();
        if(StringUtils.isEmpty(voiceFilePath)){
            return;
        }
        GangSDK.getInstance().chatManager().sendVoiceMessage(voiceFilePath, XLMessageBean.ChannelType.GANG.value(), (int) Math.ceil(seconds), new DataCallBack() {

            @Override
            public void onSuccess(int status, String message, Object data) {
                XLToastUtil.showToastShort(message);
            }

            @Override
            public void onFail(String message) {
                XLToastUtil.showToastShort(message);
            }

        });
    }


    /**
     * 更新突发任务
     * @param taskBean
     */
    private void updateNotifyAccidentTask(final XLGangAccidentTaskBean taskBean) {
        recyclerViewAccidentTask.setVisibility(View.VISIBLE);
        accidentTaskList.add(taskBean);
        accidentTaskAdapter.notifyDataSetChanged();
        new CountDownTimer(taskBean.getTimeout() * 1000, 1 * 1000){

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                recyclerViewAccidentTask.setVisibility(View.GONE);
                accidentTaskList.remove(taskBean);
                GangSDK.getInstance().taskManager().getAccidentTaskList().remove(taskBean);
                accidentTaskAdapter.notifyDataSetChanged();
            }
        }.start();
    }

    /**
     * 更新公告
     * @param xlTipsEntityBean
     */
    private void updateNotifyAnnouncement(final XLGangTipsBean xlTipsEntityBean) {
        if (xlTipsEntityBean != null){
            new CountDownTimer(60 * 1000, 1 * 1000){
                @Override
                public void onTick(long millisUntilFinished) {
                    viewNotifyAnnouncement.setVisibility(View.VISIBLE);
                    String content ="公告： "  + StringUtils.getString(xlTipsEntityBean.getMsg(), "");
                    SpannableStringBuilder spannableStringBuilder = SpannableStringStyle.buildStyleFromFirstChar(content,
                            ContextCompat.getColor(aContext, R.color.xlgangchat_tips_text_hint_color), "公告： ",
                            aContext.getResources().getDimension(R.dimen.xl_gang_chat_tips_hint));
                    tvAnnouncement.setText(spannableStringBuilder);
                }

                @Override
                public void onFinish() {
                    viewNotifyAnnouncement.setVisibility(View.GONE);
                }
            }.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GangSDK.getInstance().receiverManager().removeReceiverListener(receiveTipsListener);
        GangSDK.getInstance().receiverManager().removeReceiverListener(receiveGangAttackListener);
        GangSDK.getInstance().receiverManager().removeReceiverListener(receiveGangMessageListener);
        GangSDK.getInstance().receiverManager().removeReceiverListener(accidentTaskSuccessListener);
        GangSDK.getInstance().receiverManager().removeReceiverListener(sendChatSingleListener);
    }


    private void initRecyclerView() {
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(aContext);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new XLMessageAdapter(aContext, this.dataList);
        HeaderAndFooterRecyclerViewAdapter headerAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        recyclerView.setAdapter(headerAndFooterRecyclerViewAdapter);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.top = DensityUtil.dip2px(aContext, DensityUtil.dip2px(aContext, 5));
            }
        });
        Space footspace = new Space(aContext);
        footspace.setLayoutParams(new LinearLayout.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(aContext, 30)));
        RecyclerViewUtils.setFooterView(recyclerView, footspace);
    }

    /**
     * 突发任务适配器
     */
    class AccidentTaskAdapter extends RecyclerView.Adapter<AccidentTaskViewHolder> {

        @Override
        public AccidentTaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new AccidentTaskViewHolder(LayoutInflater.from(aContext).inflate(R.layout.item_recyclerview_accident_task, parent, false));
        }

        @Override
        public void onBindViewHolder(AccidentTaskViewHolder holder, int position) {
            final XLGangAccidentTaskBean accidentTaskBean = accidentTaskList.get(position);
            String content ="突发任务：" + StringUtils.getString(accidentTaskBean.getContent(), "");
            SpannableStringBuilder spannableStringBuilder = SpannableStringStyle.buildStyleFromFirstChar(content,
                    ContextCompat.getColor(aContext, R.color.xlgangchat_task_text_hint_color), "突发任务：",
                    aContext.getResources().getDimension(R.dimen.xl_gang_chat_tips_hint));

            holder.tvTask.setText(spannableStringBuilder);
            switch (accidentTaskBean.getType()) {
                case XLGangAccidentTaskBean.TYPE_MONSTER_ACCIDENT:
                    holder.btnTaskAttack.setBackgroundResource(R.mipmap.qm_btn_gangchat_tip_attack);
                    holder.btnTaskAttack.setText("攻击");
                    break;
                case XLGangAccidentTaskBean.TYPE_WATRE_TREE:
                    holder.btnTaskAttack.setBackgroundResource(R.mipmap.qm_btn_gangchat_tip_water);
                    holder.btnTaskAttack.setText("浇水");
                    break;
            }
            holder.btnTaskAttack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (accidentTaskBean.getType()){
                        case XLGangAccidentTaskBean.TYPE_MONSTER_ACCIDENT:
                            new DialogMonsterAttackFragment()
                                    .setTadkData(accidentTaskBean)
                                    .show(aContext.getFragmentManager());
                            break;
                        case XLGangAccidentTaskBean.TYPE_WATRE_TREE:
                            new DialogWaterTreeFragment()
                                    .setTadkData(accidentTaskBean)
                                    .show(aContext.getFragmentManager());
                            break;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return accidentTaskList.size();
        }
    }

    class AccidentTaskViewHolder extends RecyclerView.ViewHolder{
        private Button btnTaskAttack;
        private TextView tvTask;
        public AccidentTaskViewHolder(View view) {
            super(view);
            tvTask = (TextView) view.findViewById(R.id.tvTask);
            btnTaskAttack = (Button) view.findViewById(R.id.btnTaskAttack);
        }
    }

    /**
     * 获取聊天历史记录
     * @param time
     * @param size
     */
    private void getData(final String time, int size) {
        if (GangSDK.getInstance().userManager().getXlUserBean().getConsortiaid() != null) {
            Integer consortiaid = GangSDK.getInstance().userManager().getXlUserBean().getConsortiaid();
            GangSDK.getInstance().chatManager().getChatHistory(consortiaid, XLMessageBean.ChannelType.GANG.value(), size, time, new DataCallBack<List<XLMessageBody>>() {
                @Override
                public void onSuccess(int status, String message, List<XLMessageBody> data) {
                    ptrFrameLayout.refreshComplete();
                    if (StringUtils.isEmpty(time)) {
                        dataList.clear();
                        GangSDK.getInstance().chatManager().clearMessagesGangCache();
                    }

                    if (data != null && !data.isEmpty()) {
                        endTime = String.valueOf(data.get(0).getCreatetime());
                    } else {
                        return;
                    }

                    if (dataList.size() == 0) {
                        dataList.addAll(0, data);
                        adapter.notifyDataSetChanged();
                        int bottomPosition = adapter.getItemCount();
                        if (bottomPosition > 0) {
                            recyclerView.scrollToPosition(bottomPosition);
                        }
                    } else {
                        dataList.addAll(0, data);
                        adapter.notifyDataSetChanged();
                    }
                    GangSDK.getInstance().chatManager().addAllMessagesGangToCache(0, data);
                }

                @Override
                public void onFail(String message) {
                    ptrFrameLayout.refreshComplete();
                    XLToastUtil.showToastShort(message);
                }
            });
        }
    }
}
