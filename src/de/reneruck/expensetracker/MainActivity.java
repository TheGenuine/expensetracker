package de.reneruck.expensetracker;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import de.reneruck.expensetracker.settings.SettingsActivity;

public class MainActivity extends Activity {

    private AppContext appContext;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        this.appContext = (AppContext) getApplicationContext();
        
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, new FragmentAllItems(this.appContext), "FragmentAllItems");
        ft.commit();
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
