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
import com.skbkontur.elba.R;
import ru.kontur.elba.datalayer.LocaleService;
import ru.kontur.elba.domainmodel.Bill;

import java.math.BigDecimal;
import java.util.Date;

public class EditDocumentActivity extends Activity implements AdapterView.OnItemClickListener {

	private Bill bill;
	private BillRepository billRepository;
	private static final int DATE_DIALOG_ID = 0;
	private static final int ADD_ITEM = 0;
	private static final int EDIT_ITEM = 1;
	private final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker datePicker, int year, int month, int day) {
			bill.date = new Date(year - 1900, month, day); // ппц гавнище этот java.util.date
			dateView.setText(LocaleService.getInstance().formatDate(bill.date));
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

		billRepository = new BillRepository(this);
		Bundle extras = getIntent().getExtras();
		if (extras == null)
			bill = billRepository.create();
		else
			bill = billRepository.getById(extras.getInt("documentId"));

		list = (ListView) findViewById(android.R.id.list);
		list.setOnItemClickListener(this);
		list.addHeaderView(getLayoutInflater().inflate(R.layout.edit_document_header, list, false));
		list.addFooterView(getLayoutInflater().inflate(R.layout.edit_document_footer, list, false));
		numberInput = (EditText) findViewById(R.id.number);
		sumInput = (TextView) findViewById(R.id.sum);
		dateView = (TextView) findViewById(R.id.date);

		scatter(bill);
	}

	@Override
	protected void onPause() {
		super.onPause();
		gather(bill);
		billRepository.save(bill);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	public void addItem(View view) {
		Intent intent = new Intent(this, EditDocumentEntryActivity.class);
		intent.putExtra("billId", bill.id);
		startActivityForResult(intent, ADD_ITEM);
	}

	private void scatter(Bill bill) {
		numberInput.setText(bill.number);
		sumInput.setText(LocaleService.getInstance().formatCurrency(sum(bill)));
		((EditText) findViewById(R.id.contractorName)).setText(bill.contractorName);
		dateView.setText(LocaleService.getInstance().formatDate(bill.date));
		InterestingAdapter adapter = new InterestingAdapter(this, R.layout.edit_document_list_item, bill.billItems);
		list.setAdapter(adapter);
	}

	private BigDecimal sum(Bill bill) {
		BigDecimal sum = BigDecimal.ZERO;
		for (int i = 0; i < bill.billItems.size(); i++)
			sum = sum.add(bill.billItems.get(i).price.multiply(bill.billItems.get(i).quantity));
		return sum;
	}

	private void gather(Bill bill) {
		bill.number = numberInput.getText().toString();
		bill.sum = LocaleService.getInstance().parseCurrency(sumInput.getText().toString());
		bill.contractorName = ((EditText) findViewById(R.id.contractorName)).getText().toString();
	}

	public void pickDate(View view) {
		showDialog(DATE_DIALOG_ID);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Date date = bill.date != null ? bill.date : new Date();
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
		bill = billRepository.getById(bill.id);
		scatter(bill);
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
		intent.putExtra("documentId", bill.id);
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		if (billRepository != null)
			billRepository.close();
		super.onDestroy();
	}
}