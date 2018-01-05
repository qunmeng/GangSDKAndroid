package com.qm.gangsdk.ui.view.gangrank;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.utils.StringUtils;
import com.qm.gangsdk.core.outer.common.entity.XLGangListBean;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.view.common.DialogGangInfoFragment;
import com.qm.gangsdk.ui.utils.ImageLoadUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijiyuan on 2017/9/20.
 */

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.MemberSortViewHolder> {


    private Activity context;
    private List<XLGangListBean> list = new ArrayList<>();
    private int type = -1;

    public RankAdapter(Activity context, List list, int type) {
        this.context = context;
        this.list = list;
        this.type = type;
    }


    @Override
    public MemberSortViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MemberSortViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recyclerview_gangrank_sort, parent, false));
    }

    @Override
    public void onBindViewHolder(MemberSortViewHolder holder, int position) {
        final XLGangListBean sortBean = list.get(position);
        ImageLoadUtil.loadRoundImage(holder.imageGangIcon, sortBean.getIconurl());
        if(getItemViewType(position) == 0) {
            switch (position){
                case 0:
                    holder.ivNumPosition.setBackgroundResource(R.mipmap.qm_icon_rank1);
                    break;
                case 1:
                    holder.ivNumPosition.setBackgroundResource(R.mipmap.qm_icon_rank2);
                    break;
                case 2:
                    holder.ivNumPosition.setBackgroundResource(R.mipmap.qm_icon_rank3);
                    break;
            }
        }else {
            holder.ivNumPosition.setBackgroundDrawable(null);
            holder.ivNumPosition.setText(String.valueOf(position + 1));
        }
        holder.textGangLevel.setText(String.valueOf(sortBean.getBuildlevel()) + "级");
        holder.textGangName.setText(StringUtils.getString(sortBean.getConsortianame(), ""));
        holder.textGangDescribe.setText(StringUtils.getString(sortBean.getDeclaration(), ""));
        switch (type){
            case 1:
                holder.tvTitleName.setText(context.getResources().getString(R.string.member_sort_level));
                holder.tvNums.setText(StringUtils.getString(sortBean.getBuildlevel(), "")  + "级");
                break;
            case 2:
                holder.tvTitleName.setText(context.getResources().getString(R.string.member_sort_number));
                holder.tvNums.setText(StringUtils.getString(sortBean.getNownum(), ""));
                break;
            case 3:
                holder.tvTitleName.setText(context.getResources().getString(R.string.member_sort_caifu));
                holder.tvNums.setText(StringUtils.getString(sortBean.getMoneynum(), ""));
                break;
        }

        holder.imageGangIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogGangInfoFragment()
                        .setGangId(sortBean.getConsortiaid().intValue())
                        .show(context.getFragmentManager());
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if(position < 3){
            return 0;
        }else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * ViewOtherHolder
     */
    class MemberSortViewHolder extends RecyclerView.ViewHolder {

        private TextView ivNumPosition;
        private ImageView imageGangIcon;
        private TextView textGangLevel;
        private TextView textGangName;
        private TextView textGangDescribe;
        private TextView tvTitleName;
        private TextView tvNums;

        public MemberSortViewHolder(View view) {
            super(view);
            ivNumPosition = (TextView) view.findViewById(R.id.ivNumPosition);
            imageGangIcon = (ImageView) view.findViewById(R.id.imageGangIcon);
            textGangLevel = (TextView) view.findViewById(R.id.textGangLevel);
            textGangName = (TextView) view.findViewById(R.id.textGangName);
            textGangDescribe = (TextView) view.findViewById(R.id.textGangDescribe);
            tvTitleName = (TextView) view.findViewById(R.id.tvTitleName);
            tvNums = (TextView) view.findViewById(R.id.tvNums);
        }
    }
}
