package teqvirtual.deep.healthcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import teqvirtual.deep.healthcare.Helper.AsyncResponse;
import teqvirtual.deep.healthcare.Helper.Utils;
import teqvirtual.deep.healthcare.Helper.WebserviceCall;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MedicalstoreDetail extends AppCompatActivity {

    String hospital_id,name,description,image,latitude,longitude,address,mobileno;
    String url="https://teqvirtual.com/healthcare/index.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicalstore_detail);

        Intent intent=getIntent();
        String hosid=intent.getStringExtra("medicalid");

        String key[]=new String[]{"action","medicalstore_id"};
        String val[]=new String[]{"medical_view",hosid};

        String jsonRequest= Utils.createJsonRequest(key,val);

        new WebserviceCall(MedicalstoreDetail.this, url, jsonRequest, "Loading...", true, new AsyncResponse() {
            @Override
            public void onCallback(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);

                    JSONArray jsonArray=jsonObject.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);

                        String medicalstore_id=jsonObject1.getString("medicalstore_id");
                        String medical_name=jsonObject1.getString("medical_name");
                        String description=jsonObject1.getString("description");
                        String mobileno=jsonObject1.getString("mobileno");
                        String email=jsonObject1.getString("email");
                        String image=jsonObject1.getString("image");
                        String latitude=jsonObject1.getString("latitude");
                        String longitude=jsonObject1.getString("longitude");
                        String address=jsonObject1.getString("address");

                        TextView textView=(TextView) findViewById(R.id.title_medical);
                        textView.setText(medical_name);

                        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                        setSupportActionBar(toolbar);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setTitle(null);

                        ImageView expandedImage=(ImageView)findViewById(R.id.expandedImage);

                        Picasso.get().load(image).into(expandedImage);

                        TextView hospital_del_name=(TextView)findViewById(R.id.medical_del_name);
                        hospital_del_name.setText(medical_name);

                        TextView hospital_del_address=(TextView)findViewById(R.id.medical_del_address);
                        hospital_del_address.setText(address);

                        TextView hospital_del_mobile=(TextView)findViewById(R.id.medical_del_mobile);
                        hospital_del_mobile.setText(mobileno);

                        TextView hos_description=(TextView)findViewById(R.id.medical_description);
                        hos_description.setText(description);

                        TextView hos_direction=(TextView)findViewById(R.id.medical_direction);

                        hos_direction.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                Intent intent1=new Intent(MedicalstoreDetail.this,MapsActivity.class);
                                intent1.putExtra("lat",latitude);
                                intent1.putExtra("longi",longitude);
                                startActivity(intent1);


                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).execute();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
