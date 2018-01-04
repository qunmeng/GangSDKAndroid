package com.qm.gangsdk.ui.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import com.qm.gangsdk.core.outer.common.utils.StringUtils;

import java.io.File;

/**
 * Created by lijiyuan on 2017/12/7.
 * apk安装
 */

public class XLApkUtils {

    /**
     * 检查是否安装
     * @param context           上下文对象
     * @param packageName       app包名
     * @return                  true已经安装，false未安装
     */
    public static boolean checkApkExist(Context context, String packageName){
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        try {
            PackageInfo  info = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_ACTIVITIES);
            if(info != null){
                return true;
            }else {
                return false;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 启动apk
     * @param context           上下文对象
     * @param packageName       apk包名
     */
    public static void startApk(Context context, String packageName){
        if(StringUtils.isEmpty(packageName)){
            return;
        }
        if(context == null){
            return;
        }
        Intent LaunchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        context.startActivity(LaunchIntent);
    }

    /**
     * 安装apk
     * @param context       上下文
     * @param file           apk文件
     */
    public static void installApk(Context context, File file) {
        if (file != null) {
            //会根据用户的数据类型打开android系统相应的Activity。
            Intent intent = new Intent(Intent.ACTION_VIEW);
            //设置intent的数据类型是应用程序application
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            //为这个新apk开启一个新的activity栈
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //开始安装
            context.startActivity(intent);
            //关闭旧版本的应用程序的进程
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }


    /**
     * 安装apk
     * @param context       上下文
     * @param uri           apk地址
     */
    public static void installApk(Context context, Uri uri) {
        //获取下载文件的Uri
        if (uri != null) {
            Intent intent= new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    /**
     * 浏览器下载
     * @param context       上下文
     * @param url           下载地址
     */
    public static void downloadApk(Context context, String url) throws Exception{
        if(StringUtils.isEmpty(url)){
            return;
        }
        if(context == null){
            return;
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        context.startActivity(intent);
    }
}
