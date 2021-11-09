package com.messengerhelloworld.helloworld.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.messengerhelloworld.helloworld.R;

import org.json.JSONArray;
import org.json.JSONException;

public class ChatAdapter extends RecyclerView.Adapter {
	private static final String TAG = "hwChatAdapter";
	private final JSONArray localDataSet;
	private final String userid;
	private String readReceipt;

	public static class SentViewHolder extends RecyclerView.ViewHolder {
		private final TextView sentMsg;
		private final TextView sentMsgTime;
		private final ImageView readReceiptSent;
		private final ImageView readReceiptSeen;

		public SentViewHolder(View view) {
			super(view);
			sentMsg = view.findViewById(R.id.msg_rowItemMessageSent);
			sentMsgTime = view.findViewById(R.id.time_rowItemMessageSent);
			readReceiptSent = view.findViewById(R.id.readReceiptSent_rowItemMessageSent);
			readReceiptSeen = view.findViewById(R.id.readReceiptSeen_rowItemMessageSent);
		}

		public TextView getSentMsg() {
			return sentMsg;
		}
		public TextView getSentMsgTime() {
			return sentMsgTime;
		}
		public ImageView getReadReceiptSent() {
			return readReceiptSent;
		}
		public ImageView getReadReceiptSeen() {
			return readReceiptSeen;
		}
	}

	public static class ReceivedViewHolder extends RecyclerView.ViewHolder {
		private final TextView receivedMsg;
		private final TextView receivedMsgTime;

		public ReceivedViewHolder(View view) {
			super(view);
			receivedMsg = view.findViewById(R.id.msg_rowItemMessageReceived);
			receivedMsgTime = view.findViewById(R.id.time_rowItemMessageReceived);
		}

		public TextView getReceivedMsg() {
			return receivedMsg;
		}
		public TextView getReceivedMsgTime() {
			return receivedMsgTime;
		}
	}

	public ChatAdapter(JSONArray dataSet, String userid) {
		localDataSet = dataSet;
		this.userid = userid;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		if(viewType == 0) {
			View view = LayoutInflater.from(viewGroup.getContext())
					.inflate(R.layout.row_item_message_sent, viewGroup, false);
			return new SentViewHolder(view);
		}
		else {
			View view = LayoutInflater.from(viewGroup.getContext())
					.inflate(R.layout.row_item_message_received, viewGroup, false);
			return new ReceivedViewHolder(view);
		}
	}

	@Override
	public int getItemViewType(int position) {
		try {
			if(localDataSet.getJSONObject(position).getString("senderid").equals(userid))
				return 0;
		} catch (JSONException e) {
			Log.e(TAG, e.toString());
		}
		return 1;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
		if(viewHolder.getClass() == SentViewHolder.class) {
			SentViewHolder vHolder = (SentViewHolder) viewHolder;
			try {
				vHolder.getSentMsg().setText(localDataSet.getJSONObject(position).getString("message"));
				vHolder.getSentMsgTime().setText(localDataSet.getJSONObject(position).getString("dateTime").substring(11, 16));
				readReceipt = localDataSet.getJSONObject(position).getString("isMsgSeen");
				if(readReceipt.equals("1")) {
					vHolder.getReadReceiptSent().setVisibility(View.GONE);
					vHolder.getReadReceiptSeen().setVisibility(View.VISIBLE);
				}
				else if(readReceipt.equals("0")) {
					vHolder.getReadReceiptSeen().setVisibility(View.GONE);
					vHolder.getReadReceiptSent().setVisibility(View.VISIBLE);
				}
			} catch (JSONException e) {
				Log.e(TAG, e.toString());
			}
		}
		else {
			ReceivedViewHolder vHolder = (ReceivedViewHolder) viewHolder;
			try {
				vHolder.getReceivedMsg().setText(localDataSet.getJSONObject(position).getString("message"));
				vHolder.getReceivedMsgTime().setText(localDataSet.getJSONObject(position).getString("dateTime").substring(11, 16));
			} catch (JSONException e) {
				Log.e(TAG, e.toString());
			}
		}
	}

	@Override
	public int getItemCount() {
		return localDataSet.length();
	}
}
