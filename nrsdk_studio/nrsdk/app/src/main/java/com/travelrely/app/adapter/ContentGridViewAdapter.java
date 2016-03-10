package com.travelrely.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.travelrely.core.nrs.Res;
import com.travelrely.core.util.Utils;
import com.travelrely.sdk.R;

public class ContentGridViewAdapter extends BaseAdapter {

	Context context;
	private LayoutInflater g_lInflater;
	int type;
	String from;

	public ContentGridViewAdapter(Context mContext, int types, String froms) {
		// TODO Auto-generated constructor stub
		context = mContext;
		type = types;
		from = froms;
		g_lInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
	    if(type == 3){
	        if(Utils.isGroupLeader(from)){
	            return Res.expressionImgs2.length;
	        }else{
	            return Res.expressionImgs3.length;
	        }
	        
	    }else{
	        return Res.expressionImgs3.length;
	    }
		
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
	    if(type == 3){
	        if(Utils.isGroupLeader(from)){
	            return Res.expressionImgs2[position];
	        }else{
	            return Res.expressionImgs3[position];
	        }
	        
	    }else{
	        return Res.expressionImgs3[position];
	    }
		
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		GrivdHolder holder;
		if (convertView == null) {
			holder = new GrivdHolder();
			convertView = g_lInflater.inflate(R.layout.singleexpression, null);
			holder.iconDrawable = (ImageView) convertView.findViewById(R.id.image);
			holder.iconText = (TextView)convertView.findViewById(R.id.msg_content_grid_text);
			convertView.setTag(holder);
		} else {
			holder = (GrivdHolder) convertView.getTag();
		}
		if(type == 3){
		    if(Utils.isGroupLeader(from)){
		        holder.iconDrawable.setImageResource(Res.expressionImgs2[position]);
	            holder.iconText.setText(Res.expressionImgs2Names[position]);
		    }else{
		        holder.iconDrawable.setImageResource(Res.expressionImgs3[position]);
	            holder.iconText.setText(Res.expressionImgs3Names[position]);
		    }
		    
		}else{
	        holder.iconDrawable.setImageResource(Res.expressionImgs3[position]);
            holder.iconText.setText(Res.expressionImgs3Names[position]);
		}
		return convertView;
	}

	class GrivdHolder {
		public ImageView iconDrawable = null;
		public TextView iconText = null;
	}
}
