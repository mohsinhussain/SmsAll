package com.hyperon.smsall;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.Toast;

public class GroupInfoActivity extends FragmentActivity
	implements DialogComposeMessage.ComposeDialogListener
{
	public String mGroupName;
	private static String TAG = "GroupInfoActivity";
	private String INFO_TAG = "info";
	private String MEMBERS_TAG = "members";
	private String ACTIVITY_TAG = "activity";
	
	private TabHost mTabHost;
	public class TabContent implements TabContentFactory{
		private Context mContext;
		
		public TabContent(Context context){
			mContext = context;
		}
		@Override
		public View createTabContent(String tag) {
			View v = new View(mContext);
			return v;
		}
	}
	public String getGroup() {
		return mGroupName;
	}
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_group_info);

		Intent intent = getIntent();
		mGroupName = intent.getStringExtra(Constants.INTENT_KEY_GROUP_NAME);
        
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();
        
        TabHost.OnTabChangeListener tabChangeListener = new TabHost.OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				android.support.v4.app.FragmentManager fm =   getSupportFragmentManager();
				GroupTabFragment groupTabFragment = (GroupTabFragment) fm.findFragmentByTag(INFO_TAG);
				MembersTabFragment membersTabFragment = (MembersTabFragment) fm.findFragmentByTag(MEMBERS_TAG);
				ActivityTabFragment activityTabFragment =(ActivityTabFragment) fm.findFragmentByTag(ACTIVITY_TAG);
				android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				
				if (groupTabFragment != null)
					ft.detach(groupTabFragment);
				
				if (membersTabFragment != null)
					ft.detach(membersTabFragment);
				
				if (activityTabFragment != null)
					ft.detach(activityTabFragment);
				
				if (tabId.equalsIgnoreCase(INFO_TAG)) {
					if (groupTabFragment == null)	
						ft.add(R.id.realtabcontent, new GroupTabFragment(), INFO_TAG);
					else
						ft.attach(groupTabFragment);						
					
				} else if(tabId.equalsIgnoreCase(MEMBERS_TAG)){
					if (membersTabFragment == null)
						ft.add(R.id.realtabcontent, new MembersTabFragment(), MEMBERS_TAG);						
					else
						ft.attach(membersTabFragment);
				}
				else
				{
					if (activityTabFragment == null)
						ft.add(R.id.realtabcontent, new ActivityTabFragment(), ACTIVITY_TAG);						
					else
						ft.attach(activityTabFragment);
				}
				ft.commit();
			}
		};

		mTabHost.setOnTabChangedListener(tabChangeListener);
                
        TabHost.TabSpec tSpecInfo = mTabHost.newTabSpec(INFO_TAG);
        tSpecInfo.setIndicator("Info", getResources().getDrawable(android.R.drawable.ic_dialog_alert));      
        tSpecInfo.setContent(new TabContent(getBaseContext()));
        mTabHost.addTab(tSpecInfo);
        
        TabHost.TabSpec tSpecMembers = mTabHost.newTabSpec(MEMBERS_TAG);
        tSpecMembers.setIndicator("Members", getResources().getDrawable(R.drawable.memberstab_selection));        
        tSpecMembers.setContent(new TabContent(getBaseContext()));
        mTabHost.addTab(tSpecMembers);
        
        TabHost.TabSpec tSpecActivity = mTabHost.newTabSpec(ACTIVITY_TAG);
        tSpecActivity.setIndicator("Activity", getResources().getDrawable(R.drawable.activitytab_selection));        
        tSpecActivity.setContent(new TabContent(getBaseContext()));
        mTabHost.addTab(tSpecActivity);
	}
	
//	public void onClickCompose(View view) {
//		DialogComposeMessage.newInstance(mGroupName).show(getFragmentManager(),
//														  "DialogComposeMessage");
//	}

	@Override
	public void onDialogPositiveClick(String message, Boolean replyAll) {
		Log.d(TAG, "Launching task...");
		new APIClient(GroupInfoActivity.this, this, new PostMessageCallbacks ()).doPostMessage(mGroupName, message, replyAll);
	}

	@Override
	public void onDialogNegativeClick() {
		Toast.makeText(GroupInfoActivity.this, "Message Discarded", Toast.LENGTH_SHORT).show();
	}

	public class PostMessageCallbacks extends AsyncCallback {
		public void onTaskComplete(String response) {
			if (response == Constants.Success.toString()) {
//				Toast.makeText(GroupInfoActivity.this, "Message Accepted for delivery", Toast.LENGTH_SHORT).show();
			}
			else {
				Toast.makeText(GroupInfoActivity.this, "An error occurred during login. Please try again later!!", Toast.LENGTH_LONG).show();
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


