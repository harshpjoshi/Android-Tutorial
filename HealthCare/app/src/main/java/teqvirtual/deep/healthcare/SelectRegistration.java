package teqvirtual.deep.healthcare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SelectRegistration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_registration);

        getSupportActionBar().hide();

        Button button=(Button)findViewById(R.id.registration_btn_select);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RadioGroup radioGroup=(RadioGroup)findViewById(R.id.user_type);
                RadioButton doc=(RadioButton)findViewById(R.id.doctor_reg_select);
                RadioButton sub=(RadioButton)findViewById(R.id.subscriber_reg_select);

                if (doc.isChecked())
                {
                    startActivity(new Intent(SelectRegistration.this,DoctorRegistration.class));
                }
                else if (sub.isChecked())
                {
                    startActivity(new Intent(SelectRegistration.this,SubscriberRegistration.class));
                }
            }
        });

        TextView registration=(TextView)findViewById(R.id.registration_select);

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SelectRegistration.this,SelectLogin.class);
                startActivity(intent);
            }
        });
    }
}
