package com.messengerhelloworld.helloworld.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.messengerhelloworld.helloworld.R;
import com.messengerhelloworld.helloworld.interfaces.AfterJsonArrayResponseIsReceived;
import com.messengerhelloworld.helloworld.utils.DatabaseOperations;
import com.messengerhelloworld.helloworld.utils.SHA256Hash;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
	private final DatabaseOperations databaseOperations = new DatabaseOperations(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		EditText nameView = findViewById(R.id.name_activityRegister);
		EditText mobView = findViewById(R.id.mob_activityRegister);
		EditText pwdView = findViewById(R.id.pwd_activityRegister);
		EditText cpwdView = findViewById(R.id.cpwd_activityRegister);
		Button register = findViewById(R.id.reg_activityRegister);

		register.setOnClickListener(v -> {
			String name = nameView.getText().toString().trim();
			String mob = mobView.getText().toString().trim();
			String pwd = pwdView.getText().toString();
			String cpwd = cpwdView.getText().toString();

			if(name.length() == 0)
				Toast.makeText(this, "Please enter the valid Name.", Toast.LENGTH_SHORT).show();
			else if(!mob.matches("^[0-9]{10}$"))
				Toast.makeText(this, "Please enter valid Mobile No.", Toast.LENGTH_SHORT).show();
			else if(pwd.length() == 0)
				Toast.makeText(this, "Please enter the Password.", Toast.LENGTH_SHORT).show();
			else if(!pwd.equals(cpwd))
				Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
			else {
				HashMap<String, String> data = new HashMap<>();
				data.put("columns", "mobile_no");
				data.put("table_name", "users");
				data.put("WHERE", "mobile_no='" + mob + "'");

				databaseOperations.retrieve(data, new AfterJsonArrayResponseIsReceived() {
					@Override
					public void executeAfterResponse(JSONArray response) {
						try {
							response.getJSONObject(0);
							Toast.makeText(RegisterActivity.this, "Account with mobile no +91 " + mob + " already exists.", Toast.LENGTH_SHORT).show();
						} catch (JSONException je) {
							SHA256Hash sha256Hash = new SHA256Hash();
							try {
								String hash = sha256Hash.getHash(pwd);
								Intent intent = new Intent(RegisterActivity.this, OtpRegisterActivity.class);
								intent.putExtra("registeredName", name);
								intent.putExtra("registeredMob", mob);
								intent.putExtra("registeredPasswordHash", hash);
								startActivity(intent);
							} catch (Exception e) {
								Toast.makeText(RegisterActivity.this, "Sorry! Something went wrong.", Toast.LENGTH_SHORT).show();
							}
						}
					}

					@Override
					public void executeAfterErrorResponse() {
						Toast.makeText(RegisterActivity.this, "Sorry! Something went wrong.", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}
}