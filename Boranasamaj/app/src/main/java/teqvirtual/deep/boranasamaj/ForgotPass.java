package teqvirtual.deep.boranasamaj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class ForgotPass extends AppCompatActivity {

    EditText emailfg;
    Button fogot_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        emailfg=(EditText)findViewById(R.id.admin_forgot_email);

        fogot_btn=(Button)findViewById(R.id.admin_forgot_login);

        fogot_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailf=emailfg.getText().toString();

                StringRequest stringRequest=new StringRequest(Request.Method.GET, "http://teqvirtual.com/Boranasamaj/forgotpassword.php?emailid="+emailf, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        startActivity(new Intent(ForgotPass.this,LoginActivity.class));
                        Toast.makeText(ForgotPass.this, ""+response, Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ForgotPass.this, ""+error, Toast.LENGTH_SHORT).show();
                    }
                });

                RequestQueue requestQueue= Volley.newRequestQueue(ForgotPass.this);
                requestQueue.add(stringRequest);
            }
        });
    }
}
