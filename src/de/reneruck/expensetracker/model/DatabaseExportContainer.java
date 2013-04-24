package de.reneruck.expensetracker.model;

import java.util.List;

/**
 * Holds a set of {@link ExpenseEntry} to be exported from the database.
 * 
 * @author Rene
 * 
 */
public class DatabaseExportContainer {

	private long firstID;
	private long lastID;
	private List<ExpenseEntry> entries;
	
	public DatabaseExportContainer(List<ExpenseEntry> entries) {
		this.entries = entries;
		if(this.entries.size() >= 1) {
			this.firstID = this.entries.get(0).getId();
			this.lastID = this.entries.get(this.entries.size()).getId();
		}
	}

	public long getFirstID() {
		return this.firstID;
	}

	public void setFirstID(long firstID) {
		this.firstID = firstID;
	}

	public long getLastID() {
		return this.lastID;
	}

	public void setLastID(long lastID) {
		this.lastID = lastID;
	}

	public List<ExpenseEntry> getEntries() {
		return this.entries;
	}

	public void setEntries(List<ExpenseEntry> entries) {
		this.entries = entries;
	}
}
