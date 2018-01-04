package com.qm.gangsdk.ui.view.gangin.members;

import android.os.Bundle;

import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseActivity;

/**
 * Created by lijiyuan on 2017/8/4.
 * 创建社群
 */

public class MemberInfoActivity extends XLBaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activityxl_memberinfo;
    }

    @Override
    protected void initView() {

        MemberInfoFragment fragment = new MemberInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("memberid", getIntent().getIntExtra("memberid", -1));
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContent, fragment)
                .show(fragment)
                .commitAllowingStateLoss();
    }

    @Override
    protected void initData() {

    }

}
