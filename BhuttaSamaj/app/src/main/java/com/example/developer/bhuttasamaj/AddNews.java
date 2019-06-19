package com.example.developer.bhuttasamaj;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.ReferenceQueue;
import java.util.HashMap;
import java.util.Map;

public class AddNews extends AppCompatActivity {

    Bitmap bitmap;
    EditText news_desc;
    ImageView news_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);

        final ProgressDialog progressDialog=new ProgressDialog(AddNews.this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        news_desc=(EditText)findViewById(R.id.news_description);
        news_img=(ImageView)findViewById(R.id.news_img);

        news_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileChooser();
            }
        });

        Button addnews=(Button)findViewById(R.id.addnews);
        addnews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String news_desc_hold=news_desc.getText().toString();

                if(news_desc_hold.isEmpty())
                {
                    news_desc.setError("News can't empty");
                }
                else
                {
                    StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://teqvirtual.com/Boranasamaj/addnews.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.setMessage("Please Wait.....");
                            progressDialog.show();
                            Toast.makeText(AddNews.this, ""+response, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.show();
                            Toast.makeText(AddNews.this, ""+error, Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String,String> map=new HashMap<>();
                            map.put("desc",news_desc_hold);
                            map.put("image",getStringImage(bitmap));
                            return map;
                        }
                    };

                    RequestQueue referenceQueue=Volley.newRequestQueue(AddNews.this);
                    referenceQueue.add(stringRequest);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == this.RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                news_img.setVisibility(View.VISIBLE);
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                news_img.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void fileChooser()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    public String getStringImage(Bitmap bmp)
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] bytes=byteArrayOutputStream.toByteArray();
        String encodedimage= Base64.encodeToString(bytes,Base64.DEFAULT);
        return encodedimage;
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
