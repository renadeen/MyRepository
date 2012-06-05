package ru.kontur.elba.datalayer;

public class DocumentTable implements Table {

	public static final String KEY_ROWID = "_id";
	public static final String KEY_TYPE = "type";
	public static final String KEY_NUMBER = "number";
	public static String KEY_DATE = "date";
	public static final String KEY_SUM = "sum";
	public static final String KEY_CUSTOMERNAME = "customerName";
	public static final String KEY_CUSTOMERID = "customerId";
	public static final String TABLE_NAME = "Document";
	public static final String[] allColumns = new String[]{KEY_ROWID, KEY_TYPE, KEY_NUMBER, KEY_SUM, KEY_DATE, KEY_CUSTOMERNAME};

	public static final String DOCUMENT_CREATE = "create table " + TABLE_NAME + " ("
			+ KEY_ROWID + " integer primary key autoincrement, "
			+ KEY_TYPE + " integer not null, "
			+ KEY_NUMBER + " text null, "
			+ KEY_SUM + " text null, "
			+ KEY_DATE + " integer null, "
			+ KEY_CUSTOMERID + " integer null, "
			+ KEY_CUSTOMERNAME + " text null);";

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return KEY_ROWID;
	}
}
