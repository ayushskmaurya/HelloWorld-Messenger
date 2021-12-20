package com.messengerhelloworld.helloworld.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.messengerhelloworld.helloworld.R;
import com.messengerhelloworld.helloworld.utils.Base;
import com.messengerhelloworld.helloworld.utils.ShouldSync;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ProfileImageActivity extends AppCompatActivity {
	private static final String TAG = "hwmLogProfileImage";
	private static SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ShouldSync.setShouldSyncChats(false);

		sp = getSharedPreferences("HelloWorldSharedPref", Context.MODE_PRIVATE);
		if(sp.getString("HelloWorldUserProfilePhoto", null).equals("null"))
			selectImage();
		else
			setContentView(R.layout.activity_profile_image);
	}

	// Selecting an image to set as profile photo.
	private void selectImage() {
		Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
		intent.setType("image/*");
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && resultCode == RESULT_OK) {
			Uri filepath = data.getData();
			InputStream inputStream;
			try {
				inputStream = getContentResolver().openInputStream(filepath);
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

				int imageSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
				Bitmap croppedImage = Bitmap.createBitmap(bitmap, 0, 0, imageSize, imageSize);
				Bitmap scaledImage = Bitmap.createScaledBitmap(croppedImage, 60, 60, true);

				encodeProfilePhoto(sp.getString("HelloWorldUserId", null), croppedImage, scaledImage);
			} catch (FileNotFoundException e) {
				Log.e(TAG, e.toString());
				Toast.makeText(this, "Unable to set profile photo, please try again.", Toast.LENGTH_SHORT).show();
			} finally {
				onBackPressed();
			}
		}
	}

	// Encoding profile image to String.
	private void encodeProfilePhoto(String userid, Bitmap croppedImage, Bitmap scaledImage) {
		ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream();
		ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
		croppedImage.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream1);
		scaledImage.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream2);

		byte[] bytesOfFile1 = byteArrayOutputStream1.toByteArray();
		byte[] bytesOfFile2 = byteArrayOutputStream2.toByteArray();
		String encodedCroppedImage = android.util.Base64.encodeToString(bytesOfFile1, Base64.DEFAULT);
		String encodedScaledImage = android.util.Base64.encodeToString(bytesOfFile2, Base64.DEFAULT);

		Map<String, String> postData = new HashMap<>();
		postData.put("userid", userid);
		postData.put("profile_image", encodedCroppedImage);
		postData.put("reduced_profile_image", encodedScaledImage);
		uploadProfilePhotoToServer(postData);
	}

	// Uploading profile photo to the server.
	private void uploadProfilePhotoToServer(Map<String, String> postData) {
		Volley.newRequestQueue(this).add(
				new StringRequest(
						Request.Method.POST,
						Base.getBASE_URL() + "/saveProfileImage.php",
						response -> Log.d(TAG, response),
						error -> {
							Log.e(TAG, error.toString());
							Toast.makeText(this, "Unable to set profile photo, please try again.", Toast.LENGTH_SHORT).show();
						}
				) {
					@Override
					protected Map<String, String> getParams() {
						return postData;
					}
				}
		);
	}

	@Override
	public void onBackPressed() {
		NavUtils.navigateUpFromSameTask(this);
	}
}