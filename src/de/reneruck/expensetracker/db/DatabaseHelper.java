package de.reneruck.expensetracker.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * @author Rene
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String CREATE_EXPENSES_TABLE = "CREATE  TABLE IF NOT EXISTS `" + DbConfigs.TABLE_EXPENSES + "`" +
			" (`" + DbConfigs.FIELD_EXPENSES_ID + "` LONG  PRIMARY KEY, " +
			"`" + DbConfigs.FIELD_DATE + "` DATE NULL, " +
			"`" + DbConfigs.FIELD_DESCRIPTION + "` STRING NULL, " +
			"`" + DbConfigs.FIELD_VALUE + "` DOUBLE NULL, " +
			"`" + DbConfigs.FIELD_CATEGORY + "` INTEGER NULL)";
	
	private static final String CREATE_CATEGORIES_TABLE = "CREATE TABLE IF NOT EXISTS '" + DbConfigs.TABLE_CATEGORIES + "'" +
			" (`" + DbConfigs.FIELD_CATEGORY_ID + "` LONG  PRIMARY KEY, " +
			"`" + DbConfigs.FIELD_CATEGORY_VALUE + "` STRING NULL, " +
			"`" + DbConfigs.FIELD_CATEGORY_COUNT + "` INT NULL)";
	
	private static final String CREATE_DESCRIPTION_TABLE = "CREATE TABLE IF NOT EXISTS '" + DbConfigs.TABLE_DESCRIPTION + "'" +
			" (`" + DbConfigs.FIELD_DESCRIPTION_ID + "` LONG  PRIMARY KEY, " +
			"`" + DbConfigs.FIELD_DESCRIPTION_VALUE + "` STRING NULL, " +
			"`" + DbConfigs.FIELD_DESCRIPTION_COUNT + "` INT NULL)";
	
	public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, DbConfigs.DATABASE_NAME, factory,  DbConfigs.DATABASE_VERSION);
	}

	

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL(CREATE_EXPENSES_TABLE);
			db.execSQL(CREATE_CATEGORIES_TABLE);
			db.execSQL(CREATE_DESCRIPTION_TABLE);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		/*
		 * TODO: Here should happen a data migration!!  
		 */
		db.execSQL("DROP TABLE IF EXISTS " + DbConfigs.TABLE_EXPENSES + "");
		db.execSQL("DROP TABLE IF EXISTS " + DbConfigs.TABLE_CATEGORIES + "");
		db.execSQL("DROP TABLE IF EXISTS " + DbConfigs.TABLE_DESCRIPTION + "");
		onCreate(db);
	}
}
