package teqvirtual.deep.healthcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import teqvirtual.deep.healthcare.Adapter.SubCategoryAdapter;
import teqvirtual.deep.healthcare.Helper.AsyncResponse;
import teqvirtual.deep.healthcare.Helper.Utils;
import teqvirtual.deep.healthcare.Helper.WebserviceCall;
import teqvirtual.deep.healthcare.Model.SubCategoryModel;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Submedicine extends AppCompatActivity {

    String url="https://teqvirtual.com/healthcare/index.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submedicine);

        Intent intent=getIntent();
        String id=intent.getStringExtra("id");
        String name=intent.getStringExtra("name");
        String img_uri=intent.getStringExtra("image");

        TextView title_med=(TextView)findViewById(R.id.title_med);
        title_med.setText(name);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView cat_img=(ImageView)findViewById(R.id.cat_img);
        Picasso.get().load(img_uri).into(cat_img);

        final RecyclerView recyclerView=(RecyclerView)findViewById(R.id.sub_medicine_list);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(Submedicine.this);
        recyclerView.setLayoutManager(layoutManager);

        String[] keys=new String[]{"action","maincat"};
        String[] values=new String[]{"subcat_view",id};

        String jsonRequest= Utils.createJsonRequest(keys,values);



        new WebserviceCall(Submedicine.this, url, jsonRequest, "Loading...", true, new AsyncResponse() {
            @Override
            public void onCallback(String response) {

                ArrayList<SubCategoryModel> arrayList=new ArrayList<>();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);

                        String subcat_id=jsonObject1.getString("subcat_id");
                        String maincat=jsonObject1.getString("maincat");
                        String image=jsonObject1.getString("image");
                        String name=jsonObject1.getString("name");
                        String description=jsonObject1.getString("description");

                        SubCategoryModel subCategoryModel=new SubCategoryModel();
                        subCategoryModel.setId(subcat_id);
                        subCategoryModel.setImage(image);
                        subCategoryModel.setName(name);
                        subCategoryModel.setDescription(description);
                        subCategoryModel.setMaincat_id(maincat);

                        arrayList.add(subCategoryModel);
                    }

                    SubCategoryAdapter subCategoryAdapter=new SubCategoryAdapter(getApplicationContext(),arrayList);
                    recyclerView.setAdapter(subCategoryAdapter);
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
