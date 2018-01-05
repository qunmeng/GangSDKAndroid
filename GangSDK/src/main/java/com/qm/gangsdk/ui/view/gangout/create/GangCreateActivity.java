package com.qm.gangsdk.ui.view.gangout.create;

import com.qm.gangsdk.ui.base.XLBaseActivity;

import com.qm.gangsdk.ui.R;

/**
 * Created by lijiyuan on 2017/8/4.
 * 创建社群
 */

public class GangCreateActivity extends XLBaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activityxl_create_gang;
    }

    @Override
    protected void initView() {
        GangCreateFragment gangCreateFragment = new GangCreateFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentXLCreate, gangCreateFragment)
                .show(gangCreateFragment)
                .commitAllowingStateLoss();
    }

    @Override
    protected void initData() {

    }

}
