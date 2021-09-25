package com.messengerhelloworld.helloworld.ui.chats;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.messengerhelloworld.helloworld.R;
import com.messengerhelloworld.helloworld.adapters.ChatsAdapter;
import com.messengerhelloworld.helloworld.interfaces.AfterJsonArrayResponseIsReceived;
import com.messengerhelloworld.helloworld.utils.DatabaseOperations;

import org.json.JSONArray;

import java.util.HashMap;

public class ChatsFragment extends Fragment {
	private Context context;
	private DatabaseOperations databaseOperations;
	private RecyclerView chatsRecyclerView;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		this.context = context;
		databaseOperations = new DatabaseOperations((Activity) context);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
							 @Nullable Bundle savedInstanceState) {

		View chatsLayout = inflater.inflate(R.layout.fragment_chats, container, false);

		SharedPreferences sp = getActivity().getSharedPreferences("HelloWorldSharedPref", Context.MODE_PRIVATE);
		HashMap<String, String> data = new HashMap<>();
		data.put("userid", sp.getString("userId", null));

		databaseOperations.retrieveChats(data, new AfterJsonArrayResponseIsReceived() {
			@Override
			public void executeAfterResponse(JSONArray response) {
				chatsRecyclerView = chatsLayout.findViewById(R.id.chatsRecyclerView);
				chatsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
				ChatsAdapter chatsAdapter = new ChatsAdapter(response, context);
				chatsRecyclerView.setAdapter(chatsAdapter);
			}

			@Override
			public void executeAfterErrorResponse() {
				Toast.makeText(context, "Sorry! Something went wrong.", Toast.LENGTH_SHORT).show();
			}
		});
		return chatsLayout;
	}
}
