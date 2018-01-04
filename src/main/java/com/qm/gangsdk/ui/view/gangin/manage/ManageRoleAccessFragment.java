package com.qm.gangsdk.ui.view.gangin.manage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.callback.DataCallBack;
import com.qm.gangsdk.core.outer.common.entity.XLGangAccessInfoBean;
import com.qm.gangsdk.core.outer.common.entity.XLGangRoleBean;
import com.qm.gangsdk.core.outer.common.entity.XLManageRoleAccessBean;
import com.qm.gangsdk.core.outer.common.utils.StringUtils;
import com.qm.gangsdk.ui.GangSDK;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseFragment;
import com.qm.gangsdk.ui.utils.XLToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lijiyuan on 2017/9/12.
 * 职称管理页
 */

public class ManageRoleAccessFragment extends XLBaseFragment {

    private TextView tvTitle;
    private ImageButton btnMenuLeft;
    private ImageButton btnMenuRight;
    private Button btnProfessionSetSave;
    private Button btnProfessionSetCancel;
    private View viewAccessNull;
    private View viewAceessList;
    private View viewScroll;

    private RecyclerView recyclerViewRole;
    private RecyclerView recyclerViewAccess;
    private RoleAdapter roleAdapter;
    private AccessAdapter accessAdapter;

    private Integer mCanedit;
    private List<XLGangRoleBean> roleList = new ArrayList<>();
    private List<XLGangAccessInfoBean> accessList = new ArrayList<>();
    private Map<Integer,JSONArray>  resultMap = new HashMap<>();


    @Override
    protected int getContentView() {
        return R.layout.fragment_manage_roleaccess;
    }

    private void bindRecyclerView() {
        roleAdapter = new RoleAdapter();
        accessAdapter = new AccessAdapter();

        recyclerViewRole.setHasFixedSize(false);
        recyclerViewRole.setFocusable(false);
        recyclerViewRole.setLayoutManager(new LinearLayoutManager(aContext));
        recyclerViewRole.setNestedScrollingEnabled(false);
        recyclerViewRole.setAdapter(roleAdapter);

        recyclerViewAccess.setHasFixedSize(false);
        recyclerViewRole.setFocusable(false);
        recyclerViewAccess.setLayoutManager(new LinearLayoutManager(aContext));
        recyclerViewRole.setNestedScrollingEnabled(false);
        recyclerViewAccess.setAdapter(accessAdapter);

    }

