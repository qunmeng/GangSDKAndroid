package com.qm.gangsdk.ui.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.qm.gangsdk.ui.GangSDK;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.custom.dialog.ViewTools;
import com.qm.gangsdk.ui.utils.XLActivityManager;

/**
 * Created by shuzhou on 2017/8/3.
 * Activity基类
 */

public abstract class XLBaseActivity extends AppCompatActivity {

    public Context aContext;
    public Activity aActivity;
    public XLActivityManager acitivityManager;
    public Dialog loading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aContext = this;
        aActivity = this;
        acitivityManager = XLActivityManager.getInstance();
        acitivityManager.pushOneActivity(aActivity);
        initWindow();
        setContentView(getContentView());
        loading = ViewTools.createLoadingDialog(aContext,"正在加载数据...", true);
        initView();
        initData();
    }


    /**
     * 设置布局文件
     *
     * @return
     */
    protected abstract int getContentView();

    /**
     * 初始化View
     */
    protected abstract void initView();

    /**
     * 初始化数据，
     */
    protected abstract void initData();

    /**
     * 初始化window窗口
     */
    protected void initWindow() {
        //取消状态栏
        if (GangSDK.getInstance().isFullScreen()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setTheme(R.style.XLAppThemeFull);
        } else {
            setTheme(R.style.XLAppThemeCommon);
        }
        if (GangSDK.getInstance().getScreenOrientation() == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
