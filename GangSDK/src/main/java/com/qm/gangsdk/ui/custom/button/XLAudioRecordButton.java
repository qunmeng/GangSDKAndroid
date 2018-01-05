package com.qm.gangsdk.ui.custom.button;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import com.qm.gangsdk.ui.R;
import com.qm.gangsdk.ui.utils.FileSaveUtil;

import java.io.IOException;
import java.math.BigDecimal;

import static com.qm.gangsdk.ui.R.color.xlgangchat_button_press_on_color;

public class XLAudioRecordButton extends Button{
	private static final int STATE_NORMAL = 1;
	private static final int STATE_RECORDING = 2;
	private static final int STATE_WANT_TO_CANCEL = 3;
	private static final int DISTANCE_Y_CANCEL = 50;
	private static final int OVERTIME = 60;
	private int mCurrentState = STATE_NORMAL;
	// 已经开始录音
	private boolean isRecording = false;
	private XLRecordDialogManager mDialogManager;
	private float mTime = 0;
	// 是否触发了onlongclick，准备好了
	private boolean mReady;
	//private AudioManager mAudioManager;
	//private String saveDir = FileSaveUtil.voice_dir;
	/**
	 * 先实现两个参数的构造方法，布局会默认引用这个构造方法， 用一个 构造参数的构造方法来引用这个方法 * @param context
	 */

