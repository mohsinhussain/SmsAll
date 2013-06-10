package com.hyperon.smsall;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class InfoTabActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_tab);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.info_tab, menu);
		return true;
	}

}
