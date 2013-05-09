package de.reneruck.expensetracker;

import java.sql.Date;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import de.reneruck.expensetracker.db.ExpenseQueryCallback;
import de.reneruck.expensetracker.model.ExpenseEntry;

/**
 * Fragment to display a view pager to switch through the days of a month.
 * 
 * @author Rene
 *
 */
@SuppressLint("ValidFragment")
public class FragmentViewPager extends Fragment implements ExpenseQueryCallback {

    private static final String TAG = "FragmentViewPager";
	private static final int TODAY = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	private ViewGroup container;
	private AppContext context;
	private FragmentStatePageAdapter adapter;
	private ViewPager viewPager;
	private View pagerLayout;

	public FragmentViewPager(AppContext appContext) {
		this.context = appContext;
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "-- onCreate --");
        super.onCreate(savedInstanceState);
        
    }

	private void startQuery() {
		Calendar instance = Calendar.getInstance();
    	instance.set(Calendar.DAY_OF_MONTH, 1);
    	
    	Calendar instance2 = Calendar.getInstance();
    	instance2.set(Calendar.DAY_OF_MONTH, instance.getActualMaximum(Calendar.DAY_OF_MONTH));
    	
    	this.context.getDatabaseManager().getAllExpensEntriesForRange(new Date(instance.getTimeInMillis()), new Date(instance2.getTimeInMillis()), null, this);
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	Log.d(TAG, "-- onCreateView --");
		View inflated = inflater.inflate(R.layout.busy, container, false);
		this.pagerLayout = inflater.inflate(R.layout.fragment_pager, null, false);
		((TextView) inflated.findViewById(R.id.busy_text_wait)).setText(R.string.retrieving_items);
		this.container = container;
		return inflated;
    }
    
    @Override
    public void onResume() {
    	Log.d(TAG, "-- onResume --");
    	super.onResume();
    	
    	startQuery();
    }
    
	@Override
	public void onAttach(Activity activity) {
		Log.d(TAG, "-- onAttach --");
		super.onAttach(activity);
	}

	public void queryFinished(List<ExpenseEntry> resultSet) {
		Log.d(TAG, "-- queryFinished --");
		this.container.removeAllViews();
		
		this.container.addView(this.pagerLayout);
		
		Calendar calendar = Calendar.getInstance();
		final int numItems = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		this.context.setCurrentMonthEntries(summarizeDays(resultSet));
		
		this.adapter = new FragmentStatePageAdapter(getActivity().getSupportFragmentManager(), numItems);

        this.viewPager = (ViewPager) this.container.findViewById(R.id.pager);
        this.viewPager.setAdapter(this.adapter);

        this.viewPager.setCurrentItem(TODAY);
        
        // Watch for button clicks.
        TextView button = (TextView) this.container.findViewById(R.id.goto_week_minus);
		button.setOnClickListener(this.weekMinusButtonListener);
        button = (TextView) this.container.findViewById(R.id.goto_week_plus);
		button.setOnClickListener(this.weekPlusButtonListener);
        button = (TextView) this.container.findViewById(R.id.goto_today);
		button.setOnClickListener(this.todayButtonListener);
	}

	OnClickListener weekMinusButtonListener = new OnClickListener() {
		public void onClick(View v) {
			viewPager.setCurrentItem(viewPager.getCurrentItem() - 7);
		}
	};
	OnClickListener weekPlusButtonListener = new OnClickListener() {
		public void onClick(View v) {
			viewPager.setCurrentItem(viewPager.getCurrentItem() + 7);
		}
	};
	OnClickListener todayButtonListener = new OnClickListener() {
		public void onClick(View v) {
			viewPager.setCurrentItem(TODAY);
		}
	};
	
	private SparseArray<List<ExpenseEntry>> summarizeDays(List<ExpenseEntry> resultSet) {
		SparseArray<List<ExpenseEntry>> result = new SparseArray<List<ExpenseEntry>>();
		Calendar cal = Calendar.getInstance();
		for (ExpenseEntry expenseEntry : resultSet) {
			cal.setTime(expenseEntry.getDate());
			int day = cal.get(Calendar.DAY_OF_MONTH);
			List<ExpenseEntry> list = result.get(day);
			if(list == null) {
				list = new LinkedList<ExpenseEntry>();
				result.put(day, list);
			}
			list.add(expenseEntry);
		}
		return result;
	}
}
