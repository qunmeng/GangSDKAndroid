package com.qm.gangsdk.ui.view.gangin;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.entity.XLGangTaskBean;
import com.qm.gangsdk.core.outer.common.entity.XLMessageSpecialBean;
import com.qm.gangsdk.core.outer.common.entity.XLUserBean;
import com.qm.gangsdk.core.outer.common.utils.BeanUtils;
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
import com.qm.gangsdk.ui.view.gangin.chat.GangInChatFragment;
import com.qm.gangsdk.ui.view.gangin.info.GangInfoFragment;
import com.qm.gangsdk.ui.view.gangin.manage.GangManageFragment;
import com.qm.gangsdk.ui.view.gangin.members.GangMembersFragment;
import com.qm.gangsdk.ui.view.gangin.task.GangTaskFragment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：shuzhou on 2017/8/8.
 * 邮箱：810421209@qq.com
 * 版本：v1.0
 */
public class InGangTabActivity extends XLBaseActivity {

    private ImageButton btnMenuLeft;
    private View viewMainCenter;
    private ImageButton btnMessageCenter;
    private ImageButton btnGameCenter;
    private ImageButton btnUserCenter;
    private ImageButton btnGangRank;
    private ImageView imageMessageRedpoint;
    private TextView tvTitle;
    private TabLayout tabLayoutXL;
    private ViewPager viewPagerXL;
    private List<XLCategoryBean> list = new ArrayList<>();

    private GangReceiverListener receiveGangTaskListener;
    private GangReceiverListener receiveKickoutListener;
    private GangReceiverListener receiveGangDissolvedListener;
    private GangReceiverListener receiveNotifyMessageListener;
    private GangReceiverListener receiveLoginOnOtherPlatListener;
    private GangReceiverListener receiveClickedSpecialMessageListener;

    @Override

    protected int getContentView() {
        return R.layout.activityxl_ingang_tab;
    }

