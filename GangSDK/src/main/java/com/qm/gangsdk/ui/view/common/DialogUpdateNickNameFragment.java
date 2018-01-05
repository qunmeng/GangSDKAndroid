package com.qm.gangsdk.ui.view.common;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.callback.DataCallBack;
import com.qm.gangsdk.core.outer.common.utils.StringUtils;
import com.qm.gangsdk.ui.utils.XLToastUtil;
import com.qm.gangsdk.ui.GangSDK;;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseDialogFragment;

/**
 * Created by lijiyuan on 2017/10/16.
 * 更改用户昵称
 */

public class DialogUpdateNickNameFragment extends XLBaseDialogFragment {
    private EditText etContent;
    private Button btnConfirm;
    private Button btnCancel;
    private TextView textClose;


    @Override
    protected int getContentView(){
        return R.layout.dialog_fragment_update_user_nickname;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        etContent = (EditText) view.findViewById(R.id.etContent);
        btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        textClose = (TextView) view.findViewById(R.id.textClose);

        textClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = etContent.getText().toString().trim();
                if(StringUtils.isEmpty(nickname)){
                    XLToastUtil.showToastShort(aContext.getResources().getString(R.string.message_gang_update_nickname_null));
                    return;
                }
                if(StringUtils.getChineseLength(nickname) > 14){
                    XLToastUtil.showToastShort(aContext.getResources().getString(R.string.message_gang_update_nickname_size));
                    return;
                }
                loading.show();
                GangSDK.getInstance().userManager().updateNickName(nickname, new DataCallBack() {
                    @Override
                    public void onSuccess(int status, String message, Object data) {
                        loading.dismiss();
                        close();
                        if(callback != null){
                            callback.updateNickNameSuceess();
                        }
                    }

                    @Override
                    public void onFail(String message) {
                        loading.dismiss();
                        XLToastUtil.showToastShort(message);
                    }

                });
            }
        });
    }

    @Override
    protected boolean cancelTouchOutSide() {
        return false;
    }

    private UpdateNickNameSuccessCallback callback;

    public DialogUpdateNickNameFragment setCallback(UpdateNickNameSuccessCallback callback){
        this.callback = callback;
        return this;
    }

    public interface UpdateNickNameSuccessCallback{
        void updateNickNameSuceess();
    }
}
