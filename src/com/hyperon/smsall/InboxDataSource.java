package com.hyperon.smsall;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class InboxDataSource {
	
	private SQLiteDatabase db;
	
	private Context context;
	
    private InboxSQLiteOpenHelper friendsOpenHelper;
    
 
 
 public InboxDataSource(Context c)
 {
	 this.context = c;
	friendsOpenHelper =new InboxSQLiteOpenHelper(context);
	 
 }
 
 public void opendatabase()
 {
	
	 this.db=friendsOpenHelper.getWritableDatabase();
	 
 }
 
 public void closedatabase()
 {
	 this.db.close();
	friendsOpenHelper.close();
	 
 }
 

 
 public Boolean addmessage(Inbox inbox)
 {
	 
	 ContentValues cv=new ContentValues();
	
	 cv.put(InboxMetaData.COLUMN_TO, inbox.getTo());
	 Log.v("Inbox data source ", inbox.getTo()); 
	 cv.put(InboxMetaData.COLUMN_FROM, inbox.getfrom());
	 Log.v("Inbox data source ", inbox.getfrom());
	 cv.put(InboxMetaData.COLUMN_MESSAGE, inbox.getMessage());
	 Log.v("Inbox data source ", inbox.getMessage());
	 cv.put(InboxMetaData.COLUMN_TIMESTAMP,inbox.getTimestamp());
	 Log.v("Inbox data source ", String.valueOf(inbox.getTimestamp()));
	 cv.put(InboxMetaData.COLUMN_FLAG,inbox.getFlag());
	 Log.v("Inbox data source ", String.valueOf(inbox.getFlag()));
	 cv.put(InboxMetaData.COLUMN_USER_ID,inbox.getUserId());
	 Log.v("Inbox data source ", String.valueOf(inbox.getUserId()));
	 
	long rid = db.insert(InboxMetaData.INBOX_TABLE_NAME, null, cv);
	if(rid==-1) 
	return false;
	else
		return true;
 }
 
 public Cursor getListofAllMessages()
 {
//	 String [] columns= new String[]{InboxMetaData.COLUMN_ID,InboxMetaData.COLUMN_FROM ,InboxMetaData.COLUMN_MESSAGE,InboxMetaData.COLUMN_TIMESTAMP,InboxMetaData.COLUMN_FLAG,InboxMetaData.COLUMN_TO, InboxMetaData.COLUMN_USER_ID};
//     Cursor c =db.query(InboxMetaData.INBOX_TABLE_NAME, columns, InboxMetaData.COLUMN_FLAG +" = ?",new String[]{"0"} ,InboxMetaData.COLUMN_FROM,null, InboxMetaData.COLUMN_TIMESTAMP+" DESC");
     String [] columns= new String[]{InboxMetaData.COLUMN_ID,InboxMetaData.COLUMN_FROM ,InboxMetaData.COLUMN_MESSAGE,InboxMetaData.COLUMN_TIMESTAMP,InboxMetaData.COLUMN_FLAG,InboxMetaData.COLUMN_TO, InboxMetaData.COLUMN_USER_ID};
     Cursor c =db.query(InboxMetaData.INBOX_TABLE_NAME, columns, null,null ,InboxMetaData.COLUMN_USER_ID,null, InboxMetaData.COLUMN_TIMESTAMP+" DESC");
 return c;
 }

 
 public Cursor getChatMessages(String mChatmember)
 {
	 String [] columns= new String[]{InboxMetaData.COLUMN_ID,InboxMetaData.COLUMN_FROM ,InboxMetaData.COLUMN_MESSAGE,InboxMetaData.COLUMN_TIMESTAMP,InboxMetaData.COLUMN_FLAG,InboxMetaData.COLUMN_TO, InboxMetaData.COLUMN_USER_ID};
     Cursor c =db.query(InboxMetaData.INBOX_TABLE_NAME, columns, InboxMetaData.COLUMN_FROM +" = ?"  ,new String[]{mChatmember} ,null,null, InboxMetaData.COLUMN_TIMESTAMP+" DESC");
 return c;
 }

 public Cursor getChatMessages1	(String mChatmember)
 {
//	 String contactEmail = "Ali";
	 String [] columns= new String[]{InboxMetaData.COLUMN_ID,InboxMetaData.COLUMN_FROM ,InboxMetaData.COLUMN_MESSAGE,InboxMetaData.COLUMN_TIMESTAMP,InboxMetaData.COLUMN_FLAG,InboxMetaData.COLUMN_TO, InboxMetaData.COLUMN_USER_ID};
     Cursor c =db.query(InboxMetaData.INBOX_TABLE_NAME, columns, InboxMetaData.COLUMN_TO +" = ?"  ,new String[]{mChatmember} ,null,null, InboxMetaData.COLUMN_TIMESTAMP+" DESC");
     return c;
 }

 
 
}



