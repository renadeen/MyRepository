package ru.kontur.elba;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

public class TabActivity extends Activity implements TabHost.OnTabChangeListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.

		setContentView(R.layout.tab);
		TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
		// инициализация
		tabHost.setOnTabChangedListener(this);
		tabHost.setup();
		TabHost.TabSpec tabSpec;
		View v = getLayoutInflater().inflate(R.layout.tab_header, null);
		View v2 = getLayoutInflater().inflate(R.layout.tab_header, null);
		View v3 = getLayoutInflater().inflate(R.layout.tab_header2, null);

		// создаем вкладку и указываем тег
		tabSpec = tabHost.newTabSpec("tag1");
		// название вкладки
		tabSpec.setIndicator(v);

		// указываем id компонента из FrameLayout, он и станет содержимым
		tabSpec.setContent(R.id.tabContent);
		// добавляем в корневой элемент
		tabHost.addTab(tabSpec);
		tabHost.setCurrentTabByTag("tag1");
		tabSpec = tabHost.newTabSpec("tag2");
		// указываем название и картинку
		// в нашем случае вместо картинки идет xml-файл,
		// который определяет картинку по состоянию вкладки
		tabSpec.setIndicator(v2); //, getResources().getDrawable(R.drawable.tab_icon_selector));
		tabSpec.setContent(R.id.tabContent);
		tabHost.addTab(tabSpec);

		tabSpec = tabHost.newTabSpec("tag3");
		// создаем View из layout-файла
		// и устанавливаем его, как заголовок
//	tabSpec.setIndicator(v);
		tabSpec.setIndicator(v3);
		tabSpec.setContent(R.id.tabContent);
		tabHost.addTab(tabSpec);

		// вторая вкладка будет выбрана по умолчанию
		tabHost.setCurrentTabByTag("tag2");

		// обработчик переключения вкладок
	}

	@Override
	public void onTabChanged(String s) {
		Toast.makeText(getBaseContext(), "tabId = " + s, Toast.LENGTH_SHORT).show();
	}
}
