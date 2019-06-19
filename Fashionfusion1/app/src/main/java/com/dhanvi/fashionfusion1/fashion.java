package com.dhanvi.fashionfusion1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dhanvi.fashionfusion1.database_call.NetworkCall;
import com.dhanvi.fashionfusion1.database_call.jsn;

import org.json.JSONObject;

import java.util.HashMap;

public class fashion extends AppCompatActivity {
    Button btn;
    EditText edt, edt1;
    TextView txt,text;
    String emailid,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fashion);
        getSupportActionBar().hide();
         edt=(EditText)findViewById(R.id.editText);
         edt1=(EditText)findViewById(R.id.editText3);
         txt=(TextView)findViewById(R.id.textView4);
         txt.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent=new Intent(fashion.this,forgotpassword.class);
                 startActivity(intent);
             }
         });
         text=(TextView)findViewById(R.id.text);
         text.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent=new Intent(fashion.this,rejistration.class);
                 startActivity(intent);
             }
         });
        Intent intent=new Intent(fashion.this,nav_main.class);
        Button btn=(Button)findViewById(R.id.button);
                btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
               emailid=edt.getText().toString();
               password=edt1.getText().toString();

               if (emailid.isEmpty())
               {
                   edt.setError("plz enter email or phone number");
               }
               else if (password.isEmpty()){

                   edt1.setError("plz enter password");
               }
               else {
                   signIn();

               }
            }
        });

    }

    private void signIn() {

        HashMap<String, String> param=new HashMap<>();
        param.put("type","signIn");
        param.put("userName",emailid);
        param.put("password",password);
        NetworkCall.call(param).setDataResponseListener(new NetworkCall.SetDataResponse() {
            @Override
            public void setResponse(String responseStr) {
                if (jsn.checkResponseStr(responseStr)){
                    JSONObject jsonObjectAt0 = jsn.getJSONObjectAt0(responseStr);
                    MyParam.userId = jsn.getJSONString(jsonObjectAt0,"id");
                    saveSharedPref(MyParam.userId);
                    startMainActivity();
                }else {
                    Toast.makeText(fashion.this, "try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void startMainActivity() {
        Intent intent=new Intent(fashion.this, nav_main.class);
        startActivity(intent);
    }

    private void saveSharedPref(String userId) {
        SharedPreferences.Editor editor = getSharedPreferences("mypref", MODE_PRIVATE).edit();
        editor.putString("name", emailid);
        editor.putString("id", userId);
        editor.apply();
    }
}
