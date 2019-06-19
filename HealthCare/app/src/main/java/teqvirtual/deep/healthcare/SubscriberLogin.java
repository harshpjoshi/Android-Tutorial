package teqvirtual.deep.healthcare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import Helper.AsyncResponse;
import Helper.Utils;
import Helper.WebserviceCall;

public class SubscriberLogin extends AppCompatActivity {

    String[] key;
    String[] value;
    String jsonRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriber_login);

        getSupportActionBar().hide();

        final String url="http://teqvirtual.com/healthcare/index.php";

        final EditText email=(EditText)findViewById(R.id.input_email_login_sub);
        final EditText password=(EditText)findViewById(R.id.input_pasword_login_sub);

        Button button=(Button)findViewById(R.id.login_btn_sub);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email_hold=email.getText().toString();
                final String password_hold=password.getText().toString();

                if (email_hold.isEmpty())
                {
                    Toast.makeText(SubscriberLogin.this, "Email Not Empty", Toast.LENGTH_SHORT).show();
                }
                else if (password_hold.isEmpty())
                {
                    Toast.makeText(SubscriberLogin.this, "Password not Empty", Toast.LENGTH_SHORT).show();
                }
                else
                {


                        key=new String[]{"action","email","password"};
                        value=new String[]{"subscriber_login",email_hold,password_hold};
                        jsonRequest=Utils.createJsonRequest(key,value);

                        new WebserviceCall(SubscriberLogin.this, url, jsonRequest, "Loading...", true, new AsyncResponse() {
                            @Override
                            public void onCallback(String response) {

                                try {
                                    JSONObject jsonObject=new JSONObject(response);
                                    Toast.makeText(SubscriberLogin.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    JSONObject jsonObject1=jsonObject.getJSONObject("data");

                                    String id=jsonObject1.getString("subscriber_id");
                                    String firstname=jsonObject1.getString("firstname");
                                    String email=jsonObject1.getString("email");
                                    String image=jsonObject1.getString("image");

                                    Intent intent=new Intent(SubscriberLogin.this,Profile.class);
                                    intent.putExtra("id",id);
                                    intent.putExtra("name",firstname);
                                    intent.putExtra("email",email);
                                    intent.putExtra("image",image);
                                    startActivity(intent);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        }).execute();
                    }
                }
        });

        TextView registration=(TextView)findViewById(R.id.registration_sub);

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
}
