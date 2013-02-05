package de.reneruck.expensetracker.db;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.reneruck.expensetracker.model.Category;
import de.reneruck.expensetracker.model.ExpenseEntry;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

/**
 * 
 * @author Rene
 *
 */
public class AsyncStoreOrUpdateEntries extends AsyncTask<QueryInstructions, Void, Void> {

	private static final String TAG = "AsyncStoreOrUpdateEntries";
	DatabaseHelper dbHelper;
	private SqliteStorageManager callback;
	

	public AsyncStoreOrUpdateEntries(DatabaseHelper dbHelper, SqliteStorageManager managerCallback) {
		this.dbHelper = dbHelper;
		this.callback = managerCallback;
	}

	@Override
	protected Void doInBackground(QueryInstructions... params) {
		if(params != null && params.length > 0){
			QueryInstructions instructions = params[0];
			
			if(instructions.isDelete()) {
				deleteEntry(instructions.getEntryId());
			} else {
				ExpenseEntry entry = instructions.getEntry();
				
				if(entry != null) {
					checkCategory(entry);
					writeToDatabase(entry);
				} else {
					Log.e(TAG, "Entry was null, cannot procede");
				}
			}
			
		}
		return null;
	}

	private void checkCategory(ExpenseEntry entry) {
		if(entry.getCategory().getId() == -1) {
			long id = storeNewCategory(entry.getCategory());
			entry.getCategory().setId(id);
		}
	}

	private long storeNewCategory(Category category) {
		SQLiteDatabase writableDatabase = this.dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues(2);
		values.put(DbConfigs.FIELD_CATEGORY_VALUE, category.getValue());
		values.put(DbConfigs.FIELD_CATEGORY_COUNT, 1);
		long insert = writableDatabase.insert(DbConfigs.TABLE_CATEGORIES, null, values);
		writableDatabase.close();
		return insert;
	}

	private void deleteEntry(long entryId) {
		SQLiteDatabase writableDatabase = this.dbHelper.getWritableDatabase();
		if(entryId > -1 && entryExists(writableDatabase, entryId)) {
			writableDatabase.delete(DbConfigs.TABLE_EXPENSES, DbConfigs.FIELD_EXPENSES_ID + "=" + entryId, null);
		}
	}

	private void writeToDatabase(ExpenseEntry entry) {
		
		SQLiteDatabase writableDatabase = this.dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues(5);
		values.put(DbConfigs.FIELD_EXPENSES_ID, entry.getId());
		values.put(DbConfigs.FIELD_DATE, getSqlDateString(entry.getDate()));
		values.put(DbConfigs.FIELD_DESCRIPTION, entry.getDescription());
		values.put(DbConfigs.FIELD_VALUE, entry.getValue());
		values.put(DbConfigs.FIELD_CATEGORY, entry.getCategory().getId());

		if(entryExists(writableDatabase, entry.getId())){
			writableDatabase.updateWithOnConflict(DbConfigs.TABLE_EXPENSES, values, DbConfigs.FIELD_EXPENSES_ID + "=" + entry.getId(), null, SQLiteDatabase.CONFLICT_ROLLBACK);
		} else {
			writableDatabase.insertWithOnConflict(DbConfigs.TABLE_EXPENSES, null, values, SQLiteDatabase.CONFLICT_ROLLBACK);
		}
		writableDatabase.close();
	}

	private boolean entryExists(SQLiteDatabase db, long entryId) {
		Cursor c = db.query(DbConfigs.TABLE_EXPENSES, new String[]{"*"}, DbConfigs.FIELD_EXPENSES_ID + " like '" + entryId + "'", null, null, null, null);
		boolean result = c.getCount() > 0 ? true : false;
		c.close();
		return result;
	}
	
	private String getSqlDateString(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		return dateFormat.format(date);
	}

	
}
