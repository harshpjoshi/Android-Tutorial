package teqvirtual.deep.healthcare.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import teqvirtual.deep.healthcare.Adapter.SearchHolder;
import teqvirtual.deep.healthcare.Adapter.SubscriberAdapter;
import teqvirtual.deep.healthcare.Adapter.SubscriberHolder;
import teqvirtual.deep.healthcare.DoctorMessagingActivity;
import teqvirtual.deep.healthcare.FireRecycler.FirebaseRecyclerAdapters;
import teqvirtual.deep.healthcare.MessagingActivity;
import teqvirtual.deep.healthcare.Model.SubscriberModel;
import teqvirtual.deep.healthcare.R;


public class Subscriber extends Fragment {

    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference,mFriendRef;
    RecyclerView subscriberList;
    String mid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_subscriber, container, false);

        mAuth=FirebaseAuth.getInstance();

        mid=FirebaseAuth.getInstance().getCurrentUser().getUid();

        Log.d("hello2","mid"+mid);

        subscriberList=(RecyclerView)view.findViewById(R.id.subscriberlist);
        subscriberList.setLayoutManager(new LinearLayoutManager(getContext()));

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("Friend List").child(mid);
        mFriendRef=mDatabase.getReference().child("Subscribers");

        Log.d("signup","surrent userid"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"\n"+"email "+FirebaseAuth.getInstance().getCurrentUser().getEmail());


        requestFriendList();

        return view;
    }

    public void requestFriendList() {

        FirebaseRecyclerAdapters<SubscriberModel, SubscriberHolder> firebaseRecyclerAdapters=new FirebaseRecyclerAdapters<SubscriberModel, SubscriberHolder>(
                SubscriberModel.class,
                R.layout.subscriber_friend_list_item,
                SubscriberHolder.class,
                mDatabaseReference

        ) {
            @Override
            protected void populateViewHolder(SubscriberHolder viewHolder, SubscriberModel model, int position) {

                String userKey=getRef(position).getKey();

                mFriendRef.child(userKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String name=dataSnapshot.child("firstname").getValue().toString();
                        String image=dataSnapshot.child("image").getValue().toString();

                        viewHolder.setFirstname(name);
                        viewHolder.setImage(getContext(),image);

                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent=new Intent(getContext(), MessagingActivity.class);
                                intent.putExtra("userId",userKey);
                                startActivity(intent);

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
}
