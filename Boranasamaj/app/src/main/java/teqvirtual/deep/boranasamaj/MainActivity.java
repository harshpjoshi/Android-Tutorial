package teqvirtual.deep.boranasamaj;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.INTERNET;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();


        if(checkPermission())
        {
            Toast.makeText(MainActivity.this, "All Permission are Allowed", Toast.LENGTH_SHORT).show();
        }
        else
        {
            requestPermission();
        }

        Thread thread=new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2000);
                    Animation animation= AnimationUtils.loadAnimation(MainActivity.this,R.anim.fade);
                    TextView textView=(TextView)findViewById(R.id.lname);
                    textView.setAnimation(animation);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
                startActivity(new Intent(MainActivity.this,Select.class));
            }
        };

        thread.start();
    }


    private void requestPermission() {

        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {
                        INTERNET,
                        CALL_PHONE
                }, 200);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 200:

                if (grantResults.length > 0) {

                    boolean internetpermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean callphone = grantResults[1] == PackageManager.PERMISSION_GRANTED;


                    if (internetpermission && callphone) {

                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(MainActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();

                    }
                }

                break;
        }
    }

    public boolean checkPermission() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), INTERNET);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE);


        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED;

    }
}

