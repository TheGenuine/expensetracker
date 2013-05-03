package de.reneruck.expensetracker;

import java.util.Calendar;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import de.reneruck.expensetracker.db.ExpenseQueryCallback;
import de.reneruck.expensetracker.model.ExpenseEntry;
import de.reneruck.expensetracker.model.Ordering;
import de.reneruck.expensetracker.settings.SettingsActivity;

/**
 * Activity to serve as the entry point into the app.
 * 
 * @author Rene
 *
 */
public class MainActivity extends SherlockFragmentActivity {

    protected static final int TODAY = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	private static final String TAG = "MainActivity";
	private AppContext appContext;
	private int mode = R.id.menu_overview_day;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        this.appContext = (AppContext) getApplicationContext();
    }

    @Override
    protected void onResume() {
    	super.onResume();
    	Log.d(TAG, "-- onResume --");
    	setFragmentForCurrentMode();
    }

	private void setFragmentForCurrentMode(){
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		View container = findViewById(R.id.fragment_container);
		((ViewGroup) container).removeAllViews();
		switch (this.mode) {
			case R.id.menu_overview_day:
				transaction.add(R.id.fragment_container, new FragmentViewPager(this.appContext), "FragmentOverviewDay");
				break;
			case R.id.menu_overview_month:
				transaction.add(R.id.fragment_container, new FragmentNotImplemented(), "FragmentNotImplemented");
				break;
			case R.id.menu_overview_all:
				transaction.add(R.id.fragment_container, new FragmentAllItems(this.appContext), "FragmentAllItems");
				break;
			default:
				transaction.add(R.id.fragment_container, new FragmentViewPager(this.appContext), "FragmentOverviewDay");
				break;
		}
		transaction.commit();
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
			case R.id.menu_overview_day:
			case R.id.menu_overview_month:
			case R.id.menu_overview_all:
				this.mode = item.getItemId();
				setFragmentForCurrentMode();
				break;
			case R.id.menu_statistics:
				break;
			case R.id.menu_settings:
				Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
				startActivity(settingsIntent);
				break;
			case R.id.menu_export:
				this.appContext.getDatabaseManager().getAllExpensEntries(Ordering.DESC, this.exportCallback);
				break;
			}
		
		return true;
	}
	
	ExpenseQueryCallback exportCallback = new ExpenseQueryCallback() {

		public void queryFinished(List<ExpenseEntry> resultSet) {
			ExportAsync async = new ExportAsync(getApplicationContext(), resultSet, "/sdcard");
			async.execute();
		}
		
	};
}
