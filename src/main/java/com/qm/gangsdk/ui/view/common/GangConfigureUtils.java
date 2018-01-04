package com.qm.gangsdk.ui.view.common;

import com.qm.gangsdk.core.outer.common.utils.StringUtils;
import com.qm.gangsdk.ui.GangSDK;

/**
 * Created by lijiyuan on 2017/9/26.
 * 社群配置信息
 */

public class GangConfigureUtils {

    /**
     * 获取gangname
     * @return
     */
    public static String getGangName(){
        String gangname = null;
        if(!StringUtils.isEmpty(GangSDK.getInstance().groupManager().getGameConfigEntity().getGamevariable().getGangname())) {
            gangname = GangSDK.getInstance().groupManager().getGameConfigEntity().getGamevariable().getGangname();
        }
        return StringUtils.getString(gangname, "");
    }

    /**
     * 获取gangowner
     * @return
     */
    public static String getGangowner(){
        String gangowner = null;
        if(!StringUtils.isEmpty(GangSDK.getInstance().groupManager().getGameConfigEntity().getGamevariable().getGangowner())){
            gangowner =  GangSDK.getInstance().groupManager().getGameConfigEntity().getGamevariable().getGangowner();
        }
        return StringUtils.getString(gangowner, "");
    }

    /**
     * 获取moneyname
     * @return
     */
    public static String getMoneyName(){
        String moneyname = null;
        if(!StringUtils.isEmpty(GangSDK.getInstance().groupManager().getGameConfigEntity().getGamevariable().getMoneyname())){
            moneyname = GangSDK.getInstance().groupManager().getGameConfigEntity().getGamevariable().getMoneyname();
        }
        return StringUtils.getString(moneyname, "");
    }


    /**
     * 是否使用任务模块
     * @return    true ---是；false ---否；
     */
    public static boolean isAllowUseTask(){
        if(!StringUtils.isEmpty(GangSDK.getInstance().groupManager().getGameConfigEntity().getGameconfig().getIs_use_task_module())){
            String is_use_task_module = GangSDK.getInstance().groupManager().getGameConfigEntity().getGameconfig().getIs_use_task_module();
            if(1 == Integer.valueOf(is_use_task_module).intValue() ){
                return true;
            }else {
                return false;
            }
        }
        return false;
    }

    /**
     * 是否使用推荐app模块
     * @return    true ---是；false ---否；
     */
    public static boolean isAllowedUseAppRecommend(){
        if(!StringUtils.isEmpty(GangSDK.getInstance().groupManager().getGameConfigEntity().getGameconfig().getIs_use_gameapp_recommend_module())){
            String is_use_gameapp_recommend_module = GangSDK.getInstance().groupManager().getGameConfigEntity().getGameconfig().getIs_use_gameapp_recommend_module();
            if(1 == Integer.valueOf(is_use_gameapp_recommend_module).intValue()){
                return true;
            }else {
                return false;
            }
        }
        return false;
    }

    /**
     * 是否使用召集令模块
     * @return    true ---是；false ---否；
     */
    public static boolean isAllowUseCallup(){
        if(!StringUtils.isEmpty(GangSDK.getInstance().groupManager().getGameConfigEntity().getGameconfig().getIs_use_callup_function_flag())) {
            String callupFlag = GangSDK.getInstance().groupManager().getGameConfigEntity().getGameconfig().getIs_use_callup_function_flag();
            if (1 == Integer.valueOf(callupFlag).intValue()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * 是否可以上传图像
     * @return    true ---是；false ---否；
     */
    public static boolean isAllowUploadUserHead(){
        if(!StringUtils.isEmpty(GangSDK.getInstance().groupManager().getGameConfigEntity().getGameconfig().getUser_icon_is_allow_update())) {
            String user_icon_is_allow_update = GangSDK.getInstance().groupManager().getGameConfigEntity().getGameconfig().getUser_icon_is_allow_update();
            if (1 == Integer.valueOf(user_icon_is_allow_update).intValue()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * 是否有未读消息
     * @return     true ---是；false ---否；
     */
    public static boolean isHasUnreadMessage(){
        if(GangSDK.getInstance().userManager().getXlUserBean().getHasmessage() != null){
            Integer hasmessage = GangSDK.getInstance().userManager().getXlUserBean().getHasmessage();
            if(1 == hasmessage.intValue()){
                return true;
            }else {
                return false;
            }
        }
        return false;
    }

    /**
     * 是否有未完成任务
     * @return   true ---是；false ---否；
     */
    public static boolean isHasUnfinishedTask(){
        if(GangSDK.getInstance().userManager().getXlUserBean().getTaskfinished() != null) {
            Integer taskfinished = GangSDK.getInstance().userManager().getXlUserBean().getTaskfinished();
            if (0 == taskfinished.intValue()) {
                return true;
            }else {
                return false;
            }
        }
        return false;
    }
}
