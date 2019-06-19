package teqvirtual.deep.boranasamaj;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import Helper.AsyncResponse;
import Helper.Utils;
import Helper.WebserviceCall;

public class Addgallery extends AppCompatActivity {

    ImageView gallery_image;
    EditText gallery_name;
    Button add_gallery;
    Bitmap bitmap;

    String url="https://teqvirtual.com/BMS/index.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addgallery);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_gallery_add);
        toolbar.setTitleTextColor(getResources().getColor(R.color.font));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        gallery_image=(ImageView)findViewById(R.id.gallery_image);
        add_gallery=(Button)findViewById(R.id.add_gallery);
        gallery_name=(EditText)findViewById(R.id.gallery_name);

        gallery_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileChooser();
            }
        });

        add_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String gallery_name_holder=gallery_name.getText().toString().trim();

                String image_string=getStringImage(bitmap);

                if (gallery_name_holder.isEmpty())
                {
                    Toast.makeText(Addgallery.this, "Name Not Empty.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String keys[]=new String[]{"action","gallery_name","gallery_image"};
                    String values[]=new String[]{"gallery_add",gallery_name_holder,image_string};

                    String jsonrequest= Utils.createJsonRequest(keys,values);

                    new WebserviceCall(Addgallery.this, url, jsonrequest, "Adding Image...", true, new AsyncResponse() {
                        @Override
                        public void onCallback(String response) {
                            JSONObject jsonObject= null;
                            try {
                                jsonObject = new JSONObject(response);
                                String message=jsonObject.getString("message");
                                Toast.makeText(Addgallery.this, ""+message, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Addgallery.this,AdminViewGallery.class));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }).execute();
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
                gallery_image.setVisibility(View.VISIBLE);
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                gallery_image.setImageBitmap(bitmap);
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
        String encodedimage= Base64.encodeToString(bytes, Base64.DEFAULT);
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
