package com.qm.gangsdk.ui.view.gangin.manage;

import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
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
import com.qm.gangsdk.ui.custom.style.SpannableStringStyle;
import com.qm.gangsdk.ui.entity.SpannableStringBean;
import com.qm.gangsdk.ui.view.common.GangConfigureUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijiyuan on 2017/8/18.
 * 修改社群名称弹窗
 */

public class DialogUpdateGangNameFragment extends XLBaseDialogFragment {

    private EditText editGangName;
    private TextView textClose;
    private TextView textUpdateNameNote;
    private Button btnCancel;
    private Button btnConfirm;

    @Override
    protected int getContentView() {
        return R.layout.dialog_fragment_gang_update_name;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        editGangName = (EditText) view.findViewById(R.id.editGangName);
        textClose = (TextView) view.findViewById(R.id.textClose);
        textUpdateNameNote = (TextView) view.findViewById(R.id.textUpdateNameNote);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnConfirm = (Button) view.findViewById(R.id.btnConfirm);

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

        editGangName.setHint("请输入新的" + GangConfigureUtils.getGangName() + "名称");

        if(!StringUtils.isEmpty(GangSDK.getInstance().groupManager().getGameConfigEntity().getGameprompt().getModify_consortia_name())) {
            String message = GangSDK.getInstance().groupManager().getGameConfigEntity().getGameprompt().getModify_consortia_name();
            message = message.replaceAll("\\{\\$gangname\\$\\}", GangConfigureUtils.getGangName());

            //多样风格字符串
            List<SpannableStringBean> list = new ArrayList<SpannableStringBean>();
            list.add(new SpannableStringBean("\\{\\$moneyname\\$\\}", GangConfigureUtils.getMoneyName(), ContextCompat.getColor(aContext, R.color.xlmanager_dialog_edit_gangname_text_gold_color)));
            SpannableStringBuilder spannableStringBuilder = SpannableStringStyle.buildStyleReplaceParts(message, list, ContextCompat.getColor(aContext, R.color.xlmanager_dialog_edit_gangname_text_hint_color));
            textUpdateNameNote.setText(spannableStringBuilder);
        }
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gangName = editGangName.getText().toString().trim();
                if(StringUtils.isEmpty(gangName)){
                    XLToastUtil.showToastShort(GangConfigureUtils.getGangName() + "名称不能为空");
                }else if(StringUtils.getChineseLength(gangName) > 14){
                    XLToastUtil.showToastShort(GangConfigureUtils.getGangName() + "名称不能超过七个汉字");
                }else {
                    loading.show();
                    GangSDK.getInstance().groupManager().updateGangName(gangName, new DataCallBack() {
                        @Override
                        public void onSuccess(int status, String message, Object data) {
                            loading.dismiss();
                            close();
                            if(mCallBack != null){
                                mCallBack.updateSuccess();
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
        });
    }

    public UpdateCallBack mCallBack;

    public interface UpdateCallBack{
        void updateSuccess();
    }

    public DialogUpdateGangNameFragment setCallback(UpdateCallBack callback){
        this.mCallBack = callback;
        return this;
    }
}
