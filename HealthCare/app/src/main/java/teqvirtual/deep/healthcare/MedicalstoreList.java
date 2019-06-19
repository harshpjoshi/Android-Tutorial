package teqvirtual.deep.healthcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import teqvirtual.deep.healthcare.Adapter.MedicalListAdapter;
import teqvirtual.deep.healthcare.Helper.AsyncResponse;
import teqvirtual.deep.healthcare.Helper.Utils;
import teqvirtual.deep.healthcare.Helper.WebserviceCall;
import teqvirtual.deep.healthcare.Model.HospitalModel;
import teqvirtual.deep.healthcare.Model.MedicalModel;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MedicalstoreList extends AppCompatActivity {

    String url="https://teqvirtual.com/healthcare/index.php";

    ArrayList<MedicalModel> arrayList=new ArrayList<>();

    MedicalListAdapter medicalListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicalstore_list);


        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.medical_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        String key[]=new String[]{"action"};
        String val[]=new String[]{"medical_join"};

        String jsonRequest= Utils.createJsonRequest(key,val);

        new WebserviceCall(MedicalstoreList.this, url, jsonRequest, "Loading...", true, new AsyncResponse() {
            @Override
            public void onCallback(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);

                    JSONArray jsonArray=jsonObject.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++)
                    {

                        JSONObject jsonObject1=jsonArray.getJSONObject(i);

                        String medicalstore_id=jsonObject1.getString("medicalstore_id");
                        String image=jsonObject1.getString("image");
                        String name=jsonObject1.getString("medical_name");
                        String city=jsonObject1.getString("name");

                        MedicalModel medicalModel=new MedicalModel();
                        medicalModel.setMedicalid(medicalstore_id);
                        medicalModel.setName(name);
                        medicalModel.setImage(image);
                        medicalModel.setCity(city);

                        arrayList.add(medicalModel);

                    }

                    medicalListAdapter =new MedicalListAdapter(getApplicationContext(),arrayList);
                    recyclerView.setAdapter(medicalListAdapter);
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
                medicalListAdapter.getFilter().filter(newText);
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
