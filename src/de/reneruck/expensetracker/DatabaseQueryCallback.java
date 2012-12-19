package de.reneruck.expensetracker;

import java.util.List;

import de.reneruck.expensetracker.model.ExpenseEntry;

public interface DatabaseQueryCallback {
	public void queryFinished(List<ExpenseEntry> resultSet);
}
