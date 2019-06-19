package teqvirtual.deep.healthcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import teqvirtual.deep.healthcare.Adapter.SujjTips;
import teqvirtual.deep.healthcare.Helper.AsyncResponse;
import teqvirtual.deep.healthcare.Helper.Utils;
import teqvirtual.deep.healthcare.Helper.WebserviceCall;
import teqvirtual.deep.healthcare.Model.TipsModel;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SuggestedTips extends AppCompatActivity {

    String url="https://teqvirtual.com/healthcare/index.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_tips);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        String userid=pref.getString("doctorid", null);

        String key[]=new String[]{"action","doctor_id"};
        String val[]=new String[]{"tips_join",userid};

        String jsonRequest= Utils.createJsonRequest(key,val);

        new WebserviceCall(SuggestedTips.this, url, jsonRequest, "Loading...", true, new AsyncResponse() {
            @Override
            public void onCallback(String response) {

                ArrayList<TipsModel> arrayList=new ArrayList<>();

                try {

                    JSONObject jsonObject=new JSONObject(response);

                    JSONArray jsonArray=jsonObject.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);

                        String tips_id=jsonObject1.getString("tips_id");
                        String doctor_id=jsonObject1.getString("doctor_id");
                        String image=jsonObject1.getString("tips_image");
                        String description=jsonObject1.getString("description");
                        String firstname=jsonObject1.getString("firstname");
                        String tips_image=jsonObject1.getString("tips_image");

                        TipsModel tipsModel=new TipsModel();
                        tipsModel.setDescription(description);
                        tipsModel.setImage(image);

                        arrayList.add(tipsModel);

                        SujjTips sujjTips=new SujjTips(SuggestedTips.this,arrayList);
                        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.suggested_tips);
                        recyclerView.setLayoutManager(new LinearLayoutManager(SuggestedTips.this));
                        recyclerView.setAdapter(sujjTips);

                    }

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
