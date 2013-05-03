package de.reneruck.expensetracker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import de.reneruck.expensetracker.model.ExpenseEntry;

/**
 * 
 * @author Rene
 *
 */
public class PageFragment extends ListFragment {
	private Calendar calendar;
	private AppContext context;
	private List<ExpenseEntry> entries;
	private View layout;

    /**
     * Create a new instance of CountingFragment, providing "entry"
     * as an argument.
     */
    static PageFragment newInstance(Date date) {
        PageFragment pageFragment = new PageFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putSerializable("date", date);
        pageFragment.setArguments(args);

        return pageFragment;
    }

    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Date date = (Date) (getArguments() != null ? getArguments().getSerializable("date") : 1);
        this.calendar = Calendar.getInstance();
        this.calendar.setTime(date);
        this.context = (AppContext) getActivity().getApplicationContext();
    }

    /**
     * The Fragment's UI is just a simple text view showing its
     * instance number.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        this.layout = inflater.inflate(R.layout.fragment_pager_list, container, false);
        View tv = layout.findViewById(R.id.text);
        SimpleDateFormat sdf = new SimpleDateFormat("E, dd.MM.yyyy", Locale.getDefault());
        ((TextView) tv).setText(sdf.format(this.calendar.getTime()));
        
        return this.layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.entries = this.context.getCurrentMonthEntries().get(this.calendar.get(Calendar.DAY_OF_MONTH), new LinkedList<ExpenseEntry>());
        setListAdapter(new ExpenseEntryAdapter(getActivity(), R.layout.expense_entry, this.entries, false));

        // set the sum of all the expenses for today
        TextView tv = (TextView) this.layout.findViewById(R.id.sum);
        tv.setText("Σ " + getSum() + "€");
    }

    private double getSum() {
    	double sum = 0;
    	for (ExpenseEntry entry : this.entries) {
			sum += entry.getValue();
		}
		return sum;
	}

	@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.i("FragmentList", "Item clicked: " + id);
    }
}
