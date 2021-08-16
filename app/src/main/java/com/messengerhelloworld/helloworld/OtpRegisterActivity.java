package com.messengerhelloworld.helloworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class OtpRegisterActivity extends AppCompatActivity {
	private OtpVerification otpVerification;
	private TextView resendOtp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_otp_register);

		Intent intent = getIntent();
		String mob = intent.getStringExtra("registeredMob");

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
			if(!isNumeric(enteredOtp) || enteredOtp.length() != 6)
				Toast.makeText(this, "Please enter the valid OTP.", Toast.LENGTH_SHORT).show();
			else
				otpVerification.verifyOtp(enteredOtp, MainActivity.class);
		});

//		String name = intent.getStringExtra("registeredName");
//		String hash = intent.getStringExtra("registeredPasswordHash");
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

	public static boolean isNumeric(String num) {
		try {
			Double.parseDouble(num);
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
}