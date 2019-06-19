package teqvirtual.deep.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread thread=new Thread(){
            @Override
            public void run() {

                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

                String userid=pref.getString("userid", null);
                String doctorid=pref.getString("doctorid",null);
                if (userid==null && doctorid==null)
                {
                    startActivity(new Intent(MainActivity.this,Login.class));
                }
                else if(userid!=null)
                {
                    startActivity(new Intent(MainActivity.this,SubscriberProfile.class));
                }else if (doctorid!=null)
                {
                    startActivity(new Intent(MainActivity.this,DoctorMainProfile.class));
                }

                finish();
            }
        };

        thread.start();
    }
}
