package com.qm.gangsdk.ui.view.gangin.task;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.callback.DataCallBack;
import com.qm.gangsdk.core.outer.common.entity.XLGangTaskBean;
import com.qm.gangsdk.core.outer.common.entity.XLGangTaskListBean;
import com.qm.gangsdk.core.outer.common.utils.ListUtils;
import com.qm.gangsdk.core.outer.common.utils.StringUtils;
import com.qm.gangsdk.core.outer.receiver.GangPosterReceiver;
import com.qm.gangsdk.ui.GangSDK;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.custom.SectionedRecyclerViewAdapter;
import com.qm.gangsdk.ui.custom.dialog.ViewTools;
import com.qm.gangsdk.ui.event.XLCompleteTaskEvent;
import com.qm.gangsdk.ui.utils.ImageLoadUtil;
import com.qm.gangsdk.ui.utils.XLToastUtil;

import java.util.List;


/**
 * Created by lijiyuan on 2017/8/28
 * 任务adapter
 */

public class TaskAdapter extends SectionedRecyclerViewAdapter<TaskAdapter.HeaderHolder, TaskAdapter.DescHolder, RecyclerView.ViewHolder> {

    public List<XLGangTaskListBean> listData;
    private Context mContext;
    private LayoutInflater mInflater;

    private SparseBooleanArray mBooleanMap;

