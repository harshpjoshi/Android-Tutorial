package teqvirtual.deep.healthcare;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import teqvirtual.deep.healthcare.Firebase.SendNotification;
import teqvirtual.deep.healthcare.Helper.AsyncResponse;
import teqvirtual.deep.healthcare.Helper.Utils;
import teqvirtual.deep.healthcare.Helper.WebserviceCall;

public class DoctorSubProfile extends AppCompatActivity {

    String url="https://teqvirtual.com/healthcare/index.php";
    String doctype,f_doc_id;
    MaterialButton follow_btn;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mDatabase;
    String mid;
    SendNotification sendNotification;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_sub_profile);

        Intent intent=getIntent();

        mAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        mDatabase= firebaseDatabase.getReference();
        sendNotification=new SendNotification();

        String doc_id=intent.getStringExtra("id");
        f_doc_id=intent.getStringExtra("f_doctor_id");

        Log.d("hello..."," "+doc_id+"   "+f_doc_id);

        mid=FirebaseAuth.getInstance().getCurrentUser().getUid();


        follow_btn=(MaterialButton)findViewById(R.id.follow_btn);

        String key[]=new String[]{"action","doctor_id"};
        String val[]=new String[]{"doctor_view_join",doc_id};

        String jsonRequest= Utils.createJsonRequest(key,val);

        new WebserviceCall(DoctorSubProfile.this, url, jsonRequest, "Loading...", true, new AsyncResponse() {
            @Override
            public void onCallback(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);

                    JSONArray jsonArray=jsonObject.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);

                        String id=jsonObject1.getString("doctor_id");
                        String firstname=jsonObject1.getString("firstname");
                        String lastname=jsonObject1.getString("lastname");
                        String image=jsonObject1.getString("image");
                        String mobileno=jsonObject1.getString("mobileno");
                        String address=jsonObject1.getString("address");
                        String email=jsonObject1.getString("email");
                        doctype=jsonObject1.getString("type_name");

                        ImageView imageView=(ImageView)findViewById(R.id.doc_img);

                        Picasso.get().load(image).into(imageView);

                        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
                        toolbar.setTitle(firstname);
                        setSupportActionBar(toolbar);

                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                        CollapsingToolbarLayout collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsebar);
                        Typeface typeface = ResourcesCompat.getFont(DoctorSubProfile.this, R.font.rubik_medium);
                        collapsingToolbarLayout.setCollapsedTitleTypeface(typeface);
                        collapsingToolbarLayout.setExpandedTitleTypeface(typeface);
                        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);


                        TextView doc_name=(TextView)findViewById(R.id.doc_name);
                        TextView doc_mobile=(TextView)findViewById(R.id.doc_mobile);
                        TextView doc_email=(TextView)findViewById(R.id.doc_email);
                        TextView doc_address=(TextView)findViewById(R.id.doc_address);
                        TextView doc_type=(TextView)findViewById(R.id.doc_type);

                        doc_name.setText(firstname+" "+lastname);
                        doc_mobile.setText(mobileno);
                        doc_email.setText(email);
                        doc_address.setText(address);
                        doc_type.setText(doctype);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).execute();


        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Friend List");

        reference.child(mid).child(f_doc_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String type= (String) dataSnapshot.child("request_type").getValue();

                Log.d("profile","oooo "+type);

                if (type!=null)
                {
                    follow_btn.setText("Followed");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        follow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (follow_btn.getText().toString()!="Followed")
                {
                    sendFollowRequest();
                }


            }
        });



    }

    private void sendFollowRequest() {

        mDatabase.child("Request").child(f_doc_id).child(mid).child("request_type").setValue("Receive")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        mDatabase.child("Request").child(mid).child(f_doc_id).child("request_type").setValue("Send")
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        //send notification

                                        mDatabase.child("Doctors").child(f_doc_id).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                String token=dataSnapshot.child("token").getValue().toString();

                                                mDatabase.child("Subscribers").child(mid).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                        String name_hol=dataSnapshot.child("firstname").getValue().toString();

                                                        sendNotification.sendNotification(token,"Request is Receved from Subscriber",name_hol);

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    }
                                });

                    }
                });

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
