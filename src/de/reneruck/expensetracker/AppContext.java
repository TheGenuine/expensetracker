package de.reneruck.expensetracker;

import java.util.ArrayList;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import de.reneruck.expensetracker.db.SqliteStorageManager;
import de.reneruck.expensetracker.model.Category;

/**
 * 
 * @author Rene
 *
 */
public class AppContext extends Application {

	private static final String TAG = "ExpensesTracker - AppContext";

	public static final String PREF_USER = "user-preferences";
	public static final String PREF_USER_DESCRIPTIONS = "descriptions";
	public static final String PREF_USER_CATEGORIES = "categories";

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
		return this.databaseManager;
	}

	public void setDatabaseManager(SqliteStorageManager databaseManager) {
		this.databaseManager = databaseManager;
	}


	public ArrayList<Category> getCategories() {
		// TODO Auto-generated method stub
		return new ArrayList<Category>();
	}
}
