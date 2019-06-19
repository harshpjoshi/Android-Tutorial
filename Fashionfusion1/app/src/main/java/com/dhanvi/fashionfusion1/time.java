package com.dhanvi.fashionfusion1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class time extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        getSupportActionBar().hide();
        SharedPreferences prefs = getSharedPreferences("mypref", MODE_PRIVATE);
        final String restoredText = prefs.getString("name", null);

        Thread th= new Thread()
        {
            @Override
            public void run() {
                super.run();

                try {
                    sleep(3000);
                    if (restoredText==null){
                    Intent in=new Intent(time.this,fashion.class);
                    startActivity(in);}
                    else {
                        Intent in=new Intent(time.this,nav_main.class);
                        startActivity(in);
                    }
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        th.start();

    }
}
