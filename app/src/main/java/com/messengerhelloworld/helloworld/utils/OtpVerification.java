package com.messengerhelloworld.helloworld.utils;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import com.messengerhelloworld.helloworld.interfaces.AfterSuccessfulOtpVerification;

public class OtpVerification {
	private static final String TAG = "hwOtpVerification";
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
						Log.i(TAG, "OTP Verification Completed");
					}

					@Override
					public void onVerificationFailed(@NonNull FirebaseException e) {
						Log.i(TAG, "OTP Verification Failed");
					}

					@Override
					public void onCodeSent(@NonNull String verificationId,
										   @NonNull PhoneAuthProvider.ForceResendingToken token) {
						verification_id = verificationId;
						Log.i(TAG, "OTP is sent to the user successfully");
					}
				})
				.build();
		PhoneAuthProvider.verifyPhoneNumber(options);
	}

	public void verifyOtp(String enteredOtp, AfterSuccessfulOtpVerification afterSuccessfulOtpVerification, Button button, ProgressBar progressBar) {
		if(verification_id != null) {
			button.setEnabled(false);
			progressBar.setVisibility(View.VISIBLE);
			PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification_id, enteredOtp);
			mAuth.signInWithCredential(credential)
				.addOnCompleteListener(activity, task -> {
					if(task.isSuccessful())
						afterSuccessfulOtpVerification.execute();
					else
						Toast.makeText(activity, "You have entered Wrong OTP.", Toast.LENGTH_SHORT).show();
					progressBar.setVisibility(View.GONE);
					button.setEnabled(true);
				});
		}
		else
			Toast.makeText(activity, "Please enter the valid OTP.", Toast.LENGTH_SHORT).show();
	}
}
