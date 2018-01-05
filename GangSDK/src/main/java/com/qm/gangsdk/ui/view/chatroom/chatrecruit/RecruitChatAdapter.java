package com.qm.gangsdk.ui.view.chatroom.chatrecruit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.qm.gangsdk.core.outer.common.entity.XLMessageBody;
import com.qm.gangsdk.ui.view.chatroom.common.ChatAdapterHelper;

import java.util.List;

/**
 * Created by lijiyuan on 2017/8/7.
 * 招募频道聊天适配器
 */

public class RecruitChatAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<XLMessageBody> list;
    private ChatAdapterHelper chatAdapterHelper;

    public RecruitChatAdapter(Context context, List list) {
        this.mContext = context;
        this.list = list;
        chatAdapterHelper = new ChatAdapterHelper(context, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return chatAdapterHelper.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        chatAdapterHelper.onBindViewHolder(holder, position, getItemViewType(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
       return chatAdapterHelper.getItemViewType(position);
    }

}


