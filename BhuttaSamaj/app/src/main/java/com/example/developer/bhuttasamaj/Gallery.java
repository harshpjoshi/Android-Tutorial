package com.example.developer.bhuttasamaj;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class Gallery extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ProgressDialog progressDialog=new ProgressDialog(Gallery.this);
        progressDialog.setMessage("Please Wait.....");
        progressDialog.show();


        StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://teqvirtual.com/Boranasamaj/fetchgallery.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    progressDialog.dismiss();
                    final ArrayList<GalleryData> arrayList=new ArrayList<>();
                    JSONArray jsonArray=new JSONArray(response);
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String image=jsonObject.getString("image");
                        String name=jsonObject.getString("name");
                        GalleryData galleryData=new GalleryData();
                        galleryData.setImage(image);
                        galleryData.setName(name);

                        arrayList.add(galleryData);

                    }

                    RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recyclerview);

                    RecyclerView.LayoutManager layoutManager=new GridLayoutManager(Gallery.this,3);
                    recyclerView.setLayoutManager(layoutManager);
                    RecyclerView.Adapter adapter=new GalleryAdapter(Gallery.this,arrayList);
                    recyclerView.setAdapter(adapter);






                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Gallery.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(Gallery.this);
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
