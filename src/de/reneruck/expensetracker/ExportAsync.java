package de.reneruck.expensetracker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;

import de.reneruck.expensetracker.model.DatabaseExportContainer;
import de.reneruck.expensetracker.model.ExpenseEntry;

/**
 * This asyncTask takes a list of {@link ExpenseEntry}, serializes them into
 * JSON and stores them onto the sdcard at the position defined in the settings.<br>
 * Optional 2nd parameter will can be a desired path to store the export file to.
 * 
 * @author Rene
 * 
 */
public class ExportAsync extends AsyncTask<Void, Void, String> {
	
	private static final String TAG = "ExportAsync";
	private DatabaseExportContainer exportContainer;
	private String exportFilePath;

	public ExportAsync(Context context, List<ExpenseEntry> entries, String exportFilePath) {
		this.exportContainer = new DatabaseExportContainer(entries);
		if(exportFilePath != null) {
			this.exportFilePath = exportFilePath;
		} else {
			SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(Statics.PREFERENCES, Context.MODE_PRIVATE);
			this.exportFilePath = sharedPreferences.getString(Statics.PREF_EXPORT_PATH, Environment.getExternalStorageDirectory().getAbsolutePath());
		}
	}

	@Override
	protected String doInBackground(Void... params) {

		String serialized = serialize();
		File exportFile = storeToFile(serialized);
		if(exportFile.exists()) {
			long totalSpace = exportFile.getTotalSpace();
			Log.d(TAG, "String size: " + serialized.length());
			Log.d(TAG, "Total Space: " + totalSpace);
		}
		
		return exportFile.getAbsolutePath();
	}

	private String serialize() {
		String serialized = "";
		Gson gson = new Gson();
		serialized = gson.toJson(this.exportContainer);
		return serialized;
	}

	private File storeToFile(String serialized) {
		File exportFile = new File(this.exportFilePath + File.separator + "Export-" + System.currentTimeMillis() + ".json");
		try {
			exportFile.createNewFile();
			
			FileOutputStream fos = new FileOutputStream(exportFile);
			fos.write(serialized.getBytes());
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return exportFile;
	}

}
