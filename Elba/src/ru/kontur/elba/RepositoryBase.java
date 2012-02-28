package ru.kontur.elba;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ru.kontur.elba.datalayer.ElbaDatabase;
import ru.kontur.elba.datalayer.Entity;
import ru.kontur.elba.datalayer.Table;

import java.math.BigDecimal;

public abstract class RepositoryBase<T extends Entity> {
	protected Context context;
	private Table table;
	protected SQLiteDatabase db;
	private ElbaDatabase dbHelper;

	public RepositoryBase(Context context, Table table) {
		this.context = context;
		this.table = table;
		dbHelper = new ElbaDatabase(context);
		db = dbHelper.getWritableDatabase();
	}

	protected T addEntity(T entity) {
		entity.setId((int) db.insertOrThrow(table.tableName(), null, createContentValues(entity)));
		return entity;
	}

	public void save(T entity) {
		ContentValues values = createContentValues(entity);
//		if (bill.id == 0)
//			db.insertOrThrow(BillTable.TABLE_NAME, null, values);
		if(db.update(table.tableName(), values, table.keyColumn() + "=" + entity.getId(), null) == 0)
			throw new Error("can't update " +table.tableName() + "\nentityId=" + entity.getId() + "\nvalues=" + values.toString());
	}

	public boolean delete(T entity) {
		return db.delete(table.tableName(), table.keyColumn() + "=" + entity.getId(), null) > 0;
	}

	public void close() {
		dbHelper.close();
	}

	protected BigDecimal readDecimal(Cursor cursor, String column) {
		return new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(column)));
	}

	protected String decimalToString(BigDecimal decimal) {
		return (decimal == null ? BigDecimal.ZERO : decimal).toString();
	}

	protected abstract ContentValues createContentValues(T bill);
}
