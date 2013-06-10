package com.hyperon.smsall;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MessageFragment extends Fragment {
    /** (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
	ConnectivityManager connManager;
	NetworkInfo mWifi;
	ListView chatListview;
	ArrayList<Inbox> inboxArrayList = new ArrayList<Inbox>();
	ArrayList<String> userName = new ArrayList<String>();
	BroadcastReceiver updateList;
	
	private String TAG = "MessageFragment";
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
        if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }
        
          View layout = inflater.inflate(R.layout.message_fragment_layout, container, false);
          
          chatListview = (ListView) layout.findViewById(R.id.chatListView);
        return layout;
    }
    
    
    
    @Override
    public void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    	
    	connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		updateList = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
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
//				Toast.makeText(getActivity(), "Chat Users List: "+userName.toString(), Toast.LENGTH_LONG).show();
				
//				MessagesAdapter adapter = new MessagesAdapter(getActivity(), R.layout.inbox_listitems, loadMessages());
				ArrayAdapter<String> arrayAdapter =      
				         new ArrayAdapter<String>(getActivity(),R.layout.list_item,R.id.text, userName);
				chatListview.setAdapter(arrayAdapter);
			}
			
		};
		
		getActivity().registerReceiver(updateList, new IntentFilter("MyGCMMessageReceived"));
		
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
//		Toast.makeText(getActivity(), "Chat Users List: "+userName.toString(), Toast.LENGTH_LONG).show();
		
//		MessagesAdapter adapter = new MessagesAdapter(getActivity(), R.layout.inbox_listitems, loadMessages());
		ArrayAdapter<String> arrayAdapter =      
		         new ArrayAdapter<String>(getActivity(),R.layout.list_item,R.id.text, userName);
		chatListview.setAdapter(arrayAdapter);
//		chatListview.setAdapter(adapter);
		
		chatListview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//	            Toast.makeText(getActivity(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
	            
//	            Intent intent =new Intent(getApplicationContext(),GroupInfoActivity.class);
	            Intent intent =new Intent(getActivity(),ChatWindowActivity.class);
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

    }
    
   
    
    private ArrayList<Inbox> loadMessages() {
    	ArrayList<Inbox> listmessages = new ArrayList<Inbox>();
		InboxDataSource entry= new InboxDataSource(getActivity());
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
    
    
	
    
   
}

