package com.qm.gangsdk.ui.view.gangin.members;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.callback.DataCallBack;
import com.qm.gangsdk.core.outer.common.utils.StringUtils;
import com.qm.gangsdk.ui.utils.XLToastUtil;
import com.qm.gangsdk.ui.view.gangin.GangInManager;
import com.qm.gangsdk.ui.GangSDK;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseDialogFragment;
import com.qm.gangsdk.ui.view.common.GangConfigureUtils;

/**
 * 作者：shuzhou on 2017/8/18.
 * 邮箱：810421209@qq.com
 * 版本：v1.0
 */
public class DialogQuitGangFragment extends XLBaseDialogFragment {

    private TextView tvContent;
    private TextView textClose;
    private TextView textHint;
    private Button btnCancel;
    private Button btnConfirm;

    @Override
    protected int getContentView() {
        return R.layout.dialog_fragment_quit_gang;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        tvContent = (TextView) view.findViewById(R.id.tvContent);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
        textHint = (TextView) view.findViewById(R.id.textHint);
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
        tvContent.setText("确认退出当前" + GangConfigureUtils.getGangName() + "?");
        if(!StringUtils.isEmpty(GangSDK.getInstance().groupManager().getGameConfigEntity().getGameprompt().getLeft_consortia())) {
            String message = GangSDK.getInstance().groupManager().getGameConfigEntity().getGameprompt().getLeft_consortia();
            message = message.replaceAll("\\{\\$gangname\\$\\}", GangConfigureUtils.getGangName());
            textHint.setText(message);
        }
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
                GangSDK.getInstance().userManager().quitGang(new DataCallBack() {

                    @Override
                    public void onSuccess(int status, String message, Object data) {
                        GangInManager.quitGangToNoGuidActivity(aContext);
                    }

                    @Override
                    public void onFail(String message) {
                        XLToastUtil.showToastShort(message);
                    }
                });
            }
        });

    }
}
