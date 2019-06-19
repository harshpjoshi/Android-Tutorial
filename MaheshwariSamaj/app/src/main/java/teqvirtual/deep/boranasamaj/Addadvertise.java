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

public class Addadvertise extends AppCompatActivity {

    EditText advertiser_description,advertiser_mobile;
    Button add_advertise;
    ImageView advertise_img;
    Bitmap bitmap;

    String url="https://teqvirtual.com/BMS/index.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addadvertise);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_advertise_add);
        toolbar.setTitleTextColor(getResources().getColor(R.color.font));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        advertiser_description=(EditText)findViewById(R.id.advertise_description);
        advertiser_mobile=(EditText)findViewById(R.id.advertise_mobile);
        advertise_img=(ImageView)findViewById(R.id.advertise_image);
        add_advertise=(Button)findViewById(R.id.add_advertise);

        advertise_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileChooser();
            }
        });

        add_advertise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String advertise_description_holder=advertiser_description.getText().toString().trim();
                String advertise_mobile_holder=advertiser_mobile.getText().toString().trim();

                if (advertise_description_holder.isEmpty())
                {
                    Toast.makeText(Addadvertise.this, "Description Not Empty", Toast.LENGTH_SHORT).show();
                }
                else if (advertise_mobile_holder.isEmpty())
                {
                    Toast.makeText(Addadvertise.this, "Mobile Not Empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String image_advertise=getStringImage(bitmap);

                    String keys[]=new String[]{"action","advertise_description","advertise_mobile","advertise_image"};
                    String values[]=new String[]{"advertise_add",advertise_description_holder,advertise_mobile_holder,image_advertise};

                    String jsonrequest= Utils.createJsonRequest(keys,values);

                    new WebserviceCall(Addadvertise.this, url, jsonrequest, "Adding Advertise...", true, new AsyncResponse() {
                        @Override
                        public void onCallback(String response) {
                            JSONObject jsonObject= null;
                            try {
                                jsonObject = new JSONObject(response);
                                String message=jsonObject.getString("message");
                                Toast.makeText(Addadvertise.this, ""+message, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Addadvertise.this,AdminViewAdvertise.class));
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
                advertise_img.setVisibility(View.VISIBLE);
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                advertise_img.setImageBitmap(bitmap);
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
