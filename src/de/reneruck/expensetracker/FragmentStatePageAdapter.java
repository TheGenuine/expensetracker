package de.reneruck.expensetracker;

import java.util.Calendar;
import java.util.Date;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * 
 * @author Rene
 *
 */
public class FragmentStatePageAdapter extends FragmentStatePagerAdapter {

	private int numEntries;

	public FragmentStatePageAdapter(FragmentManager fm, int numEntries) {
    	super(fm);
    	this.numEntries = numEntries;
    }

    @Override
    public int getCount() {
        return this.numEntries;
    }

    @Override
    public Fragment getItem(int position) {
    	Calendar cal = Calendar.getInstance();
    	cal.set(Calendar.DAY_OF_MONTH, position);
    	return PageFragment.newInstance(new Date(cal.getTimeInMillis()));
    }
}
