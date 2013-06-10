package com.hyperon.smsall;

import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hyperon.smsall.ChatWindowActivity.PostSingleUserMessageCallbacks;
import com.hyperon.smsall.R.drawable;

import Classes.ContactDetails;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class ComposeFragment extends Fragment {
	/**
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 *      android.view.ViewGroup, android.os.Bundle)
	 */

	String TAG = "Compose Fragment";
	EditText searchEditText;
	ImageView button;
	private MyCustomAdapter mdataAdapter = null;
	String numbersList = "";
	String number = "";
	String userFullName = "";
	String Tag = "ComposeFragment";
	View layout;
	Button smsaAllButton, allButton, recentButton;
	boolean selectMultiple = false;
	JSONObject multipleContactsJsonObject = new JSONObject();
	JSONArray multipleContactsJsonArray = new JSONArray();
	ListView listView;
	Context context;
	String tag = "ComposeFragment";

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			// We have different layouts, and in one of them this
			// fragment's containing frame doesn't exist. The fragment
			// may still be created from its saved state, but there is
			// no reason to try to create its view hierarchy because it
			// won't be displayed. Note this is not needed -- we could
			// just run the code below, where we would create and return
			// the view hierarchy; it would just never be used.
			return null;
		}

		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		// setHasOptionsMenu(true);
		layout = inflater.inflate(R.layout.compose_fragment_layout, container,
				false);
		searchEditText = (EditText) layout.findViewById(R.id.search);
		button = (ImageView) layout.findViewById(R.id.select);
		smsaAllButton = (Button) layout.findViewById(R.id.smsAllButton);
		allButton = (Button) layout.findViewById(R.id.allButton);
		recentButton = (Button) layout.findViewById(R.id.recentButton);

		return layout;
	}

	// @Override
	// public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	// // TODO Auto-generated method stub
	// super.onCreateOptionsMenu(menu, inflater);
	// // MenuInflater inflater = getMenuInflater();
	// Log.v(TAG, "I am in Create Option Menu");
	// inflater.inflate(R.menu.messages_fragment_option_menu, menu);
	// // return true;
	// }
	//
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// // TODO Auto-generated method stub
	// switch (item.getItemId()) {
	// case R.id.multipleSelect:{
	// if(selectMultiple==false){
	// selectMultiple = true;
	// displayListView();
	// item.setTitle("Select One");
	// button.setVisibility(View.VISIBLE);
	// }
	// else{
	// selectMultiple = false;
	// displayListView();
	// item.setTitle("Select Multiple");
	// button.setVisibility(View.GONE);
	// }
	//
	// //do here
	// }

	// default:
	// return super.onOptionsItemSelected(item);
	// }
	// }

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		context = getActivity();
		searchEditText.setText("");
		// FADING THE PRESS ME BUTTON INITIALLY
		selectMultiple = false;
		button.setEnabled(false);
		AlphaAnimation alpha = new AlphaAnimation(1.0F, 0.0F);
		alpha.setDuration(1000); // Make animation instant
		alpha.setFillAfter(true); // Tell it to persist after the animation ends
		// And then on your layout
		button.startAnimation(alpha);
		button.setVisibility(View.GONE);
