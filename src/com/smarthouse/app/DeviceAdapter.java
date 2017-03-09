package com.smarthouse.app;

import java.util.List;

import com.iflytek.cloud.thirdparty.v;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DeviceAdapter extends ArrayAdapter<Device>{

	private int id;
	
	public DeviceAdapter(Context context, int resource, List<Device> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		id=resource;
	}

	public View getView(int position,View convertView,ViewGroup parent){
		Device device=getItem(position);
		View view =LayoutInflater.from(getContext()).inflate(id, null);
		ImageView deviceImage=(ImageView) view.findViewById(R.id.device_image);
		TextView deviceName=(TextView) view.findViewById(R.id.device_name);
		deviceImage.setImageResource(device.getImageId());
		deviceName.setText(device.getName());
		return view;
		
	}
}
