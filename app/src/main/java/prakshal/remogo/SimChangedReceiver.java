package prakshal.remogo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

public class SimChangedReceiver extends BroadcastReceiver {
    SharedPreferences settings;
    @Override
    public void onReceive(final Context context, final Intent intent) {

        Log.d("SimChangedReceiver", "--> SIM state changed <--");
        settings = context.getSharedPreferences("app_pwd",Context.MODE_PRIVATE);
        if(settings.contains("flag") && checkTime(settings.getString("flag","0"))) {

            Intent CompareSimServiceIntent = new Intent(context, compSIM.class);
            settings.edit()
                    .putString("flag",String.valueOf(System.currentTimeMillis()))
                    .commit();

            Log.i("flag i" , String.valueOf(System.currentTimeMillis()));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, compSIM.class));
            } else {
                context.startService(new Intent(context, compSIM.class));
            }

        }
        else if(!settings.contains("flag")){
            Intent CompareSimServiceIntent = new Intent(context, compSIM.class);

            settings.edit()
                    .putString("flag",String.valueOf(System.currentTimeMillis()))
                    .commit();

            Log.i("flag e" , String.valueOf(System.currentTimeMillis()) + String.valueOf(settings.contains("flag")));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, compSIM.class));
            } else {
                context.startService(new Intent(context, compSIM.class));
            }

        }

        
    }

    private boolean checkTime(String flag) {
        long t1=Long.parseLong(flag);
        long t2=System.currentTimeMillis();
        int divide=1000;
        Log.i("diff ",String.valueOf((t2-t1)/divide));
        if((t2-t1)/divide > 30)
            return true;
        else
            return false;
    }
}