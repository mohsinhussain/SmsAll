package com.hyperon.smsall;

import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	String TAG = "GCMIntentService";
	String mMessage;
	String mkey;
	String mName;
	Calendar cal;
	private static String ident;
	private static String fullName;
	private static String messageType;
	private static String content;
	private static String groupName;
	SharedPreferences mSharedPrefs;
	
	@Override
	protected void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        Utilities.displayMessage(context, "gcm error" + errorId);
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
//		String message = intent.getExtras().getString("msg");
//		Log.i(TAG, "Received message" + message);
//	    Utilities.displayMessage(context, message);
//	    generateNotification(context, message);
		mSharedPrefs = getSharedPreferences(Constants.PREFS_NAME, 0);
		Constants.FULL_NAME = mSharedPrefs.getString("fullname", null);
		String message = intent.getExtras().getString("msg");
		mMessage=message;
		JSONObject jObject;
//		String fullName, messageType, content, groupName = "";
		try {
//			String jstring="{messageobject:{'key':'Private','message':'Hello Shahroz','username':'Esha'}}";
			jObject = new JSONObject(message);

		        
		       // mMessage =msgObject.getString("message");
		        ident =jObject.getString("sender_ident");
		        messageType =jObject.getString("message_type");
		        content =jObject.getString("raw_content");
		        
		        if(messageType.equals("private_message"))
		        {
		        fullName = jObject.getString("sender_fullname");
		        int flagvalue = 0;
				String to= Constants.FULL_NAME;
		    	String from=fullName;
		    	String messsage=content;
		    	cal = Calendar.getInstance();
		    	Long timestamp= cal.getTimeInMillis();
		    	Date date = cal.getTime();
//		    	mHour = date.getHours();
//		    	mMinute = date.getMinutes();
		    	int inttimestamp= (int) (timestamp/1000);
		    	InboxDataSource inboxmessage=new InboxDataSource(getApplicationContext());
		    	Inbox f1=new Inbox(from, messsage,inttimestamp,flagvalue,to, ident);
		    	
		    	inboxmessage.opendatabase();		
		    	
		    	Boolean var=inboxmessage.addmessage(f1);
		    	inboxmessage.closedatabase();
		        }
		        else{
		        	groupName =jObject.getString("group_name");
		        }
		        
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		 Intent i = new Intent();
		
	        //Intent notificationIntent = new Intent(context, GroupChatActivity.class);
	        //notificationIntent.putExtra(Constants.INTENT_KEY_GROUP_NAME, "Test1177");
	        if(messageType.equalsIgnoreCase("private_message")){
	        	Intent i = new Intent("MyGCMMessageReceived");
		        this.sendBroadcast(i);
//		i = new Intent(context, SubitemchatlistActivity.class);
//        i.putExtra(Constants.INTENT_KEY_USER_ID, ident);
//        i.putExtra(Constants.INTENT_KEY_USER_FULL_NAME, fullName);
    }else{
    	Intent i = new Intent("MyGCMGroupMessageReceived");
        this.sendBroadcast(i);
//    	i = new Intent(getApplicationContext(), GroupChatActivity.class);
//        i.putExtra(Constants.INTENT_KEY_GROUP_NAME, groupName);
    }
	        
//  Intent notificationIntent = new Intent(context, SubitemchatlistActivity.class);
//    notificationIntent.putExtra(Constants.INTENT_KEY_USER_FULL_NAME, ident);
    
    
//    notificationIntent.putExtra, value)
    // set intent so it does not start a new activity
//    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//            Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
    
    
	         
		Log.i(TAG, "Message: " + content );
		Log.i(TAG, "fullName: " + fullName );
		Log.i(TAG, "groupName: " + groupName );
		Log.i(TAG, "ident: " + ident );
		Log.i(TAG, "type: " + messageType );
//	    Utilities.displayMessage(context, content);
	    generateNotification(context, fullName+": "+content);
//	    if(Constants.chatOpen){
//	    	startActivity(i);	
//	    }
	    

	}

	@Override
	protected void onRegistered(Context context, String regId) {
        Log.i(TAG, "Device registered: regId = " + regId);
        Utilities.displayMessage(context, "Your device is now registred with GCM");
        SMSallServer.register(context, Constants.NAME, Constants.EMAIL, Constants.PHONE_NUMBER, regId);
	}

	@Override
	protected void onUnregistered(Context context, String regId) {
        Log.i(TAG, "Device unregistered");
        Utilities.displayMessage(context, "Unregistered");
        SMSallServer.unregister(context, regId);
	}

	@Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        Utilities.displayMessage(context, "Message Deleted" + total);
        // notifies user
        generateNotification(context, "Message Deleted" + total);
    }

	@Override
	protected boolean onRecoverableError(Context context, String errorId){
		return true;
	}
	
    /**
     * Issues a notification to inform the user that server has sent a message.
     */
	private static void generateNotification(Context context, String message) {
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);
        
        String title = context.getString(R.string.app_name);
        Intent notificationIntent = new Intent();
        //Intent notificationIntent = new Intent(context, GroupChatActivity.class);
        //notificationIntent.putExtra(Constants.INTENT_KEY_GROUP_NAME, "Test1177");
        if(messageType.equalsIgnoreCase("private_message")){
        	notificationIntent = new Intent(context, ChatWindowActivity.class);
            notificationIntent.putExtra(Constants.INTENT_KEY_USER_ID, ident);
            notificationIntent.putExtra(Constants.INTENT_KEY_USER_FULL_NAME, fullName);
        }else{
        	notificationIntent = new Intent(context, GroupChatActivity.class);
            notificationIntent.putExtra(Constants.INTENT_KEY_GROUP_NAME, groupName);
        }
//      Intent notificationIntent = new Intent(context, SubitemchatlistActivity.class);
//        notificationIntent.putExtra(Constants.INTENT_KEY_USER_FULL_NAME, ident);
        
        
//        notificationIntent.putExtra, value)
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
        
        notification.setLatestEventInfo(context, title, message, intent);
//        ((Intent) notification).putExtra(Constants.INTENT_KEY_GROUP_NAME, "Test1177");
        
        Log.v("SEE", "Message: "+message);
        
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        
        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;
        
        //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");
        
        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);      

    }

}
