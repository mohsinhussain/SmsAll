package com.hyperon.smsall;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class JsonParser extends Activity{

	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "to";
	private static final String TAG_CONTENT = "content";
	private static final String TAG_ts = "ts";
	private static final String TAG_FROM = "from";
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_json_parser);
		
		// Hashmap for ListView
		ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
			
		Intent i=getIntent();
		String resp=i.getStringExtra("jsonstring");

		try {
			
			Log.v("I am in jsonparsor", resp);
			//String id,content,from,to,ts;
			JSONObject uniObject = new JSONObject(resp);
			
			JSONArray array = uniObject.getJSONArray("lst");

			for (int io = 0; io < array.length(); io++) {
			    JSONObject row = array.getJSONObject(io);
			    
				// Storing each json item in variable
				String ts = row.getString(TAG_ts);
				String name = row.getString(TAG_NAME);
				String email = row.getString(TAG_CONTENT);
				String from = "from"+ " " + row.getString(TAG_FROM);
				
				/*
				java.util.Date time=new java.util.Date((long) Long.parseLong(ts) *1000);
				 SimpleDateFormat format = new SimpleDateFormat("MMddyyHHmmss");
				 
				  time = format.parse(ts);
				  */
				  ts="at"+ "-" + " " + ts;
				
				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();
				
				// adding each child node to HashMap key => value
				map.put(TAG_ts, ts);
				map.put(TAG_NAME, name);
				map.put(TAG_CONTENT, email);
				map.put(TAG_FROM, from);

				// adding HashList to ArrayList
				contactList.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		/*catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		/**
		 * Updating parsed JSON data into ListView
		 * */
		ListAdapter adapter = new SimpleAdapter(this, contactList,
				R.layout.activity_list_items,
				new String[] { TAG_NAME, TAG_CONTENT, TAG_FROM ,TAG_ts}, new int[] {
						R.id.name, R.id.content, R.id.from , R.id.time});

		
		//setListAdapter(adapter);

		// selecting single ListView item
		ListView lv = (ListView) findViewById(R.id.mylist);

		lv.setAdapter(adapter);
		
		// Launching new screen on Selecting Single ListItem
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String name = ((TextView) view.findViewById(R.id.name)).getText().toString();
				String content = ((TextView) view.findViewById(R.id.content)).getText().toString();
				String from = ((TextView) view.findViewById(R.id.from)).getText().toString();
				String timestamp =((TextView) view.findViewById(R.id.time)).getText().toString();
				// Starting new intent
				Intent in = new Intent(getApplicationContext(), SingleMenuItemActivity.class);
				in.putExtra(TAG_NAME, name);
				in.putExtra(TAG_CONTENT, content);
				in.putExtra(TAG_FROM, from);
				in.putExtra(TAG_ts, timestamp);
				startActivity(in);

			}
		});

	}

	
	
}
