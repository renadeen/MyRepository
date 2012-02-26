package com.skbkontur.elba;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.skbkontur.elba.datalayer.BillTable;
import com.skbkontur.elba.datalayer.LocaleService;

import java.math.BigDecimal;
import java.util.Date;

public class DocumentListActivity extends Activity implements AdapterView.OnItemClickListener {
	private static final int ADD_KEY = 1;
	private BillRepository billRepository;
	private ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.document_list);
		list = (ListView) findViewById(android.R.id.list);
		list.setOnItemClickListener(this);
		list.setEmptyView(findViewById(android.R.id.empty));
		list.addHeaderView(getLayoutInflater().inflate(R.layout.header, list, false));
		billRepository = new BillRepository(this);
//		billRepository.createTestBill("22");
//		billRepository.createTestBill("33");
		refresh();
	}

	@Override
	protected void onDestroy() {
		if (billRepository != null)
			billRepository.close();
		super.onDestroy();
	}

	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
		Intent intent = new Intent(this, EditDocumentActivity.class);
		intent.putExtra("documentId", extractId(i));
		startActivityForResult(intent, 1);
	}

	public int extractId(int position) {
		Cursor item = (Cursor) list.getAdapter().getItem(position);
		return item.getInt(item.getColumnIndex(BillTable.KEY_ROWID));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		refresh();
	}

	private void refresh() {
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.document_list_item,
				billRepository.getCursor(),
				new String[]{BillTable.KEY_NUMBER, BillTable.KEY_SUM, BillTable.KEY_DATE, BillTable.KEY_CUSTOMERNAME},
				new int[]{R.id.number, R.id.sum, R.id.date, R.id.contractorName});
		adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				if (view.getId() == R.id.date) {
					((TextView) view).setText(LocaleService.getInstance().formatDate(new Date(cursor.getLong(columnIndex))));
					return true;
				}
				if (view.getId() == R.id.sum) {
					((TextView) view).setText(LocaleService.getInstance()
							.formatCurrency(BigDecimal.valueOf(cursor.getDouble(columnIndex))));
					return true;
				}
				return false;
			}
		});
		list.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean b = super.onCreateOptionsMenu(menu);
		menu.add(0, ADD_KEY, 0, "Добавить документ");
		return b;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean b = super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
			case ADD_KEY:
				createDocument();
				return true;
		}
		return b;
	}

	private void createDocument() {
		Intent i = new Intent(this, EditDocumentActivity.class);
		startActivityForResult(i, 1);
	}
}