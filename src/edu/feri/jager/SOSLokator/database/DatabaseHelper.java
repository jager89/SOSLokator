package edu.feri.jager.SOSLokator.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public  class DatabaseHelper extends SQLiteOpenHelper 
{	
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "db_contactsID";
	private static final String DATABASE_CREATE =
		"create table " + DBAdapterContacts.TABLE + " (" + DBAdapterContacts._ID + " integer primary key autoincrement, "
		+ DBAdapterContacts.CONTACT_ID + " TEXT not null);";

	DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + DBAdapterContacts.TABLE);
		onCreate(db);
	}
}
