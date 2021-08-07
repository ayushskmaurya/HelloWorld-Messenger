package com.helloworldmessenger.helloworld.ui.chats;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.helloworldmessenger.helloworld.R;

public class ChatsFragment extends Fragment {

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
							 @Nullable Bundle savedInstanceState) {

		View chatsLayout = inflater.inflate(R.layout.fragment_chats, container, false);
		TextView chatsText = chatsLayout.findViewById(R.id.chatsText);
		String text = "Chats will be displayed here...";
		chatsText.setText(text);
		return chatsLayout;
	}
}
