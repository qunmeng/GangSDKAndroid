package com.qm.gangsdk.ui.view.chatroom.common.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.entity.XLMessageBody;
import com.qm.gangsdk.core.outer.common.utils.StringUtils;
import com.qm.gangsdk.ui.R;

/**
 * Author: mengbo
 * Time: 2017/12/15 18:21
 * Description: SystemViewHolder
 */

public class SystemViewHolder extends RecyclerView.ViewHolder {

    private TextView textMessageSystem;

    public SystemViewHolder(View view) {
        super(view);
        textMessageSystem = (TextView) view.findViewById(R.id.textMessageSystem);
    }

    public static SystemViewHolder newViewHolder(Context context, ViewGroup parent) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_gangchat_system, parent, false);
        return new SystemViewHolder(view);
    }

    public void bindData(final Context context, final XLMessageBody messageBody){
        textMessageSystem.setText("系统消息：" + StringUtils.getString(messageBody.getMessage(), ""));
    }
}
