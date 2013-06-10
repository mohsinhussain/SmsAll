package com.hyperon.smsall;

public class InboxMetaData {
	public static final String DATABASE_NAME="Chatdbb";
	public static final String INBOX_TABLE_NAME="chathistrytablee";
	public static final String COLUMN_ID="_id";
	public static final String COLUMN_FROM="name";
	public static final String COLUMN_MESSAGE="address";
	public static final String COLUMN_TIMESTAMP="phone";
	public static final String COLUMN_FLAG="flag";
	public static final String COLUMN_TO="sender";
	public static final String COLUMN_USER_ID="userid";
	public static final int DATABSE_VERSION=6;

	public static final String FRIENDS_TABLE_CREATE_STMNT="CREATE TABLE "+INBOX_TABLE_NAME+" ("+COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+COLUMN_MESSAGE+" TEXT NOT NULL, "+COLUMN_FROM+" TEXT NOT NULL , "+COLUMN_TIMESTAMP+" INTEGER , "+COLUMN_FLAG+" INTEGER , "+COLUMN_TO+" TEXT , "+COLUMN_USER_ID+" TEXT NOT NULL );";

	public static final String FRIENDS_ALTER_STMNT="alter table "+INBOX_TABLE_NAME +" ADD " +COLUMN_TIMESTAMP + " text;";

}

