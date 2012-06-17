package ru.kontur.elba;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import ru.kontur.elba.datalayer.LocaleService;
import ru.kontur.elba.domainmodel.Document;

import java.math.BigDecimal;
import java.util.Date;

public class EditDocumentActivity extends Activity implements AdapterView.OnItemClickListener {

	private Document document;
	private DocumentRepository documentRepository;
	private static final int DATE_DIALOG_ID = 0;
	private static final int ADD_ITEM = 0;
	private static final int EDIT_ITEM = 1;
	private final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker datePicker, int year, int month, int day) {
			document.date = new Date(year - 1900, month, day); // ппц гавнище этот java.util.date
			dateView.setText(document.getFormattedDate());
		}
	};
	private EditText numberInput;
	private TextView sumInput;
	private TextView dateView;
	private ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_document);

		documentRepository = ((ElbaApplication) getApplication()).getBillRepository();
		Bundle extras = getIntent().getExtras();

		if (extras.containsKey("documentType"))
			document = documentRepository.create(extras.getInt("documentType"));
		else
			document = documentRepository.getById(extras.getInt("documentId"));

		list = (ListView) findViewById(android.R.id.list);
		list.setOnItemClickListener(this);
		list.addHeaderView(getLayoutInflater().inflate(R.layout.edit_document_header, list, false));
		list.addFooterView(getLayoutInflater().inflate(R.layout.edit_document_footer, list, false));
		numberInput = (EditText) findViewById(R.id.number);
		sumInput = (TextView) findViewById(R.id.sum);
		dateView = (TextView) findViewById(R.id.date);

		scatter(document);
	}

	@Override
	protected void onPause() {
		super.onPause();
		document = documentRepository.getById(document.id);
		gather(document);
		documentRepository.save(document);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	public void addItem(View view) {
		Intent intent = new Intent(this, EditDocumentEntryActivity.class);
		intent.putExtra("billId", document.id);
		startActivityForResult(intent, ADD_ITEM);
	}

	private void scatter(Document document) {
		numberInput.setText(document.number);
		sumInput.setText(LocaleService.getInstance().formatCurrency(sum(document)));
		((TextView) findViewById(R.id.contractorName)).setText(document.customerName);
		dateView.setText(document.getFormattedDate());
		InterestingAdapter adapter = new InterestingAdapter(this, R.layout.edit_document_list_item, document.documentItems);
		list.setAdapter(adapter);
	}

	private BigDecimal sum(Document document) {
		BigDecimal sum = BigDecimal.ZERO;
		for (int i = 0; i < document.documentItems.size(); i++)
			sum = sum.add(document.documentItems.get(i).price.multiply(document.documentItems.get(i).quantity));
		return sum;
	}

	private void gather(Document document) {
		document.number = numberInput.getText().toString();
		document.sum = LocaleService.getInstance().parseCurrency(sumInput.getText().toString());
	}

	public void pickDate(View view) {
		showDialog(DATE_DIALOG_ID);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Date date = document.date != null ? document.date : new Date();
		if (id == DATE_DIALOG_ID)
			return new DatePickerDialog(this, dateSetListener, date.getYear() + 1900, date.getMonth(), date.getDate());
		return null;
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
		Intent intent = new Intent(this, EditDocumentEntryActivity.class);
		intent.putExtra("billItemId", (int) id);
		startActivityForResult(intent, EDIT_ITEM);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		document = documentRepository.getById(document.id);
		scatter(document);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.document_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()){
			case R.id.preview:
				showPreview();
				return true;
			default:
				return super.onOptionsItemSelected(menuItem);
		}
	}

	private void showPreview() {
		Intent intent = new Intent(this, PreviewActivity.class);
		intent.putExtra("documentId", document.id);
		startActivity(intent);
	}

	public void chooseCustomer(View view) {
		Intent intent = new Intent(this, ChooseCustomerActivity.class);
		intent.putExtra("documentId", document.id);
		startActivity(intent);
	}
}