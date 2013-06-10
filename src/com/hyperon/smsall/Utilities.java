package com.hyperon.smsall;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.content.Intent;

public class Utilities {
    public static final String DISPLAY_MESSAGE_ACTION =
            "com.hyperon.smsall.DISPLAY_MESSAGE";
    public static final String TAG = "Utilities";

	static void displayMessage(Context context, String message) {
        Intent intent = new Intent();
        intent.putExtra(Constants.GCM_EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }

	public static String buildParams(Map<String, String> params)
	{
		if (params == null)
			return "";
	    StringBuilder bodyBuilder = new StringBuilder();
	    Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
	    while (iterator.hasNext()) {
	        Entry<String, String> param = iterator.next();
	        bodyBuilder.append(param.getKey()).append('=')
	                   .append(param.getValue());
	        if (iterator.hasNext()) {
	            bodyBuilder.append('&');
	        }
	    }
	    return bodyBuilder.toString();
	}

}
