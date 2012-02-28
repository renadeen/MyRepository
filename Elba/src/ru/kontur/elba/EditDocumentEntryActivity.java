package ru.kontur.elba;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.skbkontur.elba.R;
import ru.kontur.elba.datalayer.LocaleService;
import ru.kontur.elba.domainmodel.BillItem;

public class EditDocumentEntryActivity extends Activity {
	private EditText nameInput;
	private EditText quantityInput;
	private EditText unitInput;
	private EditText priceInput;
	private BillItem billItem;
	private BillItemRepository billItemRepository;
	private boolean removed;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_document_entry);

		nameInput = (EditText) findViewById(R.id.name);
		quantityInput = (EditText) findViewById(R.id.quantity);
		unitInput = (EditText) findViewById(R.id.unit);
		priceInput = (EditText) findViewById(R.id.price);

		billItemRepository = new BillItemRepository(this);
		Bundle extras = getIntent().getExtras();
		if (extras.containsKey("billItemId"))
			billItem = billItemRepository.getById(extras.getInt("billItemId"));
		else
			billItem = billItemRepository.create(extras.getInt("billId"));
		scatter(billItem);
	}

	public void scatter(BillItem billItem) {
		nameInput.setText(billItem.name);
		quantityInput.setText(LocaleService.getInstance().formatCurrency(billItem.quantity));
		unitInput.setText(billItem.unit);
		priceInput.setText(LocaleService.getInstance().formatCurrency(billItem.price));
	}

	public void gather(BillItem billItem) {
		billItem.name = nameInput.getText().toString();
		billItem.quantity = LocaleService.getInstance().parseCurrency(quantityInput.getText().toString());
		billItem.unit = unitInput.getText().toString();
		billItem.price = LocaleService.getInstance().parseCurrency(priceInput.getText().toString());
	}

	@Override
	public void onPause() {
		super.onPause();
		if (removed) return;
		gather(billItem);
		billItemRepository.save(billItem);
	}

	public void remove(View view) {
		billItemRepository.delete(billItem);
		removed = true;
		finish();
	}
}