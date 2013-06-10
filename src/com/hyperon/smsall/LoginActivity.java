package com.hyperon.smsall;

import java.io.IOException;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */

public class LoginActivity extends Activity {
	/**
	 * The default email to populate the email field with.
	 * 
	 */
	public static final String EXTRA_EMAIL = "com.hyperon.smsall.authenticator.extra.EMAIL";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;
	

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;
	private String mFullName;
	private String mPhoneNumber;
    private String TAG = "LoginActivity";

	// UI references.
    private EditText mPhoneNumberView;
    private EditText mFullNameView;
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	SharedPreferences mSharedPrefs;
	
	private HTTPClient mClient;
	
	ConnectivityManager connManager;
	NetworkInfo mWifi;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		TelephonyManager tMgr =(TelephonyManager) LoginActivity.this.getSystemService(Context.TELEPHONY_SERVICE);
		String phnumb = tMgr.getVoiceMailNumber();
//		phnumb = phnumb + tMgr.getAllCellInfo().toString();
//		
		Log.v(TAG, "My Number: "+ phnumb);
		
//		if (android.os.Build.VERSION.SDK_INT > 9) {
//		      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//		      StrictMode.setThreadPolicy(policy);
//		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_login);
		
		// TODO: Setup Action Bar
		//setupActionBar();

		// Set up the login form.
		mPhoneNumberView = (EditText) findViewById(R.id.phoneNumberTextView);
//		mPhoneNumber = getIntent().getStringExtra(EXTRA_EMAIL);
//		mPhoneNumberView.setText(mPhoneNumber);
		mEmailView = (EditText) findViewById(R.id.emailTextView);
		mFullNameView = (EditText) findViewById(R.id.fullnameTextView);
		
//		mEmailView.setText(mPhoneNumber);

		mPasswordView = (EditText) findViewById(R.id.passwordTextView);
//		mPasswordView
//				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//					@Override
//					public boolean onEditorAction(TextView textView, int id,
//							KeyEvent keyEvent) {
//						if (id == R.id.login || id == EditorInfo.IME_NULL) {
//							attemptLogin();
//							return true;
//						}
//						return false;
//					}
//				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						if (mWifi.isConnected()) {
						    // Do whatever
							attemptLogin();
						}
						else{
							Toast.makeText(LoginActivity.this, "Wifi is not Connected! Please Check your WIFI...", Toast.LENGTH_LONG).show();
						}
					}
				});
		mSharedPrefs = getSharedPreferences(Constants.PREFS_NAME, 0);
		mClient = new HTTPClient(mSharedPrefs);

		registerReceiver(mHandleMessageReceiver, new IntentFilter(Utilities.DISPLAY_MESSAGE_ACTION));
		
		Log.v(TAG, "Checking device");
		GCMRegistrar.checkDevice(this);
		
		Log.v(TAG, "Checking manifest");
		GCMRegistrar.checkManifest(this);
		
    	if (mSharedPrefs.getString("cookie", null) != null){
    		Constants.USER_NAME = mSharedPrefs.getString("chatname", null);
    		Constants.FULL_NAME = mSharedPrefs.getString("fullname", null);
			Log.v(TAG, "UserName: "+ Constants.USER_NAME);
			Log.v(TAG, "FullName: "+ Constants.FULL_NAME);
			Log.v(TAG, "Shared Preferences found. Trying to skip login...");
//	    	Intent intent = new Intent(LoginActivity.this, TestActivity.class);
			Intent intent = new Intent(LoginActivity.this, TabsFragmentActivity.class);
			startActivity(intent);
			finish();
		}
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			// TODO: If Settings has multiple levels, Up should navigate up
			// that hierarchy.
			//NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		mPhoneNumber = mPhoneNumberView.getText().toString();
		String subString = mPhoneNumber.substring(0, 2);
//		Toast.makeText(getApplicationContext(), "SubString: "+subString, Toast.LENGTH_LONG).show();
		if(subString.equalsIgnoreCase("03")){
			mPhoneNumber = mPhoneNumber.replaceFirst(subString, "923");
		}
		else if(subString.equalsIgnoreCase("+9")){
//			String x = mPhoneNumber.substring(0, 3);
//			mPhoneNumber = mPhoneNumber.replaceFirst(x, "92");
			Toast.makeText(getApplicationContext(), "Please use Digits/Numbers only!", Toast.LENGTH_LONG).show();
		}
		else if(mPhoneNumber.substring(0, 4).equalsIgnoreCase("0092")){
			String x = mPhoneNumber.substring(0, 4);
			mPhoneNumber = mPhoneNumber.replaceFirst(x, "92");
		}
		
