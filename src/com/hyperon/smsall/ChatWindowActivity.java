package com.hyperon.smsall;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Adapters.MessageView_CustomAdapter;
import Adapters.SingleUserDiscussArrayAdapter;
import Classes.ContactDetails;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChatWindowActivity extends Activity {
	
		private ListView listview;
		ArrayList<ContactDetails> contacts = new ArrayList<ContactDetails>();
		ArrayList<ContactDetails> originalContacts = new ArrayList<ContactDetails>();
		ImageView sendMessageButtonImageView ;
		EditText chatMessageEditText;
//		ArrayList<String> originalContactNames = new ArrayList<String>();
		String mChatmember="";
		String userId = "";
		List<Inbox> listmessages = new ArrayList<Inbox>();
		String tag="SubitemchatlistActivity";
//		ImageView backButtonImageView;
		TextView userName;
		SingleUserDiscussArrayAdapter chatAdapter;
		InboxDataSource inboxmessage;
		String phone = "";
		String contactNameFromPhoneList = "";
		BroadcastReceiver getMessage;
		ConnectivityManager connManager;
		NetworkInfo mWifi;
		boolean receiverRegistered = false;
		byte imageInByte[];
		String fullname = "";
		String ident = "";
		String jsonString = "";
		boolean multipleEntries = false;
		Calendar cal;
//		ArrayList<String> contactNames = new ArrayList<String>();
		ArrayList<String> contactNumbers = new ArrayList<String>();
		Long timestamp;
		String message;
		Inbox f1;
		Inbox f2;
		String status;
		int inttimestamp;
		SharedPreferences mSharedPrefs;
		
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			setContentView(R.layout.onetoone_chat_activity_relative_layout);
			mSharedPrefs = getSharedPreferences(Constants.PREFS_NAME, 0);
			Constants.FULL_NAME = mSharedPrefs.getString("fullname", null);
			chatMessageEditText = (EditText) findViewById(R.id.messageEditText);
			sendMessageButtonImageView = (ImageView) findViewById(R.id.sendButtonImageView);
//			backButtonImageView = (ImageView) findViewById(R.id.backbuttonImageView);
			userName = (TextView) findViewById(R.id.userNameTextView);
			listview = (ListView) findViewById(R.id.groupChatListView);
			connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
			mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			getMessage = new BroadcastReceiver() {
			    @Override
			    public void onReceive(Context context, Intent intent) {
			        // INSERT CODE TO REFRESH LIST VIEW
//			    	chatAdapter = new DiscussArrayAdapter(getApplicationContext(), R.layout.listitem_discuss);
			    	Log.v("SubItemChatListActivity", "I am coming to registeReciever");
			    	listmessages.clear();
			    	loadChatMessages();
					chatAdapter = new SingleUserDiscussArrayAdapter(getApplicationContext(), R.layout.listitem_discuss);
////					chatAdapter.addAll(listmessages);
//					
					for(int i=0;i<listmessages.size();i++){
						chatAdapter.add(listmessages.get(i));
					}
					listview.setAdapter(chatAdapter);
//					Toast.makeText(SubitemchatlistActivity.this, "UserName in Intent: "+mChatmember, Toast.LENGTH_LONG).show();
//					adapter = new ChatView_CustomAdapter(this, R.layout.onetoone_chat_activity_relative_layout, listmessages);
//					chatAdapter.no
			    	listview.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
					listview.setStackFromBottom(true);
//					listview.setAdapter(chatAdapter);
			    }
			};
			receiverRegistered = true;
			registerReceiver(getMessage, new IntentFilter("MyGCMMessageReceived"));
			
//			backButtonImageView.setOnClickListener(new OnClickListener() {
//				public void onClick(View arg0) {
//					// TODO Auto-generated method stub
//					finish();
//				}
//			});
			
			
			
			sendMessageButtonImageView.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					// TODO Auto-generated method stub
//					InboxDataSource inboxmessage=new InboxDataSource(getApplicationContext());
//					Long timestamp= cal.getTimeInMillis();
//			    	int inttimestamp= (int) (timestamp/100);
					 cal= Calendar.getInstance();
			    	message = chatMessageEditText.getText().toString();
			    	if(message.equalsIgnoreCase("")){
			    		Toast.makeText(getApplicationContext(), "Message Field is empty!", Toast.LENGTH_LONG).show();
			    	}
			    	if (mWifi.isConnected()) {
					    // Do whatever
			    		if(userId.length()>0){
				    		new APIClient(ChatWindowActivity.this, getApplicationContext(), 
					    			new PostSingleUserMessageCallbacks ())
					    					.doPostSingleUserMessage(null, userId, message, false);
				    	}
				    	if(phone.length()>0){
				    		new APIClient(ChatWindowActivity.this, getApplicationContext(), 
					    			new PostSingleUserMessageCallbacks ())
					    					.doPostSingleUserMessage(phone, null, message, false);
				    		
				    	}
				    	timestamp= cal.getTimeInMillis();
						Log.v(tag, "cal: "+cal);
						Log.v(tag, "timeStamp: "+timestamp);
				    	inttimestamp= (int) (timestamp/1000);
				    	Log.v(tag, "inttimestamp: "+inttimestamp);
				    	message = chatMessageEditText.getText().toString();
//				    	status = "wait";
				    	
				    	//saving 0 as COLUMN_USER_ID in messages that I send with flag 1
				    	f1=new Inbox(Constants.FULL_NAME,message ,inttimestamp,1,fullname, ident, "wait");
				    	
				    	chatAdapter.add(f1);
				    	
				    	
					}
					else{
						Toast.makeText(ChatWindowActivity.this, "Wifi is not Connected! Please Check your WIFI...", Toast.LENGTH_LONG).show();
					}
			    	
			    	chatMessageEditText.setText("");
			    	
			    	
			    	
			    	
			    	
			    	//SAVE TO SQL CODE
//			    	Inbox f1=new Inbox(Constants.USER_NAME,message ,inttimestamp,1,mChatmember);
//			    	
//			    	inboxmessage.opendatabase();		
//			    	
//			    	Boolean var=inboxmessage.addmessage(f1);
//			    	
//			    		listmessages.add(f1);
//			        	inboxmessage.closedatabase();
//
//			        	
//			        	adapter = new ChatView_CustomAdapter(SubitemchatlistActivity.this, R.layout.onetoone_chat_activity_relative_layout, listmessages);
//			        	listview.setAdapter(adapter);
//			        	chatMessageEditText.setText("");
			        	
			        	
			        	
			        	
			        	
//			        	chatMessageEditText.clearFocus();
//			        	userName.SetFoc
//			    	Log.v(tag, listmessages.get(0).toString());
//			    	adapter.notifyDataSetChanged();
				}
			});

			
			
			// Notification when we click on one of the row
