package de.reneruck.expensetracker;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import de.reneruck.expensetracker.db.SqliteStorageManager;

public class AppContext extends Application {

	private static final String TAG = "ExpensesTracker - AppContext";

	private SqliteStorageManager databaseManager;

	@Override
	public void onCreate() {
		super.onCreate();
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		this.databaseManager = new SqliteStorageManager(this);

		readSettings();
	}
	
	
	private void readSettings() {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		
	}


	public SqliteStorageManager getDatabaseManager() {
		return databaseManager;
	}

	public void setDatabaseManager(SqliteStorageManager databaseManager) {
		this.databaseManager = databaseManager;
	}
}
