package de.reneruck.expensetracker.db;

import java.util.List;

import de.reneruck.expensetracker.model.Description;

/**
 * 
 * @author Rene
 *
 */
public interface DescriptionQueryCallback extends QueryCallback {
	public void queryFinished(List<Description> resultSet);
}
