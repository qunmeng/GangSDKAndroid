package com.qm.gangsdk.ui.view.chatroom.chatsingle;

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
import com.qm.gangsdk.core.outer.common.utils.logger.Logger;
import com.qm.gangsdk.core.outer.receiver.GangPosterReceiver;
import com.qm.gangsdk.core.outer.receiver.base.OnGangReceiverListener;
import com.qm.gangsdk.core.outer.receiver.listener.GangReceiverListener;
import com.qm.gangsdk.ui.GangSDK;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseFragment;
import com.qm.gangsdk.ui.custom.headerfooter.HeaderAndFooterRecyclerViewAdapter;
import com.qm.gangsdk.ui.custom.headerfooter.RecyclerViewUtils;
import com.qm.gangsdk.ui.custom.loadingfooter.RecyclerViewStateUtils;
import com.qm.gangsdk.ui.event.XLChatSingleEvent;
import com.qm.gangsdk.ui.utils.DensityUtil;
import com.qm.gangsdk.ui.utils.TimeUtils;
import com.qm.gangsdk.ui.utils.XLKeyBoardUtil;
import com.qm.gangsdk.ui.utils.XLToastUtil;
import com.xl.views.ptr.PtrClassicFrameLayout;
import com.xl.views.ptr.PtrDefaultHandler;
import com.xl.views.ptr.PtrFrameLayout;
import com.xl.views.ptr.PtrHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijiyuan on 2017/8/4.
 * 私聊频道
 */

public class GangChatSingleFragment extends XLBaseFragment {

    private final static int pagesize = 10;
    private String endTime = null;

    private RecyclerView recyclerViewChatSingle;
    private EditText editContent;
    private Button btnCommit;
    private ImageView imageSwitchVoice;

    private ChatSingleAdapter adapter;
    private List<XLMessageBody> list = new ArrayList<>();
    private PtrClassicFrameLayout ptrFrameLayout;

    private GangReceiverListener receiverChatSingleListener = null;  //接收私聊消息监听
    private GangReceiverListener sendChatSingleListener = null;         //发送私聊消息监听
    private boolean sendSuccessFlag = true;    //发送消息是否成功
    private int hintLength = 0;         //私聊提示字符串长度
    private String hintString = null;   //私聊提示字符串
    private boolean isChatSingle = false;   //是否是私聊
    private Integer toUserId = null;        //私聊玩家id

    @Override
    protected int getContentView() {
        return R.layout.fragment_gang_chat_single;
    }

    @Override
    protected void initData() {
        List messagesChatSigle = GangSDK.getInstance().chatManager().getMessagesChatSingleCache();
        if(messagesChatSigle != null || !messagesChatSigle.isEmpty()){
            list.clear();
            list.addAll(messagesChatSigle);
            adapter.notifyDataSetChanged();
            int bottomPosition = adapter.getItemCount();
            if (bottomPosition > 0) {
                recyclerViewChatSingle.scrollToPosition(bottomPosition);
            }
        }

        if(list.size() > 0){
            endTime =  String.valueOf(list.get(0).getCreatetime());
        }
    }

    @Override
    protected void initView(View view) {
        recyclerViewChatSingle = (RecyclerView) view.findViewById(R.id.recyclerViewChatSingle);
        ptrFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptrFrameLayout);
        editContent = (EditText) view.findViewById(R.id.editContent);
        btnCommit = (Button) view.findViewById(R.id.btnCommit);
        imageSwitchVoice = (ImageView) view.findViewById(R.id.imageSwitchVoice);

