package de.reneruck.expensetracker;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import de.reneruck.expensetracker.db.ExpenseQueryCallback;
import de.reneruck.expensetracker.model.ExpenseEntry;
import de.reneruck.expensetracker.model.Ordering;
import de.reneruck.expensetracker.settings.SettingsActivity;

/**
 * 
 * @author Rene
 *
 */
public class MainActivity extends SherlockFragmentActivity {

    private AppContext appContext;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        this.appContext = (AppContext) getApplicationContext();
        
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, new FragmentAllItems(this.appContext), "FragmentAllItems");
        ft.commit();

        
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			break;
		case R.id.menu_add_entry:
			Intent newEntryIntent = new Intent(this, NewEntryActivtiy.class);
			startActivity(newEntryIntent);
			break;
		case R.id.menu_settings:
			Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
			startActivity(settingsIntent);
			break;
		case R.id.menu_export:
			this.appContext.getDatabaseManager().getAllExpensEntries(Ordering.DESC, callback);
			break;
		}
		
		return true;
	}
	
	ExpenseQueryCallback callback = new ExpenseQueryCallback() {

		public void queryFinished(List<ExpenseEntry> resultSet) {
			ExportAsync async = new ExportAsync(getApplicationContext(), resultSet, Environment.getExternalStorageDirectory().getAbsolutePath());
			async.execute();
		}
		
	};
}
