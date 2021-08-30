package com.messengerhelloworld.helloworld.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.messengerhelloworld.helloworld.R;

public class LoginActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		Button register = findViewById(R.id.reg_activityLogin);
		register.setOnClickListener(v -> {
			Intent intent = new Intent(this, RegisterActivity.class);
			startActivity(intent);
		});
	}
}