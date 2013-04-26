package de.reneruck.expensetracker;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import de.reneruck.expensetracker.db.ExpenseQueryCallback;
import de.reneruck.expensetracker.model.ExpenseEntry;

/**
 * 
 * @author Rene
 *
 */
public class FragmentStatePagerSupport extends FragmentActivity implements ExpenseQueryCallback  {

    protected static final int TODAY = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	private FragmentAdapter adapter;
    private ViewPager viewPager;
	private AppContext appContext;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pager);
        
        this.appContext = (AppContext) getApplicationContext();
        
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.DAY_OF_MONTH, 1);
        
        Calendar instance2 = Calendar.getInstance();
        instance2.set(Calendar.DAY_OF_MONTH, instance.getActualMaximum(Calendar.DAY_OF_MONTH));
        
		Date startDay = new Date(instance.getTimeInMillis());
		Date endDay = new Date(instance2.getTimeInMillis());
		
		this.appContext.getDatabaseManager().getAllExpensEntriesForRange(startDay, endDay, null, this);
		
    }

	public void queryFinished(List<ExpenseEntry> resultSet) {
		
		Calendar calendar = Calendar.getInstance();
		final int numItems = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		this.appContext.setCurrentMonthEntries(summarizeDays(resultSet));
		
		this.adapter = new FragmentAdapter(getSupportFragmentManager(), numItems);

        this.viewPager = (ViewPager) findViewById(R.id.pager);
        this.viewPager.setAdapter(this.adapter);

        this.viewPager.setCurrentItem(TODAY);
        
        // Watch for button clicks.
        TextView button = (TextView) findViewById(R.id.goto_week_minus);
		button.setOnClickListener(this.weekMinusButtonListener);
        button = (TextView) findViewById(R.id.goto_week_plus);
		button.setOnClickListener(this.weekPlusButtonListener);
        button = (TextView) findViewById(R.id.goto_today);
		button.setOnClickListener(this.todayButtonListener);
	}

	OnClickListener weekMinusButtonListener = new OnClickListener() {
		public void onClick(View v) {
			viewPager.setCurrentItem(viewPager.getCurrentItem() - 8);
		}
	};
	OnClickListener weekPlusButtonListener = new OnClickListener() {
		public void onClick(View v) {
			viewPager.setCurrentItem(viewPager.getCurrentItem() + 8);
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