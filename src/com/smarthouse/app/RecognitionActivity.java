package com.smarthouse.app;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import android.app.Activity;
import android.os.Bundle;

public class RecognitionActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recognition_layout);
		
		//初始化即创建语音配置对象
		SpeechUtility.createUtility(RecognitionActivity.this, SpeechConstant.APPID +"=58b95393");  
	}

}
