package com.messengerhelloworld.helloworld.utils;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import android.os.Handler;

import com.messengerhelloworld.helloworld.interfaces.AfterStringResponseIsReceived;
import com.messengerhelloworld.helloworld.interfaces.AfterJsonArrayResponseIsReceived;

import org.json.JSONArray;
import org.json.JSONException;

public class DatabaseOperations {
	private static final String TAG = "hwDatabaseOperations";
	private final Activity activity;
	private final Handler handler = new Handler();

	public DatabaseOperations(Activity activity) {
		this.activity = activity;
	}

	// Inserting new row in the database table.
	public void insert(HashMap<String, String> data, AfterStringResponseIsReceived afterStringResponseIsReceived) {
		Volley.newRequestQueue(activity).add(
				new StringRequest(
						Request.Method.POST,
						new Base().getBASE_URL() + "/insert.php",
						response -> afterStringResponseIsReceived.executeAfterResponse(response),
						error -> afterStringResponseIsReceived.executeAfterErrorResponse(error.toString())
				) {
					@Override
					protected Map<String, String> getParams() {
						return data;
					}
				}
		);
	}

	// Retrieving data from the database table.
	public void retrieve(HashMap<String, String> data, AfterJsonArrayResponseIsReceived afterJsonArrayResponseIsReceived) {
		Volley.newRequestQueue(activity).add(
				new StringRequest(
						Request.Method.POST,
						new Base().getBASE_URL() + "/retrieve.php",
						response -> {
							try {
								afterJsonArrayResponseIsReceived.executeAfterResponse(new JSONArray(response));
							} catch (JSONException e) {
								Log.e(TAG, e.toString());
								Toast.makeText(activity, "Unable to complete the process, please try again.", Toast.LENGTH_SHORT).show();
							}
						},
						error -> afterJsonArrayResponseIsReceived.executeAfterErrorResponse(error.toString())
				) {
					@Override
					protected Map<String, String> getParams() {
						return data;
					}
				}
		);
	}

	// Retrieving all the chats of the user.
	public void retrieveChats(HashMap<String, String> data, AfterJsonArrayResponseIsReceived afterJsonArrayResponseIsReceived) {
		Volley.newRequestQueue(activity).add(
				new StringRequest(
						Request.Method.POST,
						new Base().getBASE_URL() + "/retrieveChats.php",
						response -> {
							try {
								afterJsonArrayResponseIsReceived.executeAfterResponse(new JSONArray(response));
							} catch (JSONException e) {
								Log.e(TAG, e.toString());
							}
							finally {
								waitFor3Secs(data, afterJsonArrayResponseIsReceived);
							}
						},
						error -> {
							afterJsonArrayResponseIsReceived.executeAfterErrorResponse(error.toString());
							waitFor3Secs(data, afterJsonArrayResponseIsReceived);
						}
				) {
					@Override
					protected Map<String, String> getParams() {
						return data;
					}
				}
		);
	}

	// Waiting for 3 seconds.
	private void waitFor3Secs(HashMap<String, String> data, AfterJsonArrayResponseIsReceived afterJsonArrayResponseIsReceived) {
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				retrieveChats(data, afterJsonArrayResponseIsReceived);
			}
		}, 3000);
	}

	// Retrieving contacts.
	public void retrieveContacts(HashMap<String, String> data, AfterJsonArrayResponseIsReceived afterJsonArrayResponseIsReceived) {
		Volley.newRequestQueue(activity).add(
				new StringRequest(
						Request.Method.POST,
						new Base().getBASE_URL() + "/retrieveContacts.php",
						response -> {
							try {
								afterJsonArrayResponseIsReceived.executeAfterResponse(new JSONArray(response));
							} catch (JSONException e) {
								Log.e(TAG, e.toString());
							}
						},
						error -> afterJsonArrayResponseIsReceived.executeAfterErrorResponse(error.toString())
				) {
					@Override
					protected Map<String, String> getParams() {
						return data;
					}
				}
		);
	}
}
