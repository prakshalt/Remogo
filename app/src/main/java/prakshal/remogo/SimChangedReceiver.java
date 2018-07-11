package prakshal.remogo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class SimChangedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        Log.d("SimChangedReceiver", "--> SIM state changed <--");
        Intent CompareSimServiceIntent = new Intent(context, compSIM.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, compSIM.class));
        } else {
            context.startService(new Intent(context, compSIM.class));
        }

        
    }
}