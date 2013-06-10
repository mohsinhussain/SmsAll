package com.hyperon.smsall;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;
	private HTTPClient mClient;
	private JSONObject mJSONMyGroups; 
	

    public ImageAdapter(Context c) {
        mContext = c;
        
        mClient = new HTTPClient(c.getSharedPreferences(Constants.PREFS_NAME, 0));
        String groups = "";
		try {
			groups = mClient.get(Constants.API_SERVER_URL + "/me/groups", null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(groups.equalsIgnoreCase(Constants.connectionTimeOutMessage)){
			Toast.makeText(c, Constants.connectionTimeOutMessage, Toast.LENGTH_LONG).show();
		}
		else{
        try { 
			mJSONMyGroups = new JSONObject(groups);
			Log.v(null, mJSONMyGroups.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		}
    }

    public int getCount() {
        try {
        	if(mJSONMyGroups!=null){
        	int length = mJSONMyGroups.getJSONArray(Constants.JSON_LIST_NAME).length();
        	Log.v(null, "" + length);
        	return length;
        	}else{
        		return 0;
        	}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 0;
    }

    public Object getItem(int position) {
        try {
			return mJSONMyGroups.getJSONArray(Constants.JSON_LIST_NAME).getString(position);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
    	if (convertView == null) {  // if it's not recycled, initialize some attributes
	    	LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    	
	        View v = li.inflate(R.layout.group_icon, null);
	        
//	        TextView tv = (TextView)v.findViewById(R.id.group_icon_text);
//			try {
//				tv.setText(mJSONMyGroups.getJSONArray(Constants.JSON_LIST_NAME).getString(position));
//				tv.setId(position);
//				v.setTag(tv);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//	        ImageView iv = (ImageView)v.findViewById(R.id.group_icon_image);
//	        iv.setImageResource(R.drawable.average_life_battery);

	        return v;
    	}
    	else {
    		TextView tv = (TextView) convertView.getTag();
    		try {
				tv.setText(mJSONMyGroups.getJSONArray(Constants.JSON_LIST_NAME).getString(position));
			} catch (JSONException e) {
				e.printStackTrace();
			}
    	}
    	return convertView;
    }

}