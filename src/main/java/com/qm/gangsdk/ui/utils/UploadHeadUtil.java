package com.qm.gangsdk.ui.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.qm.gangsdk.ui.R;

import java.io.File;

import xl.com.jph.takephoto.app.TakePhoto;
import xl.com.jph.takephoto.compress.CompressConfig;
import xl.com.jph.takephoto.model.CropOptions;
import xl.com.jph.takephoto.model.TakePhotoOptions;

/**
 *  Created by lijiyuan on 2017/9/21
 * 上传图片管理
 */

public class UploadHeadUtil {

    private static File mUploadFile = new File(Environment.getExternalStorageDirectory(), "/gangskd/upload_icon.jpg");
    private static Uri mImageUri;
     /**
     * 显示选择照片的方式
     * @param context
     * @return
     */
    public static PopupWindow showChoosePhotoMethod(final Context context, View attachView, final TakePhoto takePhoto) {
        if(context == null || takePhoto == null) return null;
        if(mImageUri == null) {
            if (!mUploadFile.getParentFile().exists())mUploadFile.getParentFile().mkdirs();
            mImageUri = Uri.fromFile(mUploadFile);
        }
        configCompress(takePhoto);
        View viewPopUpWindow = LayoutInflater.from(context).inflate(R.layout.xl_popupwindow_photo_select, null);
        final PopupWindow popupWindow = new PopupWindow(viewPopUpWindow, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT, true);
        viewPopUpWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    popupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });
        viewPopUpWindow.findViewById(R.id.btnCamera).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(mImageUri != null) {
                    takePhoto.onPickFromCaptureWithCrop(mImageUri, getCropOptions(context));
                }
                popupWindow.dismiss();
            }
        });
        viewPopUpWindow.findViewById(R.id.btnSDCard).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(mImageUri != null) {
                    takePhoto.onPickFromGalleryWithCrop(mImageUri, getCropOptions(context));
                }
                popupWindow.dismiss();
            }
        });
        viewPopUpWindow.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(attachView, Gravity.BOTTOM, 0, 0);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        return popupWindow;
    }

    public static CropOptions getCropOptions(Context context){
        int height= DensityUtil.dip2px(context, 100);
        int width= DensityUtil.dip2px(context, 100);
        boolean withWonCrop= true;

        CropOptions.Builder builder=new CropOptions.Builder();

        builder.setAspectX(width).setAspectY(height);
        //builder.setOutputX(width).setOutputY(height);
        builder.setWithOwnCrop(withWonCrop);
        return builder.create();
    }

    private static void configCompress(TakePhoto takePhoto) {
        int maxSize = 1024 * 50;  //50kb
        int width = 100;
        int height = 100;
        boolean showProgressBar = true;
        boolean enableRawFile = false;
        CompressConfig config;
        if (true) {
            config = new CompressConfig.Builder()
                    .setMaxSize(maxSize)
                    .setMaxPixel(width >= height ? width : height)
                    .enableReserveRaw(enableRawFile)
                    .create();
        }
//        else {
//            LubanOptions option = new LubanOptions.Builder()
//                    .setMaxHeight(height)
//                    .setMaxWidth(width)
//                    .setMaxSize(maxSize)
//                    .create();
//            config = CompressConfig.ofLuban(option);
//            config.enableReserveRaw(enableRawFile);
//        }
        takePhoto.onEnableCompress(config, showProgressBar);
        TakePhotoOptions options = new TakePhotoOptions.Builder()
                .setCorrectImage(true)
                .create();
        takePhoto.setTakePhotoOptions(options);
    }
}
