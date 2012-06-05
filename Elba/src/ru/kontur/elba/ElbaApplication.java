package ru.kontur.elba;

import android.app.Application;

import java.util.HashMap;

public class ElbaApplication extends Application {
	private HashMap<String, Object> services = new HashMap<String, Object>();

	@Override
	public void onCreate() {
		super.onCreate();
		Thread.currentThread().setUncaughtExceptionHandler(new UnexpectedException(this));
		addServices(new DocumentRepository(this), new DocumentItemRepository(this));
	}

	public DocumentRepository getBillRepository() {
		return get(DocumentRepository.class);
	}

	public DocumentItemRepository getBillItemRepository() {
		return get(DocumentItemRepository.class);
	}

	private void addServices(Object... services) {
		for (Object s : services)
			this.services.put(s.getClass().getName(), s);
	}

	public <T> T get(Class<T> type) {
		return (T) services.get(type.getName());
	}
}
