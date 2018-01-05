package com.qm.gangsdk.ui.view.gangcenter.game;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.callback.DataCallBack;
import com.qm.gangsdk.core.outer.common.entity.XLGameInfoBean;
import com.qm.gangsdk.core.outer.common.utils.StringUtils;
import com.qm.gangsdk.ui.GangSDK;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseFragment;
import com.qm.gangsdk.ui.utils.ImageLoadUtil;
import com.qm.gangsdk.ui.utils.XLApkUtils;
import com.qm.gangsdk.ui.utils.XLToastUtil;
import com.qm.gangsdk.ui.view.common.DialogNetStatesHintFragment;
import com.xl.views.ptr.PtrClassicFrameLayout;
import com.xl.views.ptr.PtrDefaultHandler;
import com.xl.views.ptr.PtrFrameLayout;
import com.xl.views.ptr.PtrHandler;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lijiyuan on 2017/12/5.
 *
 * 游戏中心
 */

public class GangCenterGameFragment extends XLBaseFragment {

    private ImageButton btnMenuLeft;
    private TextView tvTitle;
    private RecyclerView recyclerViewGame;
    private GameCenterAdapter adapter;
    private PtrClassicFrameLayout ptrFrameLayout;

    private List<XLGameInfoBean> gameInfoList = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.fragment_gangcenter_game;
    }

    @Override
    protected void initData() {
        GangSDK.getInstance().gameManager().getGameList(new DataCallBack<List<XLGameInfoBean>>() {
            @Override
            public void onSuccess(int status, String message, List<XLGameInfoBean> data) {
                ptrFrameLayout.refreshComplete();
                if(data != null && !data.isEmpty()){
                    gameInfoList.clear();
                    gameInfoList.addAll(data);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFail(String message) {
                ptrFrameLayout.refreshComplete();
                XLToastUtil.showToastShort(message);
            }
        });
    }

    @Override
    protected void initView(View view) {
        btnMenuLeft = (ImageButton) view.findViewById(R.id.btnMenuLeft);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        recyclerViewGame = (RecyclerView) view.findViewById(R.id.recyclerViewGame);
        ptrFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptrFrameLayout);
        tvTitle.setText(aContext.getResources().getString(R.string.gang_game_center));
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerViewGame.setHasFixedSize(false);
        recyclerViewGame.setLayoutManager(new LinearLayoutManager(aContext));
        adapter = new GameCenterAdapter();
        recyclerViewGame.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnMenuLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aContext.finish();
            }
        });

        ptrFrameLayout.setLastUpdateTimeRelateObject(this);
        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                initData();
            }
        });

        ptrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrFrameLayout.autoRefresh(true);
            }
        }, 150);
    }

    /**
     * 游戏adapter
     */
    class GameCenterAdapter extends RecyclerView.Adapter<GameCenterViewHolder>{

        @Override
        public GameCenterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new GameCenterViewHolder(LayoutInflater.from(aContext).inflate(R.layout.item_recyclerview_game_center, parent, false));
        }

        @Override
        public void onBindViewHolder(GameCenterViewHolder holder, int position) {
            final XLGameInfoBean gameInfoBean = gameInfoList.get(position);
            ImageLoadUtil.loadRoundImage(holder.imageGameIcon, gameInfoBean.getAppicon());
            holder.textGameName.setText(StringUtils.getString(gameInfoBean.getAppname(), ""));
            holder.textGameDes.setText(StringUtils.getString(gameInfoBean.getAppinfo(), ""));

            final boolean isInstall = XLApkUtils.checkApkExist(aContext, gameInfoBean.getAndroidpackage());
            if(isInstall){
                holder.btnGameDownload.setText("启动");
                holder.btnGameDownload.setBackgroundResource(R.mipmap.qm_btn_gamecenter_start);
                holder.btnGameDownload.setTextColor(ContextCompat.getColor(aContext, R.color.xlgamecenter_item_button_start_color));
            }else {
                holder.btnGameDownload.setText("下载");
                holder.btnGameDownload.setBackgroundResource(R.mipmap.qm_btn_gamecenter_download);
                holder.btnGameDownload.setTextColor(ContextCompat.getColor(aContext, R.color.xlgamecenter_item_button_download_color));
            }

            holder.btnGameDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isInstall){
                        XLApkUtils.startApk(aContext, gameInfoBean.getAndroidpackage());
                    }else {
                        if(StringUtils.isEmpty(gameInfoBean.getAndroiddownloadurl())){
                            XLToastUtil.showToastShort("下载地址不能为空");
                        }else {
                            new DialogNetStatesHintFragment()
                                    .setOnclickCallBack(new DialogNetStatesHintFragment.CallbackOnclick() {
                                        @Override
                                        public void confirm() {
                                            try {
                                                XLApkUtils.downloadApk(aContext, gameInfoBean.getAndroiddownloadurl());
                                            } catch (Exception e) {
                                                XLToastUtil.showToastShort("下载地址格式错误");
                                                e.printStackTrace();
                                            }
                                        }
                                    })
                            .show(aContext.getFragmentManager());
                        }
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return gameInfoList.size();
        }
    }

    /**
     * 游戏ViewHolder
     */
    class GameCenterViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageGameIcon;
        private TextView textGameName;
        private TextView textGameDes;
        private Button btnGameDownload;
        public GameCenterViewHolder(View itemView) {
            super(itemView);
            imageGameIcon = (ImageView) itemView.findViewById(R.id.imageGameIcon);
            textGameName = (TextView) itemView.findViewById(R.id.textGameName);
            textGameDes = (TextView) itemView.findViewById(R.id.textGameDes);
            btnGameDownload = (Button) itemView.findViewById(R.id.btnGameDownload);
        }
    }
}
