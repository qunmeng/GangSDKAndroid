package com.qm.gangsdk.ui.view.gangin;

import android.content.Context;

import com.qm.gangsdk.ui.GangSDK;
import com.qm.gangsdk.ui.utils.XLActivityManager;
import com.qm.gangsdk.ui.view.common.GangModuleManage;

/**
 * Description: 社群内管理类
 */

public class GangInManager {

    /**
     * 退出社群并且页面跳转
     * @param context
     */
    public static void quitGangToNoGuidActivity(Context context){
        //清除用户社群信息
        GangSDK.getInstance().clearUserInGangCache();
        //退出所有页面
        XLActivityManager.getInstance().finishAllActivity();
        //跳转页面
        GangModuleManage.toOutGangTabActivity(context);
    }

}
