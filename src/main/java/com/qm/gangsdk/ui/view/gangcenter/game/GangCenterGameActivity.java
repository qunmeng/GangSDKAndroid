package com.qm.gangsdk.ui.view.gangcenter.game;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseActivity;

/**
 * Created by lijiyuan on 2017/12/5.
 *
 * 游戏中心
 */

public class GangCenterGameActivity extends XLBaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_gangcenter_game;
    }

    @Override
    protected void initView() {
        GangCenterGameFragment gangGameFragment = new GangCenterGameFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentParent, gangGameFragment)
                .show(gangGameFragment)
                .commitAllowingStateLoss();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
}
