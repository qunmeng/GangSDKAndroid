package com.qm.gangsdk.ui.view.gangcenter.user;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.callback.DataCallBack;
import com.qm.gangsdk.core.outer.common.entity.XLGameInfoBean;
import com.qm.gangsdk.core.outer.common.entity.XLGangMemberInfoBean;
import com.qm.gangsdk.core.outer.common.entity.XLUploadImageBean;
import com.qm.gangsdk.core.outer.common.utils.StringUtils;
import com.qm.gangsdk.core.outer.common.utils.logger.Logger;
import com.qm.gangsdk.ui.GangSDK;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseFragment;
import com.qm.gangsdk.ui.custom.style.SpannableStringStyle;
import com.qm.gangsdk.ui.entity.SpannableStringBean;
import com.qm.gangsdk.ui.utils.BgResourcesUtils;
import com.qm.gangsdk.ui.utils.DensityUtil;
import com.qm.gangsdk.ui.utils.ImageLoadUtil;
import com.qm.gangsdk.ui.utils.ScreenSizeUtil;
import com.qm.gangsdk.ui.utils.UploadHeadUtil;
import com.qm.gangsdk.ui.utils.XLToastUtil;
import com.qm.gangsdk.ui.view.common.DialogGameDownloadOrStartFragment;
import com.qm.gangsdk.ui.view.common.DialogGangInfoFragment;
import com.qm.gangsdk.ui.view.common.GangConfigureUtils;
import com.qm.gangsdk.ui.view.gangin.members.DialogMemberinfoHintFragment;

import java.util.ArrayList;
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
 * Created by lijiyuan on 2017/12/5.
 *
 * 个人中心
 */

public class GangCenterUserFragment extends XLBaseFragment implements TakePhoto.TakeResultListener,InvokeListener {

    private InvokeParam mInvokeParam;
    private TakePhoto mTakePhoto;
    private ImageButton btnMenuLeft;
    private TextView tvTitle;
    private ImageView ivHeader;
    private TextView tvLevel;
    private TextView tvNickname;
    private TextView tvProfession;
    private TextView tvGangName;
    private TextView tvPosition;
    private TextView tvContributeWeekNum;
    private TextView tvContributeAllNum;
    private TextView tvActiveNum;
    private Button btnGangDynamic;
    private Button btnGangInfo;
    private View viewPosition;

