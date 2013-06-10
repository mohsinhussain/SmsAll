package com.hyperon.smsall;

import java.net.URLEncoder;
import java.util.ArrayList;

import Classes.ContactDetails;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class AddContactstoOnetoOneActivity extends Activity {
	private MyCustomAdapter mdataAdapter = null;
//	private ArrayList<HashMap<String, String>> contactlist = new ArrayList<HashMap<String, String>>();
    Intent mintent = new Intent();
//    private HashMap<String, String> map;
    RadioButton mSelectedRB;
    String number = "";
    String userFullName = "";
    EditText searchEditText;
    String Tag = "AddContactstoOnetoOneActivity";

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_add_contactsto_onetoone);
        searchEditText = (EditText) findViewById(R.id.search);
       
    	Intent i= getIntent();
        mintent =i;
    	displayListView();  
    	ADDButton();
    	
    }
 
    public void ADDButton() {
   	 
    	Button button= (Button) findViewById(R.id.select);
    	button.setOnClickListener(new View.OnClickListener() {
    	    @Override
    	    public void onClick(View v) {
    	    	StringBuffer responseText = new StringBuffer();
    		    responseText.append("The following were selected...\n");
    		    
    		    ArrayList<ContactDetails> countryList = mdataAdapter.contactlist;
    		    for(int i=0;i<countryList.size();i++){
    		    	ContactDetails contact = countryList.get(i);
    		     if(contact.isSelected()){
    		    	 number = contact.getcontactnumber();
    		    	 userFullName = contact.getcontactname();
    		      responseText.append("\n" + contact.getcontactnumber() + "&" + contact.getcontactname());
//    		       map = new HashMap<String, String>();
//    		      	map.put("contactnumber", contact.getcontactnumber() );
//    		        map.put("contactname", contact.getcontactname());
//    		        contactlist.add(map);
    		     }
    		    }
    		   number=number.replace(" ", "");
 		       number=number.replace("-", "");
 		       number=number.replace("(", "");
 		       number=number.replace(")", "");
 		       number=number.replace("+", "");
 		       userFullName=userFullName.replace("-", "");
 		       userFullName=userFullName.replace("(", "");
 		       userFullName=userFullName.replace(")", "");
 		       userFullName=userFullName.replace("+", "");
 		       if(number.length()>10){
 		       if(number.substring(0, 4).equalsIgnoreCase("0092")){
 					String x = number.substring(0, 4);
 					number = number.replaceFirst(x, "+92");
 				}
 				else if(number.substring(0, 2).equalsIgnoreCase("92")){
 					String x = number.substring(0, 2);
 					number = number.replaceFirst(x, "+92");
 				}
 				else if(number.substring(0, 2).equalsIgnoreCase("03")){
 					String x = number.substring(0, 2);
 					number = number.replaceFirst(x, "+923");
 				}
 		       }
 		       userFullName = URLEncoder.encode(userFullName);
 		       number = URLEncoder.encode(number);
 		       
 		       Log.v(Tag, "Number Selected: "+number);
    		 Intent i = new Intent(AddContactstoOnetoOneActivity.this, ChatWindowActivity.class);
    		 i.putExtra(Constants.INTENT_KEY_USER_PHONE, number);
//    		 i.putExtra(Constants.INTENT_KEY_USER_NAME, number);
    		 i.putExtra(Constants.INTENT_KEY_USER_FULL_NAME, userFullName);
    		 startActivity(i);
    		 finish();
    		 
    		    
    		    
//    		    Toast.makeText(getApplicationContext(),
//    		      responseText, Toast.LENGTH_LONG).show();

//    			Messenger messngr= mintent.getParcelableExtra("handler");
//    		 	
//    		     Message message= Message.obtain();
//    				message.arg1=2;
//    				message.obj= contactlist;
//    				try {
//    					messngr.send(message);
//    				} catch (RemoteException e) {
//    					// TODO Auto-generated catch block
//    					e.printStackTrace();
//    				}
    		    
       		   }

    	    
    	});
    	
}
    
    private void displayListView() {
    	 
    	

    	  ArrayList<ContactDetails> contactlist = new ArrayList<ContactDetails>();
    	
    	  Cursor cursor = getContentResolver().query(Phone.CONTENT_URI, null, null,null, Phone.DISPLAY_NAME + " ASC");
          // now we have cusror with contacts and get diffrent value from cusror.
           while (cursor.moveToNext()) {
           String contactname =cursor.getString(cursor.getColumnIndex(Phone.DISPLAY_NAME));
           String contactphoneNumber = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
           ContactDetails country = new ContactDetails(contactname,contactphoneNumber,false);
           contactlist.add(country);  
           }
         
    	  //create an ArrayAdaptar from the String Array
           mdataAdapter = new MyCustomAdapter(this,R.layout.activity_add_contactsto_onetoone, contactlist);
    	  ListView listView = (ListView) findViewById(R.id.listView1);
    	  // Assign adapter to ListView
    	  listView.setAdapter(mdataAdapter);
    	  listView.setTextFilterEnabled(true);
    	  searchEditText.addTextChangedListener(new TextWatcher() {
         	 
        	  public void afterTextChanged(Editable s) {
        	  }
        	 
        	  public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        	  }
        	 
        	  public void onTextChanged(CharSequence s, int start, int before, int count) {
        		  mdataAdapter.getFilter().filter(s.toString());
        	  }
        	  });
    	 
    	 
    	  listView.setOnItemClickListener(new OnItemClickListener() {
    	   public void onItemClick(AdapterView<?> parent, View view,
    	     int position, long id) {
    	    // When clicked, show a toast with the TextView text
    		   ContactDetails country = (ContactDetails) parent.getItemAtPosition(position);
//    	    Toast.makeText(getApplicationContext(),
//    	      "Clicked on: " + country.getcontactname(), 
//    	      Toast.LENGTH_SHORT).show();
    	   }
    	  });
    	 
    	 }
    
    private class MyCustomAdapter extends ArrayAdapter<ContactDetails> {
   	 
  	  private ArrayList<ContactDetails> contactlist;
  	  private ArrayList<ContactDetails> originalList;
  	  public MyCustomAdapter(Context context, int textViewResourceId, 
  	    ArrayList<ContactDetails> contactlist) {
  	   super(context, textViewResourceId, contactlist);
  	   this.contactlist = new ArrayList<ContactDetails>();
  	   this.contactlist.addAll(contactlist);
  	   this.originalList = new ArrayList<ContactDetails>();
	   this.originalList.addAll(contactlist);
  	  }
  	 
  	  private class ViewHolder {
  	   TextView cname;
  	   TextView cnum;
  	   CheckBox cbox;
  	  }
  
  	  @Override
  	  public View getView(int position, View convertView, ViewGroup parent) {
  	 
  	   ViewHolder holder = null;
  	   Log.v("ConvertView", String.valueOf(position));
  	 
  	   if (convertView == null) {
  	   LayoutInflater vi = (LayoutInflater)getSystemService(
  	     Context.LAYOUT_INFLATER_SERVICE);
  	   convertView = vi.inflate(R.layout.contact_listitems, null);
  	 
  	   holder = new ViewHolder();
  	   holder.cname = (TextView) convertView.findViewById(R.id.name);
  	   holder.cnum= (TextView) convertView.findViewById(R.id.num);
  	   holder.cbox = (CheckBox) convertView.findViewById(R.id.checkBox1);
  	   convertView.setTag(holder);
  	 
  	    holder.cbox.setOnClickListener( new View.OnClickListener() {  
  	     public void onClick(View v) {  
  	      CheckBox cb = (CheckBox) v ;  
  	      ContactDetails country = (ContactDetails) cb.getTag();  
  	      country.setSelected(cb.isChecked());
  	     }  
  	    });  
  	   } 
  	   else {
  	    holder = (ViewHolder) convertView.getTag();
  	   }
  	 
  	   ContactDetails country = contactlist.get(position);
  	   holder.cname.setText(" (" +  country.getcontactname() + ")");
  	   holder.cnum.setText(  country.getcontactnumber()  );
  	   holder.cbox.setChecked(country.isSelected());
  	   holder.cbox.setTag(country);
  	 
  	   return convertView;
  	  }
  	 }
  	 
  	 }
  
