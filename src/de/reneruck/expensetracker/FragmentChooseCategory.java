package de.reneruck.expensetracker;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

import de.reneruck.expensetracker.db.CategoryQueryCallback;
import de.reneruck.expensetracker.db.ExpenseQueryCallback;
import de.reneruck.expensetracker.model.Category;
import de.reneruck.expensetracker.model.ExpenseEntry;

/**
 * Fragment that Provides a list of already used and pre-defined Categories as well as an
 * input to dynamically add a new entry category.
 * 
 * @author Rene
 * 
 */
public class FragmentChooseCategory extends SherlockFragment implements CategoryQueryCallback {

	private static final String TAG = "FragmentChooseCategory";
	private ExpenseEntry currentEntry;
	private ArrayList<Category> categories;
	private AppContext context;
	private View layout;
	private ListView categoryList;

	public FragmentChooseCategory() {
	}
	
	public FragmentChooseCategory(AppContext context) {
		this.context = context;
	}
	
	public FragmentChooseCategory(AppContext context, ExpenseEntry currentEntry) {
		this.context = context;
		this.currentEntry = currentEntry;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		this.layout = inflater.inflate(R.layout.new_entry_choose_category, container, false);
		this.categoryList = (ListView) this.layout.findViewById(R.id.predefined_category);
		this.categoryList.setOnItemClickListener(this.itemclickListener);
		((ImageView) this.layout.findViewById(R.id.button_new_category)).setOnClickListener(this.onAddNewCategoryListener);
		queryAllCategories();
		return this.layout;
	}
	
	private void queryAllCategories() {
		this.context.getDatabaseManager().getAllCategories(this);
	}

	private OnItemClickListener itemclickListener = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> va, View view, int pos, long id) {
			updateCheckVisualization();
			currentEntry.setCategory(categories.get(pos));
			view.setBackgroundResource(R.drawable.entry_checked);
		}

	};

	private void updateCheckVisualization() {
		if(this.currentEntry.getCategory() != null && this.currentEntry.getCategory().getId() > -1) {
			View childAt = this.categoryList.getSelectedView(); //.getChildAt((int) this.currentEntry.getCategory().getId() - 1);
			if(childAt != null) {
				childAt.setBackgroundResource(0);
			}
		} else {
			View input = this.layout.findViewById(R.id.new_category_input);
			input.setBackgroundResource(0);
		}
	}
	
	private OnClickListener onAddNewCategoryListener = new OnClickListener() {
		
		public void onClick(View v) {
			safeNewCategory();
		}
	};
	
	private void safeNewCategory() {
		if(this.layout != null) {
			View input = this.layout.findViewById(R.id.new_category_input);
			if(input != null){
				String newCategoryText = ((EditText) input).getText().toString();
				
				if(newCategoryText.length() > 1) {
					this.currentEntry.setCategory(new Category(-1, newCategoryText, 0));
					InputMethodManager imm = (InputMethodManager) this.context.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
					Toast.makeText(this.context, R.string.new_category_saved_message, Toast.LENGTH_SHORT).show();
					updateCheckVisualization();
					input.setBackgroundResource(R.drawable.field_checked);
					
				} else {
					Toast.makeText(this.context, R.string.new_category_empty_message, Toast.LENGTH_SHORT).show();
				}
			} 
		}
	}
	
	public void queryFinished(List<Category> resultSet) {
		this.categories = new ArrayList<Category>(resultSet);
		if(this.categoryList != null) {
			this.categoryList.setAdapter(new ArrayAdapter<Category>(getSherlockActivity(), android.R.layout.simple_selectable_list_item, this.categories));
		}
	}
	
}
