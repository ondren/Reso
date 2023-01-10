package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import api.ApiAccess;

public class LoginActivity extends Activity {
    EditText login_et;
    EditText password_et;
    SharedPreferences sp;

    


    public View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.go_button){
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("login", login_et.getText().toString());
                    jsonBody.put("password", password_et.getText().toString());

                    ApiAccess.setContext(LoginActivity.this);
                    String str = ApiAccess.getFirebaseToken();
                    jsonBody.put("token", str);

                } catch (Exception ex){

                }



                ApiAccess.setContext(LoginActivity.this);

                ApiAccess.post("auth/login", jsonBody,
                        result -> {
                            try {
                                String token = result.getString("token");
                                String place = result.getString("place");
                                ApiAccess.setAutoData(token, place);

                                //Saving token in app memory:

                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("place",place);
                                editor.putString("token",token);
                                editor.putString("phone_number", login_et.getText().toString());
                                editor.commit();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> {

                            String response = error.networkResponse != null ? new String(error.networkResponse.data) : null;
                            try {
                                String message = response == null ? error.getMessage() : (new JSONObject(response)).getString("error");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                );
            };


        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        System.out.println("Login"+"Activity " + " is working");
        RequestQueue queue = Volley.newRequestQueue(this);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        this.setContentView(binding.getRoot());
        Button but = findViewById(R.id.go_button);
        but.setOnClickListener(onClick);
        login_et = findViewById(R.id.login_et);
        password_et = findViewById(R.id.password_et);
        sp = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            System.out.println("Fetching FCM registration token failed");
                            return;
                        }

                        // Get new FCM registration token
                        String firebase_token = task.getResult();
                        ApiAccess.setContext(LoginActivity.this);
                        ApiAccess.setFirebaseToken(firebase_token);



                    }
                });

    }



}
