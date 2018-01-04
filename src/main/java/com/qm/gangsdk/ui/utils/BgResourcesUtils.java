package com.qm.gangsdk.ui.utils;

import com.qm.gangsdk.ui.R;

/**
 * 作者：shuzhou on 2017/9/19.
 * 邮箱：810421209@qq.com
 * 版本：v1.0
 */
public class BgResourcesUtils {


    public static int getPositionBg(Integer position) {
        if(position == null) {
            position = -1;
        }
        int positionResid = R.drawable.qm_bg_role3;
        switch (position) {
            case 1:
                positionResid = R.drawable.qm_bg_role1;
                break;
            case 2:
            case 3:
            case 4:
                positionResid = R.drawable.qm_bg_role2;
                break;
            case 5:
                positionResid = R.drawable.qm_bg_role3;
                break;
            default:
                positionResid = R.drawable.qm_bg_role3;
                break;
        }
        return positionResid;

    }
}
