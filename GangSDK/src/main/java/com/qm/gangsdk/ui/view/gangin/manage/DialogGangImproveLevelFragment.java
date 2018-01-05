package com.qm.gangsdk.ui.view.gangin.manage;

import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.callback.DataCallBack;
import com.qm.gangsdk.core.outer.common.utils.StringUtils;
import com.qm.gangsdk.ui.utils.XLToastUtil;
import com.qm.gangsdk.ui.GangSDK;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseDialogFragment;
import com.qm.gangsdk.ui.custom.style.SpannableStringStyle;
import com.qm.gangsdk.ui.view.common.GangConfigureUtils;

/**
 * Created by lijiyuan on 2017/8/18.
 * 社群升级弹窗
 */

public class DialogGangImproveLevelFragment extends XLBaseDialogFragment {
    private TextView textGangImproveLevelMessage;
    private TextView textNumberImprove;
    private TextView textLevelImprove;
    private TextView textClose;
    private Button btnCancel;
    private Button btnConfirm;

    private String levelMessage = "";
    private String numberMessage = "";

    @Override
    protected int getContentView() {
        return R.layout.dialog_fragment_gang_improve_level;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        textGangImproveLevelMessage = (TextView) view.findViewById(R.id.textGangImproveLevelMessage);
        textNumberImprove = (TextView) view.findViewById(R.id.textNumberImprove);
        textLevelImprove = (TextView) view.findViewById(R.id.textLevelImprove);
        textClose = (TextView) view.findViewById(R.id.textClose);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnConfirm = (Button) view.findViewById(R.id.btnConfirm);

        if(!StringUtils.isEmpty(GangSDK.getInstance().groupManager().getGameConfigEntity().getGameprompt().getConsortia_up_level())) {
            String improveMessage = GangSDK.getInstance().groupManager().getGameConfigEntity().getGameprompt().getConsortia_up_level();
            improveMessage = improveMessage.replaceAll("\\{\\$gangname\\$\\}", GangConfigureUtils.getGangName());
            improveMessage = StringUtils.getString(improveMessage, "");
            levelMessage = StringUtils.getString(levelMessage, "");
            numberMessage = StringUtils.getString(numberMessage, "");

            SpannableStringBuilder msgStyle = SpannableStringStyle.buildStyleFromLastcharToEnd(improveMessage
                    , ContextCompat.getColor(aContext, R.color.xlmanager_dialog_improvelevel_text_gold_color), "：");

            SpannableStringBuilder levelStyle = SpannableStringStyle.buildStyleFromLastcharToEnd(levelMessage
                    , ContextCompat.getColor(aContext, R.color.xlmanager_dialog_improvelevel_text_level_color), "：");

            SpannableStringBuilder numberStyle = SpannableStringStyle.buildStyleFromLastcharToEnd(numberMessage
                    , ContextCompat.getColor(aContext, R.color.xlmanager_dialog_improvelevel_text_number_color), "：");

            textGangImproveLevelMessage.setText(msgStyle);
            textLevelImprove.setText(levelStyle);
            textNumberImprove.setText(numberStyle);
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
                GangSDK.getInstance().groupManager().improveGangLevel(new DataCallBack() {

                    @Override
                    public void onSuccess(int status, String message, Object data) {
                        loading.dismiss();
                        close();
                        XLToastUtil.showToastShort(message);
                        if(mCallBack != null){
                            mCallBack.improveSuccess();
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

    public ImproveLevelCallBack mCallBack;

    public interface ImproveLevelCallBack{
        void improveSuccess();
    }

    public DialogGangImproveLevelFragment setCallback(ImproveLevelCallBack callback){
        this.mCallBack = callback;
        return this;
    }

    public DialogGangImproveLevelFragment setMessage(String levelmessage, String numberMessage){
        this.levelMessage = levelmessage;
        this.numberMessage = numberMessage;
        return this;
    }
}
