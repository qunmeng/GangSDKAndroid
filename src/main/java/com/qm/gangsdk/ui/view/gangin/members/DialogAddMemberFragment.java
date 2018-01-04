package com.qm.gangsdk.ui.view.gangin.members;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.callback.DataCallBack;
import com.qm.gangsdk.core.outer.common.utils.StringUtils;
import com.qm.gangsdk.ui.utils.XLToastUtil;
import com.qm.gangsdk.ui.GangSDK;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseDialogFragment;
import com.qm.gangsdk.ui.view.common.GangConfigureUtils;

/**
 * 作者：shuzhou on 2017/8/18.
 * 邮箱：810421209@qq.com
 * 版本：v1.0
 */
public class DialogAddMemberFragment extends XLBaseDialogFragment {

    private EditText etContent;
    private TextView textClose;
    private TextView textAddMemberNote;
    private Button btnCancel;
    private Button btnConfirm;

    @Override
    protected int getContentView() {
        return R.layout.dialog_fragment_member_add;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        etContent = (EditText) view.findViewById(R.id.etContent);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
        textAddMemberNote = (TextView) view.findViewById(R.id.textAddMemberNote);
        textClose = (TextView) view.findViewById(R.id.textClose);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        textClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        if(!StringUtils.isEmpty(GangSDK.getInstance().groupManager().getGameConfigEntity().getGameprompt().getAdd_member())) {
            String message = GangSDK.getInstance().groupManager().getGameConfigEntity().getGameprompt().getAdd_member();
            message = message.replaceAll("\\{\\$gangname\\$\\}", GangConfigureUtils.getGangName());
            textAddMemberNote.setText(message);
        }
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StringUtils.isEmpty(etContent.getText().toString())){
                    XLToastUtil.showToastShort(aContext.getResources().getString(R.string.message_gang_member_add));
                    return;
                }
                loading.show();
                GangSDK.getInstance().membersManager().inviteMember(etContent.getText().toString(), new DataCallBack() {
                    @Override
                    public void onSuccess(int status, String message, Object data) {
                        loading.dismiss();
                        close();
                        XLToastUtil.showToastShort("添加成功");
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
}
