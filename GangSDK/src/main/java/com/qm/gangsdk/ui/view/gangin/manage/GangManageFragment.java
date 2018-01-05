package com.qm.gangsdk.ui.view.gangin.manage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.callback.DataCallBack;
import com.qm.gangsdk.core.outer.common.entity.ConsortialevellistBean;
import com.qm.gangsdk.core.outer.common.entity.XLGangInfoBean;
import com.qm.gangsdk.core.outer.common.entity.XLUploadImageBean;
import com.qm.gangsdk.core.outer.common.utils.StringUtils;
import com.qm.gangsdk.core.outer.common.utils.logger.Logger;
import com.qm.gangsdk.ui.GangSDK;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseFragment;
import com.qm.gangsdk.ui.view.common.GangConfigureUtils;
import com.qm.gangsdk.ui.utils.ImageLoadUtil;
import com.qm.gangsdk.ui.utils.UploadHeadUtil;
import com.qm.gangsdk.ui.utils.XLToastUtil;
import com.qm.gangsdk.ui.view.common.GangModuleManage;

import java.util.List;

import xl.com.jph.takephoto.app.TakePhoto;
import xl.com.jph.takephoto.app.TakePhotoImpl;
import xl.com.jph.takephoto.model.InvokeParam;
import xl.com.jph.takephoto.model.TContextWrap;
import xl.com.jph.takephoto.model.TResult;
import xl.com.jph.takephoto.permission.InvokeListener;
import xl.com.jph.takephoto.permission.PermissionManager;
import xl.com.jph.takephoto.permission.TakePhotoInvocationHandler;

/**
 * 作者：shuzhou on 2017/8/8.
 * 邮箱：810421209@qq.com
 * 版本：v1.0
 * 社群管理
 */
public class GangManageFragment extends XLBaseFragment implements TakePhoto.TakeResultListener,InvokeListener {

    private InvokeParam mInvokeParam;
    private TakePhoto mTakePhoto;

    private Button btnGangJoinSwitch;
    private ImageView imageGangIcon;
    private TextView textGangName;
    private TextView textGangLevel;
    private TextView textGangAnnouncements;
    private TextView textGangDeclaration;
    private Button btnGangApply;
    private Button btnGangManageProfession;
    private Button btnGangUpdateName;
    private Button btnGangImproveLevel;
    private Button btnGangEditAnnouncements;
    private Button btnGangEditDeclaration;
    private Button btnGangDissolve;
    private  boolean isflag = false;

    @Override
    protected int getContentView() {
        return R.layout.fragment_gang_manage;
    }

    @Override
    protected void initData() {
        Integer consortiaid = GangSDK.getInstance().userManager().getXlUserBean().getConsortiaid();
        Logger.d("gangid = " + consortiaid);
        if(consortiaid != null){
            GangSDK.getInstance().groupManager().getGangInfo(consortiaid, new DataCallBack<XLGangInfoBean>() {

                @Override
                public void onSuccess(int status, String message, XLGangInfoBean data) {
                    updateViewData(data);
                }

                @Override
                public void onFail(String message) {
                    XLToastUtil.showToastShort(message);
                }
            });
        }
    }

