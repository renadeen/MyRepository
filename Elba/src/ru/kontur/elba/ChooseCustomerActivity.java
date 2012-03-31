package ru.kontur.elba;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import ru.kontur.elba.domainmodel.Bill;

import java.util.ArrayList;

public class ChooseCustomerActivity extends Activity {

	private int documentId;
	private BillRepository billRepository;
	private AutoCompleteTextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_customer);
		billRepository = ((ElbaApplication) getApplication()).getBillRepository();
		textView = (AutoCompleteTextView) findViewById(android.R.id.input);
		documentId = getIntent().getIntExtra("documentId", -1);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.choose_customer_item, getCustomers());
		textView.setText(billRepository.getById(documentId).customerName);
		textView.setThreshold(1);
		textView.setAdapter(adapter);
	}

	private String[] getCustomers() {
		Bill[] bills = ((ElbaApplication) getApplication()).getBillRepository().selectAll();
		ArrayList<String> result = new ArrayList<String>();
		for (Bill bill : bills)
			result.add(bill.customerName);
		return result.toArray(new String[0]);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Bill bill = billRepository.getById(documentId);
		bill.customerName = textView.getText().toString();
		billRepository.save(bill);
	}
}
