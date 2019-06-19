package teqvirtual.deep.boranasamaj;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Advertise extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertise);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ProgressDialog progressDialog=new ProgressDialog(Advertise.this);
        final ListView listView=(ListView)findViewById(R.id.adv_list);

        progressDialog.setMessage("Please Wait.....");
        progressDialog.show();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://teqvirtual.com/Boranasamaj/fetchadvertise.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();
                JSONArray jsonArray= null;
                try {
                    ArrayList<teqvirtual.deep.boranasamaj.AdvertiseData> advertiseData=new ArrayList<>();
                    jsonArray = new JSONArray(response);
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String image=jsonObject.getString("image");
                        String description=jsonObject.getString("description");
                        String mobile=jsonObject.getString("mobile");

                        teqvirtual.deep.boranasamaj.AdvertiseData advertiseData1=new teqvirtual.deep.boranasamaj.AdvertiseData();
                        advertiseData1.setDescription(description);
                        advertiseData1.setImage(image);
                        advertiseData1.setMobile(mobile);

                        advertiseData.add(advertiseData1);

                    }

                    AdvertiseDataAdapter advertiseDataAdapter=new AdvertiseDataAdapter(Advertise.this,advertiseData);
                    listView.setAdapter(advertiseDataAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Advertise.this, ""+error, Toast.LENGTH_SHORT).show();

            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(Advertise.this);
        requestQueue.add(stringRequest);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        if(id==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);

    }

}