	public XLAudioRecordButton(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public XLAudioRecordButton(Context context, AttributeSet attrs) {
		super(context, attrs);

		mDialogManager = new XLRecordDialogManager(getContext());

		try {
			FileSaveUtil.createSDDirectory(FileSaveUtil.voice_dir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//mAudioManager = AudioManager.getInstance(FileSaveUtil.voice_dir);
		//mAudioManager.setOnAudioStageListener(this);
		//mAudioManager.setHandle(mp3handler);
//		setOnLongClickListener(new OnLongClickListener() {
//
//			@Override
//			public boolean onLongClick(View v) {
//				// TODO Auto-generated method
//				if(mListener != null) {
//					try {
//						mListener.onStart();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//				mhandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
//				mReady = true;
//				return false;
//			}
//		});
		// TODO Auto-generated constructor stub
	}

	/**
	 * 录音完成后的回调，回调给activiy，可以获得mtime和文件的路径
	 */
	public interface AudioFinishRecorderListener {
		void onStart() throws IOException;
		void onCancel();
		void onFinished(float seconds);
	}

	private AudioFinishRecorderListener mListener;

	public void setAudioFinishRecorderListener(
			AudioFinishRecorderListener listener) {
		mListener = listener;
	}

	// 获取音量大小的runnable
	private Runnable mGetVoiceLevelRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (isRecording) {
				try {
					Thread.sleep(100);
					mTime += 0.1f;
					mhandler.sendEmptyMessage(MSG_VOICE_CHANGE);
					if (mTime >= OVERTIME) {
						mTime = 60;
						mhandler.sendEmptyMessage(MSG_OVERTIME_SEND);
						isRecording = false;
						break;
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};

	// 准备三个常量
	private static final int MSG_AUDIO_PREPARED = 0X110;
	private static final int MSG_VOICE_CHANGE = 0X111;
	private static final int MSG_DIALOG_DIMISS = 0X112;
	private static final int MSG_OVERTIME_SEND = 0X113;

	private Handler mhandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MSG_AUDIO_PREPARED:
					// 显示应该是在audio end prepare之后回调
					if (isTouch) {
						mTime = 0;
						mDialogManager.showRecordingDialog();
						isRecording = true;
						new Thread(mGetVoiceLevelRunnable).start();
					}
					// 需要开启一个线程来变换音量
					break;
				case MSG_VOICE_CHANGE:
					if(mVolumeInterface != null) {
						mDialogManager.updateVoiceLevel(mVolumeInterface.getVolumeLevel());
					}
					break;
				case MSG_DIALOG_DIMISS:
					isRecording = false;
					mDialogManager.dimissDialog();
					break;
				case MSG_OVERTIME_SEND:
					mDialogManager.tooLong();
					mhandler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS, 1300);// 持续1.3s
					if (mListener != null) {// 并且callbackActivity，保存录音
						mListener.onFinished(mTime);
					}
					isRecording = false;
					reset();// 恢复标志位
					break;
			}
		};
	};

	private VolumeInterface mVolumeInterface;
	public void setOnVolumeInterface(VolumeInterface volumeInterface){
		mVolumeInterface = volumeInterface;
	}
	public interface VolumeInterface{
		int getVolumeLevel();
	}


	private int iconpress = -1;
	private int iconstop = -1;
	private int colorpress = -1;
	private int colorstop = -1;

	/**
	 * 直接复写这个监听函数
	 */
	private boolean isTouch = false;

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int action = event.getAction();
		int x = (int) event.getX();
		int y = (int) event.getY();

		switch (action) {
			case MotionEvent.ACTION_DOWN:
				isTouch = true;
				changeState(STATE_RECORDING);


				if(mListener != null) {
					try {
						mListener.onStart();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				mhandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
				mReady = true;

				break;
			case MotionEvent.ACTION_MOVE:
				if (isRecording) {
					// 根据x，y来判断用户是否想要取消
					if (wantToCancel(x, y)) {
						changeState(STATE_WANT_TO_CANCEL);
					} else {
						changeState(STATE_RECORDING);
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				// 首先判断是否有触发onlongclick事件，没有的话直接返回reset
				isTouch = false;
				if (!mReady) {
					reset();

					return true;
//					return super.onTouchEvent(event);
				}
				// 如果按的时间太短，还没准备好或者时间录制太短，就离开了，则显示这个dialog
				if (!isRecording || mTime < 0.6f) {
					mDialogManager.tooShort();
					if(mListener != null){
						mListener.onCancel();
					}
					mhandler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS, 1300);// 持续1.3s
				} else if (mCurrentState == STATE_RECORDING) {// 正常录制结束
					mDialogManager.dimissDialog();
					//mAudioManager.release();// release释放一个mediarecorder
					if (mListener != null) {// 并且callbackActivity，保存录音
						BigDecimal b = new BigDecimal(mTime);
						float f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP)
								.floatValue();
						mListener.onFinished(f1);
					}
				} else if (mCurrentState == STATE_WANT_TO_CANCEL) {
					if(mListener != null){
						mListener.onCancel();
					}
					mDialogManager.dimissDialog();
				}
				isRecording = false;
				reset();// 恢复标志位

				break;
			case MotionEvent.ACTION_CANCEL:
				isTouch = false;
				reset();
				break;

		}

		return true;
//		return super.onTouchEvent(event);
	}

	/**
	 * 回复标志位以及状态
	 */
	private void reset() {
		// TODO Auto-generated method stub
		isRecording = false;
		changeState(STATE_NORMAL);
		mReady = false;
		mTime = 0;
	}

	private boolean wantToCancel(int x, int y) {
		// TODO Auto-generated method stub

		if (x < 0 || x > getWidth()) {// 判断是否在左边，右边，上边，下边
			return true;
		}
		if (y < -DISTANCE_Y_CANCEL || y > getHeight() + DISTANCE_Y_CANCEL) {
			return true;
		}

		return false;
	}

	private void changeState(int state) {
		// TODO Auto-generated method stub
		if (mCurrentState != state) {
			mCurrentState = state;
			switch (mCurrentState) {
				case STATE_NORMAL:
						setBackgroundResource(R.drawable.qm_btn_gangchat_press_on);
						setTextColor(ContextCompat.getColor(getContext(), R.color.xlgangchat_button_press_on_color));
					setText("按住 说话");
					break;
				case STATE_RECORDING:
						setBackgroundResource(R.drawable.qm_btn_gangchat_stop_talk);
						setTextColor(ContextCompat.getColor(getContext(), R.color.xlgangchat_button_stop_talk_color));
					setText("松开 结束");
					if (isRecording) {
						mDialogManager.recording();
						// 复写dialog.recording();
					}
					break;

				case STATE_WANT_TO_CANCEL:
						setBackgroundResource(R.drawable.qm_btn_gangchat_stop_talk);
						setTextColor(ContextCompat.getColor(getContext(), R.color.xlgangchat_button_stop_talk_color));
					setText("松开手指，取消发送");
					// dialog want to voice_record_cancel
					mDialogManager.wantToCancel();
					break;

			}
		}

	}

	public void initUiRes(int iconpresson, int iconstop, int colorpresson, int colorstop){
		this.iconpress = iconpresson;
		this.iconstop = iconstop;
		this.colorpress = colorpresson;
		this.colorstop = colorstop;
	}

	@Override
	public boolean onPreDraw() {
		// TODO Auto-generated method stub
		return false;
	}

}
