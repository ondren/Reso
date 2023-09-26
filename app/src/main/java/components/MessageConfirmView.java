package components;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatButton;

import com.example.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import Messages.MessageConfirmActivity;
import api.ApiAccess;

public class MessageConfirmView extends RelativeLayout {
    String code;
    String title;
    String text;
    String date;
    AppCompatButton message_btn;
    ImageButton confirm_btn;

    public MessageConfirmView(Context context) {
        super(context);
        initComponent();
    }
    void initComponent(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.message_confirm_layout, this);

        message_btn = findViewById(R.id.message_btn);
        confirm_btn = findViewById(R.id.confirm_btn);
    }

    public void setData(String code, String title, String message, String date){
        this.code = code;
        this.title = title;
        this.text = message;
        this.date = date;

        String txt = title.equals("null") ? text : title;
        message_btn.setText(txt);
        message_btn.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), MessageConfirmActivity.class);
            intent.putExtra("text", text);
            intent.putExtra("title", title.equals("null") ? "":title);
            intent.putExtra("date", date);
            intent.putExtra("code", code);

            getContext().startActivity(intent);

        });
        confirm_btn.setOnLongClickListener(view -> {
            ApiAccess.setContext(getContext());
            HashMap<String, String> params = new HashMap<>();
            params.put("code", code);
            ApiAccess.get("messages/confirm", params,
                    response -> {
                        confirm_btn.setImageResource(R.drawable.check_after_press);
                        confirm_btn.setOnLongClickListener(null);
                    }, error -> {
                        confirm_btn.setImageResource(R.drawable.close_red);

                        String response = error.networkResponse != null ? new String(error.networkResponse.data) : null;
                        String msg = "";
                        try {
                            msg = response == null ? error.getMessage() : (new JSONObject(response)).getString("error");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        message_btn.setText(txt + "\n" + msg);
                        error.printStackTrace();
                    });
            return true;
        });
    }

}
