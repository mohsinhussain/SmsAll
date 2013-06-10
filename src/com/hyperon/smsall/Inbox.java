package com.hyperon.smsall;

public class Inbox {
	private String from;
	private String to;
	private int timestamp;
	private String message;
	private int flag;
	private String userId;
	private String status;


	public Inbox(String from ,String message,int timestamp , int flagvalue , String to, String userId) {
		this.from = from;
		this.flag = flagvalue;
		this.timestamp = timestamp;
		this.message = message;
		this.to = to;
		this.userId = userId;
		this.status = "sent";
		
	}
	
	public Inbox(String from ,String message,int timestamp , int flagvalue , String to, String userId, String status) {
		this.from = from;
		this.flag = flagvalue;
		this.timestamp = timestamp;
		this.message = message;
		this.to = to;
		this.userId = userId;
		this.status = status;
		
	}
	
	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public Inbox(String from ,String message , int timestamp, String to, String userId) {
	
		this.from = from;
		this.timestamp = timestamp;
		this.message = message;
		this.to = to;
		this.userId = userId;
	}

	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getfrom() {
		return from;
	}

	public void setfrom(String from) {
		this.from = from;
	}

	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	

}
