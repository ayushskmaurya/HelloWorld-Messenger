package com.helloworldmessenger.helloworld.ui.contacts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.helloworldmessenger.helloworld.R;

public class ContactsFragment extends Fragment {

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
							 @Nullable Bundle savedInstanceState) {

		View contactsLayout = inflater.inflate(R.layout.fragment_contacts, container, false);
		TextView contactsText = contactsLayout.findViewById(R.id.contactsText);
		String text = "Contacts will be displayed here...";
		contactsText.setText(text);
		return contactsLayout;
	}
}
