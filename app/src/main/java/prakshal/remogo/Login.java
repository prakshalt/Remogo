package prakshal.remogo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.provider.Telephony.Carriers.PASSWORD;

public class Login extends AppCompatActivity {
    String pw;
    String cpw;
    EditText pwd1;
    EditText confpwd1;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        settings = getSharedPreferences("app_pwd", 0);
        //  Log.i("LoginActivity","here");
        Button submit = (Button)findViewById(R.id.submitbtn);
        pwd1=(EditText)findViewById(R.id.setpwd);
        confpwd1=(EditText)findViewById(R.id.confpwd);
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Set password(Only one time)")
                .setMessage("Set password for this device so that you can use it to perform remote functions in the future(App cannot work without it,please refer to help)")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory( Intent.CATEGORY_HOME );
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                        Toast.makeText(Login.this,"You may have not set the password",Toast.LENGTH_LONG).show();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.9F);
                view.startAnimation(buttonClick);
                submitfn();
            }
        });
    }
    protected void submitfn() {
        //Log.i("LoginActivity","here");
        pw = pwd1.getText().toString();
        cpw = confpwd1.getText().toString();
        if (pw.isEmpty() || cpw.isEmpty()) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
        }
        if (!pw.equals(cpw)) {
            Toast.makeText(this, "Both passwords do not match", Toast.LENGTH_SHORT).show();
        }
        if (pw.equals(cpw)) {
            compwd();

        }
    }
    protected void compwd()
    {
        settings = getSharedPreferences("app_pwd", 0);
        settings.edit()
                .putString("Password",pw)
                .putBoolean("Done",true)
                .apply();
        // Log.d("loginpref",Boolean.toString(settings.getBoolean("Done",false)));
        Intent main=new Intent(Login.this,MainActivity.class);
        startActivity(main);
    }
}

