package com.smarthouse.app;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RecognitionActivity extends Activity{
	

    private ImageButton btn_click;
    
    private Button btn1;
    private Button btn2;
    private Button btn3;

    //private EditText mResultText;
    
    private BlueTooth blueTooth;

    

    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recognition_layout);
        btn_click = (ImageButton) findViewById(R.id.btn_click);
        btn1=(Button) findViewById(R.id.btn_1);
        btn2=(Button) findViewById(R.id.btn_2);
        btn3=(Button) findViewById(R.id.btn_3);
       // mResultText = ((EditText) findViewById(R.id.result));
        

        //初始化语音配置对象
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=58b95393");

        btn_click.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int flag=0;
		     	blueTooth=new BlueTooth();
		     	
		    	//设置弹出对话框
		    	AlertDialog.Builder dialog=new AlertDialog.Builder(RecognitionActivity.this);
		    	dialog.setCancelable(false);
		        
		    	switch(blueTooth.IsEnabled(flag)){
		    	case 0:
		    		dialog.setMessage("当前设备不支持蓝牙功能");
		    		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
		    			
		    			@Override
		    			public void onClick(DialogInterface dialog, int which) {
		    				// TODO Auto-generated method stub
		    				
		    			}
		    		});
		    		dialog.show();
		    		break;
		    	case 1:
		    		dialog.setMessage("蓝牙未开启,是否开启？");
		    		dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
		    			
		    			@Override
		    			public void onClick(DialogInterface dialog, int which) {
		    				// TODO Auto-generated method stub
		    				//开启蓝牙
		    				blueTooth.Open(RecognitionActivity.this); 
		    				blueTooth.adapter.startDiscovery();
		    				blueTooth.getDevice();
		    				
		    			}
		    		});
		    		dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
		    				
		    				@Override
		    			public void onClick(DialogInterface dialog, int which) {
		    				// TODO Auto-generated method stub
		    					
		    			}
		    		});
		    		dialog.show();
		    		break;
		    	case 2:
		    		btnVoice();
		    		break;
		    	default:
		    		break;
		    	}
			}
		});     
        btn1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		    		btn1.setSelected(true);
		    		btn2.setSelected(false);
		    		btn3.setSelected(false);
			}
		}); 
        btn2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			    	btn1.setSelected(false);
			    	btn2.setSelected(true);
			    	btn3.setSelected(false);
			}
		}); 
        btn3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				btn1.setSelected(false);
		    	btn2.setSelected(false);
		    	btn3.setSelected(true);
			}
		}); 
       
        
        // 设置广播信息过滤      
        IntentFilter intentFilter = new IntentFilter();     
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);     
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);     
        intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);     
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);     
        // 注册广播接收器，接收并处理搜索结果      
        registerReceiver(receiver, intentFilter);     
//        // 寻找蓝牙设备，android会将查找到的设备以广播形式发出去      
//        blueTooth.adapter.startDiscovery(); 
    }

    

    //TODO 开始说话：
    private void btnVoice() {
        RecognizerDialog dialog = new RecognizerDialog(this,null);
        dialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        dialog.setParameter(SpeechConstant.ACCENT, "mandarin");

        dialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                printResult(recognizerResult);
            }
            @Override
            public void onError(SpeechError speechError) {
            }
        });
        dialog.show();
        Toast.makeText(this, "请开始说话", Toast.LENGTH_SHORT).show();
    }
        
    //回调结果：
    private void printResult(RecognizerResult results) {
        String text = parseIatResult(results.getResultString());
        // 自动填写地址
       // mResultText.append(text);
    }

    public static String parseIatResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);

            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }
    
    //蓝牙设备的配对和状态监视
  	private BroadcastReceiver receiver = new BroadcastReceiver() {     
  	    @Override     
  	    public void onReceive(Context context, Intent intent) {     
  	        String action = intent.getAction();     
  	        int connectState;
  			String name = null;
  			if (BluetoothDevice.ACTION_FOUND.equals(action)) {     
  	            // 获取查找到的蓝牙设备      
  	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);     
  	            System.out.println(device.getName());     
  	            // 如果查找到的设备符合要连接的设备，处理      
  	            if (device.getName().equalsIgnoreCase(name)) {     
  	                // 搜索蓝牙设备的过程占用资源比较多，一旦找到需要连接的设备后需要及时关闭搜索      
  	                blueTooth.adapter.cancelDiscovery();     
  	                // 获取蓝牙设备的连接状态      
  	                connectState = device.getBondState();     
  	                switch (connectState) {     
  	                    // 未配对      
  	                    case BluetoothDevice.BOND_NONE:     
  	                        // 配对      
  	                        try {     
  	                            Method createBondMethod = BluetoothDevice.class.getMethod("createBond");     
  	                            createBondMethod.invoke(device);     
  	                        } catch (Exception e) {      
  	                            e.printStackTrace();     
  	                        }     
  	                        break;     
  	                    // 已配对      
  	                    case BluetoothDevice.BOND_BONDED:     
  	                        try {     
  	                            // 连接      
  	                           blueTooth.connect(device);     
  	                        } catch (IOException e) {     
  	                            e.printStackTrace();     
  	                        }     
  	                        break;     
  	                }     
  	            }     
  	       } else if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {     
  	            // 状态改变的广播      
  	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);     
  	            if (device.getName().equalsIgnoreCase(name)) {      
  	                connectState = device.getBondState();     
  	                switch (connectState) {     
  	                    case BluetoothDevice.BOND_NONE:     
  	                        break;     
  	                    case BluetoothDevice.BOND_BONDING:     
  	                        break;     
  	                    case BluetoothDevice.BOND_BONDED:     
  	                        try {     
  	                            // 连接      
  	                            blueTooth.connect(device);     
  	                        } catch (IOException e) {     
  	                            e.printStackTrace();     
  	                        }     
  	                        break;     
  	                }     
  	            }     
  	        }     
  	    }     
  	};
}
