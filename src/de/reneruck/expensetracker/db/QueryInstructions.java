package de.reneruck.expensetracker.db;

import java.sql.Date;

import de.reneruck.expensetracker.model.Category;
import de.reneruck.expensetracker.model.ExpenseEntry;
import de.reneruck.expensetracker.model.Ordering;

public class QueryInstructions {
	
	long entryId;
	private ExpenseEntry entry;
	Date day1;
	Date day2;
	Ordering ordering;
	Category category;
	
	public QueryInstructions(long entryId, ExpenseEntry entry, Date day1, Date day2, Ordering ordering, Category category) {
		super();
		this.entryId = entryId;
		this.entry = entry;
		this.day1 = day1;
		this.day2 = day2;
		this.ordering = ordering;
		this.category = category;
	}
	
	public long getEntryId() {
		return entryId;
	}
	public void setEntryId(long entryId) {
		this.entryId = entryId;
	}
	public Date getDay1() {
		return day1;
	}
	public void setDay1(Date day1) {
		this.day1 = day1;
	}
	public Date getDay2() {
		return day2;
	}
	public void setDay2(Date day2) {
		this.day2 = day2;
	}
	public Ordering getOrdering() {
		return ordering;
	}
	public void setOrdering(Ordering ordering) {
		this.ordering = ordering;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}

	public ExpenseEntry getEntry() {
		return entry;
	}

	public void setEntry(ExpenseEntry entry) {
		this.entry = entry;
	}
}
