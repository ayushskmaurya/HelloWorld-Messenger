package com.messengerhelloworld.helloworld;

import android.app.Activity;
import android.content.Intent;

public class AfterOtpIsVerified {
	private final Activity activity;

	public AfterOtpIsVerified(Activity activity) {
		this.activity = activity;
	}

	public void register() {
		Intent intent2 = new Intent(activity, MainActivity.class);
		intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		activity.startActivity(intent2);
	}

	public void whatToDoAfterVerificationSuccess(String whatToDo) {
		if(whatToDo.equals("register"))
			register();
	}
}
