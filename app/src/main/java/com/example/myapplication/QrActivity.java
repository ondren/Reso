package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import api.ApiAccess;

public class QrActivity extends Activity {
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_activity);
        layout=findViewById(R.id.qr_activity_id);
        System.out.println("QR"+"Activity " + " is working");

        Button back_but = findViewById(R.id.back_btn);
        back_but.setOnClickListener(view -> {
            finish();
        });

        ApiAccess.get("qr",
                response -> {
            try {
                JSONArray JSONArray_codes = response.getJSONArray("codes");
                for (int i = 0; i < JSONArray_codes.length(); i++) {
                    JSONObject j = JSONArray_codes.getJSONObject(i);
                    String code = j.getString("code");
                    System.out.println(code.toString());

                    final Button button = new Button(this);
                    button.setLayoutParams(new LinearLayout.LayoutParams(350, 150));
                    button.setId(i);
                    button.setHeight(80);
                    button.setText("QR " + i);

                    button.setOnClickListener(view -> {
                        Intent intent = new Intent(QrActivity.this, QRSettingsActivity.class);
                        intent.putExtra("code", code);
                        System.out.println(code);
                        startActivity(intent);

                    });

                    layout.addView(button);

                }

            }catch (JSONException e){
                e.printStackTrace();
            }

                },
                error -> {

                }
        );
    }
}
