package com.hyperon.smsall;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;

public class TabsFragmentActivity extends FragmentActivity implements
		TabHost.OnTabChangeListener {

	private TabHost mTabHost;
	private HashMap mapTabInfo = new HashMap();
	private TabInfo mLastTab = null;

	private class TabInfo {
		private String tag;
		private Class clss;
		private Bundle args;
		private Fragment fragment;

		TabInfo(String tag, Class clazz, Bundle args) {
			this.tag = tag;
			this.clss = clazz;
			this.args = args;
		}

	}

	class TabFactory implements TabContentFactory {

		private final Context mContext;

		/**
		 * @param context
		 */
		public TabFactory(Context context) {
			mContext = context;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
		 */
		public View createTabContent(String tag) {
			View v = new View(mContext);
			v.setMinimumWidth(97);
			v.setMinimumHeight(0);
			if(tag.equalsIgnoreCase("messageFragment")){
				v.setBackgroundDrawable(getResources().getDrawable(R.drawable.message_tab_selector));
			}
			else if(tag.equalsIgnoreCase("composeFragment")){
				v.setBackgroundDrawable(getResources().getDrawable(R.drawable.compose_tab_selector));
			}
			else if(tag.equalsIgnoreCase("groupFragment")){
				v.setBackgroundDrawable(getResources().getDrawable(R.drawable.group_tab_selector));
			}
			else if(tag.equalsIgnoreCase("hashFragment")){
				v.setBackgroundDrawable(getResources().getDrawable(R.drawable.hash_tab_selector));
			}
			else if(tag.equalsIgnoreCase("profileFragment")){
				v.setBackgroundDrawable(getResources().getDrawable(R.drawable.profile_tab_selector));
			}
			return v;
		}

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Step 1: Inflate layout
		setContentView(R.layout.tabs_layout);
		// Step 2: Setup TabHost
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		initialiseTabHost(savedInstanceState);
		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
			// set the tab as per the
			// saved
			// state
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
	 */
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("tab", mTabHost.getCurrentTabTag()); // save the tab
																// selected
		super.onSaveInstanceState(outState);
	}

	/**
	 * Step 2: Setup TabHost
	 */
	private void initialiseTabHost(Bundle args) {
		Drawable messageSelector = getResources().getDrawable(R.drawable.message_tab_selector);
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		TabInfo tabInfo = null;
		TabsFragmentActivity.addTab(this, this.mTabHost, this.mTabHost
				.newTabSpec("messageFragment").setIndicator(""),
				(tabInfo = new TabInfo("messageFragment",
						MessageFragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		TabsFragmentActivity.addTab(this, this.mTabHost, this.mTabHost
				.newTabSpec("composeFragment").setIndicator(""),
				(tabInfo = new TabInfo("composeFragment",
						ComposeFragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		TabsFragmentActivity.addTab(this, this.mTabHost, this.mTabHost
				.newTabSpec("groupFragment").setIndicator(""),
				(tabInfo = new TabInfo("groupFragment", GroupFragment.class,
						args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		TabsFragmentActivity.addTab(this, this.mTabHost, this.mTabHost
				.newTabSpec("hashFragment").setIndicator(""),
				(tabInfo = new TabInfo("hashFragment", GroupFragment.class,
						args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		TabsFragmentActivity.addTab(this, this.mTabHost, this.mTabHost
				.newTabSpec("profileFragment").setIndicator("Profile", messageSelector),
				(tabInfo = new TabInfo("profileFragment",
						ProfileFragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		// Default to first tab
		this.onTabChanged("composeFragment");
		mTabHost.setCurrentTab(1);
		View v = mTabHost.getCurrentTabView();
//		v.setbac(messageSelector);

		//
		mTabHost.setOnTabChangedListener(this);
	}

	/**
	 * @param activity
	 * @param tabHost
	 * @param tabSpec
	 * @param clss
	 * @param args
	 */
	@SuppressLint("NewApi")
	private static void addTab(TabsFragmentActivity activity, TabHost tabHost,
			TabHost.TabSpec tabSpec, TabInfo tabInfo) {
		// Attach a Tab view factory to the spec
		tabSpec.setContent(activity.new TabFactory(activity));
		String tag = tabSpec.getTag();
		tabSpec.setIndicator(activity.new TabFactory(activity).createTabContent(tag));
		

		// Check to see if we already have a fragment for this tab, probably
		// from a previously saved state. If so, deactivate it, because our
		// initial state is that a tab isn't shown.
		tabInfo.fragment = activity.getSupportFragmentManager()
				.findFragmentByTag(tag);
		if (tabInfo.fragment != null && !tabInfo.fragment.isDetached()) {
			FragmentTransaction ft = activity.getSupportFragmentManager()
					.beginTransaction();
			ft.detach(tabInfo.fragment);
			ft.commit();
			activity.getSupportFragmentManager().executePendingTransactions();
		}

		tabHost.addTab(tabSpec);
		
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
	 */
	public void onTabChanged(String tag) {
		TabInfo newTab = (TabInfo) mapTabInfo.get(tag);
//		mTabHost.getCurrentTabView().setBackground(background);
		if (mLastTab != newTab) {
			FragmentTransaction ft = this.getSupportFragmentManager()
					.beginTransaction();
			if (mLastTab != null) {
				if (mLastTab.fragment != null) {
					ft.detach(mLastTab.fragment);
				}
			}
			if (newTab != null) {
				if (newTab.fragment == null) {
					newTab.fragment = Fragment.instantiate(this,
							newTab.clss.getName(), newTab.args);
					ft.add(R.id.realtabcontent, newTab.fragment, newTab.tag);
				} else {
					ft.attach(newTab.fragment);
				}
			}

			
			mLastTab = newTab;
			ft.commit();
			this.getSupportFragmentManager().executePendingTransactions();
		}
	}

}