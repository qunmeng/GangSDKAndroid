package com.qm.gangsdk.ui.custom.button;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.qm.gangsdk.ui.utils.ScreenSizeUtil;
import com.qm.gangsdk.ui.R;


/**
 * Created by lijiyuan on 2017/8/24.
 * 录音弹框管理类
 */
@SuppressLint("InflateParams")
public class XLRecordDialogManager {

	/**
	 * 以下为dialog的初始化控件，包括其中的布局文件
	 */

	private Dialog mDialog;

	//private ImageView mIcon;
	private ImageView mVoice;

	private TextView mLable;

	private Context mContext;

	public XLRecordDialogManager(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public void showRecordingDialog() {
		// TODO Auto-generated method stub

		mDialog = new Dialog(mContext, R.style.Theme_audioDialog);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// 用layoutinflater来引用布局
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.dialog_voice_record, null);
		mDialog.setContentView(view);

		//mIcon = (ImageView) mDialog.findViewById(R.id.dialog_icon);
		mVoice = (ImageView) mDialog.findViewById(R.id.dialog_voice);
		mLable = (TextView) mDialog.findViewById(R.id.recorder_dialogtext);

		Window dialogWindow = mDialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		int width = ScreenSizeUtil.getScreenWidth((Activity)mContext) / 2;
		lp.width = width; // 宽度
		lp.height = width; // 高度
		dialogWindow.setAttributes(lp);
		mDialog.setCancelable(false);
		mDialog.show();

	}

	/**
	 * 设置正在录音时的dialog界面
	 */
	public void recording() {
		if (mDialog != null && mDialog.isShowing()) {
			//mIcon.setVisibility(View.GONE);
			mVoice.setVisibility(View.VISIBLE);
			mLable.setVisibility(View.VISIBLE);
			mLable.setText("手指上滑，取消发送");
		}
	}

	/**
	 * 取消界面
	 */
	public void wantToCancel() {
		// TODO Auto-generated method stub
		if (mDialog != null && mDialog.isShowing()) {
			//mIcon.setVisibility(View.VISIBLE);
			mVoice.setVisibility(View.VISIBLE);
			mLable.setVisibility(View.VISIBLE);
			//mIcon.setImageResource(R.mipmap.voice_record_cancel);
			mLable.setText("松开手指，取消发送");
		}

	}

	// 时间过短
	public void tooShort() {
		// TODO Auto-generated method stub
		if (mDialog != null && mDialog.isShowing()) {
			//mIcon.setVisibility(View.VISIBLE);
			mVoice.setVisibility(View.VISIBLE);
			mLable.setVisibility(View.VISIBLE);

			//mIcon.setImageResource(R.mipmap.record_voice_to_short);
			mLable.setText("录音时间过短");
		}

	}
	// 时间过长
	public void tooLong() {
		// TODO Auto-generated method stub
		if (mDialog != null && mDialog.isShowing()) {
			//mIcon.setVisibility(View.VISIBLE);
			mVoice.setVisibility(View.VISIBLE);
			mLable.setVisibility(View.VISIBLE);

			//mIcon.setImageResource(R.mipmap.record_voice_to_short);
			mLable.setText("录音时间过长");
		}
	}

	// 隐藏dialog
	public void dimissDialog() {
		// TODO Auto-generated method stub

		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
			mDialog = null;
		}

	}

	public void updateVoiceLevel(int level) {
		// TODO Auto-generated method stub

		if (mDialog != null && mDialog.isShowing()) {
			int resId;
			if(level < 1){
				resId = mContext.getResources().getIdentifier("qm_record_tone0",
						"mipmap", mContext.getPackageName());
			}else if(level >= 1 && level < 2){
				resId = mContext.getResources().getIdentifier("qm_record_tone1",
					"mipmap", mContext.getPackageName());
			}else if(level >= 2 && level < 3){
				resId = mContext.getResources().getIdentifier("qm_record_tone2",
						"mipmap", mContext.getPackageName());
			}else if(level >= 3 && level < 4){
				resId = mContext.getResources().getIdentifier("qm_record_tone3",
						"mipmap", mContext.getPackageName());
			}else if(level >= 4 && level < 5){
				resId = mContext.getResources().getIdentifier("qm_record_tone4",
						"mipmap", mContext.getPackageName());
			}else if(level >= 5 && level < 6){
				resId = mContext.getResources().getIdentifier("qm_record_tone5",
						"mipmap", mContext.getPackageName());
			}else if(level >= 6 && level < 7){
				resId = mContext.getResources().getIdentifier("qm_record_tone6",
						"mipmap", mContext.getPackageName());
			}else if(level >= 7 && level < 8){
				resId = mContext.getResources().getIdentifier("qm_record_tone7",
						"mipmap", mContext.getPackageName());
			}else{
				resId = mContext.getResources().getIdentifier("qm_record_tone8",
						"mipmap", mContext.getPackageName());
			}
			mVoice.setImageResource(resId);
		}

	}

}
