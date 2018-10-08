package prakshal.remogo;

import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class sendActivitymenu extends AppCompatActivity {
    EditText txtphoneNo;
    EditText txtcname;
    EditText pwd;
    String phoneNo;
    Vibrator vib;
    String contact="";
    String password;
    SmsManager sms = SmsManager.getDefault();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_menu);
        Button rem_ring = (Button) findViewById(R.id.btn_remote_ringer);
        Button rem_con = (Button) findViewById(R.id.btn_remote_contact);
        Button ring=(Button) findViewById(R.id.ringbtn) ;
        Button rem_net=(Button)findViewById(R.id.remnet);
        Button rem_lock=(Button)findViewById(R.id.rem_lock_btn);
        Button rem_wipe=(Button)findViewById(R.id.rem_wipe);
        Button rem_call_log=(Button)findViewById(R.id.rem_call_log);
        txtcname = (EditText) findViewById(R.id.editText2);
        Log.i("here","in");
        //   Intent int1 = getIntent();

        //Bundle extras = getIntent().getExtras();
        //  if (extras != null) {
        phoneNo= getIntent().getStringExtra("phoneNo");
        Log.i("phone",phoneNo);
        password=getIntent().getStringExtra("pwd");
        //The key argument here must match that used in the other activity
        //  }
        //  Log.i("phoneno",phoneNo);
        //   Log.i("pwd",password);
        //  txtphoneNo = (EditText) findViewById(R.id.editText);
        //  pwd=(EditText) findViewById(R.id.editText3);
        rem_con.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.9F);
                view.startAnimation(buttonClick);
                sendcontact();
            }
        });
        rem_net.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.9F);
                view.startAnimation(buttonClick);
                networkstatus();
            }
        });
        rem_ring.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.9F);
                view.startAnimation(buttonClick);
                sendSMSMessage();
            }


        });
        ring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.9F);
                v.startAnimation(buttonClick);
                rngphone();
            }
        });
        rem_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.9F);
                v.startAnimation(buttonClick);
                remotelock();
            }
        });
        rem_wipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.9F);
                v.startAnimation(buttonClick);
                remotewipe();
            }
        });

        rem_call_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.9F);
                v.startAnimation(buttonClick);
                remotecalllog();
            }
        });
    }
    protected void remotelock(){
        //  phoneNo = txtphoneNo.getText().toString();
        // password=pwd.getText().toString();
     /*   if(phoneNo.isEmpty())
        {
            Toast.makeText(this,"Please enter phone no",Toast.LENGTH_SHORT).show();
            Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vib.vibrate(500);
        }*/
        // phoneNo="91"+phoneNo;
        try {
            Log.i("no",phoneNo);
            sms.sendTextMessage(phoneNo, null, phoneNo+":\n"+password+"4", null, null);
        }catch(Exception e){
            Log.i("sms", "Exception: " + e);
        }
    }
    protected void rngphone(){
        // phoneNo = txtphoneNo.getText().toString();
        // password=pwd.getText().toString();
      /*  if(phoneNo.isEmpty())
        {
            Toast.makeText(this,"Please enter phone no",Toast.LENGTH_SHORT).show();
            Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vib.vibrate(500);
        }*/
        //phoneNo="91"+phoneNo;
        try {

            sms.sendTextMessage(phoneNo, null, phoneNo+":\n"+password+"3", null, null);
        }catch(Exception e){
            Log.i("sms", "Exception: " + e);
        }
    }
    protected void remotewipe(){
        // phoneNo = txtphoneNo.getText().toString();
        // password=pwd.getText().toString();
      /*  if(phoneNo.isEmpty())
        {
            Toast.makeText(this,"Please enter phone no",Toast.LENGTH_SHORT).show();
            Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vib.vibrate(500);
        }*/
        //phoneNo="91"+phoneNo;
        try {

            sms.sendTextMessage(phoneNo, null, phoneNo+":\n"+password+"6", null, null);
        }catch(Exception e){
            Log.i("sms", "Exception: " + e);
        }
    }
    protected void sendSMSMessage() {
        //phoneNo = txtphoneNo.getText().toString();
        //password=pwd.getText().toString();
      /*  if(phoneNo.isEmpty())
        {
            Toast.makeText(this,"Please enter phone no",Toast.LENGTH_SHORT).show();
            Vibrator vib1 = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vib1.vibrate(500);
        }*/
        // phoneNo="91"+phoneNo;
        try {

            sms.sendTextMessage(phoneNo, null, phoneNo+":\n"+password+"1", null, null);
        }catch(Exception e){
            Log.i("sms", "Exception: " + e);
        }


    }
    protected void networkstatus(){
        //  phoneNo = txtphoneNo.getText().toString();
        // password=pwd.getText().toString();
     /*   if(phoneNo.isEmpty())
        {
            Toast.makeText(this,"Please enter phone no",Toast.LENGTH_SHORT).show();
            Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vib.vibrate(500);
        }*/
        // phoneNo="91"+phoneNo;
        try {
            Log.i("no",phoneNo);
            sms.sendTextMessage(phoneNo, null, phoneNo+":\n"+password+"5", null, null);
        }catch(Exception e){
            Log.i("sms", "Exception: " + e);
        }
    }
    protected void remotecalllog(){
        //  phoneNo = txtphoneNo.getText().toString();
        // password=pwd.getText().toString();
     /*   if(phoneNo.isEmpty())
        {
            Toast.makeText(this,"Please enter phone no",Toast.LENGTH_SHORT).show();
            Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vib.vibrate(500);
        }*/
        // phoneNo="91"+phoneNo;
        try {
            Log.i("no",phoneNo);
            sms.sendTextMessage(phoneNo, null, phoneNo+":\n"+password+"7", null, null);
        }catch(Exception e){
            Log.i("sms", "Exception: " + e);
        }
    }
    protected void sendcontact(){
        //phoneNo = txtphoneNo.getText().toString();
        //password=pwd.getText().toString();
      /*  if(phoneNo.isEmpty())
        {
            Toast.makeText(this,"Please enter phone no",Toast.LENGTH_SHORT).show();
            Vibrator vib2 = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vib2.vibrate(500);
        }*/
        // phoneNo="91"+phoneNo;
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
        /*try {
            SmsReceiver.bindListener(new SmsListener() {
                public void messageReceived(String sender, String messageText) {
                }
            });
        }catch (Exception e){
        }*/
    }

   /* @Override
    public void finish() {
        super.finish();
    }*/
}
