package prakshal.remogo;

import android.content.Intent;
import android.content.SharedPreferences;
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

