package teqvirtual.deep.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.material.button.MaterialButton;


public class SelectSignup extends AppCompatActivity {

    String slect_type;
    MaterialButton selectButton;
    String[] type={"Select User Type","Subscriber","Doctor"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_signup);

        Spinner spinner = (Spinner) findViewById(R.id.signup_type);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(SelectSignup.this,android.R.layout.simple_spinner_dropdown_item,type);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                slect_type=type[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        selectButton=(MaterialButton)findViewById(R.id.button_select_signup);

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (slect_type.equals("Subscriber"))
                {
                    startActivity(new Intent(SelectSignup.this,SubscriberSignup.class));
                }
                else if (slect_type.equals("Doctor"))
                {
                    startActivity(new Intent(SelectSignup.this,DoctorSignup.class));
                }

            }
        });

    }
}
