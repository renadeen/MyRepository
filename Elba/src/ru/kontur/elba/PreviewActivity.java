package ru.kontur.elba;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import ru.kontur.elba.core.NumberService;
import ru.kontur.elba.domainmodel.Bill;
import ru.kontur.elba.domainmodel.Organization;

import java.io.*;
import java.util.HashMap;

public class PreviewActivity extends Activity {

	private WebView view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		Thread.currentThread().setUncaughtExceptionHandler(new UnexpectedException(this));
		super.onCreate(savedInstanceState);
		BillRepository billRepository = ((ElbaApplication) getApplication()).getBillRepository();
		Bill bill = billRepository.getById(getIntent().getIntExtra("documentId", 0));
		String result = renderTemplate(getTemplate(), bill);
		view = new WebView(this);
//		saveToFile(result, "huj.htm");
		view.loadDataWithBaseURL("about:blank", result, "text/html", "UTF-8", null);
		setContentView(view);
	}

	//	public Bitmap getBitmap(final WebView w, int containerWidth, int containerHeight, final String baseURL, final String content) {
//		final CountDownLatch signal = new CountDownLatch(1);
//		final Bitmap b = Bitmap.createBitmap(containerWidth, containerHeight, Bitmap.Config.ARGB_8888);
//		final AtomicBoolean ready = new AtomicBoolean(false);
//		w.post(new Runnable() {
//
//			@Override
//			public void run() {
//				w.setWebViewClient(new WebViewClient() {
//					@Override
//					public void onPageFinished(WebView view, String url) {
//						ready.set(true);
//					}
//				});
//				w.setPictureListener(new WebView.PictureListener() {
//					@Override
//					public void onNewPicture(WebView view, Picture picture) {
//						if (ready.get()) {
//							final Canvas c = new Canvas(b);
//							view.draw(c);
//							w.setPictureListener(null);
//							signal.countDown();
//						}
//					}
//				});
//				w.layout(0, 0, rect.width(), rect.height());
//				w.loadDataWithBaseURL(baseURL, content, "text/html", "UTF-8", null);
//			}});
//		try {
//			signal.await();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		return b;
//	}

	private void saveToFile(String result, String fileName) {
		//check ready
		File directory = new File(Environment.getExternalStorageDirectory(), "/Android/data/skbkontur.elba/files/");
		try {
			if (!directory.mkdirs())
				throw new Error();
			File file = new File(directory, fileName);
			file.createNewFile();
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(result);
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String renderTemplate(String template, final Bill bill) {
		Template compile = Mustache.compiler().defaultValue("huj").compile(template);
		final Organization organization = new OrganizationRepository().Get();
		final PrintData printData = new PrintData() {{
			sumInWords = NumberService.getInstance().sumInWords(bill.sum);
		}};
		return compile.execute(new HashMap<String, Object>() {{
			put("bill", bill);
			put("me", organization);
			put("printData", printData);
		}});
	}

	private class PrintData {
		public String sumInWords;
	}

	private String getTemplate() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.bill)));
		StringBuilder stringBuilder = new StringBuilder();
		String s;
		try {
			while ((s = reader.readLine()) != null) {
				stringBuilder.append(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "сохранить в файл");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		File directory = new File(Environment.getExternalStorageDirectory(), "/Android/data/ru.kontur.elba/files/");
		Bitmap b = Bitmap.createBitmap(1130, 1600, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(b);
		view.draw(canvas);
		try {
			directory.mkdirs();

			File file = new File(directory, "bill.png");
			if (file.exists())
				file.delete();
			file.createNewFile();
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			b.compress(Bitmap.CompressFormat.PNG, 10, fileOutputStream);
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (IOException e) {
		}
		return true;
	}
}
