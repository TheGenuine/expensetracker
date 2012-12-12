package de.reneruck.expensetracker.db;

import java.sql.Date;
import java.util.List;

import de.reneruck.expensetracker.model.Category;
import de.reneruck.expensetracker.model.ExpenseEntry;
import de.reneruck.expensetracker.model.Ordering;

public class SqliteStorageManagerImpl implements StorageManager {

	@Override
	public void storeOrUpdateExpenseEntry(ExpenseEntry expenseEntry) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteExpenseEntry(ExpenseEntry expenseEntry) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteExpenseEntry(long expenseEntryId) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<ExpenseEntry> getAllExpensEntries(Ordering ordering) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ExpenseEntry> getAllExpensEntriesForDay(Date day, Ordering ordering) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ExpenseEntry> getAllExpensEntriesForRange(Date startDay, Date endDay, Ordering ordering) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ExpenseEntry> getAllExpensEntriesForCategory(Category category, Ordering ordering) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ExpenseEntry> getAllExpensEntriesForCategory(Date day, Category category, Ordering ordering) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ExpenseEntry> getAllExpensEntriesForCategory(Date startDay, Date endDay, Category category, Ordering ordering) {
		// TODO Auto-generated method stub
		return null;
	}

}
