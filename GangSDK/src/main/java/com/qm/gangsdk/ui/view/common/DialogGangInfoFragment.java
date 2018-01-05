package com.qm.gangsdk.ui.view.common;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.callback.DataCallBack;
import com.qm.gangsdk.core.outer.common.entity.XLGangInfoBean;
import com.qm.gangsdk.core.outer.common.utils.logger.Logger;
import com.qm.gangsdk.ui.GangSDK;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseDialogFragment;
import com.qm.gangsdk.ui.custom.dialog.ViewTools;
import com.qm.gangsdk.ui.utils.ImageLoadUtil;
import com.qm.gangsdk.ui.utils.XLActivityManager;
import com.qm.gangsdk.ui.utils.XLToastUtil;

/**
 * Created by lijiyuan on 2017/8/7.
 * 查看社群信息弹框
 */

public class DialogGangInfoFragment extends XLBaseDialogFragment {

    private int gangid = -1;
    private TextView textClose;
    private TextView textGangName;
    private TextView textGangId;
    private TextView textGangInfo;
    private ImageView imageGangIcon;
    private Button btnApply;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gangid > 0) {
                    if (GangSDK.getInstance().userManager().getXlUserBean().getConsortiaid() != null){
                        if(GangSDK.getInstance().userManager().getXlUserBean().getConsortiaid().intValue() > 0) {
                            close();
                        }
                    } else {
                        final Dialog loading = ViewTools.createLoadingDialog(aContext, "正在提交数据...", false);
                        loading.show();
                        GangSDK.getInstance().userManager().applyJoinGang(gangid, "", new DataCallBack<XLGangInfoBean>() {

                            @Override
                            public void onSuccess(int status, String message, XLGangInfoBean data) {
                                loading.dismiss();
                                if (data != null) {
                                    close();
                                    XLActivityManager.getInstance().finishAllActivity();
                                    GangModuleManage.toInGangTabActivity(aContext);
                                } else {
                                    XLToastUtil.showToastShort(message);
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
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_fragment_gang_info;
    }

    @Override
    protected void initData() {
        Logger.d("gangid = " + gangid);
        GangSDK.getInstance().groupManager().getGangInfo(gangid, new DataCallBack<XLGangInfoBean>() {

            @Override
            public void onSuccess(int status, String message, XLGangInfoBean data) {
                updateViewData(data);
            }

            @Override
            public void onFail(String message) {
                XLToastUtil.showToastShort(message);
            }

        });
    }

    /**
     * 更新界面数据
     * @param data
     */
    private void updateViewData(XLGangInfoBean data) {
        if(data == null){
            return;
        }
        ImageLoadUtil.loadRoundImage(imageGangIcon, data.getIconurl());
        textGangName.setText(GangConfigureUtils.getGangName() + ": " + data.getConsortianame());
        textGangId.setText(aContext.getResources().getString(R.string.gang_id_text) + ": " + data.getConsortiaid());
        textGangInfo.setText(aContext.getResources().getString(R.string.gang_leader_text) + ": " + data.getChairman() + "\n" +
                aContext.getResources().getString(R.string.gang_build_text) + ": " + data.getBuildnum() + "\n" +
                aContext.getResources().getString(R.string.gang_level_text) + ": " + data.getBuildlevel() + "级\n" +
                aContext.getResources().getString(R.string.gang_member_text) + ": " + data.getNownum() + "/" + data.getMaxnum() + "\n" +
                aContext.getResources().getString(R.string.gang_active_text) + ": " + data.getActivelevel() + "\n" +
                aContext.getResources().getString(R.string.gang_caifu_text) + ": " + data.getMoneynum() + "\n" +
                aContext.getResources().getString(R.string. gang_declaration_text) + ": " + "\n" + data.getDeclaration());
    }

    @Override
    protected void initView(View view) {
        textClose = (TextView) view.findViewById(R.id.textClose);
        textGangName = (TextView) view.findViewById(R.id.textGangName);
        textGangId = (TextView) view.findViewById(R.id.textGangId);
        textGangInfo = (TextView) view.findViewById(R.id.textGangInfo);
        imageGangIcon = (ImageView) view.findViewById(R.id.imageGangIcon);
        btnApply = (Button) view.findViewById(R.id.btnApply);

        if(gangid > 0) {
            if (GangSDK.getInstance().userManager().getXlUserBean().getConsortiaid() != null){
                if(GangSDK.getInstance().userManager().getXlUserBean().getConsortiaid().intValue() > 0) {
                    btnApply.setText("关闭");
                }
            } else {
                btnApply.setText("申请加入");
            }
        }
    }


    /**
     * 设置社群id
     * @param gangid
     * @return
     */
    public DialogGangInfoFragment setGangId(int gangid){
        this.gangid = gangid;
        return this;
    }
}
