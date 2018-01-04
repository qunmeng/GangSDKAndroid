package com.qm.gangsdk.ui.view.gangin.members;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.callback.DataCallBack;
import com.qm.gangsdk.core.outer.common.utils.StringUtils;
import com.qm.gangsdk.ui.utils.BgResourcesUtils;
import com.qm.gangsdk.ui.utils.TimeUtils;
import com.qm.gangsdk.ui.utils.XLToastUtil;
import com.qm.gangsdk.core.outer.common.entity.XLGangMemberInfoBean;
import com.qm.gangsdk.ui.GangSDK;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.utils.ImageLoadUtil;
import com.qm.gangsdk.ui.view.common.GangModuleManage;

import java.util.List;

/**
 * Created by shuzhou on 16/2/1.
 */
public class XLMemberMuteAdapter extends RecyclerView.Adapter<XLMemberMuteAdapter.ViewHolder> {
    private Context context;
    private List<XLGangMemberInfoBean> list;


    public XLMemberMuteAdapter(Context context, List<XLGangMemberInfoBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recyclerview_member_notalk, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final XLGangMemberInfoBean entity = list.get(position);
        holder.tvNickname.setText(StringUtils.getString(entity.getNickname(), ""));
        holder.viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GangModuleManage.toMemberInfoActivity(context, entity.getUserid());
            }
        });
        ImageLoadUtil.loadRoundImage(holder.ivHeader, entity.getIconurl());
        holder.btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        GangSDK.getInstance().membersManager().cancelMuteMember(entity.getUserid().toString(), new DataCallBack() {
                            @Override
                            public void onSuccess(int status, String message, Object data) {
                                list.remove(entity);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onFail(String message) {
                                XLToastUtil.showToastShort(message);
                            }
                        });
                    }
        });
        holder.tvNickname.setText(StringUtils.getString(entity.getNickname(), ""));
        if(entity.getGamelevel() == null || 0 == entity.getGamelevel()){
            holder.tvLevel.setVisibility(View.GONE);
        }else {
            holder.tvLevel.setVisibility(View.VISIBLE);
            holder.tvLevel.setText("Lv." + StringUtils.getString(entity.getGamelevel(), ""));
        }
        holder.tvPosition.setText(StringUtils.getString(entity.getRolename(), ""));
        if(entity.getRolelevel() != null) {
            holder.tvPosition.setPadding(5, 2, 5, 2);
            holder.tvPosition.setBackgroundResource(BgResourcesUtils.getPositionBg(entity.getRolelevel()));
        }
        holder.tvProfession.setText(StringUtils.getString(entity.getGamerole(), ""));
        holder.tvTime.setText("剩余：" + TimeUtils.cntTimeDifference(entity.getOvertime()));
        if (entity.getUserid().intValue() == GangSDK.getInstance().userManager().getXlUserBean().getUserid().intValue()) {
            holder.ivIsme.setVisibility(View.VISIBLE);
        } else {
            holder.ivIsme.setVisibility(View.GONE);
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
        TextView tvProfession;
        TextView tvPosition;
        TextView tvTime;

        public ViewHolder(View view) {
            super(view);
            viewAll = view.findViewById(R.id.viewAll);
            tvNickname = (TextView) view.findViewById(R.id.tvNickname);
            tvProfession = (TextView) view.findViewById(R.id.tvProfession);
            tvPosition = (TextView) view.findViewById(R.id.tvPosition);
            tvLevel = (TextView) view.findViewById(R.id.tvLevel);
            btnOut = (Button) view.findViewById(R.id.btnOut);
            ivHeader = (ImageView) view.findViewById(R.id.ivHeader);
            ivIsme = (TextView) view.findViewById(R.id.ivIsme);
            tvTime = (TextView) view.findViewById(R.id.tvTime);
        }
    }

}