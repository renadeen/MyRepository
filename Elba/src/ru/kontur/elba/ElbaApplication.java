package ru.kontur.elba;

import android.app.Application;
import ru.kontur.elba.BillItemRepository;
import ru.kontur.elba.BillRepository;
import ru.kontur.elba.UnexpectedException;

import java.util.HashMap;

public class ElbaApplication extends Application {
	private HashMap<String, Object> services = new HashMap<String, Object>();

	@Override
	public void onCreate() {
		super.onCreate();
		Thread.currentThread().setUncaughtExceptionHandler(new UnexpectedException(this));
		addServices(new BillRepository(this), new BillItemRepository(this));
	}

	public BillRepository getBillRepository() {
		return get(BillRepository.class);
	}

	public BillItemRepository getBillItemRepository() {
		return get(BillItemRepository.class);
	}

	private void addServices(Object... services) {
		for (Object s : services)
			this.services.put(s.getClass().getName(), s);
	}

	public <T> T get(Class<T> type) {
		return (T) services.get(type.getName());
	}
}
