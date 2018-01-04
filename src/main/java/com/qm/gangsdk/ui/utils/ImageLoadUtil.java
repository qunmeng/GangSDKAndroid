package com.qm.gangsdk.ui.utils;

import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.qm.gangsdk.core.GangSDKCore;

/**
 * Created by shuzhou on 2017/8/3.
 * 图片加载工具类
 */

public class ImageLoadUtil {

    private static final int ROUND_RADIUS = 5; //圆角大小（固定）
    public static ImageLoader mImageLoader;

    public interface ImageLoader {
        void load(ImageView imageView, String url);
    }

    public static void load(ImageView imageView, String url) {
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader() {
                @Override
                public void load(ImageView imageView, String url) {
                    displayImage(imageView, url);
                }
            };
        }
        mImageLoader.load(imageView, url);
    }


    public static void displayImage(ImageView imageView, String url) {
        Glide.with(GangSDKCore.getInstance().getContext()).load(url).fitCenter().dontAnimate().into(imageView);
    }


    /**
     * 加载正常图片
     * @param imageView
     * @param url
     */
    public static void loadNormalImage(ImageView imageView, String url) {
        Glide.with(GangSDKCore.getInstance().getContext()).load(url).dontAnimate().into(imageView);
    }

    /**
     * 加载圆角图片
     * @param imageView
     * @param url
     */
    public static void loadRoundImage(ImageView imageView, String url) {
        Glide.with(GangSDKCore.getInstance().getContext()).load(url).asBitmap()
                .transform(new CenterCrop(GangSDKCore.getInstance().getContext()),new GlideRoundTransform(GangSDKCore.getInstance().getContext(), ROUND_RADIUS))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    /**
     * 加载圆形图片
     * @param imageView
     * @param url
     */
    public static void loadCircleImage(final ImageView imageView, String url) {
        Glide.with(GangSDKCore.getInstance().getContext()).load(url).asBitmap().centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(GangSDKCore.getInstance().getContext().getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        });
    }
}
