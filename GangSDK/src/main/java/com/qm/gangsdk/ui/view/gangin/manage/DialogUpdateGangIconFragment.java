package com.qm.gangsdk.ui.view.gangin.manage;

import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.utils.StringUtils;
import com.qm.gangsdk.ui.GangSDK;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseDialogFragment;
import com.qm.gangsdk.ui.custom.style.SpannableStringStyle;
import com.qm.gangsdk.ui.entity.SpannableStringBean;
import com.qm.gangsdk.ui.view.common.GangConfigureUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijiyuan on 2017/8/18.
 * 解散社群提示信息
 */

public class DialogUpdateGangIconFragment extends XLBaseDialogFragment {

    private TextView textHint;
    private TextView textClose;
    private Button btnCancel;
    private Button btnConfirm;

    @Override
    protected int getContentView() {
        return R.layout.dialog_fragment_update_gangicon;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        textHint = (TextView) view.findViewById(R.id.textHint);
        textClose = (TextView) view.findViewById(R.id.textClose);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnConfirm = (Button) view.findViewById(R.id.btnConfirm);

        if(!StringUtils.isEmpty(GangSDK.getInstance().groupManager().getGameConfigEntity().getGameprompt().getModify_consortia_icon())) {
            String message = GangSDK.getInstance().groupManager().getGameConfigEntity().getGameprompt().getModify_consortia_icon();
            message = message.replaceAll("\\{\\$gangname\\$\\}", GangConfigureUtils.getGangName());

            //多样风格字符串
            List<SpannableStringBean> list = new ArrayList<SpannableStringBean>();
            list.add(new SpannableStringBean("\\{\\$moneyname\\$\\}", GangConfigureUtils.getMoneyName(), ContextCompat.getColor(aContext, R.color.xlmanager_dialog_update_gangicon_text_gold_color)));
            SpannableStringBuilder spannableStringBuilder = SpannableStringStyle.buildStyleReplaceParts(message, list, ContextCompat.getColor(aContext, R.color.xlmanager_dialog_update_gangicon_text_content_color));

            textHint.setText(spannableStringBuilder);
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
                close();
                if(callbackClick != null){
                    callbackClick.confirmClick();
                }
            }
        });
    }

    /**
     * 设置回调
     * @param callBack
     * @return
     */
    public DialogUpdateGangIconFragment setOnclickCallBack(CallbackClick callBack){
        this.callbackClick = callBack;
        return this;
    }

    private CallbackClick callbackClick;
    public interface CallbackClick{
        void confirmClick();
    }
}
