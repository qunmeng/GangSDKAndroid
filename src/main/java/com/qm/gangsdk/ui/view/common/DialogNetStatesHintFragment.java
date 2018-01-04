package com.qm.gangsdk.ui.view.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseDialogFragment;
import com.qm.gangsdk.ui.utils.XLNetStatesUtil;

/**
 * Created by lijiyuan on 2017/12/15.
 *
 * 网络状态提示框
 */

public class DialogNetStatesHintFragment extends XLBaseDialogFragment {

    private CallbackOnclick onclickCallback;
    private TextView textClose;
    private Button btnConfirm;
    private Button btnCancel;
    private TextView textMessage;

    @Override
    protected int getContentView() {
        return R.layout.dialog_fragment_netstates_hint;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        textClose = (TextView) view.findViewById(R.id.textClose);
        textMessage = (TextView) view.findViewById(R.id.textMessage);
        btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);

        if (XLNetStatesUtil.hasNetWorkConnection(aContext)) {
            if (XLNetStatesUtil.hasWifiConnection(aContext)) {
                textMessage.setText(aContext.getResources().getString(R.string.xlnetstates_type_wifi));
            } else{
                textMessage.setText(aContext.getResources().getString(R.string.xlnetstates_type_mobile));
            }
        }else {
            textMessage.setText(aContext.getResources().getString(R.string.xlnetstates_type_unconnected));
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
                if(onclickCallback != null){
                    onclickCallback.confirm();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 设置回调
     * @param callBack
     * @return
     */
    public DialogNetStatesHintFragment setOnclickCallBack(CallbackOnclick callBack){
        this.onclickCallback = callBack;
        return this;
    }

    /**
     * 回调接口
     */
    public interface CallbackOnclick{
        void confirm();
    }
}
