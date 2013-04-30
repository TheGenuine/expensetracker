package de.reneruck.expensetracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * @author Rene
 *
 */
public class FragmentNotImplemented extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View inflated = inflater.inflate(R.layout.not_implemented, container, false);
		return inflated;
	}
}
