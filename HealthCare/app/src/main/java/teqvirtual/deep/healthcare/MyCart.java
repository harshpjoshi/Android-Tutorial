package teqvirtual.deep.healthcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import teqvirtual.deep.healthcare.Adapter.CartMedicineAdapter;
import teqvirtual.deep.healthcare.Helper.AsyncResponse;
import teqvirtual.deep.healthcare.Helper.Utils;
import teqvirtual.deep.healthcare.Helper.WebserviceCall;
import teqvirtual.deep.healthcare.Model.CartMedicineModel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyCart extends AppCompatActivity {

    String url="https://teqvirtual.com/healthcare/index.php";
    String total_price;
    MaterialButton buynow;
    String preid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        String userid=pref.getString("userid", null);
        String pdate=pref.getString("pdate", null);
        String ptime=pref.getString("ptime", null);

        buynow=(MaterialButton)findViewById(R.id.buynow);

        final RecyclerView recyclerView=(RecyclerView)findViewById(R.id.bag_item_list);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        String[] key=new String[]{"action","subscriber"};
        String[] value=new String[]{"like_view_join",userid};

        String jsonRequest= Utils.createJsonRequest(key,value);

        new WebserviceCall(MyCart.this, url, jsonRequest, "Loading...", true, new AsyncResponse() {
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

                    CartMedicineAdapter cartMedicineAdapter=new CartMedicineAdapter(MyCart.this,arrayList);
                    recyclerView.setAdapter(cartMedicineAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }).execute();



        String[] key2=new String[]{"action","subscriber"};
        String[] value2=new String[]{"like_view_join_sum",userid};

        String jsonRequest2=Utils.createJsonRequest(key2,value2);

        new WebserviceCall(MyCart.this, url, jsonRequest2, "Loading...", false, new AsyncResponse() {
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

                    TextView bag_order_total_price=(TextView)findViewById(R.id.total_price);
                    Toast.makeText(MyCart.this, ""+total_price, Toast.LENGTH_SHORT).show();
                    bag_order_total_price.setText("\u20B9"+total_price);

                    Log.d("mainact","total+++"+bag_order_total_price.getText().toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }).execute();


        buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String key[]=new String[]{"action","subscriber_id"};
                String val[]=new String[]{"order_now",userid};

                String jsonReq=Utils.createJsonRequest(key,val);

                new WebserviceCall(MyCart.this, url, jsonReq, "Loading...", true, new AsyncResponse() {
                    @Override
                    public void onCallback(String response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response);

                            Toast.makeText(MyCart.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(MyCart.this,PaymentSelect.class));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }).execute();
            }
        });
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
