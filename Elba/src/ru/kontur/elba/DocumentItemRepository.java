package ru.kontur.elba;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import ru.kontur.elba.datalayer.DocumentItemTable;
import ru.kontur.elba.domainmodel.DocumentItem;

import java.util.ArrayList;

public class DocumentItemRepository extends RepositoryBase<DocumentItem> {
	public DocumentItemRepository(Context context) {
		super(context, new DocumentItemTable());
	}

	public ArrayList<DocumentItem> selectEntriesOfBill(int billId) {
		ArrayList<DocumentItem> result = new ArrayList<DocumentItem>();
		Cursor cursor = db.query(true, DocumentItemTable.TABLE_NAME,
				DocumentItemTable.allColumns,
				DocumentItemTable.KEY_DOCUMENTID + "=" + billId, null, null, null, null, null);
		while (cursor.moveToNext())
			result.add(inflate(cursor));
		cursor.close();
		return result;
	}

	public DocumentItem getById(int billItemId) throws SQLException {
		Cursor cursor = db.query(true, DocumentItemTable.TABLE_NAME,
				DocumentItemTable.allColumns,
				DocumentItemTable.KEY_ROWID + "=" + billItemId, null, null, null, null, null);
		if (cursor == null || !cursor.moveToFirst())
			throw new SQLException(String.format("DocumentItem with id = %d not found in repository", billItemId));

		DocumentItem result = inflate(cursor);
		cursor.close();
		return result;
	}

	private DocumentItem inflate(Cursor mCursor) {
		DocumentItem documentItem = new DocumentItem();
		documentItem.id = mCursor.getInt(mCursor.getColumnIndexOrThrow(DocumentItemTable.KEY_ROWID));
		documentItem.documentId = mCursor.getInt(mCursor.getColumnIndexOrThrow(DocumentItemTable.KEY_DOCUMENTID));
		documentItem.name = mCursor.getString(mCursor.getColumnIndexOrThrow(DocumentItemTable.KEY_NAME));
		documentItem.quantity = readDecimal(mCursor, DocumentItemTable.KEY_QUANTITY);
		documentItem.unit = mCursor.getString(mCursor.getColumnIndexOrThrow(DocumentItemTable.KEY_UNIT));
		documentItem.price = readDecimal(mCursor, DocumentItemTable.KEY_PRICE);
		documentItem.sum = readDecimal(mCursor, DocumentItemTable.KEY_SUM);
		return documentItem;
	}

	protected ContentValues createContentValues(DocumentItem documentItem) {
		ContentValues values = new ContentValues();
		values.put(DocumentItemTable.KEY_DOCUMENTID, documentItem.documentId);
		values.put(DocumentItemTable.KEY_NAME, documentItem.name);
		values.put(DocumentItemTable.KEY_QUANTITY, decimalToString(documentItem.quantity));
		values.put(DocumentItemTable.KEY_PRICE, decimalToString(documentItem.price));
		values.put(DocumentItemTable.KEY_UNIT, documentItem.unit);
		values.put(DocumentItemTable.KEY_SUM, decimalToString(documentItem.sum));
		return values;
	}

	public DocumentItem create(int billId) {
		DocumentItem documentItem = new DocumentItem();
		documentItem.documentId = billId;
		return addEntity(documentItem);
	}
}