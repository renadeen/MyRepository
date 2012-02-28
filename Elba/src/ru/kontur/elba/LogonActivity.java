package ru.kontur.elba;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.skbkontur.elba.R;
import ru.kontur.elba.datalayer.ElbaDatabase;

public class LogonActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		Thread.currentThread().setUncaughtExceptionHandler(new UnexpectedException(this));
	}

    public void login(View view){
    	startActivity(new Intent(this, DocumentListActivity.class));
    }

    public void recreateDatabase(View view){
    	new ElbaDatabase(this).recreate();
    }
}