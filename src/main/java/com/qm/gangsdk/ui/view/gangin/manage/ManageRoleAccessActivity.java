package com.qm.gangsdk.ui.view.gangin.manage;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseActivity;

/**
 * Created by lijiyuan on 2017/9/12.
 * 职称管理页
 */

public class ManageRoleAccessActivity extends XLBaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_manage_roleaccess;
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
        ManageRoleAccessFragment fragment = new ManageRoleAccessFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContent, fragment)
                .show(fragment)
                .commitAllowingStateLoss();
    }
}
