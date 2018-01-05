package com.qm.gangsdk.ui.view.gangin.chat;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qm.gangsdk.core.outer.common.callback.DataCallBack;
import com.qm.gangsdk.core.outer.receiver.GangPosterReceiver;
import com.qm.gangsdk.core.outer.common.utils.StringUtils;
import com.qm.gangsdk.core.outer.common.entity.XLGangAccidentTaskBean;
import com.qm.gangsdk.ui.GangSDK;
import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.base.XLBaseDialogFragment;
import com.qm.gangsdk.ui.event.XLAccidentTaskSuccessEvent;
import com.qm.gangsdk.ui.utils.DensityUtil;
import com.qm.gangsdk.ui.utils.ImageLoadUtil;
import com.qm.gangsdk.ui.utils.XLToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijiyuan on 2017/10/11.
 * 怪兽袭击弹窗
 */

public class DialogMonsterAttackFragment extends XLBaseDialogFragment {

    private ImageView imageMonsterIcon;
    private TextView textClose;
    private Button btnMonsterAccident;
    private RelativeLayout viewParent;
    private View endAnim; //动画终点
    private View fromView; //动画终点
    private LinearLayout linearMonsterBlood;
    private XLGangAccidentTaskBean accidentTaskBean;

    private int attackNum = 1; //记录攻击次数
    private static final int attackMaxNum = 10; //记录攻击次数

    List<Integer> list = new ArrayList();

    @Override
    protected int getContentView() {
        return R.layout.dialog_fragment_monster_accident_task;
    }

    @Override
    protected void initData() {


    }

    @Override
    protected void initView(View view) {
        imageMonsterIcon = (ImageView) view.findViewById(R.id.imageMonsterIcon);
        textClose = (TextView) view.findViewById(R.id.textClose);
        btnMonsterAccident = (Button) view.findViewById(R.id.btnMonsterAccident);
        endAnim = view.findViewById(R.id.endAnim);
        fromView = view.findViewById(R.id.fromView);
        viewParent = (RelativeLayout) view.findViewById(R.id.viewParent);
        linearMonsterBlood = (LinearLayout) view.findViewById(R.id.linearMonsterBlood);

        textClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(accidentTaskBean == null){
            return;
        }
        if(StringUtils.isEmpty(accidentTaskBean.getTaskiconurl())){
            return;
        }else {
            ImageLoadUtil.loadNormalImage(imageMonsterIcon, accidentTaskBean.getTaskiconurl());
        }

        btnMonsterAccident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(attackNum >= attackMaxNum ){
                    updateMonsterBlood();
                    loading.show();
                    GangSDK.getInstance().groupManager().dealTask(accidentTaskBean.getTasktype(), accidentTaskBean.getTaskid(), new DataCallBack() {
                        @Override
                        public void onSuccess(int status, String message, Object data) {
                            loading.dismiss();
                            close();
                            GangPosterReceiver.post(new XLAccidentTaskSuccessEvent(XLGangAccidentTaskBean.TYPE_MONSTER_ACCIDENT));
                        }

                        @Override
                        public void onFail(String message) {
                            loading.dismiss();
                            XLToastUtil.showToastShort(message);
                        }
                    });

                }else {
                    animView();
                    updateMonsterBlood();
                }
            }
        });

    }

    /**
     * 获取数据
     * @param accidentTaskBean
     * @return
     */
    public DialogMonsterAttackFragment setTadkData(XLGangAccidentTaskBean accidentTaskBean){
        this.accidentTaskBean = accidentTaskBean;
        return this;
    }

    @Override
    protected boolean cancelTouchOutSide() {
        return false;
    }

    /**
     * 怪兽血条变化
     */
    private void updateMonsterBlood() {
        linearMonsterBlood.removeAllViews();
        linearMonsterBlood.setBackgroundResource(R.drawable.qm_shape_task_monster_blood_bg);
        View viewup = new View(aContext);
        viewup.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, (attackMaxNum-attackNum)));
        viewup.setBackgroundResource(R.drawable.qm_shape_task_monster_blood_red);

        View viewdown = new View(aContext);
        viewdown.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, attackNum));

        linearMonsterBlood.addView(viewup);
        linearMonsterBlood.addView(viewdown);
        attackNum++;

    }


    /**
     * 攻击动画
     */
    public void animView(){
        final int[] fromPosition = new int[2];
        final int[] toPositon = new int[2];
        fromView.getLocationInWindow(fromPosition);
        endAnim.getLocationInWindow(toPositon);
        int number = (int) (Math.random()*500 + 50);
        final TextView textView = new TextView(aContext);
        final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(DensityUtil.dip2px(aContext,50), DensityUtil.dip2px(aContext,50));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        textView.setLayoutParams(layoutParams);
        textView.setGravity(Gravity.CENTER);
        textView.setText(String.valueOf(number));
        textView.setTextSize(DensityUtil.px2sp(aContext, 40));
        TextPaint tp = textView.getPaint();
        tp.setFakeBoldText(true);
        textView.setTextColor(ContextCompat.getColor(aContext, R.color.xlgangchat_task_text_hint_color));
        viewParent.addView(textView);
        list.add(textView.getId());
        int width = textView.getLayoutParams().width;
        int height = textView.getLayoutParams().height;

        AnimationSet animationset = new AnimationSet(false);
        TranslateAnimation translateAnimation = new TranslateAnimation(
                0 + width/4,
                toPositon[0] - fromPosition[0],
                0 - height*2,
                (toPositon[1] - fromPosition[1])*4/3
        );
        ScaleAnimation scaleanimation = new ScaleAnimation(2.0f, 0.5f, 2.0f, 0.5f);
        animationset.addAnimation(scaleanimation);
        animationset.addAnimation(translateAnimation);

        animationset.setDuration(2000);
        animationset.setFillAfter(true);

        textView.startAnimation(animationset);
        animationset.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                new Handler().post(new Runnable() {
                    public void run() {
                        viewParent.removeView(textView);
                    }
                });
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }
}
