package de.reneruck.expensetracker;

import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

import de.reneruck.expensetracker.db.ExpenseQueryCallback;
import de.reneruck.expensetracker.model.ExpenseEntry;
import de.reneruck.expensetracker.model.Ordering;

/**
 * 
 * @author Rene
 * 
 */
public class FragmentAllItems extends SherlockFragment implements ExpenseQueryCallback {

	private static final String TAG = "FragmentAllItems";
	private ViewGroup container;
	private AppContext context;

	public FragmentAllItems() {
	}
	
	public FragmentAllItems(AppContext context) {
		this.context = context;
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "OnResume");
		if(this.context != null) {
			this.context.getDatabaseManager().getAllExpensEntries(Ordering.DESC, this);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(TAG, "OnCreateView");
		View inflated = inflater.inflate(R.layout.busy, container, false);
		((TextView) inflated.findViewById(R.id.busy_text_wait)).setText(R.string.retrieving_items);
		this.container = container;
		return inflated;
	}

	public void queryFinished(List<ExpenseEntry> resultSet) {
		this.container.removeAllViews();

		ListView list = new ListView(getActivity());
		list.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		list.setAdapter(new ExpenseEntryAdapter(getActivity(), R.layout.expense_entry, resultSet));

		TextView emptyText = new TextView(getActivity());
		emptyText.setText(getString(R.string.empty_list));
		list.setEmptyView(emptyText);

		this.container.addView(list);
	}
}
