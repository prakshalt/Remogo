package prakshal.remogo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.NavigationView;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends BaseDrawerActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS =0;
    DevicePolicyManager mDevicePolicyManager;
    private GcmNetworkManager mGcmNetworkManager;
   // NotificationManager notificationManager;
    SharedPreferences settings;
    private DrawerLayout mDrawerLayout;
    int simstatus;
    String msisdn;
    int dndresult;
    int deviceadminreq;
    EditText txtphoneNo;
    EditText pwd;
    ImageButton contact_img;
    ImageButton remove_btn;
    ImageButton remove_sugg_btn;
    ImageButton remove_contact_btn;
    RelativeLayout contact_rel_layout;
    String phoneNo="";
    String password;
    String code="";
    String name;
    AutoCompleteTextView sbox;
   // Spinner s;
    Spinner sendmenuspinner;
    String oldph="";
    EditText txtcname;
    String contact="";
  //  TextView countrytv;
    SmsManager sms = SmsManager.getDefault();
    String selectedCountry;
    String selectedsendmenuitem;
    ArrayList<String> menuList;
    ArrayList<String> countryArrayList;
    ArrayList<String> codeArrayList;
    BroadcastReceiver br;
    String perms[]={Manifest.permission.RECEIVE_SMS,Manifest.permission.RECORD_AUDIO,Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_CALL_LOG,Manifest.permission.CHANGE_NETWORK_STATE,Manifest.permission.CHANGE_WIFI_STATE,Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.BROADCAST_SMS,Manifest.permission.MODIFY_AUDIO_SETTINGS,Manifest.permission.READ_CONTACTS,Manifest.permission.READ_PHONE_STATE,Manifest.permission.SEND_SMS};
    int rec_sms_perm, read_sms_perm, read_con_perm, send_sms_perm, read_phone_perm, camera_perm, write_ext_perm, read_call_perm, record_audio_perm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        getLayoutInflater().inflate(R.layout.activity_main, frameLayout);
     /*   Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        int id=menuItem.getItemId();
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();
                        if(id==R.id.nav_gallery)
                        {
                            Intent sendintent = new Intent(getApplicationContext(), sendActivity.class);
                            startActivity(sendintent);
                            //overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        }
                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });*/
        AlertDialog.Builder builder;
        rec_sms_perm = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        record_audio_perm = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        camera_perm = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        write_ext_perm = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        read_call_perm = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG);
        read_con_perm = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        read_phone_perm = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        send_sms_perm = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (rec_sms_perm != PackageManager.PERMISSION_GRANTED || record_audio_perm != PackageManager.PERMISSION_GRANTED || camera_perm != PackageManager.PERMISSION_GRANTED || write_ext_perm != PackageManager.PERMISSION_GRANTED || read_call_perm != PackageManager.PERMISSION_GRANTED || read_con_perm != PackageManager.PERMISSION_GRANTED ||  send_sms_perm != PackageManager.PERMISSION_GRANTED||read_phone_perm!= PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(this);
                }
                builder.setTitle("Permissions required")
                        .setMessage("Do you want to grant the permissions required(App cannot work without them,please refer to help)")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                requestperms();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                                homeIntent.addCategory( Intent.CATEGORY_HOME );
                                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(homeIntent);
                                Toast.makeText(MainActivity.this,"You may have not granted one of the permissions",Toast.LENGTH_LONG).show();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
       DNDaccess();
        settings = getSharedPreferences("app_pwd", 0);
        if (!(settings.getBoolean("Done", false))) {
                Intent login = new Intent(MainActivity.this, Login.class);
                startActivity(login);
            }
        mGcmNetworkManager = GcmNetworkManager.getInstance(this);
        Task task = new PeriodicTask.Builder()
                .setService(GCMService.class)
                .setPeriod(30)
                .setFlex(10)
                .setTag("Periodic log")
                .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
               // .setPersisted(true)
                .build();

        mGcmNetworkManager.schedule(task);
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
        br = new NetworkChangeReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(br, filter);
        settings = getSharedPreferences("app_pwd", 0);
        settings.edit()
                .putString("Sim",msisdn)
                .apply();
        startService(new Intent(this,NetCheckerService.class));
        Log.i("Sim from shared pref",settings.getString("Sim","DEFAULT"));
       /* Switch onOffSwitch = (Switch)  findViewById(R.id.switch1);
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
        });*/
        codeArrayList = new ArrayList<String>(Arrays.asList(CountryDetails.getCode()));
        countryArrayList = new ArrayList<String>(Arrays.asList(CountryDetails.getCountry()));
        menuList=new ArrayList<String>(Arrays.asList(CountryDetails.getSendmenu()));


        Button submit = (Button) findViewById(R.id.submit);
        // final Button pck = (Button) findViewById(R.id.pick);
        contact_img = (ImageButton) findViewById(R.id.contact_img_btn);
        remove_btn=(ImageButton)findViewById(R.id.remove_pwd_btn);
        remove_sugg_btn=(ImageButton)findViewById(R.id.remove_autocomplete_btn);
        txtphoneNo = (EditText) findViewById(R.id.editText);
        pwd = (EditText) findViewById(R.id.editText3);
        contact_rel_layout=(RelativeLayout)findViewById(R.id.contactrelativeLayout);
        txtcname=(EditText)findViewById(R.id.contact_name_to_retrieve);
        remove_contact_btn=(ImageButton)findViewById(R.id.remove_contact_btn);

       // RadioGroup rg=(RadioGroup) findViewById(R.id.radioGroup);
      /*  rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch(checkedId)
                {
                    case R.id.radioButton:
                        contact_img.setEnabled(false);
                        txtphoneNo.setEnabled(true);
                        txtphoneNo.setFocusableInTouchMode(true);
                        // code='+'+code;
                        //txtphoneNo.setText(code);
                        break;
                    case R.id.radioButton3:
                        contact_img.setEnabled(true);
                        txtphoneNo.setEnabled(false);
                        txtphoneNo.setFocusableInTouchMode(false);
                        break;
                }
            }
        });*/
        sendmenuspinner=(Spinner) findViewById(R.id.sendmenuspinner);
        ArrayAdapter<String> sendmenuadapter = new ArrayAdapter<>(MainActivity.this,R.layout.spinner_item,menuList);
        sendmenuadapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sendmenuspinner.setAdapter(sendmenuadapter);
        sendmenuspinner.setOnItemSelectedListener(new MainActivity.MyOnItemSelectedListener());
        sbox=(AutoCompleteTextView)findViewById(R.id.suggestion_box);
        sbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String autoCompleteText = (String) adapterView.getItemAtPosition(i);
                sbox.setText(autoCompleteText);
                int len=autoCompleteText.length();
                sbox.setSelection(len);
                int index=countryArrayList.indexOf(autoCompleteText);
                code=codeArrayList.get(index);
                code=code.replaceAll("[^0-9]","");
                //code="+"+code;
                Log.i("country",selectedCountry+code);
                if(index>0) {
                    if (oldph.isEmpty()) {
                        String temp = '+' + code;
                        int len1=temp.length();
                        txtphoneNo.setText(temp);
                        txtphoneNo.setSelection(len1);
                    } else {
                        String temp = '+' + code + oldph;
                        int len1=temp.length();
                        txtphoneNo.setText(temp);
                        txtphoneNo.setSelection(len1);
                    }
                }
            }
        });

       // s = (Spinner) findViewById(R.id.spinner);  // id of your spinner
     //   s.setTitle("Select Country for country code");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,R.layout.spinner_item, countryArrayList);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sbox.setAdapter(adapter);
        sbox.setThreshold(1);
       sbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                sbox.showDropDown();
            }
        });
       // s.setAdapter(adapter);
        sbox.setOnItemSelectedListener(new MainActivity.MyOnItemSelectedListener());
       // s.setOnItemSelectedListener(new MainActivity.MyOnItemSelectedListener());
        final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.9F);

        remove_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                pwd.setText("");

            }
        });
        remove_sugg_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                sbox.setText("");

            }
        });
        remove_contact_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                sbox.setText("");

            }
        });
        contact_img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                Uri uri = Uri.parse("content://contacts");
                Intent intent = new Intent(Intent.ACTION_PICK, uri);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, 1);

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.9F);
                view.startAnimation(buttonClick);
                phoneNo = txtphoneNo.getText().toString();
                password = pwd.getText().toString();
                if (phoneNo.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter phone no", Toast.LENGTH_SHORT).show();
                    Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vib.vibrate(500);
                }
                if (password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_SHORT).show();
                    Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vib.vibrate(500);
                }
                // Log.i("ph",phoneNo);
                // Log.i("pw",password);
                if (!(phoneNo.isEmpty()) && !(password.isEmpty())) {
                    senditemviasms(phoneNo,password);

                    /*sendint.putExtra("phoneNo", phoneNo);
                    sendint.putExtra("pwd", password);
                    startActivity(sendint);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);*/
                }
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        // to check current activity in the navigation drawer
       navigationView.getMenu().getItem(0).setChecked(true);
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        try {
            unregisterReceiver(br);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
    public boolean checkpermissions(){
        rec_sms_perm = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        record_audio_perm = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        camera_perm = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        write_ext_perm = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        read_call_perm = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG);
        read_con_perm = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        read_phone_perm = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        //   read_sms_perm = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        send_sms_perm = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        boolean temp= (rec_sms_perm == PackageManager.PERMISSION_GRANTED && record_audio_perm == PackageManager.PERMISSION_GRANTED && camera_perm == PackageManager.PERMISSION_GRANTED && write_ext_perm == PackageManager.PERMISSION_GRANTED && read_call_perm == PackageManager.PERMISSION_GRANTED && read_con_perm == PackageManager.PERMISSION_GRANTED  && send_sms_perm == PackageManager.PERMISSION_GRANTED && read_phone_perm== PackageManager.PERMISSION_GRANTED);
        return temp;
    }
    public void requestperms(){
        ActivityCompat.requestPermissions(MainActivity.this, perms,
                MY_PERMISSIONS_REQUEST_RECEIVE_SMS);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        DNDaccess();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        DevicePolicyManager mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName mAdminName = new ComponentName(this, DemoAdminReceiver.class);


        if(requestCode==dndresult)
        {
            if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && notificationManager.isNotificationPolicyAccessGranted() && mDPM != null && !mDPM.isAdminActive(mAdminName))
            {
                mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
                // mDevicePolicyManager.lockNow();
                ComponentName mDeviceAdminSample;
                mDeviceAdminSample = new ComponentName(this,DemoAdminReceiver.class);
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                        mDeviceAdminSample);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                        "You need to activate Device Administrator to perform phonelost tasks!");
                startActivityForResult(intent, deviceadminreq);
            }
        }
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

                Cursor cursor = getContentResolver().query(uri, projection,
                        null, null, null);
                cursor.moveToFirst();

                int numberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                phoneNo = cursor.getString(numberColumnIndex);
                //phoneNo=phoneNo.replaceAll("[^0-9][^+]","");

                //Log.i("no",phoneNo);

                int nameColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                name = cursor.getString(nameColumnIndex);
                String first=phoneNo.substring(0,1);
                String plus="+";
                Log.i("no",first);
                Log.i("+",plus);
                phoneNo=phoneNo.replaceAll("\\s","");
                phoneNo=phoneNo.replaceAll("[-]","");
                //Log.i("phoneno",phoneNo);
                //phoneNo.replaceAll("[^0-9][^+]","");
                Log.i("phoneno",phoneNo);
                if(first.equals(plus))
                {
                    Log.d("contact", "ZZZ number : " + phoneNo + " , name : " + name);
                    txtphoneNo.setText(phoneNo);
                }
                else
                {
                    if(code.equals(""))
                    {
                        Toast.makeText(this,"Selected contact does not have country code,please select country code",Toast.LENGTH_LONG).show();
                        sbox.requestFocus();
                        sbox.performClick();
                        oldph=phoneNo;
                        //phoneNo='+'+code+phoneNo;
                        //txtphoneNo.setText(phoneNo);
                    }
                    else
                    {
                        phoneNo='+'+code+phoneNo;
                        txtphoneNo.setText(phoneNo);
                    }
                    // phoneNo=phoneNo.replaceAll("[^0-9]","");
                    // phoneNo=code+phoneNo;
                    // Log.d("contact", "ZZZ number : " + phoneNo + " , name : " + name);
                }

            }
        }
       /* if(requestCode==deviceadminreq && resultCode==RESULT_OK) {

        }*/
    }
    public void DNDaccess(){
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        AlertDialog.Builder builder;
        if (checkpermissions()) {
            Log.d("DND","inside");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !notificationManager.isNotificationPolicyAccessGranted()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(this);
                }
                builder.setTitle("Do not disturb access required")
                        .setMessage("Do you want to grant the permission required(App cannot work without them,please refer to help)")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                Intent intent = new Intent(
                                        android.provider.Settings
                                                .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

                                startActivityForResult(intent,dndresult);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                                homeIntent.addCategory(Intent.CATEGORY_HOME);
                                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(homeIntent);
                                Toast.makeText(MainActivity.this,"You may have not granted one of the permissions",Toast.LENGTH_LONG).show();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
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
     /*   if(item.getItemId() == android.R.id.home){
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }
    public class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            String selectedItem = parent.getItemAtPosition(pos).toString();
            //Log.d("parent",Integer.toString((parent.getId())));
            //check which spinner triggered the listener
            switch (parent.getId()) {
                //country spinner
            /*    case R.id.spinner:
                    //make sure the country was already selected during the onCreate
                    if (selectedCountry != null) {
                        //Toast.makeText(parent.getContext(), "Country you selected is " + selectedItem,
                        //       Toast.LENGTH_LONG).show();
                    }
                    selectedCountry = selectedItem;
                    int index=countryArrayList.indexOf(selectedCountry);
                    code=codeArrayList.get(index);
                    code=code.replaceAll("[^0-9]","");
                    //code="+"+code;
                    Log.i("country",selectedCountry+code);
                    if(index>0) {
                        if (oldph.isEmpty()) {
                            String temp = '+' + code;
                            int len=temp.length();
                            txtphoneNo.setText(temp);
                            txtphoneNo.setSelection(len);
                        } else {
                            String temp = '+' + code + oldph;
                            int len=temp.length();
                            txtphoneNo.setText(temp);
                            txtphoneNo.setSelection(len);
                        }
                    }
                    break;*/
                case R.id.sendmenuspinner:
                    //Toast.makeText(MainActivity.this,selectedItem,Toast.LENGTH_SHORT).show();

                    if(selectedItem.equals("Retrieve Contact"))
                    {
                        contact_rel_layout.setVisibility(View.VISIBLE);
                        txtcname.requestFocus();
                        txtcname.callOnClick();
                    }
                    else
                    {
                        contact_rel_layout.setVisibility(View.GONE);
                    }
                    setselecteditem(selectedItem);

                    break;
            }


        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
    }
    public void setselecteditem(String sendmenuitem)
    {
        selectedsendmenuitem=sendmenuitem;
    }
    public void senditemviasms(String ph,String pwd)
    {
        switch (selectedsendmenuitem){
            case "Ringer":
                sendsms("1");
                break;
            case "Retrieve Contact":
                sendcontact();
                break;
            case "Find my phone":
                sendsms("3");
                break;
            case "Lock":
                sendsms("4");
                break;
            case "Network Status":
                sendsms("5");
                break;
            case "Wipe":
                sendsms("6");
                break;
            case "Call log":
                sendsms("7");
                break;
            case "Image capture":
                sendsms("8");
                break;
            case "Audio record":
                sendsms("9");
                break;
        }
    }
    protected void sendsms(String funcno)
    {
        String SENT = "SMS_SENT";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);
        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        try {
            Log.i("no",phoneNo);
            sms.sendTextMessage(phoneNo, null, phoneNo+":\n"+password+funcno, sentPI, null);
        }catch(Exception e){
            Log.i("sms", "Exception: " + e);
        }

    }
    protected void sendcontact(){
        contact = txtcname.getText().toString();
        if(contact.isEmpty())
        {
            Toast.makeText(this,"Please enter contact name",Toast.LENGTH_SHORT).show();
        }
        try {

            sms.sendTextMessage(phoneNo, null, phoneNo+":\n"+password+"2"+contact, null, null);
        }catch(Exception e){
            Log.i("sms", "Exception: " + e);
        }
    }
}
