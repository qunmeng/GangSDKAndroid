package com.qm.gangsdk.ui.view.gangin.members;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.utils.StringUtils;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseDialogFragment;

/**
 * Created by lijiyuan on 2017/11/23.
 */

public class DialogMemberinfoHintFragment extends XLBaseDialogFragment{

    private CallbackOnclick onclickCallback;

    private CharSequence message = "";
    private Button btnConfirm;
    private Button btnCancel;
    private TextView textMessage;
    private TextView textClose;
    @Override
    protected int getContentView() {
        return R.layout.dialog_memberinfo_hint;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        textMessage = (TextView) view.findViewById(R.id.textMessage);
        textClose = (TextView) view.findViewById(R.id.textClose);

        if(!StringUtils.isEmpty(message)){
            textMessage.setText(message);
        }

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
                if(onclickCallback != null){
                    onclickCallback.cancel();
                }
            }
        });

        textClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onclickCallback != null){
                    onclickCallback.cancel();
                }
                close();
            }
        });
    }


    /**
     * 设置提示信息
     * @param message
     * @return
     */
    public DialogMemberinfoHintFragment setMessage(CharSequence message){
        this.message = message;
        return this;
    }

    /**
     * 设置回调
     * @param callBack
     * @return
     */
    public DialogMemberinfoHintFragment setOnclickCallBack(CallbackOnclick callBack){
        this.onclickCallback = callBack;
        return this;
    }

    /**
     * 回调接口
     */
    public interface CallbackOnclick{
        void confirm();
        void cancel();
    }
}
