package teqvirtual.deep.healthcare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Helper.AsyncResponse;
import Helper.Utils;
import Helper.WebserviceCall;
import Model.CartMedicineModel;

public class Bag extends AppCompatActivity {

    String total_price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bag);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_bag);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        String userid=intent.getStringExtra("userid");

        final TextView total_prize=(TextView)findViewById(R.id.bag_prize_total);

        String url="http://teqvirtual.com/healthcare/index.php";

        final RecyclerView recyclerView=(RecyclerView)findViewById(R.id.bag_item_list);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        String[] key=new String[]{"action","subscriber"};
        String[] value=new String[]{"like_view_join",userid};

        String jsonRequest=Utils.createJsonRequest(key,value);

        new WebserviceCall(Bag.this, url, jsonRequest, "Loading...", true, new AsyncResponse() {
            @Override
            public void onCallback(String response) {

                ArrayList<CartMedicineModel> arrayList=new ArrayList<>();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String cart_id=jsonObject1.getString("id");
                        String subscriber=jsonObject1.getString("subscriber");
                        String medicine=jsonObject1.getString("medicine");
                        String barcode=jsonObject1.getString("barcode");
                        String description=jsonObject1.getString("description");
                        String image=jsonObject1.getString("image");
                        String price=jsonObject1.getString("price");
                        String power=jsonObject1.getString("power");
                        String name=jsonObject1.getString("name");
                        String subcat=jsonObject1.getString("subcat");
                        String quantity=jsonObject1.getString("quantity");

                        CartMedicineModel cartMedicineModel=new CartMedicineModel();
                        cartMedicineModel.setCart_id(cart_id);
                        cartMedicineModel.setSubscriber(subscriber);
                        cartMedicineModel.setMedicine(medicine);
                        cartMedicineModel.setBarcode(barcode);
                        cartMedicineModel.setDescription(description);
                        cartMedicineModel.setImage(image);
                        cartMedicineModel.setPower(power);
                        cartMedicineModel.setPrice(price);
                        cartMedicineModel.setSubcat(subcat);
                        cartMedicineModel.setName(name);
                        cartMedicineModel.setQuantity(quantity);

                        arrayList.add(cartMedicineModel);

                    }

                    CartMedicineAdapter cartMedicineAdapter=new CartMedicineAdapter(Bag.this,arrayList);
                    recyclerView.setAdapter(cartMedicineAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }).execute();


        String[] key2=new String[]{"action","subscriber"};
        String[] value2=new String[]{"like_view_join_sum",userid};

        String jsonRequest2=Utils.createJsonRequest(key2,value2);

        new WebserviceCall(Bag.this, url, jsonRequest2, "Loading...", false, new AsyncResponse() {
            @Override
            public void onCallback(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        total_price=jsonObject1.getString("SUM(`medicine`.`price` * `cart`.`quantity`)");
                    }

                    TextView bag_order_total_price=(TextView)findViewById(R.id.bag_order_total_price);
                    TextView bag_total_order=(TextView)findViewById(R.id.bag_total_order);

                    bag_order_total_price.setText("Rs. "+total_price);
                    bag_total_order.setText("Rs. "+total_price);
                    total_prize.setText("Rs. "+total_price);

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
