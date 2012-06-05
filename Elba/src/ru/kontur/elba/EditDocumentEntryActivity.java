package ru.kontur.elba;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import ru.kontur.elba.datalayer.LocaleService;
import ru.kontur.elba.domainmodel.DocumentItem;

public class EditDocumentEntryActivity extends Activity {
	private EditText nameInput;
	private EditText quantityInput;
	private EditText unitInput;
	private EditText priceInput;
	private DocumentItem documentItem;
	private DocumentItemRepository documentItemRepository;
	private boolean removed;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_document_entry);

		nameInput = (EditText) findViewById(R.id.name);
		quantityInput = (EditText) findViewById(R.id.quantity);
		unitInput = (EditText) findViewById(R.id.unit);
		priceInput = (EditText) findViewById(R.id.price);

		documentItemRepository = ((ElbaApplication) getApplication()).getBillItemRepository();
		Bundle extras = getIntent().getExtras();
		if (extras.containsKey("billItemId"))
			documentItem = documentItemRepository.getById(extras.getInt("billItemId"));
		else
			documentItem = documentItemRepository.create(extras.getInt("billId"));
		scatter(documentItem);
	}

	public void scatter(DocumentItem documentItem) {
		nameInput.setText(documentItem.name);
		quantityInput.setText(LocaleService.getInstance().formatCurrency(documentItem.quantity));
		unitInput.setText(documentItem.unit);
		priceInput.setText(LocaleService.getInstance().formatCurrency(documentItem.price));
	}

	public void gather(DocumentItem documentItem) {
		documentItem.name = nameInput.getText().toString();
		documentItem.quantity = LocaleService.getInstance().parseCurrency(quantityInput.getText().toString());
		documentItem.unit = unitInput.getText().toString();
		documentItem.price = LocaleService.getInstance().parseCurrency(priceInput.getText().toString());
	}

	@Override
	public void onPause() {
		super.onPause();
		if (removed) return;
		gather(documentItem);
		documentItemRepository.save(documentItem);
	}

	public void remove(View view) {
		documentItemRepository.delete(documentItem);
		removed = true;
		finish();
	}
}