package de.reneruck.expensetracker;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * @author Rene
 *
 */
public class FragmentStatePagerSupport extends FragmentActivity {

    FragmentAdapter adapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pager);

        Calendar calendar = Calendar.getInstance();
        final int NUM_ITEMS = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        List<Fragment> list = new LinkedList<Fragment>();
       
        
        this.adapter = new FragmentAdapter(getSupportFragmentManager(), list);

        this.viewPager = (ViewPager) findViewById(R.id.pager);
        this.viewPager.setAdapter(this.adapter);

        // Watch for button clicks.
        TextView button = (TextView) findViewById(R.id.goto_week_minus);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                viewPager.setCurrentItem(NUM_ITEMS - 8);
            }
        });
        button = (TextView) findViewById(R.id.goto_week_plus);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                viewPager.setCurrentItem(NUM_ITEMS + 8);
            }
        });
        button = (TextView) findViewById(R.id.goto_today);
        button.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		viewPager.setCurrentItem(0);
        	}
        });
    }

    /**
     * 
     * @author Rene
     *
     */
    public static class FragmentAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> list;

		public FragmentAdapter(FragmentManager fm, List<Fragment> list) {
        	super(fm);
        	this.list = list;
        }

        @Override
        public int getCount() {
            return this.list.size();
        }

        @Override
        public Fragment getItem(int position) {
            return PageFragment.newInstance(position);
        }
    }

    /**
     * 
     * @author Rene
     *
     */
    public static class PageFragment extends ListFragment {
        int mNum;

        /**
         * Create a new instance of CountingFragment, providing "num"
         * as an argument.
         */
        static PageFragment newInstance(int num) {
            PageFragment f = new PageFragment();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putInt("num", num);
            f.setArguments(args);

            return f;
        }

        /**
         * When creating, retrieve this instance's number from its arguments.
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mNum = getArguments() != null ? getArguments().getInt("num") : 1;
        }

        /**
         * The Fragment's UI is just a simple text view showing its
         * instance number.
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_pager_list, container, false);
            View tv = v.findViewById(R.id.text);
            ((TextView) tv).setText("Fragment #" + mNum);
            return v;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            setListAdapter(new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, Cheeses.sCheesesnStrings));
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            Log.i("FragmentList", "Item clicked: " + id);
        }
    }
    
    static class Cheeses {
    	private static String[] sCheesesnStrings = {"Gouda", "Emmentaler", "Morzarella", "Brie"};
    }
}