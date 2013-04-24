package de.reneruck.expensetracker;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import de.reneruck.expensetracker.db.SqliteStorageManager;
import de.reneruck.expensetracker.model.ExpenseEntry;

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

	private ExpenseEntry entryToEdit;
	
	@Override
	public void onCreate() {
		super.onCreate();
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

	public ExpenseEntry getEntryToEdit() {
		return this.entryToEdit;
	}


	public void setEntryToEdit(ExpenseEntry entryToEdit) {
		this.entryToEdit = entryToEdit;
	}
}
