package com.qm.gangsdk.ui.view.common;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qm.gangsdk.ui.R;

/**
 * Created by lijiyuan on 2017/11/10.
 *
 * 本应用中通用的TabLayout,设置TabLayout上的TabItem选中与未选中状态。
 */

public class TabSelectedListener implements TabLayout.OnTabSelectedListener {

    private Context context;
    private int notifyPosition = -1;      //需要更新提示信息的TabItem下标(默认没有通知)

    public TabSelectedListener(Context context, int position) {
        this.context = context;
        this.notifyPosition = position;
    }

    public TabSelectedListener(Context context) {
        this.context = context;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if(tab.getPosition() < 0){
            return;
        }
        View customView = tab.getCustomView();
        ImageView imageTabTedPoint = (ImageView) customView.findViewById(R.id.imageTabTedPoint);
        TextView textTabTitle = (TextView) customView.findViewById(R.id.textTabTitle);
        textTabTitle.setTextColor(ContextCompat.getColor(context, R.color.xlgang_text_tab_selected_color));
        if(tab.getPosition() == notifyPosition){
            imageTabTedPoint.setVisibility(View.GONE);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        if(tab.getPosition() < 0){
            return;
        }
        View customView = tab.getCustomView();
        TextView textTabTitle = (TextView) customView.findViewById(R.id.textTabTitle);
        textTabTitle.setTextColor(ContextCompat.getColor(context, R.color.xlgang_text_tab_normal_color));
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
