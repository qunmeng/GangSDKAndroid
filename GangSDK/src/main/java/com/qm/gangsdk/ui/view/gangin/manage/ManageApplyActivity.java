package com.qm.gangsdk.ui.view.gangin.manage;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseActivity;

/**
 * Created by lijiyuan on 2017/8/21.
 * 社群成员申请列表
 */

public class ManageApplyActivity extends XLBaseActivity{
    @Override
    protected int getContentView() {
        return R.layout.activityxl_gang_applylist;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ManageApplyFragment fragment = new ManageApplyFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContent, fragment)
                .show(fragment)
                .commitAllowingStateLoss();
    }
}
