package com.hyperon.smsall;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Adapters.DiscussArrayAdapter;
import Classes.ChatMessage;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class GroupChatActivity extends Activity implements DialogComposeMessage.ComposeDialogListener {

	ListView chatListView;
	ImageView backButtonImageView;
	ImageView inviteButtonImageView;
	TextView groupNameTextView;
	String mGroupName;
	EditText chatMessageEditText;
	ImageView sendMessageButtonImageView;
	ArrayList<ChatMessage> chatMessageArrayList = new ArrayList<ChatMessage>(); 
	ArrayList<HashMap<String, String>> mActivityList;
	private static final String TAG_FROM = "from";
	private static final String TAG_MESSAGE = "content";
	private static final String TAG_TIMESTAMP = "ts";
//	private ListView mListView;
	private JSONObject mActivityJSON;
	private String TAG = "GroupChatActivity";
	DiscussArrayAdapter chatAdapter;
	BroadcastReceiver updateGroupChat;
	ConnectivityManager connManager;
	NetworkInfo mWifi;
	boolean revieverRegistered = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.group_chat_activity_relative_layout);
		connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		Intent intent = getIntent();
		mGroupName = intent.getStringExtra(Constants.INTENT_KEY_GROUP_NAME);
		
		if(intent.hasExtra(Constants.INTENT_KEY_GROUP_NAME)){
			Log.v("SEE", "Intent: "+ intent.getExtras().getString(Constants.INTENT_KEY_GROUP_NAME));
		}
		
		
		chatListView = (ListView) findViewById(R.id.groupChatListView);
		backButtonImageView = (ImageView) findViewById(R.id.backbuttonImageView);
		inviteButtonImageView = (ImageView) findViewById(R.id.inviteButtonImageView);
		groupNameTextView = (TextView) findViewById(R.id.groupNameTextView);
		chatMessageEditText = (EditText) findViewById(R.id.messageEditText);
		sendMessageButtonImageView = (ImageView) findViewById(R.id.sendButtonImageView);
		updateGroupChat = new BroadcastReceiver() {
		    @Override
		    public void onReceive(Context context, Intent intent) {
		        // INSERT CODE TO REFRESH LIST VIEW
//		    	chatAdapter = new DiscussArrayAdapter(getApplicationContext(), R.layout.listitem_discuss);
		    	
		    	if (mWifi.isConnected()) {
				    // Do whatever
		    		Log.v(TAG, "I am coming to registeReciever");
			    	chatListView.setAdapter(chatAdapter);
					new APIClient(GroupChatActivity.this, getApplicationContext(), new GetActivityCallback()).doGetActivity(mGroupName, "1");
				}
				else{
					Toast.makeText(GroupChatActivity.this, "Wifi is not Connected! Please Check your WIFI...", Toast.LENGTH_LONG).show();
				}
		    }
		};
		revieverRegistered = true;
		registerReceiver(updateGroupChat, new IntentFilter("MyGCMGroupMessageReceived"));
		
		inviteButtonImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				Intent i = new Intent(GroupChatActivity.this, AddContactsToGroupActivity.class);
				startActivityForResult(i, 1);
			}
		});
		
		
		
		groupNameTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent =new Intent(getApplicationContext(),NewGroupInfoActivity.class);
	            intent.putExtra(Constants.INTENT_KEY_GROUP_NAME ,mGroupName);
	            
	            startActivity(intent);
			}
		});
		sendMessageButtonImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(chatMessageEditText.getText().toString().equalsIgnoreCase("")){
					Toast.makeText(getApplicationContext(), "Please Enter Message First!", Toast.LENGTH_LONG).show();
					chatMessageEditText.requestFocus();
				}
				else{
					
					if (mWifi.isConnected()) {
				    // Do whatever
						DialogComposeMessage.newInstance(mGroupName, chatMessageEditText.getText().toString()).show(getFragmentManager(),
								  "DialogComposeMessage");
					}
					else{
						Toast.makeText(GroupChatActivity.this, "Wifi is not Connected! Please Check your WIFI...", Toast.LENGTH_LONG).show();
					}
					
				}
				
			}
		});
		
		backButtonImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		
		groupNameTextView.setText(mGroupName);

	}
	
	
	@Override
	protected void onStop()
	{
//		if(revieverRegistered){
//			unregisterReceiver(updateGroupChat);
//		}
	    
//	    unregisterReceiver(deliveryBroadcastReceiver);
	    super.onStop();
	}
	
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);


	    // Checks whether a hardware keyboard is available
	    if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
