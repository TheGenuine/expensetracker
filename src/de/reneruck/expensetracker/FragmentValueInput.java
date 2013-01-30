package de.reneruck.expensetracker;

import android.os.Bundle;
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


	private TextView valueInput;
	private String formerInputValue;
	private ExpenseEntry currentEntry;

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
		this.valueInput.setText(this.formerInputValue);
		
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
	
	private OnClickListener delButtonClickListener = new OnClickListener() {
		
		public void onClick(View v) {
			CharSequence text = valueInput.getText();
			if (text.length() > 0) {
				valueInput.setText(text.subSequence(0, text.length() - 1));
			}
		}
	};
	
	
	private OnClickListener numberButtonClickListener = new OnClickListener() {
		
		public void onClick(View v) {
			String tag = (String) v.getTag();
			
			if (tag != "0" && valueInput.getText() == "0") {
				valueInput.setText("");
			}
			
			if (tag == "," && valueInput.getText().length() == 0) {
				valueInput.setText("0");
			}
			String newValue = valueInput.getText() + tag;
			valueInput.setText(newValue);
		}
	};
}
