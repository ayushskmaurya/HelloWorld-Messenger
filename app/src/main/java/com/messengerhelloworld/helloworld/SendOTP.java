package com.messengerhelloworld.helloworld;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SendOTP {
	private final Activity activity;
	private String verification_id;

	public SendOTP(Activity activity) {
		this.activity = activity;
		this.verification_id = null;
	}

	public void sendOtp(String mob) {
		FirebaseAuth mAuth = FirebaseAuth.getInstance();
		PhoneAuthOptions options =
			PhoneAuthOptions.newBuilder(mAuth)
				.setPhoneNumber("+91" + mob)
				.setTimeout(60L, TimeUnit.SECONDS)
				.setActivity(activity)
				.setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

					@Override
					public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
						Log.d("HelloWorld", "Verification Completed");
					}

					@Override
					public void onVerificationFailed(@NonNull FirebaseException e) {
						Log.d("HelloWorld", "Verification Failed");
					}

					@Override
					public void onCodeSent(@NonNull String verificationId,
										   @NonNull PhoneAuthProvider.ForceResendingToken token) {
						verification_id = verificationId;
						Log.d("HelloWorld", "Code Sent");
					}
				})
				.build();
		PhoneAuthProvider.verifyPhoneNumber(options);
	}

	public String getVerificationId() {
		return verification_id;
	}
}
