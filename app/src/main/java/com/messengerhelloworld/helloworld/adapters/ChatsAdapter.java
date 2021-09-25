package com.messengerhelloworld.helloworld.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.messengerhelloworld.helloworld.R;

import org.json.JSONArray;
import org.json.JSONException;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder> {
	private final JSONArray localDataSet;
	private Context context;

	public static class ViewHolder extends RecyclerView.ViewHolder {
		private final View userChat;
		private final TextView userName;
		private final TextView lastMsg;
		private final TextView lastMsgTime;

		public ViewHolder(View view) {
			super(view);
			userChat = view.findViewById(R.id.user_chat);
			userName = (TextView) view.findViewById(R.id.user_name);
			lastMsg = (TextView) view.findViewById(R.id.last_msg);
			lastMsgTime = (TextView) view.findViewById(R.id.last_msg_time);
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

	public ChatsAdapter(JSONArray dataSet, Context context) {
		localDataSet = dataSet;
		this.context = context;
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
				Log.d("HelloWorld: ChatId", localDataSet.getJSONObject(position).getString("chatid"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		});
		try {
			viewHolder.getUserName().setText(localDataSet.getJSONObject(position).getString("name"));
			viewHolder.getLastMsg().setText(localDataSet.getJSONObject(position).getString("message"));
			viewHolder.getLastMsgTime().setText(localDataSet.getJSONObject(position).getString("dateTime").substring(11, 16));
		} catch (JSONException e) {
			Toast.makeText(context, "Sorry! Something went wrong.", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public int getItemCount() {
		return localDataSet.length();
	}
}
