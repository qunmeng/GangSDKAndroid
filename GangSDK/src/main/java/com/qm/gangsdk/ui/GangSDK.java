package com.qm.gangsdk.ui;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;

import com.qm.gangsdk.core.GangSDKCore;
import com.qm.gangsdk.core.outer.common.callback.DataCallBack;
import com.qm.gangsdk.core.outer.common.entity.XLUserBean;
import com.qm.gangsdk.core.outer.common.utils.StringUtils;
import com.qm.gangsdk.ui.custom.dialog.ViewTools;
import com.qm.gangsdk.ui.utils.XLToastUtil;
import com.qm.gangsdk.ui.view.common.DialogGangInfoFragment;
import com.qm.gangsdk.ui.view.common.DialogUpdateNickNameFragment;
import com.qm.gangsdk.ui.view.common.GangModuleManage;

import java.util.Map;

/**
 * Created by shuihan on 2017/7/31.
 * 核心初始化类
 */

public class GangSDK extends GangSDKCore{
    private static final String TAG = "GangSDK";
    protected static GangSDK mInstance;
    private static boolean isFullScreen = false;
    private static int screenOrientation = Configuration.ORIENTATION_PORTRAIT;

    /**
     * GangSDK单例
     *
     * @return      GangSDK对象
     */
    public static GangSDK getInstance() {
        if (mInstance == null) {
            synchronized (GangSDK.class) {
                if (mInstance == null) {
                    mInstance = new GangSDK();
                }
            }
        }
        return mInstance;
    }

    /**
     * GangSDK初始化
     *
     * @param application   Application对象
     * @return GangSDK对象
     */
    public GangSDK init(final Application application) {
        super.init(application);
        setScreenFull(true);
        setScreenOrientation(Configuration.ORIENTATION_PORTRAIT);
        return getInstance();
    }

    /**
     * GangSDK初始化
     *
     * @param application   Application对象
     * @param appKey     使用GangSDK应用的唯一标识
     * @return GangSDK对象
     */
    public GangSDK init(final Application application, String appKey) {
        init(application);
        setAppKey(appKey);
        return getInstance();
    }

    /**
     * GangSDK初始化
     *
     * @param application   Application对象
     * @param debugMode     true:显示, false:不显示
     * @return GangSDK对象
     */
    public GangSDK init(final Application application, boolean debugMode) {
        init(application);
        setDebugMode(debugMode);
        return getInstance();
    }

    /**
     * 是否显示调试日志
     *
     * @param debugMode true:显示, false:不显示
     * @return
     */
    public GangSDK setDebugMode(boolean debugMode) {
        super.setDebugMode(debugMode);
        return getInstance();
    }

    /**
     * 设置AppKey
     *
     * @param appKey 使用GangSDK应用的唯一标识
     * @return
     */
    public GangSDK setAppKey(String appKey) {
        super.setAppKey(appKey);
        return getInstance();
    }

    /**
     * 设置全屏模式
     *
     * @param isFullScreen true:全屏, false:不全屏
     */
    public GangSDK setScreenFull(boolean isFullScreen) {
        this.isFullScreen = isFullScreen;
        return getInstance();
    }

    /**
     * 是否全屏
     *
     * @return
     */
    public boolean isFullScreen() {
        return isFullScreen;
    }

    /**
     * 获取横、竖屏
     *
     * @return
     */
    public int getScreenOrientation() {
        return screenOrientation;
    }

    /**
     * 设置横竖屏
     *
     * @return
     */
    public GangSDK setScreenOrientation(int screenOrientation) {
        this.screenOrientation = screenOrientation;
        return getInstance();
    }

    /**
     * 启动社群界面
     * @param context       context
     */
    public void startUI(final Context context) {
        startUI(context, null, null, null, null, null, null);
    }

    /**
     * 启动社群界面
     * @param context       context
     * @param gameuserid    用户id
     */
    public void startUI(final Context context, String gameuserid) {
        startUI(context, gameuserid, null, null, null, null, null);
    }

    /**
     * 启动社群界面
     * @param context       context
     * @param gameuserid    用户id
     * @param nickname      昵称
     */
    public void startUI(final Context context, String gameuserid, String nickname) {
        startUI(context, gameuserid, nickname, null, null, null, null);
    }

    /**
     * 启动社群界面
     * @param context       context
     * @param gameuserid    用户id
     * @param nickname      昵称
     * @param headiconurl   用户头像地址
     */
    public void startUI(final Context context, String gameuserid, String nickname, String headiconurl) {
        startUI(context, gameuserid, nickname, headiconurl, null, null, null);
    }

    /**
     * 启动社群界面
     * @param context       context
     * @param gameuserid    用户id
     * @param nickname      昵称
     * @param headiconurl   用户头像地址
     * @param gamelevel     游戏等级
     * @param gamerole      游戏角色
     * @param extDic        扩展信息
     */
    public void startUI(final Context context, String gameuserid, String nickname, String headiconurl, String gamelevel, String gamerole, Map extDic) {
        final Dialog loading = ViewTools.createLoadingDialog(context, "正在登录...", true);
        loading.show();
        login(gameuserid, nickname, headiconurl, gamelevel, gamerole, extDic, new DataCallBack<XLUserBean>() {
            @Override
            public void onSuccess(int status, final String message, final XLUserBean data) {
                loading.dismiss();
                if (data != null) {
                    if (StringUtils.isEmpty(data.getNickname())) {
                        new DialogUpdateNickNameFragment()
                                .setCallback(new DialogUpdateNickNameFragment.UpdateNickNameSuccessCallback() {
                                    @Override
                                    public void updateNickNameSuceess() {
                                        if (data.getConsortiaid() != null && data.getConsortiaid().intValue() > 0) {
                                            GangModuleManage.toInGangTabActivity(context);
                                        } else {
                                            GangModuleManage.toOutGangTabActivity(context);
                                        }
                                    }
                                })
                                .show(((Activity) context).getFragmentManager());
                    } else {
                        if (data.getConsortiaid() != null && data.getConsortiaid().intValue() > 0) {
                            GangModuleManage.toInGangTabActivity(context);
                        } else {
                            GangModuleManage.toOutGangTabActivity(context);
                        }
                    }
                }
            }

            @Override
            public void onFail(String message) {
                loading.dismiss();
                XLToastUtil.showToastShort(message);
            }
        });
    }

    /**
     * 显示社群信息界面
     * @param context       Context对象
     * @param consortiaid   社群ID（不能为空）
     */
    public void showGangInfo(Context context, String consortiaid){
        if(StringUtils.isEmpty(consortiaid)){
            XLToastUtil.showToastShort("ID不能为空");
        }else {
            new DialogGangInfoFragment()
                    .setGangId(Integer.valueOf(consortiaid).intValue())
                    .show(((Activity)context).getFragmentManager());
        }
    }
}
