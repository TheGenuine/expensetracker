package de.reneruck.expensetracker.db;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.reneruck.expensetracker.model.ExpenseEntry;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

public class AsyncStoreEntries extends AsyncTask<QueryInstructions, Void, Void> {

	DatabaseHelper dbHelper;
	

	public AsyncStoreEntries(DatabaseHelper dbHelper) {
		this.dbHelper = dbHelper;
	}


	@Override
	protected Void doInBackground(QueryInstructions... params) {
		if(params != null && params.length > 0){
			QueryInstructions instructions = params[0];
			
			ExpenseEntry entry = instructions.getEntry();
			
			if(entry != null) {
				writeToDatabase(entry);
			}
		}
		return null;
	}


	private void writeToDatabase(ExpenseEntry entry) {
		
		SQLiteDatabase writableDatabase = this.dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues(5);
		values.put(DbConfigs.FIELD_EXPENSES_ID, entry.getId());
		values.put(DbConfigs.FIELD_DATE, getSqlDateString(entry.getDate()));
		values.put(DbConfigs.FIELD_DESCRIPTION, entry.getDescription());
		values.put(DbConfigs.FIELD_VALUE, entry.getValue());
		values.put(DbConfigs.FIELD_CATEGORY, entry.getCategory().ordinal());

		if(entryExists(writableDatabase, entry.getId())){
			writableDatabase.updateWithOnConflict(DbConfigs.TABLE_EXPENSES, values, DbConfigs.FIELD_EXPENSES_ID + "=" + entry.getId(), null,SQLiteDatabase.CONFLICT_ROLLBACK);
		} else {
			writableDatabase.insertWithOnConflict(DbConfigs.TABLE_EXPENSES, null, values, SQLiteDatabase.CONFLICT_ROLLBACK);
		}
	}

	private boolean entryExists(SQLiteDatabase db, long entryId) {
		Cursor c = db.query(DbConfigs.TABLE_EXPENSES, new String[]{"*"}, DbConfigs.FIELD_EXPENSES_ID + " like '" + entryId+ "'", null, null, null,null);
		boolean result = c.getCount() > 0 ? true : false;
		c.close();
		return result;
	}
	
	private String getSqlDateString(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		return dateFormat.format(date);
	}

	
}
