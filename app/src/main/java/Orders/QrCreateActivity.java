package Orders;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.databinding.QrCreateActivityBinding;

public class QrCreateActivity extends Activity {

    QrCreateActivityBinding binding;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = QrCreateActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backBtn.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
        binding.goButton.setOnClickListener(v -> {
            String comment = binding.comment.getText().toString();
            if(comment.equals(""))
                return;
            Intent intent = getIntent();
            intent.putExtra("comment", comment);
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}