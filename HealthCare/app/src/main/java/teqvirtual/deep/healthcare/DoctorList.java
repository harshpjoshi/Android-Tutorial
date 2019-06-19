package teqvirtual.deep.healthcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import teqvirtual.deep.healthcare.Adapter.DoctorListAdapter;
import teqvirtual.deep.healthcare.Helper.AsyncResponse;
import teqvirtual.deep.healthcare.Helper.Utils;
import teqvirtual.deep.healthcare.Helper.WebserviceCall;
import teqvirtual.deep.healthcare.Model.DoctorModel;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DoctorList extends AppCompatActivity {

    String url="https://teqvirtual.com/healthcare/index.php";

    ArrayList<DoctorModel> arrayList=new ArrayList<>();

    DoctorListAdapter doctorListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.doctor_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        String key[]=new String[]{"action"};
        String val[]=new String[]{"doctor_view_join"};

        String jsonRequest= Utils.createJsonRequest(key,val);

        new WebserviceCall(DoctorList.this, url, jsonRequest, "Loading...", true, new AsyncResponse() {
            @Override
            public void onCallback(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);

                    JSONArray jsonArray=jsonObject.getJSONArray("data");

                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);

                        String doctor_id=jsonObject1.getString("doctor_id");
                        String firstname=jsonObject1.getString("firstname");
                        String lastname=jsonObject1.getString("lastname");
                        String image=jsonObject1.getString("image");
                        String address=jsonObject1.getString("address");
                        String email=jsonObject1.getString("email");
                        String city=jsonObject1.getString("name");
                        String type=jsonObject1.getString("type_name");
                        String password=jsonObject1.getString("password");
                        String f_doctor_id=jsonObject1.getString("f_doctor_id");
                        String mobileno=jsonObject1.getString("mobileno");

                        DoctorModel doctorModel=new DoctorModel();

                        doctorModel.setId(doctor_id);
                        doctorModel.setFirstname(firstname);
                        doctorModel.setLastname(lastname);
                        doctorModel.setEmail(email);
                        doctorModel.setMobileno(mobileno);
                        doctorModel.setImage(image);
                        doctorModel.setCity(city);
                        doctorModel.setType(type);
                        doctorModel.setF_doctor_id(f_doctor_id);
                        doctorModel.setAddress(address);

                        arrayList.add(doctorModel);
                    }

                    doctorListAdapter =new DoctorListAdapter(getApplicationContext(),arrayList);
                    recyclerView.setAdapter(doctorListAdapter);
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
                doctorListAdapter.getFilter().filter(newText);
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
