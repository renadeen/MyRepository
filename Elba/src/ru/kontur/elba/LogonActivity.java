package ru.kontur.elba;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import ru.kontur.elba.datalayer.ElbaDatabase;

public class LogonActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
	}

    public void login(View view){
    	startActivity(new Intent(this, DocumentListActivity.class));
    }

    public void open(View view){
    	startActivity(new Intent(this, TabActivity.class));
    }

    public void recreateDatabase(View view){
    	new ElbaDatabase(this).recreate();
    }
}