package teqvirtual.deep.newchat.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import teqvirtual.deep.newchat.Adapter.SearchHolder;
import teqvirtual.deep.newchat.FriendProfileActivity;
import teqvirtual.deep.newchat.R;
import teqvirtual.deep.newchat.SearchUser;

import static android.content.Context.MODE_PRIVATE;

public class RequestFragment extends Fragment {


    View view;
    RecyclerView recyclerView;

    String mid,name,image,status;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference,mFriendRef;
    StorageReference mStorage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friend_request, container, false);

        mAuth= FirebaseAuth.getInstance();

        //mid=mAuth.getCurrentUser().getUid();
        SharedPreferences settings = getContext().getSharedPreferences("settings", MODE_PRIVATE);
        String mobile = settings.getString("mobile_key", "");
        mid =settings.getString("c_userid","");

        DatabaseReference rootRef=FirebaseDatabase.getInstance().getReference();
        DatabaseReference onlineRef=rootRef.child("Users").child(mid).child("screnn_status");
        onlineRef.setValue("Online");

        //screen status

        screenStatus();

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("Request").child(mid);
        mFriendRef=mDatabase.getReference().child("Users");

        recyclerView = (RecyclerView) view.findViewById(R.id.reqlist);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        requestFriendList();


        return view;
    }

    private void requestFriendList() {

        FirebaseRecyclerAdapter<SearchUser, SearchHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<SearchUser, SearchHolder>(
                SearchUser.class,
                R.layout.show_search_friend,
                SearchHolder.class,
                mDatabaseReference
        ) {
            @Override
            protected void populateViewHolder(final SearchHolder searchHolder, final SearchUser searchUser, int i) {

                final String userKey=getRef(i).getKey();

                mFriendRef.child(userKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String name=dataSnapshot.child("name").getValue().toString();
                        String status=dataSnapshot.child("status").getValue().toString();
                        String image=dataSnapshot.child("image").getValue().toString();

                        searchHolder.set_Name(name);
                        searchHolder.set_Status(status);
                        searchHolder.set_Profile(getContext().getApplicationContext(),image);

                        searchHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent=new Intent(getContext(), FriendProfileActivity.class);
                                intent.putExtra("key",userKey);
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

        recyclerView.setAdapter(firebaseRecyclerAdapter);


    }

    public void screenStatus()
    {
        DatabaseReference rootRef=FirebaseDatabase.getInstance().getReference();
        DatabaseReference onlineRef=rootRef.child("Users").child(mid).child("screnn_status");
        String time = new SimpleDateFormat("hh : mm a", Locale.getDefault()).format(Calendar.getInstance().getTime());
        onlineRef.onDisconnect().setValue("last seen "+time);
    }
}
