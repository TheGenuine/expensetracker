package de.reneruck.expensetracker.db;

/**
 * 
 * @author Rene
 *
 */
public class DbConfigs {

	public static String DATABASE_NAME = "Expenses.db";

	public static int DATABASE_VERSION = 1;
	
	public static final String TABLE_EXPENSES = "Expenses";
	public static final String FIELD_EXPENSES_ID = "id";
	public static final String FIELD_DATE = "date";
	public static final String FIELD_DESCRIPTION = "description";
	public static final String FIELD_VALUE = "value";
	public static final String FIELD_CATEGORY = "category";

	public static final String TABLE_CATEGORIES = "Categories";
	public static final String FIELD_CATEGORY_ID = "id";
	public static final String FIELD_CATEGORY_VALUE = "value";
	public static final String FIELD_CATEGORY_COUNT = "count";
	
	
	public static final String TABLE_DESCRIPTION = "Description";
	public static final String FIELD_DESCRIPTION_ID = "id";
	public static final String FIELD_DESCRIPTION_VALUE = "value";
	public static final String FIELD_DESCRIPTION_COUNT = "count";
}
