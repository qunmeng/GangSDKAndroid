package com.qm.gangsdk.ui.view.gangin.members;


import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.utils.StringUtils;
import com.qm.gangsdk.ui.utils.BgResourcesUtils;
import com.qm.gangsdk.ui.utils.TimeUtils;
import com.qm.gangsdk.core.outer.common.entity.XLGangMemberInfoBean;
import com.qm.gangsdk.ui.GangSDK;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.view.common.GangConfigureUtils;
import com.qm.gangsdk.ui.utils.ImageLoadUtil;
import com.qm.gangsdk.ui.view.common.GangModuleManage;

import java.util.List;

/**
 * Created by shuzhou on 16/2/1.
 */
public class XLMemberAdapter extends RecyclerView.Adapter<XLMemberAdapter.ViewHolder> {
    private Context context;
    private List list;
    public Integer type;

    public XLMemberAdapter(Context context, List list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recyclerview_member, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final XLGangMemberInfoBean entity = (XLGangMemberInfoBean) list.get(position);
        holder.tvNickname.setText(StringUtils.getString(entity.getNickname(), ""));
        holder.viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(entity.getUserid() != null){
                    GangModuleManage.toMemberInfoActivity(context, entity.getUserid().intValue());
                }
            }
        });
        ImageLoadUtil.loadRoundImage(holder.ivHeader, entity.getIconurl());
        holder.btnOut.setText("退出" + GangConfigureUtils.getGangName());
        holder.btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogQuitGangFragment().show(((Activity)context).getFragmentManager());
            }
        });
        holder.tvNickname.setText(StringUtils.getString(entity.getNickname(), ""));
        if(entity.getGamelevel() == null || 0 == entity.getGamelevel()){
            holder.tvLevel.setVisibility(View.GONE);
        }else {
            holder.tvLevel.setVisibility(View.VISIBLE);
            holder.tvLevel.setText("Lv." + StringUtils.getString(entity.getGamelevel(), ""));
        }
        holder.tvAtribute.setText("周贡献:" + StringUtils.getString(entity.getWeekcontributenum(), ""));
        holder.tvPosition.setText(StringUtils.getString(entity.getRolename(), ""));
        holder.tvPosition.setPadding(5, 2, 5, 2);
        if(entity.getRolelevel() != null) {
            holder.tvPosition.setBackgroundResource(BgResourcesUtils.getPositionBg(entity.getRolelevel()));
        }
        holder.tvProfession.setText(StringUtils.getString(entity.getGamerole(), ""));
        if(entity.getIsforbiddenspeak() != null){
            if(entity.getIsforbiddenspeak().intValue() > 0){
                holder.ivNotalk.setVisibility(View.VISIBLE);
            }else {
                holder.ivNotalk.setVisibility(View.GONE);
            }
        }
        if (entity.getUserid().intValue() == GangSDK.getInstance().userManager().getXlUserBean().getUserid().intValue()) {
            holder.ivIsme.setVisibility(View.VISIBLE);
            holder.btnOut.setVisibility(View.VISIBLE);
        } else {
            holder.ivIsme.setVisibility(View.GONE);
            holder.btnOut.setVisibility(View.GONE);
        }
        if(entity.getIsonline().equals(1)){
            holder.tvTime.setText("在线");
        }else {
            holder.tvTime.setText(TimeUtils.dateStringToOnlineTimeFormat(entity.getLastlogintime()));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        View viewAll;
        TextView tvNickname;
        Button btnOut;
        ImageView ivHeader;
        TextView tvLevel;
        TextView ivIsme;
        TextView ivNotalk;
        TextView tvProfession;
        TextView tvPosition;
        TextView tvAtribute;
        TextView tvTime;

        public ViewHolder(View view) {
            super(view);
            viewAll = view.findViewById(R.id.viewAll);
            tvNickname = (TextView) view.findViewById(R.id.tvNickname);
            tvProfession = (TextView) view.findViewById(R.id.tvProfession);
            tvPosition = (TextView) view.findViewById(R.id.tvPosition);
            tvAtribute = (TextView) view.findViewById(R.id.tvNums);
            tvLevel = (TextView) view.findViewById(R.id.tvLevel);
            btnOut = (Button) view.findViewById(R.id.btnOut);
            ivHeader = (ImageView) view.findViewById(R.id.ivHeader);
            ivIsme = (TextView) view.findViewById(R.id.ivIsme);
            ivNotalk = (TextView) view.findViewById(R.id.ivNotalk);
            tvTime = (TextView) view.findViewById(R.id.tvTime);
        }
    }

}