package teqvirtual.deep.healthcare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import okhttp3.internal.Util;
import teqvirtual.deep.healthcare.Helper.AsyncResponse;
import teqvirtual.deep.healthcare.Helper.Utils;
import teqvirtual.deep.healthcare.Helper.WebserviceCall;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SuggestTips extends AppCompatActivity {

    TextInputEditText name_sug;
    ImageView sub_profile;
    Bitmap bitmap;
    Uri imageUri;
    String url="https://teqvirtual.com/healthcare/index.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_tips);


        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MaterialButton materialButton=(MaterialButton)findViewById(R.id.sug_btn);
        name_sug=(TextInputEditText)findViewById(R.id.tips_sug);
        sub_profile=(ImageView)findViewById(R.id.tips_sug_img);

        sub_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                Intent chooser = Intent.createChooser(intent, "Choose a Picture");
                startActivityForResult(chooser,5);
            }
        });

        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                suggestTips();

            }
        });
    }

    private void suggestTips() {

        String desc_holder=name_sug.getText().toString().trim();
        String imageholder=getStringImage(bitmap);

        if (desc_holder.isEmpty())
        {
            alertDialog("You Can't Suggest Empty Description");
        }
        else if (imageholder.isEmpty())
        {
            alertDialog("Pelase Select Tips Image");
        }
        else
        {

            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

            String userid=pref.getString("doctorid", null);

            Toast.makeText(SuggestTips.this, ""+userid, Toast.LENGTH_SHORT).show();

            String[] key=new String[]{"action","doctor_id","tips_image","description"};
            String[] val=new String[]{"tips_add",userid,imageholder,desc_holder};

            String jsonReq= Utils.createJsonRequest(key,val);

            new WebserviceCall(SuggestTips.this, url, jsonReq, "Loading...", true, new AsyncResponse() {
                @Override
                public void onCallback(String response) {

                    try {
                        JSONObject jsonObject=new JSONObject(response);

                        Toast.makeText(SuggestTips.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }).execute();

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 5 && resultCode == RESULT_OK) {
            //for copy image path
            Uri path = data.getData();
            CropImage.activity(path).setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(SuggestTips.this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Picasso.get().load(imageUri).into(sub_profile);
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

    public void alertDialog(String msg)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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
