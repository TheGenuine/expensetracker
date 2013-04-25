package de.reneruck.expensetracker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;

import de.reneruck.expensetracker.model.DatabaseExportContainer;
import de.reneruck.expensetracker.model.ExpenseEntry;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml.Encoding;

/**
 * AsyncTask to sync Entries to the serverside.
 * 
 * @author Rene
 * 
 */
public class SyncEntryAsync extends AsyncTask<Void, Void, Void> {

	private static final String TAG = "SyncEntryAsync";
	private ExpenseEntry currentEntry;

	public SyncEntryAsync(ExpenseEntry currentEntry) {
		this.currentEntry = currentEntry;
	}

	@Override
	protected Void doInBackground(Void... params) {
		Gson gson = new Gson();
		
		List<ExpenseEntry> list = new LinkedList<ExpenseEntry>();
		list.add(this.currentEntry);
		
		String serializedEntry = gson.toJson(new DatabaseExportContainer(list));

		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://192.168.1.27:8080/data");

		try {
			ByteArrayEntity entitiy = new ByteArrayEntity(serializedEntry.getBytes());
			entitiy.setContentType(ContentType.APPLICATION_JSON.getMimeType());
			post.setEntity(entitiy);
			post.setHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType());
			
			HttpResponse response = client.execute(post);

			if (response.getStatusLine().getStatusCode() == 200) {
				Log.i(TAG, "Entry accepted");
			} else {
				Log.e(TAG, "Error while syncing entry: " + response.getStatusLine());
			}
		} catch (IOException e) {
			Log.e(TAG, "Error while syncing entry: " + e.getMessage());
		}
		return null;
	}

}
