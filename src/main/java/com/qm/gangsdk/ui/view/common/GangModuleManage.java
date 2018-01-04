package com.qm.gangsdk.ui.view.common;

import android.content.Context;
import android.content.Intent;

import com.qm.gangsdk.ui.view.gangcenter.game.GangCenterGameActivity;
import com.qm.gangsdk.ui.view.gangcenter.message.GangCenterMessageActivity;
import com.qm.gangsdk.ui.view.gangcenter.user.GangCenterUserActivity;
import com.qm.gangsdk.ui.view.gangin.InGangTabActivity;
import com.qm.gangsdk.ui.view.gangin.manage.ManageApplyActivity;
import com.qm.gangsdk.ui.view.gangin.manage.ManageRoleAccessActivity;
import com.qm.gangsdk.ui.view.gangin.members.MemberInfoActivity;
import com.qm.gangsdk.ui.view.gangin.members.MemberListActivity;
import com.qm.gangsdk.ui.view.gangout.OutGangTabActivity;
import com.qm.gangsdk.ui.view.gangout.create.GangCreateActivity;

/**
 * Created by shuzhou on 2017/8/3.
 */

public class GangModuleManage {

    //跳转到没有社群主界面
    public static void toOutGangTabActivity(Context context) {
        Intent intent = new Intent(context, OutGangTabActivity.class);
        context.startActivity(intent);
    }

    //跳转到创建社群主界面
    public static void toGangCreateActivity(Context context) {
        Intent intent = new Intent(context, GangCreateActivity.class);
        context.startActivity(intent);
    }

    //跳转到社群主界面
    public static void toInGangTabActivity(Context context) {
        Intent intent = new Intent(context, InGangTabActivity.class);
        context.startActivity(intent);
    }

    //跳转活跃榜
    public static void toMemberActiveActivity(Context context) {
        Intent intent = new Intent(context, MemberListActivity.class);
        intent.putExtra(MemberListActivity.TYPE, MemberListActivity.ACTIVIE);
        context.startActivity(intent);
    }

    //跳转贡献榜
    public static void toMemberContributeActivity(Context context) {
        Intent intent = new Intent(context, MemberListActivity.class);
        intent.putExtra(MemberListActivity.TYPE, MemberListActivity.CONTRUBUTE);
        context.startActivity(intent);
    }


    //跳转用户排行榜
    public static void toMemberSortActivity(Context context) {
        Intent intent = new Intent(context, MemberListActivity.class);
        intent.putExtra(MemberListActivity.TYPE, MemberListActivity.SORT);
        context.startActivity(intent);
    }

    //跳转禁言列表
    public static void toMemberNotalkActivity(Context context) {
        Intent intent = new Intent(context, MemberListActivity.class);
        intent.putExtra(MemberListActivity.TYPE, MemberListActivity.NOTALK);
        context.startActivity(intent);
    }

    //跳转玩家详情
    public static void toMemberInfoActivity(Context context, int memberid) {
        Intent intent = new Intent(context, MemberInfoActivity.class);
        intent.putExtra("memberid", memberid);
        context.startActivity(intent);
    }

    //跳转申请列表
    public static void toGangApplyListActivity(Context context){
        Intent intent = new Intent(context, ManageApplyActivity.class);
        context.startActivity(intent);
    }

    //跳转职称管理
    public static void toManageProfessionActivity(Context context){
        Intent intent = new Intent(context, ManageRoleAccessActivity.class);
        context.startActivity(intent);
    }

    //跳转游戏中心
    public static void toGangCenterGameActivity(Context context){
        Intent intent = new Intent(context, GangCenterGameActivity.class);
        context.startActivity(intent);
    }

    //跳转消息中心
    public static void toGangCenterMessageActivity(Context context){
        Intent intent = new Intent(context, GangCenterMessageActivity.class);
        context.startActivity(intent);
    }

    //跳转个人中心
    public static void toGangCenterUserActivity(Context context){
        Intent intent = new Intent(context, GangCenterUserActivity.class);
        context.startActivity(intent);
    }
}
