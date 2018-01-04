package com.qm.gangsdk.ui.utils;

import android.os.Environment;

import com.xl.undercover.mp3recorder.MP3Recorder;

import java.io.File;

/**
 * Created by lijiyuan on 2017/8/24.
 * 录音管理类
 */
public class XLVoiceRecorderManage {
	private MP3Recorder mp3Recorder;
	private File voiceFile = null;


	public XLVoiceRecorderManage() {
		voiceFile = new File(Environment.getExternalStorageDirectory(),"gangsdk_temp.mp3");
	}

	public MP3Recorder getRecorder(){
		if(mp3Recorder == null){
			mp3Recorder = new MP3Recorder(voiceFile);
		}else {
			return mp3Recorder;
		}
		return mp3Recorder;
	}

	public String getVoicePath(){
		return voiceFile.getAbsolutePath();
	}


}
