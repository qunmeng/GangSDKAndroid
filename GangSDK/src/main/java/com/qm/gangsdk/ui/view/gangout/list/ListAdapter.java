package com.qm.gangsdk.ui.view.gangout.list;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.callback.DataCallBack;
import com.qm.gangsdk.ui.utils.XLToastUtil;
import com.qm.gangsdk.core.outer.common.entity.XLGangInfoBean;
import com.qm.gangsdk.core.outer.common.entity.XLGangListBean;
import com.qm.gangsdk.ui.GangSDK;;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.custom.dialog.ViewTools;
import com.qm.gangsdk.ui.utils.ImageLoadUtil;
import com.qm.gangsdk.ui.utils.XLActivityManager;
import com.qm.gangsdk.ui.view.common.GangModuleManage;
import com.qm.gangsdk.ui.view.common.DialogGangInfoFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijiyuan on 2017/8/4.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.CommonViewHolder> {


    private Activity context;
    private List<XLGangListBean> list = new ArrayList<>();
    public ListAdapter(Activity context, List list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_ganglist, parent, false);
        CommonViewHolder holder = new CommonViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        final XLGangListBean sortBean = list.get(position);

        holder.textGangNumber.setText(String.valueOf((position+ 1)));
        ImageLoadUtil.loadRoundImage(holder.imageGangIcon, sortBean.getIconurl());
        holder.textGangLevel.setText(String.valueOf(sortBean.getBuildlevel())+ "级");
        holder.textGangName.setText(sortBean.getConsortianame());
        ImageLoadUtil.loadRoundImage(holder.imageGangLabel, sortBean.getLabelurl());
        holder.textGangDescribe.setText(sortBean.getDeclaration());
        holder.textGangPeopleNumber.setText(String.valueOf(sortBean.getNownum()));
        holder.relateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                new DialogGangInfoFragment()
                        .setGangId(sortBean.getConsortiaid().intValue())
                        .show(context.getFragmentManager());
            }
        });
        holder.btnGangApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gangApplyJoin(sortBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * ViewOtherHolder
     */
    class CommonViewHolder extends ViewHolder{

        private View relateView;
        private TextView textGangNumber;
        private ImageView imageGangIcon;
        private TextView textGangLevel;
        private TextView textGangName;
        private ImageView imageGangLabel;
        private TextView textGangDescribe;
        private TextView textGangPeopleNumber;
        private Button btnGangApply;

        public CommonViewHolder(View view) {
            super(view);
            relateView = view.findViewById(R.id.relateSortView);
            textGangNumber = (TextView) view.findViewById(R.id.textGangNumber);
            imageGangIcon = (ImageView) view.findViewById(R.id.imageGangIcon);
            textGangLevel = (TextView) view.findViewById(R.id.textGangLevel);
            textGangName = (TextView) view.findViewById(R.id.textGangName);
            imageGangLabel = (ImageView) view.findViewById(R.id.imageGangLabel);
            textGangDescribe = (TextView) view.findViewById(R.id.textGangDescribe);
            textGangPeopleNumber = (TextView) view.findViewById(R.id.textGangPeopleNumber);
            btnGangApply = (Button) view.findViewById(R.id.btnGangApply);
        }
    }

    /**
     * 申请加入
     * @param sortBean
     */
    private void gangApplyJoin(XLGangListBean sortBean) {
        final Dialog loading = ViewTools.createLoadingDialog(context,"正在提交数据...", false);
        loading.show();
        GangSDK.getInstance().userManager().applyJoinGang(sortBean.getConsortiaid(), "", new DataCallBack<XLGangInfoBean>() {

            @Override
            public void onSuccess(int status, String message, XLGangInfoBean data) {
                loading.dismiss();
                if (data != null) {
                    XLActivityManager.getInstance().finishAllActivity();
                    GangModuleManage.toInGangTabActivity(context);
                } else {
                    XLToastUtil.showToastShort(message);
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

