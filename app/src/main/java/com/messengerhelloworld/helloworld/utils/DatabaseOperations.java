package com.messengerhelloworld.helloworld.utils;

import android.app.Activity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import com.messengerhelloworld.helloworld.interfaces.AfterStringResponseIsReceived;
import com.messengerhelloworld.helloworld.interfaces.AfterJsonArrayResponseIsReceived;

import org.json.JSONArray;
import org.json.JSONException;

public class DatabaseOperations {
	private final Activity activity;

	public DatabaseOperations(Activity activity) {
		this.activity = activity;
	}

	public void insert(HashMap<String, String> data, AfterStringResponseIsReceived afterStringResponseIsReceived) {
		Volley.newRequestQueue(activity).add(
				new StringRequest(
						Request.Method.POST,
						new Base().getBASE_URL() + "/insert.php",
						response -> afterStringResponseIsReceived.executeAfterResponse(response),
						error -> afterStringResponseIsReceived.executeAfterErrorResponse()
				) {
					@Override
					protected Map<String, String> getParams() {
						return data;
					}
				}
		);
	}

	public void retrieve(HashMap<String, String> data, AfterJsonArrayResponseIsReceived afterJsonArrayResponseIsReceived) {
		Volley.newRequestQueue(activity).add(
				new StringRequest(
						Request.Method.POST,
						new Base().getBASE_URL() + "/retrieve.php",
						response -> {
							try {
								afterJsonArrayResponseIsReceived.executeAfterResponse(new JSONArray(response));
							} catch (JSONException e) {
								Toast.makeText(activity, "333Sorry! Something went wrong.", Toast.LENGTH_SHORT).show();
							}
						},
						error -> {
							Toast.makeText(activity, String.valueOf(error), Toast.LENGTH_SHORT).show();
							afterJsonArrayResponseIsReceived.executeAfterErrorResponse();}
				) {
					@Override
					protected Map<String, String> getParams() {
						return data;
					}
				}
		);
	}

	public void retrieveChats(HashMap<String, String> data, AfterJsonArrayResponseIsReceived afterJsonArrayResponseIsReceived) {
		Volley.newRequestQueue(activity).add(
				new StringRequest(
						Request.Method.POST,
						new Base().getBASE_URL() + "/retrieveChats.php",
						response -> {
							try {
								afterJsonArrayResponseIsReceived.executeAfterResponse(new JSONArray(response));
							} catch (JSONException e) {
								Toast.makeText(activity, "Sorry! Something went wrong.", Toast.LENGTH_SHORT).show();
							}
						},
						error -> afterJsonArrayResponseIsReceived.executeAfterErrorResponse()
				) {
					@Override
					protected Map<String, String> getParams() {
						return data;
					}
				}
		);
	}
}
