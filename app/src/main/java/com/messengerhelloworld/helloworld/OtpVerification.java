package com.messengerhelloworld.helloworld;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OtpVerification {
	private final FirebaseAuth mAuth;
	private final Activity activity;
	private String verification_id;

	public OtpVerification(Activity activity) {
		this.mAuth = FirebaseAuth.getInstance();
		this.activity = activity;
		this.verification_id = null;
	}

	public void sendOtp(String mob) {
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

	public void verifyOtp(String enteredOtp, Class<?> destClass) {
		if(verification_id != null) {
			PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification_id, enteredOtp);
			mAuth.signInWithCredential(credential)
				.addOnCompleteListener(activity, task -> {
					if(task.isSuccessful()) {
						Intent intent = new Intent(activity, destClass);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
						activity.startActivity(intent);
					}
					else
						Toast.makeText(activity, "You have entered Wrong OTP.", Toast.LENGTH_SHORT).show();
				});
		}
		else
			Toast.makeText(activity, "Please enter the valid OTP.", Toast.LENGTH_SHORT).show();
	}
}
