package de.reneruck.expensetracker;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;

import de.reneruck.expensetracker.model.Category;
import de.reneruck.expensetracker.model.ExpenseEntry;

/**
 * Fragment that Provides a list of already used and pre-defined Categories as well as an
 * input to dynamically add a new entry category.
 * 
 * @author Rene
 * 
 */
public class FragmentChooseCategory extends SherlockFragment {

	private static final String TAG = "FragmentChooseCategory";
	private ExpenseEntry currentEntry;
	private ArrayList<Category> categories;
	private AppContext context;

	public FragmentChooseCategory() {
	}
	
	public FragmentChooseCategory(AppContext context) {
		this.context = context;
		getStoredCategories();
	}
	
	public FragmentChooseCategory(AppContext context, ExpenseEntry currentEntry) {
		this.context = context;
		this.currentEntry = currentEntry;
		getStoredCategories();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View inflated = inflater.inflate(R.layout.new_entry_choose_category, container, false);
		ListView categoryList = (ListView) inflated.findViewById(R.id.predefined_category);
		categoryList.setOnItemClickListener(this.itemclickListener);
		fillCategoryList(categoryList);
		return inflated;
	}
	
	private OnItemClickListener itemclickListener = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> va, View view, int pos, long id) {
			currentEntry.setCategory(categories.get(pos));
		}
	};

	private void fillCategoryList(ListView categoryList) {
		if(categoryList != null) {
			categoryList.setAdapter(new ArrayAdapter<Category>(getSherlockActivity(), android.R.layout.simple_selectable_list_item, this.categories));
		}
	}

	private void getStoredCategories() {
		this.categories = this.context.getAllCategories(); 
	}
	
}