//			listview.setOnItemClickListener(new OnItemClickListener() {
//				public void onItemClick(AdapterView<?> parent, View view,
//						int position, long id) {
//					Toast.makeText(SubitemchatlistActivity.this,
//							"Clicked on full row for "+ listview.getItemAtPosition(position).getFlag(),Toast.LENGTH_SHORT).show();
//					}
//			});
			
			
			////////////////////////////////////////////////////////////////////////////
//			ONRESUME FUNCTIONALITYY IMPLEMENTED IN ONCREATE
			Constants.chatOpen = true;
			Intent intent = getIntent();
			if(intent.hasExtra(Constants.INTENT_KEY_MULTIPLE_CONTACTS_LIST)){
				jsonString = intent.getExtras().getString(Constants.INTENT_KEY_MULTIPLE_CONTACTS_LIST);
				multipleEntries = true;
				Log.v(tag, "JSONARRAY: "+jsonString);
				try {
					JSONArray jsonArray = new JSONArray(jsonString);
					for(int i=0;i<jsonArray.length();i++){
						String name = jsonArray.getJSONObject(i).getString("name");
						String number = jsonArray.getJSONObject(i).getString("number");
						Log.v(tag, "Name: "+name);
						Log.v(tag, "Number: "+number);
						contacts.add(new ContactDetails(name, number, false));
//						contactNames.add(name);
//						contactNumbers.add(number);
					}
					originalContacts.addAll(contacts);
//					originalContactNames.addAll(contactNames);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			if(intent.hasExtra(Constants.INTENT_KEY_USER_FULL_NAME)){
				mChatmember = intent.getExtras().getString(Constants.INTENT_KEY_USER_FULL_NAME);
			}
			if(intent.hasExtra(Constants.INTENT_KEY_USER_ID)){
				userId = intent.getExtras().getString(Constants.INTENT_KEY_USER_ID);
			}
			if(intent.hasExtra(Constants.INTENT_KEY_USER_PHONE)){
				phone = intent.getExtras().getString(Constants.INTENT_KEY_USER_PHONE);
//				contactNameFromPhoneList = intent.getExtras().getString(Constants.inten);
				if(phone.length()==0){
					finish();
				}
			}
			
			Log.v(tag, "mChatNumber:"+mChatmember);
			Log.v(tag, "phone:"+phone);
			Log.v(tag, "UserID:"+userId);
			if(multipleEntries){
				String Names = "";
				for(int i=0; i<contacts.size(); i++){
					
					Names = Names + "("+contacts.get(i).getcontactname()+") ";
				}
				userName.setText(Names);
			}else{
				userName.setText(mChatmember);
				loadChatMessages();
				chatAdapter = new SingleUserDiscussArrayAdapter(getApplicationContext(), R.layout.listitem_discuss);
//				chatAdapter.addAll(listmessages);
				
				for(int i=0;i<listmessages.size();i++){
					chatAdapter.add(listmessages.get(i));
				}
//				listview.setAdapter(chatAdapter);
//				Toast.makeText(SubitemchatlistActivity.this, "UserName in Intent: "+mChatmember, Toast.LENGTH_LONG).show();
//				adapter = new ChatView_CustomAdapter(this, R.layout.onetoone_chat_activity_relative_layout, listmessages);
				listview.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
				listview.setStackFromBottom(true);
				listview.setAdapter(chatAdapter);
//				chatAdapter.sort(new Comparator<Inbox>() {
//				})
//				listview.setAdapter(adapter);
			}
			////////////////////////////////////////////////////////////////////////////
			
			if(multipleEntries){
				
				userName.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// custom dialog
						final Dialog dialog = new Dialog(ChatWindowActivity.this);
						dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						dialog.setContentView(R.layout.custom_dialog);
						dialog.setCancelable(false);
//						final ArrayList<String> originalContactNames =  contactNames;
//						originalContactNames = contactNames;
						MessageView_CustomAdapter namesAdapter = new MessageView_CustomAdapter(ChatWindowActivity.this, contacts);
						
//						dialog.setTitle("Title...");
//			 
//						// set the custom dialog components - text, image and button
						ListView namesListView = (ListView) dialog.findViewById(R.id.contactsListView);
						Button confirmButton = (Button) dialog.findViewById(R.id.confirmButton);
						Button cancelButton = (Button) dialog.findViewById(R.id.CancelButton);
						
						namesListView.setAdapter(namesAdapter);
//						namesAdapter.notifyDataSetChanged();
						
//						dialog.setOnDismissListener(new OnDismissListener() {
							
//							@Override
//							public void onDismiss(DialogInterface arg0) {
//								// TODO Auto-generated method stub
//								Log.v(tag, "At Dismiss contactNames: "+contactNames);
//								Log.v(tag, "At Confirm Button originalNames: "+originalContactNames);
//								contactNames.clear();
//								contactNames.addAll(originalContactNames);
//							}
//						});
						
						cancelButton.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								contacts.clear();
								contacts.addAll(originalContacts);
								String Names = "";
								for(int i=0; i<contacts.size(); i++){
									
									Names = Names + "("+contacts.get(i).getcontactname()+") ";
								}
								userName.setText(Names);
								Log.v(tag, "At Cancel Button contactNames: "+contacts);
								Log.v(tag, "At Cancel Button originalNames: "+originalContacts);
								dialog.dismiss();
							}
						});
						
						confirmButton.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								
								if(contacts.size()<originalContacts.size()){
									String Names = "";
									for(int i=0; i<contacts.size(); i++){
										
										Names = Names + "("+contacts.get(i).getcontactname()+") ";
									}
									userName.setText(Names);
								}
								originalContacts.clear();
								originalContacts.addAll(contacts);
								Log.v(tag, "At Confirm Button contactNames: "+contacts);
								Log.v(tag, "At Confirm Button originalNames: "+originalContacts);
								
								dialog.dismiss();
								if(contacts.size()==0){
									Toast.makeText(getApplicationContext(), "All contacts are removed!", Toast.LENGTH_LONG).show();
									finish();
								}
							}
						});
