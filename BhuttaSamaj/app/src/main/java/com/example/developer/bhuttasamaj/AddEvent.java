package com.example.developer.bhuttasamaj;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddEvent extends AppCompatActivity {

    Bitmap bitmap;
    ImageView event_image;
    EditText e_name,e_date,e_time,e_location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        final ProgressDialog progressDialog=new ProgressDialog(AddEvent.this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        e_name=(EditText)findViewById(R.id.event_name);
        e_date=(EditText)findViewById(R.id.event_date);
        e_time=(EditText)findViewById(R.id.event_time);
        e_location=(EditText)findViewById(R.id.event_location);
        event_image=(ImageView)findViewById(R.id.event_img);

        event_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileChooser();
            }
        });

        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
            private void updateLabel() {
                String myFormat = "yy/MM/dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                e_date.setText(sdf.format(myCalendar.getTime()));
            }
        };

        e_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddEvent.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

      final TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.MINUTE,minute);
                myCalendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                updateLabel();
            }

            private void updateLabel() {
                String myFormat = "hh:MM"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                e_time.setText(sdf.format(myCalendar.getTime()));
            }
        };

        e_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
               new TimePickerDialog(AddEvent.this,timeSetListener,myCalendar.get(Calendar.HOUR_OF_DAY),myCalendar.get(Calendar.MINUTE),false).show();
            }
        });


        Button addevent=(Button)findViewById(R.id.addevent);

        addevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String e_name_hold=e_name.getText().toString();
                final String e_date_hold=e_date.getText().toString();
                final String e_time_hold=e_time.getText().toString();
                final String e_location_hold=e_location.getText().toString();


                if (e_name_hold.isEmpty() && e_date_hold.isEmpty() && e_time_hold.isEmpty() && e_location_hold.isEmpty())
                {
                    e_name.setError("Name can't Empty");
                    e_date.setError("date can't empty");
                    e_time.setError("Time can't empty");
                    e_location.setError("Location can't empty");
                }
                if (e_name_hold.isEmpty())
                {
                    e_name.setError("Name can't Empty");
                }
                if (e_date_hold.isEmpty())
                {
                    e_date.setError("date can't empty");
                }
                if (e_time_hold.isEmpty())
                {
                    e_time.setError("Time can't empty");
                }
                if (e_location_hold.isEmpty())
                {
                    e_location.setError("Location can't empty");
                }
                else{
                    StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://teqvirtual.com/Boranasamaj/addevent.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            progressDialog.setMessage("Please Wait.....");
                            progressDialog.show();
                            Toast.makeText(AddEvent.this, ""+response, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(AddEvent.this, ""+error, Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String,String> map=new HashMap<>();
                            map.put("name",e_name_hold);
                            map.put("time",e_time_hold);
                            map.put("date",e_date_hold);
                            map.put("location",e_location_hold);
                            map.put("image",getStringImage(bitmap));
                            return map;
                        }
                    };

                    RequestQueue requestQueue= Volley.newRequestQueue(AddEvent.this);
                    requestQueue.add(stringRequest);

                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == this.RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                event_image.setVisibility(View.VISIBLE);
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                event_image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void fileChooser()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    public String getStringImage(Bitmap bmp)
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] bytes=byteArrayOutputStream.toByteArray();
        String encodedimage= Base64.encodeToString(bytes,Base64.DEFAULT);
        return encodedimage;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        if (id==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
