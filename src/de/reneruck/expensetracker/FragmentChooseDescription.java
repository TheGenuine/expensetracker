package de.reneruck.expensetracker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

import de.reneruck.expensetracker.db.DescriptionQueryCallback;
import de.reneruck.expensetracker.model.Description;
import de.reneruck.expensetracker.model.ExpenseEntry;

/**
 * Fragment that Provides a list of already used Descriptions as well as an
 * input to dynamically add a new entry description.
 * 
 * @author Rene
 * 
 */
public class FragmentChooseDescription extends SherlockFragment implements DescriptionQueryCallback {

	private static final String TAG = "FragmentChooseDescription";
	private ExpenseEntry currentEntry;
	private AppContext context;
	private View layout;
	private ListView descriptionList;
	private ArrayList<Description> descriptions;

	public FragmentChooseDescription() {
	}
	
	public FragmentChooseDescription(AppContext context, ExpenseEntry currentEntry) {
		this.context = context;
		this.currentEntry = currentEntry;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		this.layout = inflater.inflate(R.layout.new_entry_choose_description, container, false);
		this.descriptionList = (ListView) this.layout.findViewById(R.id.predefined_description);
		this.descriptionList.setOnItemClickListener(this.itemclickListener);
		((ImageView) this.layout.findViewById(R.id.button_new_description)).setOnClickListener(this.onAddNewDescriptionListener);
		queryAllDescriptions();
		return this.layout;
	}
	
	private OnItemClickListener itemclickListener = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> va, View view, int pos, long id) {
			updateCheckVisualization();
			currentEntry.setDescription(descriptions.get(pos));
			view.setBackgroundResource(R.drawable.entry_checked);
		}

	};

	private void updateCheckVisualization() {
		if(this.currentEntry.getCategory() != null && this.currentEntry.getCategory().getId() > -1) {
			View childAt = this.descriptionList.getSelectedView(); //.getChildAt((int) this.currentEntry.getCategory().getId() - 1);
			if(childAt != null) {
				childAt.setBackgroundResource(0);
			}
		} else {
			View input = this.layout.findViewById(R.id.new_description_input);
			input.setBackgroundResource(0);
		}
	}
	
	private OnClickListener onAddNewDescriptionListener = new OnClickListener() {
		
		public void onClick(View v) {
			safeNewDescription();
		}
	};
	
	private void safeNewDescription() {
		if(this.layout != null) {
			View input = this.layout.findViewById(R.id.new_description_input);
			if(input != null){
				String newDescriptionText = ((EditText) input).getText().toString();
				
				if(newDescriptionText.length() > 1) {
					this.currentEntry.setDescription(new Description(-1, newDescriptionText, 1));
					
					InputMethodManager imm = (InputMethodManager) this.context.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
					Toast.makeText(this.context, getString(R.string.new_description_saved_message), Toast.LENGTH_SHORT).show();
					
					//color text input to indicate acceptance
					input.setBackgroundResource(R.drawable.field_checked);
				} else {
					Toast.makeText(this.context, getString(R.string.new_description_empty_message), Toast.LENGTH_SHORT).show();
				}
			} 
		}
	}
	
	private void queryAllDescriptions() {
		this.context.getDatabaseManager().getAllDescriptions(this);
	}

	private List<String> getStoredDescriptions() {
		SharedPreferences preferences = getSherlockActivity().getSharedPreferences(AppContext.PREF_USER, Context.MODE_PRIVATE);
		return getList(preferences.getString(AppContext.PREF_USER_DESCRIPTIONS, ""));
	}

	private List<String> getList(String string) {
		if(string.length() > 0) {
			String[] split = string.split(";");
			return Arrays.asList(split);
		}
		return new LinkedList<String>();
	}

	public void queryFinished(List<Description> resultSet) {
		this.descriptions = new ArrayList<Description>(resultSet);
		if(this.descriptionList != null) {
			this.descriptionList.setAdapter(new ArrayAdapter<Description>(getSherlockActivity(), android.R.layout.simple_selectable_list_item, this.descriptions));
		}
	}
}
