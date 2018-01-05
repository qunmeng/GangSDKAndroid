package com.qm.gangsdk.ui.view.gangin.members;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.callback.DataCallBack;
import com.qm.gangsdk.core.outer.common.entity.XLGameInfoBean;
import com.qm.gangsdk.core.outer.common.entity.XLGangMemberInfoBean;
import com.qm.gangsdk.core.outer.common.entity.XLGangRoleEntityBean;
import com.qm.gangsdk.core.outer.common.utils.StringUtils;
import com.qm.gangsdk.core.outer.receiver.GangPosterReceiver;
import com.qm.gangsdk.ui.GangSDK;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseFragment;
import com.qm.gangsdk.ui.custom.radiogroup.FlowRadioGroup;
import com.qm.gangsdk.ui.custom.style.SpannableStringStyle;
import com.qm.gangsdk.ui.entity.SpannableStringBean;
import com.qm.gangsdk.ui.event.XLKickUserSuccessEvent;
import com.qm.gangsdk.ui.utils.BgResourcesUtils;
import com.qm.gangsdk.ui.utils.DensityUtil;
import com.qm.gangsdk.ui.utils.ImageLoadUtil;
import com.qm.gangsdk.ui.utils.ScreenSizeUtil;
import com.qm.gangsdk.ui.utils.XLToastUtil;
import com.qm.gangsdk.ui.view.common.DialogGameDownloadOrStartFragment;
import com.qm.gangsdk.ui.view.common.DialogGangInfoFragment;
import com.qm.gangsdk.ui.view.common.GangConfigureUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import static com.qm.gangsdk.ui.view.gangin.members.DialogMemberinfoHintFragment.CallbackOnclick;

/**
 * 作者：shuzhou on 2017/8/21.
 * 邮箱：810421209@qq.com
 * 版本：v1.0
 */
public class MemberInfoFragment extends XLBaseFragment{

    private ImageButton btnMenuLeft;
    private Button btnKick;
    private Button btnChange;
    private TextView tvTitle;
    private TextView tvNickname;
    private TextView tvContributeAllNum;
    private TextView tvActiveNum;
    private TextView tvLevel;
    private TextView tvMemberRole;
    private TextView tvPosition;
    private TextView tvContributeWeekNum;
    private TextView tvGangName;
    private FlowRadioGroup radioGroupNotalk;
    private FlowRadioGroup radioGroupPosition;
    private ImageView ivHeader;
    private Button btnGangDynamic;
    private Button btnGangInfo;
    private Button btnInvite;
    private RecyclerView recyclerViewGame;
    private PlayingGameAdapter adapter;
    private View viewMute;
    private View viewPosition;
    private View viewKickParent;
    private View viewContributeUnderline;
    private View viewGangExist;

