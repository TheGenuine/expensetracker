package de.reneruck.expensetracker.model;

/**
 * 
 * @author Rene
 *
 */
public class Description {
	
	private long id = -1;
	private String value;
	private int count;
	
	public Description() {
	}
	
	public Description(long id, String value, int count) {
		super();
		this.id = id;
		this.value = value;
		this.count = count;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getCount() {
		return this.count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	@Override
	public String toString() {
		return "Description={id=" + this.id + ", value=" + this.value + ", count=" + this.count + "}";
	}
}
