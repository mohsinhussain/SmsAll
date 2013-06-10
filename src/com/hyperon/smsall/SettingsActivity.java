package com.hyperon.smsall;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_relative_layout);
	}
}
