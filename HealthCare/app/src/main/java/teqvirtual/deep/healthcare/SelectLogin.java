package teqvirtual.deep.healthcare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import Helper.AsyncResponse;
import Helper.Utils;
import Helper.WebserviceCall;

public class SelectLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_login);

        getSupportActionBar().hide();

        Button button=(Button)findViewById(R.id.login_btn_select);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    RadioGroup radioGroup=(RadioGroup)findViewById(R.id.user_type);
                    RadioButton doc=(RadioButton)findViewById(R.id.doctor_reg);
                    RadioButton sub=(RadioButton)findViewById(R.id.subscriber_reg);

                    if (doc.isChecked())
                    {
                        startActivity(new Intent(SelectLogin.this,DoctorLogin.class));
                    }
                    else if (sub.isChecked())
                    {
                        startActivity(new Intent(SelectLogin.this,SubscriberLogin.class));
                    }
            }
        });

        TextView registration=(TextView)findViewById(R.id.registration_select);

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SelectLogin.this,SelectRegistration.class);
                startActivity(intent);
            }
        });
    }
}
