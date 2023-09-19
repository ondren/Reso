package Utils;


import android.content.Context;
import android.os.Vibrator;

public  class VibrateUtil {
    public static final long[] IMPORTANT_VIBRATION_PATTERN = {0, 1000, 300, 1000, 300, 2000, 300, 1000, 500, 1000, 500, 1000};
    public static final long[] SMALL_MESSAGE_VIBRATION_PATTERN = {0, 1000, 300, 1000};

    public static void vibrate(Context context){
        vibrate(context, IMPORTANT_VIBRATION_PATTERN);
    }
    public static void vibrateShort(Context context){
        vibrate(context, SMALL_MESSAGE_VIBRATION_PATTERN);
    }
    public static void vibrate(Context context, long[] pattern){
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        final int indexInPatternRepeat = -1;
        vibrator.vibrate(pattern, indexInPatternRepeat);
    }
}

