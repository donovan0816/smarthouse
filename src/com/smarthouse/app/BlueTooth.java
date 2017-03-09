package com.smarthouse.app;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BlueTooth {

	BluetoothAdapter adapter= BluetoothAdapter.getDefaultAdapter();

	// 检查设备是否支持蓝牙      
	public int IsEnabled(int flag){		 
		
		if (adapter == null)  // 设备不支持蓝牙         
			flag= 0;
		else if(!adapter.isEnabled()) //蓝牙未开启
			flag= 1;
		else if(adapter.isEnabled()) //蓝牙已开启
			flag= 2;
		return flag;	   
	}
	
	
	// 打开蓝牙      
	public void Open(Context context){
		    
		 Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);     
		 // 设置蓝牙可见性，最多300秒      
		 intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);     
		 context.startActivity(intent);  		   
	} 
	
	//获取已配对的蓝牙设备
	public void getDevice(){
		Set<BluetoothDevice> devices = adapter.getBondedDevices();     
		for(int i=0; i<devices.size(); i++)     
		{     
		    BluetoothDevice device = (BluetoothDevice) devices.iterator().next();        
		    Log.d("device",device.getName());
		}   
	}

	
	
	//蓝牙设备的连接
	public void connect(BluetoothDevice device) throws IOException {     
	    // 固定的UUID      
	    final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";     
	    UUID uuid = UUID.fromString(SPP_UUID);     
	    BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid);     
	    socket.connect();     
	}  
	
}

