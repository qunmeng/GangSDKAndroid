package com.qm.gangsdk.ui.view.gangin.task;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qm.gangsdk.core.outer.common.callback.DataCallBack;
import com.qm.gangsdk.core.outer.receiver.GangPosterReceiver;
import com.qm.gangsdk.core.outer.receiver.listener.GangReceiverListener;
import com.qm.gangsdk.core.outer.receiver.base.OnGangReceiverListener;
import com.qm.gangsdk.core.outer.common.entity.XLGangTaskBean;
import com.qm.gangsdk.core.outer.common.entity.XLGangTaskListBean;
import com.qm.gangsdk.ui.GangSDK;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseFragment;
import com.qm.gangsdk.ui.event.XLCompleteTaskEvent;
import com.qm.gangsdk.ui.utils.XLToastUtil;

import java.util.List;

/**
 * 作者：shuzhou on 2017/8/8.
 * 邮箱：810421209@qq.com
 * 版本：v1.0
 *
 * 任务页面
 */
public class GangTaskFragment extends XLBaseFragment {

    private RecyclerView recyclerViewTask;
    private TaskAdapter adapter;

    private GangReceiverListener receiveGangTaskListener;
    private GangReceiverListener completeTaskListener;

    @Override
    protected int getContentView() {
        return R.layout.fragment_gang_task;
    }

    @Override
    protected void initData(){
        GangSDK.getInstance().groupManager().getGangTaskList(new DataCallBack<List<XLGangTaskListBean>>() {
            @Override
            public void onSuccess(int status, String message, List<XLGangTaskListBean> data) {
                if(data != null && !data.isEmpty()){
                    adapter.setData(data);
                }
            }

            @Override
            public void onFail(String message) {
                XLToastUtil.showToastShort(message);
            }

        });
    }

    @Override
    protected void initView(View view) {
        recyclerViewTask = (RecyclerView) view.findViewById(R.id.recyclerViewTask);
        initRecyclerView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //任务完成监听
        completeTaskListener = GangPosterReceiver.addReceiverListener(this, XLCompleteTaskEvent.class, new OnGangReceiverListener<Object>() {
            @Override
            public void onReceived(Object data) {
                initData();
            }
        });

        //新任务监听
        receiveGangTaskListener = GangSDK.getInstance().receiverManager().addReceiveGangTaskListener(this, new OnGangReceiverListener<XLGangTaskBean>() {
            @Override
            public void onReceived(XLGangTaskBean data) {
                initData();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GangSDK.getInstance().receiverManager().removeReceiverListener(receiveGangTaskListener);
        GangSDK.getInstance().receiverManager().removeReceiverListener(completeTaskListener);
    }

    private void initRecyclerView() {
        adapter = new TaskAdapter(aContext);
        recyclerViewTask.setHasFixedSize(false);
        recyclerViewTask.setLayoutManager(new LinearLayoutManager(aContext));
        recyclerViewTask.setAdapter(adapter);
    }
}
