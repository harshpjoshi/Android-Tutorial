package teqvirtual.deep.healthcare;

import androidx.appcompat.app.AppCompatActivity;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import teqvirtual.deep.healthcare.Helper.AsyncResponse;
import teqvirtual.deep.healthcare.Helper.Utils;
import teqvirtual.deep.healthcare.Helper.WebserviceCall;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QrscanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    ZXingScannerView scannerView;
    String url="https://teqvirtual.com/healthcare/index.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView=new ZXingScannerView(this);
        setContentView(scannerView);


    }

    @Override
    public void handleResult(Result result) {

        String result_code=result.getText().toString();

        String key[]=new String[]{"action","barcode"};
        String val[]=new String[]{"medicine_view",result_code};

        String jsonRequest= Utils.createJsonRequest(key,val);

        new WebserviceCall(QrscanActivity.this, url, jsonRequest, "Loading...", true, new AsyncResponse() {
            @Override
            public void onCallback(String response) {

                try {

                    JSONObject jsonObject=new JSONObject(response);

                    JSONArray jsonArray=jsonObject.getJSONArray("data");


                    if(response.equals("{\"data\":[]}"))
                    {
                        onBackPressed();
                        Toast.makeText(QrscanActivity.this, "no match found", Toast.LENGTH_SHORT).show();
                    }

                        for (int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);

                            String id=jsonObject1.getString("medicine_id");
                            String name=jsonObject1.getString("name");

                             Intent intent=new Intent(QrscanActivity.this,MainMedicine.class);
                               intent.putExtra("id",id);
                               intent.putExtra("name",name);
                               startActivity(intent);

                        }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).execute();


    }

    @Override
    protected void onPause() {
        super.onPause();

        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();

        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
}
