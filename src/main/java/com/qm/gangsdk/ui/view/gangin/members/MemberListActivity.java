package com.qm.gangsdk.ui.view.gangin.members;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseActivity;
import com.qm.gangsdk.ui.view.gangrank.GangRankFragment;

/**
 * 作者：shuzhou on 2017/8/17.
 * 邮箱：810421209@qq.com
 * 版本：v1.0
 */
public class MemberListActivity extends XLBaseActivity {

    public final static String TYPE = "membertype";
    public final static int ACTIVIE = 1;
    public final static int CONTRUBUTE = 2;
    public final static int SORT = 3;
    public final static int NOTALK = 4;

    @Override
    protected int getContentView() {
        return R.layout.activityxl_memberlist;
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

        int typeData = getIntent().getIntExtra(TYPE, -1);
        switch (typeData) {
            case ACTIVIE:
                Fragment fragmentA = new ActiveListFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContent, fragmentA)
                        .show(fragmentA)
                        .commitAllowingStateLoss();
                break;
            case CONTRUBUTE:
                Fragment fragmentC = new DonateListFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContent, fragmentC)
                        .show(fragmentC)
                        .commitAllowingStateLoss();
                break;
            case SORT:
                Fragment fragmentS = new GangRankFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContent, fragmentS)
                        .show(fragmentS)
                        .commitAllowingStateLoss();
                break;
            case NOTALK:
                Fragment fragmentN = new MuteListFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContent, fragmentN)
                        .show(fragmentN)
                        .commitAllowingStateLoss();
                break;
        }

    }
}
