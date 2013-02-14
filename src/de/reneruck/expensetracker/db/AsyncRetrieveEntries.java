package de.reneruck.expensetracker.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import de.reneruck.expensetracker.model.Category;
import de.reneruck.expensetracker.model.Description;
import de.reneruck.expensetracker.model.ExpenseEntry;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

/**
 * 
 * @author Rene
 *
 */
public class AsyncRetrieveEntries extends AsyncTask<QueryInstructions, Void, List<ExpenseEntry>> {

	private static final String TAG = "AsyncRetriveEntries";
	private static final long TWENTY_FOUR_HOURS_IN_MS = 86400000;
	private DatabaseHelper dbHelper;
	private ExpenseQueryCallback callback;
	private List<Category> prefetchedCategories;
	private List<Description> prefetchedDescriptions;
	
	public AsyncRetrieveEntries(DatabaseHelper dbHelper, ExpenseQueryCallback callback) {
		this.dbHelper = dbHelper;
		this.callback = callback;
	}

	@Override
	protected void onPostExecute(List<ExpenseEntry> result) {
		Log.d(TAG, "Database query finished, " + result.size() + " entries retrieved");
		if(this.callback != null) {
			this.callback.queryFinished(result);
		}
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
		StringBuilder statement = new StringBuilder("");
		if(instructions.getEntryId() > -1) {
			statement.append(DbConfigs.FIELD_EXPENSES_ID + "=" + instructions.getEntryId());
		}

		if(instructions.getDay1() != null) {
			if(instructions.getDay2() != null){
				statement.append(DbConfigs.FIELD_DATE + ">" + instructions.getEntryId() 
						+ " AND " + DbConfigs.FIELD_DATE + "<" + instructions.getEntryId());
			} else {
				statement.append(DbConfigs.FIELD_DATE + ">" + instructions.getDay1()
				+ " AND " + DbConfigs.FIELD_DATE + "<" + dayPlus24h(instructions.getDay1()));
			}
		}

		if(instructions.getCategory() != null) {
			statement.append(DbConfigs.FIELD_CATEGORY + "=" + instructions.getCategory());
		}
		
		return statement.toString().length() > 1 ? statement.toString() : null;
	}

	private String dayPlus24h(Date day1) {
		return sqlDateToString(new Date(day1.getTime() + TWENTY_FOUR_HOURS_IN_MS));
	}

	private List<ExpenseEntry> queryDatabase(String selection) {
		SQLiteDatabase readableDatabase = this.dbHelper.getReadableDatabase();
		
		Cursor query = readableDatabase.query(DbConfigs.TABLE_EXPENSES, new String[]{"*"}, selection, null, null, null, DbConfigs.FIELD_DATE);
		
		List<ExpenseEntry> result = new LinkedList<ExpenseEntry>();
		if(query.getCount() > 0){
			while(query.moveToNext()){
				result.add(new ExpenseEntry(stringToSqlDate(query.getString(1)), getDescriptionForId(query.getInt(2), readableDatabase), query.getDouble(3), getCategoryForId(query.getInt(4), readableDatabase)));
			}
		}
		query.close();
		readableDatabase.close();
		return result;
	}

	private Description getDescriptionForId(int descriptionId, SQLiteDatabase readableDatabase) {

		if(this.prefetchedDescriptions == null) {
			this.prefetchedDescriptions = getAllDescriptions(readableDatabase);
		}
		
		for (Description description : this.prefetchedDescriptions) {
			if(description.getId() == descriptionId) {
				return description;
			}
		}
		return null;
	}

	private List<Description> getAllDescriptions(SQLiteDatabase readableDatabase) {
		List<Description> result = new ArrayList<Description>();

		Cursor query = readableDatabase.query(DbConfigs.TABLE_DESCRIPTION, new String[]{"*"}, null, null, null, null, DbConfigs.FIELD_DESCRIPTION_COUNT);
		
		if(query.getCount() > 0){
			while(query.moveToNext()){
				result.add(new Description(query.getInt(0), query.getString(1), query.getInt(2)));
			}
		}
		return result;
	}

	private Category getCategoryForId(int id, SQLiteDatabase readableDatabase) {
		
		if(this.prefetchedCategories == null) {
			this.prefetchedCategories = getAllCategories(readableDatabase);
		}
		
		for (Category categorie : this.prefetchedCategories) {
			if(categorie.getId() == id) {
				return categorie;
			}
		}
		return null;
	}

	private List<Category> getAllCategories(SQLiteDatabase readableDatabase) {
		Cursor query = readableDatabase.query(DbConfigs.TABLE_CATEGORIES, new String[]{"*"}, null, null, null, null, null);
		
		List<Category> resultCategories = new ArrayList<Category>();
		if(query.getCount() > 0){
			while(query.moveToNext()){
				resultCategories.add(new Category(query.getLong(0), query.getString(1), query.getInt(2)));
			}
		}
		return resultCategories;
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
	
	private String sqlDateToString(Date sqlDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		return dateFormat.format(sqlDate);
	}
}
