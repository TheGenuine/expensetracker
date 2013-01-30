package de.reneruck.expensetracker.db;

import java.sql.Date;
import java.util.List;

import android.content.Context;
import de.reneruck.expensetracker.model.Category;
import de.reneruck.expensetracker.model.ExpenseEntry;
import de.reneruck.expensetracker.model.Ordering;

/**
 * 
 * @author Rene
 *
 */
public class SqliteStorageManager {

	private DatabaseHelper dbHelper;

	public SqliteStorageManager(Context context) {
		this.dbHelper = new DatabaseHelper(context, DbConfigs.DATABASE_NAME, null, DbConfigs.DATABASE_VERSION);
	}
	
	/**
	 * Persists an {@link ExpenseEntry}.<br>
	 * If an entry with the same ID already exists, it will be updates by this
	 * one.
	 * 
	 * @param expenseEntry
	 *            the {@link ExpenseEntry} to store
	 */
	public void storeOrUpdateExpenseEntry(ExpenseEntry expenseEntry){
		QueryInstructions instructions = new QueryInstructions(-1, expenseEntry, null, null, null, null);
		AsyncStoreOrUpdateEntries storeTask = new AsyncStoreOrUpdateEntries(this.dbHelper, this);
		storeTask.execute(instructions);
	}
	
	
	/**
	 * Deletes the given Entry from persistent storage, if existent.
	 * 
	 * @param expenseEntry
	 *            the {@link ExpenseEntry} to delete
	 */
	public void deleteExpenseEntry(ExpenseEntry expenseEntry){
		deleteExpenseEntry(expenseEntry.getId());
	}
	
	/**
	 * Deletes the {@link ExpenseEntry} with the given id from persistent
	 * storage, if existent.
	 * 
	 * @param expenseEntryId
	 *            the id of the {@link ExpenseEntry} to delete
	 */
	public void deleteExpenseEntry(long expenseEntryId){
		QueryInstructions instructions = new QueryInstructions(expenseEntryId, null, null, null, null, null);
		instructions.setDeleteFlag(true);
		AsyncStoreOrUpdateEntries storeTask = new AsyncStoreOrUpdateEntries(this.dbHelper, this);
		storeTask.execute(instructions);
	}
	
	/**
	 * Returns a list of all currently stored {@link ExpenseEntry}.
	 * 
	 * @param ordering
	 *            the timely ordering of the returned {@link List}, default is
	 *            ascending
	 * @param callback 
	 * 
	 * @return a list of {@link ExpenseEntry} ordered in the given ordering
	 */
	public void getAllExpensEntries(Ordering ordering, DatabaseQueryCallback callback){
		getAllExpensEntriesForRange(null, null, ordering, callback);
	}
	
	/**
	 * Returns a list of all {@link ExpenseEntry} for the given day.
	 * 
	 * @param day
	 *            specific day to retrieve all {@link ExpenseEntry} for
	 * @param ordering
	 *            the timely ordering of the returned {@link List}, default is
	 *            ascending
	 * @return a list of {@link ExpenseEntry} ordered in the given ordering
	 */
	public void getAllExpensEntriesForDay(Date day, Ordering ordering, DatabaseQueryCallback callback){
		getAllExpensEntriesForRange(day, null, ordering, callback);
	}
	
	/**
	 * Returns a list of all {@link ExpenseEntry} for the given period between
	 * the given startDay and the endDay.<br>
	 * The start day should be before the end day, otherwise the two dates will
	 * be switched to get a valid range.
	 * 
	 * @param startDay
	 *            start day of the period.
	 * @param endDay
	 *            end day of the period.
	 * @param ordering
	 *            the timely ordering of the returned {@link List}, default is
	 *            ascending
	 * @return a list of {@link ExpenseEntry} ordered in the given ordering
	 */
	public void getAllExpensEntriesForRange(Date startDay, Date endDay, Ordering ordering, DatabaseQueryCallback callback){
		QueryInstructions instructionSet = new QueryInstructions(-1, null, startDay, endDay, ordering, null);
		
		AsyncRetrieveEntries retriveTask = new AsyncRetrieveEntries(this.dbHelper, callback);
		retriveTask.execute(instructionSet);
	}
	
	
	/**
	 * Returns a list of all {@link ExpenseEntry} for the given {@link Category}.
	 * 
	 * @param category
	 *            specific {@link Category} to retrieve all {@link ExpenseEntry} for.
	 * @param ordering
	 *            the timely ordering of the returned {@link List}, default is
	 *            ascending
	 * @return a list of {@link ExpenseEntry} ordered in the given ordering
	 */
	public void getAllExpensEntriesForCategory(Category category, Ordering ordering, DatabaseQueryCallback callback){
		getAllExpensEntriesForCategory(null, null, category, ordering, callback);
	}
	
	/**
	 * Returns a list of all {@link ExpenseEntry} for the given {@link Category}
	 * at the given day.
	 * 
	 * @param day
	 *            specific day to retrieve all {@link ExpenseEntry} for.
	 * @param category
	 *            specific {@link Category} to retrieve all {@link ExpenseEntry}
	 *            for.
	 * @param ordering
	 *            the timely ordering of the returned {@link List}, default is
	 *            ascending
	 * @return a list of {@link ExpenseEntry} ordered in the given ordering
	 */
	public void getAllExpensEntriesForCategory(Date day, Category category, Ordering ordering, DatabaseQueryCallback callback){
		getAllExpensEntriesForCategory(day, null, category, ordering, callback);
	}
	
	/**
	 * Returns a list of all {@link ExpenseEntry} for the given {@link Category}
	 * within the given range.
	 * 
	 * @param startDay
	 *            start day of the period.
	 * @param endDay
	 *            end day of the period.
	 * @param category
	 *            specific {@link Category} to retrieve all {@link ExpenseEntry}
	 *            for.
	 * @param ordering
	 *            the timely ordering of the returned {@link List}, default is
	 *            ascending
	 * @return a list of {@link ExpenseEntry} ordered in the given ordering
	 */
	public void getAllExpensEntriesForCategory(Date startDay, Date endDay, Category category, Ordering ordering, DatabaseQueryCallback callback){
		QueryInstructions instructionSet = new QueryInstructions(-1, null, startDay, endDay, ordering, category);
		
		AsyncRetrieveEntries retriveTask = new AsyncRetrieveEntries(this.dbHelper, callback);
		retriveTask.execute(instructionSet);
	}
}