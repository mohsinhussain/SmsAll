package com.hyperon.smsall;

import com.hyperon.smsall.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

public class FullscreenActivity extends Activity 
	implements AdapterView.OnItemSelectedListener {
	private TextView selection;
	private static final String[] items={"lorem", "ipsum", "dolor",
	          "sit", "amet",
	          "consectetuer", "adipiscing", "elit", "morbi", "vel",
	          "ligula", "vitae", "arcu", "aliquet", "mollis",
	          "etiam", "vel", "erat", "placerat", "ante",
	          "porttitor", "sodales", "pellentesque", "augue", "purus"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_fullscreen);

		GridView gridView = (GridView) findViewById(R.id.grid_screen);
		gridView.setAdapter(new ArrayAdapter<String>(this, R.layout.cell, items));
		
		selection=(TextView)findViewById(R.id.selection);
		
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		selection.setText(items[arg2]);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		selection.setText("");
	}

}
