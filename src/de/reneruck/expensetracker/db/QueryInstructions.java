package de.reneruck.expensetracker.db;

import java.sql.Date;

import de.reneruck.expensetracker.model.Category;
import de.reneruck.expensetracker.model.ExpenseEntry;
import de.reneruck.expensetracker.model.Ordering;

/**
 * 
 * @author Rene
 *
 */
public class QueryInstructions {
	
	private long entryId = -1;
	private ExpenseEntry entry;
	private Date day1;
	private Date day2;
	private Ordering ordering;
	private Category category;
	private boolean delete = false;
	
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
		return this.entryId;
	}
	public void setEntryId(long entryId) {
		this.entryId = entryId;
	}
	public Date getDay1() {
		return this.day1;
	}
	public void setDay1(Date day1) {
		this.day1 = day1;
	}
	public Date getDay2() {
		return this.day2;
	}
	public void setDay2(Date day2) {
		this.day2 = day2;
	}
	public Ordering getOrdering() {
		return this.ordering;
	}
	public void setOrdering(Ordering ordering) {
		this.ordering = ordering;
	}
	public Category getCategory() {
		return this.category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}

	public ExpenseEntry getEntry() {
		return this.entry;
	}

	public void setEntry(ExpenseEntry entry) {
		this.entry = entry;
	}

	public void setDeleteFlag(boolean delete) {
		this.delete  = delete;
	}

	public boolean isDelete() {
		return this.delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}
}
