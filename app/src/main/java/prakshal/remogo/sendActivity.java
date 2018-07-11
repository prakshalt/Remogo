package prakshal.remogo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class sendActivity extends AppCompatActivity {
    EditText txtphoneNo;
    EditText pwd;
    ImageButton contact_img;
    String phoneNo="";
    String password;
    String code="";
    String name;
    Spinner s;
    String oldph="";

    String selectedCountry;
    ArrayList<String> countryArrayList;
    ArrayList<String> codeArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        codeArrayList = new ArrayList<String>(Arrays.asList(CountryDetails.getCode()));
        countryArrayList = new ArrayList<String>(Arrays.asList(CountryDetails.getCountry()));


        Button submit = (Button) findViewById(R.id.submit);
       // final Button pck = (Button) findViewById(R.id.pick);
        contact_img = (ImageButton) findViewById(R.id.contact_img_btn);
        txtphoneNo = (EditText) findViewById(R.id.editText);
        pwd = (EditText) findViewById(R.id.editText3);
        RadioGroup rg=(RadioGroup) findViewById(R.id.radioGroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
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
        });
        s = (Spinner) findViewById(R.id.spinner);  // id of your spinner

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(sendActivity.this,R.layout.spinner_item, countryArrayList);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        s.setAdapter(adapter);
        s.setOnItemSelectedListener(new MyOnItemSelectedListener());
        final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.9F);
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
                Intent sendint = new Intent(sendActivity.this, sendActivitymenu.class);
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

                    sendint.putExtra("phoneNo", phoneNo);
                    sendint.putExtra("pwd", password);
                    startActivity(sendint);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri uri = intent.getData();
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
                        s.requestFocus();
                        s.performClick();
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
    }

    public class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            String selectedItem = parent.getItemAtPosition(pos).toString();

            //check which spinner triggered the listener
            switch (parent.getId()) {
                //country spinner
                case R.id.spinner:
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
                    if(oldph.isEmpty()) {
                        String temp='+'+code;
                        txtphoneNo.setText(temp);
                    }
                    else
                    {
                        String temp='+'+code+oldph;
                        txtphoneNo.setText(temp);
                    }
                    break;
            }


        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
    }
}

