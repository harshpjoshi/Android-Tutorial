package teqvirtual.deep.boranasamaj;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapter.GalleryListAdapter;
import Helper.AsyncResponse;
import Helper.Utils;
import Helper.WebserviceCall;
import Model.GalleryModel;

public class Gallery extends AppCompatActivity {

    String url="https://teqvirtual.com/BMS/index.php";

    GalleryListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_gallery);
        toolbar.setTitleTextColor(getResources().getColor(R.color.font));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final RecyclerView recyclerView=(RecyclerView)findViewById(R.id.gallery_list);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        String[] keys=new String[]{"action"};
        String[] values=new String[]{"gallery_view"};

        String jsonRequest= Utils.createJsonRequest(keys,values);


        new WebserviceCall(Gallery.this, url, jsonRequest, "Loading...", true, new AsyncResponse() {
            @Override
            public void onCallback(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    ArrayList<GalleryModel> arrayList=new ArrayList<>();
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String gallery_id=jsonObject1.getString("id");
                        String gallery_name=jsonObject1.getString("gallery_name");
                        String gallery_image=jsonObject1.getString("gallery_image");

                        GalleryModel galleryModel=new GalleryModel();
                        galleryModel.setId(gallery_id);
                        galleryModel.setName(gallery_name);
                        galleryModel.setImage(gallery_image);
                        arrayList.add(galleryModel);
                    }
                    adapter=new GalleryListAdapter(Gallery.this,arrayList);
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
