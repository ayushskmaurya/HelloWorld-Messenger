package com.messengerhelloworld.helloworld.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.messengerhelloworld.helloworld.R;
import com.messengerhelloworld.helloworld.utils.OtpVerification;
import com.messengerhelloworld.helloworld.utils.DatabaseOperations;
import com.messengerhelloworld.helloworld.interfaces.AfterStringResponseIsReceived;

import java.util.HashMap;

public class OtpRegisterActivity extends AppCompatActivity {
	private OtpVerification otpVerification;
	private TextView resendOtp;
	private final DatabaseOperations databaseOperations = new DatabaseOperations(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_otp_register);

		Intent intent1 = getIntent();
		String name = intent1.getStringExtra("registeredName");
		String mob = intent1.getStringExtra("registeredMob");

		resendOtp = findViewById(R.id.resend_otpActivity);
		startTimer(mob);

		// Sending OTP to the user.
		otpVerification = new OtpVerification(OtpRegisterActivity.this);
		otpVerification.sendOtp(mob);

		TextView textView = findViewById(R.id.text_otpActivity);
		String text = "The OTP has been sent to +91 " + mob + ". Please enter the OTP to complete the registration.";
		textView.setText(text);

		// Verifying the OTP entered by user.
		EditText enteredOtpView = findViewById(R.id.otp_otpActivity);
		Button verify = findViewById(R.id.verify_otpActivity);
		verify.setOnClickListener(v -> {
			String enteredOtp = enteredOtpView.getText().toString().trim();
			if(!enteredOtp.matches("^[0-9]{6}$"))
				Toast.makeText(this, "Please enter the valid OTP.", Toast.LENGTH_SHORT).show();
			else
				otpVerification.verifyOtp(enteredOtp, () -> {
					HashMap<String, String> data = new HashMap<>();
					data.put("table_name", "users");
					data.put("name", name);
					data.put("mobile_no", mob);
					data.put("password", intent1.getStringExtra("registeredPasswordHash"));

					databaseOperations.insert(data, new AfterStringResponseIsReceived() {
						@Override
						public void executeAfterResponse(String response) {
							SharedPreferences sp = getSharedPreferences("HelloWorldSharedPref", Context.MODE_PRIVATE);
							SharedPreferences.Editor ed = sp.edit();
							ed.putString("userId", response);
							ed.putString("userName", name);
							ed.putString("userMob", mob);
							ed.apply();

							intent1.removeExtra("registeredMob");
							intent1.removeExtra("registeredName");
							intent1.removeExtra("registeredPasswordHash");

							Intent intent2 = new Intent(OtpRegisterActivity.this, MainActivity.class);
							intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
							startActivity(intent2);
						}

						@Override
						public void executeAfterErrorResponse() {
							Toast.makeText(OtpRegisterActivity.this, "Sorry! Something went wrong.", Toast.LENGTH_SHORT).show();
						}
					});
				});
		});
	}

	private void startTimer(String mob) {
		new CountDownTimer(60000, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				String resendOtpText = "Resend OTP in " + (millisUntilFinished / 1000) + " seconds.";
				resendOtp.setText(resendOtpText);
			}

			@Override
			public void onFinish() {
				resendOtp.setTextColor(ContextCompat.getColor(OtpRegisterActivity.this, R.color.black));
				resendOtp.setTypeface(null, Typeface.BOLD);
				String resendOtpText = "Resend OTP";
				resendOtp.setText(resendOtpText);
				resendOtp.setOnClickListener(v -> {
					resendOtp.setText("");
					resendOtp.setTextColor(ContextCompat.getColor(OtpRegisterActivity.this, R.color.black1));
					resendOtp.setTypeface(null, Typeface.NORMAL);
					startTimer(mob);
					otpVerification.sendOtp(mob);
				});
			}
		}.start();
	}
}