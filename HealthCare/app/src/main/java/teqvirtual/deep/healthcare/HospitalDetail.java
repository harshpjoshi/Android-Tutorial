package teqvirtual.deep.healthcare;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import teqvirtual.deep.healthcare.Helper.AsyncResponse;
import teqvirtual.deep.healthcare.Helper.Utils;
import teqvirtual.deep.healthcare.Helper.WebserviceCall;

import static teqvirtual.deep.healthcare.R.font.rubik_medium;

public class HospitalDetail extends AppCompatActivity {

    String hospital_id,name,description,image,latitude,longitude,address,mobileno;
    String url="https://teqvirtual.com/healthcare/index.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_detail);

        Intent intent=getIntent();
        String hosid=intent.getStringExtra("hosid");

        String key[]=new String[]{"action","hospital_id"};
        String val[]=new String[]{"hospital_view",hosid};

        String jsonRequest= Utils.createJsonRequest(key,val);

        new WebserviceCall(HospitalDetail.this, url, jsonRequest, "Loading...", true, new AsyncResponse() {
            @Override
            public void onCallback(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);

                        hospital_id=jsonObject1.getString("hospital_id");
                        name=jsonObject1.getString("hospital_name");
                        description=jsonObject1.getString("description");
                        image=jsonObject1.getString("image");
                        latitude=jsonObject1.getString("latitude");
                        longitude=jsonObject1.getString("longitude");
                        address=jsonObject1.getString("address");
                        mobileno=jsonObject1.getString("mobileno");


                        TextView textView=(TextView) findViewById(R.id.title_hos);
                        textView.setText(name);

                        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hos);
                        setSupportActionBar(toolbar);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setTitle(null);

                        ImageView expandedImage=(ImageView)findViewById(R.id.expandedImage);

                        Picasso.get().load(image).into(expandedImage);

                        TextView hospital_del_name=(TextView)findViewById(R.id.hospital_del_name);
                        hospital_del_name.setText(name);

                        TextView hospital_del_address=(TextView)findViewById(R.id.hospital_del_address);
                        hospital_del_address.setText(address);

                        TextView hospital_del_mobile=(TextView)findViewById(R.id.hospital_del_mobile);
                        hospital_del_mobile.setText(mobileno);

                        TextView hos_description=(TextView)findViewById(R.id.hos_description);
                        hos_description.setText(description);

                        TextView hos_direction=(TextView)findViewById(R.id.hos_direction);

                        hos_direction.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                Intent intent1=new Intent(HospitalDetail.this,MapsActivity.class);
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
