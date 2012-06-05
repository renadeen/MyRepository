package ru.kontur.elba.datalayer;

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
		database.execSQL(DocumentTable.DOCUMENT_CREATE);
		database.execSQL(DocumentItemTable.DOCUMENTITEM_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
	}

	public void recreate() {
		SQLiteDatabase database = getWritableDatabase();
		database.execSQL("DROP TABLE IF EXISTS Document");
		database.execSQL("DROP TABLE IF EXISTS DocumentItem");
		onCreate(database);
	}
}