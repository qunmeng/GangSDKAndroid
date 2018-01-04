package com.qm.gangsdk.ui.view.gangin.manage;

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
 * Created by lijiyuan on 2017/8/18.
 * 解散社群提示信息
 */

public class DialogGangDissolvedFragment extends XLBaseDialogFragment {

    private TextView textGangDissolveMessage;
    private TextView textGangName;
    private TextView textClose;
    private Button btnCancel;
    private Button btnConfirm;

    @Override
    protected int getContentView() {
        return R.layout.dialog_fragment_gang_dissolve;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        textGangDissolveMessage = (TextView) view.findViewById(R.id.textGangDissolveMessage);
        textGangName = (TextView) view.findViewById(R.id.textGangName);
        textClose = (TextView) view.findViewById(R.id.textClose);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
        textGangName.setText("确认解散当前" + GangConfigureUtils.getGangName() + "?");
        if(!StringUtils.isEmpty(GangSDK.getInstance().groupManager().getGameConfigEntity().getGameprompt().getDisovle_consortia())) {
            String dissolveMessage = GangSDK.getInstance().groupManager().getGameConfigEntity().getGameprompt().getDisovle_consortia();
            dissolveMessage = dissolveMessage.replaceAll("\\{\\$gangname\\$\\}", GangConfigureUtils.getGangName());
            textGangDissolveMessage.setText(StringUtils.getString(dissolveMessage, ""));
        }
        textClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading.show();
                GangSDK.getInstance().groupManager().dissolveGang(new DataCallBack() {

                    @Override
                    public void onSuccess(int status, String message, Object data) {
                        loading.dismiss();
                        close();
                        GangInManager.quitGangToNoGuidActivity(aContext);
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
