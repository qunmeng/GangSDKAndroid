package com.qm.gangsdk.ui.view.gangin.manage;

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
 * Created by lijiyuan on 2017/8/18.
 * 编辑宣言弹窗
 */

public class DialogEditDeclarationFragment extends XLBaseDialogFragment {

    private EditText editGangContent;
    private TextView textClose;
    private TextView tvTitle;
    private Button btnGangPublish;
    private Button btnCancel;

    @Override
    protected int getContentView() {
        return R.layout.dialog_fragment_gang_edit_declaration;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        editGangContent = (EditText) view.findViewById(R.id.editGangContent);
        textClose = (TextView) view.findViewById(R.id.textClose);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        btnGangPublish = (Button) view.findViewById(R.id.btnGangPublish);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        tvTitle.setText("编辑宣言");

        textClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });

        btnGangPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publishContent();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
    }

    /**
     * 发布宣言
     */
    private void publishContent() {
        final String content = editGangContent.getText().toString().trim();
        if(StringUtils.isEmpty(content)){
            XLToastUtil.showToastShort(aContext.getResources().getString(R.string.message_gang_recruit_edit_null));
            return;
        }

        if(StringUtils.getChineseLength(content) > 60){
            XLToastUtil.showToastShort(GangConfigureUtils.getGangName() + "宣言不能超过三十个汉字");
        }else {
            loading.show();
            GangSDK.getInstance().groupManager().updateGangDeclaration(content, new DataCallBack() {

                @Override
                public void onSuccess(int status, String message, Object data) {
                    loading.dismiss();
                    close();
                    if (mCallBack != null) {
                        mCallBack.publishSuccess();
                    }
                }

                @Override
                public void onFail(String message) {
                    loading.dismiss();
                    XLToastUtil.showToastShort(message);
                }
            });
        }

    }

    public PublishCallBack mCallBack;

    public interface PublishCallBack{
        void publishSuccess();
    }

    public DialogEditDeclarationFragment setCallback(PublishCallBack callback){
        this.mCallBack = callback;
        return this;
    }
}
