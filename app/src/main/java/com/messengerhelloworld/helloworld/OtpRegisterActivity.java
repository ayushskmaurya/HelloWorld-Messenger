package com.messengerhelloworld.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OtpRegisterActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_otp_register);

		Intent intent = getIntent();
		String mob = intent.getStringExtra("registeredMob");

		// Sending OTP to the user.
		SendOTP sendOtp = new SendOTP(OtpRegisterActivity.this);
		sendOtp.sendOtp(mob);

		TextView textView = findViewById(R.id.text_otpActivity);
		String text = "The OTP has been sent to +91 " + mob + ". Please enter the OTP to complete the registration.";
		textView.setText(text);

		// Verifying the OTP entered by user.
		EditText enteredOtpView = findViewById(R.id.otp_otpActivity);
		Button verify = findViewById(R.id.verify_otpActivity);
		verify.setOnClickListener(v -> {
			String enteredOtp = enteredOtpView.getText().toString().trim();
			String verificationId = sendOtp.getVerificationId();

			if(!isNumeric(enteredOtp) || enteredOtp.length() != 6)
				Toast.makeText(this, "Please enter the valid OTP.", Toast.LENGTH_SHORT).show();
			else if(verificationId != null) {
				PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, enteredOtp);
				FirebaseAuth mAuth = FirebaseAuth.getInstance();
				mAuth.signInWithCredential(credential)
					.addOnCompleteListener(this, task -> {
						if(task.isSuccessful()) {
							Toast.makeText(this, "OTP verified Successfully.", Toast.LENGTH_SHORT).show();
						}
						else
							Toast.makeText(this, "You have entered Wrong OTP.", Toast.LENGTH_SHORT).show();
					});
			}
		});

//		String name = intent.getStringExtra("registeredName");
//		String hash = intent.getStringExtra("registeredPasswordHash");
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