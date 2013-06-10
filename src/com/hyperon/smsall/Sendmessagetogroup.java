package com.hyperon.smsall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Sendmessagetogroup extends Activity {

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sendgroup_message);


		Intent in =getIntent();
		
		//TextView gn = (TextView) findViewById(R.id.groupname);
		
		//gn.setText(groupname);
	
	}

	
}
