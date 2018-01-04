package com.qm.gangsdk.ui.view.gangin.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.callback.DataCallBack;
import com.qm.gangsdk.core.outer.receiver.GangPosterReceiver;
import com.qm.gangsdk.core.outer.common.utils.StringUtils;
import com.qm.gangsdk.core.outer.common.entity.XLGangAccidentTaskBean;
import com.qm.gangsdk.ui.GangSDK;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseDialogFragment;
import com.qm.gangsdk.ui.event.XLAccidentTaskSuccessEvent;
import com.qm.gangsdk.ui.utils.ImageLoadUtil;
import com.qm.gangsdk.ui.utils.XLToastUtil;

/**
 * Created by lijiyuan on 2017/10/11.
 * 浇水弹窗
 */

public class DialogWaterTreeFragment extends XLBaseDialogFragment {

    private ImageView imageTreeIcon;
    private TextView textClose;
    private Button btnWaterTree;
    private XLGangAccidentTaskBean accidentTaskBean;

    @Override
    protected int getContentView() {
        return R.layout.dialog_fragment_water_tree_task;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView(View view) {
        imageTreeIcon = (ImageView) view.findViewById(R.id.imageTreeIcon);
        textClose = (TextView) view.findViewById(R.id.textClose);
        btnWaterTree = (Button) view.findViewById(R.id.btnWaterTree);

        textClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(accidentTaskBean == null){
            return;
        }

        if(StringUtils.isEmpty(accidentTaskBean.getTaskiconurl())){
            return;
        }else {
            ImageLoadUtil.loadNormalImage(imageTreeIcon, accidentTaskBean.getTaskiconurl());
        }

        btnWaterTree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.show();
                GangSDK.getInstance().groupManager().dealTask(accidentTaskBean.getTasktype(), accidentTaskBean.getTaskid(), new DataCallBack() {
                    @Override
                    public void onSuccess(int status, String message, Object data) {
                        loading.dismiss();
                        close();
                        GangPosterReceiver.post(new XLAccidentTaskSuccessEvent(XLGangAccidentTaskBean.TYPE_WATRE_TREE));
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

    /**
     * 获取数据
     * @param accidentTaskBean
     * @return
     */
    public DialogWaterTreeFragment setTadkData(XLGangAccidentTaskBean accidentTaskBean){
        this.accidentTaskBean = accidentTaskBean;
        return this;
    }
}
