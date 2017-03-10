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
//		View view =LayoutInflater.from(getContext()).inflate(id, null);
		View view;
		ViewHolder viewHolder;
		if(convertView==null){
			view=LayoutInflater.from(getContext()).inflate(id, null);
			viewHolder=new ViewHolder();
			viewHolder.deviceImage=(ImageView) view.findViewById(R.id.device_image);
			viewHolder.deviceName=(TextView) view.findViewById(R.id.device_name);
			view.setTag(viewHolder);
		}else{
			view =convertView;
			viewHolder=(ViewHolder) view.getTag();
		}
//		ImageView deviceImage=(ImageView) view.findViewById(R.id.device_image);
//		TextView deviceName=(TextView) view.findViewById(R.id.device_name);
		viewHolder.deviceImage.setImageResource(device.getImageId());
		viewHolder.deviceName.setText(device.getName());
		return view;		
	}
	
	class ViewHolder{
		ImageView deviceImage;
		
		TextView deviceName;
	}
}
