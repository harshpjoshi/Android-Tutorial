package teqvirtual.deep.healthcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import teqvirtual.deep.healthcare.Adapter.HospitalListAdapter;
import teqvirtual.deep.healthcare.Helper.AsyncResponse;
import teqvirtual.deep.healthcare.Helper.Utils;
import teqvirtual.deep.healthcare.Helper.WebserviceCall;
import teqvirtual.deep.healthcare.Model.DoctorModel;
import teqvirtual.deep.healthcare.Model.HospitalModel;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HospitalList extends AppCompatActivity {

    String url="https://teqvirtual.com/healthcare/index.php";

    ArrayList<HospitalModel> arrayList=new ArrayList<>();
    HospitalListAdapter hospitalListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_list);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.hospital_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        String key[]=new String[]{"action"};
        String val[]=new String[]{"hospital_join"};

        String jsonRequest= Utils.createJsonRequest(key,val);

        new WebserviceCall(HospitalList.this, url, jsonRequest, "Loading...", true, new AsyncResponse() {
            @Override
            public void onCallback(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);

                        String hospital_id=jsonObject1.getString("hospital_id");
                        String name=jsonObject1.getString("hospital_name");
                        String description=jsonObject1.getString("description");
                        String image=jsonObject1.getString("image");
                        String latitude=jsonObject1.getString("latitude");
                        String longitude=jsonObject1.getString("longitude");
                        String address=jsonObject1.getString("address");
                        String mobileno=jsonObject1.getString("mobileno");
                        String city=jsonObject1.getString("name");
                        String type=jsonObject1.getString("hos_type_name");

                        HospitalModel hospitalModel=new HospitalModel();
                        hospitalModel.setId(hospital_id);
                        hospitalModel.setName(name);
                        hospitalModel.setDescription(description);
                        hospitalModel.setImage(image);
                        hospitalModel.setLati(latitude);
                        hospitalModel.setLongi(longitude);
                        hospitalModel.setAddress(address);
                        hospitalModel.setMobileno(mobileno);
                        hospitalModel.setCity(city);
                        hospitalModel.setType(type);

                        arrayList.add(hospitalModel);
                    }

                    hospitalListAdapter =new HospitalListAdapter(getApplicationContext(),arrayList);
                    recyclerView.setAdapter(hospitalListAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_menu,menu);

        MenuItem searchItem=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView)searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                hospitalListAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
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
