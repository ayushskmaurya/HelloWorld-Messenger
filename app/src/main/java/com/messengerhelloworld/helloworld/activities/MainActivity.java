package com.messengerhelloworld.helloworld.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.messengerhelloworld.helloworld.databinding.ActivityMainBinding;
import com.messengerhelloworld.helloworld.R;
import com.messengerhelloworld.helloworld.utils.Base;
import com.messengerhelloworld.helloworld.utils.DatabaseHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private static final String TAG = "hwMainActivity";
    private final DatabaseHandler databaseHandler = new DatabaseHandler(this, "helloworld", null, 1);
    private final Handler handler = new Handler();
    private static boolean shouldUploadAttachments = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = getSharedPreferences("HelloWorldSharedPref", Context.MODE_PRIVATE);
        if(sp.getString("HelloWorldUserId", null) == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else {

            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            setSupportActionBar(binding.appBarMain.toolbar);
            DrawerLayout drawer = binding.drawerLayout;
            NavigationView navigationView = binding.navView;

            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_chats, R.id.nav_contacts)
                    .setDrawerLayout(drawer)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);

            View headerView = navigationView.getHeaderView(0);
            TextView userName = headerView.findViewById(R.id.userName);
            TextView userMobileNo = headerView.findViewById(R.id.userMobileNo);
            userName.setText(sp.getString("HelloWorldUserName", null));
            userMobileNo.setText(sp.getString("HelloWorldUserMobileNo", null));

            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.menu_logout).setOnMenuItemClickListener(item -> {
                sp.edit().clear().commit();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            });

            // Permissions
            String[] permissionsRequired = {
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            };
            ArrayList<String> permissions = new ArrayList<>();
            for(String permission : permissionsRequired)
                if(ContextCompat.checkSelfPermission(this,
                        permission) != PackageManager.PERMISSION_GRANTED)
                    permissions.add(permission);
            if(permissions.size() > 0)
                ActivityCompat.requestPermissions(this,
                        permissions.toArray(new String[permissions.size()]), 1);

            // Starting to upload all the pending attachments to server.
            if(shouldUploadAttachments) {
                shouldUploadAttachments = false;
                uploadAttachmentsToServer();
            }
        }
    }

    // Uploading all the pending attachments to server.
    public void uploadAttachmentsToServer() {
        if(databaseHandler.getAttachmentsCount() > 0) {
            Map<String, String> row = databaseHandler.getAttachment();
            Uri filepath = Uri.parse(row.get("filepath"));

            InputStream inputStream;
			try {
				inputStream = getContentResolver().openInputStream(filepath);
				int noOfBytesRead;
				byte[] buffer = new byte[10240];
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

				while((noOfBytesRead = inputStream.read(buffer)) != -1)
					byteArrayOutputStream.write(buffer, 0, noOfBytesRead);

				byte[] bytesOfFile = byteArrayOutputStream.toByteArray();
				String encodedFileStr = android.util.Base64.encodeToString(bytesOfFile, Base64.DEFAULT);

				Map<String, String> postData = new HashMap<>();
                postData.put("whatToDo", "saveAttachedFile");
                postData.put("msgid", row.get("msgid"));
				postData.put("temp_filename", row.get("temp_filename"));
				postData.put("encoded_file_str", encodedFileStr);
				uploadFile(postData);

			} catch (IOException e) {
                Log.e(TAG, e.toString());
			}
        }
        else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    uploadAttachmentsToServer();
                }
            }, 3000);
        }
    }

    private void uploadFile(Map<String, String> postData) {
        Volley.newRequestQueue(this).add(
                new StringRequest(
                        Request.Method.POST,
                        Base.getBASE_URL() + "/manageAttachment.php",
                        response -> {
                            if(response.equals("1"))
                                databaseHandler.deleteAttachment(postData.get("msgid"));
                            uploadAttachmentsToServer();
                        },
                        error -> {
                            Log.e(TAG, error.toString());
                            uploadAttachmentsToServer();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}