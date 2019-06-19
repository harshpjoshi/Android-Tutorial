package teqvirtual.deep.healthcare.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import teqvirtual.deep.healthcare.Adapter.SearchHolder;
import teqvirtual.deep.healthcare.FireRecycler.FirebaseRecyclerAdapters;
import teqvirtual.deep.healthcare.Firebase.SendNotification;
import teqvirtual.deep.healthcare.Model.SubscriberModel;
import teqvirtual.deep.healthcare.R;


public class Requests extends Fragment {

    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference,mFriendRef;
    RecyclerView subscriberList;
    String mid;

    SendNotification sendNotification;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mAuth=FirebaseAuth.getInstance();

        mid=FirebaseAuth.getInstance().getCurrentUser().getUid();

        sendNotification=new SendNotification();

        Log.d("hello","mid"+mid);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("Request").child(mid);
        mFriendRef=mDatabase.getReference().child("Subscribers");

        View view=inflater.inflate(R.layout.fragment_requests, container, false);

        subscriberList=(RecyclerView)view.findViewById(R.id.subscriberlist);
        subscriberList.setLayoutManager(new LinearLayoutManager(getContext()));

        requestFriendList();

        return view;
    }

    public void requestFriendList() {

       FirebaseRecyclerAdapters<SubscriberModel, SearchHolder> firebaseRecyclerAdapters=new FirebaseRecyclerAdapters<SubscriberModel, SearchHolder>(
               SubscriberModel.class,
               R.layout.request_list_item,
               SearchHolder.class,
               mDatabaseReference

       ) {
           @Override
           protected void populateViewHolder(SearchHolder viewHolder, SubscriberModel model, int position) {

               String userKey=getRef(position).getKey();

               mFriendRef.child(userKey).addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {

                       String name=dataSnapshot.child("firstname").getValue().toString();
                       String image=dataSnapshot.child("image").getValue().toString();

                       viewHolder.setFirstname(name);
                       viewHolder.setImage(getContext().getApplicationContext(),image);
                       viewHolder.getAccept().setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               acceptRequest(userKey);
                           }
                       });

                       viewHolder.getDecline().setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {

                               declineRequest(userKey);

                           }
                       });
                   }

                   @Override
                   public void onCancelled(DatabaseError databaseError) {

                   }
               });
           }
       };

       subscriberList.setAdapter(firebaseRecyclerAdapters);

    }

    private void acceptRequest(String frekey) {

        DatabaseReference mref=FirebaseDatabase.getInstance().getReference();

        mref.child("Friend List").child(frekey).child(mid)
                .child("request_type").setValue("Friends")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        mref.child("Friend List").child(mid).child(frekey)
                                .child("request_type").setValue("Friends")
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        mref.child("Request").child(frekey).child(mid).removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        mref.child("Request").child(mid).child(frekey).removeValue()
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                        //send notification

                                                                        mref.child("Subscribers").child(frekey).addValueEventListener(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                if (task.isSuccessful())
                                                                                {
                                                                                    Log.d("accept","token "+dataSnapshot.child("token").getValue());

                                                                                    String token= (String) dataSnapshot.child("token").getValue();

                                                                                    mref.child("Doctors").child(mid).addValueEventListener(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                            String firstname= (String) dataSnapshot.child("firstname").getValue();

                                                                                            sendNotification.sendNotification(token,"Your request is accepted by Doctor",firstname);
                                                                                        }

                                                                                        @Override
                                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                        }
                                                                                    });

                                                                                }
                                                                                else
                                                                                {
                                                                                    Log.d("accept","errorrrrr");
                                                                                }

                                                                            }

                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                Log.d("accept","errorr "+databaseError.getDetails());

                                                                            }
                                                                        });

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


    private void declineRequest(String frekey) {

        DatabaseReference mref=FirebaseDatabase.getInstance().getReference();

        mref.child("Request").child(frekey).child(mid).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        mref.child("Request").child(mid).child(frekey).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        getActivity().finish();
                                    }
                                });
                    }
                });


    }

}