//		Toast.makeText(getApplicationContext(), mPhoneNumber, Toast.LENGTH_LONG).show();
		mPassword = mPasswordView.getText().toString();
		mEmail = mEmailView.getText().toString();
		mFullName = mFullNameView.getText().toString();

		// FIXME: hard coded for debugging! 
		Constants.PHONE_NUMBER = mPhoneNumber;
		Constants.NAME = mFullName; 
//		mPhoneNumber = "+923434430193";
		Constants.EMAIL = mEmail;
//		mPassword = "qwerty";

		boolean cancel = false;
		View focusView = null;

		// Check for a valid phone number.
				if (TextUtils.isEmpty(mPhoneNumber)) {
					mPhoneNumberView.setError(getString(R.string.error_field_required));
					focusView = mPhoneNumberView;
					cancel = true;
				} else if (mPhoneNumber.length() < 10) {
					mPhoneNumberView.setError(getString(R.string.error_invalid_phone));
					focusView = mPhoneNumberView;
					cancel = true;
				}
				
				
		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}
		
		if (TextUtils.isEmpty(mFullName)) {
			mFullNameView.setError(getString(R.string.error_field_required));
			focusView = mFullNameView;
			cancel = true;
		} 

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} 
			else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Context) this);

		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Context, Void, Integer> {
		@Override
		protected Integer doInBackground(Context... contexts) {
			try {
				Context context = contexts[0];
				String regId = GCMRegistrar.getRegistrationId(context);
				Log.v(TAG, "Registrar Reg id at login activity: " + regId);
				if (regId.equals("")) {
					Log.v(TAG, "Registering device");
					GCMRegistrar.register(context, Constants.GCM_SENDER_ID);
//					regId = GCMRegistrar.getRegistrationId(context);
				}
				Log.v(TAG, "Registering device on SMSall server...");
//				SMSallServer.register(context,
//						  mFullName,
//						  mEmail,
//						  mPhoneNumber,
//						  regId);

				if (mClient.login(mPhoneNumber, mPassword)){
					Log.i(TAG, "Logged In");
		    		return Constants.Success;
				}
				return Constants.LoginFailed;
			} catch (IOException e) {
				e.printStackTrace();
				Log.e("LoginActivity", "EXCEPTION");
			}
			return Constants.Exception;
		}

		@Override
		protected void onPostExecute(final Integer response) {
			Log.v("null","onPostExecute");
			mAuthTask = null;
			showProgress(false);

			if (response == Constants.Success) {
				//finish();
//		    	Intent intent = new Intent(LoginActivity.this, TestActivity.class);
				Intent intent = new Intent(LoginActivity.this, TabsFragmentActivity.class);
		    	intent.putExtra("jsonstring", response);				
				startActivity(intent);
				Toast.makeText(LoginActivity.this, "Login success!!", Toast.LENGTH_SHORT).show();
				finish();
			} else if (response == Constants.LoginFailed) {
				mPasswordView
						.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
			else {
				Toast.makeText(LoginActivity.this, "An error occurred during login. Please try again later!!", Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(Constants.GCM_EXTRA_MESSAGE);
			/**
			 * Take appropriate action on this message
			 * depending upon your app requirement
			 * For now i am just displaying it on the screen
			 * */
			
			// Showing received message
			//lblMessage.append(newMessage + "\n");			
			Log.v(TAG, newMessage + "\n");
//			Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();
		}
	};
	

	@Override
	protected void onDestroy() {
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}
	/*
    private final Handler handler =  new Handler() {
        public void handleMessage(Message msg) {
            String state = (String) msg.obj;
            if (state != null && msg.arg1==1)
            {
            	sendfeedtojsonparser(state);
            }
        }
    };

    public void getfeedfromfile()
    {
    	Messenger msg = new Messenger(handler);
    	
      	 Intent i= new Intent(LoginActivity.this,CacheAccess.class); 
       	
      	 i.putExtra("key","key_feed");
      	 i.putExtra("handler", msg);
           
      	 startActivity(i);
    }
    
    public void savefeedinfile(String v)
    {
    	Intent i= new Intent(LoginActivity.this,CacheAccess.class); 
     	
        i.putExtra("key","key_feed");
        i.putExtra("value",v);
        i.putExtra("time","3");
 		
 		startActivity(i);
    }
    
    public void sendfeedtojsonparser(String feed)
    {
    	Intent i= new Intent(LoginActivity.this, JsonParser.class); 
		i.putExtra("jsonstring",feed);
		
		startActivity(i);
    }
	*/

}
