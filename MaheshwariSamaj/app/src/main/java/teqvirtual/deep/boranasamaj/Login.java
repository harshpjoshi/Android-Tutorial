package teqvirtual.deep.boranasamaj;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import Helper.AsyncResponse;
import Helper.Utils;
import Helper.WebserviceCall;

public class Login extends AppCompatActivity {

    String url="https://teqvirtual.com/BMS/index.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        final EditText email_log=(EditText)findViewById(R.id.login_email);
        final EditText password_log=(EditText)findViewById(R.id.login_password);

        Button login_btn=(Button)findViewById(R.id.login_btn);

        TextView forgot_password=(TextView)findViewById(R.id.login_forgot_password);

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,ForgotPasword.class));
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emai_holder=email_log.getText().toString().trim();
                String password_holder=password_log.getText().toString().trim();

                if (emai_holder.isEmpty())
                {
                    Toast.makeText(Login.this, "Email Not Empty", Toast.LENGTH_SHORT).show();
                }
                else if (password_holder.isEmpty())
                {
                    Toast.makeText(Login.this, "Password Not Empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String[] keys=new String[]{"action","email","password"};
                    String[] values=new String[]{"login",emai_holder,password_holder};

                    final String jsonrequest= Utils.createJsonRequest(keys,values);


                    new WebserviceCall(Login.this, url, jsonrequest, "Login in...", true, new AsyncResponse() {
                        @Override
                        public void onCallback(String response) {
                            try {

                                JSONObject jsonObject=new JSONObject(response);

                                String message=jsonObject.getString("message");
                                Toast.makeText(Login.this, ""+message, Toast.LENGTH_SHORT).show();

                                if (!message.equals("Invalid email or password."))
                                {
                                    startActivity(new Intent(Login.this,Adminprofile.class));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }



                        }
                    }).execute();
                }

            }
        });
    }
}
