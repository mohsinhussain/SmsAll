package com.hyperon.smsall;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import Adapters.OnetoOneChatUserListAdapter;
import Classes.ChatHistory;
import Classes.UserOnetoOne;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class TestActivity extends Activity implements DialogCreateGroup.ComposeDialogListener  {
	private String TAG = "TestActivity";
	Activity activity = TestActivity.this;
	OnetoOneChatUserListAdapter chatAdapter;
	ArrayList<UserOnetoOne> userChatList = new ArrayList<UserOnetoOne>();
	ArrayList<ChatHistory> userChatHistory = new ArrayList<ChatHistory>();
	ImageView groupButtonImageView;
	ImageView chatButtonImageView;
	Button createGroup;
	ListView chatListview;
	ListView groupListview;
	Button composeNewMessageButton;
	ArrayList<Inbox> inboxArrayList = new ArrayList<Inbox>();
	Calendar cal = Calendar.getInstance();
	ArrayList<String> userName = new ArrayList<String>();
	ConnectivityManager connManager;
	NetworkInfo mWifi;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_test);
		
		connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		
		
		userChatHistory.add(new ChatHistory("My name is Mohsin", "12:01"));
		userChatHistory.add(new ChatHistory("My name is Shahroz", "12:02"));
		userChatList.add(new UserOnetoOne("Mohsin", userChatHistory));
		groupButtonImageView = (ImageView) findViewById(R.id.groupButtonImageView);
		chatButtonImageView = (ImageView) findViewById(R.id.chatButtonImageView);
		groupListview = (ListView) findViewById(R.id.groupListView);
		chatListview = (ListView) findViewById(R.id.chatListView);
		createGroup = (Button) findViewById(R.id.createGroupButton);
		composeNewMessageButton = (Button) findViewById(R.id.composeMessageButton);
		groupButtonImageView.setImageDrawable(this.getResources().getDrawable(R.drawable.group_button_highlighted));
		chatButtonImageView.setImageDrawable(this.getResources().getDrawable(R.drawable.chat_button_normal));
		if (mWifi.isConnected()) {
		    // Do whatever
			groupListview.setAdapter(new ImageAdapter(this));
		}
		else{
			Toast.makeText(activity, "Wifi is not Connected! Please Check your WIFI...", Toast.LENGTH_LONG).show();
		}
		
		
		
//		chatAdapter = new OnetoOneChatUserListAdapter(activity, userName);
//		chatListview.setAdapter(chatAdapter);
//		chatAdapter.notifyDataSetChanged();
		
		groupListview.setVisibility(View.VISIBLE);
		chatListview.setVisibility(View.GONE);
//		createGroup.setVisibility(View.VISIBLE);
//		composeNewMessageButton.setVisibility(View.GONE);
		
		//Mohsin Ident = 2897357
		//Waqas Farooq Ident = 3047
		
		
//		int flagvalue = 0;
//		String to= Constants.FULL_NAME;
//		String user_id = "3047";
//    	String from="Waqas Farooq";
//    	String messsage="So wat is up? Any thing new???";
//    	Long timestamp= cal.getTimeInMillis();
//    	int inttimestamp= (int) (timestamp/100);
//    	InboxDataSource inboxmessage=new InboxDataSource(getApplicationContext());
//    	Inbox f1=new Inbox(from, messsage,inttimestamp,flagvalue,to, user_id );
//    	
//    	inboxmessage.opendatabase();		
//    	
//    	Boolean var=inboxmessage.addmessage(f1);
//    	if(var){
//    		Log.v(TAG, "Entry has been added");
//    	}
//    	
//    	inboxmessage.closedatabase();
    	
    	composeNewMessageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(TestActivity.this, AddContactstoOnetoOneActivity.class);
				startActivity(i);
			}
		});
		
		
		createGroup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				DialogCreateGroup.newInstance().show(getFragmentManager(),
						  "DialogComposeMessage");
			}
		});
		
		
		groupButtonImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				groupButtonImageView.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.group_button_highlighted));
				chatButtonImageView.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.chat_button_normal));
				groupListview.setVisibility(View.VISIBLE);
				chatListview.setVisibility(View.GONE);
//				createGroup.setVisibility(View.VISIBLE);
//				composeNewMessageButton.setVisibility(View.GONE);
			}
		});
		
		
		chatButtonImageView.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				groupButtonImageView.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.group_button_normal));
				chatButtonImageView.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.chat_button_highlighted));
				groupListview.setVisibility(View.GONE);
				chatListview.setVisibility(View.VISIBLE);
//				createGroup.setVisibility(View.GONE);
//				composeNewMessageButton.setVisibility(View.VISIBLE);
				
						}
		});
		
		chatListview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//	            Toast.makeText(TestActivity.this, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
	            
//	            Intent intent =new Intent(getApplicationContext(),GroupInfoActivity.class);
	            Intent intent =new Intent(getApplicationContext(),ChatWindowActivity.class);
	            Log.v(TAG, "inboxArrayList: "+inboxArrayList.get(position).getClass().toString());
	            Log.v(TAG, "userId: "+inboxArrayList.get(position).getUserId());
	            Log.v(TAG, "from: "+inboxArrayList.get(position).getfrom());
	            Log.v(TAG, "to: "+inboxArrayList.get(position).getTo());
	            Log.v(TAG, "flag: "+inboxArrayList.get(position).getFlag());
	            Log.v(TAG, "message: "+inboxArrayList.get(position).getMessage());
	            if(inboxArrayList.get(position).getFlag()==0){
	            	intent.putExtra(Constants.INTENT_KEY_USER_FULL_NAME ,inboxArrayList.get(position).getfrom());
	            }else{
	            	intent.putExtra(Constants.INTENT_KEY_USER_FULL_NAME ,inboxArrayList.get(position).getTo());
	            }
	            
	            intent.putExtra(Constants.INTENT_KEY_USER_ID ,inboxArrayList.get(position).getUserId());
