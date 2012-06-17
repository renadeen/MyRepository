package ru.kontur.elba;

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
import ru.kontur.elba.datalayer.DocumentTable;
import ru.kontur.elba.datalayer.LocaleService;
import ru.kontur.elba.domainmodel.DocumentType;

import java.math.BigDecimal;
import java.util.Date;

public class DocumentListActivity extends Activity implements AdapterView.OnItemClickListener {
	private DocumentRepository documentRepository;
	private ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.document_list);
		list = (ListView) findViewById(android.R.id.list);
		list.setOnItemClickListener(this);
		list.setEmptyView(findViewById(android.R.id.empty));
		list.addHeaderView(getLayoutInflater().inflate(R.layout.header, list, false));
		documentRepository = ((ElbaApplication) getApplication()).getBillRepository();
		refresh();
	}

	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
		Intent intent = new Intent(this, EditDocumentActivity.class);
		intent.putExtra("documentId", extractId(i));
		startActivityForResult(intent, 1);
	}

	public int extractId(int position) {
		Cursor item = (Cursor) list.getAdapter().getItem(position);
		return item.getInt(item.getColumnIndex(DocumentTable.KEY_ROWID));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		refresh();
	}

	private void refresh() {
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.document_list_item,
				documentRepository.getCursor(),
				new String[]{DocumentTable.KEY_TYPE, DocumentTable.KEY_NUMBER, DocumentTable.KEY_SUM, DocumentTable.KEY_DATE, DocumentTable.KEY_CUSTOMERNAME},
				new int[]{R.id.type, R.id.number, R.id.sum, R.id.date, R.id.contractorName});
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
				if (view.getId() == R.id.type) {
					((TextView) view).setText(DocumentType.values()[cursor.getInt(columnIndex)].toString());
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
		for (DocumentType t : DocumentType.values())
			menu.add(Menu.NONE, t.ordinal(), Menu.NONE, t.toString());
		return b;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		createDocument(item.getItemId());
		return true;
	}

	private void createDocument(int documentType) {
		Intent i = new Intent(this, EditDocumentActivity.class);
		i.putExtra("documentType", documentType);
		startActivityForResult(i, 1);
	}
}