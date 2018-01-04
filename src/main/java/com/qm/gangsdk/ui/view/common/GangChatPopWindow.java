package com.qm.gangsdk.ui.view.common;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.utils.DensityUtil;
import com.qm.gangsdk.ui.utils.XLToastUtil;

/**
 * Created by lijiyuan on 2017/12/12.
 *
 * 聊天频道PopWindow
 */

public class GangChatPopWindow {
    private PopupWindow mPopwindow;
    public ClickCallBack clickCallBack;

    public GangChatPopWindow(ClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    public void showGangChatPopWindow(final Context context, View viewparent, final Integer gangid, final Integer userid){
        int[] location = new int[2];
        viewparent.getLocationOnScreen(location);
        View view = LayoutInflater.from(context).inflate(R.layout.gang_chat_popwindow, null);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Button btnChatSingle = (Button) view.findViewById(R.id.btnChatSingle);
        Button btnCheckUserInfo = (Button) view.findViewById(R.id.btnCheckUserInfo);
        Button btnCheckGangInfo = (Button) view.findViewById(R.id.btnCheckGangInfo);
        btnCheckGangInfo.setText(GangConfigureUtils.getGangName() + "信息");

        //私聊
        btnChatSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickCallBack != null){
                    clickCallBack.chatSingle();
                }
                closedPopWindow();
            }
        });
        //个人信息
        btnCheckUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userid != null) {
                    GangModuleManage.toMemberInfoActivity(context, userid.intValue());
                }
                closedPopWindow();
            }
        });
        //社群信息
        btnCheckGangInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gangid != null) {
                    new DialogGangInfoFragment().setGangId(gangid.intValue()).show(((Activity) context).getFragmentManager());
                }else {
                    XLToastUtil.showToastShort("玩家未加入" + GangConfigureUtils.getGangName());
                }
                closedPopWindow();
            }
        });
        mPopwindow = new PopupWindow(view, DensityUtil.dip2px(context, 100), DensityUtil.dip2px(context, 110));
        mPopwindow.setFocusable(true);
        mPopwindow.setOutsideTouchable(true);
        mPopwindow.setBackgroundDrawable(new BitmapDrawable());
        mPopwindow.showAtLocation(viewparent, Gravity.NO_GRAVITY, location[0] + viewparent.getWidth(), location[1]);
    }

    /**
     * 关闭PopWindow
     */
    private void closedPopWindow(){
        if(mPopwindow != null || mPopwindow.isShowing()){
            mPopwindow.dismiss();
        }
    }

    /**
     * 私聊回调
     */
    public interface ClickCallBack{
        void chatSingle();
    }
}
