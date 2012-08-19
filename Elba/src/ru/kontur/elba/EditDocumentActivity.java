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
import ru.kontur.elba.domainmodel.DocumentItem;

import java.math.BigDecimal;
import java.util.ArrayList;
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
			dateButton.setText(document.getFormattedDate());
		}
	};
	private EditText numberInput;
	private TextView sumTextView;
	private Button dateButton;
	private ListView list;
	private AutoCompleteTextView customerName;

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

		initListView();
		numberInput = (EditText) findViewById(R.id.number);
		sumTextView = (TextView) findViewById(R.id.sum);
		dateButton = (Button) findViewById(R.id.date);
		initCustomerAutocomplete();
		scatter(document);
	}

	private void initListView() {
		list = (ListView) findViewById(android.R.id.list);
		list.setOnItemClickListener(this);
		list.addHeaderView(getLayoutInflater().inflate(R.layout.edit_document_header, list, false));
		list.addFooterView(getLayoutInflater().inflate(R.layout.edit_document_footer, list, false));
	}

	private void initCustomerAutocomplete() {
		customerName = (AutoCompleteTextView) findViewById(R.id.customerName);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.choose_customer_item, getCustomers());
		customerName.setText(documentRepository.getById(document.id).customerName);
		customerName.setAdapter(adapter);
	}

	private String[] getCustomers() {
		Document[] documents = documentRepository.selectAll();
		ArrayList<String> result = new ArrayList<String>();
		for (Document document : documents)
			result.add(document.customerName);
		return result.toArray(new String[0]);
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
		sumTextView.setText(LocaleService.getInstance().formatCurrency(sum(document)));
		customerName.setText(document.customerName);
		dateButton.setText(document.getFormattedDate());
		DocumentItem[] a = new DocumentItem[document.documentItems.size()];
		PlainAdapter adapter = new PlainAdapter<DocumentItem>(this, R.layout.edit_document_list_item, document.documentItems.toArray(a),
				new PlainAdapter.ItemViewBinder<DocumentItem>() {
					@Override
					public void bindItemView(DocumentItem item, View itemView) {
						((TextView) itemView.findViewById(R.id.name)).setText(item.name);
						((TextView) itemView.findViewById(R.id.details)).setText(String.format("%1$s %2$s x %3$s р.", item.quantity, item.unit, item.price));
						((TextView) itemView.findViewById(R.id.sum)).setText(LocaleService.getInstance().formatCurrency(item.quantity.multiply(item.price)) + " р.");
					}
				});
		list.setAdapter(adapter);
		if (document.documentItems.size() == 0) {
			findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
			findViewById(android.R.id.summary).setVisibility(View.GONE);
		} else {
			findViewById(android.R.id.empty).setVisibility(View.GONE);
			findViewById(android.R.id.summary).setVisibility(View.VISIBLE);
		}
	}

	private BigDecimal sum(Document document) {
		BigDecimal sum = BigDecimal.ZERO;
		for (int i = 0; i < document.documentItems.size(); i++)
			sum = sum.add(document.documentItems.get(i).price.multiply(document.documentItems.get(i).quantity));
		return sum;
	}

	private void gather(Document document) {
		document.number = numberInput.getText().toString();
		document.customerName = customerName.getText().toString();
		document.sum = LocaleService.getInstance().parseCurrency(sumTextView.getText().toString());
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
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.document_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
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
}