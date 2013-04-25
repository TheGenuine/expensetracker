package de.reneruck.expensetracker.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import de.reneruck.expensetracker.model.Category;
import de.reneruck.expensetracker.model.Description;

/**
 * {@link AsyncTask} to get various stuff out of the database.
 * 
 * @author Rene
 *
 */
public class AsyncRetriever extends AsyncTask<Void, Void, List<?>> {

	private QueryCallback callback;
	private DatabaseHelper dbHelper;

	public AsyncRetriever(DatabaseHelper dbHelper, QueryCallback callback) {
		this.dbHelper = dbHelper;
		this.callback = callback;
	}

	@Override
	protected void onPostExecute(List<?> result) {
		if(this.callback instanceof DescriptionQueryCallback) {
			((DescriptionQueryCallback) this.callback).queryFinished((List<Description>) result);
		} else if(this.callback instanceof CategoryQueryCallback) {
			((CategoryQueryCallback) this.callback).queryFinished((List<Category>) result);
		}
	}
	
	@Override
	protected List<?> doInBackground(Void... params) {

		if(this.callback instanceof DescriptionQueryCallback) {
			return getAllDescriptions(this.dbHelper.getReadableDatabase());
		} else if(this.callback instanceof CategoryQueryCallback) {
			return getAllCategories(this.dbHelper.getReadableDatabase());
		} 
		return new ArrayList<Object>();
	}

	private List<Category> getAllCategories(SQLiteDatabase readableDatabase) {
		List<Category> resultCategories = new ArrayList<Category>();

		Cursor query = readableDatabase.query(DbConfigs.TABLE_CATEGORIES, new String[]{"*"}, null, null, null, null, DbConfigs.FIELD_CATEGORY_COUNT);
		
		if(query.getCount() > 0){
			while(query.moveToNext()){
				resultCategories.add(new Category(query.getLong(0), query.getString(1), query.getInt(2)));
			}
		}
		return resultCategories;
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
}
