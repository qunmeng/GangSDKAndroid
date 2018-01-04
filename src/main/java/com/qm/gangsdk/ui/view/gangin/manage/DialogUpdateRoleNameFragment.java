package com.qm.gangsdk.ui.view.gangin.manage;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.callback.DataCallBack;
import com.qm.gangsdk.core.outer.common.utils.StringUtils;
import com.qm.gangsdk.ui.GangSDK;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseDialogFragment;
import com.qm.gangsdk.ui.utils.XLToastUtil;

/**
 * Created by lijiyuan on 2017/9/14.
 * 更改职业、权限
 */

public class DialogUpdateRoleNameFragment extends XLBaseDialogFragment {

    private int roleid = -1;

    private EditText etRoleName;
    private Button btnSave;
    private Button btnCancel;
    private TextView textClose;

    @Override
    protected int getContentView() {
        return R.layout.dialog_fragment_update_profession_name;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        etRoleName = (EditText) view.findViewById(R.id.etRoleName);
        btnSave = (Button) view.findViewById(R.id.btnSave);
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

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = etRoleName.getText().toString().trim();
                if(StringUtils.isEmpty(name)){
                    XLToastUtil.showToastShort(aContext.getResources().getString(R.string.message_gang_manage_update_role));
                }else if(StringUtils.getChineseLength(name) > 10){
                    XLToastUtil.showToastShort("职位名称不能超过五个汉字");
                }else {
                    loading.show();
                    GangSDK.getInstance().groupManager().updateGangRoleName(roleid, name,
                            new DataCallBack() {
                                @Override
                                public void onSuccess(int status, String message, Object data) {
                                    loading.dismiss();
                                    if(callbackClick != null){
                                        callbackClick.updateSuccess(name);
                                    }
                                    close();
                                }

                                @Override
                                public void onFail(String message) {
                                    loading.dismiss();
                                    XLToastUtil.showToastShort(message);
                                }
                            });
                }

            }
        });
    }

    public DialogUpdateRoleNameFragment setRoleId(int roleid){
        this.roleid = roleid;
        return this;
    }

    public DialogUpdateRoleNameFragment setCallbackClick(CallbackClick callbackClick){
        this.callbackClick = callbackClick;
        return this;
    }


    private CallbackClick callbackClick;
    public interface CallbackClick{
        void updateSuccess(String roloname);
    }
}
