package teqvirtual.deep.healthcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import teqvirtual.deep.healthcare.Adapter.YourSujjMedical;
import teqvirtual.deep.healthcare.Helper.AsyncResponse;
import teqvirtual.deep.healthcare.Helper.Utils;
import teqvirtual.deep.healthcare.Helper.WebserviceCall;
import teqvirtual.deep.healthcare.Model.SujjestedMedicalModel;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SuggestedStore extends AppCompatActivity {

    String url="https://teqvirtual.com/healthcare/index.php";
    ArrayList<SujjestedMedicalModel> arrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_store);


        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        String userid=pref.getString("userid", null);

        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.your_medical_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String[] key=new String[]{"action","subscriber"};
        String[] val=new String[]{"smedical_join",userid};

        String jsonReq= Utils.createJsonRequest(key,val);

        new WebserviceCall(SuggestedStore.this, url, jsonReq, "Loading...", true, new AsyncResponse() {
            @Override
            public void onCallback(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);

                    JSONArray jsonArray=jsonObject.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);

                        String medical_id=jsonObject1.getString("medical_id");
                        String smedical_name=jsonObject1.getString("smedical_name");
                        String city=jsonObject1.getString("name");
                        String image=jsonObject1.getString("image");

                        SujjestedMedicalModel sujjestedMedicalModel=new SujjestedMedicalModel();
                        sujjestedMedicalModel.setId(medical_id);
                        sujjestedMedicalModel.setName(smedical_name);
                        sujjestedMedicalModel.setCity(city);
                        sujjestedMedicalModel.setImage(image);

                        arrayList.add(sujjestedMedicalModel);
                    }

                    YourSujjMedical yourSujjMedical=new YourSujjMedical(SuggestedStore.this,arrayList);
                    recyclerView.setAdapter(yourSujjMedical);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).execute();

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
