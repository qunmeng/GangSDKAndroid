package com.qm.gangsdk.ui.view.gangout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.receiver.base.OnGangReceiverListener;
import com.qm.gangsdk.core.outer.receiver.listener.GangReceiverListener;
import com.qm.gangsdk.ui.GangSDK;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseActivity;
import com.qm.gangsdk.ui.entity.XLCategoryBean;
import com.qm.gangsdk.ui.utils.XLActivityManager;
import com.qm.gangsdk.ui.utils.XLToastUtil;
import com.qm.gangsdk.ui.view.common.GangConfigureUtils;
import com.qm.gangsdk.ui.view.common.GangModuleManage;
import com.qm.gangsdk.ui.view.common.TabSelectedListener;
import com.qm.gangsdk.ui.view.gangout.apply.GangApplyFragment;
import com.qm.gangsdk.ui.view.gangout.chat.GangOutChatFragment;
import com.qm.gangsdk.ui.view.gangout.list.GangListFragment;
import com.qm.gangsdk.ui.view.gangout.recommend.GangRecommendFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijiyuan on 2017/8/4.
 */

public class OutGangTabActivity extends XLBaseActivity {

    private ImageButton btnMenuLeft;
    private View viewMainCenter;
    private ImageButton btnMessageCenter;
    private ImageButton btnGameCenter;
    private ImageButton btnUserCenter;
    private ImageView imageMessageRedpoint;
    private ImageButton btnCreateGang;
    private TextView tvTitle;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<XLCategoryBean> list = new ArrayList<>();

    private GangReceiverListener receiveNotifyMessageListener;
    private GangReceiverListener receiveGangAgreeListener;
    private GangReceiverListener receiveLoginOnOtherPlatListener;

    @Override
    protected int getContentView() {
        return R.layout.activityxl_out_gang_tab;
    }

    @Override
    protected void initView() {
        btnMenuLeft = (ImageButton) findViewById(R.id.btnMenuLeft);
        viewMainCenter = findViewById(R.id.viewMainCenter);
        btnMessageCenter = (ImageButton) findViewById(R.id.btnMessageCenter);
        btnGameCenter = (ImageButton) findViewById(R.id.btnGameCenter);
        btnUserCenter = (ImageButton) findViewById(R.id.btnUserCenter);
        btnCreateGang = (ImageButton) findViewById(R.id.btnCreateGang);
        imageMessageRedpoint = (ImageView) findViewById(R.id.imageMessageRedpoint);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tvTitle.setText(GangConfigureUtils.getGangName());
        viewMainCenter.setVisibility(View.VISIBLE);

        isHasUnreadMessage();
        isAllowedUseAppRecommend();
        initTabViewPager();
    }

    /**
     * 是否有未读消息
     */
    private void isHasUnreadMessage(){
        if(GangConfigureUtils.isHasUnreadMessage()){
            imageMessageRedpoint.setVisibility(View.VISIBLE);
        }else {
            imageMessageRedpoint.setVisibility(View.GONE);
        }
    }

