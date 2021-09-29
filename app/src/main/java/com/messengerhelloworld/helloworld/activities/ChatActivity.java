package com.messengerhelloworld.helloworld.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.messengerhelloworld.helloworld.R;
import com.messengerhelloworld.helloworld.adapters.ChatAdapter;
import com.messengerhelloworld.helloworld.interfaces.AfterJsonArrayResponseIsReceived;
import com.messengerhelloworld.helloworld.utils.DatabaseOperations;

import org.json.JSONArray;

import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {
	private static final String TAG = "hwChatActivity";
	private static final String CHAT_ID = "com.messengerhelloworld.helloworld.chatId";
	private static final String RECEIVER_USER_NAME = "com.messengerhelloworld.helloworld.receiverUserName";
	private final DatabaseOperations databaseOperations = new DatabaseOperations(this);
	private RecyclerView chatRecyclerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);

		Intent intent = getIntent();
		String chatId = intent.getStringExtra(CHAT_ID);
		String receiverUserName = intent.getStringExtra(RECEIVER_USER_NAME);
		SharedPreferences sp = getSharedPreferences("HelloWorldSharedPref", Context.MODE_PRIVATE);

		getSupportActionBar().setTitle(receiverUserName);

		chatRecyclerView = findViewById(R.id.chat_activityChat);

		HashMap<String, String> data = new HashMap<>();
		data.put("columns", "*");
		data.put("table_name", "messages");
		data.put("WHERE", "chatid=" + chatId);
		data.put("ORDER_BY", "dateTime");

		databaseOperations.retrieve(data, new AfterJsonArrayResponseIsReceived() {
			@Override
			public void executeAfterResponse(JSONArray response) {
				chatRecyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
				ChatAdapter chatAdapter = new ChatAdapter(response, sp.getString("HelloWorldUserId", null));
				chatRecyclerView.setAdapter(chatAdapter);
			}

			@Override
			public void executeAfterErrorResponse(String error) {
				Log.e(TAG, error);
			}
		});
	}
}
