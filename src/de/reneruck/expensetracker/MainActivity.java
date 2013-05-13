package de.reneruck.expensetracker;

import java.util.Calendar;
import java.util.List;

import android.R.color;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import de.reneruck.expensetracker.db.ExpenseQueryCallback;
import de.reneruck.expensetracker.menu.FragmentMenuList;
import de.reneruck.expensetracker.model.ExpenseEntry;
import de.reneruck.expensetracker.settings.SettingsActivity;

/**
 * Activity to serve as the entry point into the app.
 * 
 * @author Rene
 *
 */
public class MainActivity extends SlidingFragmentActivity {

    protected static final int TODAY = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	private static final String TAG = "MainActivity";
	private AppContext appContext;
	private int mode = 0;
	private Fragment mFrag;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupSlidingMenu(savedInstanceState);
    }
	
    @Override
    protected void onResume() {
    	super.onResume();
    	Log.d(TAG, "-- onResume --");
    	
    	getActionBar().setDisplayHomeAsUpEnabled(true);
    	
    	this.appContext = (AppContext) getApplicationContext();
    	setFragmentForCurrentMode();
    }
	
    private void setupSlidingMenu(Bundle savedInstanceState) {
		
    	setBehindContentView(R.layout.menu_frame);
		if (savedInstanceState == null) {
			FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
			this.mFrag = new FragmentMenuList();
			t.replace(R.id.menu_frame, this.mFrag);
			t.commit();
		} else {
			this.mFrag = (ListFragment) this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
		}

		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT);
		sm.setBackgroundColor(color.darker_gray);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setBehindScrollScale(0.25f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		sm.setTouchModeBehind(SlidingMenu.TOUCHMODE_MARGIN);
	}



	private void setFragmentForCurrentMode(){
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment byTag = fragmentManager.findFragmentByTag("FragmentOverviewDay");
		
		if(byTag != null) {
			transaction.show(byTag);
		} else {
			
			View container = findViewById(R.id.fragment_container);
			((ViewGroup) container).removeAllViews();
			switch (this.mode) {
	//			case R.id.menu_overview_day:
	//				transaction.add(R.id.fragment_container, new FragmentViewPager(this.appContext), "FragmentOverviewDay");
	//				break;
	//			case R.id.menu_overview_month:
	//				transaction.add(R.id.fragment_container, new FragmentNotImplemented(), "FragmentNotImplemented");
	//				break;
	//			case R.id.menu_overview_all:
	//				transaction.add(R.id.fragment_container, new FragmentAllItems(this.appContext), "FragmentAllItems");
	//				break;
				default:
					transaction.add(R.id.fragment_container, new FragmentViewPager(this.appContext), "FragmentOverviewDay");
					break;
			}
		}
		transaction.commit();
	}
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				break;
			case R.id.menu_add_entry:
				Intent newEntryIntent = new Intent(this, NewEntryActivtiy.class);
				checkAndSetDifferentDay(newEntryIntent);
				startActivity(newEntryIntent);
				break;
//			case R.id.menu_overview_day:
//			case R.id.menu_overview_month:
//			case R.id.menu_overview_all:
//				this.mode = item.getItemId();
//				setFragmentForCurrentMode();
//				break;
//			case R.id.menu_statistics:
//				break;
			case R.id.menu_settings:
				Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
				startActivity(settingsIntent);
				break;
//			case R.id.menu_export:
//				this.appContext.getDatabaseManager().getAllExpensEntries(Ordering.DESC, this.exportCallback);
//				break;
			}
		
		return true;
	}
	
	private void checkAndSetDifferentDay(Intent newEntryIntent) {
		Fragment byTag = getSupportFragmentManager().findFragmentByTag("FragmentOverviewDay");
		if(byTag != null) {
			newEntryIntent.putExtra(Statics.EXTRA_DAY, ((FragmentViewPager) byTag).getCurrentPage());
		}
	}

	ExpenseQueryCallback exportCallback = new ExpenseQueryCallback() {

		public void queryFinished(List<ExpenseEntry> resultSet) {
			ExportAsync async = new ExportAsync(getApplicationContext(), resultSet, "/sdcard");
			async.execute();
		}
		
	};
}
