package com.qm.gangsdk.ui.view.gangout.create;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.callback.DataCallBack;
import com.qm.gangsdk.ui.utils.DensityUtil;
import com.qm.gangsdk.core.outer.common.utils.StringUtils;
import com.qm.gangsdk.ui.utils.XLToastUtil;
import com.qm.gangsdk.core.outer.common.entity.XLGangInfoBean;
import com.qm.gangsdk.ui.GangSDK;;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseFragment;
import com.qm.gangsdk.ui.view.common.GangConfigureUtils;
import com.qm.gangsdk.ui.utils.ImageLoadUtil;
import com.qm.gangsdk.ui.utils.XLActivityManager;
import com.qm.gangsdk.ui.view.common.GangModuleManage;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijiyuan on 2017/8/8.
 * 创建社群fragment
 */

public class GangCreateFragment extends XLBaseFragment {

    private EditText editGangName;
    private EditText editGangDeclaration;
    private TextView textGangRule;
    private ImageButton btnMenuLeft;
    private ImageButton btnMenuRight;
    private TextView tvTitle;
    private ImageView imageGangIconPreview;

    private RecyclerView recyclerViewGangIcon;
    private IconAdapter adpater;

    private String iconurl = null;
    private List<String> listicon = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.fragment_create_gang;
    }

    @Override
    protected void initData() {
        listicon.clear();
        JSONArray consortia_icons_list = GangSDK.getInstance().groupManager().getGameConfigEntity().getGameconfig().getConsortia_icons();
        if (consortia_icons_list != null) {
            try {
                for (int i = 0; i < consortia_icons_list.length(); i++) {
                    listicon.add(consortia_icons_list.get(i).toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        adpater.notifyDataSetChanged();

        if(!StringUtils.isEmpty(GangSDK.getInstance().groupManager().getGameConfigEntity().getGameprompt().getCreate_consortia())) {
            String create_consortia = GangSDK.getInstance().groupManager().getGameConfigEntity().getGameprompt().getCreate_consortia();
            create_consortia = create_consortia.replaceAll("\\{\\$gangname\\$\\}", GangConfigureUtils.getGangName());
            textGangRule.setText("规则：" + create_consortia);
        }
    }

    @Override
    protected void initView(View view) {
        btnMenuLeft = (ImageButton) view.findViewById(R.id.btnMenuLeft);
        btnMenuRight = (ImageButton) view.findViewById(R.id.btnMenuRight);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        editGangName = (EditText) view.findViewById(R.id.editGangName);
        editGangDeclaration = (EditText) view.findViewById(R.id.editGangDeclaration);
        textGangRule = (TextView) view.findViewById(R.id.textGangRule);
        recyclerViewGangIcon = (RecyclerView) view.findViewById(R.id.recyclerViewGangIcon);
        imageGangIconPreview = (ImageView) view.findViewById(R.id.imageGangIconPreview);

        btnMenuRight.setVisibility(View.VISIBLE);
        btnMenuRight.setImageResource(R.mipmap.qm_btn_creategang);
        tvTitle.setText("创建" + GangConfigureUtils.getGangName());

        initRecyclerView();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        onIconAdapterClickListener = new OnIconAdapterItemClickListener() {
            @Override
            public void OnItemClick(int position, String url) {
                adpater.selectedPosition = position; //选择的position赋值给参数
                adpater.notifyDataSetChanged();
                iconurl = StringUtils.getString(url, "");
                ImageLoadUtil.loadRoundImage(imageGangIconPreview, url);
            }
        };

        //初始化点击事件
        onIconAdapterClickListener.OnItemClick(0, listicon.get(0));

        btnMenuLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aContext.finish();
            }
        });

        btnMenuRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creatGang();
            }
        });

    }

    private void initRecyclerView() {
        recyclerViewGangIcon.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(aContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewGangIcon.setLayoutManager(linearLayoutManager);
        adpater = new IconAdapter();
        recyclerViewGangIcon.setAdapter(adpater);

        recyclerViewGangIcon.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.left = DensityUtil.dip2px(aContext, 5);
                outRect.right = DensityUtil.dip2px(aContext, 5);
            }
        });
    }

    //创建社群
    private void creatGang() {
        String gangName = editGangName.getText().toString().trim();
        String gangDeclaration = editGangDeclaration.getText().toString().trim();

        if(StringUtils.isEmpty(gangName)){
            XLToastUtil.showToastShort(GangConfigureUtils.getGangName() + "名称不能为空");
            return;
        }else if(StringUtils.getChineseLength(gangName) > 14){
            XLToastUtil.showToastShort(GangConfigureUtils.getGangName() + "名称不能超过七个汉字");
            return;
        }

        if(StringUtils.getChineseLength(gangDeclaration) > 60){
            XLToastUtil.showToastShort(GangConfigureUtils.getGangName() + "宣言内容不能超过三十个汉字");
            return;
        }

        if(StringUtils.isEmpty(iconurl)){
            XLToastUtil.showToastShort("请选择一张图片");
            return;
        }
        loading.show();
        GangSDK.getInstance().groupManager().createGang(gangName, iconurl, gangDeclaration,
                new DataCallBack<XLGangInfoBean>() {

                    @Override
                    public void onSuccess(int status, String message, XLGangInfoBean data) {
                        loading.dismiss();
                        XLToastUtil.showToastShort("创建" + GangConfigureUtils.getGangName() + "成功");
                        XLActivityManager.getInstance().finishAllActivity();
                        GangModuleManage.toInGangTabActivity(aContext);
                    }
                    @Override
                    public void onFail(String message) {
                        loading.dismiss();
                        XLToastUtil.showToastShort(message);
                    }
                });
    }

    /**
     * 社群图标adpter
     */
    class IconAdapter extends RecyclerView.Adapter<IconViewHolder>{

        private int selectedPosition = 0; //默认一个参数

        @Override
        public IconViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new IconViewHolder(LayoutInflater.from(aContext).inflate(R.layout.item_recyclerview_icon, parent, false));
        }

        @Override
        public void onBindViewHolder(IconViewHolder holder, final int position) {
            ImageLoadUtil.loadRoundImage(holder.imageGangIcon, listicon.get(position));

            holder.imageGangIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onIconAdapterClickListener != null){
                        onIconAdapterClickListener.OnItemClick(position, listicon.get(position));
                    }
                }
            });

            if(selectedPosition == position){
                holder.imageGangIconBg.setImageResource(R.mipmap.qm_bg_creategang_icon_selected);
            }else {
                holder.imageGangIconBg.setImageResource(R.mipmap.qm_bg_creategang_icon_normal);
            }
        }

        @Override
        public int getItemCount() {
            return listicon.size();
        }
    }

    class IconViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageGangIcon;
        private ImageView imageGangIconBg;
        public IconViewHolder(View itemView) {
            super(itemView);
            imageGangIcon = (ImageView) itemView.findViewById(R.id.imageGangIcon);
            imageGangIconBg = (ImageView) itemView.findViewById(R.id.imageGangIconBg);
        }
    }

    //定义接口，实现Recyclerview点击事件
    private OnIconAdapterItemClickListener onIconAdapterClickListener;

    public  interface OnIconAdapterItemClickListener {
        void OnItemClick(int position, String iconUrl);
    }
}
