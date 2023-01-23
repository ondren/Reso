package Service;

import static Utils.Utils.vibrate;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import api.ApiAccess;

public class MyService extends Service {

    public static final int notify = 60000;  //interval between two services(Here Service run every 1 Minute)
    private Handler mHandler = new Handler();   //run on another Thread to avoid crash
    private Timer mTimer = null;    //timer handling

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        if (mTimer != null) // Cancel if already existed
            mTimer.cancel();
        else
            mTimer = new Timer();   //recreate new
        mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, notify);   //Schedule task
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();    //For Cancel Timer
        Toast.makeText(this, "Service is Destroyed", Toast.LENGTH_SHORT).show();
    }

    //class TimeDisplay for handling task
    class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    // display toast
                    //Toast.makeText(MyService.this, "Service is running", Toast.LENGTH_SHORT).show();
                    ApiAccess.setContext(getApplicationContext());
                    HashMap<String, String> params = new HashMap<>();
                    Context context = getApplicationContext();
                    params.put("status", "created");

                    ApiAccess.get("users/messages", params,
                           response -> {
                               try {
                                   JSONArray jsonArray = response.getJSONArray("messages");
                                   if (jsonArray.length() > 0) {
                                       Toast.makeText(MyService.this, "You have " +
                                               jsonArray.length() + " unread messages", Toast.LENGTH_LONG).show();
                                       int v = 2;

                                       vibrate(getApplicationContext());
                                       //вибрацию
//                                       Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//                                       long[] vibrationPattern = {0, 1000, 1000, 1500};
//                                       final int indexInPatternRepeat = -1;
//                                       vibrator.vibrate(vibrationPattern, indexInPatternRepeat);
                                   }
                               } catch (JSONException e) {
                                   e.printStackTrace();
                               }

                           }
                    );



                }
            });
        }
    }
}