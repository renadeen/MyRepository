package com.skbkontur.elba.datalayer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ElbaDatabase extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "elba";
	private static final int DATABASE_VERSION = 1;

	public ElbaDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(BillTable.BILL_CREATE);
		database.execSQL(BillItemTable.BILLITEM_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
//		Log.w(Bill.class.getName(), "Upgrading database from version "
//				+ oldVersion + " to " + newVersion
//				+ ", which will destroy all old data");
//		database.execSQL("DROP TABLE IF EXISTS Bill");
//		onCreate(database);
	}

	public void recreate() {
		SQLiteDatabase database = getWritableDatabase();
		database.execSQL("DROP TABLE IF EXISTS Bill");
		database.execSQL("DROP TABLE IF EXISTS BillItem");
		onCreate(database);
	}
}