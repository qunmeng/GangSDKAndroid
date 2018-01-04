package com.qm.gangsdk.ui.view.gangcenter.user;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseActivity;

/**
 * Created by lijiyuan on 2017/12/5.
 *
 * 个人中心
 */

public class GangCenterUserActivity extends XLBaseActivity {
    @Override
    protected int getContentView() {
        return R.layout.activity_gangcenter_user;
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

        GangCenterUserFragment gangUserFragment = new GangCenterUserFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentParent, gangUserFragment)
                .show(gangUserFragment)
                .commitAllowingStateLoss();
    }
}
