package de.reneruck.expensetracker.db;

import java.util.List;

import de.reneruck.expensetracker.model.ExpenseEntry;

/**
 * 
 * @author Rene
 *
 */
public interface DatabaseQueryCallback {
	public void queryFinished(List<ExpenseEntry> resultSet);
}
