package prakshal.remogo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class SIMTestReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent CompareSimServiceIntent = new Intent(context, compSIM.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, compSIM.class));
        } else {
            context.startService(new Intent(context, compSIM.class));
        }
       // }

    }
}