package ru.kontur.elba;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import ru.kontur.elba.datalayer.DocumentTable;
import ru.kontur.elba.domainmodel.Document;
import ru.kontur.elba.domainmodel.DocumentType;

import java.util.ArrayList;
import java.util.Date;

public class DocumentRepository extends RepositoryBase<Document> {

	private final DocumentType[] documentTypes;

	public DocumentRepository(Context context) {
		super(context, new DocumentTable());
		documentTypes = DocumentType.values();
	}

	public Document create(int documentType) {
		return addEntity(newDocument(documentType));
	}

	private Document newDocument(int documentType) {
		Document result = new Document();
		result.date = new Date();
		result.number = "1";
		result.type = DocumentType.values()[documentType];
		return result;
	}

	public Cursor getCursor() {
		return db.query(DocumentTable.TABLE_NAME, DocumentTable.allColumns, null, null, null, null, null);
	}

	public Document getById(int billId) throws SQLException {
		Cursor cursor = db.query(true, DocumentTable.TABLE_NAME,
				DocumentTable.allColumns,
				DocumentTable.KEY_ROWID + "=" + billId, null, null, null, null, null);
		if (cursor == null)
			throw new SQLException(String.format("Document with id = %d not found in repository", billId));
		cursor.moveToFirst();
		Document document = inflateBill(cursor);
		document.documentItems = new DocumentItemRepository(context).selectEntriesOfBill(document.id);
		cursor.close();
		return document;
	}

	private Document inflateBill(Cursor mCursor) {
		Document document = new Document();
		document.id = mCursor.getInt(mCursor.getColumnIndexOrThrow(DocumentTable.KEY_ROWID));
		document.type = documentTypes[mCursor.getInt(mCursor.getColumnIndexOrThrow(DocumentTable.KEY_TYPE))];
		document.number = mCursor.getString(mCursor.getColumnIndexOrThrow(DocumentTable.KEY_NUMBER));
		document.sum = readDecimal(mCursor, DocumentTable.KEY_SUM);
		document.date = new Date(mCursor.getLong(mCursor.getColumnIndexOrThrow(DocumentTable.KEY_DATE)));
		document.customerName = mCursor.getString(mCursor.getColumnIndexOrThrow(DocumentTable.KEY_CUSTOMERNAME));
		return document;
	}

	public Document[] selectAll() {
		ArrayList<Document> result = new ArrayList<Document>();
		Cursor cursor = getCursor();
		while (cursor.moveToNext())
			result.add(inflateBill(cursor));
		cursor.close();
		return result.toArray(new Document[result.size()]);
	}

	protected ContentValues createContentValues(Document document) {
		ContentValues values = new ContentValues();
		values.put(DocumentTable.KEY_NUMBER, document.number);
		values.put(DocumentTable.KEY_TYPE, document.type.ordinal());
		values.put(DocumentTable.KEY_SUM, decimalToString(document.sum));
		values.put(DocumentTable.KEY_CUSTOMERNAME, document.customerName);
		values.put(DocumentTable.KEY_DATE, document.date == null ? null : document.date.getTime());
		return values;
	}
}