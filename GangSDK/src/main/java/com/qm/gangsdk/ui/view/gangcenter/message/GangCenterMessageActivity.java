package com.qm.gangsdk.ui.view.gangcenter.message;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseActivity;

/**
 * 消息中心页面
 */

public class GangCenterMessageActivity extends XLBaseActivity{

    @Override
    protected int getContentView() {
        return R.layout.activity_gangcenter_message;
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
        GangCenterMessageFragment gangMessageFragment = new GangCenterMessageFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentParent, gangMessageFragment)
                .show(gangMessageFragment)
                .commitAllowingStateLoss();
    }
}