    /**
     * 初始化界面数据
     * @param data
     */
    private void updateViewData(final XLGangInfoBean data) {
        if(data != null){
            ImageLoadUtil.loadRoundImage(imageGangIcon, data.getIconurl());
            textGangName.setText("名称:  " + data.getConsortianame());
            textGangLevel.setText(aContext.getResources().getString(R.string.gang_level_text) + ":  " + data.getBuildlevel() + "级");
            textGangDeclaration.setText(data.getDeclaration());
            if(!data.getAnnouncements().isEmpty()) {
                textGangAnnouncements.setText(StringUtils.getString(data.getAnnouncements().get((data.getAnnouncements().size() - 1)).getContent(), ""));
            }
            if(data.getIsneedapprove() == 1){
                isflag = true;
                btnGangJoinSwitch.setBackgroundResource(R.mipmap.qm_btn_manager_open_approve);
            }else {
                isflag = false;
                btnGangJoinSwitch.setBackgroundResource(R.mipmap.qm_btn_manager_close_approve);
            }
        }

        //提高社群等级
        btnGangImproveLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String levelMessage = "";
                String numberMessage = "";
                if(data == null){
                    return;
                }
                Integer buildlevel = data.getBuildlevel();
                List<ConsortialevellistBean> consortialevellist = GangSDK.getInstance().groupManager().getGameConfigEntity().getConsortialevellist();
                if(buildlevel >= consortialevellist.get(consortialevellist.size()-1).getBuildlevel()){
                    XLToastUtil.showToastShort(GangConfigureUtils.getGangName() + "等级已经达到最高");
                }else {
                    for (int i = 0; i < consortialevellist.size(); i++) {
                        ConsortialevellistBean consortialevelBean = consortialevellist.get(i);
                        if (buildlevel == consortialevelBean.getBuildlevel()) {
                            levelMessage = GangConfigureUtils.getGangName() + "等级： L" + consortialevellist.get(i).getBuildlevel() + ">>>" + "L" + consortialevellist.get(i + 1).getBuildlevel();
                            numberMessage = "人数提升： " + consortialevellist.get(i).getMaxnum() + ">>>" + consortialevellist.get(i + 1).getMaxnum();
                            break;
                        }
                    }
                    new DialogGangImproveLevelFragment()
                            .setMessage(levelMessage, numberMessage)
                            .setCallback(new DialogGangImproveLevelFragment.ImproveLevelCallBack() {
                                @Override
                                public void improveSuccess() {
                                    initData();
                                }
                            })
                            .show(aContext.getFragmentManager());
                }

            }
        });

    }

    @Override
    protected void initView(View view) {
        btnGangApply = (Button) view.findViewById(R.id.btnGangApply);
        btnGangManageProfession = (Button) view.findViewById(R.id.btnGangManageProfession);
        btnGangUpdateName = (Button) view.findViewById(R.id.btnGangUpdateName);
        btnGangImproveLevel = (Button) view.findViewById(R.id.btnGangImproveLevel);
        btnGangEditAnnouncements = (Button) view.findViewById(R.id.btnGangEditAnnouncements);
        btnGangEditDeclaration = (Button) view.findViewById(R.id.btnGangEditDeclaration);
        btnGangDissolve = (Button) view.findViewById(R.id.btnGangDissolve);
        imageGangIcon = (ImageView) view.findViewById(R.id.imageGangIcon);
        btnGangJoinSwitch = (Button) view.findViewById(R.id.btnGangJoinSwitch);
        textGangName = (TextView) view.findViewById(R.id.textGangName);
        textGangLevel = (TextView) view.findViewById(R.id.textGangLevel);
        textGangAnnouncements = (TextView) view.findViewById(R.id.textGangAnnouncements);
        textGangDeclaration = (TextView) view.findViewById(R.id.textGangDeclaration);

        btnGangDissolve.setText("解散" + GangConfigureUtils.getGangName());

        //查看申请列表
        btnGangApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GangModuleManage.toGangApplyListActivity(aContext);
            }
        });

        //职称管理
        btnGangManageProfession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GangModuleManage.toManageProfessionActivity(aContext);
            }
        });

        //修改社群图标
        imageGangIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            new DialogUpdateGangIconFragment()
                    .setOnclickCallBack(new DialogUpdateGangIconFragment.CallbackClick() {
                        @Override
                        public void confirmClick() {
                            UploadHeadUtil.showChoosePhotoMethod(aContext, imageGangIcon, mTakePhoto);
                        }
                    })
                    .show(aContext.getFragmentManager());
            }
        });

        //更改社群名称
        btnGangUpdateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogUpdateGangNameFragment()
                        .setCallback(new DialogUpdateGangNameFragment.UpdateCallBack() {
                            @Override
                            public void updateSuccess() {
                                initData();
                            }
                        })
                        .show(aContext.getFragmentManager());
            }
        });


        //编辑社群公告
        btnGangEditAnnouncements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogEditTipsFragment()
                        .setCallback(new DialogEditTipsFragment.PublishCallBack() {
                            @Override
                            public void publishSuccess() {
                                initData();
                            }
                        }).show(aContext.getFragmentManager());
            }
        });
        //编辑社群宣言
        btnGangEditDeclaration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogEditDeclarationFragment()
                        .setCallback(new DialogEditDeclarationFragment.PublishCallBack() {
                            @Override
                            public void publishSuccess() {
                                initData();
                            }
                        })
                        .show(aContext.getFragmentManager());
            }
        });
        //解散社群
        btnGangDissolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogGangDissolvedFragment().show(aContext.getFragmentManager());
            }
        });

        //设置加入社群是否需要审批
        btnGangJoinSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.d("isflag = " + isflag);
                GangSDK.getInstance().groupManager().changeGangApproveSwitch(!isflag ? 1 : 0, new DataCallBack() {

                    @Override
                    public void onSuccess(int status, String message, Object data) {
                        initData();
                    }

                    @Override
                    public void onFail(String message) {
                        XLToastUtil.showToastShort(message);
                    }
                });
            }
        });
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type=PermissionManager.checkPermission(TContextWrap.of(this),invokeParam.getMethod());
        if(PermissionManager.TPermissionType.WAIT.equals(type)){
            this.mInvokeParam = invokeParam;
        }
        return type;
    }

    @Override
    public void takeSuccess(TResult result) {
        Logger.d("CompressPath:" + result.getImage().getCompressPath());
        if(result.getImage().getCompressPath() != null) {
            loading.show();
            GangSDK.getInstance().groupManager().updateGangIcon(result.getImage().getCompressPath(), new DataCallBack<XLUploadImageBean>() {

                @Override
                public void onSuccess(int status, String message, XLUploadImageBean data) {
                    loading.dismiss();
                    if(data != null) {
                        ImageLoadUtil.loadRoundImage(imageGangIcon, data.getIconurl());
                    }
                }

                @Override
                public void onFail(String message) {
                    loading.dismiss();
                    XLToastUtil.showToastShort(message);
                }

            });
        }

    }

    @Override
    public void takeFail(TResult result, String msg) {
        Logger.d("takeFail:" + msg);
    }

    @Override
    public void takeCancel() {
        Logger.d("取消操作");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     *  获取TakePhoto实例
     * @return
     */
    public TakePhoto getTakePhoto(){
        if (mTakePhoto == null){
            mTakePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this,this));
        }
        return mTakePhoto;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type=PermissionManager.onRequestPermissionsResult(requestCode,permissions,grantResults);
        PermissionManager.handlePermissionsResult(getActivity(), type, mInvokeParam, this);
    }
}
