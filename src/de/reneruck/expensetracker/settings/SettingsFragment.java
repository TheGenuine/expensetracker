package de.reneruck.expensetracker.settings;

import de.reneruck.expensetracker.R;
import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * 
 * @author Rene
 *
 */
public class SettingsFragment extends PreferenceFragment {
   
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}
