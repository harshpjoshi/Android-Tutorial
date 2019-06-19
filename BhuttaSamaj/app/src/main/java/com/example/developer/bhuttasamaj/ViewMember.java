package com.example.developer.bhuttasamaj;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewMember extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Member> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_member);

        final ProgressDialog progressDialog=new ProgressDialog(ViewMember.this);

        progressDialog.setMessage("Please Wait....");
        progressDialog.show();

        //go back to activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        StringRequest stringRequest =new StringRequest(Request.Method.POST, "http://teqvirtual.com/Boranasamaj/fetchmember.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               progressDialog.dismiss();
                JSONArray jsonArray= null;
                try {
                    arrayList=new ArrayList<>();
                    jsonArray = new JSONArray(response);
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String mem_name=jsonObject.getString("name");
                        String mem_dob=jsonObject.getString("dob");
                        String mem_address=jsonObject.getString("address");
                        String mem_city=jsonObject.getString("city");
                        String mem_mobile=jsonObject.getString("mobile");
                        String mem_occu=jsonObject.getString("occupation");
                        String mem_image=jsonObject.getString("image");

                        Member member=new Member();
                        member.setName(mem_name);
                        member.setDob(mem_dob);
                        member.setCity(mem_city);
                        member.setAddress(mem_address);
                        member.setDesignation(mem_occu);
                        member.setImage(mem_image);
                        member.setMobile(mem_mobile);

                        arrayList.add(member);
                    }

                    ListView listView=(ListView)findViewById(R.id.mem_list);
                    MyAdapter myAdapter=new MyAdapter(ViewMember.this,arrayList);
                    listView.setAdapter(myAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ViewMember.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue=Volley.newRequestQueue(ViewMember.this);
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
