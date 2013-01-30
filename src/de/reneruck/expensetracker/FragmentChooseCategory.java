package de.reneruck.expensetracker;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;

import de.reneruck.expensetracker.model.ExpenseEntry;

/**
 * Fragment that Provides a list of already used and pre-defined Categories as well as an
 * input to dynamically add a new entry category.
 * 
 * @author Rene
 * 
 */
public class FragmentChooseCategory extends SherlockFragment {

	private ExpenseEntry currentEntry;

	public FragmentChooseCategory() {
	}
	
	public FragmentChooseCategory(ExpenseEntry currentEntry) {
		this.currentEntry = currentEntry;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View inflated = inflater.inflate(R.layout.new_entry_choose_category, container, false);
		ListView categoryList = (ListView) inflated.findViewById(R.id.predefined_category);
		fillCategoryList(categoryList);
		return inflated;
	}

	private void fillCategoryList(ListView categoryList) {
		List<String> categories = getStoredCategories();
		if(categoryList != null) {
			categoryList.setAdapter(new ArrayAdapter<String>(getSherlockActivity(), android.R.layout.simple_selectable_list_item, categories));
		}
	}

	private List<String> getStoredCategories() {
		SharedPreferences preferences = getSherlockActivity().getSharedPreferences(AppContext.PREF_USER, Context.MODE_PRIVATE);
		return getList(preferences.getString(AppContext.PREF_USER_CATEGORIES, ""));
	}

	private List<String> getList(String string) {
		if(string.length() > 0) {
			String[] split = string.split(";");
			return Arrays.asList(split);
		}
		return new LinkedList<String>();
	}
	
}
