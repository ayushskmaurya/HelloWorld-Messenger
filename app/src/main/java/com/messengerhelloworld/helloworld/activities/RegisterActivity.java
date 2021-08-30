package com.messengerhelloworld.helloworld.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.messengerhelloworld.helloworld.R;
import com.messengerhelloworld.helloworld.utils.SHA256Hash;

public class RegisterActivity extends AppCompatActivity {

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
				SHA256Hash sha256Hash = new SHA256Hash();
				try {
					String hash = sha256Hash.getHash(pwd);
					Intent intent = new Intent(this, OtpRegisterActivity.class);
					intent.putExtra("registeredName", name);
					intent.putExtra("registeredMob", mob);
					intent.putExtra("registeredPasswordHash", hash);
					startActivity(intent);
				}
				catch (Exception e) {
					Toast.makeText(this, "Error occurred.", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}