    @Override
    protected void initView() {
        btnMenuLeft = (ImageButton) findViewById(R.id.btnMenuLeft);
        tabLayoutXL = (TabLayout) findViewById(R.id.tabLayoutXL);
        viewPagerXL = (ViewPager) findViewById(R.id.viewPagerXL);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(GangConfigureUtils.getGangName());
        viewMainCenter = findViewById(R.id.viewMainCenter);
        btnMessageCenter = (ImageButton) findViewById(R.id.btnMessageCenter);
        btnGameCenter = (ImageButton) findViewById(R.id.btnGameCenter);
        btnUserCenter = (ImageButton) findViewById(R.id.btnUserCenter);
        btnGangRank = (ImageButton) findViewById(R.id.btnGangRank);
        imageMessageRedpoint = (ImageView) findViewById(R.id.imageMessageRedpoint);

        isHasUnreadMessage();
        isAllowedUseAppRecommend();
        viewMainCenter.setVisibility(View.VISIBLE);
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

    @Override
    protected void initData() {
        if (GangConfigureUtils.isHasUnfinishedTask()) {
            updateTabRedpoint();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewPager();
        btnMenuLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XLActivityManager.getInstance().finishAllActivity();
            }
        });
        btnGangRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GangModuleManage.toMemberSortActivity(aContext);
            }
        });

        //新任务
        receiveGangTaskListener = GangSDK.getInstance().receiverManager().addReceiveGangTaskListener(this, new OnGangReceiverListener<XLGangTaskBean>() {
            @Override
            public void onReceived(XLGangTaskBean data) {
                updateTabRedpoint();
            }
        });

        //踢出用戶
        receiveKickoutListener = GangSDK.getInstance().receiverManager().addReceiveKickoutListener(this, new OnGangReceiverListener() {
            @Override
            public void onReceived(Object data) {
                GangInManager.quitGangToNoGuidActivity(aContext);
            }
        });

        //收到通知消息监听
        receiveNotifyMessageListener = GangSDK.getInstance().receiverManager().addReceiveNotifyMessageListener(this, new OnGangReceiverListener() {
            @Override
            public void onReceived(Object data) {
                imageMessageRedpoint.setVisibility(View.VISIBLE);
            }
        });

        //解散社群
        receiveGangDissolvedListener = GangSDK.getInstance().receiverManager().addReceiveGangDissolvedListener(this, new OnGangReceiverListener() {
            @Override
            public void onReceived(Object data) {
                GangInManager.quitGangToNoGuidActivity(aContext);
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

        //收到点击特殊消息
        receiveClickedSpecialMessageListener = GangSDK.getInstance().receiverManager().addReceiveClickedSpecialMessageListener(this, new OnGangReceiverListener<XLMessageSpecialBean>() {
            @Override
            public void onReceived(XLMessageSpecialBean data) {
                try{
                    XLUserBean bean = BeanUtils.convertToBean((JSONObject)data.getExtra(), XLUserBean.class);
                    XLToastUtil.showToastShort("点击特殊消息啦 -- Gameid:"+bean.getGameid() + " -- Userid:" +bean.getUserid());
                }catch(Exception e){
                    e.printStackTrace();
                }
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
        GangSDK.getInstance().receiverManager().removeReceiverListener(receiveGangTaskListener);
        GangSDK.getInstance().receiverManager().removeReceiverListener(receiveKickoutListener);
        GangSDK.getInstance().receiverManager().removeReceiverListener(receiveGangDissolvedListener);
        GangSDK.getInstance().receiverManager().removeReceiverListener(receiveNotifyMessageListener);
        GangSDK.getInstance().receiverManager().removeReceiverListener(receiveLoginOnOtherPlatListener);
        GangSDK.getInstance().receiverManager().removeReceiverListener(receiveClickedSpecialMessageListener);
    }

    /**
     * 设置显示通知小红点
     */
    private void updateTabRedpoint() {
        if(list == null || list.isEmpty()){
            return;
        }
        View customView = tabLayoutXL.getTabAt(list.size() - 1).getCustomView();
        View imageTabTedPoint = customView.findViewById(R.id.imageTabTedPoint);
        if (tabLayoutXL.getSelectedTabPosition() == (list.size() - 1)) {
            imageTabTedPoint.setVisibility(View.GONE);
        } else{
            imageTabTedPoint.setVisibility(View.VISIBLE);
        }
    }

    private void initViewPager() {
        list.add(new XLCategoryBean(XLCategoryBean.TYPE_GANGIN_CHAT, aContext.getResources().getString(R.string.gangin_chat_tab)));
        list.add(new XLCategoryBean(XLCategoryBean.TYPE_INFO, aContext.getResources().getString(R.string.gang_info_tab)));
        list.add(new XLCategoryBean(XLCategoryBean.TYPE_MEMBER, aContext.getResources().getString(R.string.gang_member_tab)));
        list.add(new XLCategoryBean(XLCategoryBean.TYPE_MANAGE, aContext.getResources().getString(R.string.gang_manage_tab)));
        if(GangConfigureUtils.isAllowUseTask()){
            list.add(new XLCategoryBean(XLCategoryBean.TYPE_TASK, aContext.getResources().getString(R.string.gang_task_tab)));
        }
        FragmentInGangPagerAdapter adapter = new FragmentInGangPagerAdapter(getSupportFragmentManager(), list);
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
        tabLayoutXL.addOnTabSelectedListener(new TabSelectedListener(aContext, list.size() - 1));
    }


    private class FragmentInGangPagerAdapter extends FragmentStatePagerAdapter {
        List<XLCategoryBean> list;

        public FragmentInGangPagerAdapter(FragmentManager fm, List list) {
            super(fm);
            this.list = list;
        }

        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch ((list.get(position)).getType()) {
                case XLCategoryBean.TYPE_GANGIN_CHAT:
                    fragment = new GangInChatFragment();
                    break;
                case XLCategoryBean.TYPE_INFO:
                    fragment = new GangInfoFragment();
                    break;
                case XLCategoryBean.TYPE_MEMBER:
                    fragment = new GangMembersFragment();
                    break;
                case XLCategoryBean.TYPE_MANAGE:
                    fragment = new GangManageFragment();
                    break;
                case XLCategoryBean.TYPE_TASK:
                    fragment = new GangTaskFragment();
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
            return ((XLCategoryBean) list.get(position)).getName();
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
