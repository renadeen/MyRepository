package com.skbkontur.elba.datalayer;

public class BillItemTable implements Table{
	public static final String TABLE_NAME = "BillItem";
	public static final String KEY_ROWID = "_id";
	public static final String KEY_DOCUMENTID = "documentId";
	public static final String KEY_NAME = "name";
	public static final String KEY_QUANTITY = "quantity";
	public static final String KEY_UNIT = "unit";
	public static final String KEY_PRICE = "price";
	public static final String KEY_SUM = "sum";
	public static final String BILLITEM_CREATE = "create table BillItem ("
			+ "_id integer primary key autoincrement, "
			+ KEY_DOCUMENTID + " int not null, "
			+ KEY_NAME + " text null, "
			+ KEY_QUANTITY + " text null, "
			+ KEY_UNIT + " text null,"
			+ KEY_PRICE + " text null,"
			+ KEY_SUM + " text null);";

	public static final String[] allColumns = new String[]{KEY_ROWID, KEY_DOCUMENTID, KEY_NAME, KEY_QUANTITY, KEY_UNIT, KEY_PRICE, KEY_SUM};

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return KEY_ROWID;
	}
}
