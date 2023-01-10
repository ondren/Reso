package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import api.ApiAccess;

public class MessagesActivity extends Activity {
    LinearLayout layout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages_activity);
        layout = findViewById(R.id.messages_activity_id);

        Button back_btn = findViewById(R.id.back_btn);
        System.out.println("Messages"+"Activity " + " is working");



        back_btn.setOnClickListener(view -> {
            finish();
        });

        redraw();

    }

    void redraw(){
        ApiAccess.setContext(this);
        ApiAccess.get("users/messages",
                response -> {
                    try {
                        JSONArray JSONArray_messages = response.getJSONArray("messages");
                        for (int i = 0; i < JSONArray_messages.length(); i++){
                            final Button message_btn = new Button(this);
                            message_btn.setLayoutParams(new LinearLayout.LayoutParams(350, 150));
                            message_btn.setId(i);
                            JSONObject message = JSONArray_messages.getJSONObject(i);
                            String message_text = message.getString("message");
                            String message_code = message.getString("code");
                            System.out.println("message code is " + message_code);
                            System.out.println("message text is  " + message_text);
                            message_btn.setText(message_text);
                            message_btn.setGravity(Gravity.CENTER);
                            layout.addView(message_btn);
                            message_btn.setOnClickListener(view -> {

                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });

    }
}
