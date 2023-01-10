package com.example.myapplication;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import org.json.JSONObject;

import api.ApiAccess;


public class MainActivity extends Activity {

//    String CHANNEL_ID = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_test_activity);
        ApiAccess.setContext(this);

        String token = ApiAccess.getToken();

        if(token.equals("")){
            System.out.println("no token");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            JSONObject jsonBody = new JSONObject();
            ApiAccess.get("users/get",
                    response ->{
                        Intent intent = new Intent(this, HubActivity.class);
                        startActivity(intent);
                        finish();

                    },
                    error -> {
                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    );
        }



//            private void createNotificationChannel() {
//            // Create the NotificationChannel, but only on API 26+ because
//            // the NotificationChannel class is new and not in the support library
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                CharSequence name = getString(R.string.app_name);
//                String description = getString(R.string.app_name);
//                int importance = NotificationManager.IMPORTANCE_DEFAULT;
//                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "notification", importance);
//                channel.setDescription(description);
//                // Register the channel with the system; you can't change the importance
//                // or other notification behaviors after this
//                NotificationManager notificationManager = getSystemService(NotificationManager.class);
//                notificationManager.createNotificationChannel(channel);
//            }





    }
}