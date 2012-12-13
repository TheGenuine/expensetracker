package de.reneruck.expensetracker.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import de.reneruck.expensetracker.model.Category;
import de.reneruck.expensetracker.model.ExpenseEntry;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncRetrieveEntries extends AsyncTask<QueryInstructions, Void, List<ExpenseEntry>> {

	private static final String TAG = null;
	private DatabaseHelper dbHelper;
	
	public AsyncRetrieveEntries(DatabaseHelper dbHelper) {
		this.dbHelper = dbHelper;
	}

	@Override
	protected List<ExpenseEntry> doInBackground(QueryInstructions... params) {
		if(params != null && params.length > 0){
			QueryInstructions instructions = params[0];
			return queryDatabase(createSelectionStatement(instructions));
		}
		return new LinkedList<ExpenseEntry>();
	}

	private String createSelectionStatement(QueryInstructions instructions) {
		return "";
	}

	private List<ExpenseEntry> queryDatabase(String selection) {
		SQLiteDatabase readableDatabase = this.dbHelper.getReadableDatabase();
		
		Cursor query = readableDatabase.query(DbConfigs.TABLE_EXPENSES, new String[]{"*"}, selection, null, null, null, DbConfigs.FIELD_DATE);
		
		List<ExpenseEntry> result = new LinkedList<ExpenseEntry>();
		if(query.getCount() > 0){
			for(query.moveToFirst(); query.isLast(); query.moveToNext()){
				result.add(new ExpenseEntry(getDate(query.getString(1)), query.getString(2), query.getDouble(3), Category.values()[query.getInt(4)]));
			}
		}
		return result;
	}

	private Date getDate(String sqlDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		try {
			return dateFormat.parse(sqlDate);
		} catch (ParseException e) {
			Log.e(TAG, e.getMessage());
			return new Date();
		}
	}
}
