package com.hyperon.smsall;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InboxSQLiteOpenHelper extends SQLiteOpenHelper {

	public InboxSQLiteOpenHelper(Context context) {
		super(context, InboxMetaData.DATABASE_NAME, null, InboxMetaData.DATABSE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(InboxMetaData.FRIENDS_TABLE_CREATE_STMNT);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		//db.execSQL("DROP TABLE IF EXISTS" + FriendsMetaData.FRIENDS_TABLE_NAME);
		//onCreate(db);
		db.execSQL(InboxMetaData.FRIENDS_TABLE_CREATE_STMNT);
	}

}
