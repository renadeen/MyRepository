package ru.kontur.elba;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import ru.kontur.elba.datalayer.LocaleService;
import ru.kontur.elba.domainmodel.DocumentItem;

import java.util.List;

public class InterestingAdapter extends BaseAdapter {
	private Activity activity;
	private int itemLayout;
	private List<DocumentItem> documentItems;

	public InterestingAdapter(Activity context, int itemLayout, List<DocumentItem> documentItems) {
		this.activity = context;
		this.itemLayout = itemLayout;
		this.documentItems = documentItems;
	}

	public void reload(List<DocumentItem> items) {
		documentItems = items;
	}

	public int getCount() {
		return documentItems.size();
	}

	public Object getItem(int i) {
		return documentItems.get(i);
	}

	public long getItemId(int i) {
		return documentItems.get(i).id;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = activity.getLayoutInflater().inflate(itemLayout, parent, false);
		TextView name = (TextView) view.findViewById(R.id.name);
		TextView details = (TextView) view.findViewById(R.id.details);
		TextView sum = (TextView) view.findViewById(R.id.sum);
		DocumentItem item = documentItems.get(position);
		name.setText(item.name);
		details.setText(String.format("%1$s %2$s x %3$s р.", item.quantity, item.unit, item.price));
		sum.setText(LocaleService.getInstance().formatCurrency(item.quantity.multiply(item.price)) + " р.");
		return view;
	}
}
