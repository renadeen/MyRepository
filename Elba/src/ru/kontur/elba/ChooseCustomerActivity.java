package ru.kontur.elba;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import ru.kontur.elba.domainmodel.Document;

import java.util.ArrayList;

public class ChooseCustomerActivity extends Activity {

	private int documentId;
	private DocumentRepository documentRepository;
	private AutoCompleteTextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_customer);
		documentRepository = ((ElbaApplication) getApplication()).getBillRepository();
		textView = (AutoCompleteTextView) findViewById(android.R.id.input);
		documentId = getIntent().getIntExtra("documentId", -1);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.choose_customer_item, getCustomers());
		textView.setText(documentRepository.getById(documentId).customerName);
		textView.setThreshold(1);
		textView.setAdapter(adapter);
	}

	private String[] getCustomers() {
		Document[] documents = ((ElbaApplication) getApplication()).getBillRepository().selectAll();
		ArrayList<String> result = new ArrayList<String>();
		for (Document document : documents)
			result.add(document.customerName);
		return result.toArray(new String[0]);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Document document = documentRepository.getById(documentId);
		document.customerName = textView.getText().toString();
		documentRepository.save(document);
	}
}
