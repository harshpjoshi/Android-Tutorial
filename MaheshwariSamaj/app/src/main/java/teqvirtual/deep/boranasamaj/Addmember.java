package teqvirtual.deep.boranasamaj;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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

public class Addmember extends AppCompatActivity {

    EditText m_name,m_birthdate,m_address,m_city,m_mobile,m_occupation;
    Button add_member;
    ImageView member_img;
    Bitmap bitmap;

    int ayear,amonth,adate;
    DatePickerDialog.OnDateSetListener dateListener;

    String url="https://teqvirtual.com/BMS/index.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmember);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_member_add);
        toolbar.setTitleTextColor(getResources().getColor(R.color.font));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        m_name=(EditText)findViewById(R.id.member_name);
        m_birthdate=(EditText)findViewById(R.id.member_birthday);
        m_address=(EditText)findViewById(R.id.member_address);
        m_city=(EditText)findViewById(R.id.member_city);
        m_mobile=(EditText)findViewById(R.id.member_mobile);
        m_occupation=(EditText)findViewById(R.id.member_occupation);

        member_img=(ImageView)findViewById(R.id.member_image);


        final Calendar calendar=Calendar.getInstance();

        dateListener =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);

                Log.d("year iss","hello  "+year);

                String myFormat = "yyyy/MM/dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                m_birthdate.setText(sdf.format(calendar.getTime()));

            }
        };

        m_birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(Addmember.this, dateListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.MONTH)).show();

                Log.d("year iss","hello  "+calendar.get(Calendar.YEAR));
            }
        });

        member_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fileChooser();

            }
        });

        add_member=(Button)findViewById(R.id.add_member);

        add_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String m_name_hold = m_name.getText().toString();
                final String m_birthdate_hold = m_birthdate.getText().toString();
                final String m_address_hold = m_address.getText().toString();
                final String m_city_hold = m_city.getText().toString();
                final String m_mobile_hold = m_mobile.getText().toString();
                final String m_occupation_hold = m_occupation.getText().toString();


                Log.d("main","dob "+m_birthdate_hold);

                if (m_name_hold.isEmpty())
                {
                    Toast.makeText(Addmember.this, "Name Not Empty", Toast.LENGTH_SHORT).show();
                }
                if (m_birthdate_hold.isEmpty())
                {
                    Toast.makeText(Addmember.this, "Birthdate Not Empty", Toast.LENGTH_SHORT).show();
                }
                if (m_address_hold.isEmpty())
                {
                    Toast.makeText(Addmember.this, "Address Not Empty", Toast.LENGTH_SHORT).show();
                }
                if (m_city_hold.isEmpty())
                {
                    Toast.makeText(Addmember.this, "City Not Empty", Toast.LENGTH_SHORT).show();
                }
                if (m_mobile_hold.isEmpty())
                {
                    Toast.makeText(Addmember.this, "Mobileno Not Empty", Toast.LENGTH_SHORT).show();
                }
                if (m_occupation_hold.isEmpty())
                {
                    Toast.makeText(Addmember.this, "Occupation Not Empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String image_holder=getStringImage(bitmap);

                    String keys[]=new String[]{"action","member_name","member_dob","member_address","member_city","member_mobile","member_occupation","member_image"};
                    String values[]=new String[]{"member_add",m_name_hold,m_birthdate_hold,m_address_hold,m_city_hold,m_mobile_hold,m_occupation_hold,image_holder};

                    String jsonrequest= Utils.createJsonRequest(keys,values);

                    new WebserviceCall(Addmember.this, url, jsonrequest, "Adding Member...", true, new AsyncResponse() {
                        @Override
                        public void onCallback(String response) {
                            JSONObject jsonObject= null;
                            try {

                                jsonObject = new JSONObject(response);
                                String message=jsonObject.getString("message");
                                Toast.makeText(Addmember.this, ""+message, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Addmember.this,AdminViewMember.class));
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
    protected Dialog onCreateDialog(int id) {

        if (id==999)
        {
            return new DatePickerDialog(this,dateListener,ayear,amonth,adate);
        }
        return null;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == this.RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                member_img.setVisibility(View.VISIBLE);
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                member_img.setImageBitmap(bitmap);
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
