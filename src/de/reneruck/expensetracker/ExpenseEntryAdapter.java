package de.reneruck.expensetracker;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.reneruck.expensetracker.model.ExpenseEntry;

/**
 * A concrete BaseAdapter that is backed by an array of arbitrary
 * objects.  By default this class expects that the provided resource id references
 * a single TextView.  If you want to use a more complex layout, use the constructors that
 * also takes a field id.  That field id should reference a TextView in the larger layout
 * resource.
 *
 * <p>However the TextView is referenced, it will be filled with the toString() of each object in
 * the array. You can add lists or arrays of custom objects. Override the toString() method
 * of your objects to determine what text will be displayed for the item in the list.
 *
 * <p>To use something other than TextViews for the array display, for instance, ImageViews,
 * or to have some of data besides toString() results fill the views,
 * override {@link #getView(int, View, ViewGroup)} to return the type of view you want.
 */
public class ExpenseEntryAdapter extends BaseAdapter implements Filterable {
    /**
     * Contains the list of objects that represent the data of this ArrayAdapter.
     * The content of this list is referred to as "the array" in the documentation.
     */
    private List<ExpenseEntry> mObjects;

    /**
     * Lock used to modify the content of {@link #mObjects}. Any write operation
     * performed on the array should be synchronized on this lock. This lock is also
     * used by the filter (see {@link #getFilter()} to make a synchronized copy of
     * the original array of data.
     */
    private final Object mLock = new Object();

    /**
     * The resource indicating what views to inflate to display the content of this
     * array adapter.
     */
    private int mResource;

    /**
     * The resource indicating what views to inflate to display the content of this
     * array adapter in a drop down widget.
     */
    private int mDropDownResource;

    /**
     * If the inflated resource is not a TextView, {@link #mFieldId} is used to find
     * a TextView inside the inflated views hierarchy. This field must contain the
     * identifier that matches the one defined in the resource file.
     */
    private int mFieldId = 0;

    /**
     * Indicates whether or not {@link #notifyDataSetChanged()} must be called whenever
     * {@link #mObjects} is modified.
     */
    private boolean mNotifyOnChange = true;

    private Context mContext;

    // A copy of the original mObjects array, initialized from and then used instead as soon as
    // the mFilter ArrayFilter is used. mObjects will then only contain the filtered values.
    private ArrayList<ExpenseEntry> mOriginalValues;
    private ArrayFilter mFilter;

    private LayoutInflater mInflater;

	private Calendar currentDate;

	private boolean enableDayDivider;

    /**
     * Constructor.
     *
     * @param context The current context.
     * @param textViewResourceId The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     */
    public ExpenseEntryAdapter(Context context, int textViewResourceId) {
        init(context, textViewResourceId, 0, new ArrayList<ExpenseEntry>(), true);
    }

    /**
     * Constructor.
     *
     * @param context The current context.
     * @param resource The resource ID for a layout file containing a layout to use when
     *                 instantiating views.
     * @param textViewResourceId The id of the TextView within the layout resource to be populated
     */
    public ExpenseEntryAdapter(Context context, int resource, int textViewResourceId) {
        init(context, resource, textViewResourceId, new ArrayList<ExpenseEntry>(), true);
    }

    /**
     * Constructor.
     *
     * @param context The current context.
     * @param textViewResourceId The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects The objects to represent in the ListView.
     */
    public ExpenseEntryAdapter(Context context, int textViewResourceId, ExpenseEntry[] objects) {
        init(context, textViewResourceId, 0, Arrays.asList(objects), true);
    }

    /**
     * Constructor
     *
     * @param context The current context.
     * @param resource The resource ID for a layout file containing a layout to use when
     *                 instantiating views.
     * @param textViewResourceId The id of the TextView within the layout resource to be populated
     * @param objects The objects to represent in the ListView.
     */
    public ExpenseEntryAdapter(Context context, int resource, int textViewResourceId, ExpenseEntry[] objects) {
        init(context, resource, textViewResourceId, Arrays.asList(objects), true);
    }

    /**
     * Constructor
     *
     * @param context The current context.
     * @param textViewResourceId The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects The objects to represent in the ListView.
     */
    public ExpenseEntryAdapter(Context context, int textViewResourceId, List<ExpenseEntry> objects, boolean enableDayDivider) {
        init(context, textViewResourceId, 0, objects, enableDayDivider);
    }

    /**
     * Constructor
     *
     * @param context The current context.
     * @param resource The resource ID for a layout file containing a layout to use when
     *                 instantiating views.
     * @param textViewResourceId The id of the TextView within the layout resource to be populated
     * @param objects The objects to represent in the ListView.
     */
    public ExpenseEntryAdapter(Context context, int resource, int textViewResourceId, List<ExpenseEntry> objects) {
        init(context, resource, textViewResourceId, objects, true);
    }

