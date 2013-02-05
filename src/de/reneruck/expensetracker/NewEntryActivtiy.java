package de.reneruck.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

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
public class NewEntryActivtiy extends SherlockFragmentActivity {

	private static final String TAG = "NewEntryActivity";
	
	private ExpenseEntry currentEntry;
	private AppContext context;
	private SectionsPagerAdapter sectionsPagerAdapter;
	private ViewPager viewPager;
	
	private Fragment fragmentValueInput;

	private FragmentChooseDescription fragmentChooseDescription;

	private FragmentChooseCategory fragmentChooseCategory;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_entry);
		
        this.context = (AppContext) getApplicationContext();
		
        this.currentEntry = this.context.getEntryToEdit();
        
        if(this.currentEntry != null) {
        	getSupportActionBar().setTitle("Edit Entry");
        } else {
        	this.currentEntry = new ExpenseEntry();
        }
        
		getSupportActionBar().setHomeButtonEnabled(true);
		
		setupFragments();
		
        this.sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        this.viewPager = (ViewPager) findViewById(R.id.pager);
        this.viewPager.setAdapter(this.sectionsPagerAdapter);
	}

	private void setupFragments() {
		this.fragmentValueInput = new FragmentValueInput(this.currentEntry);		
		this.fragmentChooseCategory = new FragmentChooseCategory(this.context, this.currentEntry);		
		this.fragmentChooseDescription = new FragmentChooseDescription(this.context, this.currentEntry);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_new_entry, menu);
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
        	finish();
        	break;
        case R.id.menu_settings:
        	Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
        	startActivity(i);
        	break;
		}
		return true;
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
        public Fragment getItem(int i) {
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
    public static class DummySectionFragment extends Fragment {
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