//		button.setBackgroundDrawable(getActivity().getResources().getDrawable(
//				R.drawable.pressmeunhighlited));

		displayListView();
		ADDButton();
		activateButton();

	}

	public void activateButton() {
		smsaAllButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// Toast.makeText(getActivity(), "show SMSALL Contacts List!",
				// Toast.LENGTH_LONG).show();
				smsaAllButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.smsall_btn_active));
				allButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.all_btn_normal));
				recentButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.recent_btn_normal));
			}
		});

		allButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// Toast.makeText(getActivity(), "show All Contacts List!",
				// Toast.LENGTH_LONG).show();
				smsaAllButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.smsall_btn_normal));
				allButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.all_btn_active));
				recentButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.recent_btn_normal));
			}
		});

		recentButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// Toast.makeText(getActivity(), "show Recent Contacts List!",
				// Toast.LENGTH_LONG).show();
				smsaAllButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.smsall_btn_normal));
				allButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.all_btn_normal));
				recentButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.recent_btn_active));
			}
		});
	}

	public void ADDButton() {

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// StringBuffer responseText = new StringBuffer();
				// responseText.append("The following were selected...\n");
				multipleContactsJsonArray = new JSONArray();

				ArrayList<ContactDetails> countryList = mdataAdapter.contactlist;
				for (int i = 0; i < countryList.size(); i++) {
					multipleContactsJsonObject = new JSONObject();
					ContactDetails contact = countryList.get(i);
					if (contact.isSelected()) {
						// responseText.append("\n" + contact.getcontactnumber()
						// + " " + contact.getcontactname());
						// map = new HashMap<String, String>();
						// map.put("contactnumber", contact.getcontactnumber()
						// );
						// map.put("contactname", contact.getcontactname());
						String number = contact.getcontactnumber();
						String name = contact.getcontactname();

						// Toast.makeText(getApplicationContext(),
						// "SubString: "+subString, Toast.LENGTH_LONG).show();
						// if(subString.equalsIgnoreCase("03")){
						// mPhoneNumber = mPhoneNumber.replaceFirst(subString,
						// "923");
						// }
						number = number.replace(" ", "");
						number = number.replace("-", "");
						number = number.replace("(", "");
						number = number.replace(")", "");
						number = number.replace("+", "");

						if (number.substring(0, 4).equalsIgnoreCase("0092")) {
							String x = number.substring(0, 4);
							number = number.replaceFirst(x, "0");
						} else if (number.substring(0, 2)
								.equalsIgnoreCase("92")) {
							String x = number.substring(0, 2);
							number = number.replaceFirst(x, "0");
						}
						try {
							multipleContactsJsonObject.put("name", name);
							multipleContactsJsonObject.put("number", number);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						multipleContactsJsonArray
								.put(multipleContactsJsonObject);
						// number = number+",";
						// number = URLEncoder.encode(number);
						// numbersList = numbersList +number;

						// contactlist.add(map);
					}
				}
				// map.clear();
				Intent i = new Intent(getActivity(),
						ChatWindowActivity.class);
				String jsonString = multipleContactsJsonArray.toString();
				i.putExtra(Constants.INTENT_KEY_MULTIPLE_CONTACTS_LIST,
						jsonString);
				startActivity(i);
				// Toast.makeText(getActivity(),
				// "Number List: "+URLDecoder.decode(numbersList),
				// Toast.LENGTH_LONG).show();

				// Intent i = getIntent();
				// i.putExtra(Constants.INTENT_CONTACTS_SELECTED, numbersList);
				// setResult(RESULT_OK, i);
				// finish();

				// Messenger messngr= mintent.getParcelableExtra("handler");

				// Message message= Message.obtain();
				// message.arg1=1;
				// message.obj= contactlist;
				// try {
				// messngr.send(message);
				// } catch (RemoteException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }

			}

		});

	}

	private void displayListView() {

		ArrayList<ContactDetails> contactlist = new ArrayList<ContactDetails>();

		Cursor cursor = getActivity().getContentResolver().query(
				Phone.CONTENT_URI, null, null, null,
				Phone.DISPLAY_NAME + " ASC");
		// now we have cusror with contacts and get diffrent value from cusror.
		while (cursor.moveToNext()) {
			String contactname = cursor.getString(cursor
					.getColumnIndex(Phone.DISPLAY_NAME));
			// contactname=number.replace(" ", "");
			// contactname=contactname.replace("-", "");
			contactname = contactname.replace("(", "");
			contactname = contactname.replace(")", "");
			// contactname=contactname.replace("+", "");
			String contactphoneNumber = cursor.getString(cursor
					.getColumnIndex(Phone.NUMBER));
			ContactDetails country = new ContactDetails(contactname,
					contactphoneNumber, false);
			contactlist.add(country);
		}

		// create an ArrayAdaptar from the String Array
		mdataAdapter = new MyCustomAdapter(getActivity(),
				R.layout.activity_add_contactsto_onetoone, contactlist);

		// Assign adapter to ListView
		listView = (ListView) layout.findViewById(R.id.listView1);
		listView.setAdapter(mdataAdapter);
		listView.setTextFilterEnabled(true);
		searchEditText.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mdataAdapter.getFilter().filter(s.toString());
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (!selectMultiple) {
					// When clicked, show a toast with the TextView text
					ContactDetails country = (ContactDetails) parent
							.getItemAtPosition(position);
					// Toast.makeText(getActivity(),
					// "Clicked on: " + country.getcontactname(),
					// Toast.LENGTH_SHORT).show();
					String number = country.getcontactnumber();
					String userFullName = country.getcontactname();
					number = number.replace(" ", "");
					number = number.replace("-", "");
					number = number.replace("(", "");
					number = number.replace(")", "");
					number = number.replace("+", "");
					userFullName = userFullName.replace("-", "");
					userFullName = userFullName.replace("(", "");
					userFullName = userFullName.replace(")", "");
					userFullName = userFullName.replace("+", "");
					if (number.length() > 10) {
						if (number.substring(0, 4).equalsIgnoreCase("0092")) {
							String x = number.substring(0, 4);
							number = number.replaceFirst(x, "+92");
						} else if (number.substring(0, 2)
								.equalsIgnoreCase("92")) {
							String x = number.substring(0, 2);
							number = number.replaceFirst(x, "+92");
						} else if (number.substring(0, 2)
								.equalsIgnoreCase("03")) {
							String x = number.substring(0, 2);
							number = number.replaceFirst(x, "+923");
						}
					}
					// userFullName = URLEncoder.encode(userFullName);
					number = URLEncoder.encode(number);

					// Log.v(Tag, "Number Selected: "+number);
					Intent i = new Intent(getActivity(),
							ChatWindowActivity.class);
					i.putExtra(Constants.INTENT_KEY_USER_PHONE, number);
					// i.putExtra(Constants.INTENT_KEY_USER_NAME, number);
					i.putExtra(Constants.INTENT_KEY_USER_FULL_NAME,
							userFullName);
					startActivity(i);
				} else {
					Animation shake = AnimationUtils.loadAnimation(
							getActivity(), R.anim.shake);
					button.startAnimation(shake);
					// Toast.makeText(getActivity(), "Multiple Selected",
					// Toast.LENGTH_LONG).show();
				}
				// finish();
			}
		});
		
		/**
		 * TODO: API CALL TO GET ALL SMSALL CONTACTS FOR THE VERY FIRST TIME
		 * 
		 */
		new APIClient(getActivity(), context, 
    			new PostSyncContactListCallbacks ())
    					.doPostContactSync(contactlist);

	}
	
	public class PostSyncContactListCallbacks extends AsyncCallback {
		public void onTaskComplete(String response) {
			Log.v(tag, "End Response: "+response);
			String status = "";
//			String username = "";
//			String fullname = "";
//			String ident = "";
			if(response.equalsIgnoreCase(Constants.connectionTimeOutMessage)){
				Toast.makeText(getActivity(), Constants.connectionTimeOutMessage, Toast.LENGTH_LONG).show();
			}else{
				
			
			try {
				JSONObject respObject = new JSONObject(response);
//				status = respObject.getString("status");
				JSONObject dictionary = respObject.getJSONObject("dictionary");
//				username = dictionary.getString("username");
//				fullname = dictionary.getString("fullname");
//				if((fullname.equalsIgnoreCase("null"))||(fullname.equalsIgnoreCase(""))||
//						(fullname.equalsIgnoreCase("no name"))||((fullname.equalsIgnoreCase("noname")))){
//					fullname=mChatmember;
//				}
//				ident = dictionary.getString("ident");
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Log.v(tag, "status: "+status);
//			Log.v(tag, "username: "+username);
//			Log.v(tag, "fullname: "+fullname);
//			Log.v(tag, "ident: "+ident);
			
			
//			if (status.equalsIgnoreCase("0")) {
////				Toast.makeText(SubitemchatlistActivity.this, "Message Accepted for delivery", Toast.LENGTH_SHORT).show();
//				//SAVE TO SQL CODE
//				inboxmessage=new InboxDataSource(getApplicationContext());
////				message = chatMessageEditText.getText().toString();
//				f2=new Inbox(Constants.FULL_NAME,message ,inttimestamp,1,fullname, ident);
//		    	inboxmessage.opendatabase();		
//		    	Boolean var=inboxmessage.addmessage(f2);
//		        inboxmessage.closedatabase();
//
//		        
//		        //Showing it on screen
////		        chatAdapter.add(f1);
//		        chatAdapter.getItem(chatAdapter.getLastItemIndex()).setStatus("sent");
//		        chatAdapter.notifyDataSetChanged();
//		        chatMessageEditText.setText("");
//				
//			}
//			else {
//				Toast.makeText(SubitemchatlistActivity.this, "Error: "+status, Toast.LENGTH_LONG).show();
//				chatAdapter.getItem(chatAdapter.getLastItemIndex()).setStatus("error");
//		        chatAdapter.notifyDataSetChanged();
//			}
			}
//			chatMessageEditText.setText("");
		}
		@Override
		public void onTaskCancelled() {
		}
		@Override
		public void onPreExecute() {
			// TODO Auto-generated method stub
			
		}
	}

	private class MyCustomAdapter extends ArrayAdapter<ContactDetails> {

		private ArrayList<ContactDetails> contactlist;
		private ArrayList<ContactDetails> originalList;
		private CountryFilter filter;

		public MyCustomAdapter(Context context, int textViewResourceId,
				ArrayList<ContactDetails> contactlist) {
			super(context, textViewResourceId, contactlist);
			this.contactlist = new ArrayList<ContactDetails>();
			this.contactlist.addAll(contactlist);
			this.originalList = new ArrayList<ContactDetails>();
			this.originalList.addAll(contactlist);
		}

		@Override
		public Filter getFilter() {
			if (filter == null) {
				filter = new CountryFilter();
			}
			return filter;
		}

		private class ViewHolder {
			RelativeLayout stripLayout;
			TextView cname;
			TextView cnum;
			CheckBox cbox;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			Log.v("ConvertView", String.valueOf(position));

			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.contact_listitems, null);

				holder = new ViewHolder();
				holder.stripLayout = (RelativeLayout) convertView.findViewById(R.id.stripLinearLayout);
				holder.cname = (TextView) convertView.findViewById(R.id.name);
				holder.cnum = (TextView) convertView.findViewById(R.id.num);
				holder.cbox = (CheckBox) convertView
						.findViewById(R.id.checkBox1);
				// if(selectMultiple){
				// holder.cbox.setVisibility(View.VISIBLE);
				// }
				// else{
				// holder.cbox.setVisibility(View.GONE);
				// }
				convertView.setTag(holder);

				holder.cbox.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						CheckBox cb = (CheckBox) v;
						ContactDetails country = (ContactDetails) cb.getTag();
						country.setSelected(cb.isChecked());
						int count = 0;
						for (int i = 0; i < contactlist.size(); i++) {
							if (contactlist.get(i).isSelected()) {
								count++;

								Log.v(Tag, "Selected");
							}
						}
						if (count > 0) {
							if (!selectMultiple) {
								button.setEnabled(true);
								button.setVisibility(View.VISIBLE);
//								holder.stripLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_strip_active));
								AlphaAnimation alpha = new AlphaAnimation(0.0F,
										1.0F);
								alpha.setDuration(1000); // Make animation
															// instant
								alpha.setFillAfter(true); // Tell it to persist
															// after the
															// animation ends
								// And then on your layout
								button.startAnimation(alpha);
//								button.setBackgroundDrawable(getActivity()
//										.getResources().getDrawable(
//												R.drawable.pressmehighlighted));
							}
							selectMultiple = true;
							
						} else {
							if (selectMultiple) {
								button.setEnabled(false);
//								holder.stripLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_));
								AlphaAnimation alpha = new AlphaAnimation(1.0F,
										0.0F);
								alpha.setDuration(1000); // Make animation
															// instant
								alpha.setFillAfter(true); // Tell it to persist
															// after the
															// animation ends
								// And then on your layout
								button.startAnimation(alpha);
								button.setVisibility(View.GONE);
//								button.setBackgroundDrawable(getActivity()
//										.getResources().getDrawable(
//												R.drawable.pressmeunhighlited));
							}
							selectMultiple = false;
						}
						Log.v(Tag, "selectMultiple: " + selectMultiple);
					}
				});
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			ContactDetails country = contactlist.get(position);
			holder.cname.setText(country.getcontactname());
			holder.cnum.setText(country.getcontactnumber());
			holder.cbox.setChecked(country.isSelected());
			holder.cbox.setTag(country);

			return convertView;
		}

		private class CountryFilter extends Filter {

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {

				constraint = constraint.toString().toLowerCase();
				FilterResults result = new FilterResults();
				if (constraint != null && constraint.toString().length() > 0) {
					ArrayList<ContactDetails> filteredItems = new ArrayList<ContactDetails>();

					for (int i = 0, l = originalList.size(); i < l; i++) {
						ContactDetails country = originalList.get(i);
						if ((country.getcontactname().toString().toLowerCase()
								.contains(constraint))
								|| (country.getcontactnumber().toString()
										.toLowerCase().contains(constraint)))
							filteredItems.add(country);
					}
					result.count = filteredItems.size();
					result.values = filteredItems;
				} else {
					synchronized (this) {
						result.values = originalList;
						result.count = originalList.size();
					}
				}
				return result;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {

				contactlist = (ArrayList<ContactDetails>) results.values;
				notifyDataSetChanged();
				clear();
				for (int i = 0, l = contactlist.size(); i < l; i++)
					add(contactlist.get(i));
				notifyDataSetInvalidated();
			}
		}

	}

}