    private RecyclerView recyclerViewGame;
    private PlayingGameAdapter adapter;
    private List<XLGameInfoBean> gameInfoList = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.fragment_gangcenter_user;
    }

    @Override
    protected void initData() {
        if(GangSDK.getInstance().userManager().getXlUserBean().getUserid() == null) {
            return;
        }
        loading.show();
        GangSDK.getInstance().membersManager().getGangMemberInfo(GangSDK.getInstance().userManager().getXlUserBean().getUserid(), new DataCallBack<XLGangMemberInfoBean>() {
            @Override
            public void onSuccess(int status, String message, XLGangMemberInfoBean data) {
                loading.dismiss();
                if (data != null) {
                    bindData(data);
                    if (data.getPlaygamelist() == null || data.getPlaygamelist().isEmpty()) {
                        return;
                    }
                    gameInfoList.clear();
                    gameInfoList.addAll(data.getPlaygamelist());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFail(String message) {
                loading.dismiss();
                XLToastUtil.showToastShort(message);
            }
        });
    }

    @Override
    protected void initView(View view) {
        btnMenuLeft = (ImageButton) view.findViewById(R.id.btnMenuLeft);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvTitle.setText(aContext.getResources().getString(R.string.gang_user_center));

        ivHeader = (ImageView) view.findViewById(R.id.ivHeader);
        tvLevel = (TextView) view.findViewById(R.id.tvLevel);
        tvNickname = (TextView) view.findViewById(R.id.tvNickname);
        tvProfession = (TextView) view.findViewById(R.id.tvProfession);
        tvGangName = (TextView) view.findViewById(R.id.tvGangName);
        tvPosition = (TextView) view.findViewById(R.id.tvPosition);
        tvContributeWeekNum = (TextView) view.findViewById(R.id.tvContributeWeekNum);
        tvContributeAllNum = (TextView) view.findViewById(R.id.tvContributeAllNum);
        tvActiveNum = (TextView) view.findViewById(R.id.tvActiveNum);
        btnGangDynamic = (Button) view.findViewById(R.id.btnGangDynamic);
        btnGangInfo = (Button) view.findViewById(R.id.btnGangInfo);
        recyclerViewGame = (RecyclerView) view.findViewById(R.id.recyclerViewGame);
        viewPosition = view.findViewById(R.id.viewPosition);
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerViewGame.setHasFixedSize(false);
        recyclerViewGame.setLayoutManager(new GridLayoutManager(aContext, 4));
        recyclerViewGame.setNestedScrollingEnabled(true);
        adapter = new PlayingGameAdapter();
        recyclerViewGame.setAdapter(adapter);

        recyclerViewGame.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int screenWidth = ScreenSizeUtil.getScreenWidth(aContext);
                int rectPadding = (screenWidth - (DensityUtil.dip2px(aContext, 360)))/8;
                outRect.left = rectPadding;
                outRect.right= rectPadding;
            }
        });
    }


    /**
     * 绑定界面数据
     * @param data
     */
    private void bindData(final XLGangMemberInfoBean data) {
        ImageLoadUtil.loadRoundImage(ivHeader, data.getIconurl());
        tvNickname.setText(StringUtils.getString(data.getNickname(), ""));
        if(data.getRolelevel() != null) {
            tvPosition.setPadding(5, 2, 5, 2);
            tvPosition.setBackgroundResource(BgResourcesUtils.getPositionBg(data.getRolelevel()));
        }
        if(data.getGamelevel() == null || 0 == data.getGamelevel()){
            tvLevel.setVisibility(View.GONE);
        }else {
            tvLevel.setVisibility(View.VISIBLE);
            tvLevel.setText("Lv." + StringUtils.getString(data.getGamelevel(), ""));
        }
        tvContributeAllNum.setText("总贡献:" + data.getContributenum());
        tvActiveNum.setText("活跃度:" + data.getActivenum());
        tvContributeWeekNum.setText("周贡献:" + data.getWeekcontributenum());

        if(data.getConsortiaid() != null && data.getConsortiaid().intValue() > 0){
            viewPosition.setVisibility(View.VISIBLE);
            btnGangInfo.setVisibility(View.VISIBLE);
            tvProfession.setText(StringUtils.getString(data.getGamerole(), ""));
            tvPosition.setText(StringUtils.getString(data.getRolename(), ""));
            tvGangName.setText(StringUtils.getString(data.getConsortianame(), ""));
        }else {
            viewPosition.setVisibility(View.GONE);
            btnGangInfo.setVisibility(View.INVISIBLE);
        }

        //修改图像
        ivHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if(data.getUserid() != null)
                    showUploadHeadDialog(data.getUserid().intValue());
            }
        });
    }

    private void showUploadHeadDialog(int userid) {
        if (GangConfigureUtils.isAllowUploadUserHead()) {
            if(GangSDK.getInstance().userManager().getXlUserBean().getUserid() == null) {
                return;
            }
            if (userid == GangSDK.getInstance().userManager().getXlUserBean().getUserid().intValue()) {
                if(!StringUtils.isEmpty(GangSDK.getInstance().groupManager().getGameConfigEntity().getGameprompt().getModify_user_icon())){
                    String message = GangSDK.getInstance().groupManager().getGameConfigEntity().getGameprompt().getModify_user_icon();
                    List<SpannableStringBean> list = new ArrayList<>();
                    list.add(new SpannableStringBean("\\{\\$moneyname\\$\\}", GangConfigureUtils.getMoneyName(), ContextCompat.getColor(aContext, R.color.xlmemberinfo_dialog_text_gold_color)));
                    SpannableStringBuilder spannableStringBuilder = SpannableStringStyle.buildStyleReplaceParts(message, list, ContextCompat.getColor(aContext, R.color.xlmemberinfo_dialog_text_content_color));
                    showDialogMemberinfoHint(aContext, spannableStringBuilder, new DialogMemberinfoHintFragment.CallbackOnclick() {
                        @Override
                        public void confirm() {
                            UploadHeadUtil.showChoosePhotoMethod(aContext, ivHeader, mTakePhoto);
                        }

                        @Override
                        public void cancel() {
                        }
                    });
                }
            }
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnMenuLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aContext.finish();
            }
        });

        btnGangInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GangSDK.getInstance().userManager().getXlUserBean().getConsortiaid() != null) {
                    new DialogGangInfoFragment()
                            .setGangId(GangSDK.getInstance().userManager().getXlUserBean().getConsortiaid().intValue())
                            .show(aContext.getFragmentManager());
                }else {
                    XLToastUtil.showToastShort("您还没有加入" + GangConfigureUtils.getGangName());
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
        if(result.getImage().getCompressPath() != null) {
            Logger.e("path = " + result.getImage().getCompressPath());
            loading.show();
            GangSDK.getInstance().userManager().updateHeadIcon(result.getImage().getCompressPath(), new DataCallBack<XLUploadImageBean>() {
                @Override
                public void onSuccess(int status, String message, final XLUploadImageBean data) {
                    loading.dismiss();
                    Logger.e("iconurl = " + data.getIconurl() );
                    if(data != null){
                        ImageLoadUtil.loadRoundImage(ivHeader, data.getIconurl());
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


    /**
     * 在玩游戏adapter
     */
    class PlayingGameAdapter extends RecyclerView.Adapter<PlayingGameViewHolder>{

        @Override
        public PlayingGameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PlayingGameViewHolder(LayoutInflater.from(aContext).inflate(R.layout.item_recyclerview_usercenter_game, parent, false));
        }

        @Override
        public void onBindViewHolder(PlayingGameViewHolder holder, int position) {
            final XLGameInfoBean gameinfo = gameInfoList.get(position);
            ImageLoadUtil.loadRoundImage(holder.imageGameIcon, gameinfo.getAppicon());
            holder.textGameName.setText(StringUtils.getString(gameinfo.getAppname(), ""));
            holder.viewParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(gameinfo.getAndroidpackage() != null) {
                        if (gameinfo.getAndroidpackage().equals(GangSDK.getInstance().getApplication().getPackageName())) {
                            XLToastUtil.showToastShort(aContext.getResources().getString(R.string.xlgame_playing));
                            return;
                        }
                    }
                    if(gameinfo != null ) {
                        new DialogGameDownloadOrStartFragment()
                                .setGameInfo(gameinfo)
                                .show(aContext.getFragmentManager());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return gameInfoList.size();
        }
    }

    class PlayingGameViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageGameIcon;
        private TextView textGameName;
        private View viewParent;
        public PlayingGameViewHolder(View itemView) {
            super(itemView);
            imageGameIcon = (ImageView) itemView.findViewById(R.id.imageGameIcon);
            textGameName = (TextView) itemView.findViewById(R.id.textGameName);
            viewParent = itemView.findViewById(R.id.viewParent);
        }
    }

    /***
     * 显示提示信息弹窗
     * @param activity              Activity
     * @param message               提示信息
     * @param callbackOnclick       回调
     */
    public static void showDialogMemberinfoHint(Activity activity, CharSequence message, DialogMemberinfoHintFragment.CallbackOnclick callbackOnclick) {
        new DialogMemberinfoHintFragment()
                .setMessage(message)
                .setOnclickCallBack(callbackOnclick)
                .show(activity.getFragmentManager());
    }
}
