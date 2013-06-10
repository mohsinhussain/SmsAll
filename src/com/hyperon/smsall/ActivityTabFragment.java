package com.hyperon.smsall;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ActivityTabFragment extends ListFragment {
	ArrayList<HashMap<String, String>> mActivityList;
	private static final String TAG_FROM = "from";
	private static final String TAG_MESSAGE = "content";
	private static final String TAG_TIMESTAMP = "ts";
	private ListView mListView;
	private JSONObject mActivityJSON;
	private String TAG = "ActivityTabFragment";
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	if (container == null) 
    		return null;
    	return inflater.inflate(R.layout.fragment_activity_tab, container, false);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
        mListView = getListView();
//        new APIClient(ActivityTabFragment.this, getActivity(), new GetActivityCallback()).doGetActivity(((GroupInfoActivity)getActivity()).getGroup());
    }
    
	public class GetActivityCallback extends AsyncCallback {
		public void onTaskComplete(String response) {
			try {
				Log.v(TAG, "GetActivityCallback Response: " + response);
				
				mActivityJSON = new JSONObject(response);
				mActivityList = new ArrayList<HashMap<String,String>>();
				
		        JSONArray list = mActivityJSON.getJSONArray(Constants.JSON_LIST_NAME);
				for (int i = 0; i < list.length(); i++)
				{
					JSONObject activity = list.getJSONObject(i);
					HashMap<String, String> map = new HashMap<String, String>();
					Double unixTime = Double.valueOf(activity.getString(TAG_TIMESTAMP));  
					long timestamp = (long) (unixTime * 1000L);  // msec
					java.util.Date d = new java.util.Date(timestamp);  
					map.put(TAG_FROM, activity.getString(TAG_FROM));
					map.put(TAG_MESSAGE, activity.getString(TAG_MESSAGE));
					map.put(TAG_TIMESTAMP, d.toString());
					
					mActivityList.add(map);
				}
				String[] from = { TAG_FROM, TAG_MESSAGE, TAG_TIMESTAMP };
		        int[] to = { R.id.from, R.id.message, R.id.date };
				mListView = getListView();
				ListAdapter adapter = new SimpleAdapter(getActivity().getApplicationContext(), mActivityList,R.layout.groupactivites_listitems, from, to);
		        mListView.setAdapter(adapter);
			}
			catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(getActivity(), "Group Information could not be loaded!", Toast.LENGTH_LONG).show();
			}
			
	        }
		@Override
		public void onTaskCancelled() {
		}
		@Override
		public void onPreExecute() {
			// TODO Auto-generated method stub
			
		}
	}
        
}

