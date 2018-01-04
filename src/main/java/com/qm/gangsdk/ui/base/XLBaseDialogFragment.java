package com.qm.gangsdk.ui.base;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.qm.gangsdk.ui.custom.dialog.ViewTools;

/**
 * Created by xl on 2017/8/16.
 * Fragment基类
 */

public abstract class XLBaseDialogFragment extends DialogFragment {

    private View mView;

    public Fragment mContext;

    public Activity aContext;
    public Dialog loading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        aContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        if(dialog != null){
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(cancelTouchOutSide());
        }

        mView = inflater.inflate(getContentView(), null);
        loading = ViewTools.createLoadingDialog(aContext,"正在加载数据...", true);
        initView(mView);
        initData();
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 设置布局文件
     *
     * @return
     */
    protected abstract int getContentView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化View
     */
    protected abstract void initView(View view);


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    /**
     * 关闭对话框
     */
    public void close(){
        dismissAllowingStateLoss();
    }

    /**
     * 设置在屏幕外点击是否显示
     * @return
     */
    protected boolean cancelTouchOutSide(){
        return true;
    }

    /**
     * 显示对话框
     * @param manager
     */
    public void show(FragmentManager manager){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if(manager.isDestroyed()) return;
        }
        try {
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            if (isAdded()) {
                fragmentTransaction.show(this).commitAllowingStateLoss();
            } else {
                fragmentTransaction.add(this, getClass().getName()).commitAllowingStateLoss();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
