package teqvirtual.deep.boranasamaj;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapter.AdminAdvertiseListAdapter;
import Helper.AsyncResponse;
import Helper.Utils;
import Helper.WebserviceCall;
import Model.AdvertiseModel;

public class AdminViewAdvertise extends AppCompatActivity {

    AdminAdvertiseListAdapter adapter;
    String url="https://teqvirtual.com/BMS/index.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_advertise);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_advertise_admin);
        toolbar.setTitleTextColor(getResources().getColor(R.color.font));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final RecyclerView recyclerView=(RecyclerView)findViewById(R.id.advertise_list_admin);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        String[] keys=new String[]{"action"};
        String[] values=new String[]{"advertise_view"};

        String jsonRequest= Utils.createJsonRequest(keys,values);


        new WebserviceCall(AdminViewAdvertise.this, url, jsonRequest, "Loading...", true, new AsyncResponse() {
            @Override
            public void onCallback(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);

                    ArrayList<AdvertiseModel> advertiseModels=new ArrayList<>();

                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String add_id=jsonObject1.getString("id");
                        String add_description=jsonObject1.getString("advertise_description");
                        String add_image=jsonObject1.getString("advertise_image");
                        String add_mobile=jsonObject1.getString("advertise_mobile");

                        AdvertiseModel advertiseModel=new AdvertiseModel();
                        advertiseModel.setId(add_id);
                        advertiseModel.setMobile(add_mobile);
                        advertiseModel.setDescription(add_description);
                        advertiseModel.setImage(add_image);

                        advertiseModels.add(advertiseModel);
                    }
                    adapter=new AdminAdvertiseListAdapter(AdminViewAdvertise.this,advertiseModels);
                    recyclerView.setAdapter(adapter);

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