//						text.setText("Android custom dialog example!");
//						ImageView image = (ImageView) dialog.findViewById(R.id.image);
//						image.setImageResource(R.drawable.ic_launcher);
//			 
//						Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
//						// if button is clicked, close the custom dialog
//						dialogButton.setOnClickListener(new OnClickListener() {
//							@Override
//							public void onClick(View v) {
//								dialog.dismiss();
//							}
//						});
			 
						dialog.show();
					}
				});
			}
		}
		
		@Override
		protected void onStop()
		{
//			if(receiverRegistered){
//				unregisterReceiver(getMessage);
//			}
		    
//		    unregisterReceiver(deliveryBroadcastReceiver);
		    super.onStop();
		}
		
		
		@Override
		protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Constants.chatOpen = false;
		}
		@Override
		protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}

		

		private void loadChatMessages() 
		{
	    	
			InboxDataSource entry= new InboxDataSource(getApplicationContext());
			entry.opendatabase();
			
			Cursor mCursor=entry.getChatMessages(mChatmember);
			
			int ifrom=mCursor.getColumnIndex(InboxMetaData.COLUMN_FROM);
			int imessages=mCursor.getColumnIndex(InboxMetaData.COLUMN_MESSAGE);
			int itimestamp=mCursor.getColumnIndex(InboxMetaData.COLUMN_TIMESTAMP);
			int ito=mCursor.getColumnIndex(InboxMetaData.COLUMN_TO);
	        int iflag=mCursor.getColumnIndex(InboxMetaData.COLUMN_FLAG);
	        int iuserid=mCursor.getColumnIndex(InboxMetaData.COLUMN_USER_ID);
			for(mCursor.moveToFirst();!mCursor.isAfterLast();mCursor.moveToNext())
			{
				listmessages.add(new Inbox(mCursor.getString(ifrom),mCursor.getString(imessages),
						mCursor.getInt(itimestamp),mCursor.getInt(iflag),mCursor.getString(ito),
						mCursor.getString(iuserid)));
			}
			
			Cursor m1Cursor=entry.getChatMessages1(mChatmember);
			
			 ifrom=m1Cursor.getColumnIndex(InboxMetaData.COLUMN_FROM);
			 imessages=m1Cursor.getColumnIndex(InboxMetaData.COLUMN_MESSAGE);
			 itimestamp=m1Cursor.getColumnIndex(InboxMetaData.COLUMN_TIMESTAMP);
			  ito=m1Cursor.getColumnIndex(InboxMetaData.COLUMN_TO);
		      iflag=m1Cursor.getColumnIndex(InboxMetaData.COLUMN_FLAG);
		      iuserid=m1Cursor.getColumnIndex(InboxMetaData.COLUMN_USER_ID);

	       
			for(m1Cursor.moveToFirst();!m1Cursor.isAfterLast();m1Cursor.moveToNext())
			{
				listmessages.add(new Inbox(m1Cursor.getString(ifrom),m1Cursor.getString(imessages),
						m1Cursor.getInt(itimestamp),m1Cursor.getInt(iflag),m1Cursor.getString(ito),
						m1Cursor.getString(iuserid)));
			}
		
			Collections.sort(listmessages, new Comparator<Inbox>() {

				public int compare(Inbox arg0, Inbox arg1)
						{
					if(arg0.getTimestamp()<arg1.getTimestamp())
					{
						return -1;
					}
					else{
						return 1;
					}
//					return 0;
				}

			});
	        entry.closedatabase();
	        
//			return listmessages;
			
		}

		public class PostSingleUserMessageCallbacks extends AsyncCallback {
			public void onTaskComplete(String response) {
				Log.v(tag, "End Response: "+response);
				String status = "";
				String username = "";
				String fullname = "";
				String ident = "";
				if(response.equalsIgnoreCase(Constants.connectionTimeOutMessage)){
					Toast.makeText(getApplicationContext(), Constants.connectionTimeOutMessage, Toast.LENGTH_LONG).show();
				}else{
					
				
				try {
					JSONObject respObject = new JSONObject(response);
					status = respObject.getString("status");
					JSONObject dictionary = respObject.getJSONObject("dictionary");
					username = dictionary.getString("username");
					fullname = dictionary.getString("fullname");
					if((fullname.equalsIgnoreCase("null"))||(fullname.equalsIgnoreCase(""))||
							(fullname.equalsIgnoreCase("no name"))||((fullname.equalsIgnoreCase("noname")))){
						fullname=mChatmember;
					}
					ident = dictionary.getString("ident");
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Log.v(tag, "status: "+status);
				Log.v(tag, "username: "+username);
				Log.v(tag, "fullname: "+fullname);
				Log.v(tag, "ident: "+ident);
				
				
				if (status.equalsIgnoreCase("0")) {
//					Toast.makeText(SubitemchatlistActivity.this, "Message Accepted for delivery", Toast.LENGTH_SHORT).show();
					//SAVE TO SQL CODE
					inboxmessage=new InboxDataSource(getApplicationContext());
//					message = chatMessageEditText.getText().toString();
					f2=new Inbox(Constants.FULL_NAME,message ,inttimestamp,1,fullname, ident);
			    	inboxmessage.opendatabase();		
			    	Boolean var=inboxmessage.addmessage(f2);
			        inboxmessage.closedatabase();

			        
			        //Showing it on screen
//			        chatAdapter.add(f1);
			        chatAdapter.getItem(chatAdapter.getLastItemIndex()).setStatus("sent");
			        chatAdapter.notifyDataSetChanged();
			        chatMessageEditText.setText("");
					
				}
				else {
					Toast.makeText(ChatWindowActivity.this, "Error: "+status, Toast.LENGTH_LONG).show();
					chatAdapter.getItem(chatAdapter.getLastItemIndex()).setStatus("error");
			        chatAdapter.notifyDataSetChanged();
				}
				}
//				chatMessageEditText.setText("");
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
		protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(inboxmessage!=null){
			inboxmessage.closedatabase();
		}
		}
		
		

	public class ChatView_CustomAdapter extends ArrayAdapter<Inbox>
	{
		
		private LayoutInflater	mInflater;
		List<Inbox> objects=new ArrayList<Inbox>();
		
	public ChatView_CustomAdapter(Context context, int textViewResourceId,
			List<Inbox> objects) 
	{
		super(context, textViewResourceId, objects);
		mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.objects = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		
		if (convertView == null) {
			holder = new ViewHolder();
			if(objects.get(position).getFlag() == 0){
				convertView = mInflater.inflate(R.layout.list_row_layout_left, null);
				holder.textName = (TextView)convertView.findViewById(R.id.fromTextView);
				holder.message = (TextView)convertView.findViewById(R.id.messageTextView);
				holder.timestamp = (TextView)convertView.findViewById(R.id.timeStampTextView);
			}
			else if(objects.get(position).getFlag() == 1){
				convertView = mInflater.inflate(R.layout.list_row_layout_right, null);
				holder.textName = (TextView)convertView.findViewById(R.id.fromTextView);
				holder.message = (TextView)convertView.findViewById(R.id.messageTextView);
				holder.timestamp = (TextView)convertView.findViewById(R.id.timeStampTextView);
			}	                
			Log.v(tag,"Adapter Flag: "+Integer.toString(objects.get(position).getFlag()));
			convertView.setTag(holder);
		}
//		if (convertView == null) {
//			holder = new ViewHolder();
//			if(objects.get(position).getfrom().equalsIgnoreCase(Constants.USER_NAME)){
//				convertView = mInflater.inflate(R.layout.list_row_layout_right, null);
//				holder.textName = (TextView)convertView.findViewById(R.id.fromTextView);
//				holder.message = (TextView)convertView.findViewById(R.id.messageTextView);
//				holder.timestamp = (TextView)convertView.findViewById(R.id.timeStampTextView);
//			}
//			else{
//				convertView = mInflater.inflate(R.layout.list_row_layout_left, null);
//				holder.textName = (TextView)convertView.findViewById(R.id.fromTextView);
//				holder.message = (TextView)convertView.findViewById(R.id.messageTextView);
//				holder.timestamp = (TextView)convertView.findViewById(R.id.timeStampTextView);
//			}	                
//			Log.v(tag,"Adapter Flag: "+Integer.toString(objects.get(position).getFlag()));
//			convertView.setTag(holder);
//		}
		
		else {
			
			holder = (ViewHolder) convertView.getTag();
		}
		holder.textName.setText(objects.get(position).getfrom() );
		holder.message.setText(objects.get(position).getMessage());
		Date d= new Date(objects.get(position).getTimestamp());
		holder.timestamp.setText(String.valueOf(d.toString()));
		Log.v(tag,String.valueOf(d.toString()));
		
		return convertView;
	}

		class ViewHolder {
		TextView textName;
		TextView timestamp;
		TextView textto;
		TextView message;
	}

}

	}
