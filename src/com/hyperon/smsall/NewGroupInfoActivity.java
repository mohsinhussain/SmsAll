package com.hyperon.smsall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class NewGroupInfoActivity extends Activity{

	private JSONObject mGroupInfoJSON;
	private String TAG = "GroupInfoActivity";
	String groupName;
	Activity mActivity;
	Context mContext;
	private JSONObject mMembersJSON;
	ListView mListView;
	ImageView backButton;
	ScrollView scrollView;
	ImageView infoButton;
	ImageView membersButton;
	Activity activity;
	ConnectivityManager connManager;
	NetworkInfo mWifi;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.new_group_info_layout);
		//Remove title bar
		activity = NewGroupInfoActivity.this;
		connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		mListView = (ListView) findViewById(R.id.membersListView);
		backButton = (ImageView) findViewById(R.id.backbuttonImageView);
		scrollView = (ScrollView) findViewById(R.id.scrollView);
		infoButton = (ImageView) findViewById(R.id.infoButtonImageView);
		membersButton = (ImageView) findViewById(R.id.membersButtonImageView);
		scrollView.setVisibility(View.VISIBLE);
		mListView.setVisibility(View.GONE);
		infoButton.setImageDrawable(this.getResources().getDrawable(R.drawable.info_highlighted));
		membersButton.setImageDrawable(this.getResources().getDrawable(R.drawable.members_normal));
		
		mActivity = NewGroupInfoActivity.this;
		mContext = this;
		Intent intent = getIntent();
		groupName = intent.getStringExtra(Constants.INTENT_KEY_GROUP_NAME);
		if (mWifi.isConnected()) {
		    // Do whatever
			new APIClient(mActivity, mContext,  new GetInfoCallback()).doGetGroupInfo(groupName);
			new APIClient(mActivity, mContext,  new GetMembersCallback()).doGetMembers(groupName);
		}
		else{
			Toast.makeText(activity, "Wifi is not Connected! Please Check your WIFI...", Toast.LENGTH_LONG).show();
		}
		
		
		infoButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				scrollView.setVisibility(View.VISIBLE);
				mListView.setVisibility(View.GONE);
				infoButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.info_highlighted));
				membersButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.members_normal));
			}
		});
		
		membersButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				scrollView.setVisibility(View.GONE);
				mListView.setVisibility(View.VISIBLE);
				infoButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.info_normal));
				membersButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.members_highlited));
			}
		});
		
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	public class GetInfoCallback extends AsyncCallback {
		public void onTaskComplete(String response) {
			try {
				mGroupInfoJSON = new JSONObject(response);
				JSONObject dictionary = mGroupInfoJSON.getJSONObject(Constants.JSON_DICTIONARY_NAME);

//				((TextView) findViewById(R.id.group_name)).setText(groupName);

				String desc = Constants.NOT_AVAILABLE;
				try { desc = dictionary.getString("description"); }
				catch (JSONException e){ Log.e(TAG, e.toString()); }				
				((TextView) findViewById(R.id.group_description)).setText(desc);

				String creator = Constants.NOT_AVAILABLE;
				try { creator = dictionary.getString("creator"); }
				catch (JSONException e){ Log.e(TAG, e.toString()); }
				((TextView) findViewById(R.id.group_creator)).setText(creator);
				
				String created_on = Constants.NOT_AVAILABLE;
				try { created_on = dictionary.getString("date_created"); }
				catch (JSONException e){ Log.e(TAG, e.toString()); }				
				((TextView) findViewById(R.id.group_created_on)).setText(created_on);
				
				String member_count = Constants.NOT_AVAILABLE;
				try { member_count = dictionary.getString("member_count"); }
				catch (JSONException e){ Log.e(TAG, e.toString()); }				
				((TextView) findViewById(R.id.group_member_count)).setText(member_count);
				
				String membership = Constants.NOT_AVAILABLE;
				try { membership = dictionary.getString("membership"); }
				catch (JSONException e){ Log.e(TAG, e.toString()); }				
				((TextView) findViewById(R.id.group_membership)).setText(membership);

				String traffic = Constants.NOT_AVAILABLE;
				try { traffic = dictionary.getString("traffic_control"); }
				catch (JSONException e){ Log.e(TAG, e.toString()); }				
				((TextView) findViewById(R.id.group_traffic)).setText(traffic);

				String type = Constants.NOT_AVAILABLE;
				try { type = dictionary.getString("type"); }
				catch (JSONException e){ Log.e(TAG, e.toString()); }
				((TextView) findViewById(R.id.group_type)).setText(type);

				String status = "false";
				try { type = dictionary.getString("suspended"); }
				catch (JSONException e){ Log.e(TAG, e.toString()); }
				((TextView) findViewById(R.id.group_status)).setText(status == "false" ? "resumed" : "suspended");
				
			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "Group Information could not be loaded!", Toast.LENGTH_LONG).show();
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
	
	public class GetMembersCallback extends AsyncCallback {
		public void onTaskComplete(String response) {
			try {
				Log.v(TAG, "GetMembersCallback Response: " + response);
				mMembersJSON = new JSONObject(response);
				JSONArray list = mMembersJSON.getJSONArray(Constants.JSON_LIST_NAME);
				String strArray[] = new String[list.length()];
				
				for (int i = 0; i < list.length(); i++)
					strArray[i] = list.getString(i);
		        mListView.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
   					 R.layout.layout_member_item, strArray));
		        
			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(NewGroupInfoActivity.this, "Group Information could not be loaded!", Toast.LENGTH_LONG).show();
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
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
}
