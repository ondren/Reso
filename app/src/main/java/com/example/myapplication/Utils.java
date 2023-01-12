package com.example.myapplication;


import static android.content.Context.VIBRATOR_SERVICE;

import android.content.Context;
import android.os.Vibrator;



import android.os.Vibrator;

public  class Utils {
    public static final long[] DEFAULT_VIBRATION_PATTERN = {0, 1000, 300, 1000, 300, 2000, 300, 1000, 500, 1000, 500, 1000};

    public static void vibrate(Context context){
        vibrate(context,DEFAULT_VIBRATION_PATTERN);
    }
    public static void vibrate(Context context, long[] pattern){
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        final int indexInPatternRepeat = -1;
        vibrator.vibrate(pattern, indexInPatternRepeat);
    }
}

