package prakshal.remogo;

import android.Manifest;
import android.app.NotificationManager;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS =0;
    DevicePolicyManager mDevicePolicyManager;
    SharedPreferences settings;
    int simstatus;
    String msisdn;
    String perms[]={Manifest.permission.RECEIVE_SMS,Manifest.permission.READ_CALL_LOG,Manifest.permission.CHANGE_NETWORK_STATE,Manifest.permission.CHANGE_WIFI_STATE,Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.BROADCAST_SMS,Manifest.permission.MODIFY_AUDIO_SETTINGS,Manifest.permission.READ_CONTACTS,Manifest.permission.READ_PHONE_STATE,Manifest.permission.SEND_SMS};


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, perms,
                    MY_PERMISSIONS_REQUEST_RECEIVE_SMS);}
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

            startActivity(intent);
        }
        settings = getSharedPreferences("app_pwd", 0);
        //Log.d("pref",Boolean.toString(settings.getBoolean("Done",false)));
        if(!(settings.getBoolean("Done",false))){
            Intent login = new Intent(MainActivity.this,Login.class);
            startActivity(login);
        }
        mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        // mDevicePolicyManager.lockNow();
        ComponentName mDeviceAdminSample;
        mDeviceAdminSample = new ComponentName(this,DemoAdminReceiver.class);
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                mDeviceAdminSample);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "You need to activate Device Administrator to perform phonelost tasks!");
        startActivityForResult(intent, 1);
        TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        simstatus = tManager.getSimState();
        if (simstatus != TelephonyManager.SIM_STATE_ABSENT) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                        == PackageManager.PERMISSION_GRANTED){
                    msisdn = tManager.getSimSerialNumber();
                }
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
                    msisdn = tManager.getSimSerialNumber();
                }
            }
        }
//     Log.i("Sim sr no",msisdn);
        settings = getSharedPreferences("app_pwd", 0);
        settings.edit()
                .putString("Sim",msisdn)
                .apply();

        Log.i("Sim from shared pref",settings.getString("Sim","DEFAULT"));
        Switch onOffSwitch = (Switch)  findViewById(R.id.switch1);
        settings = getSharedPreferences("app_pwd", 0);
        boolean state=settings.getBoolean("notify",false);
        onOffSwitch.setChecked(state);
        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", ""+isChecked);
                settings = getSharedPreferences("app_pwd", 0);
                if (isChecked) {
                    settings.edit().putBoolean("notify",true).apply();
                } else {
                    settings.edit().putBoolean("notify",false).apply();
                }
            }

        });
        Button send=(Button) findViewById(R.id.sendbtn);
        final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.9F);
        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                Intent sendintent = new Intent(MainActivity.this, sendActivity.class);
                startActivity(sendintent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });
        Button addno=(Button) findViewById(R.id.addnobtn);
        addno.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                Intent addnointent = new Intent(MainActivity.this, addnoactivity.class);
                startActivity(addnointent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });
    }

    public class DemoAdminReceiver extends DeviceAdminReceiver {
        @Override
        public void onEnabled(Context context, Intent intent) {
            super.onEnabled(context, intent);

        }
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.mymenu,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.about) {
            startActivity(new Intent(this, about.class));
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        }
        return super.onOptionsItemSelected(item);
    }
}