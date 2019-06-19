package teqvirtual.deep.boranasamaj;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class News extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        final ProgressDialog progressDialog=new ProgressDialog(News.this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog.setMessage("Please Wait....");
        progressDialog.show();

        final ListView listView=(ListView)findViewById(R.id.news_list);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://teqvirtual.com/Boranasamaj/fetchnews.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                ArrayList<NewsData> arrayList=new ArrayList<>();
                JSONArray jsonArray= null;
                try {

                    progressDialog.dismiss();
                    jsonArray = new JSONArray(response);
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String image=jsonObject.getString("image");
                        String description=jsonObject.getString("description");
                        NewsData newsData=new NewsData();
                        newsData.setImage(image);
                        newsData.setDescription(description);
                        arrayList.add(newsData);
                    }

                    NewsDataAdapter newsDataAdapter=new NewsDataAdapter(News.this,arrayList);
                    listView.setAdapter(newsDataAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(News.this, ""+error, Toast.LENGTH_SHORT).show();

            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(News.this);
        requestQueue.add(stringRequest);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        if(id==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
