package ru.kontur.elba;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import ru.kontur.elba.datalayer.BillItemTable;
import ru.kontur.elba.domainmodel.BillItem;

import java.util.ArrayList;

public class BillItemRepository extends RepositoryBase<BillItem> {
	public BillItemRepository(Context context) {
		super(context, new BillItemTable());
	}

	public ArrayList<BillItem> selectEntriesOfBill(int billId) {
		ArrayList<BillItem> result = new ArrayList<BillItem>();
		Cursor cursor = db.query(true, BillItemTable.TABLE_NAME,
				BillItemTable.allColumns,
				BillItemTable.KEY_DOCUMENTID + "=" + billId, null, null, null, null, null);
		if (cursor == null || !cursor.moveToFirst())
			return result;
		do
			result.add(inflate(cursor));
		while (cursor.moveToNext());
		cursor.close();
		return result;
	}

	public BillItem getById(int billItemId) throws SQLException {
		Cursor cursor = db.query(true, BillItemTable.TABLE_NAME,
				BillItemTable.allColumns,
				BillItemTable.KEY_ROWID + "=" + billItemId, null, null, null, null, null);
		if (cursor == null || !cursor.moveToFirst())
			throw new SQLException(String.format("BillItem with id = %d not found in repository", billItemId));

		BillItem result = inflate(cursor);
		cursor.close();
		return result;
	}

	private BillItem inflate(Cursor mCursor) {
		BillItem billItem = new BillItem();
		billItem.id = mCursor.getInt(mCursor.getColumnIndexOrThrow(BillItemTable.KEY_ROWID));
		billItem.documentId = mCursor.getInt(mCursor.getColumnIndexOrThrow(BillItemTable.KEY_DOCUMENTID));
		billItem.name = mCursor.getString(mCursor.getColumnIndexOrThrow(BillItemTable.KEY_NAME));
		billItem.quantity = readDecimal(mCursor, BillItemTable.KEY_QUANTITY);
		billItem.unit = mCursor.getString(mCursor.getColumnIndexOrThrow(BillItemTable.KEY_UNIT));
		billItem.price = readDecimal(mCursor, BillItemTable.KEY_PRICE);
		billItem.sum = readDecimal(mCursor, BillItemTable.KEY_SUM);
		return billItem;
	}

	protected ContentValues createContentValues(BillItem billItem) {
		ContentValues values = new ContentValues();
		values.put(BillItemTable.KEY_DOCUMENTID, billItem.documentId);
		values.put(BillItemTable.KEY_NAME, billItem.name);
		values.put(BillItemTable.KEY_QUANTITY, decimalToString(billItem.quantity));
		values.put(BillItemTable.KEY_PRICE, decimalToString(billItem.price));
		values.put(BillItemTable.KEY_UNIT, billItem.unit);
		values.put(BillItemTable.KEY_SUM, decimalToString(billItem.sum));
		return values;
	}

	public BillItem create(int billId) {
		BillItem billItem = new BillItem();
		billItem.documentId = billId;
		return addEntity(billItem);
	}
}