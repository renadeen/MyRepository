package ru.kontur.elba;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import ru.kontur.elba.datalayer.BillTable;
import ru.kontur.elba.domainmodel.Bill;

import java.util.Date;

public class BillRepository extends RepositoryBase<Bill> {

	public BillRepository(Context context) {
		super(context, new BillTable());
	}

	public Bill create() {
		return addEntity(newBill());
	}

	private Bill newBill() {
		Bill result = new Bill();
		result.date = new Date();
		result.number = "1";
		return result;
	}

	public Cursor getCursor() {
		return db.query(BillTable.TABLE_NAME, BillTable.allColumns, null, null, null, null, null);
	}

	public Bill getById(int billId) throws SQLException {
		Cursor mCursor = db.query(true, BillTable.TABLE_NAME,
				BillTable.allColumns,
				BillTable.KEY_ROWID + "=" + billId, null, null, null, null, null);
		if (mCursor == null)
			throw new SQLException(String.format("Bill with id = %d not found in repository", billId));
		mCursor.moveToFirst();
		Bill bill = newBill();
		bill.id = mCursor.getInt(mCursor.getColumnIndexOrThrow(BillTable.KEY_ROWID));
		bill.number = mCursor.getString(mCursor.getColumnIndexOrThrow(BillTable.KEY_NUMBER));
		bill.sum = readDecimal(mCursor, BillTable.KEY_SUM);
		bill.date = new Date(mCursor.getLong(mCursor.getColumnIndexOrThrow(BillTable.KEY_DATE)));
		bill.contractorName = mCursor.getString(mCursor.getColumnIndexOrThrow(BillTable.KEY_CUSTOMERNAME));
		bill.billItems = new BillItemRepository(context).selectEntriesOfBill(bill.id);
		return bill;
	}

	protected ContentValues createContentValues(Bill bill) {
		ContentValues values = new ContentValues();
		values.put(BillTable.KEY_NUMBER, bill.number);
		values.put(BillTable.KEY_SUM, decimalToString(bill.sum));
		values.put(BillTable.KEY_CUSTOMERNAME, bill.contractorName);
		values.put(BillTable.KEY_DATE, bill.date == null ? null : bill.date.getTime());
		return values;
	}
}