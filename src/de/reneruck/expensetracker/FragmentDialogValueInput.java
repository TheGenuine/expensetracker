package de.reneruck.expensetracker;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class FragmentDialogValueInput extends DialogFragment {


	private TextView valueInput;
	private NewEntryActivtiy callback;
	private String formerInputValue;

	public FragmentDialogValueInput(NewEntryActivtiy newEntryActivtiy, String formerInputValue) {
		this.callback = newEntryActivtiy;
		this.formerInputValue = formerInputValue;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setTitle(R.string.dialog_value_input_title);
		builder.setView(getLayout());
		builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dismiss();
			}
			
		});
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				callback.setValueInput(valueInput.getText());
			}
		});
		return builder.create();
	}
	
	private View getLayout() {
		View inflated = getActivity().getLayoutInflater().inflate(R.layout.value_input, null);
		setButtonListener(inflated);
		this.valueInput = (TextView) inflated.findViewById(R.id.value_input_value);
		this.valueInput.setFocusable(false);
		this.valueInput.setText(formerInputValue);
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
		
		@Override
		public void onClick(View v) {
			CharSequence text = valueInput.getText();
			if(text.length() > 0) {
				valueInput.setText(text.subSequence(0, text.length()-1));
			}
		}
	};
	
	
	private OnClickListener numberButtonClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String tag = (String) v.getTag();
			
			if(tag != "0" && valueInput.getText() == "0") {
				valueInput.setText("");
			}
			
			if(tag == "," && valueInput.getText().length() == 0) {
				valueInput.setText("0");
			}
			String newValue = valueInput.getText() + tag;
			valueInput.setText(newValue);
		}
	};
	
	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		super.onDismiss(dialog);
	}
	
}
