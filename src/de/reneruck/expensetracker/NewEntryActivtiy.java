package de.reneruck.expensetracker;

import java.util.Calendar;

import android.R.color;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import de.reneruck.expensetracker.menu.FragmentMenuList;
import de.reneruck.expensetracker.model.ExpenseEntry;
import de.reneruck.expensetracker.settings.SettingsActivity;

/**
 * This Activity constitutes the container which handles the displaying of the
 * different steps (fragments) when creating a new Entry.<br>
 * 
 * It also takes care of storing the newly created entry to a permanent storage.
 * 
 * @author Rene
 * 
 */
public class NewEntryActivtiy extends SlidingFragmentActivity {

	private static final String TAG = "NewEntryActivity";
	
	private ExpenseEntry currentEntry;
	private AppContext context;
	private SectionsPagerAdapter sectionsPagerAdapter;
	private ViewPager viewPager;
	
	private Fragment fragmentValueInput;

	private FragmentChooseDescription fragmentChooseDescription;

	private FragmentChooseCategory fragmentChooseCategory;

	private Fragment mFrag;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_entry);
		
        this.context = (AppContext) getApplicationContext();
		
        getCurrentEntry();
		
        checkForDifferentDate();
        
		setupFragments();
		setupSlidingMenu(savedInstanceState);
		getActionBar().setHomeButtonEnabled(true);
		
        this.sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        this.viewPager = (ViewPager) findViewById(R.id.pager);
        this.viewPager.setAdapter(this.sectionsPagerAdapter);
	}

	private void getCurrentEntry() {
		this.currentEntry = this.context.getEntryToEdit();
        
        if(this.currentEntry != null) {
        	getActionBar().setTitle("Edit Entry");
        } else {
        	this.currentEntry = new ExpenseEntry();
        }
	}

	private void checkForDifferentDate() {
		int extraDay = getIntent().getExtras().getInt(Statics.EXTRA_DAY);
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        
        if(extraDay > 0 && extraDay != currentDay) {
        	Calendar date = Calendar.getInstance();
        	date.set(Calendar.DAY_OF_MONTH, extraDay);
        	this.currentEntry.setDate(date.getTime());
        }
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
	
	private void setupFragments() {
		this.fragmentValueInput = new FragmentValueInput(this.currentEntry);		
		this.fragmentChooseCategory = new FragmentChooseCategory(this.context, this.currentEntry);		
		this.fragmentChooseDescription = new FragmentChooseDescription(this.context, this.currentEntry);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_new_entry, menu);
		return true;
	};
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
        case android.R.id.home:
        	//TODO: show warning dialog loosing not stored data
        	finish();
        	break;
        case R.id.menu_store_entry:
        	//TODO: make a check for empty inputs
        	collectData();
        	storeEntry();
        	syncToServer();
        	finish();
        	break;
        case R.id.menu_settings:
        	Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
        	startActivity(i);
        	break;
		}
		return true;
    }
    
	private void syncToServer() {
		if (haveNetworkConnection()) {
			new SyncEntryAsync(this.currentEntry).execute();
		}
	}

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI")){
            	if (ni.isConnected()){
            		haveConnectedWifi = true;
            	}
            }
            if (ni.getTypeName().equalsIgnoreCase("MOBILE")){
                if (ni.isConnected()){
                    haveConnectedMobile = true;
                }
            }
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
    
	private void storeEntry() {
    	Toast.makeText(getApplicationContext(), "Storing Entry", Toast.LENGTH_SHORT).show();
    	this.context.getDatabaseManager().storeOrUpdateExpenseEntry(this.currentEntry);
	}

	private void collectData() {
		
	}

	/**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {


		public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int i) {
        	Fragment fragment = null;
        	Bundle args = new Bundle();
        	
        	switch (i) {
				case 0: // value input
					fragment = fragmentValueInput;
					break;
				case 1: // choose category
					fragment = fragmentChooseCategory;
					break;
				case 2: // choose description
					fragment = fragmentChooseDescription;
					break;

			default:
				fragment = new DummySectionFragment();
				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
				fragment.setArguments(args);
				break;
			}
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return getString(R.string.title_new_entry_section1);
                case 1: return getString(R.string.title_new_entry_section2);
                case 2: return getString(R.string.title_new_entry_section3);
            }
            return null;
        }
    }
    
    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    public static class DummySectionFragment extends android.support.v4.app.Fragment {
        public DummySectionFragment() {
        }

        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            TextView textView = new TextView(getActivity());
            textView.setGravity(Gravity.CENTER);
            Bundle args = getArguments();
            textView.setText(Integer.toString(args.getInt(ARG_SECTION_NUMBER)));
            return textView;
        }
    }
}
