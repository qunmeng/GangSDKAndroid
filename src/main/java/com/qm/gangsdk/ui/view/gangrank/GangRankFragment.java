package com.qm.gangsdk.ui.view.gangrank;

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
import android.widget.ImageButton;
import android.widget.TextView;

import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseFragment;
import com.qm.gangsdk.ui.entity.XLCategoryBean;
import com.qm.gangsdk.ui.view.common.GangConfigureUtils;
import com.qm.gangsdk.ui.view.common.TabSelectedListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lijiyuan on 2017/9/20.
 */

public class GangRankFragment extends XLBaseFragment {

    private ImageButton btnMenuLeft;
    private ImageButton btnMenuRight;

    private TextView tvTitle;
    private TabLayout tabLayoutXL;
    private ViewPager viewPagerXL;

    @Override
    protected int getContentView() {
        return R.layout.fragment_rank;
    }

    @Override
    protected void initView(View view) {
        btnMenuLeft = (ImageButton) view.findViewById(R.id.btnMenuLeft);
        btnMenuRight = (ImageButton) view.findViewById(R.id.btnMenuRight);
        tabLayoutXL = (TabLayout) view.findViewById(R.id.tabLayoutXL);
        viewPagerXL = (ViewPager) view.findViewById(R.id.viewPagerXL);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvTitle.setText(GangConfigureUtils.getGangName()+"排行榜");
        btnMenuRight.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViewPager();
        btnMenuLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aContext.finish();
            }
        });
    }

    private void initViewPager() {
        List<XLCategoryBean> list = new ArrayList<>();
        list.add(new XLCategoryBean(XLCategoryBean.TYPE_RENQI, aContext.getResources().getString(R.string.gang_rank_renqi_tab)));
        list.add(new XLCategoryBean(XLCategoryBean.TYPE_LEVEL, aContext.getResources().getString(R.string.gang_rank_level_tab)));
        list.add(new XLCategoryBean(XLCategoryBean.TYPE_CAIFU, aContext.getResources().getString(R.string.gang_rank_caifu_tab)));
        FragmentRankPagerAdapter adapter = new FragmentRankPagerAdapter(getChildFragmentManager(), list);
        viewPagerXL.setAdapter(adapter);
        tabLayoutXL.setupWithViewPager(viewPagerXL);
        viewPagerXL.setOffscreenPageLimit(0);
        for (int i = 0; i < tabLayoutXL.getTabCount(); i++) {
            View tabView = adapter.getTabView(i);
            tabLayoutXL.getTabAt(i).setCustomView(tabView);
            if (i == 0) {
                // 设置第一个tab的TextView是被选择的样式
                View customView = tabLayoutXL.getTabAt(i).getCustomView();
                TextView textTabTitle = (TextView) customView.findViewById(R.id.textTabTitle);
                textTabTitle.setTextColor(ContextCompat.getColor(aContext, R.color.xlgang_text_tab_selected_color));
            }
        }
        viewPagerXL.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutXL));
        tabLayoutXL.addOnTabSelectedListener(new TabSelectedListener(aContext));
    }


    private class FragmentRankPagerAdapter extends FragmentStatePagerAdapter {
        List<XLCategoryBean> list;

        public FragmentRankPagerAdapter(FragmentManager fm, List list) {
            super(fm);
            this.list = list;
        }

        public Fragment getItem(int position) {
            Fragment fragment = null;
            Bundle bundle = null;
            switch ((list.get(position)).getType()) {
                case XLCategoryBean.TYPE_RENQI:
                    bundle = new Bundle();
                    bundle.putInt("type", RankFragment.TYPE_RENQI);
                    fragment = new RankFragment();
                    fragment.setArguments(bundle);
                    break;
                case XLCategoryBean.TYPE_LEVEL:
                    bundle = new Bundle();
                    bundle.putInt("type", RankFragment.TYPE_LEVEL);
                    fragment = new RankFragment();
                    fragment.setArguments(bundle);
                    break;
                case XLCategoryBean.TYPE_CAIFU:
                    bundle = new Bundle();
                    bundle.putInt("type", RankFragment.TYPE_CAIFU);
                    fragment = new RankFragment();
                    fragment.setArguments(bundle);
                    break;
            }
            return fragment;
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
            return list.get(position).getName();
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
