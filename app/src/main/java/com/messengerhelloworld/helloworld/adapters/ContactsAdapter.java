package com.messengerhelloworld.helloworld.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.messengerhelloworld.helloworld.R;

import org.json.JSONArray;
import org.json.JSONException;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {
	private static final String TAG = "hwContactsAdapter";
	private final Context context;
	private final JSONArray localDataSet;

	public static class ViewHolder extends RecyclerView.ViewHolder {
		private final View contact;
		private final TextView userName;

		public ViewHolder(View view) {
			super(view);
			contact = view.findViewById(R.id.contact_rowItemContacts);
			userName = view.findViewById(R.id.userName_rowItemContacts);
		}

		public View getContact() {
			return contact;
		}
		public TextView getUserName() {
			return userName;
		}
	}

	public ContactsAdapter(Context context, JSONArray dataSet) {
		this.context = context;
		localDataSet = dataSet;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		View view = LayoutInflater.from(viewGroup.getContext())
				.inflate(R.layout.row_item_contacts, viewGroup, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, final int position) {
		try {
			viewHolder.getUserName().setText(localDataSet.getJSONObject(position).getString("name"));
		} catch (JSONException e) {
			Log.e(TAG, e.toString());
		}
	}

	@Override
	public int getItemCount() {
		return localDataSet.length();
	}
}