    /**
     * 是否使用应用推荐模块
     */
    private void isAllowedUseAppRecommend() {
        if(GangConfigureUtils.isAllowedUseAppRecommend()){
            btnGameCenter.setVisibility(View.VISIBLE);
        }else {
            btnGameCenter.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //收到通知消息监听
        receiveNotifyMessageListener = GangSDK.getInstance().receiverManager().addReceiveNotifyMessageListener(this, new OnGangReceiverListener() {
            @Override
            public void onReceived(Object data) {
                imageMessageRedpoint.setVisibility(View.VISIBLE);
            }
        });

        //同意申请监听
        receiveGangAgreeListener = GangSDK.getInstance().receiverManager().addReceiveGangAgreeListener(this, new OnGangReceiverListener() {
            @Override
            public void onReceived(Object data) {
                XLActivityManager.getInstance().finishAllActivity();
                GangModuleManage.toInGangTabActivity(aContext);
            }
        });

        //其他设备登陆
        receiveLoginOnOtherPlatListener = GangSDK.getInstance().receiverManager().addReceiveLoginOnOtherPlatListener(this, new OnGangReceiverListener() {
            @Override
            public void onReceived(Object data) {
                XLToastUtil.showToastShort("您已在其他设备登录");
                XLActivityManager.getInstance().finishAllActivity();
            }
        });

        btnMenuLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                XLActivityManager.getInstance().finishAllActivity();
            }
        });

        btnCreateGang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GangModuleManage.toGangCreateActivity(OutGangTabActivity.this);
            }
        });

        btnGameCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GangModuleManage.toGangCenterGameActivity(aContext);
            }
        });

        btnMessageCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageMessageRedpoint.getVisibility() == View.VISIBLE){
                    imageMessageRedpoint.setVisibility(View.GONE);
                    GangSDK.getInstance().userManager().getXlUserBean().setHasmessage(0);
                }
                GangModuleManage.toGangCenterMessageActivity(aContext);
            }
        });

        btnUserCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GangModuleManage.toGangCenterUserActivity(aContext);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GangSDK.getInstance().receiverManager().removeReceiverListener(receiveNotifyMessageListener);
        GangSDK.getInstance().receiverManager().removeReceiverListener(receiveGangAgreeListener);
        GangSDK.getInstance().receiverManager().removeReceiverListener(receiveLoginOnOtherPlatListener);
    }

    //初始化TabLayout,ViewPager
    private void initTabViewPager() {
        list.clear();
        list.add(new XLCategoryBean(XLCategoryBean.TYPE_SORT, GangConfigureUtils.getGangName() + "排行"));
        list.add(new XLCategoryBean(XLCategoryBean.TYPE_GANGOUT_CHAT, aContext.getResources().getString(R.string.gangout_chat_tab)));
        list.add(new XLCategoryBean(XLCategoryBean.TYPE_RECOMMEND, GangConfigureUtils.getGangName() + "推荐"));
        list.add(new XLCategoryBean(XLCategoryBean.TYPE_APPLY, aContext.getResources().getString(R.string.gang_apply_tab)));
        FragmentOutGangPagerAdapter adapter = new FragmentOutGangPagerAdapter(getSupportFragmentManager(), list);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(0);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            View tabView = adapter.getTabView(i);
            tabLayout.getTabAt(i).setCustomView(tabView);
            if (i == 0) {
                // 设置第一个tab的TextView是被选择的样式
                View customView = tabLayout.getTabAt(i).getCustomView();
                TextView textTabTitle = (TextView) customView.findViewById(R.id.textTabTitle);
                textTabTitle.setTextColor(ContextCompat.getColor(aContext, R.color.xlgang_text_tab_selected_color));
            }
        }

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabSelectedListener(aContext, list.size() - 1));
    }
    /**
     * 创建adapter,用于绑定ViewPager
     */
    public class FragmentOutGangPagerAdapter extends FragmentPagerAdapter {
        //这个是viewpager的填充视图
        private List<XLCategoryBean> list;

        public FragmentOutGangPagerAdapter(FragmentManager fm, List<XLCategoryBean> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        //这个是和tablelayout相关的
        @Override
        public CharSequence getPageTitle(int position) {
            return list.get(position).getName();
        }

        //这个是和ViewPage相关的
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (list.get(position).getType()){
                case XLCategoryBean.TYPE_SORT:
                    fragment = new GangListFragment();
                    break;
                case XLCategoryBean.TYPE_GANGOUT_CHAT:
                    fragment = new GangOutChatFragment();
                    break;
                case XLCategoryBean.TYPE_RECOMMEND:
                    fragment = new GangRecommendFragment();
                    break;
                case XLCategoryBean.TYPE_APPLY:
                    fragment = new GangApplyFragment();
                    break;
            }
            return fragment;
        }

        /**
         * 自定义tabItem
         * @param position
         * @return
         */
        public View getTabView(int position) {
            View tabView = LayoutInflater.from(aContext).inflate(R.layout.common_main_tablayout_tabitem, null);
            TextView textTabTitle = (TextView) tabView.findViewById(R.id.textTabTitle);
            textTabTitle.setText(list.get(position).getName());
            return tabView;
        }
    }
}
