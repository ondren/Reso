package Messages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.databinding.MessagesActivityBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import api.ApiAccess;

public class MessagesActivity extends Activity {
    MessagesActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MessagesActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backBtn.setOnClickListener((l)->finish());

        redraw();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String message_code = data.getStringExtra("message_code");
        if (data == null) {return;}
        if (resultCode == RESULT_OK && requestCode == 202){
            System.out.println("message_code in MessagesActivity is: " + message_code);

            ApiAccess.setContext(this);
            HashMap<String, String> params = new HashMap<>();
            params.put("code", message_code);
            ApiAccess.get("users/messages/confirm", params,
                    response -> {

                        try {
                            String result = response.getString("message");

                            System.out.println(result);
                            setResult(RESULT_OK, data);

                            System.out.println("Result ok ");
                            binding.messagesActivityId.removeAllViews();
                            redraw();

                            System.out.println(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    });


        }

    }

    void redraw(){
        ApiAccess.setContext(this);
        System.out.println("Redrawing...");
        ApiAccess.get("users/messages",
            response -> {
                try {
                    JSONArray JSONArray_messages = response.getJSONArray("messages");
                    System.out.println(JSONArray_messages.length() + " this length");
                    for (int i = 0; i < JSONArray_messages.length(); i++){
                        final Button message_btn = new Button(this);
                        message_btn.setLayoutParams(new LinearLayout.LayoutParams(350, 150));
                        message_btn.setId(i);
                        message_btn.setMinHeight(60);

                        message_btn.setLayoutParams(new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        ));
                        JSONObject message = JSONArray_messages.getJSONObject(i);
                        String message_text = message.getString("message");
                        String message_title = message.getString("title");
                        String message_code = message.getString("code");
                        String message_date = message.getString("created_at");

                        String txt = message_title.equals("null") ? message_text : message_title;
                        message_btn.setText(message_text);
                        message_btn.setGravity(Gravity.CENTER);
                        message_btn.setOnClickListener(view -> {
                            Intent intent = new Intent(MessagesActivity.this, MessageConfirmActivity.class);
                            intent.putExtra("text", message_text);
                            intent.putExtra("title", message_title.equals("null") ? "":message_title);
                            intent.putExtra("date", message_date);
                            intent.putExtra("code", message_code);

                            startActivityForResult(intent, 202);

                        });
                        binding.messagesActivityId.addView(message_btn);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });

    }
}
