package com.messengerhelloworld.helloworld.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.messengerhelloworld.helloworld.R;
import com.messengerhelloworld.helloworld.adapters.ChatAdapter;
import com.messengerhelloworld.helloworld.interfaces.AfterJsonArrayResponseIsReceived;
import com.messengerhelloworld.helloworld.interfaces.AfterStringResponseIsReceived;
import com.messengerhelloworld.helloworld.utils.DatabaseOperations;

import org.json.JSONArray;

import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {
	private static final String TAG = "hwChatActivity";
	private static final String CHAT_ID = "com.messengerhelloworld.helloworld.chatId";
	private static final String RECEIVER_USER_NAME = "com.messengerhelloworld.helloworld.receiverUserName";
	private static final String RECEIVER_USER_ID = "com.messengerhelloworld.helloworld.receiverUserId";
	private final DatabaseOperations databaseOperations = new DatabaseOperations(this);
	private RecyclerView chatRecyclerView;
	private String chatId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);

		Intent intent = getIntent();
		chatId = intent.getStringExtra(CHAT_ID);
		String receiverUserName = intent.getStringExtra(RECEIVER_USER_NAME);
		String receiverId = intent.getStringExtra(RECEIVER_USER_ID);
		SharedPreferences sp = getSharedPreferences("HelloWorldSharedPref", Context.MODE_PRIVATE);
		String senderId = sp.getString("HelloWorldUserId", null);

		getSupportActionBar().setTitle(receiverUserName);

		// Retrieving messages.
		if(!chatId.equals("null")) {
			chatRecyclerView = findViewById(R.id.chat_activityChat);

			HashMap<String, String> data = new HashMap<>();
			data.put("columns", "senderid, message, dateTime");
			data.put("table_name", "messages");
			data.put("WHERE", "chatid=" + chatId);
			data.put("ORDER_BY", "dateTime");

			databaseOperations.retrieve(data, new AfterJsonArrayResponseIsReceived() {
				@Override
				public void executeAfterResponse(JSONArray response) {
					chatRecyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
					ChatAdapter chatAdapter = new ChatAdapter(response, senderId);
					chatRecyclerView.setAdapter(chatAdapter);
				}

				@Override
				public void executeAfterErrorResponse(String error) {
					Log.e(TAG, error);
				}
			});
		}

		// Sending message.
		TextView messageTextView = findViewById(R.id.msg_activityChat);
		ImageButton sendMessage = findViewById(R.id.sendMsg_activityChat);
		sendMessage.setOnClickListener(v -> {
			String message = messageTextView.getText().toString().trim();
			if(message.length() > 0) {
				messageTextView.setText("");
				HashMap<String, String> data = new HashMap<>();
				data.put("chatid", chatId);
				data.put("senderid", senderId);
				data.put("receiverid", receiverId);
				data.put("message", message);

				databaseOperations.insertMessage(data, new AfterStringResponseIsReceived() {
					@Override
					public void executeAfterResponse(String response) {
						chatId = response;
					}

					@Override
					public void executeAfterErrorResponse(String error) {
						Log.e(TAG, error);
						Toast.makeText(ChatActivity.this, "Unable to send message, please try again.", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}
}