        bindRecyclerView();
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        receiverChatSingleListener = GangSDK.getInstance().receiverManager().addReceiveChatSingleMessagesListener(this, new OnGangReceiverListener<XLMessageBody>() {
            @Override
            public void onReceived(XLMessageBody messageBody) {
                Logger.d("message chat single = " + messageBody);
                if(messageBody != null){
                    list.add(messageBody);
                    adapter.notifyDataSetChanged();
                    if(RecyclerViewStateUtils.isScrolledBottom(recyclerViewChatSingle)){
                        int bottomPosition = adapter.getItemCount();
                        if (bottomPosition > 0) {
                            recyclerViewChatSingle.scrollToPosition(bottomPosition);
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
                    if (XLMessageBean.ChannelType.CHATSINGLE.value() == data.getChanneltype().intValue()) {
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
                String content = editContent.getText().toString().trim();
                if(StringUtils.isEmpty(content)){
                    XLToastUtil.showToastShort(aContext.getResources().getString(R.string.message_gang_recruit_edit_null));
                    return;
                }
                if(hintString == null) {
                    XLToastUtil.showToastShort("请选择私聊玩家");
                    return;
                }else {
                    content = content.replace(hintString, "");
                }
                if(StringUtils.getChineseLength(content) > 100){
                    XLToastUtil.showToastShort("内容不能超过五十个汉字");
                    return;
                }
                if (sendSuccessFlag) {
                    sendSuccessFlag = false;
                    sendChatSingleMessager(content, toUserId);
                }else {
                    if(TimeUtils.isFastDoubleClick()){
                        sendSuccessFlag = false;
                    }else {
                        sendSuccessFlag = true;
                    }
                }
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
                XLToastUtil.showToastShort("私聊频道不能发送语音");
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
     * 发送私聊消息
     * @param content
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
                if(!StringUtils.isEmpty(hintString)){
                    editContent.setText(hintString);
                    editContent.setSelection(hintString.length());
                }
                list.add(data);
                adapter.notifyDataSetChanged();
                int bottomPosition = adapter.getItemCount();
                if (bottomPosition > 0) {
                    recyclerViewChatSingle.scrollToPosition(bottomPosition);
                }
            }

            @Override
            public void onFail(String message) {
                sendSuccessFlag = true;
                XLToastUtil.showToastShort(message);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GangSDK.getInstance().receiverManager().removeReceiverListener(receiverChatSingleListener);
        GangSDK.getInstance().receiverManager().removeReceiverListener(sendChatSingleListener);
    }

    public void updateViewData(){
        initData();
    }

    private void bindRecyclerView() {
        recyclerViewChatSingle.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(aContext);
        linearLayoutManager.setStackFromEnd(true);
        recyclerViewChatSingle.setLayoutManager(linearLayoutManager);
        adapter = new ChatSingleAdapter(aContext, list);
        HeaderAndFooterRecyclerViewAdapter headerAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        recyclerViewChatSingle.setAdapter(headerAndFooterRecyclerViewAdapter);

        recyclerViewChatSingle.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.top = DensityUtil.dip2px(aContext, DensityUtil.dip2px(aContext, 5));
            }

        });

        Space footspace = new Space(aContext);
        footspace.setLayoutParams(new LinearLayout.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(aContext, 20)));
        RecyclerViewUtils.setFooterView(recyclerViewChatSingle, footspace);
    }

    /**
     * 获取聊天历史记录
     * @param time
     * @param size
     */
    private void getData(final String time, int size) {
        Integer consortiaid = GangSDK.getInstance().userManager().getXlUserBean().getConsortiaid();
        GangSDK.getInstance().chatManager().getChatHistory(consortiaid, XLMessageBean.ChannelType.CHATSINGLE.value(), size, time, new DataCallBack<List<XLMessageBody>>() {
            @Override
            public void onSuccess(int status, String message, List<XLMessageBody> data) {
                ptrFrameLayout.refreshComplete();
                if (StringUtils.isEmpty(time)) {
                    list.clear();
                    GangSDK.getInstance().chatManager().clearMessagesChatSingleCache();
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
                        recyclerViewChatSingle.scrollToPosition(bottomPosition);
                    }
                } else {
                    list.addAll(0, data);
                    adapter.notifyDataSetChanged();
                }
                GangSDK.getInstance().chatManager().addAllMessagesChatSingleToCache(0, data);
            }

            @Override
            public void onFail(String message) {
                ptrFrameLayout.refreshComplete();
                XLToastUtil.showToastShort(message);
            }
        });
    }
}
