package com.hyperon.smsall;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Classes.ContactDetails;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class APIClient {
	private String TAG = "APIClient";
	private SMSallAsyncTask mTask;
	private SMSallAsyncTaskNoDialog xTask;
	private Context mContext;
	private Activity mActivity;
	private AsyncCallback mCallback;
	private int CONTEXT_INDEX = 0;
	private int URI_INDEX = 1;
	private int METHOD_INDEX = 2;
	private int PARAMS_INDEX = 3;

	public APIClient(Activity activity, Context context, AsyncCallback callback) {
		mContext = context;
		mActivity = activity;
		mCallback = callback;

	}

	public void doPostCreateGroup(String groupName) {
		Map<String, String> params = new HashMap<String, String>();
		// params.put("message", groupName);
		// params.put("reply_all", replyAll ? "true" : "false");

		mTask = new SMSallAsyncTask();
		mTask.execute(mContext, Constants.API_SERVER_URL + "/group/"
				+ groupName, "POST", params);
	}

	/**
	 * 
	 * POST API FOR SYNC CONTACT LIST
	 */

	public void doPostContactSync(ArrayList<ContactDetails> contactList) {
		String params1 = "";
		JSONArray contactsArray = new JSONArray();

		for (int i = 0; i < contactList.size(); i++) {
			try {
				JSONObject contactObject = new JSONObject();
				contactObject.put("name", contactList.get(i).getcontactname());
				contactObject.put("phone", contactList.get(i)
						.getcontactnumber());
				contactsArray.put(i, contactObject);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		params1 = "phonebook=" + URLEncoder.encode(contactsArray.toString());
		Log.v(TAG, contactsArray.toString());
		// params1 = URLEncoder.encode(contactsArray.toString());
		// Log.v(TAG, params1);
		// Map <String, String> params = new HashMap<String, String>();
		// params.put("phonebook", params1);

		// message= URLEncoder.encode(message);
		//
		// if(userId!=null && phone==null){
		// userId = URLEncoder.encode(userId);
		// params.put("ident", userId);
		// Log.v(TAG, "id: "+userId);
		// }
		// else if(userId==null && phone!=null){
		// // phone = URLEncoder.encode(phone);
		// params.put("phone_number", phone);
		// Log.v(TAG, "phone: "+phone);
		// }
		//
		// params.put("message", message);
		//
		// Log.v(TAG, "message: "+message);
		// params.put("send_notification", deliveryReport ? "true" : "false");
		//
		mTask = new SMSallAsyncTask();
		mTask.execute(mContext, Constants.API_SERVER_URL + "/me/phonebook",
				"POST", params1);
	}

	// single user
	public void doPostSingleUserMessage(String phone, String userId,
			String message, Boolean deliveryReport, String... s) {
		Map<String, String> params = new HashMap<String, String>();

		message = URLEncoder.encode(message);

		if (userId != null && phone == null) {
			userId = URLEncoder.encode(userId);
			params.put("ident", userId);
			Log.v(TAG, "id: " + userId);
		} else if (userId == null && phone != null) {
			// phone = URLEncoder.encode(phone);
			params.put("phone_number", phone);
			Log.v(TAG, "phone: " + phone);
		}

		params.put("message", message);

		Log.v(TAG, "message: " + message);
		params.put("send_notification", deliveryReport ? "true" : "false");
		
		xTask = new SMSallAsyncTaskNoDialog();
		xTask.execute(mContext, Constants.API_SERVER_URL + "/me/private",
				"POST", params);
	}

	// group
	public void doPostMessage(String groupName, String message,
			Boolean replyAll, String... s) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("message", message);
		params.put("reply_all", replyAll ? "true" : "false");

		xTask = new SMSallAsyncTaskNoDialog();
		xTask.execute(mContext, Constants.API_SERVER_URL + "/group/"
				+ groupName + "/message", "POST", params);
	}

	public void doPutInviteContacts(String groupName, String contactsList) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("invitees", contactsList);
		params.put("action", "invite");

		mTask = new SMSallAsyncTask();
		mTask.execute(mContext, Constants.API_SERVER_URL + "/membership/group/"
				+ groupName + "/", "PUT", params);
	}

	public void doGetMembers(String groupName) {
		mTask = new SMSallAsyncTask();
		mTask.execute(mContext, Constants.API_SERVER_URL + "/group/"
				+ groupName + "/members", "GET", null);
	}

	public void doGetGroupInfo(String groupName) {
		mTask = new SMSallAsyncTask();
		mTask.execute(mContext, Constants.API_SERVER_URL + "/group/"
				+ groupName + "/info", "GET", new HashMap<String, String>());
	}

	public void doGetActivity(String groupName, String pageSize) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("page_size", pageSize);
		mTask = new SMSallAsyncTask();
		mTask.execute(mContext, Constants.API_SERVER_URL + "/group/"
				+ groupName + "/history", "GET", params);
	}

	public class SMSallAsyncTask extends AsyncTask<Object, Void, String> {
		// 0: Context
		// 1: URL
		// 2: Method
		// 3: Params

		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			dialog = new ProgressDialog(mActivity); // App - your main activity
													// class
			dialog.setMessage("Please, wait...");
			dialog.show();
		}

		@SuppressWarnings("unchecked")
		@Override
		protected String doInBackground(Object... parameters) {
			String postResponse = "";
			try {
				Context context = (Context) parameters[CONTEXT_INDEX];
				String url = (String) parameters[URI_INDEX];
				HTTPClient client = new HTTPClient(
						context.getSharedPreferences(Constants.PREFS_NAME, 0));
				// HttpParams params2 = client.getParams();
				// params2.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
				// new Integer(3000));
				// params2.setParameter(CoreConnectionPNames.SO_TIMEOUT, new
				// Integer(3000));
				// client.setParams(params2);

				Map<String, String> params = null;
//				String params1 = "";
				if (parameters[PARAMS_INDEX] instanceof HashMap) {
					params = (HashMap<String, String>) parameters[PARAMS_INDEX];
					
				}
//				else if (parameters[PARAMS_INDEX] instanceof String){
//					params1 = (String) parameters[PARAMS_INDEX];
//				}

				if (parameters[METHOD_INDEX] == "POST") {
					if (parameters[PARAMS_INDEX] instanceof HashMap) {
						postResponse = client.post(url, params);
						
					}
//					else if (parameters[PARAMS_INDEX] instanceof String){
//						postResponse = client.post1(url, params1);
//						
//					}
//					
					Log.v(TAG, "Post End Response: " + postResponse);
					return postResponse;

				} else if (parameters[METHOD_INDEX] == "GET")
					return client.get(url, params);
				else if (parameters[METHOD_INDEX] == "PUT")
					return client.put(url, params);
				else if (parameters[METHOD_INDEX] == "DELETE")
					return client.delete(url, params);

				return Constants.Success.toString();
			} catch (Exception e) {
				e.printStackTrace();
				Log.e(TAG, "EXCEPTION: " + e.toString());
			}
			return Constants.Exception.toString();
		}

		@Override
		protected void onPostExecute(final String response) {
			Log.v(TAG, "onPostExecute");
			mTask = null;
			mCallback.onTaskComplete(response);
			dialog.dismiss();
		}

		@Override
		protected void onCancelled() {
			mTask = null;
			mCallback.onTaskCancelled();
		}
	}

	public class SMSallAsyncTaskNoDialog extends
			AsyncTask<Object, Void, String> {
		// 0: Context
		// 1: URL
		// 2: Method
		// 3: Params

		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			// dialog = new ProgressDialog(mActivity); // App - your main
			// activity class
			// dialog.setMessage("Please, wait...");
			// dialog.show();
		}

		@SuppressWarnings("unchecked")
		@Override
		protected String doInBackground(Object... parameters) {
			String postResponse = "";
			try {
				Context context = (Context) parameters[CONTEXT_INDEX];
				String url = (String) parameters[URI_INDEX];
				HTTPClient client = new HTTPClient(
						context.getSharedPreferences(Constants.PREFS_NAME, 0));
				// HttpParams params2 = client.getParams();
				// params2.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
				// new Integer(3000));
				// params2.setParameter(CoreConnectionPNames.SO_TIMEOUT, new
				// Integer(3000));
				// client.setParams(params2);

				Map<String, String> params = null;
				if (parameters[PARAMS_INDEX] instanceof HashMap)
					params = (HashMap<String, String>) parameters[PARAMS_INDEX];

				if (parameters[METHOD_INDEX] == "POST") {

					postResponse = client.post(url, params);
					Log.v(TAG, "Post End Response: " + postResponse);
					return postResponse;

				} else if (parameters[METHOD_INDEX] == "GET")
					return client.get(url, params);
				else if (parameters[METHOD_INDEX] == "PUT")
					return client.put(url, params);
				else if (parameters[METHOD_INDEX] == "DELETE")
					return client.delete(url, params);

				return Constants.Success.toString();
			} catch (Exception e) {
				e.printStackTrace();
				Log.e(TAG, "EXCEPTION: " + e.toString());
			}
			return Constants.Exception.toString();
		}

		@Override
		protected void onPostExecute(final String response) {
			Log.v(TAG, "onPostExecute");
			mTask = null;
			mCallback.onTaskComplete(response);
			// dialog.dismiss();
		}

		@Override
		protected void onCancelled() {
			mTask = null;
			mCallback.onTaskCancelled();
		}
	}

}
