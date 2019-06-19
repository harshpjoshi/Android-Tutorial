package com.dhanvi.fashionfusion1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dhanvi.fashionfusion1.database_call.NetworkCall;
import com.dhanvi.fashionfusion1.database_call.jsn;

import java.security.AccessController;
import java.security.Provider;
import java.util.HashMap;

public class forgotpassword extends AppCompatActivity {
EditText editText;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_forgotpassword);
        editText=(EditText)findViewById(R.id.editText2);
        btn=(Button)findViewById(R.id.fg_btn);
       btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String email = editText.getText().toString();
               sendEmailFromDB(email);
//               sendEmail();
           }
       });
}

    private void sendEmailFromDB(String email) {
        HashMap<String, String> param=new HashMap<>();
        param.put("type","forgetPassword");
        param.put("Email",email);

        NetworkCall.call(param).setDataResponseListener(new NetworkCall.SetDataResponse() {
            @Override
            public void setResponse(String responseStr) {
                if (jsn.checkResponseStr(responseStr)){
                    Toast.makeText(forgotpassword.this, "Password sent successfully..", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(forgotpassword.this, "Insert valid registered email...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("LongLogTag")
     protected void sendEmail() {
         Log.i("Send email", "");
         String[] TO = {"fashion.fusion@gmail.com"};
         String[] BCC = {"fashion.fusion@gmail.com"};
        // String[] FROM = {"fashion.fusion@gmail.com"};
         String[] CC = {"fashion.fusion@gmail.com"};
         Intent emailIntent = new Intent(Intent.ACTION_SEND);
         emailIntent.setData(Uri.parse("mailto:"));
         emailIntent.setType("text/plain");
         emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
         emailIntent.putExtra(Intent.EXTRA_CC, CC);
         emailIntent.putExtra(Intent.EXTRA_BCC, BCC);
        // emailIntent.putExtra(Intent.EXTRA_FROM_STORAGE, FROM);
         emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
         emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

         try {
             startActivity(Intent.createChooser(emailIntent, "Send mail..."));
             finish();
             Log.i("Finished sending email...", "");

         } catch (android.content.ActivityNotFoundException ex) {
             Toast.makeText(forgotpassword.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
         }
     }
 }
