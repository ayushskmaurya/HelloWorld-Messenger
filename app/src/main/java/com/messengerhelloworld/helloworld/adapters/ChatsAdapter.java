package com.messengerhelloworld.helloworld.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.messengerhelloworld.helloworld.R;
import com.messengerhelloworld.helloworld.activities.ChatActivity;

import org.json.JSONArray;
import org.json.JSONException;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder> {
	private static final String TAG = "hwChatsAdapter";
	private final Context context;
	private final JSONArray localDataSet;

	public static class ViewHolder extends RecyclerView.ViewHolder {
		private final View userChat;
		private final TextView userName;
		private final TextView lastMsg;
		private final TextView lastMsgTime;

		public ViewHolder(View view) {
			super(view);
			userChat = view.findViewById(R.id.chat_RowItemChats);
			userName = view.findViewById(R.id.name_RowItemChats);
			lastMsg = view.findViewById(R.id.lastMsg_RowItemChats);
			lastMsgTime = view.findViewById(R.id.lastMsgTime_RowItemChats);
		}

		public View getUserChat() {
			return userChat;
		}
		public TextView getUserName() {
			return userName;
		}
		public TextView getLastMsg() {
			return lastMsg;
		}
		public TextView getLastMsgTime() {
			return lastMsgTime;
		}
	}

	public ChatsAdapter(Context context, JSONArray dataSet) {
		this.context = context;
		localDataSet = dataSet;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		View view = LayoutInflater.from(viewGroup.getContext())
				.inflate(R.layout.row_item_chats, viewGroup, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, final int position) {
		viewHolder.getUserChat().setOnClickListener(v -> {
			try {
				Intent intent = new Intent(context, ChatActivity.class);
				intent.putExtra("chatId", localDataSet.getJSONObject(position).getString("chatid"));
				intent.putExtra("receiverUserName", localDataSet.getJSONObject(position).getString("name"));
				context.startActivity(intent);
			} catch (JSONException e) {
				Log.e(TAG, e.toString());
			}
		});
		try {
			viewHolder.getUserName().setText(localDataSet.getJSONObject(position).getString("name"));
			viewHolder.getLastMsg().setText(localDataSet.getJSONObject(position).getString("message"));
			viewHolder.getLastMsgTime().setText(localDataSet.getJSONObject(position).getString("dateTime").substring(11, 16));
		} catch (JSONException e) {
			Log.e(TAG, e.toString());
		}
	}

	@Override
	public int getItemCount() {
		return localDataSet.length();
	}
}
