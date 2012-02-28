package ru.kontur.elba;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

public class UnexpectedException implements Thread.UncaughtExceptionHandler{
	private Context context;

	public UnexpectedException(Context context) {
		this.context = context;
	}

	@Override
	public void uncaughtException(Thread thread, final Throwable throwable) {
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}.start();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Thread.getDefaultUncaughtExceptionHandler().uncaughtException(thread, throwable);
	}
}
