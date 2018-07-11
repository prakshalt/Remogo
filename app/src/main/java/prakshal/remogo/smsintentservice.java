package prakshal.remogo;

import android.app.IntentService;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

public class smsintentservice extends IntentService {
    SharedPreferences settings;
    SmsManager sms = SmsManager.getDefault();
    public void setRinger2Silent()
    {
        AudioManager audioManager= (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    }
    public void setRinger2Normal()
    {
        AudioManager audioManager= (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    }
    public boolean IsRingerSilent()
    {
        AudioManager audioManager =
                (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT || audioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE;
    }


    public smsintentservice() {
        super("smsintentservice");
    }

        @Override
        protected void onHandleIntent(Intent intent) {
            Log.i("info","intentservice started");
            settings = getSharedPreferences("app_pwd", 0);
            //Log.d("pref",Boolean.toString(settings.getBoolean("Done",false)));
            if(!(settings.getBoolean("Done",false))){
                Intent login = new Intent(this,Login.class);
                startActivity(login);
            }
            DevicePolicyManager mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
            String password=settings.getString("Password","DEFAULT");
            String sender = intent.getStringExtra("no");
            String msg  = intent.getStringExtra("message");
            String sendto = sender;
            int pwlen=password.length();
            int no_len=sender.length();
            Log.i("oldmsg",msg);
            msg=msg.replaceAll("[\n\r]", "");
            String temp=msg.substring(0,1);
            String colon=":";
            int i=1;
            while(!(temp.equals(colon))){
                temp=msg.substring(i,i+1);
                i++;
            }
            msg=msg.substring(i);
            //msg=msg.substring(no_len+1);
            //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            Log.i("no",sender);
            Log.i("msg",msg);
            if(msg.substring(0,pwlen).equals(password))
            {
                if(msg.substring(pwlen,pwlen+1).equals("4"))
                {
                    mDevicePolicyManager.lockNow();
                }

                if(msg.substring(pwlen,pwlen+1).equals("1"))
                {
                    if(IsRingerSilent())
                    {
                        setRinger2Normal();
                    }
                    else
                    {
                        setRinger2Silent();
                    }
                }
                if(msg.substring(pwlen,pwlen+1).equals("3"))
                {
                    if(IsRingerSilent())
                    {
                        setRinger2Normal();
                    }
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                    final Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                    r.play();
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            r.stop();
                        }
                    }, 10000);
                }
                String no=msg.substring(pwlen,pwlen+1);
                String contact = msg.substring(pwlen+1);
                //Log.i("contact",no);
                //Toast.makeText(MainActivity.this,no,Toast.LENGTH_LONG).show();
                if(no.equals("5")){
                    final ConnectivityManager cm =
                            (ConnectivityManager)getApplication().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                    final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    final boolean isConnected = activeNetwork != null &&
                            activeNetwork.isConnectedOrConnecting();
                    //setMobileDataEnabled(getApplicationContext(),true);
                    final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    //enableInternet(true);
                    if(!(wifiManager.isWifiEnabled())){
                        wifiManager.setWifiEnabled(true);
                    }
                  /*  if((wifiManager.isWifiEnabled())){
                        wifiManager.setWifiEnabled(false);
                    }*/
                    //final Handler handler = new Handler();
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {


                   /* new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Log.i("Wait","wait");
                        }
                    }, 20000);*/
                   Log.i("in","timer");

                    if(isConnected){
                        String type = activeNetwork.getTypeName();
                        Log.i("wifi",type);
                        if(type.equals("WIFI")){
                            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                            String ssid=connectionInfo.getSSID();
                            int ip=connectionInfo.getIpAddress();
                            int speed=connectionInfo.getLinkSpeed();
                            String mac=connectionInfo.getBSSID();
                            String ipadd=String.format("%d.%d.%d.%d",(ip & 0xff),(ip >> 8 & 0xff),(ip >> 16 & 0xff),(ip >> 24 & 0xff));
                            String speed_str=Integer.toString(speed);
                            String final_message="Connected to WIFI,details are as follows"+"\n"+"SSID:"+ssid+"\n"+"MAC:"+mac+"\n"+"IP:"+ipadd+"\n"+"Speed:"+speed_str+"mbps";
                           // sms.sendTextMessage(sendto, null,final_message, null, null);
                        }
                        if(type.equals("MOBILE")){
                            String m_message="Connected to mobile data";
                            //sms.sendTextMessage(sendto,null,m_message,null,null);
                        }
                    }
                    else{
                        Log.i("type","null");
                        String message="Not connected to internet";
                        //sms.sendTextMessage(sendto,null,message,null,null);
                    }
                        }
                    }, 15000);
                }
                if(no.equals("2")) {
                    Cursor phoneCursor = null;
                    // Log.i("in","hello");
                    try {
                        Uri uContactsUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                        String strProjection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
                        phoneCursor = getContentResolver().query(uContactsUri, null, null, null, strProjection);
                        phoneCursor.moveToFirst();
                        String name = "";
                        String phoneNumber = "";
                        int nameColumn = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                        int phoneColumn = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        while (!phoneCursor.isAfterLast()) {
                            name = phoneCursor.getString(nameColumn);
                            phoneNumber = phoneCursor.getString(phoneColumn);
                            //Log.i(name,phoneNumber);
                            if(name.equals(contact))
                            {
                                String tobesent = phoneNumber;
                                sms.sendTextMessage(sendto, null,tobesent, null, null);
                                break;
                                //Log.i("here:",tobesent);
                            }
                            phoneCursor.moveToNext();
                        }
                    }

                    catch(Exception e){
                        Log.e("contact", e.toString());
                    }
                    finally{
                        if(phoneCursor != null){
                            phoneCursor.close();
                            phoneCursor = null;
                        }
                    }
                }
            }
        }
    void enableInternet(boolean yes)
    {
        ConnectivityManager iMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        Method iMthd = null;
        try {
            iMthd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
        } catch (Exception e) {
        }
        iMthd.setAccessible(false);

        if(yes)
        {

            try {
                iMthd.invoke(iMgr, true);
                Toast.makeText(getApplicationContext(), "Data connection Enabled", Toast.LENGTH_SHORT).show();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                //dataButton.setChecked(false);
                Toast.makeText(getApplicationContext(), "IllegalArgumentException", Toast.LENGTH_SHORT).show();

            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                Toast.makeText(getApplicationContext(), "IllegalAccessException", Toast.LENGTH_SHORT).show();

                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                //dataButton.setChecked(false);
                Toast.makeText(getApplicationContext(), "InvocationTargetException", Toast.LENGTH_SHORT).show();

            }

        }
        else
        {
            try {
                iMthd.invoke(iMgr, true);
                Toast.makeText(getApplicationContext(), "Data connection Disabled", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                //dataButton.setChecked(true);
                Toast.makeText(getApplicationContext(), "Error Disabling Data connection", Toast.LENGTH_SHORT).show();
            }
        }
    }
        }
