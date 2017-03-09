package com.smarthouse.app;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends Activity{

	private List<Device> deviceList=new ArrayList<Device>();
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		
		initDevices();
		DeviceAdapter adapter=new DeviceAdapter(MainActivity.this, R.layout.device_item, deviceList);
		ListView listView=(ListView) findViewById(R.id.list_view);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MainActivity.this,RecognitionActivity.class);
				startActivity(intent);
				
			}
		});
	}

	private void initDevices() {
		// TODO Auto-generated method stub
		Device item1=new Device("照明器具", R.drawable.item_1);
		deviceList.add(item1);
		Device item2=new Device("空调器具", R.drawable.item_2);
		deviceList.add(item2);
		Device item3=new Device("清洁器具", R.drawable.item_3);
		deviceList.add(item3);
		Device item4=new Device("厨房器具", R.drawable.item_4);
		deviceList.add(item4);
		Device item5=new Device("电子器具", R.drawable.item_5);
		deviceList.add(item5);
		Device item6=new Device("健身器具", R.drawable.item_6);
		deviceList.add(item6);
		Device item7=new Device("制冷器具", R.drawable.item_7);
		deviceList.add(item7);
		Device item8=new Device("取暖器具", R.drawable.item_8);
		deviceList.add(item8);
	}
}
