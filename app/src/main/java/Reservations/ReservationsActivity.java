package Reservations;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityReservationsBinding;

import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Messages.MessageConfirmActivity;
import Messages.MessagesActivity;
import api.ApiAccess;

public class ReservationsActivity extends AppCompatActivity {

    private ActivityReservationsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityReservationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backBtn.setOnClickListener((l)->finish());

        redraw();
    }

    void redraw(){
        ApiAccess.setContext(this);
        SharedPreferences sp = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String place = sp.getString("place", "edr100");
        System.out.println("Redrawing...");
        ApiAccess.get("reservations/"+place,
            response -> {
                try {
                    JSONArray JSONArray_messages = response.getJSONArray("reservations");
                    System.out.println(JSONArray_messages.length() + " this length");
                    for (int i = 0; i < JSONArray_messages.length(); i++){
                        final Button message_btn = new Button(this);
                        message_btn.setLayoutParams(new LinearLayout.LayoutParams(350, 150));
                        message_btn.setId(i);
                        message_btn.setMinHeight(60);
                        message_btn.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
                        message_btn.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
                        JSONObject message = JSONArray_messages.getJSONObject(i);
                        String name = message.getString("name");
                        message_btn.setText(name);
                        message_btn.setGravity(Gravity.CENTER);
                        message_btn.setOnClickListener(view -> {
//                            Intent intent = new Intent(ReservationsActivity.this, MessageConfirmActivity.class);
//                            intent.putExtra("text", message_text);
//                            intent.putExtra("title", message_title.equals("null") ? "":message_title);
//                            intent.putExtra("date", message_date);
//                            intent.putExtra("code", message_code);
//
//                            startActivityForResult(intent, 202);

                        });
                        binding.reservationsList.addView(message_btn);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });

    }
}