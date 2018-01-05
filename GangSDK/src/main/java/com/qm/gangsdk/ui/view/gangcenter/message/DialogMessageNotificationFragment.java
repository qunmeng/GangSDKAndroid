package com.qm.gangsdk.ui.view.gangcenter.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.entity.XLGangCenterMessageCommonBean;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseDialogFragment;
import com.qm.gangsdk.ui.utils.TimeUtils;

/**
 * 消息通知对话框页面
 */

public class DialogMessageNotificationFragment extends XLBaseDialogFragment {

    private TextView textClose;
    private TextView textViewContent;
    private TextView textViewFrom;
    private TextView textViewDateTime;
    private Button btnIgnore;

    private XLGangCenterMessageCommonBean messageBean;

    @Override
    protected int getContentView() {
        return R.layout.dialog_fragment_gangcenter_message_notification;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        textClose = (TextView) view.findViewById(R.id.textClose);
        textViewContent = (TextView) view.findViewById(R.id.textViewContent);
        textViewFrom = (TextView) view.findViewById(R.id.textViewFrom);
        textViewDateTime = (TextView) view.findViewById(R.id.textViewDateTime);
        btnIgnore = (Button) view.findViewById(R.id.btnIgnore);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        messageBean = (XLGangCenterMessageCommonBean)getArguments().getSerializable("message");

        textViewContent.setText(messageBean.getContent());
        textViewFrom.setText("官方团队");
        textViewDateTime.setText(TimeUtils.getTime(messageBean.getCreatetime(), TimeUtils.DATE_FORMAT_DATE));

        textClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });

        btnIgnore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });
    }
}
