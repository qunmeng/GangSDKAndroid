package com.qm.gangsdk.ui.view.gangin.members;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.utils.StringUtils;
import com.qm.gangsdk.core.outer.common.entity.XLGangMemberInfoBean;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.utils.BgResourcesUtils;
import com.qm.gangsdk.ui.utils.ImageLoadUtil;
import com.qm.gangsdk.ui.view.common.GangModuleManage;

import java.util.List;

/**
 * Created by shuzhou on 16/2/1.
 */
public class XLDonateListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List list;

    public XLDonateListAdapter(Context context, List list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case 0:
                holder = new ViewTopHolder(LayoutInflater.from(context).inflate(R.layout.item_recyclerview_member_donate_sorttop, parent, false));
                break;
            case 1:
                holder = new ViewOtherHolder(LayoutInflater.from(context).inflate(R.layout.item_recyclerview_member_donate_sortother, parent, false));
                break;
        }
        return holder;
    }


    @Override
    public int getItemViewType(int position) {
        if (position < 3) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final XLGangMemberInfoBean entity = (XLGangMemberInfoBean) list.get(position);
        switch (getItemViewType(position)) {
            case 0:
                final ViewTopHolder topHolder = (ViewTopHolder) holder;
                topHolder.tvNickname.setText(StringUtils.getString(entity.getNickname(), ""));
                ImageLoadUtil.loadRoundImage(topHolder.ivHeader, entity.getIconurl());
                if(entity.getGamelevel() == null || 0 == entity.getGamelevel()){
                    topHolder.tvLevel.setVisibility(View.GONE);
                }else {
                    topHolder.tvLevel.setVisibility(View.VISIBLE);
                    topHolder.tvLevel.setText("Lv." + StringUtils.getString(entity.getGamelevel(), ""));
                }
                topHolder.tvPosition.setText(StringUtils.getString(entity.getRolename(), ""));
                if(entity.getRolelevel() != null) {
                    topHolder.tvPosition.setPadding(5, 2, 5, 2);
                    topHolder.tvPosition.setBackgroundResource(BgResourcesUtils.getPositionBg(entity.getRolelevel()));
                }
                topHolder.tvProfession.setText(StringUtils.getString(entity.getGamerole(), ""));
                topHolder.viewAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GangModuleManage.toMemberInfoActivity(context, entity.getUserid());
                    }
                });
                int resid = R.mipmap.qm_icon_rank1;
                switch (position) {
                    case 0:
                        resid = R.mipmap.qm_icon_rank1;
                        break;
                    case 1:
                        resid = R.mipmap.qm_icon_rank2;
                        break;
                    case 2:
                        resid = R.mipmap.qm_icon_rank3;
                        break;
                }
                topHolder.ivNumPosition.setImageResource(resid);
                topHolder.tvNums.setText(StringUtils.getString(entity.getContributenum(), "0"));

                break;
            case 1:
                final ViewOtherHolder otherHolder = (ViewOtherHolder) holder;
                otherHolder.tvNumPosition.setText(StringUtils.getString(position + 1, ""));
                otherHolder.tvNickname.setText(StringUtils.getString(entity.getNickname(), ""));
                if(entity.getGamelevel() == null || 0 == entity.getGamelevel()){
                    otherHolder.tvLevel.setVisibility(View.GONE);
                }else {
                    otherHolder.tvLevel.setVisibility(View.VISIBLE);
                    otherHolder.tvLevel.setText("Lv." + StringUtils.getString(entity.getGamelevel(), ""));
                }
                otherHolder.tvPosition.setText(StringUtils.getString(entity.getRolename(), ""));
                if(entity.getRolelevel() != null) {
                    otherHolder.tvPosition.setPadding(5, 2, 5, 2);
                    otherHolder.tvPosition.setBackgroundResource(BgResourcesUtils.getPositionBg(entity.getRolelevel()));
                }
                otherHolder.tvProfession.setText(StringUtils.getString(entity.getGamerole(), ""));
                otherHolder.viewAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GangModuleManage.toMemberInfoActivity(context, entity.getUserid());
                    }
                });
                otherHolder.tvNums.setText(StringUtils.getString(entity.getContributenum(), "0"));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewTopHolder extends RecyclerView.ViewHolder {

        View viewAll;
        TextView tvNickname;
        ImageView ivHeader;
        TextView tvLevel;
        TextView tvProfession;
        TextView tvPosition;
        TextView tvNums;
        TextView tvTitleName;
        ImageView ivNumPosition;

        public ViewTopHolder(View view) {
            super(view);
            viewAll = view.findViewById(R.id.viewAll);
            tvNickname = (TextView) view.findViewById(R.id.tvNickname);
            tvProfession = (TextView) view.findViewById(R.id.tvProfession);
            tvPosition = (TextView) view.findViewById(R.id.tvPosition);
            tvNums = (TextView) view.findViewById(R.id.tvNums);
            tvLevel = (TextView) view.findViewById(R.id.tvLevel);
            ivHeader = (ImageView) view.findViewById(R.id.ivHeader);
            tvTitleName = (TextView) view.findViewById(R.id.tvTitleName);
            ivNumPosition = (ImageView) view.findViewById(R.id.ivNumPosition);
        }
    }

    class ViewOtherHolder extends RecyclerView.ViewHolder {

        View viewAll;
        TextView tvNickname;
        TextView tvLevel;
        TextView tvProfession;
        TextView tvPosition;
        TextView tvNums;
        TextView tvTitleName;
        TextView tvNumPosition;

        public ViewOtherHolder(View view) {
            super(view);
            viewAll = view.findViewById(R.id.viewAll);
            tvNickname = (TextView) view.findViewById(R.id.tvNickname);
            tvProfession = (TextView) view.findViewById(R.id.tvProfession);
            tvPosition = (TextView) view.findViewById(R.id.tvPosition);
            tvNums = (TextView) view.findViewById(R.id.tvNums);
            tvLevel = (TextView) view.findViewById(R.id.tvLevel);
            tvTitleName = (TextView) view.findViewById(R.id.tvTitleName);
            tvNumPosition = (TextView) view.findViewById(R.id.tvNumPosition);
        }
    }

}