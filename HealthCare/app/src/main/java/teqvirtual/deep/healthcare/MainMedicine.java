package teqvirtual.deep.healthcare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import teqvirtual.deep.healthcare.Helper.AsyncResponse;
import teqvirtual.deep.healthcare.Helper.Utils;
import teqvirtual.deep.healthcare.Helper.WebserviceCall;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainMedicine extends AppCompatActivity {

    Bitmap bitmap;
    Uri imageUri;
    String qty[]={"1","2","3","4","5","6","7","8","9","10"};
    Spinner spinner;
    String qty_holder;
    int flag=1;
    TextView textCartItemCount,status_text;
    String counter;
    int mCartItemCount;
    String status,prescription_id;
    ImageView imageView1;
    int pflag;
    String url="https://teqvirtual.com/healthcare/index.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_medicine);

        Intent intent=getIntent();
        String id=intent.getStringExtra("id");
        String name=intent.getStringExtra("name");

        TextView title_med=(TextView)findViewById(R.id.title_med);
        title_med.setText(name);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        String userid=pref.getString("userid", null);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinner=(Spinner)findViewById(R.id.qty);

        final TextView medicine_name=(TextView)findViewById(R.id.medicine_name_main);
        final TextView medicine_price=(TextView)findViewById(R.id.medicine_price_main);
        status_text=(TextView)findViewById(R.id.status_text);
        final ImageView imageView=(ImageView)findViewById(R.id.medicine_img) ;

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(MainMedicine.this,android.R.layout.simple_spinner_dropdown_item,qty);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                qty_holder=qty[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        String key2[]=new String[]{"action","subscriber"};
        String value2[]=new String[]{"like_count",userid};

        String medicin_count=Utils.createJsonRequest(key2,value2);

        new WebserviceCall(MainMedicine.this, url, medicin_count, "Loading...", true, new AsyncResponse() {
            @Override
            public void onCallback(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        counter=jsonObject1.getString("COUNT(id)");

                        mCartItemCount= Integer.parseInt(counter);
                        setBadge();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).execute();


        String keys[]=new String[]{"action","medicine_id"};
        final String values[]=new String[]{"medicine_view",id};

        String jsonrequest= Utils.createJsonRequest(keys,values);

        new WebserviceCall(MainMedicine.this, url, jsonrequest, "Loading...", true, new AsyncResponse() {
            @Override
            public void onCallback(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);

                        String medicine_id=jsonObject1.getString("medicine_id");
                        String barcode=jsonObject1.getString("barcode");
                        String description=jsonObject1.getString("description");
                        String image=jsonObject1.getString("image");
                        String price=jsonObject1.getString("price");
                        String power=jsonObject1.getString("power");
                        String name=jsonObject1.getString("name");
                        String subcat=jsonObject1.getString("subcat");
                        String status=jsonObject1.getString("status");


                        pflag=status.compareTo("0");

                        Picasso.get().load(image).into(imageView);
                        medicine_name.setText(name);
                        medicine_price.setText("Rs: "+price);

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }).execute();


        String key3[]=new String[]{"action","subscriber"};
        String value3[]=new String[]{"like_count",userid};

        String medicin_count3=Utils.createJsonRequest(key3,value3);

        new WebserviceCall(MainMedicine.this, url, medicin_count3, "Loading...", true, new AsyncResponse() {
            @Override
            public void onCallback(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        counter=jsonObject1.getString("COUNT(id)");

                        mCartItemCount= Integer.parseInt(counter);
                        setBadge();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).execute();

       MaterialButton materialButton=(MaterialButton)findViewById(R.id.cart);

        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(pflag==0)
                {


                    String key[]=new String[]{"action","subscriber","medicine","quantity"};
                    String value[]=new String[]{"like_add",userid,id,qty_holder};

                    String medicin_add=Utils.createJsonRequest(key,value);

                    new WebserviceCall(MainMedicine.this, url, medicin_add, "Loading...", true, new AsyncResponse() {
                        @Override
                        public void onCallback(String response) {

                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                Toast.makeText(MainMedicine.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }).execute();

                }
                else
                {

                    Toast.makeText(MainMedicine.this, ""+pflag, Toast.LENGTH_SHORT).show();
                    Dialog dialog=new Dialog(MainMedicine.this);
                    dialog.setContentView(R.layout.content_upload_prescription);
                    dialog.setTitle("Upload Prescription");

                    MaterialButton uppresc_btn=(MaterialButton)dialog.findViewById(R.id.uppresc_btn);
                    MaterialButton upcontinue=(MaterialButton)dialog.findViewById(R.id.upcontinue);
                    imageView1=(ImageView)dialog.findViewById(R.id.up_img);

                    uppresc_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");

                            Intent chooser = Intent.createChooser(intent, "Choose a Picture");
                            startActivityForResult(chooser,5);

                        }
                    });

                    upcontinue.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Calendar calendar=Calendar.getInstance();
                            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                            String cdate=simpleDateFormat.format(calendar.getTime());

                            SimpleDateFormat dateFormat=new SimpleDateFormat("hh:mm:ss");
                            String ctime=dateFormat.format(calendar.getTime());

                            String key[]=new String[]{"action","psubscriber","pimage","date","time"};
                            String val[]=new String[]{"prescription_add",userid,getStringImage(bitmap),cdate,ctime};

                            String jsonRequest=Utils.createJsonRequest(key,val);

                            new WebserviceCall(MainMedicine.this, url, jsonRequest, "Loading...", true, new AsyncResponse() {
                                @Override
                                public void onCallback(String response) {

                                    try {
                                        JSONObject jsonObject=new JSONObject(response);

                                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("pdate",cdate);
                                        editor.putString("ptime",ctime);
                                        editor.commit();

                                        Toast.makeText(MainMedicine.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }).execute();

                            String key2[]=new String[]{"action","date","time"};
                            String val2[]=new String[]{"prescription_view",cdate,ctime};

                            String jsonReq=Utils.createJsonRequest(key2,val2);

                            new WebserviceCall(MainMedicine.this, url, jsonReq, "Loading...", true, new AsyncResponse() {
                                @Override
                                public void onCallback(String response) {

                                    try {
                                        JSONObject jsonObject=new JSONObject(response);

                                        JSONArray jsonArray=jsonObject.getJSONArray("data");

                                        for (int i=0;i<jsonArray.length();i++)
                                        {
                                            JSONObject jsonObject1=jsonArray.getJSONObject(i);

                                            prescription_id=jsonObject1.getString("prescription_id");

                                            String key4[]=new String[]{"action","subscriber","medicine","quantity","prescription"};
                                            String value4[]=new String[]{"like_add",userid,id,qty_holder,prescription_id};

                                            String medicin_add=Utils.createJsonRequest(key4,value4);

                                            new WebserviceCall(MainMedicine.this, url, medicin_add, "Loading...", true, new AsyncResponse() {
                                                @Override
                                                public void onCallback(String response) {

                                                    try {
                                                        JSONObject jsonObject=new JSONObject(response);
                                                        Toast.makeText(MainMedicine.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                                        startActivity(new Intent(MainMedicine.this,MyCart.class));

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }


                                                }
                                            }).execute();

                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }).execute();




                        }
                    });

                    dialog.show();


                }

                String key2[]=new String[]{"action","subscriber"};
                String value2[]=new String[]{"like_count",userid};

                String medicin_count=Utils.createJsonRequest(key2,value2);

                new WebserviceCall(MainMedicine.this, url, medicin_count, "Loading...", true, new AsyncResponse() {
                    @Override
                    public void onCallback(String response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("data");

                            for (int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                counter=jsonObject1.getString("COUNT(id)");

                                mCartItemCount= Integer.parseInt(counter);
                                setBadge();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }).execute();

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 5 && resultCode == RESULT_OK) {
            //for copy image path
            Uri path = data.getData();
            CropImage.activity(path).setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(MainMedicine.this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Picasso.get().load(imageUri).into(imageView1);
            }
        }
    }

    public String getStringImage(Bitmap bmp)
    {
        if (bmp!=null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            String encodedimage = Base64.encodeToString(bytes, Base64.DEFAULT);
            return encodedimage;
        }
        return null;
    }

    private void setBadge()
    {
        if(textCartItemCount!=null)
        {
            if (mCartItemCount==0)
            {
                if(textCartItemCount.getVisibility() != View.GONE)
                {
                    textCartItemCount.setVisibility(View.GONE);
                }
            }
            else
            {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount,99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);
        final MenuItem menuItem=menu.findItem(R.id.action_cart);
        View actionview= MenuItemCompat.getActionView(menuItem);
        textCartItemCount=(TextView)actionview.findViewById(R.id.cart_badge);

        actionview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);

                startActivity(new Intent(MainMedicine.this,MyCart.class));

            }
        });

        return true;
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
