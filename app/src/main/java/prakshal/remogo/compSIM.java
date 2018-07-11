package prakshal.remogo;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class compSIM extends Service {
    SharedPreferences settings;
    String oldsim;
    String newsim;
    int simstatus;
    boolean notify;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        Log.i("compSIM","started");
        startForeground(1,new Notification());
        //Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        try {
            settings = getSharedPreferences("app_pwd", 0);
            notify=settings.getBoolean("notify",false);
            oldsim = settings.getString("Sim", "DEFAULT");
            simstatus = tManager.getSimState();
            if(notify==true){
                Log.i("notify","true");
            }
            if(notify==false){
                Log.i("notify","false");
            }
            if(notify==true) {
                if (simstatus != TelephonyManager.SIM_STATE_ABSENT) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                                == PackageManager.PERMISSION_GRANTED) {
                            newsim = tManager.getSimSerialNumber();
                        }
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                            newsim = tManager.getSimSerialNumber();
                        }
                    }
                }
                //System.out.println("---New SIM no is:" + newsiminfo);
                if (oldsim.equals(newsim)) {
                    //System.out.println("------Old sim Present:");
              /*  Toast.makeText(this, "Old SIM", Toast.LENGTH_LONG).show();
                SmsManager smsMngr = SmsManager.getDefault();
                String destinationaddress = "9173136119";
                String scAddress = null;
                String text = "Old Sim Is Inserted In Your Device";
                PendingIntent sentIntent = null;
                PendingIntent deliveryIntent = null;
                smsMngr.sendTextMessage(destinationaddress, scAddress, text,
                        sentIntent, deliveryIntent);*/
                } else {
                    //Toast.makeText(this, "New SIM", Toast.LENGTH_LONG).show();
                    SmsManager smsMngr = SmsManager.getDefault();
                    settings=getSharedPreferences("app_pwd",0);
                    String num1=settings.getString("num1","0");
                    if(!(num1.isEmpty()))
                    {
                        String destinationaddress =num1;
                        String scAddress = null;
                        String text = "New Sim Is Inserted In Your Device";
                        PendingIntent sentIntent = null;
                        PendingIntent deliveryIntent = null;
                        smsMngr.sendTextMessage(destinationaddress, scAddress, text,
                                sentIntent, deliveryIntent);
                    }
                    String num2=settings.getString("num1","0");
                    if(!(num2.isEmpty()))
                    {
                        String destinationaddress =num2;
                        String scAddress = null;
                        String text = "New Sim Is Inserted In Your Device";
                        PendingIntent sentIntent = null;
                        PendingIntent deliveryIntent = null;
                        smsMngr.sendTextMessage(destinationaddress, scAddress, text,
                                sentIntent, deliveryIntent);
                    }


                    //System.out.println("-----SMS Send");
                }
            }

        } catch (Exception e) {

        }
        return startId;

    }
}