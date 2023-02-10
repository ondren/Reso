package Messages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myapplication.R;

public class MessageConfirmActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("MessageConfirmActivity is working");
        setContentView(R.layout.message_confirm_activity);


        TextView message_text_textview = findViewById(R.id.message_text_id);
        TextView message_date_textview = findViewById(R.id.message_date_id);

        String message_text = getIntent().getStringExtra("message_text");
        String message_date = getIntent().getStringExtra("message_date");

        System.out.println("message text is " + message_text);
        System.out.println("message date is " + message_date);


        message_date_textview.setText("Created at: " + message_date);
        message_text_textview.setText("Text is: " + message_text);

        Button back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(view -> {
            Intent intent = getIntent();
            setResult(RESULT_CANCELED, intent);
            finish();
        });

        Button confirm_btn = findViewById(R.id.confirm_btn);
        confirm_btn.setOnClickListener(view -> {
            Intent intent = getIntent();
            setResult(RESULT_OK, intent);
            finish();

        });
    }
}
