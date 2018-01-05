package com.qm.gangsdk.demo;

import android.app.Application;

import com.qm.gangsdk.ui.GangSDK;

/**
 *
 * 自定义Application
 */

public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        GangSDK.getInstance().init(this, true);
    }

}
