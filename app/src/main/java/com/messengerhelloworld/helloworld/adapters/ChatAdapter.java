package com.messengerhelloworld.helloworld.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

	public static class SentFileViewHolder extends RecyclerView.ViewHolder {
		private final TextView sentFilename;
		private final TextView sentCaption;
		private final TextView sentCaptionTime;
		private final ProgressBar progressBar;
		private final ImageView cancelUpload;
		private final ImageView downloadSentFile;
		private final ImageView readReceiptFileSending;
		private final ImageView readReceiptFileSent;
		private final ImageView readReceiptFileSeen;

		public SentFileViewHolder(View view) {
			super(view);
			sentFilename = view.findViewById(R.id.filename_rowItemAttachmentSent);
			sentCaption = view.findViewById(R.id.caption_rowItemAttachmentSent);
			sentCaptionTime = view.findViewById(R.id.time_rowItemAttachmentSent);
			progressBar = view.findViewById(R.id.progressBar_rowItemAttachmentSent);
			cancelUpload = view.findViewById(R.id.cancel_rowItemAttachmentSent);
			downloadSentFile = view.findViewById(R.id.download_rowItemAttachmentSent);
			readReceiptFileSending = view.findViewById(R.id.readReceiptSending_rowItemAttachmentSent);
			readReceiptFileSent = view.findViewById(R.id.readReceiptSent_rowItemAttachmentSent);
			readReceiptFileSeen = view.findViewById(R.id.readReceiptSeen_rowItemAttachmentSent);
		}

		public TextView getSentFilename() {
			return sentFilename;
		}
		public TextView getSentCaption() {
			return sentCaption;
		}
		public TextView getSentCaptionTime() {
			return sentCaptionTime;
		}
		public ProgressBar getProgressBar() {
			return progressBar;
		}
		public ImageView getCancelUpload() {
			return cancelUpload;
		}
		public ImageView getDownloadSentFile() {
			return downloadSentFile;
		}
		public ImageView getReadReceiptFileSending() {
			return readReceiptFileSending;
		}
		public ImageView getReadReceiptFileSent() {
			return readReceiptFileSent;
		}
		public ImageView getReadReceiptFileSeen() {
			return readReceiptFileSeen;
		}
	}

	public static class ReceivedFileViewHolder extends RecyclerView.ViewHolder {
		private final TextView receivedFilename;
		private final TextView receivedCaption;
		private final TextView receivedCaptionTime;
		private final ImageView downloadReceivedFile;

		public ReceivedFileViewHolder(View view) {
			super(view);
			receivedFilename = view.findViewById(R.id.filename_rowItemAttachmentReceived);
			receivedCaption = view.findViewById(R.id.caption_rowItemAttachmentReceived);
			receivedCaptionTime = view.findViewById(R.id.time_rowItemAttachmentReceived);
			downloadReceivedFile = view.findViewById(R.id.download_rowItemAttachmentReceived);
		}

		public TextView getReceivedFilename() {
			return receivedFilename;
		}
		public TextView getReceivedCaption() {
			return receivedCaption;
		}
		public TextView getReceivedCaptionTime() {
			return receivedCaptionTime;
		}
		public ImageView getDownloadReceivedFile() {
			return downloadReceivedFile;
		}
	}

	public static class BlankViewHolder extends RecyclerView.ViewHolder {
		public BlankViewHolder(View view) {
			super(view);
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
		else if(viewType == 1) {
			View view = LayoutInflater.from(viewGroup.getContext())
					.inflate(R.layout.row_item_message_received, viewGroup, false);
			return new ReceivedViewHolder(view);
		}
		else if(viewType == 2) {
			View view = LayoutInflater.from(viewGroup.getContext())
					.inflate(R.layout.row_item_attachment_sent, viewGroup, false);
			return new SentFileViewHolder(view);
		}
		else if(viewType == 3) {
			View view = LayoutInflater.from(viewGroup.getContext())
					.inflate(R.layout.row_item_attachment_received, viewGroup, false);
			return new ReceivedFileViewHolder(view);
		}
		else {
			View view = LayoutInflater.from(viewGroup.getContext())
					.inflate(R.layout.row_item_blank, viewGroup, false);
			return new BlankViewHolder(view);
		}
	}

	@Override
	public int getItemViewType(int position) {
		// 0 --> The message is sent by the user.
		// 1 --> The message is received by the user.
		// 2 --> The attachment is sent by the user.
		// 3 --> The attachment is received by the user.
		// 4 --> The attachment is not received by the user Or if any of the above conditions is not met.

		try {
			if(localDataSet.getJSONObject(position).getString("senderid").equals(userid)) {
				if(localDataSet.getJSONObject(position).getString("filename").equals(""))
					return 0;
				return 2;
			}
			else {
				if(localDataSet.getJSONObject(position).getString("filename").equals(""))
					return 1;
				else if(localDataSet.getJSONObject(position).getString("isFileUploaded").equals("1"))
					return 3;
				return 4;
			}
		} catch (JSONException e) {
			Log.e(TAG, e.toString());
			return 4;
		}
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

		else if(viewHolder.getClass() == ReceivedViewHolder.class) {
			ReceivedViewHolder vHolder = (ReceivedViewHolder) viewHolder;
			try {
				vHolder.getReceivedMsg().setText(localDataSet.getJSONObject(position).getString("message"));
				vHolder.getReceivedMsgTime().setText(localDataSet.getJSONObject(position).getString("dateTime").substring(11, 16));
			} catch (JSONException e) {
				Log.e(TAG, e.toString());
			}
		}

		else if(viewHolder.getClass() == SentFileViewHolder.class) {
			SentFileViewHolder vHolder = (SentFileViewHolder) viewHolder;
			try {
				vHolder.getSentFilename().setText(localDataSet.getJSONObject(position).getString("filename"));
				vHolder.getSentCaption().setText(localDataSet.getJSONObject(position).getString("message"));
				vHolder.getSentCaptionTime().setText(localDataSet.getJSONObject(position).getString("dateTime").substring(11, 16));
				if(localDataSet.getJSONObject(position).getString("isFileUploaded").equals("1")) {
					vHolder.getProgressBar().setVisibility(View.GONE);
					vHolder.getCancelUpload().setVisibility(View.GONE);
					vHolder.getDownloadSentFile().setVisibility(View.VISIBLE);
					readReceipt = localDataSet.getJSONObject(position).getString("isMsgSeen");
					if(readReceipt.equals("1")) {
						vHolder.getReadReceiptFileSending().setVisibility(View.GONE);
						vHolder.getReadReceiptFileSent().setVisibility(View.GONE);
						vHolder.getReadReceiptFileSeen().setVisibility(View.VISIBLE);
					}
					else if(readReceipt.equals("0")) {
						vHolder.getReadReceiptFileSending().setVisibility(View.GONE);
						vHolder.getReadReceiptFileSeen().setVisibility(View.GONE);
						vHolder.getReadReceiptFileSent().setVisibility(View.VISIBLE);
					}
				}
				else {
					vHolder.getDownloadSentFile().setVisibility(View.GONE);
					vHolder.getProgressBar().setVisibility(View.VISIBLE);
					vHolder.getCancelUpload().setVisibility(View.VISIBLE);
					vHolder.getReadReceiptFileSent().setVisibility(View.GONE);
					vHolder.getReadReceiptFileSeen().setVisibility(View.GONE);
					vHolder.getReadReceiptFileSending().setVisibility(View.VISIBLE);
				}
			} catch (JSONException e) {
				Log.e(TAG, e.toString());
			}
		}

		else if(viewHolder.getClass() == ReceivedFileViewHolder.class) {
			ReceivedFileViewHolder vHolder = (ReceivedFileViewHolder) viewHolder;
			try {
				vHolder.getReceivedFilename().setText(localDataSet.getJSONObject(position).getString("filename"));
				vHolder.getReceivedCaption().setText(localDataSet.getJSONObject(position).getString("message"));
				vHolder.getReceivedCaptionTime().setText(localDataSet.getJSONObject(position).getString("dateTime").substring(11, 16));
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
