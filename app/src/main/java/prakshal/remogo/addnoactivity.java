package prakshal.remogo;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class addnoactivity extends BaseDrawerActivity {
    int numberOfLines=0;
    SharedPreferences settings;
    String number;
    String num1,num2;
    String key;
    String nooflines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_addno);
        getLayoutInflater().inflate(R.layout.activity_addno, frameLayout);
        checkprevious();

        Button Submit= (Button)findViewById(R.id.sub_btn);
        final EditText e1=(EditText)findViewById(R.id.number1et);
        final EditText e2=(EditText)findViewById(R.id.number2et);


        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num1=e1.getText().toString();
                num2=e2.getText().toString();
                Log.i("num1",num1);
                Log.i("num2",num2);
                settings = getSharedPreferences("app_pwd", 0);
                settings.edit().putString("num1",num1).apply();
                settings.edit().putString("num2",num2).apply();
                String temp=settings.getString("num2","0");
                Log.i("num2",temp);
            }
        });

    }
  /*  View.OnClickListener myClickLIstener= new View.OnClickListener() {
        public void onClick(View v) {
            String tag = (String) v.getTag();
            Log.i(tag,"done");
            EditText text = (EditText)findViewById(Integer.valueOf(tag));
            number=text.getText().toString();
            if (!(number.substring(0,1).equals("+")))
            {
                number='+'+number;
            }
            settings = getSharedPreferences("app_pwd", 0);
            nooflines=Integer.toString(numberOfLines);
            key="number"+nooflines;
            settings.edit().putInt("numberoflines",numberOfLines).apply();
            settings.edit().putString(key,number).apply();
        }
    };
    public void Add_Line() {
        LinearLayout l1 = (LinearLayout)findViewById(R.id.editTextContainer);
        LinearLayout l2 = new LinearLayout(this);
        EditText et = new EditText(this);
        numberOfLines++;
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        et.setLayoutParams(p);
        et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
        et.setHint("Enter mobile number");
        et.setId(numberOfLines);
        l1.addView(l2);
        l2.addView(et);
        Button sub= new Button(this);
        sub.setText("submit");
        sub.setLayoutParams(p);
        sub.setId(1000+numberOfLines);
        sub.setOnClickListener(myClickLIstener);
        String cust_tag=Integer.toString(numberOfLines);
        sub.setTag(cust_tag);
        l2.addView(sub);
        settings = getSharedPreferences("app_pwd", 0);
        settings.edit().putInt("numberoflines",numberOfLines).apply();
    }

    public void checkprevious() {
        settings = getSharedPreferences("app_pwd", 0);
        int nol;
        int count=1;
        nol=settings.getInt("numberoflines",0);
        if(nol>0){
            numberOfLines=nol;
        }
        LinearLayout ll = (LinearLayout)findViewById(R.id.editTextContainer);
        ll.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        for (int i=0;i<nol;i++){
            LinearLayout l2 = new LinearLayout(this);
            l2.setOrientation(LinearLayout.HORIZONTAL);
            Button sub= new Button(this);
            sub.setText("submit");
            sub.setLayoutParams(p);
            sub.setId(1000+numberOfLines);
            sub.setOnClickListener(myClickLIstener);
            String cust_tag=Integer.toString(numberOfLines);
            sub.setTag(cust_tag);
            EditText et = new EditText(this);
            et.setLayoutParams(p);
            et.setId(count);
            count++;
            String key="number"+Integer.toString(count);
            String value=settings.getString(key,"+");
            if(value.equals("+")){
                numberOfLines--;
                settings.edit().putInt("numberoflines",numberOfLines).apply();
            }
            else
            {
                et.setText(value);
                l2.addView(et);
                l2.addView(sub);
            }

            ll.addView(l2);
        }
    }*/
    public void checkprevious(){
        String n1,n2;
        EditText e1=(EditText)findViewById(R.id.number1et);
        EditText e2=(EditText)findViewById(R.id.number2et);
        settings=getSharedPreferences("app_pwd",0);
        n1=settings.getString("num1","0");
        if(!(n1.isEmpty()))
            e1.setText(n1);
        n2=settings.getString("num2","0");
        if(!(n2.isEmpty()))
            e2.setText(n2);
    }
    @Override
    protected void onResume() {
        super.onResume();
        // to check current activity in the navigation drawer
        navigationView.getMenu().getItem(3).setChecked(true);
    }

}
