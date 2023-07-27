package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.databinding.LoginActivityBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Messages.MessageConfirmActivity;
import Messages.MessagesActivity;
import api.ApiAccess;

public class LoginActivity extends Activity {
    SharedPreferences sp;
    LoginActivityBinding binding;

    


    public void onLoginClick() {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("login", binding.loginEt.getText().toString());
            jsonBody.put("password", binding.passwordEt.getText().toString());

            ApiAccess.setContext(LoginActivity.this);
            String str = ApiAccess.getFirebaseToken();
            jsonBody.put("token", str);

        } catch (Exception ex){

        }



        ApiAccess.setContext(LoginActivity.this);

        ApiAccess.post("auth/login", jsonBody,
            result -> {
                try {
                    binding.loginInterface.setVisibility(View.INVISIBLE);
                    binding.placesList.setVisibility(View.VISIBLE);

                    binding.placesListContainer.removeAllViews();
                    String token = result.getString("token");
                    String place = result.getString("place");

                    JSONArray places = result.getJSONArray("places");
                    if(places.length() > 1){
                        for (int i = 0; i < places.length(); i++){
                            final Button message_btn = new Button(LoginActivity.this);
                            message_btn.setLayoutParams(new LinearLayout.LayoutParams(350, 150));
                            message_btn.setId(i);
                            message_btn.setMinHeight(60);
                            message_btn.setLayoutParams(new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            ));
                            JSONObject _place = places.getJSONObject(i);
                            String place_name = _place.getString("name");
                            String place_code = _place.getString("code");

                            message_btn.setText(place_name);
                            message_btn.setGravity(Gravity.CENTER);
                            message_btn.setOnClickListener(v -> {
                                setDataAndRedirect(token, place_code);
                            });
                            binding.placesListContainer.addView(message_btn);
                        }
                    } else {
                        setDataAndRedirect(token, place);
                    }


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
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LoginActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        RequestQueue queue = Volley.newRequestQueue(this);
        sp = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        initButtons();
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

    void initButtons(){
        binding.goButton.setOnClickListener((v) -> {onLoginClick();});
        binding.placesBackBtn.setOnClickListener((l)->{
            binding.loginInterface.setVisibility(View.VISIBLE);
            binding.placesList.setVisibility(View.INVISIBLE);
        });
    }

    void setDataAndRedirect(String token, String place){
        ApiAccess.setAutoData(token, place);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("place",place);
        editor.putString("token",token);
        editor.putString("phone_number", binding.loginEt.getText().toString());
        editor.commit();
        ApiAccess.setAutoData(token, place);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }



}
