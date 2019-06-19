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

public class Event extends AppCompatActivity {



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        final ProgressDialog progressDialog=new ProgressDialog(Event.this);
        progressDialog.setMessage("Please Wait....");
        progressDialog.show();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final ListView listView=(ListView)findViewById(R.id.event_list);

        final ArrayList<Eventdate> arrayList=new ArrayList<>();



        StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://teqvirtual.com/Boranasamaj/fetchevent.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    progressDialog.dismiss();
                    JSONArray jsonArray=new JSONArray(response);
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String e_image=jsonObject.getString("image");
                        String e_name=jsonObject.getString("name");
                        String e_time=jsonObject.getString("time");
                        String e_date=jsonObject.getString("date");
                        String e_location=jsonObject.getString("location");

                        Eventdate eventdate=new Eventdate();
                        eventdate.setName(e_name);
                        eventdate.setImage(e_image);
                        eventdate.setDate(e_date);
                        eventdate.setLocation(e_location);
                        eventdate.setTime(e_time);
                        arrayList.add(eventdate);

                    }
                    EventAdapter eventAdapter=new EventAdapter(Event.this,arrayList);
                    listView.setAdapter(eventAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Event.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(Event.this);
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
