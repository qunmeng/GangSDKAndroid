package com.qm.gangsdk.ui.view.gangin.info;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.callback.DataCallBack;
import com.qm.gangsdk.core.outer.receiver.GangPosterReceiver;
import com.qm.gangsdk.core.outer.common.utils.StringUtils;
import com.qm.gangsdk.ui.GangSDK;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseDialogFragment;
import com.qm.gangsdk.ui.event.XLDonateSuccessEvent;
import com.qm.gangsdk.ui.view.common.GangConfigureUtils;
import com.qm.gangsdk.ui.utils.XLToastUtil;

/**
 * Created by lijiyuan on 2017/8/18.
 * 捐赠
 */

public class DialogGangDonateFragment extends XLBaseDialogFragment {

    private EditText etContent;
    private TextView textClose;
    private TextView tvHint;
    private Button btnCancel;
    private Button btnConfirm;

    @Override
    protected int getContentView() {
        return R.layout.dialog_fragment_gang_donate;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        etContent = (EditText) view.findViewById(R.id.etContent);
        textClose = (TextView) view.findViewById(R.id.textClose);
        tvHint = (TextView) view.findViewById(R.id.tvHint);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
        if(GangConfigureUtils.getMoneyName() !=  null) {
            etContent.setHint("请输入" + GangConfigureUtils.getMoneyName() + "数量");
        }
        if(!StringUtils.isEmpty(GangSDK.getInstance().groupManager().getGameConfigEntity().getGameprompt().getConsortia_contribute())) {
            String contributeMessage = GangSDK.getInstance().groupManager().getGameConfigEntity().getGameprompt().getConsortia_contribute();
            contributeMessage = contributeMessage.replaceAll("\\{\\$gangname\\$\\}", GangConfigureUtils.getGangName());
            contributeMessage = contributeMessage.replaceAll("\\{\\$moneyname\\$\\}", GangConfigureUtils.getMoneyName());
            tvHint.setText(StringUtils.getString(contributeMessage, ""));
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
                String num = etContent.getText().toString().trim();
                if (StringUtils.isEmpty(num)) {
                    XLToastUtil.showToastShort(aContext.getResources().getString(R.string.message_gang_donate));
                } else {
                    loading.show();
                    GangSDK.getInstance().groupManager().donateToGang(num, new DataCallBack() {
                        @Override
                        public void onSuccess(int status, String message, Object data) {
                            loading.dismiss();
                            close();
                            XLToastUtil.showToastShort(message);
                            GangPosterReceiver.post(new XLDonateSuccessEvent());
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

}