    private int memberid;
    private List<XLGameInfoBean> gameInfoList = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.fragment_members_info;
    }

    @Override
    protected void initData() {
        memberid = getArguments().getInt("memberid");

        loading.show();
        viewGangExist.setVisibility(View.GONE);
        GangSDK.getInstance().membersManager().getGangMemberInfo(memberid, new DataCallBack<XLGangMemberInfoBean>() {
            @Override
            public void onSuccess(int status, String message, XLGangMemberInfoBean data) {
                loading.dismiss();
                viewGangExist.setVisibility(View.VISIBLE);
                if(data != null) {
                    bindData(data);
                    if(data.getPlaygamelist() == null || data.getPlaygamelist().isEmpty()){
                        return;
                    }
                    gameInfoList.clear();
                    gameInfoList.addAll(data.getPlaygamelist());
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFail(String message) {
                loading.dismiss();
                viewGangExist.setVisibility(View.VISIBLE);
                XLToastUtil.showToastShort(message);
            }
        });
    }

    @Override
    protected void initView(View view) {
        btnMenuLeft = (ImageButton) view.findViewById(R.id.btnMenuLeft);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvTitle.setText("玩家信息");
        btnKick = (Button) view.findViewById(R.id.btnKick);
        btnChange = (Button) view.findViewById(R.id.btnChange);
        ivHeader = (ImageView) view.findViewById(R.id.ivHeader);
        tvNickname = (TextView) view.findViewById(R.id.tvNickname);
        tvContributeAllNum = (TextView) view.findViewById(R.id.tvContributeAllNum);
        tvActiveNum = (TextView) view.findViewById(R.id.tvActiveNum);
        tvLevel = (TextView) view.findViewById(R.id.tvLevel);
        tvContributeWeekNum = (TextView) view.findViewById(R.id.tvContributeWeekNum);
        tvMemberRole = (TextView) view.findViewById(R.id.tvMemberRole);
        tvPosition = (TextView) view.findViewById(R.id.tvPosition);
        radioGroupNotalk = (FlowRadioGroup) view.findViewById(R.id.radioGroupNotalk);
        radioGroupPosition = (FlowRadioGroup) view.findViewById(R.id.radioGroupPosition);
        btnGangDynamic = (Button) view.findViewById(R.id.btnGangDynamic);
        btnGangInfo = (Button) view.findViewById(R.id.btnGangInfo);
        tvGangName = (TextView) view.findViewById(R.id.tvGangName);
        recyclerViewGame = (RecyclerView) view.findViewById(R.id.recyclerViewGame);
        btnInvite = (Button) view.findViewById(R.id.btnInvite);
        viewMute = view.findViewById(R.id.viewMute);
        viewPosition = view.findViewById(R.id.viewPosition);
        viewKickParent = view.findViewById(R.id.viewKickParent);
        viewContributeUnderline = view.findViewById(R.id.viewContributeUnderline);
        viewGangExist = view.findViewById(R.id.viewGangExist);

        btnKick.setText("踢出" + GangConfigureUtils.getGangName());
        btnChange.setText(GangConfigureUtils.getGangName() + "转让");
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerViewGame.setHasFixedSize(false);
        recyclerViewGame.setLayoutManager(new GridLayoutManager(aContext, 4));
        recyclerViewGame.setNestedScrollingEnabled(true);
        adapter = new PlayingGameAdapter();
        recyclerViewGame.setAdapter(adapter);

        recyclerViewGame.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int screenWidth = ScreenSizeUtil.getScreenWidth(aContext);
                int rectPadding = (screenWidth - (DensityUtil.dip2px(aContext, 360)))/8;
                outRect.left = rectPadding;
                outRect.right= rectPadding;
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnMenuLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aContext.finish();
            }
        });
    }

    /**
     * 绑定数据
     * @param data
     */
    private void bindData(final XLGangMemberInfoBean data) {
        ImageLoadUtil.loadRoundImage(ivHeader, data.getIconurl());
        if(data.getGamelevel() == null || 0 == data.getGamelevel()){
            tvLevel.setVisibility(View.GONE);
        }else {
            tvLevel.setVisibility(View.VISIBLE);
            tvLevel.setText("Lv." + StringUtils.getString(data.getGamelevel(), ""));
        }
        tvMemberRole.setText(StringUtils.getString(data.getGamerole(), ""));
        tvNickname.setText(StringUtils.getString(data.getNickname(), ""));

        //没有社群
        if(data.getConsortiaid() == null || data.getConsortiaid() <= 0){
            tvGangName.setVisibility(View.GONE);
            tvPosition.setVisibility(View.GONE);
            btnInvite.setVisibility(View.VISIBLE);
            btnGangInfo.setVisibility(View.GONE);

            viewMute.setVisibility(View.GONE);
            viewPosition.setVisibility(View.GONE);
            viewContributeUnderline.setVisibility(View.GONE);
            viewKickParent.setVisibility(View.GONE);

            tvContributeAllNum.setText("总贡献:0");
            tvActiveNum.setText("活跃度:0");
            tvContributeWeekNum.setText("周贡献:0");

            btnInvite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loading.show();
                    GangSDK.getInstance().membersManager().inviteMember(data.getNickname(), new DataCallBack() {
                        @Override
                        public void onSuccess(int status, String message, Object data) {
                            loading.dismiss();
                            XLToastUtil.showToastShort("邀请成功");
                        }

                        @Override
                        public void onFail(String message) {
                            loading.dismiss();
                            XLToastUtil.showToastShort(message);
                        }
                    });
                }
            });
        }else {
            tvGangName.setVisibility(View.VISIBLE);
            tvPosition.setVisibility(View.VISIBLE);
            btnInvite.setVisibility(View.GONE);
            btnGangInfo.setVisibility(View.VISIBLE);

            tvPosition.setText(StringUtils.getString(data.getRolename(), ""));
            if(data.getRolelevel() != null) {
                tvPosition.setPadding(5, 2, 5, 2);
                tvPosition.setBackgroundResource(BgResourcesUtils.getPositionBg(data.getRolelevel()));
            }
            tvContributeAllNum.setText("总贡献:" + data.getContributenum());
            tvActiveNum.setText("活跃度:" + data.getActivenum());
            tvContributeWeekNum.setText("周贡献:" + data.getWeekcontributenum());
            tvGangName.setText(StringUtils.getString(data.getConsortianame(), ""));

            btnGangInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data.getConsortiaid() != null) {
                        new DialogGangInfoFragment()
                                .setGangId(data.getConsortiaid().intValue())
                                .show(aContext.getFragmentManager());
                    }
                }
            });

            Integer consortiaID = GangSDK.getInstance().userManager().getXlUserBean().getConsortiaid();
            if(consortiaID != null && data.getConsortiaid().intValue() == consortiaID.intValue()){
                //同一个社群
                viewMute.setVisibility(View.VISIBLE);
                viewPosition.setVisibility(View.VISIBLE);
                viewContributeUnderline.setVisibility(View.VISIBLE);
                viewKickParent.setVisibility(View.VISIBLE);

                btnKick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!StringUtils.isEmpty(GangSDK.getInstance().groupManager().getGameConfigEntity().getGameprompt().getKick_user())) {
                            String message = GangSDK.getInstance().groupManager().getGameConfigEntity().getGameprompt().getKick_user();
                            //多样风格字符串
                            List<SpannableStringBean> list = new ArrayList<SpannableStringBean>();
                            list.add(new SpannableStringBean("\\{\\$nickname\\$\\}", data.getNickname(), ContextCompat.getColor(aContext, R.color.xlmemberinfo_dialog_text_nickname_color)));
                            list.add(new SpannableStringBean("\\{\\$gangname\\$\\}", GangConfigureUtils.getGangName(), ContextCompat.getColor(aContext, R.color.xlmemberinfo_dialog_text_content_color)));
                            SpannableStringBuilder spannableStringBuilder = SpannableStringStyle.buildStyleReplaceParts(message, list, ContextCompat.getColor(aContext, R.color.xlmemberinfo_dialog_text_content_color));
                            showDialogMemberinfoHint(aContext, spannableStringBuilder, new CallbackOnclick() {
                                @Override
                                public void confirm() {
                                    loading.show();
                                    GangSDK.getInstance().membersManager().kickOutMember(memberid, "辣鸡", new DataCallBack() {
                                        @Override
                                        public void onSuccess(int status, String message, Object data) {
                                            GangPosterReceiver.post(new XLKickUserSuccessEvent());
                                            aContext.finish();
                                            XLToastUtil.showToastShort("踢出成功");
                                            loading.dismiss();
                                        }

                                        @Override
                                        public void onFail(String message) {
                                            XLToastUtil.showToastShort(message);
                                            loading.dismiss();
                                        }
                                    });
                                }

                                @Override
                                public void cancel() {
                                }
                            });
                        }
                    }
                });

                //会长转让
                btnChange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!StringUtils.isEmpty(GangSDK.getInstance().groupManager().getGameConfigEntity().getGameprompt().getChange_master())){
                            String message = GangSDK.getInstance().groupManager().getGameConfigEntity().getGameprompt().getChange_master();
                            //多样风格字符串
                            List<SpannableStringBean> list = new ArrayList<SpannableStringBean>();
                            list.add(new SpannableStringBean("\\{\\$nickname\\$\\}", data.getNickname(), ContextCompat.getColor(aContext, R.color.xlmemberinfo_dialog_text_nickname_color)));
                            list.add(new SpannableStringBean("\\{\\$gangowner\\$\\}", GangConfigureUtils.getGangName(), ContextCompat.getColor(aContext, R.color.xlmemberinfo_dialog_text_content_color)));
                            SpannableStringBuilder spannableStringBuilder = SpannableStringStyle.buildStyleReplaceParts(message, list, ContextCompat.getColor(aContext, R.color.xlmemberinfo_dialog_text_content_color));
                            showDialogMemberinfoHint(aContext, spannableStringBuilder, new CallbackOnclick() {
                                @Override
                                public void confirm() {
                                    loading.show();
                                    GangSDK.getInstance().membersManager().transferGangOwner(
                                            data.getUserid(), new DataCallBack() {
                                                @Override
                                                public void onSuccess(int status, String message, Object data) {
                                                    XLToastUtil.showToastShort("操作成功");
                                                    initData();
                                                    loading.dismiss();
                                                }

                                                @Override
                                                public void onFail(String message) {
                                                    XLToastUtil.showToastShort(message);
                                                    loading.dismiss();
                                                }
                                            });
                                }

                                @Override
                                public void cancel() {
                                }
                            });
                        }
                    }
                });
                addNoTalkRadioButton(data, initTalkForbbidenData());
                addPositionRadioButton(data);

            }else {         //不同社群
                viewMute.setVisibility(View.GONE);
                viewPosition.setVisibility(View.GONE);
                viewContributeUnderline.setVisibility(View.GONE);
                viewKickParent.setVisibility(View.GONE);
            }
        }

    }

    private List<Integer> initTalkForbbidenData() {
        List<Integer> list = new ArrayList<>();
        JSONArray speak_forbbiden_time_list = GangSDK.getInstance().groupManager().getGameConfigEntity().getGameconfig().getSpeak_forbbiden_time_list();
        if (speak_forbbiden_time_list != null) {
            list.add(0);
            try {
                for (int i = 0; i < speak_forbbiden_time_list.length(); i++) {
                    Integer time = Integer.valueOf(speak_forbbiden_time_list.get(i).toString()) ;
                    list.add(time);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private void addNoTalkRadioButton(final XLGangMemberInfoBean data,final  List listTime) {
        if(data == null || listTime == null || listTime.isEmpty()){
            return;
        }
        radioGroupNotalk.removeAllViews();
        for (int i = 0; i < listTime.size(); i++) {
            final int timeseconds = (int) listTime.get(i);
            timeToString(timeseconds);
            final RadioButton radioButton = new RadioButton(aContext);
            final RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(DensityUtil.dip2px(aContext, 56), DensityUtil.dip2px(aContext, 24));
            layoutParams.setMargins(DensityUtil.dip2px(aContext, 5), DensityUtil.dip2px(aContext, 5), DensityUtil.dip2px(aContext, 5), DensityUtil.dip2px(aContext, 5));
            radioButton.setLayoutParams(layoutParams);
            radioButton.setText(timeToString(timeseconds));
            radioButton.setTextSize(12);
            ColorStateList csl = ContextCompat.getColorStateList(aContext, R.color.qm_selector_memberinfo_mute_color);
            radioButton.setTextColor(csl);
            radioButton.setBackgroundResource(R.drawable.qm_selector_memberinfo_mute);
            radioButton.setGravity(Gravity.CENTER);
            radioButton.setPadding(0, 0, 0, 0);
            radioButton.setLines(1);
            radioButton.setEllipsize(TextUtils.TruncateAt.END);
            radioButton.setButtonDrawable(android.R.color.transparent);//隐藏单选圆形按钮
            if(data.getKeeptime() != null){
                if(data.getKeeptime() == timeseconds) {
                    radioButton.setChecked(true);
                }else {
                    radioButton.setChecked(false);
                }
            }else {
                if (data.getIsforbiddenspeak() <= 0 && timeseconds == 0) {
                    radioButton.setChecked(true);
                } else {
                    radioButton.setChecked(false);
                }
            }
            radioGroupNotalk.addView(radioButton);

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //多样风格字符串
                    List<SpannableStringBean> list = new ArrayList<SpannableStringBean>();
                    list.add(new SpannableStringBean("确认将", 0));
                    list.add(new SpannableStringBean(data.getNickname(), ContextCompat.getColor(aContext, R.color.xlmemberinfo_dialog_text_nickname_color)));
                    list.add(new SpannableStringBean("禁言", 0));
                    list.add(new SpannableStringBean(timeToString(timeseconds), ContextCompat.getColor(aContext, R.color.xlmemberinfo_dialog_text_time_color)));
                    SpannableStringBuilder spannableStringBuilder = SpannableStringStyle.buildStyle(list, ContextCompat.getColor(aContext, R.color.xlmemberinfo_dialog_text_content_color));

                    showDialogMemberinfoHint(aContext, spannableStringBuilder, new CallbackOnclick() {
                        @Override
                        public void confirm() {
                            loading.show();
                            if(timeseconds == 0){
                                GangSDK.getInstance().membersManager().cancelMuteMember(String.valueOf(data.getUserid()), new DataCallBack() {
                                    @Override
                                    public void onSuccess(int status, String message, Object data) {
                                        initData();
                                        XLToastUtil.showToastShort("操作成功");
                                        loading.dismiss();
                                    }

                                    @Override
                                    public void onFail(String message) {
                                        radioButton.setChecked(false);
                                        XLToastUtil.showToastShort(message);
                                        loading.dismiss();
                                    }
                                });
                            }else {
                                GangSDK.getInstance().membersManager().muteMember(memberid, timeseconds, "辣鸡", new DataCallBack() {
                                    @Override
                                    public void onSuccess(int status, String message, Object data) {
                                        initData();
                                        XLToastUtil.showToastShort("操作成功");
                                        loading.dismiss();
                                    }

                                    @Override
                                    public void onFail(String message) {
                                        radioButton.setChecked(false);
                                        XLToastUtil.showToastShort(message);
                                        loading.dismiss();
                                    }
                                });
                            }
                        }

                        @Override
                        public void cancel() {
                            addNoTalkRadioButton(data, listTime);
                        }
                    });
                }
            });
        }
        setRadioGroupSize(66, 34, listTime.size(), radioGroupNotalk);
    }
    
    

    /**
     * 时间转换
     * @param timeseconds
     * @return
     */
    private String timeToString(Integer timeseconds) {
        String timeString = "";
        if(timeseconds == 0){
            timeString = "解禁";
        }else if(timeseconds == -1){
            timeString = "永久";
        }else {
            if (timeseconds / 60 / 60 / 24 > 0) {
                if (timeseconds / 60 / 60 % 24 == 0) {
                    timeString = timeseconds / 60 / 60 / 24 + "天";
                } else {
                    if ((timeseconds / 60 / 60 % 24) % 60 == 0) {
                        timeString = timeseconds / 60 / 60 / 24 + "天" + timeseconds / 60 / 60 % 24 + "小时";
                    } else {
                        timeString = timeseconds / 60 / 60 / 24 + "天" + (timeseconds / 60 / 60 % 24) / 60 + "小时" + (timeseconds / 60 / 60 % 24) % 60 + "分钟";
                    }
                }
            } else if (timeseconds / 60 / 60 > 0) {
                if (timeseconds / 60 % 60 == 0) {
                    timeString = timeseconds / 60 / 60 + "小时";
                } else {
                    timeString = timeseconds / 60 / 60 + "小时" + timeseconds / 60 % 60 + "分钟";
                }
            } else {
                timeString = timeseconds / 60 + "分钟";
            }
        }
        return timeString;
    }

    private void addPositionRadioButton(final XLGangMemberInfoBean data) {
        if(data.getRolelist() == null || data.getRolelist().isEmpty()){
            return;
        }
        radioGroupPosition.removeAllViews();
        for (int i = 0; i < data.getRolelist().size(); i++) {
            final RadioButton radioButton = new RadioButton(aContext);
            final XLGangRoleEntityBean entity = data.getRolelist().get(i);
            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(DensityUtil.dip2px(aContext, 69), DensityUtil.dip2px(aContext, 24));
            layoutParams.setMargins(DensityUtil.dip2px(aContext, 5), DensityUtil.dip2px(aContext, 5), DensityUtil.dip2px(aContext, 5), DensityUtil.dip2px(aContext, 5));
            radioButton.setLayoutParams(layoutParams);
            radioButton.setText(entity.getRolename());
            radioButton.setTextSize(12);
            ColorStateList csl = ContextCompat.getColorStateList(aContext, R.color.qm_selector_memberinfo_mute_color);
            radioButton.setTextColor(csl);
            radioButton.setBackgroundResource(R.drawable.qm_selector_memberinfo_mute);
            radioButton.setGravity(Gravity.CENTER);
            radioButton.setLines(1);
            radioButton.setEllipsize(TextUtils.TruncateAt.END);
            radioButton.setPadding(0, 0, 0, 0);
            radioButton.setButtonDrawable(android.R.color.transparent);//隐藏单选圆形按钮
            if(data.getRolename().equals(data.getRolelist().get(i).getRolename())) {
                radioButton.setChecked(true);
            }else {
                radioButton.setChecked(false);
            }
            radioGroupPosition.addView(radioButton);


            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!StringUtils.isEmpty(GangSDK.getInstance().groupManager().getGameConfigEntity().getGameprompt().getModify_position())) {
                        String message = GangSDK.getInstance().groupManager().getGameConfigEntity().getGameprompt().getModify_position();

                        //多样风格字符串
                        List<SpannableStringBean> list = new ArrayList<SpannableStringBean>();
                        list.add(new SpannableStringBean("\\{\\$nickname\\$\\}", data.getNickname(), ContextCompat.getColor(aContext, R.color.xlmemberinfo_dialog_text_nickname_color)));
                        SpannableStringBuilder spannableStringBuilder = SpannableStringStyle.buildStyleReplaceParts(message, list, ContextCompat.getColor(aContext, R.color.xlmemberinfo_dialog_text_content_color));

                        SpannableStringBuilder roleSpannableString = new SpannableStringBuilder(entity.getRolename());
                        roleSpannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(aContext, R.color.xlmemberinfo_dialog_text_content_color))
                                , 0, entity.getRolename().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                        spannableStringBuilder.append(roleSpannableString);

                        showDialogMemberinfoHint(aContext, spannableStringBuilder, new CallbackOnclick() {
                            @Override
                            public void confirm() {
                                loading.show();
                                GangSDK.getInstance().membersManager().modifyMemberRole(
                                        data.getUserid(), entity.getRoleid(), new DataCallBack() {

                                            @Override
                                            public void onSuccess(int status, String message, Object data) {
                                                initData();
                                                XLToastUtil.showToastShort("操作成功");
                                                loading.dismiss();
                                            }

                                            @Override
                                            public void onFail(String message) {
                                                radioButton.setChecked(false);
                                                XLToastUtil.showToastShort(message);
                                                loading.dismiss();
                                            }
                                        });
                            }

                            @Override
                            public void cancel() {
                                addPositionRadioButton(data);
                            }
                        });
                    }
                }
            });
        }
        setRadioGroupSize(80, 34, data.getRolelist().size(), radioGroupPosition);
    }

    /**
     * 动态设置RadioGroup宽高
     * @param viewwidth         添加view的宽
     * @param viewheight        添加view的高
     * @param viewnum           添加view的数量
     * @param radioGroup        父容器RadioGroup
     */
    private void setRadioGroupSize(int viewwidth, int viewheight, int viewnum, View radioGroup) {
        if(viewheight <= 0 || viewheight <= 0 || viewnum <= 0 || radioGroup == null){
            return;
        }
        int screenWidth = ScreenSizeUtil.getScreenWidth(aContext);
        int row = (DensityUtil.dip2px(aContext, viewwidth) * viewnum) / (screenWidth - DensityUtil.dip2px(aContext, 50));
        int remainder = (DensityUtil.dip2px(aContext, viewwidth) * viewnum) % (screenWidth - DensityUtil.dip2px(aContext, 50));
        RelativeLayout.LayoutParams layoutParams = null;
        if(row == 0){
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(aContext, viewheight));
        }else if(remainder == 0){
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(aContext, viewheight * row));
        }else {
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(aContext, viewheight* (row + 1)));
        }
        layoutParams.setMargins(DensityUtil.dip2px(aContext, 24), DensityUtil.dip2px(aContext, 38), DensityUtil.dip2px(aContext, 24), DensityUtil.dip2px(aContext, 0));
        radioGroup.setLayoutParams(layoutParams);
    }

    /***
     * 显示提示信息弹窗
     * @param activity              Activity
     * @param message               提示信息
     * @param callbackOnclick       回调
     */
    public static void showDialogMemberinfoHint(Activity activity, CharSequence message, CallbackOnclick callbackOnclick) {
        new DialogMemberinfoHintFragment()
                .setMessage(message)
                .setOnclickCallBack(callbackOnclick)
                .show(activity.getFragmentManager());
    }

    /**
     * 在玩游戏adapter
     */
    class PlayingGameAdapter extends RecyclerView.Adapter<PlayingGameViewHolder>{

        @Override
        public PlayingGameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PlayingGameViewHolder(LayoutInflater.from(aContext).inflate(R.layout.item_recyclerview_memberinfo_game, parent, false));
        }

        @Override
        public void onBindViewHolder(PlayingGameViewHolder holder, int position) {
            final XLGameInfoBean gameinfo = gameInfoList.get(position);
            ImageLoadUtil.loadRoundImage(holder.imageGameIcon, gameinfo.getAppicon());
            holder.textGameName.setText(StringUtils.getString(gameinfo.getAppname(), ""));
            holder.viewParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(gameinfo.getAndroidpackage() != null) {
                        if (gameinfo.getAndroidpackage().equals(GangSDK.getInstance().getApplication().getPackageName())) {
                            XLToastUtil.showToastShort(aContext.getResources().getString(R.string.xlgame_playing));
                            return;
                        }
                    }
                    if(gameinfo != null ) {
                        new DialogGameDownloadOrStartFragment()
                                .setGameInfo(gameinfo)
                                .show(aContext.getFragmentManager());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return gameInfoList.size();
        }
    }

    class PlayingGameViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageGameIcon;
        private TextView textGameName;
        private View viewParent;
        public PlayingGameViewHolder(View itemView) {
            super(itemView);
            imageGameIcon = (ImageView) itemView.findViewById(R.id.imageGameIcon);
            textGameName = (TextView) itemView.findViewById(R.id.textGameName);
            viewParent = itemView.findViewById(R.id.viewParent);
        }
    }
}
