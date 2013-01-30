package de.reneruck.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

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
		}
		return true;
	}
}
