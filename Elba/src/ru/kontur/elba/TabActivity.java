package ru.kontur.elba;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

public class TabActivity extends Activity implements TabHost.TabContentFactory {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.

		setContentView(R.layout.tab);
		TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
		// инициализация
		tabHost.setup();
		TabHost.TabSpec tabSpec;
		View v = getLayoutInflater().inflate(R.layout.tab_header, null);

		// создаем вкладку и указываем тег
		tabSpec = tabHost.newTabSpec("tag1");
		// название вкладки
		tabSpec.setIndicator("Вкладка 1");

		// указываем id компонента из FrameLayout, он и станет содержимым
		tabSpec.setContent(this);
		// добавляем в корневой элемент
		tabHost.addTab(tabSpec);
		tabHost.setCurrentTabByTag("tag1");
		tabSpec = tabHost.newTabSpec("tag2");
		// указываем название и картинку
		// в нашем случае вместо картинки идет xml-файл,
		// который определяет картинку по состоянию вкладки
		tabSpec.setIndicator("Вкладка 2"); //, getResources().getDrawable(R.drawable.tab_icon_selector));
		tabSpec.setContent(this);
		tabHost.addTab(tabSpec);
		tabSpec = tabHost.newTabSpec("tag3");
		// создаем View из layout-файла
		// и устанавливаем его, как заголовок
		tabSpec.setIndicator(v);
		tabSpec.setContent(this);
		tabHost.addTab(tabSpec);

		// вторая вкладка будет выбрана по умолчанию
		tabHost.setCurrentTabByTag("tag2");

		// обработчик переключения вкладок
	}

	@Override
	public View createTabContent(String s) {
		Toast.makeText(getBaseContext(), "tabId = " + s, Toast.LENGTH_SHORT).show();
		return findViewById(R.id.tvTab4);  //To change body of implemented methods use File | Settings | File Templates.
	}
}
