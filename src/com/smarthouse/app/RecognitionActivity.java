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
        

        //��ʼ���������ö���
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=58b95393");

        btn_click.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int flag=0;
		     	blueTooth=new BlueTooth();
		     	
		    	//���õ����Ի���
		    	AlertDialog.Builder dialog=new AlertDialog.Builder(RecognitionActivity.this);
		    	dialog.setCancelable(false);
		        
		    	switch(blueTooth.IsEnabled(flag)){
		    	case 0:
		    		dialog.setMessage("��ǰ�豸��֧����������");
		    		dialog.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
		    			
		    			@Override
		    			public void onClick(DialogInterface dialog, int which) {
		    				// TODO Auto-generated method stub
		    				
		    			}
		    		});
		    		dialog.show();
		    		break;
		    	case 1:
		    		dialog.setMessage("����δ����,�Ƿ�����");
		    		dialog.setPositiveButton("��", new DialogInterface.OnClickListener() {
		    			
		    			@Override
		    			public void onClick(DialogInterface dialog, int which) {
		    				// TODO Auto-generated method stub
		    				//��������
		    				blueTooth.Open(RecognitionActivity.this); 
		    				blueTooth.adapter.startDiscovery();
		    				blueTooth.getDevice();
		    				
		    			}
		    		});
		    		dialog.setNegativeButton("��", new DialogInterface.OnClickListener() {
		    				
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
       
        
        // ���ù㲥��Ϣ����      
        IntentFilter intentFilter = new IntentFilter();     
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);     
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);     
        intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);     
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);     
        // ע��㲥�����������ղ������������      
        registerReceiver(receiver, intentFilter);     
//        // Ѱ�������豸��android�Ὣ���ҵ����豸�Թ㲥��ʽ����ȥ      
//        blueTooth.adapter.startDiscovery(); 
    }

    

    //TODO ��ʼ˵����
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
        Toast.makeText(this, "�뿪ʼ˵��", Toast.LENGTH_SHORT).show();
    }
        
    //�ص������
    private void printResult(RecognizerResult results) {
        String text = parseIatResult(results.getResultString());
        // �Զ���д��ַ
       // mResultText.append(text);
    }

    public static String parseIatResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);

            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // תд����ʣ�Ĭ��ʹ�õ�һ�����
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }
    
    //�����豸����Ժ�״̬����
  	private BroadcastReceiver receiver = new BroadcastReceiver() {     
  	    @Override     
  	    public void onReceive(Context context, Intent intent) {     
  	        String action = intent.getAction();     
  	        int connectState;
  			String name = null;
  			if (BluetoothDevice.ACTION_FOUND.equals(action)) {     
  	            // ��ȡ���ҵ��������豸      
  	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);     
  	            System.out.println(device.getName());     
  	            // ������ҵ����豸����Ҫ���ӵ��豸������      
  	            if (device.getName().equalsIgnoreCase(name)) {     
  	                // ���������豸�Ĺ���ռ����Դ�Ƚ϶࣬һ���ҵ���Ҫ���ӵ��豸����Ҫ��ʱ�ر�����      
  	                blueTooth.adapter.cancelDiscovery();     
  	                // ��ȡ�����豸������״̬      
  	                connectState = device.getBondState();     
  	                switch (connectState) {     
  	                    // δ���      
  	                    case BluetoothDevice.BOND_NONE:     
  	                        // ���      
  	                        try {     
  	                            Method createBondMethod = BluetoothDevice.class.getMethod("createBond");     
  	                            createBondMethod.invoke(device);     
  	                        } catch (Exception e) {      
  	                            e.printStackTrace();     
  	                        }     
  	                        break;     
  	                    // �����      
  	                    case BluetoothDevice.BOND_BONDED:     
  	                        try {     
  	                            // ����      
  	                           blueTooth.connect(device);     
  	                        } catch (IOException e) {     
  	                            e.printStackTrace();     
  	                        }     
  	                        break;     
  	                }     
  	            }     
  	       } else if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {     
  	            // ״̬�ı�Ĺ㲥      
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
  	                            // ����      
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