    @Override
    protected void initData() {
        roleList.clear();
        accessList.clear();
        GangSDK.getInstance().groupManager().getGangRoleAndAccess(new DataCallBack<XLManageRoleAccessBean>() {

            @Override
            public void onSuccess(int status, String message, XLManageRoleAccessBean data) {
                viewAccessNull.setVisibility(View.GONE);
                viewAceessList.setVisibility(View.VISIBLE);
                if(data != null ){
                    mCanedit = data.getCanedit();
                    if(data.getRoleinfo() != null && !data.getRoleinfo().isEmpty()) {
                        roleList.addAll(data.getRoleinfo());
                        for (XLGangRoleBean roleinfo : roleList) {
                            resultMap.put(roleinfo.getRoleid(), roleinfo.getAccesslist());
                        }
                        accessList.addAll(data.getAccessinfo());
                        roleAdapter.notifyDataSetChanged();
                        accessAdapter.notifyDataSetChanged();
                        onRoleAdapterClickListener.OnItemClick(0);
                    }

                    if(hasEditPermission()){
                        btnProfessionSetSave.setVisibility(View.VISIBLE);
                        btnProfessionSetCancel.setVisibility(View.VISIBLE);
                    }else{
                        btnProfessionSetSave.setVisibility(View.GONE);
                        btnProfessionSetCancel.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFail(String message) {
                viewAccessNull.setVisibility(View.VISIBLE);
                viewAceessList.setVisibility(View.GONE);
                XLToastUtil.showToastShort(message);
            }
        });
    }

    @Override
    protected void initView(View view) {
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        btnMenuLeft = (ImageButton) view.findViewById(R.id.btnMenuLeft);
        btnMenuRight = (ImageButton) view.findViewById(R.id.btnMenuRight);
        btnProfessionSetSave = (Button) view.findViewById(R.id.btnProfessionSetSave);
        btnProfessionSetCancel = (Button) view.findViewById(R.id.btnProfessionSetCancel);
        recyclerViewRole = (RecyclerView) view.findViewById(R.id.recyclerViewRole);
        recyclerViewAccess = (RecyclerView) view.findViewById(R.id.recyclerViewAccess);
        viewAccessNull = view.findViewById(R.id.viewAccessNull);
        viewAceessList = view.findViewById(R.id.viewAceessList);
        viewScroll = view.findViewById(R.id.viewScroll);
        viewScroll.setFocusable(true);
        viewScroll.setFocusableInTouchMode(true);
        viewScroll.requestFocus();

        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(aContext.getResources().getString(R.string.manage_role_access_title));
        btnMenuRight.setVisibility(View.GONE);
        btnMenuLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aContext.finish();
            }
        });
        btnProfessionSetCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aContext.finish();
            }
        });

        btnProfessionSetSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String accesslist = resultMapToString();
                if(!StringUtils.isEmpty(accesslist)){
                    loading.show();
                    GangSDK.getInstance().groupManager().updateGangAccess(accesslist,
                            new DataCallBack() {
                                @Override
                                public void onSuccess(int status, String message, Object data) {
                                    loading.dismiss();
                                    aContext.finish();
                                }

                                @Override
                                public void onFail(String message) {
                                    loading.dismiss();
                                    XLToastUtil.showToastShort(message);
                                }
                            });
                }
            }
        });

        bindRecyclerView();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onRoleAdapterClickListener = new OnRoleAdapterItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                roleAdapter.selectedPosition = position; //选择的position赋值给参数，
                roleAdapter.notifyDataSetChanged();
                if(roleList == null || roleList.isEmpty()){
                    return;
                }
                XLGangRoleBean roleInfoBean = roleList.get(position);
                updateAccessAdapterView(roleInfoBean.getAccesslist(), roleInfoBean.getRoleid(), roleInfoBean.getCanedit());
            }
        };
    }

    /**
     * 更新权限界面
     * @param accessArray
     * @param roleid
     */
    private void updateAccessAdapterView( JSONArray accessArray, int roleid, int canedit) {
        List<Integer> list = new ArrayList<>();
        if (accessArray != null) {
            for (int i = 0; i < accessArray.length(); i++) {
                try {
                    Integer id = (Integer) accessArray.get(i);
                    list.add(id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        if (list != null && accessList != null) {
            List<XLGangAccessInfoBean> aslist = new ArrayList<>();
            for (XLGangAccessInfoBean accessInfoBean : accessList) {
                if(canedit == 1){
                    accessInfoBean.setIsonclick(true);
                }else {
                    accessInfoBean.setIsonclick(false);
                }
                accessInfoBean.setChecked(false);
                accessInfoBean.setRoleid(roleid);
                for (Integer id : list) {
                    if (accessInfoBean.getAccessid() == id) {
                        accessInfoBean.setChecked(true);
                        break;
                    }
                }
                aslist.add(accessInfoBean);
            }
            accessList.clear();
            accessList.addAll(aslist);
        }
        accessAdapter.notifyDataSetChanged();
    }

    /**
     * 职称适配器
     */
    class RoleAdapter extends RecyclerView.Adapter<RoleViewHolder>{

        private int selectedPosition = 0; //默认一个参数

        @Override
        public RoleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RoleViewHolder(LayoutInflater.from(aContext).inflate(R.layout.item_recyclerview_role, parent, false));
        }

        @Override
        public void onBindViewHolder(final RoleViewHolder holder, final int position) {
            final XLGangRoleBean roleInfoBean = roleList.get(position);

            holder.textRoleName.setText(roleInfoBean.getRolename());

            if (selectedPosition == position) {
                holder.textRoleName.setBackgroundResource(R.drawable.qm_bg_roleaccess_role_selected);
                holder.textRoleName.setPadding(10, 5, 40, 5);
                holder.textRoleName.setTextColor(ContextCompat.getColor(aContext, R.color.xlroleaccess_item_role_text_selected_color));
            } else {
                holder.textRoleName.setBackgroundDrawable(null);
                holder.textRoleName.setPadding(10, 5, 40, 5);
                holder.textRoleName.setTextColor(ContextCompat.getColor(aContext, R.color.xlroleaccess_item_role_text_normal_color));
            }

            holder.viewPrent.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (onRoleAdapterClickListener != null) {
                        onRoleAdapterClickListener.OnItemClick(position);
                    }
                    return false;
                }
            });

            holder.imageRoleEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedPosition == position) {
                        if(hasEditPermission()){
                            new DialogUpdateRoleNameFragment()
                                    .setRoleId(roleInfoBean.getRoleid())
                                    .setCallbackClick(new DialogUpdateRoleNameFragment.CallbackClick() {
                                        @Override
                                        public void updateSuccess(String roloname) {
                                            roleInfoBean.setRolename(StringUtils.getString(roloname, ""));
                                            roleAdapter.notifyDataSetChanged();
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
            return roleList.size();
        }


    }

    class RoleViewHolder extends RecyclerView.ViewHolder{
        private TextView textRoleName;
        private ImageView imageRoleEdit;
        private View viewPrent;

        public RoleViewHolder(View itemView) {
            super(itemView);
            textRoleName = (TextView) itemView.findViewById(R.id.textRoleName);
            imageRoleEdit = (ImageView) itemView.findViewById(R.id.imageRoleEdit);
            viewPrent = itemView.findViewById(R.id.viewPrent);
        }
    }

    /**
     * 权限适配器
     */
    class AccessAdapter extends RecyclerView.Adapter<AccessViewHolder>{

        @Override
        public AccessViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new AccessViewHolder(LayoutInflater.from(aContext).inflate(R.layout.item_recyclerview_access, parent, false));
        }

        @Override
        public void onBindViewHolder(final AccessViewHolder holder,final int position) {
            final XLGangAccessInfoBean accessInfoBean = accessList.get(position);
            holder.textAccessName.setText(accessInfoBean.getAccessdesc());

            if(hasEditPermission()){
                if(accessInfoBean.isChecked()){
                    if(accessInfoBean.isonclick()){
                        holder.imageSetAccess.setEnabled(true);
                        holder.imageSetAccess.setImageResource(R.mipmap.qm_btn_roleaccess_access_selected);
                    }else {
                        holder.imageSetAccess.setEnabled(false);
                        holder.imageSetAccess.setImageResource(R.mipmap.qm_btn_roleaccess_access_selected_dark);
                    }
                }else {
                    if(accessInfoBean.isonclick()){
                        holder.imageSetAccess.setEnabled(true);
                        holder.imageSetAccess.setImageResource(R.mipmap.qm_btn_roleaccess_access_normal);
                    }else {
                        holder.imageSetAccess.setEnabled(false);
                        holder.imageSetAccess.setImageResource(R.mipmap.qm_btn_roleaceess_access_normal_dark);
                    }
                }
            }else{
                if(accessInfoBean.isChecked()){
                    holder.imageSetAccess.setEnabled(false);
                    holder.imageSetAccess.setImageResource(R.mipmap.qm_btn_roleaccess_access_selected_dark);
                }else {
                    holder.imageSetAccess.setEnabled(false);
                    holder.imageSetAccess.setImageResource(R.mipmap.qm_btn_roleaceess_access_normal_dark);
                }
            }

            holder.imageSetAccess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(accessInfoBean.isChecked()){
                        accessInfoBean.setChecked(false);
                        accessList.set(position,accessInfoBean);
                        JSONArray accessIdsArray = resultMap.get(accessInfoBean.getRoleid());
                        JSONArray accessIdsArrayTemp = new JSONArray();
                        if (accessIdsArray != null) {
                            try {
                                for (int i = 0; i < accessIdsArray.length(); i++) {
                                    Integer id = (Integer) accessIdsArray.get(i);
                                    if (id != accessInfoBean.getAccessid()) {
                                        accessIdsArrayTemp.put(id);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        resultMap.put(accessInfoBean.getRoleid(), accessIdsArrayTemp);
                    }else {
                        accessInfoBean.setChecked(true);
                        accessList.set(position,accessInfoBean);
                        JSONArray accessIdsArray = resultMap.get(accessInfoBean.getRoleid());
                        if(accessIdsArray == null){
                            JSONArray newAccessIdsArraynew = new JSONArray();
                            resultMap.put(accessInfoBean.getRoleid(), newAccessIdsArraynew.put(accessInfoBean.getAccessid()));
                        }else {
                            resultMap.put(accessInfoBean.getRoleid(), accessIdsArray.put(accessInfoBean.getAccessid()));
                        }
                    }
                    accessAdapter.notifyDataSetChanged();
                }
            });


        }

        @Override
        public int getItemCount() {
            return accessList.size();
        }
    }

    class AccessViewHolder extends RecyclerView.ViewHolder {
        private TextView textAccessName;
        private ImageView imageSetAccess;

        public AccessViewHolder(View itemView) {
            super(itemView);
            textAccessName = (TextView) itemView.findViewById(R.id.textAccessName);
            imageSetAccess = (ImageView) itemView.findViewById(R.id.imageSetAccess);
        }
    }

    //定义接口，实现Recyclerview点击事件
    private OnRoleAdapterItemClickListener onRoleAdapterClickListener;

    public  interface OnRoleAdapterItemClickListener {
        void OnItemClick(int position);
    }

    /**
     * 数据格式转换
     * @return
     */
    public String resultMapToString(){
        JSONObject jsonObject = new JSONObject();
        if(resultMap != null) {
            for (Integer key : resultMap.keySet()) {
                try {
                    jsonObject.put(key + "", resultMap.get(key));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println(jsonObject.toString());
        }
        return  jsonObject.toString();
    }

    public boolean hasEditPermission(){
        return (mCanedit != null && mCanedit.intValue() == 1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
