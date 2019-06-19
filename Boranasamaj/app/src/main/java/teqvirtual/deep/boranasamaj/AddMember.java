package teqvirtual.deep.boranasamaj;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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

public class AddMember extends AppCompatActivity {

    EditText m_name,m_birthdate,m_address,m_city,m_mobile,m_occupation;
    Button add_member;
    ImageView member_img;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        final ProgressDialog progressDialog=new ProgressDialog(AddMember.this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        m_name=(EditText)findViewById(R.id.member_name);
        m_birthdate=(EditText)findViewById(R.id.member_birthdate);
        m_address=(EditText)findViewById(R.id.member_address);
        m_city=(EditText)findViewById(R.id.member_city);
        m_mobile=(EditText)findViewById(R.id.member_mobile);
        m_occupation=(EditText)findViewById(R.id.member_occupation);

        member_img=(ImageView)findViewById(R.id.member_img);

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

                m_birthdate.setText(sdf.format(myCalendar.getTime()));
            }
        };

        m_birthdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddMember.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        member_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fileChooser();

            }
        });

        add_member=(Button)findViewById(R.id.addmember);

        add_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String m_name_hold=m_name.getText().toString();
                final String m_birthdate_hold=m_birthdate.getText().toString();
                final String m_address_hold=m_address.getText().toString();
                final String m_city_hold=m_city.getText().toString();
                final String m_mobile_hold=m_mobile.getText().toString();
                final String m_occupation_hold=m_occupation.getText().toString();

                if (m_name_hold.isEmpty() && m_birthdate_hold.isEmpty() && m_address_hold.isEmpty() && m_city_hold.isEmpty() && m_mobile_hold.isEmpty() && m_occupation_hold.isEmpty())
                {
                    m_name.setError("Name can't Empty");
                    m_birthdate.setError("Birthdate can't empty");
                    m_address.setError("Address can't empty");
                    m_city.setError("City can't empty");
                    m_mobile.setError("Mobilno can't empty");
                    m_occupation.setError("Occupation can't empty");
                }
                if (m_name_hold.isEmpty())
                {
                    m_name.setError("Name can't Empty");
                }
                if (m_birthdate_hold.isEmpty())
                {
                    m_birthdate.setError("Birthdate can't empty");
                }
                if (m_address_hold.isEmpty())
                {
                    m_address.setError("Address can't empty");
                }
                if (m_city_hold.isEmpty())
                {
                    m_city.setError("City can't empty");
                }
                if (m_mobile_hold.isEmpty())
                {
                    m_mobile.setError("Mobilno can't empty");
                }
                if (m_occupation_hold.isEmpty())
                {
                    m_occupation.setError("Occupation can't empty");
                }
                else
                {
                    StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://teqvirtual.com/Boranasamaj/addmember.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.setMessage("Please Wait....");
                            progressDialog.show();
                            Toast.makeText(AddMember.this, ""+response, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(AddMember.this, ""+error, Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String,String> map=new HashMap<>();
                            map.put("name",m_name_hold);
                            map.put("dob",m_birthdate_hold.toString());
                            map.put("address",m_address_hold);
                            map.put("city",m_city_hold);
                            map.put("mobileno",m_mobile_hold);
                            map.put("occupation",m_occupation_hold);
                            map.put("image",getStringImage(bitmap));
                            return map;
                        }
                    };

                    RequestQueue requestQueue= Volley.newRequestQueue(AddMember.this);
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
