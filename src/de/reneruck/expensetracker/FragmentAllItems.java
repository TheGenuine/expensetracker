package de.reneruck.expensetracker;

import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import de.reneruck.expensetracker.model.ExpenseEntry;
import de.reneruck.expensetracker.model.Ordering;

public class FragmentAllItems extends Fragment implements DatabaseQueryCallback {

	private ViewGroup container;
	private AppContext context;

	public FragmentAllItems(AppContext context) {
		this.context = context;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View inflated = inflater.inflate(R.layout.busy, container, false);	
		((TextView)inflated.findViewById(R.id.busy_text_wait)).setText(R.string.retrieving_items);
		this.container = container;
		this.context.getDatabaseManager().getAllExpensEntries(Ordering.DESC, this);
		return inflated;
	}

	@Override
	public void queryFinished(List<ExpenseEntry> resultSet) {
		this.container.removeAllViews();
		ListView list = new ListView(getActivity());
		list.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		list.setAdapter(new ExpenseEntryAdapter<ExpenseEntry>(getActivity(), resultSet));
		this.container.addView(list);
	}
}