//	        Toast.makeText(GroupChatActivity.this, "keyboard visible", Toast.LENGTH_SHORT).show();
	    } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
//	        Toast.makeText(GroupChatActivity.this, "keyboard hidden", Toast.LENGTH_SHORT).show();
	    }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(data!=null){
//		Toast.makeText(getApplicationContext(), "Numbers Added: "+data.getExtras().getString(Constants.INTENT_CONTACTS_SELECTED) , Toast.LENGTH_LONG).show();
		
			String contactsList = data.getExtras().getString(Constants.INTENT_CONTACTS_SELECTED);
			if(contactsList.equalsIgnoreCase("")){
//				Toast.makeText(getApplicationContext(), "No Contact was selected" , Toast.LENGTH_LONG).show();
			}
			else{
				if (mWifi.isConnected()) {
				    // Do whatever
					new APIClient(GroupChatActivity.this, this, new PutInviteContactsCallbacks()).doPutInviteContacts(mGroupName, contactsList);
					}
					else{
						Toast.makeText(GroupChatActivity.this, "Wifi is not Connected! Please Check your WIFI...", Toast.LENGTH_LONG).show();
					}
				
			}
			}
		else{
//			Toast.makeText(getApplicationContext(), "Back was pressed", Toast.LENGTH_LONG).show();
		}

	}
	
	@Override
	public void onDialogPositiveClick(String message, Boolean replyAll) {
		if (mWifi.isConnected()) {
		    // Do whatever
			Log.d(TAG, "Launching task...");
			new APIClient(GroupChatActivity.this, this, new PostMessageCallbacks ()).doPostMessage(mGroupName, chatMessageEditText.getText().toString(), replyAll);
			}
			else{
				Toast.makeText(GroupChatActivity.this, "Wifi is not Connected! Please Check your WIFI...", Toast.LENGTH_LONG).show();
			}
	}

	@Override
	public void onDialogNegativeClick() {
		Toast.makeText(GroupChatActivity.this, "Message Discarded", Toast.LENGTH_SHORT).show();
	}

	public class PutInviteContactsCallbacks extends AsyncCallback {
		public void onTaskComplete(String response) {
			if (response == Constants.Success.toString()) {
				Toast.makeText(GroupChatActivity.this, "Contacts Added To Group", Toast.LENGTH_SHORT).show();
//				if (mWifi.isConnected()) {
//				    // Do whatever
//					Log.d(TAG, "Launching task...");
//					new APIClient(GroupChatActivity.this, getApplicationContext(), new GetActivityCallback()).doGetActivity(mGroupName, "1");
//					}
//					else{
//						Toast.makeText(GroupChatActivity.this, "Wifi is not Connected! Please Check your WIFI...", Toast.LENGTH_LONG).show();
//					}
				
			}
			else if(response.contains("4304")){
				Toast.makeText(GroupChatActivity.this, "A request has Already been sent to selected numbers to joing group", Toast.LENGTH_LONG).show();
			}
			
			else if(response.contains("2000")){
				Toast.makeText(GroupChatActivity.this, "You are trying to add an invalid number", Toast.LENGTH_LONG).show();
			}
//			chatMessageEditText.setText("");
		}
		@Override
		public void onTaskCancelled() {
		}
		@Override
		public void onPreExecute() {
			// TODO Auto-generated method stub
			
		}
	}
	
	
	public class PostMessageCallbacks extends AsyncCallback {
		public void onTaskComplete(String response) {
			Log.v(TAG, "Response: "+response);
			String status = "";
			
			if(response.equalsIgnoreCase(Constants.connectionTimeOutMessage)){
				Toast.makeText(GroupChatActivity.this, Constants.connectionTimeOutMessage, Toast.LENGTH_LONG).show();
			}
			else{
				try {
					JSONObject jsonResponse = new JSONObject(response);
					status = Integer.toString(jsonResponse.getInt("status"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (status.equalsIgnoreCase("3102")) {
//					Toast.makeText(GroupChatActivity.this, "Message Accepted for delivery", Toast.LENGTH_SHORT).show();
					new APIClient(GroupChatActivity.this, getApplicationContext(), new GetActivityCallback()).doGetActivity(mGroupName, "1");
				}
				else {
					Toast.makeText(GroupChatActivity.this, "Error: "+status, Toast.LENGTH_LONG).show();
				}
			}
			chatMessageEditText.setText("");
		}
		@Override
		public void onTaskCancelled() {
		}
		@Override
		public void onPreExecute() {
			// TODO Auto-generated method stub
			
		}
	}
	
	public class GetActivityCallback extends AsyncCallback {
		public void onTaskComplete(String response) {
			try {
				Log.v(TAG, "GetActivityCallback Response: " + response);
				
				mActivityJSON = new JSONObject(response);
				mActivityList = new ArrayList<HashMap<String,String>>();
				
		        JSONArray list = mActivityJSON.getJSONArray(Constants.JSON_LIST_NAME);
		        int x = list.length();
		        x = x-1;
				for (int i = x; i >=0; i--)
				{
					JSONObject activity = list.getJSONObject(i);
					HashMap<String, String> map = new HashMap<String, String>();
					Double unixTime = Double.valueOf(activity.getString(TAG_TIMESTAMP));  
					long timestamp = (long) (unixTime * 1000L);  // msec
					java.util.Date d = new java.util.Date(timestamp);  
					map.put(TAG_FROM, activity.getString(TAG_FROM));
					map.put(TAG_MESSAGE, activity.getString(TAG_MESSAGE));
					map.put(TAG_TIMESTAMP, d.toString());
//					chatAdapter = new DiscussArrayAdapter(getApplicationContext(), R.layout.listitem_discuss);
					if(activity.getString(TAG_FROM).equalsIgnoreCase(Constants.USER_NAME)){
//						chatMessageArrayList.add(new ChatMessage(activity.getString(TAG_FROM), activity.getString(TAG_MESSAGE), d.toString(), false));
						chatAdapter.add(new ChatMessage(activity.getString(TAG_FROM), activity.getString(TAG_MESSAGE), d.toString(), false));
					}
					else{
//						chatMessageArrayList.add(new ChatMessage(activity.getString(TAG_FROM), activity.getString(TAG_MESSAGE), d.toString(), true));
						chatAdapter.add(new ChatMessage(activity.getString(TAG_FROM), activity.getString(TAG_MESSAGE), d.toString(), true));
					}
					
//					mActivityList.add(map);
				}
				
//				chatAdapter.sort()
//				if(list.length()>1){
//					Collections.reverse(chatMessageArrayList);
//				}
				
//				chatAdapter.notifyDataSetChanged();
				
//				for(int i=0;i<=chatMessageArrayList.size();i++){
//					chatAdapter.add(chatMessageArrayList.get(i));
//				}
//				chatListView.setAdapter(chatAdapter);
				
//				String[] from = { TAG_FROM, TAG_MESSAGE, TAG_TIMESTAMP };
//		        int[] to = { R.id.from, R.id.message, R.id.date };
		        
//				mListView = getListView();
//		        Collections.reverse(mActivityList);
//				ListAdapter adapter = new SimpleAdapter(GroupChatActivity.this, mActivityList,R.layout.groupactivites_listitems, from, to);
//				if(chatListView==null){
//					Log.v(TAG, "ChatListView is NULL!!!");
//				}else{
////				
////					if(mActivityList.size()==1){
////						
////					}
//					chatListView.setAdapter(adapter);
					chatListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
					chatListView.setStackFromBottom(true);
					
//					int firstVisiblePosition = chatListView.getFirstVisiblePosition();
//					View view = chatListView.getChildAt(0);
//					int distFromTop = (view == null) ? 0 : view.getTop();
//					chatListView.setSelectionFromTop(firstVisiblePosition, distFromTop);
					
//				}
				
			}
			catch (JSONException e) {
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
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Constants.chatOpen = true;
		chatAdapter = new DiscussArrayAdapter(getApplicationContext(), R.layout.listitem_discuss);
		chatListView.setAdapter(chatAdapter);
		if (mWifi.isConnected()) {
		    // Do whatever
			Log.d(TAG, "Launching task...");
			new APIClient(GroupChatActivity.this, getApplicationContext(), new GetActivityCallback()).doGetActivity(mGroupName, "5");
			}
			else{
				Toast.makeText(GroupChatActivity.this, "Wifi is not Connected! Please Check your WIFI...", Toast.LENGTH_LONG).show();
			}
	}
	
	 private class ChatListAdapter extends BaseAdapter {
		 
	        private static final int TYPE_ITEM = 0;
	        private static final int TYPE_SEPARATOR = 1;
	        private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;
	 
//	        private ArrayList mData = new ArrayList();
	        private LayoutInflater mInflater;
	        ArrayList<ChatMessage> chatArrayList;
	 
//	        private TreeSet mSeparatorsSet = new TreeSet();
	 
	        public ChatListAdapter(ArrayList<ChatMessage> chatArrayList) {
	            mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            this.chatArrayList = chatArrayList;
	        }
	 
//	        public void addItem(final String item) {
//	            mData.add(item);
//	            notifyDataSetChanged();
//	        }
	 
//	        public void addSeparatorItem(final String item) {
//	            mData.add(item);
//	            // save separator position
//	            mSeparatorsSet.add(mData.size() - 1);
//	            notifyDataSetChanged();
//	        }
	 
//	        @Override
//	        public int getItemViewType(int position) {
//	            return mSeparatorsSet.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
//	        }
	 
	        @Override
	        public int getViewTypeCount() {
	            return TYPE_MAX_COUNT;
	        }
	 
	        @Override
	        public int getCount() {
	            return chatArrayList.size();
	        }
	 
	        @Override
	        public String getItem(int position) {
	            return chatArrayList.get(position).toString();
	        }
	 
	        @Override
	        public long getItemId(int position) {
	            return position;
	        }
	 
	        @Override
	        public View getView(int position, View convertView, ViewGroup parent) {
	            ViewHolder holder = null;
	            int type = getItemViewType(position);
	            System.out.println("getView " + position + " " + convertView + " type = " + type);
	            if (convertView == null) {
	                holder = new ViewHolder();
	                if(!chatArrayList.get(position).getFrom().equalsIgnoreCase(Constants.USER_NAME)){
	                	convertView = mInflater.inflate(R.layout.list_row_layout_left, null);
                        holder.fromTextView = (TextView)convertView.findViewById(R.id.fromTextView);
                        holder.messageTextView = (TextView)convertView.findViewById(R.id.messageTextView);
                        holder.timeStampTextView = (TextView)convertView.findViewById(R.id.timeStampTextView);
	                }
	                else {
	                	convertView = mInflater.inflate(R.layout.list_row_layout_right, null);
	                	holder.fromTextView = (TextView)convertView.findViewById(R.id.fromTextView);
	                	holder.messageTextView = (TextView)convertView.findViewById(R.id.messageTextView);
	                	holder.timeStampTextView = (TextView)convertView.findViewById(R.id.timeStampTextView);
	                }
//	                switch (type) {
//	                    case TYPE_ITEM:
//	                        convertView = mInflater.inflate(R.layout.item1, null);
//	                        holder.textView = (TextView)convertView.findViewById(R.id.text);
//	                        break;
//	                    case TYPE_SEPARATOR:
//	                        convertView = mInflater.inflate(R.layout.item2, null);
//	                        holder.textView = (TextView)convertView.findViewById(R.id.textSeparator);
//	                        break;
//	                }
	                convertView.setTag(holder);
	            } else {
	                holder = (ViewHolder)convertView.getTag();
	            }
	            holder.fromTextView.setText(chatArrayList.get(position).getFrom());
	            holder.messageTextView.setText(chatArrayList.get(position).getMessage());
	            holder.timeStampTextView.setText(chatArrayList.get(position).getTimeStamp());
	            return convertView;
	        }
	 
	    }
	 
	 @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Constants.chatOpen = false;
//		finish();
	}
	 
	    public static class ViewHolder {
	        public TextView fromTextView;
	        public TextView messageTextView;
	        public TextView timeStampTextView;
	    }
	}
