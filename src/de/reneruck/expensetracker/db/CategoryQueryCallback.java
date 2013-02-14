package de.reneruck.expensetracker.db;

import java.util.List;

import de.reneruck.expensetracker.model.Category;

/**
 * 
 * @author Rene
 *
 */
public interface CategoryQueryCallback extends QueryCallback{
	public void queryFinished(List<Category> resultSet);
}
