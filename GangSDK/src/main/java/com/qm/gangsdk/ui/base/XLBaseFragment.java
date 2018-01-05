package com.qm.gangsdk.ui.base;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qm.gangsdk.ui.custom.dialog.ViewTools;

/**
 * Created by xl on 2017/8/1.
 * Fragment基类
 */

public abstract class XLBaseFragment extends Fragment {

    public Fragment mContext;

    public Activity aContext;
    private View mView;
    public Dialog loading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getContentView(), null);
        loading = ViewTools.createLoadingDialog(aContext,"正在加载数据...", true);
        initView(mView);
        initData();
        return mView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        aContext = getActivity();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void showDialogLoading(){
        if(loading != null){
            loading.show();
        }
    }

    public void dismissDialogLoading(){
        try{
            Thread.sleep(500);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(loading != null && loading.isShowing()){
            loading.dismiss();
        }
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
     * 初始化view
     * @param view
     */
    protected abstract void initView(View view);


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
