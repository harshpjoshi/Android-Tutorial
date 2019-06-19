package teqvirtual.deep.boranasamaj;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Option extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        getSupportActionBar().hide();

        Button user_btn=(Button)findViewById(R.id.user_btn);
        Button admin_btmn=(Button)findViewById(R.id.admin_btn);

        user_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Option.this,Userprofile.class));
            }
        });

        admin_btmn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Option.this,Login.class));
            }
        });
    }
}
