package com.qm.gangsdk.ui.view.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.entity.XLGameInfoBean;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseDialogFragment;
import com.qm.gangsdk.ui.utils.XLApkUtils;
import com.qm.gangsdk.ui.utils.XLToastUtil;

/**
 * Created by lijiyuan on 2017/12/8.
 * 下载或启动apk弹窗
 */

public class DialogGameDownloadOrStartFragment extends XLBaseDialogFragment{

    private Button btnDownloadStart;
    private TextView textMessage;
    private TextView textClose;

    private XLGameInfoBean gameInfoBean;

    @Override
    protected int getContentView() {
        return R.layout.dialog_fragment_game_download_start;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        btnDownloadStart = (Button) view.findViewById(R.id.btnDownloadStart);
        textMessage = (TextView) view.findViewById(R.id.textMessage);
        textClose = (TextView) view.findViewById(R.id.textClose);

        if(gameInfoBean != null){
            if(XLApkUtils.checkApkExist(aContext, gameInfoBean.getAndroidpackage())){
                textMessage.setText(aContext.getResources().getString(R.string.xlgame_start));
                btnDownloadStart.setBackgroundResource(R.mipmap.qm_btn_userinfo_start);
                btnDownloadStart.setText("启动");
                btnDownloadStart.setTextColor(ContextCompat.getColor(aContext, R.color.xldialog_download_start_button_start_color));
            }else {
                textMessage.setText(aContext.getResources().getString(R.string.xlgame_download));
                btnDownloadStart.setBackgroundResource(R.mipmap.qm_btn_userinfo_download);
                btnDownloadStart.setText("下载");
                btnDownloadStart.setTextColor(ContextCompat.getColor(aContext, R.color.xldialog_download_start_button_download_color));
            }

        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        textClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });

        btnDownloadStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
                downloadOrStartApp();
            }
        });
    }

    /**
     * 设置app信息
     * @param gameInfo      app信息
     * @return              app信息
     */
    public DialogGameDownloadOrStartFragment setGameInfo(XLGameInfoBean gameInfo){
        this.gameInfoBean = gameInfo;
        return this;
    }

    /**
     * 下载或者启动app
     */
    private void downloadOrStartApp() {
        if(gameInfoBean != null){
            if(XLApkUtils.checkApkExist(aContext, gameInfoBean.getAndroidpackage())){
                XLApkUtils.startApk(aContext, gameInfoBean.getAndroidpackage());
            }else {
                new DialogNetStatesHintFragment()
                        .setOnclickCallBack(new DialogNetStatesHintFragment.CallbackOnclick() {
                            @Override
                            public void confirm() {
                                try {
                                    XLApkUtils.downloadApk(aContext, gameInfoBean.getAndroiddownloadurl());
                                } catch (Exception e) {
                                    XLToastUtil.showToastShort("下载地址格式错误");
                                    e.printStackTrace();
                                }
                            }
                        }).show(aContext.getFragmentManager());
            }
        }
    }


}
