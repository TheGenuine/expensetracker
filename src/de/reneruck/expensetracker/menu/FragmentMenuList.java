package de.reneruck.expensetracker.menu;

import java.lang.reflect.Constructor;

import de.reneruck.expensetracker.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * @author Rene
 *
 */
public class FragmentMenuList extends ListFragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Menu menu = newMenuInstance(getActivity());
		
		if(menu != null) {
			new MenuInflater(getActivity()).inflate(R.menu.sliding_menu, menu);
			MenuListAdapter adapter = new MenuListAdapter(getActivity());
			for (int i = 0; i < menu.size(); i++) {
				MenuItem item = menu.getItem(i);
				adapter.add(new SampleItem(item.getTitle().toString(), item.getIcon()));
			}
			
			setListAdapter(adapter);
			
		} else {
			TextView emptyText = new TextView(getActivity());
			emptyText.setText("No Menu");
			getListView().setEmptyView(emptyText);
		}
		ListView listView = new ListView(getActivity());
		listView.setId(android.R.id.list);
		return listView;
	}

	private class SampleItem {
		Drawable iconRes;
		String text;
		
		public SampleItem(String text, Drawable iconRes) {
			this.text = text;
			this.iconRes = iconRes;
		}
	}



	private Menu newMenuInstance(Context context) {
		try {
			Class<?> menuBuilderClass = Class.forName("com.android.internal.view.menu.MenuBuilder");
			Constructor<?> constructor = menuBuilderClass.getDeclaredConstructor(Context.class);
			return (Menu) constructor.newInstance(context);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @author Rene
	 *
	 */
	public class MenuListAdapter extends ArrayAdapter<SampleItem> {

		public MenuListAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.menu_row, null);
			}
			ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
			icon.setImageDrawable(getItem(position).iconRes);
			TextView title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(getItem(position).text);
			return convertView;
		}

	}
}
