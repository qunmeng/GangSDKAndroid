package com.qm.gangsdk.ui.view.chatroom.chatgang;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.qm.gangsdk.core.outer.common.entity.XLMessageBody;
import com.qm.gangsdk.ui.view.chatroom.common.ChatAdapterHelper;

import java.util.List;

/**
 * 作者：shuzhou on 2017/8/23.
 * 邮箱：810421209@qq.com
 * 版本：v1.0
 */
public class XLMessageAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<XLMessageBody> list;
    private ChatAdapterHelper chatAdapterHelper;

    public XLMessageAdapter(Context context, List list) {
        this.mContext = context;
        this.list = list;
        chatAdapterHelper = new ChatAdapterHelper(context, list);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return chatAdapterHelper.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
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

    private String textUserNameAndRoleName(final XLMessageBody messageBody){
        if(messageBody != null){
            if(messageBody.getRolename() == null){
                return messageBody.getNickname();
            }else{
                return (messageBody.getNickname() + " <" + messageBody.getRolename() + ">");
            }
        }else{
            return "";
        }
    }

}
