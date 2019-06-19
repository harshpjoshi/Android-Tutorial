package teqvirtual.deep.boranasamaj;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import Helper.AsyncResponse;
import Helper.Utils;
import Helper.WebserviceCall;

public class ForgotPasword extends AppCompatActivity {

    String url="https://teqvirtual.com/BMS/index.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pasword);

        getSupportActionBar().hide();

        final EditText forgot_email=(EditText)findViewById(R.id.forgot_email);

        Button forgot_btn=(Button)findViewById(R.id.forgot_btn);

        forgot_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email=forgot_email.getText().toString();

                if (email.isEmpty())
                {
                    Toast.makeText(ForgotPasword.this, "Email can't Empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String keys[]=new String[]{"action","email"};
                    String[] values=new String[]{"forgot_password",email};


                    String jsonRequest=Utils.createJsonRequest(keys,values);

                    new WebserviceCall(ForgotPasword.this, url, jsonRequest, "Loading...", true, new AsyncResponse() {
                        @Override
                        public void onCallback(String response) {

                            Toast.makeText(ForgotPasword.this, ""+response, Toast.LENGTH_SHORT).show();

                        }
                    }).execute();
                }

            }
        });
    }
}
