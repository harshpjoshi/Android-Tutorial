package teqvirtual.deep.boranasamaj;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.squareup.picasso.Picasso;

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

public class UpdateMember extends AppCompatActivity {

    EditText m_name_up, m_birthdate_up, m_address_up, m_city_up, m_mobile_up, m_occupation_up;
    Button update_member_btn;
    ImageView member_img_up;
    Bitmap bitmap;

    DatePickerDialog.OnDateSetListener dateListener;

    String url="https://teqvirtual.com/BMS/index.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_member);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_member_update);
        toolbar.setTitleTextColor(getResources().getColor(R.color.font));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        String member_name = intent.getStringExtra("member_name");
        String member_occupation = intent.getStringExtra("member_occupation");
        String member_birthdate = intent.getStringExtra("member_dob");
        String member_address = intent.getStringExtra("member_address");
        final String member_city = intent.getStringExtra("member_city");
        String member_mobile = intent.getStringExtra("member_mobile");
        final String member_image = intent.getStringExtra("member_image");
        final String member_id=intent.getStringExtra("member_id");

        Toast.makeText(this, ""+member_name, Toast.LENGTH_SHORT).show();

        m_name_up=(EditText)findViewById(R.id.update_member_name);
        m_birthdate_up=(EditText)findViewById(R.id.update_member_birthday);
        m_address_up=(EditText)findViewById(R.id.update_member_address);
        m_city_up=(EditText)findViewById(R.id.update_member_city);
        m_mobile_up=(EditText)findViewById(R.id.update_member_mobile);
        m_occupation_up=(EditText)findViewById(R.id.update_member_occupation);
        member_img_up=(ImageView)findViewById(R.id.update_member_image);
        update_member_btn=(Button)findViewById(R.id.update_add_member);

        m_name_up.setText(member_name);
        m_occupation_up.setText(member_occupation);
        m_birthdate_up.setText(member_birthdate);
        m_address_up.setText(member_address);
        m_city_up.setText(member_city);
        m_mobile_up.setText(member_mobile);

        Picasso.get().load(member_image).into(member_img_up);

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
                m_birthdate_up.setText(sdf.format(calendar.getTime()));

            }
        };

        m_birthdate_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(UpdateMember.this, dateListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.MONTH)).show();

                Log.d("year iss","hello  "+calendar.get(Calendar.YEAR));
            }
        });

        member_img_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fileChooser();

            }
        });


        update_member_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String member_name_holder=m_name_up.getText().toString().trim();
                String member_birtdate_holder=m_birthdate_up.getText().toString().trim();
                String member_address_holder=m_address_up.getText().toString().trim();
                String member_city_holder_hoder=m_city_up.getText().toString().trim();
                String member_mobile_holder=m_mobile_up.getText().toString().trim();
                String member_occupation_holder=m_occupation_up.getText().toString().trim();

                if (member_name_holder.isEmpty())
                {
                    Toast.makeText(UpdateMember.this, "Name Not Empty", Toast.LENGTH_SHORT).show();
                }
                else if (member_birtdate_holder.isEmpty())
                {
                    Toast.makeText(UpdateMember.this, "Birthdate Not Empty", Toast.LENGTH_SHORT).show();
                }
                else if (member_address_holder.isEmpty())
                {
                    Toast.makeText(UpdateMember.this, "Address Not Empty", Toast.LENGTH_SHORT).show();
                }
                else if (member_mobile_holder.isEmpty())
                {
                    Toast.makeText(UpdateMember.this, "Mobileno Not Empty", Toast.LENGTH_SHORT).show();
                }
                else if (member_city_holder_hoder.isEmpty())
                {
                    Toast.makeText(UpdateMember.this, "City Not Empty", Toast.LENGTH_SHORT).show();
                }
                else if (member_occupation_holder.isEmpty())
                {
                    Toast.makeText(UpdateMember.this, "Occupation Not Empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String update_image;

                        update_image=getStringImage(bitmap);


                        String keys[]=new String[]{"action","member_name","member_address","member_city","member_occupation","member_dob","member_mobile","member_image","id"};
                        String values[]=new String[]{"member_update",member_name_holder,member_address_holder,member_city_holder_hoder,member_occupation_holder,member_birtdate_holder,member_mobile_holder,update_image,member_id};

                        String jsonRequest=Utils.createJsonRequest(keys,values);


                        new WebserviceCall(UpdateMember.this, url, jsonRequest, "Updating...", true, new AsyncResponse() {
                            @Override
                            public void onCallback(String response) {

                                try {
                                   JSONObject jsonObject = new JSONObject(response);
                                    String message=jsonObject.getString("message");
                                    Toast.makeText(UpdateMember.this, ""+message, Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(UpdateMember.this,AdminViewMember.class));
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
                member_img_up.setVisibility(View.VISIBLE);
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                member_img_up.setImageBitmap(bitmap);
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
      if (bmp!=null)
      {
          ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
          bmp.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
          byte[] bytes=byteArrayOutputStream.toByteArray();
          String encodedimage= Base64.encodeToString(bytes, Base64.DEFAULT);
          return encodedimage;
      }
       return null;
    }

    public Bitmap StringToBitMap(String image){
        try{
            byte [] encodeByte=Base64.decode(image,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
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