package de.reneruck.expensetracker;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

import de.reneruck.expensetracker.model.ExpenseEntry;

/**
 * Fragment to provide a calculator like input for the amount of money spend.<br>
 * TODO: Additional features planned: add calculation functionality.
 * 
 * @author Rene
 * 
 */
public class FragmentValueInput extends SherlockFragment {


	private static final String TAG = "FragmentValueInput";
	private TextView valueInput;
	private ExpenseEntry currentEntry;
	private boolean tempZero = false;
	
	public FragmentValueInput() {
	}
	
	public FragmentValueInput(ExpenseEntry currentEntry) {
		this.currentEntry = currentEntry;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View inflated = inflater.inflate(R.layout.new_entry_value_input, container, false);
		setButtonListener(inflated);
		this.valueInput = (TextView) inflated.findViewById(R.id.value_input_value);
		this.valueInput.setFocusable(false);
		
		return inflated;
	}
	
	private void setButtonListener(View inflated) {
		
		inflated.findViewById(R.id.button_del).setOnClickListener(this.delButtonClickListener);
		
		inflated.findViewById(R.id.button_1).setOnClickListener(this.numberButtonClickListener);
		inflated.findViewById(R.id.button_2).setOnClickListener(this.numberButtonClickListener);
		inflated.findViewById(R.id.button_3).setOnClickListener(this.numberButtonClickListener);
		inflated.findViewById(R.id.button_4).setOnClickListener(this.numberButtonClickListener);
		inflated.findViewById(R.id.button_5).setOnClickListener(this.numberButtonClickListener);
		inflated.findViewById(R.id.button_6).setOnClickListener(this.numberButtonClickListener);
		inflated.findViewById(R.id.button_7).setOnClickListener(this.numberButtonClickListener);
		inflated.findViewById(R.id.button_8).setOnClickListener(this.numberButtonClickListener);
		inflated.findViewById(R.id.button_9).setOnClickListener(this.numberButtonClickListener);
		inflated.findViewById(R.id.button_0).setOnClickListener(this.numberButtonClickListener);
		inflated.findViewById(R.id.button_comma).setOnClickListener(this.numberButtonClickListener);
	}
	
	private void updateEntryValue() {
		this.currentEntry.setValue(this.valueInput.getText().length() > 0 ? Double.parseDouble(this.valueInput.getText().toString()) : 0);
	}
	
	private OnClickListener delButtonClickListener = new OnClickListener() {
		
		public void onClick(View v) {
			CharSequence text = valueInput.getText();
			if (text.length() > 0) {
				int end = text.length() - 1;
				if(end > 0) {
					valueInput.setText(text.subSequence(0, end));
				} else {
					valueInput.setText("");
				}
				updateEntryValue();
			}
		}

	};
	
	
	private OnClickListener numberButtonClickListener = new OnClickListener() {
		
		public void onClick(View v) {
			handleButtonPress(v);
			updateEntryValue();
		}
	};
	
	private void handleButtonPress(View v) {
		String tag = (String) v.getTag();
		String inputfieldValue = this.valueInput.getText().toString();
		
		String newValue = "";
		if(inputfieldValue.endsWith("0") && this.tempZero) {
			this.tempZero = false;
			inputfieldValue = inputfieldValue.substring(0, inputfieldValue.length() - 1);
		}
		
		if (tag.equals(".")) {
			
			if(!inputfieldValue.contains(".")){
				if(inputfieldValue.length() == 0) {
					newValue = "0.";
				} else {
					newValue = inputfieldValue + tag;
				}
			} else {
				newValue = inputfieldValue;
			}
		} else {
			newValue = inputfieldValue + tag;
		}
		
		if(newValue.endsWith(".")) {
			this.tempZero = true;
			newValue = newValue + "0";
		}

		this.valueInput.setText(removeLeadingZeros(newValue));
	}
	
	private String removeLeadingZeros(String newValue) {
		CharSequence sequence = newValue.substring(0);

		for (int j = 0; j < sequence.length(); j++) {
			if(sequence.charAt(j) == '.') {
				return newValue.substring(j - 1);
			} else if(sequence.charAt(j) != '0') {
				return newValue.substring(j);
			}
		}
		return newValue;
	}

	public void onPause() {
		super.onPause();
	};
}
