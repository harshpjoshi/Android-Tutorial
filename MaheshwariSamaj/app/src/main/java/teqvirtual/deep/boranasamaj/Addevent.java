package teqvirtual.deep.boranasamaj;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import Helper.AsyncResponse;
import Helper.Utils;
import Helper.WebserviceCall;

public class Addevent extends AppCompatActivity {

    EditText e_name,e_date,e_location,e_time;
    Button add_event;
    ImageView event_img;
    Bitmap bitmap;

    String url="https://teqvirtual.com/BMS/index.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addevent);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_event_add);
        toolbar.setTitleTextColor(getResources().getColor(R.color.font));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        e_name=(EditText)findViewById(R.id.event_name);
        e_date=(EditText)findViewById(R.id.event_date);
        e_location=(EditText)findViewById(R.id.event_location);
        e_time=(EditText)findViewById(R.id.event_time);
        add_event=(Button)findViewById(R.id.add_event);


        event_img=(ImageView)findViewById(R.id.event_image);

        event_img.setOnClickListener(new View.OnClickListener() {
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
                new DatePickerDialog(Addevent.this, date, myCalendar
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
                new TimePickerDialog(Addevent.this,timeSetListener,myCalendar.get(Calendar.HOUR_OF_DAY),myCalendar.get(Calendar.MINUTE),false).show();
            }
        });

        add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String e_name_hold=e_name.getText().toString();
                final String e_date_hold=e_date.getText().toString();
                final String e_time_hold=e_time.getText().toString();
                final String e_location_hold=e_location.getText().toString();

                if (e_date_hold.isEmpty())
                {
                    Toast.makeText(Addevent.this, "Date Not Empty", Toast.LENGTH_SHORT).show();
                }
                else if (e_name_hold.isEmpty())
                {
                    Toast.makeText(Addevent.this, "Name Not Empty", Toast.LENGTH_SHORT).show();
                }
                else if (e_time_hold.isEmpty())
                {
                    Toast.makeText(Addevent.this, "Time Not Empty", Toast.LENGTH_SHORT).show();
                }
                else if (e_location_hold.isEmpty())
                {
                    Toast.makeText(Addevent.this, "Location Not Empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String image_holder=getStringImage(bitmap);

                    String keys[]=new String[]{"action","event_name","event_date","event_time","event_location","event_image"};
                    String values[]=new String[]{"event_add",e_name_hold,e_date_hold,e_time_hold,e_location_hold,image_holder};

                    String jsonrequest= Utils.createJsonRequest(keys,values);

                    new WebserviceCall(Addevent.this, url, jsonrequest, "Adding Event...", true, new AsyncResponse() {
                        @Override
                        public void onCallback(String response) {

                            JSONObject jsonObject= null;
                            try {
                                jsonObject = new JSONObject(response);
                                String message=jsonObject.getString("message");
                                Toast.makeText(Addevent.this, ""+message, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Addevent.this,AdminViewEvent.class));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }).execute();
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
                event_img.setVisibility(View.VISIBLE);
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                event_img.setImageBitmap(bitmap);
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
        String encodedimage= Base64.encodeToString(bytes, Base64.DEFAULT);
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
