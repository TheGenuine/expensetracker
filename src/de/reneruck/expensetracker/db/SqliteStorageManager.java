package de.reneruck.expensetracker.db;

import java.sql.Date;
import java.util.List;

import de.reneruck.expensetracker.model.Category;
import de.reneruck.expensetracker.model.ExpenseEntry;
import de.reneruck.expensetracker.model.Ordering;

public class SqliteStorageManager {

	/**
	 * Persists an {@link ExpenseEntry}.<br>
	 * If an entry with the same ID already exists, it will be updates by this
	 * one.
	 * 
	 * @param expenseEntry
	 *            the {@link ExpenseEntry} to store
	 */
	public void storeOrUpdateExpenseEntry(ExpenseEntry expenseEntry){
		
	}
	
	
	/**
	 * Deletes the given Entry from persistent storage, if existent.
	 * 
	 * @param expenseEntry
	 *            the {@link ExpenseEntry} to delete
	 */
	public void deleteExpenseEntry(ExpenseEntry expenseEntry){
		
	}
	
	/**
	 * Deletes the {@link ExpenseEntry} with the given id from persistent
	 * storage, if existent.
	 * 
	 * @param expenseEntryId
	 *            the id of the {@link ExpenseEntry} to delete
	 */
	public void deleteExpenseEntry(long expenseEntryId){
		
	}
	
	/**
	 * Returns a list of all currently stored {@link ExpenseEntry}.
	 * 
	 * @param ordering
	 *            the timely ordering of the returned {@link List}, default is
	 *            ascending
	 * 
	 * @return a list of {@link ExpenseEntry} ordered in the given ordering
	 */
	public List<ExpenseEntry> getAllExpensEntries(Ordering ordering){
		return null;
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
	public List<ExpenseEntry> getAllExpensEntriesForDay(Date day, Ordering ordering){
		return null;
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
	public List<ExpenseEntry> getAllExpensEntriesForRange(Date startDay, Date endDay, Ordering ordering){
		return null;
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
	public List<ExpenseEntry> getAllExpensEntriesForCategory(Category category, Ordering ordering){
		return null;
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
	public List<ExpenseEntry> getAllExpensEntriesForCategory(Date day, Category category, Ordering ordering){
		return null;
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
	public List<ExpenseEntry> getAllExpensEntriesForCategory(Date startDay, Date endDay, Category category, Ordering ordering){
		return null;
	}
}