    /**
     * Adds the specified object at the end of the array.
     *
     * @param object The object to add at the end of the array.
     */
    public void add(ExpenseEntry object) {
        synchronized (this.mLock) {
            if (this.mOriginalValues != null) {
                this.mOriginalValues.add(object);
            } else {
                this.mObjects.add(object);
            }
        }
        if (this.mNotifyOnChange) {
			notifyDataSetChanged();
		}
    }

    /**
     * Adds the specified Collection at the end of the array.
     *
     * @param collection The Collection to add at the end of the array.
     */
    public void addAll(Collection<? extends ExpenseEntry> collection) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.addAll(collection);
            } else {
                mObjects.addAll(collection);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Adds the specified items at the end of the array.
     *
     * @param items The items to add at the end of the array.
     */
    public void addAll(ExpenseEntry ... items) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                Collections.addAll(mOriginalValues, items);
            } else {
                Collections.addAll(mObjects, items);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Inserts the specified object at the specified index in the array.
     *
     * @param object The object to insert into the array.
     * @param index The index at which the object must be inserted.
     */
    public void insert(ExpenseEntry object, int index) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.add(index, object);
            } else {
                mObjects.add(index, object);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Removes the specified object from the array.
     *
     * @param object The object to remove.
     */
    public void remove(String object) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.remove(object);
            } else {
                mObjects.remove(object);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Remove all elements from the list.
     */
    public void clear() {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.clear();
            } else {
                mObjects.clear();
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Sorts the content of this adapter using the specified comparator.
     *
     * @param comparator The comparator used to sort the objects contained
     *        in this adapter.
     */
    public void sort(Comparator<? super ExpenseEntry> comparator) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                Collections.sort(mOriginalValues, comparator);
            } else {
                Collections.sort(mObjects, comparator);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mNotifyOnChange = true;
    }

    /**
     * Control whether methods that change the list ({@link #add},
     * {@link #insert}, {@link #remove}, {@link #clear}) automatically call
     * {@link #notifyDataSetChanged}.  If set to false, caller must
     * manually call notifyDataSetChanged() to have the changes
     * reflected in the attached view.
     *
     * The default is true, and calling notifyDataSetChanged()
     * resets the flag to true.
     *
     * @param notifyOnChange if true, modifications to the list will
     *                       automatically call {@link
     *                       #notifyDataSetChanged}
     */
    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }

    private void init(Context context, int resource, int textViewResourceId, List<ExpenseEntry> objects, boolean enableDayDivider) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResource = mDropDownResource = resource;
        mObjects = objects;
        mFieldId = textViewResourceId;
        this.enableDayDivider = enableDayDivider;
    }

    /**
     * Returns the context associated with this array adapter. The context is used
     * to create views from the resource passed to the constructor.
     *
     * @return The Context associated with this adapter.
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * {@inheritDoc}
     */
    public int getCount() {
        return mObjects.size();
    }

    /**
     * {@inheritDoc}
     */
    public ExpenseEntry getItem(int position) {
        return mObjects.get(position);
    }

    /**
     * Returns the position of the specified item in the array.
     *
     * @param item The item to retrieve the position of.
     *
     * @return The position of the specified item.
     */
    public int getPosition(String item) {
        return mObjects.indexOf(item);
    }

    /**
     * {@inheritDoc}
     */
    public long getItemId(int position) {
        return position;
    }

    /**
     * {@inheritDoc}
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, mResource);
    }

    private View createViewFromResource(int position, View convertView, ViewGroup parent,
            int resource) {
        View view;
        TextView description;
        TextView category;
        TextView value;

        if (convertView == null | convertView instanceof LinearLayout) {
            view = mInflater.inflate(resource, parent, false);
        } else {
            view = convertView;
        }

        try {
        	//  Otherwise, find the TextView field within the layout
            description = (TextView) view.findViewById(R.id.expense_entry_description);
            category = (TextView) view.findViewById(R.id.expense_entry_category);
            value = (TextView) view.findViewById(R.id.expense_entry_value);
        } catch (ClassCastException e) {
            Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException(
                    "ArrayAdapter requires the resource ID to be a TextView", e);
        }

        ExpenseEntry item = getItem(position);
        
        if (this.enableDayDivider && isNewDay(item)) {
        	
            view = mInflater.inflate(R.layout.expense_entry_day_divider, parent, false);
            
            // set Date text
            TextView dateText = ((TextView) view.findViewById(R.id.expense_entry_divider_date));
    		SimpleDateFormat formatter = new SimpleDateFormat("E, dd.MM.yyyy");
    		dateText.setText(formatter.format(new Date(item.getDate().getTime())));
    		
    		// append normal entry
    		view = appendEntry((ViewGroup) view, item);
        } else {
    		description.setText(item.getDescription().getValue());
    		category.setText(item.getCategory().getValue());
        	value.setText(item.getValue() + " €");        	
        }
        return view;
    }

    private View appendEntry(ViewGroup view, ExpenseEntry item) {
    	LinearLayout frame = new LinearLayout(mContext);
    	frame.setOrientation(LinearLayout.VERTICAL);

    	frame.addView(view);
    	View inflated = this.mInflater.inflate(R.layout.expense_entry, view, false);
    	frame.addView(createEntry(inflated, item));
    	return frame;
	}

	private View createEntry(View view, ExpenseEntry item) {
		((TextView) view.findViewById(R.id.expense_entry_description)).setText(item.getDescription().getValue());
		((TextView) view.findViewById(R.id.expense_entry_value)).setText(item.getValue() + "€");
		((TextView) view.findViewById(R.id.expense_entry_category)).setText(item.getCategory().getValue());
		return view;
	}

	private boolean isNewDay(ExpenseEntry item) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(item.getDate());
		int itemDay = calendar.get(Calendar.DAY_OF_MONTH);
		int itemMonth = calendar.get(Calendar.MONTH);
		if(this.currentDate == null) {
			this.currentDate = calendar;
			return true;
		} else {
			if(this.currentDate.get(Calendar.MONTH) < itemMonth) {
				this.currentDate = calendar;
				return true;
			} else {
				if(this.currentDate.get(Calendar.DAY_OF_MONTH) < itemDay) {
					this.currentDate = calendar;
					return true;
				}
			}
		}
		
		this.currentDate = calendar;
		return false;
	}

	private CharSequence getItemText(String item) {
    	String[] split = item.split(File.separator);
    	if (split.length > 1) {
    		return split[split.length - 1].replace(".mp3", " ").trim();
    	}
		return item;
	}

	/**
     * <p>Sets the layout resource to create the drop down views.</p>
     *
     * @param resource the layout resource defining the drop down views
     * @see #getDropDownView(int, android.view.View, android.view.ViewGroup)
     */
    public void setDropDownViewResource(int resource) {
        this.mDropDownResource = resource;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, mDropDownResource);
    }

    /**
     * Creates a new ArrayAdapter from external resources. The content of the array is
     * obtained through {@link android.content.res.Resources#getTextArray(int)}.
     *
     * @param context The application's environment.
     * @param textArrayResId The identifier of the array to use as the data source.
     * @param textViewResId The identifier of the layout used to create views.
     *
     * @return An ArrayAdapter<CharSequence>.
     */
    public static ArrayAdapter<CharSequence> createFromResource(Context context,
            int textArrayResId, int textViewResId) {
        CharSequence[] strings = context.getResources().getTextArray(textArrayResId);
        return new ArrayAdapter<CharSequence>(context, textViewResId, strings);
    }

    /**
     * {@inheritDoc}
     */
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    /**
     * <p>An array filter constrains the content of the array adapter with
     * a prefix. Each item that does not start with the supplied prefix
     * is removed from the list.</p>
     */
    private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = new ArrayList<ExpenseEntry>(mObjects);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                ArrayList<ExpenseEntry> list;
                synchronized (mLock) {
                    list = new ArrayList<ExpenseEntry>(mOriginalValues);
                }
                results.values = list;
                results.count = list.size();
            } else {
                String prefixString = prefix.toString().toLowerCase();

                ArrayList<ExpenseEntry> values;
                synchronized (mLock) {
                    values = new ArrayList<ExpenseEntry>(mOriginalValues);
                }

                final int count = values.size();
                final ArrayList<ExpenseEntry> newValues = new ArrayList<ExpenseEntry>();

                for (int i = 0; i < count; i++) {
                    final ExpenseEntry value = values.get(i);
                    final String valueText = value.toString().toLowerCase();

                    // First match against the whole, non-splitted value
                    if (valueText.startsWith(prefixString)) {
                        newValues.add(value);
                    } else {
                        final String[] words = valueText.split(" ");
                        final int wordCount = words.length;

                        // Start at index 0, in case valueText starts with space(s)
                        for (int k = 0; k < wordCount; k++) {
                            if (words[k].startsWith(prefixString)) {
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            mObjects = (List<ExpenseEntry>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}