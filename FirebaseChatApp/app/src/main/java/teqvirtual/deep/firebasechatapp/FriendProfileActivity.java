package teqvirtual.deep.firebasechatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FriendProfileActivity extends AppCompatActivity {

    String TAG="fre";
    String frekey,mName,mStatus,mid,mImage;

    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference;

    int currentState=0;

    ImageView profile;
    TextView name,status;
    Button send,cancel;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);

        frekey=getIntent().getStringExtra("key");

        mid=FirebaseAuth.getInstance().getCurrentUser().getUid();

        Log.d(TAG,"friendid"+frekey);

        progressDialog=new ProgressDialog(this);

        mAuth=FirebaseAuth.getInstance();
        mDatabase=FirebaseDatabase.getInstance();
        mDatabaseReference=mDatabase.getReference();

        profile=(ImageView)findViewById(R.id.friend_profile_image);
        name=(TextView)findViewById(R.id.friend_profile_name);
        status=(TextView)findViewById(R.id.friend_profile_status);

        send=(Button)findViewById(R.id.sendReq_btn);
        cancel=(Button)findViewById(R.id.cancelReq);
        cancel.setVisibility(View.INVISIBLE);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendFriendRequest();

            }
        });

        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        mDatabaseReference.child("Users").child(frekey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                mName=dataSnapshot.child("name").getValue().toString();
                mStatus=dataSnapshot.child("status").getValue().toString();
                mImage=dataSnapshot.child("image").getValue().toString();

                Picasso.get().load(mImage).placeholder(R.drawable.profile).into(profile);
                name.setText(mName);
                status.setText(mStatus);

                progressDialog.dismiss();

                mDatabaseReference.child("Request").child(frekey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChild(mid))
                        {
                            String request=dataSnapshot.child(mid).child(getString(R.string.request_type)).getValue().toString();

                            if (request.equals(getString(R.string.received)))
                            {
                                send.setEnabled(true);

                                send.setText("Cancel Request");
                                send.setBackground(getResources().getDrawable(R.color.primaryText));
                                currentState=1;
                            }
                            else if (request.equals(getString(R.string.sent)))
                            {
                                send.setEnabled(true);

                                send.setText("Accept Request");
                                cancel.setVisibility(View.VISIBLE);
                                send.setBackground(getResources().getDrawable(R.color.colorPrimary));
                                currentState=2;

                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        declineRequest();

                                    }
                                });
                            }


                        }
                        else
                        {
                            mDatabaseReference.child("Friend List").child(mid).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.hasChild(frekey))
                                    {
                                        String request=dataSnapshot.child(frekey).child(getString(R.string.request_type)).getValue().toString();

                                        cancel.setVisibility(View.INVISIBLE);
                                        send.setEnabled(true);
                                        send.setText("Unfriend");
                                        send.setBackground(getResources().getDrawable(R.color.primaryText));


                                        send.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                unFriend();

                                            }
                                        });
                                        //currentState=1;
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void unFriend() {

        mDatabaseReference.child("Friend List").child(frekey).child(mid).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        mDatabaseReference.child("Friend List").child(mid).child(frekey).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        finish();
                                    }
                                });
                    }
                });

    }

    private void declineRequest() {

        mDatabaseReference.child("Request").child(frekey).child(mid).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        mDatabaseReference.child("Request").child(mid).child(frekey).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        finish();
                                    }
                                });
                    }
                });


    }

    private void sendFriendRequest() {

        send.setEnabled(false);

       if (currentState==0)
       {
           mDatabaseReference.child("Request").child(frekey).child(mid)
                   .child(getString(R.string.request_type)).setValue(getString(R.string.received))
                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {

                           mDatabaseReference.child("Request").child(mid).child(frekey)
                                   .child(getString(R.string.request_type)).setValue(getString(R.string.sent))
                                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {

                                           send.setEnabled(true);

                                           send.setText("Cancel Request");
                                           send.setBackground(getResources().getDrawable(R.color.primaryText));
                                           currentState=1;


                                       }
                                   });

                       }
                   });
       }

        if (currentState==1)
        {
            mDatabaseReference.child("Request").child(frekey).child(mid).removeValue()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            send.setEnabled(true);
                            cancel.setVisibility(View.INVISIBLE);
                            send.setText("Send Request");
                            send.setBackground(getResources().getDrawable(R.color.colorPrimary));
                            currentState=0;


                        }
                    });
        }

        if (currentState==2)
        {
            mDatabaseReference.child("Friend List").child(frekey).child(mid)
                    .child(getString(R.string.request_type)).setValue(getString(R.string.friend))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            mDatabaseReference.child("Friend List").child(mid).child(frekey)
                                    .child(getString(R.string.request_type)).setValue(getString(R.string.friend))
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            mDatabaseReference.child("Request").child(frekey).child(mid).removeValue()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            mDatabaseReference.child("Request").child(mid).child(frekey).removeValue()
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                            cancel.setVisibility(View.INVISIBLE);
                                                                            send.setEnabled(true);
                                                                            send.setText("Unfriend");
                                                                            send.setBackground(getResources().getDrawable(R.color.primaryText));
                                                                            //currentState=1;
                                                                        }
                                                                    });
                                                        }
                                                    });

                                        }
                                    });

                        }
                    });
        }

    }
}
