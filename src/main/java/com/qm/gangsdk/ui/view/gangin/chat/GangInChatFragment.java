package com.qm.gangsdk.ui.view.gangin.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseFragment;
import com.qm.gangsdk.ui.entity.XLCategoryBean;
import com.qm.gangsdk.ui.view.common.GangConfigureUtils;
import com.qm.gangsdk.ui.view.chatroom.chatgang.GangMessageFragment;
import com.qm.gangsdk.ui.view.chatroom.chatrecruit.GangRecruitFragment;
import com.qm.gangsdk.ui.view.chatroom.chatsingle.GangChatSingleFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：shuzhou on 2017/8/9.
 * 邮箱：810421209@qq.com
 * 版本：v1.0
 * 社群聊天
 */
public class GangInChatFragment extends XLBaseFragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentInGangPagerAdapter adapter;
    @Override
    protected int getContentView() {
        return R.layout.fragment_gang_chat;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViewPager();
    }

    private void initViewPager() {
        List<XLCategoryBean> list = new ArrayList<>();
        list.add(new XLCategoryBean(XLCategoryBean.TYPE_GANGMINE, GangConfigureUtils.getGangName() + "频道"));
        list.add(new XLCategoryBean(XLCategoryBean.TYPE_GANGRECRUIT, aContext.getResources().getString(R.string.gang_recruit_tab)));
        list.add(new XLCategoryBean(XLCategoryBean.TYPE_CHATSINGLE, aContext.getResources().getString(R.string.gang_chat_single_tab)));
        adapter = new FragmentInGangPagerAdapter(getChildFragmentManager(), list);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setOffscreenPageLimit(0);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            View tabView = adapter.getTabView(i);
            tabLayout.getTabAt(i).setCustomView(tabView);
            View customView = tabLayout.getTabAt(i).getCustomView();
            TextView textTabTitle = (TextView) customView.findViewById(R.id.textTabTitle);
            if (i == 0) {
                // 设置第一个tab的TextView被选择的样式
                textTabTitle.setTextColor(ContextCompat.getColor(aContext, R.color.xlgangchat_text_tab_selected_color));
                textTabTitle.setBackgroundResource(R.mipmap.qm_btn_gangchat_selected);
            }else {
                textTabTitle.setTextColor(ContextCompat.getColor(aContext, R.color.xlgangchat_text_tab_normal_color));
                textTabTitle.setBackgroundResource(R.mipmap.qm_btn_gangchat_normal);
            }
        }

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(adapter != null) {
                    if (adapter.getFragment(position) instanceof GangChatSingleFragment) {
                        GangChatSingleFragment gangChatSingleFragment = (GangChatSingleFragment) adapter.getFragment(position);
                        gangChatSingleFragment.updateViewData();
                    }
                }
            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() < 0){
                    return;
                }
                viewPager.setCurrentItem(tab.getPosition());
                View customView = tab.getCustomView();
                TextView textTabTitle = (TextView) customView.findViewById(R.id.textTabTitle);
                textTabTitle.setTextColor(ContextCompat.getColor(aContext, R.color.xlgangchat_text_tab_selected_color));
                textTabTitle.setBackgroundResource(R.mipmap.qm_btn_gangchat_selected);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(tab.getPosition() < 0){
                    return;
                }
                View customView = tab.getCustomView();
                TextView textTabTitle = (TextView) customView.findViewById(R.id.textTabTitle);
                textTabTitle.setTextColor(ContextCompat.getColor(aContext, R.color.xlgangchat_text_tab_normal_color));
                textTabTitle.setBackgroundResource(R.mipmap.qm_btn_gangchat_normal);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private class FragmentInGangPagerAdapter extends FragmentStatePagerAdapter {
        List<XLCategoryBean> list;
        Fragment[] fragments;
        public FragmentInGangPagerAdapter(FragmentManager fm, List list) {
            super(fm);
            this.list = list;
            if(!list.isEmpty() && list.size() > 0) {
                fragments = new Fragment[list.size()];
            }
        }

        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch ((list.get(position)).getType()) {
                case XLCategoryBean.TYPE_GANGMINE:
                    fragment = new GangMessageFragment();
                    break;
                case XLCategoryBean.TYPE_GANGRECRUIT:
                    fragment = new GangRecruitFragment();
                    break;
                case XLCategoryBean.TYPE_CHATSINGLE:
                    fragment = new GangChatSingleFragment();
                    break;
            }
            fragments[position] = fragment;
            return fragment;
        }

        public Fragment getFragment(int position){
            if(fragments != null && fragments.length >0){
                return fragments[position];
            }else {
                return null;
            }
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);
        }

        @Override
        public void startUpdate(ViewGroup container) {
            super.startUpdate(container);
        }

        @Override
        public Object instantiateItem(ViewGroup arg0, int arg1) {
            return super.instantiateItem(arg0, arg1);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return ((XLCategoryBean) list.get(position)).getName();
        }

        /**
         * 自定义tabItem
         * @param position
         * @return
         */
        public View getTabView(int position) {
            View tabView = LayoutInflater.from(aContext).inflate(R.layout.common_gang_chat_tablayout_tabitem, null);
            TextView textTabTitle = (TextView) tabView.findViewById(R.id.textTabTitle);
            textTabTitle.setText(list.get(position).getName());
            return tabView;
        }

    }
}
