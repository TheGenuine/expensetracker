package de.reneruck.expensetracker.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String CREATE_EXPENSES_TABLE = "CREATE  TABLE IF NOT EXISTS `" + DbConfigs.TABLE_EXPENSES+ "`" +
			" (`" + DbConfigs.FIELD_EXPENSES_ID + "` LONG  PRIMARY KEY AUTOINCREMENT ," +
			"`" + DbConfigs.FIELD_DATE + "` DATE NULL ," +
			"`" + DbConfigs.FIELD_DESCRIPTION + "` STRING NULL ," +
			"`" + DbConfigs.FIELD_VALUE + "` DOUBLE NULL" +
			"`" + DbConfigs.FIELD_CATEGORY + "` INTEGER NULL)";

	public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, DbConfigs.databaseName, factory,  DbConfigs.databaseVersion);
	}

	

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL(CREATE_EXPENSES_TABLE);
		} catch (SQLException e) {
			System.err.println(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		/*
		 * TODO: Here should happen a data migration!!  
		 */
		db.execSQL("DROP TABLE IF EXISTS " + DbConfigs.TABLE_EXPENSES + "");
		onCreate(db);
	}
}