//	            Toast.makeText(activity, "UserName: "+parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
	            
	            startActivity(intent);
	        }
	    });

		groupListview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//	            Toast.makeText(TestActivity.this, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
	            
//	            Intent intent =new Intent(getApplicationContext(),GroupInfoActivity.class);
	            Intent intent =new Intent(getApplicationContext(),GroupChatActivity.class);
	            intent.putExtra(Constants.INTENT_KEY_GROUP_NAME ,parent.getItemAtPosition(position).toString());
	            
	            startActivity(intent);
	        }
	    });
	}
	
	private ArrayList<Inbox> loadMessages() {
    	ArrayList<Inbox> listmessages = new ArrayList<Inbox>();
		InboxDataSource entry= new InboxDataSource(TestActivity.this);
		entry.opendatabase();
		
		Cursor mCursor=entry.getListofAllMessages();
		
		int ifrom=mCursor.getColumnIndex(InboxMetaData.COLUMN_FROM);
		int imessages=mCursor.getColumnIndex(InboxMetaData.COLUMN_MESSAGE);
		int itimestamp=mCursor.getColumnIndex(InboxMetaData.COLUMN_TIMESTAMP);
		int iflag=mCursor.getColumnIndex(InboxMetaData.COLUMN_FLAG);
		int ito=mCursor.getColumnIndex(InboxMetaData.COLUMN_TO);
		int iuserid=mCursor.getColumnIndex(InboxMetaData.COLUMN_USER_ID);
        
		for(mCursor.moveToFirst();!mCursor.isAfterLast();mCursor.moveToNext())
		{
			listmessages.add(new Inbox(mCursor.getString(ifrom),mCursor.getString(imessages),
					mCursor.getInt(itimestamp), mCursor.getInt(iflag), mCursor.getString(ito), mCursor.getString(iuserid)));
		}
		
		
		entry.closedatabase();
		return listmessages;
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		inboxArrayList.clear();
		inboxArrayList = loadMessages();
		userName = new ArrayList<String>();
		for(int i=0;i<inboxArrayList.size();i++){
			if(inboxArrayList.get(i).getFlag() == 0){
				userName.add(inboxArrayList.get(i).getfrom());
				Log.v(TAG, "USER ID IN RESUME: "+ inboxArrayList.get(i).getUserId());
			}
			else if(inboxArrayList.get(i).getFlag() == 1){
				userName.add(inboxArrayList.get(i).getTo());
				Log.v(TAG, "USER ID IN RESUME: "+ inboxArrayList.get(i).getUserId());
			}
		}
//		Toast.makeText(activity, "Chat Users List: "+userName.toString(), Toast.LENGTH_LONG).show();
		
		ArrayAdapter<String> arrayAdapter =      
		         new ArrayAdapter<String>(activity,R.layout.list_item,R.id.text, userName);
		
//		setListAdapter(new ArrayAdapter<String>(activity, R.layout.list_item, R.id.text, userName));
//		{
//
//		             @Override
//		             public View getView(int position, View convertView,
//		                     ViewGroup parent) {
//		                 View view =super.getView(position, convertView, parent);
//
//		                 TextView textView=(TextView) view.findViewById(android.R.id.text1);
//
//		                 /*YOUR CHOICE OF COLOR*/
//		                 textView.setTextColor(Color.BLACK);
//
//		                 return view;
//		             }
//		         };
		         chatListview.setAdapter(arrayAdapter);

	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test, menu);
		return true;
	}

	@Override
	public void onDialogPositiveClick(String groupName) {
		// TODO Auto-generated method stub
		if(groupName.equalsIgnoreCase("")){
			Toast.makeText(getApplicationContext(), "Please Enter Valid Text in Group Name", Toast.LENGTH_LONG).show();
		}else{
			if (mWifi.isConnected()) {
			    // Do whatever
				Log.d(TAG, "Creating Group...");
				new APIClient(TestActivity.this, this, new PostCreateGroupCallbacks()).doPostCreateGroup(groupName);
			}
			else{
				Toast.makeText(activity, "Wifi is not Connected! Please Check your WIFI...", Toast.LENGTH_LONG).show();
			}
			
		}
		
	}

	@Override
	public void onDialogNegativeClick() {
		// TODO Auto-generated method stub
		
	}
	
	public class PostCreateGroupCallbacks extends AsyncCallback {
		public void onTaskComplete(String response) {
			String status = "";
//			response.
			if(response.equalsIgnoreCase(Constants.connectionTimeOutMessage)){
				Toast.makeText(TestActivity.this, Constants.connectionTimeOutMessage, Toast.LENGTH_LONG).show();
			}
			else{
				try {
					JSONObject Json = new JSONObject(response);
					status = Integer.toString(Json.getInt("status"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (status.equalsIgnoreCase("0")) {
//					Toast.makeText(TestActivity.this, "Group Created", Toast.LENGTH_SHORT).show();
					groupListview.setAdapter(new ImageAdapter(activity));
//					groupListview.setAdapter(chatAdapter);
//					new APIClient(GroupChatActivity.this, getApplicationContext(), new GetActivityCallback()).doGetActivity(mGroupName, "1");
				}
				else {
					Toast.makeText(TestActivity.this, "An error occurred during creation of Group. Please try again later!!", Toast.LENGTH_LONG).show();
				}
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
	
	

}
