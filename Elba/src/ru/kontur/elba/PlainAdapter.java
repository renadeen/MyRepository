package ru.kontur.elba;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import ru.kontur.elba.datalayer.Entity;

public class PlainAdapter<T extends Entity> extends BaseAdapter {
	private Activity activity;
	private int itemLayout;
	private T[] items;
	private ItemViewBinder<T> binder;

	public PlainAdapter(Activity context, int itemLayout, T[] items, ItemViewBinder<T> binder) {
		this.activity = context;
		this.itemLayout = itemLayout;
		this.items = items;
		this.binder = binder;
	}

	public void reload(T[] items) {
		this.items = items;
	}

	public int getCount() {
		return items.length;
	}

	public Object getItem(int i) {
		return items[i];
	}

	public long getItemId(int i) {
		return items[i].getId();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = activity.getLayoutInflater().inflate(itemLayout, parent, false);
		T item = items[position];
		binder.bindItemView(item, view);
		return view;
	}

	public interface ItemViewBinder<T>{
		void bindItemView(T item, View itemView);
	}
}
