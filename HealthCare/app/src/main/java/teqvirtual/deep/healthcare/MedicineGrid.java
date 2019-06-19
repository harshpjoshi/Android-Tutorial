package teqvirtual.deep.healthcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import teqvirtual.deep.healthcare.Adapter.MedicineGridAdapter;
import teqvirtual.deep.healthcare.Helper.AsyncResponse;
import teqvirtual.deep.healthcare.Helper.Utils;
import teqvirtual.deep.healthcare.Helper.WebserviceCall;
import teqvirtual.deep.healthcare.Model.MedicineModel;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MedicineGrid extends AppCompatActivity {

    String url="https://teqvirtual.com/healthcare/index.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_grid);

        Intent intent=getIntent();
        String id=intent.getStringExtra("id");
        String name=intent.getStringExtra("name");

        TextView title_med=(TextView)findViewById(R.id.title_med);
        title_med.setText(name);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final RecyclerView recyclerView=(RecyclerView)findViewById(R.id.medicine_grid_list);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(MedicineGrid.this,2);
        recyclerView.setLayoutManager(layoutManager);

        String keys[]=new String[]{"action","subcat"};
        String values[]=new String[]{"medicine_view",id};

        String hotsel_jsonreq= Utils.createJsonRequest(keys,values);

        new WebserviceCall(MedicineGrid.this, url, hotsel_jsonreq, "Loading...", true, new AsyncResponse() {
            @Override
            public void onCallback(String response) {

                ArrayList<MedicineModel> arrayList=new ArrayList<>();
                try {

                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);

                        String id=jsonObject1.getString("medicine_id");
                        String barcode=jsonObject1.getString("barcode");
                        String description=jsonObject1.getString("description");
                        String image=jsonObject1.getString("image");
                        String price=jsonObject1.getString("price");
                        String power=jsonObject1.getString("power");
                        String name=jsonObject1.getString("name");
                        String subcat=jsonObject1.getString("subcat");

                        MedicineModel medicineModel=new MedicineModel();
                        medicineModel.setId(id);
                        medicineModel.setBarcode(barcode);
                        medicineModel.setDescription(description);
                        medicineModel.setImage(image);
                        medicineModel.setPrice(price);
                        medicineModel.setPower(power);
                        medicineModel.setName(name);
                        medicineModel.setSubcat(subcat);

                        arrayList.add(medicineModel);

                    }

                    MedicineGridAdapter medicineGridAdapter=new MedicineGridAdapter(MedicineGrid.this,arrayList);
                    recyclerView.setAdapter(medicineGridAdapter);

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
