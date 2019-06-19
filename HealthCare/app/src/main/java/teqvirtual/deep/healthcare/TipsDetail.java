package teqvirtual.deep.healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import teqvirtual.deep.healthcare.Helper.AsyncResponse;
import teqvirtual.deep.healthcare.Helper.Utils;
import teqvirtual.deep.healthcare.Helper.WebserviceCall;
import teqvirtual.deep.healthcare.Model.TipsModel;

public class TipsDetail extends AppCompatActivity {

    String url="https://teqvirtual.com/healthcare/index.php";
    String tips_id,doctor_id,doctor_image,description,doctor_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_tips);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);

        Intent intent=getIntent();
        tips_id=intent.getStringExtra("tipsid");
        doctor_id=intent.getStringExtra("docid");

        String key[]=new String[]{"action","tips_id"};
        String val[]=new String[]{"tips_join",tips_id};

        String jsonRequest= Utils.createJsonRequest(key,val);

        new WebserviceCall(TipsDetail.this, url, jsonRequest, "Loading...", true, new AsyncResponse() {
            @Override
            public void onCallback(String response) {

                try {

                    JSONObject jsonObject=new JSONObject(response);

                    JSONArray jsonArray=jsonObject.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);

                        String tips_id=jsonObject1.getString("tips_id");
                        String doctor_id=jsonObject1.getString("doctor_id");
                        String image=jsonObject1.getString("image");
                        String description=jsonObject1.getString("description");
                        String firstname=jsonObject1.getString("firstname");
                        String tips_image=jsonObject1.getString("tips_image");

                        ImageView expandedImage=(ImageView)findViewById(R.id.expandedImage);
                        ImageView doc_tip_img=(ImageView)findViewById(R.id.doc_tip_img);

                        Picasso.get().load(tips_image).into(expandedImage);
                        Picasso.get().load(image).into(doc_tip_img);


                        TextView tip_desc=(TextView)findViewById(R.id.tip_desc);
                        TextView doc_tip_name=(TextView)findViewById(R.id.doc_tip_name);

                        tip_desc.setText(description);
                        doc_tip_name.setText(firstname);


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).execute();

        String[] key2=new String[]{"action","tips_id"};
        String[] val2=new String[]{"tips_join",tips_id};

        String jsonReq=Utils.createJsonRequest(key2,val2);

        new WebserviceCall(TipsDetail.this, url, jsonReq, "Loading...", true, new AsyncResponse() {
            @Override
            public void onCallback(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String image=jsonObject1.getString("image");
                        String firstname=jsonObject1.getString("firstname");
                        String lastname=jsonObject1.getString("lastname");

                        ImageView imageView=(ImageView)findViewById(R.id.doc_tip_img);
                        Picasso.get().load(image).into(imageView);

                        TextView textView=(TextView)findViewById(R.id.doc_tip_name);
                        textView.setText(firstname+" "+lastname);
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