    public TaskAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mBooleanMap = new SparseBooleanArray();
    }

    public void setData(List<XLGangTaskListBean> listData) {
        this.listData = listData;
        notifyDataSetChanged();
    }

    @Override
    protected int getSectionCount() {
        return ListUtils.isEmpty(listData) ? 0 : listData.size();
    }

    @Override
    protected int getItemCountForSection(int section) {
        int count = listData.get(section).getGroupitems().size();
        return ListUtils.isEmpty(listData.get(section).getGroupitems()) ? 0 : count;
    }

    //是否有footer布局
    @Override
    protected boolean hasFooterInSection(int section) {
        return true;
    }

    @Override
    protected HeaderHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        return new HeaderHolder(mInflater.inflate(R.layout.item_recyclerview_gang_task_title, parent, false));
    }


    @Override
    protected RecyclerView.ViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return new HeaderHolder(mInflater.inflate(R.layout.item_recyclerview_gang_task_footer, parent, false));

    }

    @Override
    protected DescHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new DescHolder(mInflater.inflate(R.layout.item_recyclerview_gang_task, parent, false));
    }

    @Override
    protected void onBindSectionHeaderViewHolder(final HeaderHolder holder, final int section) {
        holder.textTaskGroupTitle.setText(listData.get(section).getGroupname());
    }

    @Override
    protected void onBindSectionFooterViewHolder(RecyclerView.ViewHolder holder, int section) {

    }

    @Override
    protected void onBindItemViewHolder(final DescHolder holder, int section, int position) {

        if(getItemCountForSection(section) == 1){
            holder.ivUnderline.setVisibility(View.GONE);
            holder.viewParent.setBackgroundResource(R.drawable.qm_bg_gangtask_bottom_blue);
        }else {
            switch (position % 2){
                case 0:
                    if(position == 0){
                        holder.viewParent.setBackgroundResource(R.drawable.qm_bg_gangtask_top);
                        holder.ivUnderline.setVisibility(View.VISIBLE);
                    }else if(position == (getItemCountForSection(section)-1)) {
                        holder.viewParent.setBackgroundResource(R.drawable.qm_bg_gangtask_bottom_blue);
                        holder.ivUnderline.setVisibility(View.GONE);
                    }else {
                        holder.viewParent.setBackgroundResource(R.drawable.qm_bg_gangtask_center_blue);
                        holder.ivUnderline.setVisibility(View.VISIBLE);
                    }
                    break;
                case 1:
                    if(position == (getItemCountForSection(section)-1)){
                        holder.viewParent.setBackgroundResource(R.drawable.qm_bg_gangtask_bottom_blue_dark);
                        holder.ivUnderline.setVisibility(View.GONE);
                    }else {
                        holder.viewParent.setBackgroundResource(R.drawable.qm_bg_gangtask_center_blue_dark);
                        holder.ivUnderline.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }

        final XLGangTaskBean taskBean = listData.get(section).getGroupitems().get(position);
        ImageLoadUtil.loadRoundImage(holder.imageTaskIcon, taskBean.getTaskiconurl());
        holder.textTaskName.setText(taskBean.getTasktitle());
        holder.textTaskDescribe.setText(StringUtils.getString(taskBean.getTaskdesc(), ""));
        holder.textTaskRewards.setText(StringUtils.getString(taskBean.getRewarddesc(), ""));
        holder.textTaskSchedule.setText(taskBean.getNownum() + "/" + taskBean.getNeednum());

        switch (taskBean.getStatus()){
            case 0:
                holder.btnTaskComplete.setText(mContext.getResources().getString(R.string.gang_task_sign_btn));
                holder.btnTaskComplete.setBackgroundResource(R.mipmap.qm_btn_gangtask_sign);
                holder.btnTaskComplete.setTextColor(ContextCompat.getColor(mContext, R.color.xlgangtask_item_button_sign_color));
                holder.btnTaskComplete.setEnabled(true);
                break;
            case 1:
                holder.btnTaskComplete.setText(mContext.getResources().getString(R.string.gang_task_receive_btn));
                holder.btnTaskComplete.setBackgroundResource(R.mipmap.qm_btn_gangtask_sign);
                holder.btnTaskComplete.setTextColor(ContextCompat.getColor(mContext, R.color.xlgangtask_item_button_sign_color));
                holder.btnTaskComplete.setEnabled(true);
                break;
            case 2:
                holder.btnTaskComplete.setText(mContext.getResources().getString(R.string.gang_task_doing_btn));
                holder.btnTaskComplete.setBackgroundResource(R.mipmap.qm_btn_gangtask_doing);
                holder.btnTaskComplete.setTextColor(ContextCompat.getColor(mContext, R.color.xlgangtask_item_button_doing_color));
                holder.btnTaskComplete.setEnabled(false);
                break;
            case 3:
                holder.btnTaskComplete.setText(mContext.getResources().getString(R.string.gang_task_complete_btn));
                holder.btnTaskComplete.setBackgroundResource(R.mipmap.qm_btn_gangtask_finished);
                holder.btnTaskComplete.setTextColor(ContextCompat.getColor(mContext, R.color.xlgangtask_item_button_finished_color));
                holder.btnTaskComplete.setEnabled(false);
                break;
        }

        switch (taskBean.getCycletype()){
            case 1:
                holder.textTaskType.setVisibility(View.VISIBLE);
                holder.textTaskType.setText(mContext.getResources().getString(R.string.gang_task_day_text));
                holder.textTaskType.setTextColor(ContextCompat.getColor(mContext, R.color.xlgangtask_item_text_daily_color));
                holder.textTaskType.setBackgroundResource(R.mipmap.qm_bg_gangtask_daily);
                break;
            case 2:
                holder.textTaskType.setVisibility(View.VISIBLE);
                holder.textTaskType.setText(mContext.getResources().getString(R.string.gang_task_week_text));
                holder.textTaskType.setTextColor(ContextCompat.getColor(mContext, R.color.xlgangtask_item_text_week_color));
                holder.textTaskType.setBackgroundResource(R.mipmap.qm_bg_gangtask_week);
                break;
            case 3:
            case 4:
                holder.textTaskType.setVisibility(View.GONE);
                break;
        }

        holder.btnTaskComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog loading = ViewTools.createLoadingDialog(mContext,"正在提交数据...", false);
                loading.show();
                GangSDK.getInstance().groupManager().dealTask(
                        taskBean.getTasktype(), taskBean.getTaskid(), new DataCallBack() {

                            @Override
                            public void onSuccess(int status, String message, Object data) {
                                loading.dismiss();
                                XLToastUtil.showToastShort(message);
                                GangPosterReceiver.post(new XLCompleteTaskEvent());
                            }

                            @Override
                            public void onFail(String message) {
                                loading.dismiss();
                                XLToastUtil.showToastShort(message);
                            }
                        }
                );
            }
        });
    }

    public static class DescHolder extends RecyclerView.ViewHolder {

        private View viewParent;
        private ImageView imageTaskIcon;
        private TextView textTaskName;
        private TextView textTaskDescribe;
        private TextView textTaskRewards;
        private TextView textTaskSchedule;
        private TextView textTaskType;
        private Button btnTaskComplete;
        private ImageView ivUnderline;

        public DescHolder(View view) {
            super(view);
            viewParent = view.findViewById(R.id.viewParent);
            imageTaskIcon = (ImageView) view.findViewById(R.id.imageTaskIcon);
            textTaskName = (TextView) view.findViewById(R.id.textTaskName);
            textTaskDescribe = (TextView) view.findViewById(R.id.textTaskDescribe);
            textTaskRewards = (TextView) view.findViewById(R.id.textTaskRewards);
            textTaskSchedule = (TextView) view.findViewById(R.id.textTaskSchedule);
            textTaskType = (TextView) view.findViewById(R.id.textTaskType);
            btnTaskComplete = (Button) view.findViewById(R.id.btnTaskComplete);
            ivUnderline = (ImageView) view.findViewById(R.id.ivUnderline);
        }
    }

    public static class HeaderHolder extends RecyclerView.ViewHolder {
        public TextView textTaskGroupTitle;

        public HeaderHolder(View itemView) {
            super(itemView);
            textTaskGroupTitle = (TextView) itemView.findViewById(R.id.textTaskGroupTitle);
        }
    }
}
