package Messages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.databinding.MessageConfirmActivityBinding;

import org.json.JSONException;

import java.util.HashMap;

import api.ApiAccess;

public class MessageConfirmActivity extends Activity {

    MessageConfirmActivityBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = MessageConfirmActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String message_title = getIntent().getStringExtra("title");
        String message_text = getIntent().getStringExtra("text");
        String message_date = getIntent().getStringExtra("date");
        String message_code = getIntent().getStringExtra("code");

        binding.messageDate.setText(message_date);
        binding.messageText.setText(message_text);
        binding.messageTitle.setText(message_title);

        binding.backBtn.setOnClickListener((l)->{
            Intent intent = getIntent();
            setResult(RESULT_CANCELED, intent);
            finish();
        });

        binding.confirmBtn.setOnClickListener((l)->{
            ApiAccess.setContext(this);
            HashMap<String, String> params = new HashMap<>();
            params.put("code", message_code);
            ApiAccess.get("messages/confirm", params,
                response -> {
                    try {
                        String result = response.getString("message");
                        System.out.println(result);
                        setResult(RESULT_OK);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
                    error.printStackTrace();
                    setResult(RESULT_CANCELED);
                    finish();
                });
        });
    }
}
