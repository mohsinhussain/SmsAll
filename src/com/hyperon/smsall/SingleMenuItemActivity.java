package com.hyperon.smsall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SingleMenuItemActivity  extends Activity {
	
	// JSON node keys
	private static final String TAG_NAME = "to";
	private static final String TAG_CONTENT = "content";
	private static final String TAG_FROM = "from";
	private static final String TAG_ts = "from";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_item);
        
        // getting intent data
        Intent in = getIntent();
        
        // Get JSON values from previous intent
        String name = in.getStringExtra(TAG_NAME);
        String content = in.getStringExtra(TAG_CONTENT);
        String from = in.getStringExtra(TAG_FROM);
        String timestamp = in.getStringExtra(TAG_ts);
        
        // Displaying all values on the screen
        TextView lblName = (TextView) findViewById(R.id.name_label);
        TextView lblContent = (TextView) findViewById(R.id.content_label);
        TextView lblfrom = (TextView) findViewById(R.id.from_label);
        TextView lblts = (TextView) findViewById(R.id.ts_label);
        
        lblName.setText(name);
        lblContent.setText(content);
        lblfrom.setText(from);
        lblts.setText(timestamp);
        
    }
}
