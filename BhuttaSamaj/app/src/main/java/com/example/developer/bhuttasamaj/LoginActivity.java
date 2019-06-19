package com.example.developer.bhuttasamaj;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText admin_email,admin_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final ProgressDialog progressDialog=new ProgressDialog(LoginActivity.this);


        admin_email=(EditText)findViewById(R.id.admin_email);
        admin_pass=(EditText)findViewById(R.id.admin_pass);

        Button admin_log=(Button)findViewById(R.id.admin_login);

        TextView textView=(TextView)findViewById(R.id.forgottext);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgotPass.class));
            }
        });

        if(isNetworkConnected())
        {
            admin_log.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String email_holder = admin_email.getText().toString();
                    final String pass_holder = admin_pass.getText().toString();

                    if (email_holder.isEmpty() && pass_holder.isEmpty()) {
                        admin_email.setError("Email can't empty");
                        admin_pass.setError("Password can't empty");
                    }
                    if (email_holder.isEmpty()) {
                        admin_email.setError("Email can't empty");
                    }
                    if (pass_holder.isEmpty()) {
                        admin_pass.setError("Password can't empty");
                    } else {

                        StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://teqvirtual.com/Boranasamaj/adminlogin.php", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.setMessage("Please Wait.....");
                                progressDialog.show();

                                Toast.makeText(LoginActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                if(response.equals("Sucessfully Login"))
                                {
                                    Intent intent=new Intent(LoginActivity.this,AdminProfile.class);
                                    intent.putExtra("email_custom",email_holder);
                                    startActivity(intent);
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {

                                Map<String,String> map=new HashMap<>();
                                map.put("email",email_holder);
                                map.put("password",pass_holder);
                                return map;
                            }
                        };
                        RequestQueue requestQueue= Volley.newRequestQueue(LoginActivity.this);
                        requestQueue.add(stringRequest);

                    }
                }

            });
        }
        else
        {
            Toast.makeText(this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(LoginActivity.this.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
