package com.hyperon.smsall;

public abstract class AsyncCallback {
	public abstract void onTaskComplete(String response);
	public abstract void onTaskCancelled();
	public abstract void onPreExecute();
}
