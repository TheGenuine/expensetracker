package de.reneruck.expensetracker;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import de.reneruck.expensetracker.model.ExpenseEntry;
import de.reneruck.expensetracker.settings.SettingsActivity;

public class NewEntryActivtiy extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_new_entry);
		
		getActionBar().setHomeButtonEnabled(true);
		setDateTextAndListener();
		setCategories();
	}

	private void setCategories() {
		Spinner categories = (Spinner) findViewById(R.id.new_entry_category);
		
		SpinnerAdapter categoryEntriesAdapter = new ArrayAdapter<String>(getApplicationContext(), R.array.categories);
		categories.setAdapter(categoryEntriesAdapter);
	}

	private void setDateTextAndListener() {
		TextView date = (TextView) findViewById(R.id.text_date);
		SimpleDateFormat formatter = new SimpleDateFormat("E, dd.MM.yyyy");
		date.setText(formatter.format(new Date(System.currentTimeMillis())));
		date.setOnClickListener(this.onDateClickListener);
	}
	private OnClickListener onDateClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Toast.makeText(getApplicationContext(), "Show Date chooser", Toast.LENGTH_SHORT).show();
		}
	};
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_new_entry, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
        case android.R.id.home:
        	// show warning dialog
        	finish();
        	break;
        case R.id.menu_store_entry:
        	Toast.makeText(getApplicationContext(), "Storing Entry", Toast.LENGTH_SHORT).show();
        	ExpenseEntry entry = collectInputData();
        	break;
        case R.id.menu_settings:
        	Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
        	startActivity(i);
        	break;
		}
		return true;
    }

	private ExpenseEntry collectInputData() {
		
		return null;
	}
}
