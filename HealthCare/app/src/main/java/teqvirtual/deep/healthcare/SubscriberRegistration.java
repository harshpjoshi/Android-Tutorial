package teqvirtual.deep.healthcare;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import Adapter.CityAdapter;
import Helper.AsyncResponse;
import Helper.Utils;
import Helper.WebserviceCall;
import Model.CityModel;

public class SubscriberRegistration extends AppCompatActivity {

    Bitmap bitmap;
    ImageView sub_image;
    EditText firstname_sub,lastname_sub,email_sub,password_sub,contact_sub,address_sub;
    Spinner city_sub;
    String city_holder;
    ArrayList<CityModel> arrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriber_registration);
        getSupportActionBar().hide();

        firstname_sub=(EditText)findViewById(R.id.input_firstname_login_sub);
        lastname_sub=(EditText)findViewById(R.id.input_lastname_login_sub);
        email_sub=(EditText)findViewById(R.id.input_email_login_sub);
        password_sub=(EditText)findViewById(R.id.input_pasword_login_sub);
        contact_sub=(EditText)findViewById(R.id.input_contactno_login_sub);
        sub_image=(ImageView) findViewById(R.id.logo);
        city_sub=(Spinner)findViewById(R.id.city_sub);
        address_sub=(EditText)findViewById(R.id.input_address_login_sub);
        Button button=(Button)findViewById(R.id.login_btn_sub);

        final String url="http://teqvirtual.com/healthcare/index.php";

        String c_key[]=new String[]{"action"};
        String c_value[]=new String[]{"city_view"};

        String c_jsonRequest=Utils.createJsonRequest(c_key,c_value);

        new WebserviceCall(SubscriberRegistration.this, url, c_jsonRequest, "Loading...", true, new AsyncResponse() {
            @Override
            public void onCallback(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);

                    JSONArray jsonArray=jsonObject.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String id=jsonObject1.getString("city_id");
                        String state_id=jsonObject1.getString("state_id");
                        String name=jsonObject1.getString("name");

                        CityModel cityModel=new CityModel();
                        cityModel.setId(id);
                        cityModel.setStateid(state_id);
                        cityModel.setName(name);

                        arrayList.add(cityModel);
                    }

                    CityAdapter cityAdapter=new CityAdapter(SubscriberRegistration.this,arrayList);
                    city_sub.setAdapter(cityAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).execute();

        city_sub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                city_holder=arrayList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fname_holder=firstname_sub.getText().toString().trim();
                String lname_holder=lastname_sub.getText().toString().trim();
                String email_holder=email_sub.getText().toString().trim();
                String password_holder=password_sub.getText().toString().trim();
                String contact_holder=contact_sub.getText().toString().trim();
                String image_holder=getStringImage(bitmap);
                String address_holder=address_sub.getText().toString().trim();

                if (fname_holder.isEmpty())
                {
                    Toast.makeText(SubscriberRegistration.this, "Firstname Not Empty", Toast.LENGTH_SHORT).show();
                }
                else if (lname_holder.isEmpty())
                {
                    Toast.makeText(SubscriberRegistration.this, "Lastname Not Empty", Toast.LENGTH_SHORT).show();
                }
                else if (email_holder.isEmpty())
                {
                    Toast.makeText(SubscriberRegistration.this, "Email Not Empty", Toast.LENGTH_SHORT).show();
                }
                else if (password_holder.isEmpty())
                {
                    Toast.makeText(SubscriberRegistration.this, "Password Not Empty", Toast.LENGTH_SHORT).show();
                }
                else if (contact_holder.isEmpty())
                {
                    Toast.makeText(SubscriberRegistration.this, "Mobileno Not Empty", Toast.LENGTH_SHORT).show();
                }
                else if (image_holder.isEmpty())
                {
                    Toast.makeText(SubscriberRegistration.this, "Please Choose Profile Image", Toast.LENGTH_SHORT).show();
                }
                else if (address_holder.isEmpty())
                {
                    Toast.makeText(SubscriberRegistration.this, "Address Can't Empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String key[]=new String[]{"action","firstname","lastname","email","mobileno","image","password","city","address"};
                    String[] value=new String[]{"subscriber_registration",fname_holder,lname_holder,email_holder,contact_holder,image_holder,password_holder,city_holder,address_holder};

                    String jsonRequest=Utils.createJsonRequest(key,value);

                    new WebserviceCall(SubscriberRegistration.this, url, jsonRequest, "Loading...", true, new AsyncResponse() {
                        @Override
                        public void onCallback(String response) {

                            try {

                                JSONObject jsonObject=new JSONObject(response);

                                Toast.makeText(SubscriberRegistration.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                JSONArray jsonArray=jsonObject.getJSONArray("data");



                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    String subscriber_id=jsonObject1.getString("subscriber_id");
                                    String email=jsonObject1.getString("email");
                                    String firstname=jsonObject1.getString("firstname");
                                    String image=jsonObject1.getString("image");

                                    Intent intent=new Intent(SubscriberRegistration.this,Profile.class);
                                    intent.putExtra("id",subscriber_id);
                                    intent.putExtra("email",email);
                                    intent.putExtra("name",firstname);
                                    intent.putExtra("image",image);
                                    startActivity(intent);

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }).execute();
                }
            }
        });

        sub_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileChooser();
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
               sub_image.setVisibility(View.VISIBLE);
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                sub_image.setImageBitmap(bitmap);
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
        if (bmp!=null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            String encodedimage = Base64.encodeToString(bytes, Base64.DEFAULT);
            return encodedimage;
        }
        return null;
    }
}
