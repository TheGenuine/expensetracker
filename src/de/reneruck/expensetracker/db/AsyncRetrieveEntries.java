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
	private static final long TWENTY_FOUR_HOURS_IN_MS = 86400000;
	private DatabaseHelper dbHelper;
	private DatabaseQueryCallback callback;
	
	public AsyncRetrieveEntries(DatabaseHelper dbHelper, DatabaseQueryCallback callback) {
		this.dbHelper = dbHelper;
		this.callback = callback;
	}

	@Override
	protected void onPostExecute(List<ExpenseEntry> result) {
		this.callback.queryFinished(result);
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
		StringBuilder statement = new StringBuilder("SELECT * FROM " + DbConfigs.TABLE_EXPENSES + " ");
		if(instructions.getEntryId() > -1) {
			statement.append("WHERE " + DbConfigs.FIELD_EXPENSES_ID + "=" + instructions.getEntryId());
		}

		if(instructions.getDay1() != null) {
			if(instructions.getDay2() != null){
				statement.append("WHERE " + DbConfigs.FIELD_DATE+ ">" + instructions.getEntryId() 
						+ " AND " + DbConfigs.FIELD_DATE+ "<" + instructions.getEntryId());
			} else {
				statement.append("WHERE " + DbConfigs.FIELD_DATE+ ">" + instructions.getDay1()
				+ " AND " + DbConfigs.FIELD_DATE+ "<" + dayPlus24h(instructions.getDay1()));
			}
		}

		if(instructions.getCategory() != null) {
			statement.append("WHERE " + DbConfigs.FIELD_CATEGORY + "=" + instructions.getCategory());
		}
		
		statement.append(" ORDER BY" + DbConfigs.FIELD_DATE);
		
		if(instructions.getOrdering() != null) {
			statement.append(" " + instructions.getOrdering().toString());
		} else {
			statement.append(" DESC");
		}
		
		return fixWhereEntries(statement.toString());
	}

	/**
	 * Replaces all WHERE entries after the first with AND to make the query
	 * valid
	 * 
	 * @param statement
	 *            probably invalid sql statement
	 * @return
	 */
	private String fixWhereEntries(String statement) {
		int firstWhere = statement.indexOf("WHERE");
		String substring = statement.substring(firstWhere).replace("WHERE", "AND");
		return statement.substring(0, firstWhere) + substring;
	}

	private String dayPlus24h(Date day1) {
		return SqlDateToString(new Date(day1.getTime() + TWENTY_FOUR_HOURS_IN_MS));
	}

	private List<ExpenseEntry> queryDatabase(String selection) {
		SQLiteDatabase readableDatabase = this.dbHelper.getReadableDatabase();
		
		Cursor query = readableDatabase.query(DbConfigs.TABLE_EXPENSES, new String[]{"*"}, selection, null, null, null, DbConfigs.FIELD_DATE);
		
		List<ExpenseEntry> result = new LinkedList<ExpenseEntry>();
		if(query.getCount() > 0){
			for(query.moveToFirst(); query.isLast(); query.moveToNext()){
				result.add(new ExpenseEntry(stringToSqlDate(query.getString(1)), query.getString(2), query.getDouble(3), Category.values()[query.getInt(4)]));
			}
		}
		return result;
	}

	private Date stringToSqlDate(String sqlDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		try {
			return dateFormat.parse(sqlDate);
		} catch (ParseException e) {
			Log.e(TAG, e.getMessage());
			return new Date();
		}
	}
	
	private String SqlDateToString(Date sqlDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		return dateFormat.format(sqlDate);
	}
}
