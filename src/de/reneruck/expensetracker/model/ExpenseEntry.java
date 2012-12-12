package de.reneruck.expensetracker.model;

import java.sql.Date;

/**
 * Constitutes an expense entry.<br>
 * ExpenseEntries use the current time in ms as a unique id. So they can be
 * ordered chronologically by this id.
 * 
 * @author Rene
 * 
 */
public class ExpenseEntry {

	long id;
	Date date;
	String description;
	double value;
	Category category;
	
	/**
	 * Creates a new {@link ExpenseEntry}.
	 * 
	 * @param date
	 *            will be set to the current time, if not stated different
	 * @param description
	 *            description of the entry
	 * @param value
	 *            spend money for this entry in <b>Euro</b>
	 * @param category
	 *            one of a {@link Category} if not set, a default category will
	 *            be used.
	 */
	public ExpenseEntry(Date date, String description, double value, Category category) {
		super();
		this.id = System.currentTimeMillis();
		this.date = date;
		this.description = description;
		this.value = value;
		this.category = category;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
}
