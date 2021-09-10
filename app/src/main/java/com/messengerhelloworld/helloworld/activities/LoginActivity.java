package com.messengerhelloworld.helloworld.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.messengerhelloworld.helloworld.R;
import com.messengerhelloworld.helloworld.interfaces.AfterJsonArrayResponseIsReceived;
import com.messengerhelloworld.helloworld.utils.DatabaseOperations;
import com.messengerhelloworld.helloworld.utils.SHA256Hash;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
	private final DatabaseOperations databaseOperations = new DatabaseOperations(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		TextView mobView = findViewById(R.id.mob_activityLogin);
		TextView pwdView = findViewById(R.id.pwd_activityLogin);

		Button login = findViewById(R.id.login_activityLogin);
		login.setOnClickListener(v -> {
			String mob = mobView.getText().toString().trim();
			String pwd = pwdView.getText().toString();

			if(!mob.matches("^[0-9]{10}$"))
				Toast.makeText(this, "Please enter valid Mobile No.", Toast.LENGTH_SHORT).show();
			else if(pwd.length() == 0)
				Toast.makeText(this, "Please enter the Password.", Toast.LENGTH_SHORT).show();
			else {
				HashMap<String, String> data = new HashMap<>();
				data.put("columns", "userid, password");
				data.put("table_name", "users");
				data.put("WHERE", "mobile_no='" + mob + "'");

				databaseOperations.retrieve(data, new AfterJsonArrayResponseIsReceived() {
					@Override
					public void executeAfterResponse(JSONArray response) {
						try {
							JSONObject user = response.getJSONObject(0);
							try {
								String hash = new SHA256Hash().getHash(pwd);
								if(hash.equals(user.getString("password"))) {
									SharedPreferences sp = getSharedPreferences("HelloWorldSharedPref", Context.MODE_PRIVATE);
									SharedPreferences.Editor ed = sp.edit();
									ed.putString("userId", user.getString("userid"));
									ed.putString("userMob", mob);
									ed.apply();

									Intent intent = new Intent(LoginActivity.this, MainActivity.class);
									intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
									startActivity(intent);
								}

								else
									Toast.makeText(LoginActivity.this, "You have entered incorrect password.", Toast.LENGTH_SHORT).show();
							} catch (NoSuchAlgorithmException e) {
								Toast.makeText(LoginActivity.this, "Sorry! Something went wrong.", Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							Toast.makeText(LoginActivity.this, "Account with mobile no +91 " + mob + " doesn't exist.", Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void executeAfterErrorResponse() {
						Toast.makeText(LoginActivity.this, "Sorry! Something went wrong.", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});

		Button register = findViewById(R.id.reg_activityLogin);
		register.setOnClickListener(v -> {
			Intent intent = new Intent(this, RegisterActivity.class);
			startActivity(intent);
		});
	}
}