package com.qm.gangsdk.ui.utils;

import android.widget.Toast;

import com.qm.gangsdk.core.GangSDKCore;
import com.qm.gangsdk.core.outer.common.utils.StringUtils;


/**
 * Created by shuzhou on 2017/8/3.
 * Toast工具类
 */

public class XLToastUtil {

    /**
     * 显示一个Long的Toast
     *
     * @param message 提示信息
     */
    public static void showToastLong(String message) {
        if (StringUtils.isEmpty(message)) return;
        Toast.makeText(GangSDKCore.getInstance().getContext(), message, Toast.LENGTH_LONG).show();
    }

    /**
     * 显示一个SHORT的Toast
     *
     * @param message 提示信息
     */
    public static void showToastShort(String message) {
        if (StringUtils.isEmpty(message)) return;
        Toast.makeText(GangSDKCore.getInstance().getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
