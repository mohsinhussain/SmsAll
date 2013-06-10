package com.hyperon.smsall;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import Classes.ContactDetails;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AddContactsToGroupActivity extends Activity {
	private MyCustomAdapter mdataAdapter = null;
	private ArrayList<HashMap<String, String>> contactlist = new ArrayList<HashMap<String, String>>();
    Intent mintent = new Intent();
//    private HashMap<String, String> map;
    String numbersList = "";
    
    
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_contacts_to_group);
        
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
//    	    	StringBuffer responseText = new StringBuffer();
//    		    responseText.append("The following were selected...\n");
    		    
    		    ArrayList<ContactDetails> countryList = mdataAdapter.contactlist;
    		    for(int i=0;i<countryList.size();i++){
    		    	ContactDetails contact = countryList.get(i);
    		     if(contact.isSelected()){
//    		      responseText.append("\n" + contact.getcontactnumber() + " " + contact.getcontactname());
//    		       map = new HashMap<String, String>();
//    		      	map.put("contactnumber", contact.getcontactnumber() );
//    		        map.put("contactname", contact.getcontactname());
    		       String number = contact.getcontactnumber();
//    				Toast.makeText(getApplicationContext(), "SubString: "+subString, Toast.LENGTH_LONG).show();
//    				if(subString.equalsIgnoreCase("03")){
//    					mPhoneNumber = mPhoneNumber.replaceFirst(subString, "923");
//    				}
    		       number=number.replace(" ", "");
    		       number=number.replace("-", "");
    		       number=number.replace("(", "");
    		       number=number.replace(")", "");
    		       number=number.replace("+", "");
    		       
    		       if(number.substring(0, 4).equalsIgnoreCase("0092")){
    					String x = number.substring(0, 4);
    					number = number.replaceFirst(x, "0");
    				}
    				else if(number.substring(0, 2).equalsIgnoreCase("92")){
    					String x = number.substring(0, 2);
    					number = number.replaceFirst(x, "0");
    				}
    		       number = number+",";
    		       number = URLEncoder.encode(number);
    		        	 numbersList = numbersList +number;
    		        
    		       
//    		        contactlist.add(map);
    		     }
    		    }
//    		    map.clear();
    		 
//    		    Toast.makeText(getApplicationContext(),
//    		      "Number List: "+URLDecoder.decode(numbersList), Toast.LENGTH_LONG).show();
    		    
    		    Intent i = getIntent();
    		    i.putExtra(Constants.INTENT_CONTACTS_SELECTED, numbersList);
    		    setResult(RESULT_OK, i);
    		    finish();

//    		Messenger messngr= mintent.getParcelableExtra("handler");
    		 	
//    		     Message message= Message.obtain();
//    				message.arg1=1;
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
    	
    	  Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,null, null);
          // now we have cusror with contacts and get diffrent value from cusror.
           while (cursor.moveToNext()) {
           String contactname =cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
           String contactphoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
           ContactDetails country = new ContactDetails(contactname,contactphoneNumber,false);
           contactlist.add(country);  
           }
         
    	  //create an ArrayAdaptar from the String Array
           mdataAdapter = new MyCustomAdapter(this,R.layout.contact_listitems, contactlist);
    	  ListView listView = (ListView) findViewById(R.id.listView1);
    	  // Assign adapter to ListView
    	  listView.setAdapter(mdataAdapter);
    	 
    	 
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
    	 
    	  public MyCustomAdapter(Context context, int textViewResourceId, 
    	    ArrayList<ContactDetails> contactlist) {
    	   super(context, textViewResourceId, contactlist);
    	   this.contactlist = new ArrayList<ContactDetails>();
    	   this.contactlist.addAll(contactlist);
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
    
