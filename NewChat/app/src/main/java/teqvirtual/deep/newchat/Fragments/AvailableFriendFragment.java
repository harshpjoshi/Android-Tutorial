package teqvirtual.deep.newchat.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import teqvirtual.deep.newchat.Adapter.AllFriendsAdapter;
import teqvirtual.deep.newchat.R;
import teqvirtual.deep.newchat.User;

import static android.content.Context.MODE_PRIVATE;


public class AvailableFriendFragment extends Fragment {

    FirebaseUser fuser;
    DatabaseReference reference;
    String userId;
    RecyclerView recyclerView;

    String current_user;
    ArrayList<User> arrayList;
    AllFriendsAdapter allFriendsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_available_friend, container, false);

        recyclerView=(RecyclerView)view.findViewById(R.id.avail_friends);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        SharedPreferences settings = getContext().getSharedPreferences("settings", MODE_PRIVATE);
        String mobile = settings.getString("mobile_key", "");
        current_user=settings.getString("c_userid","");


        DatabaseReference rootRef=FirebaseDatabase.getInstance().getReference();
        DatabaseReference onlineRef=rootRef.child("Users").child(current_user).child("screnn_status");
        onlineRef.setValue("Online");

        //screen status

        screenStatus();

        getUsers();

        String token=FirebaseInstanceId.getInstance().getToken();

        Log.d("Message","toooken is:  "+token);

        return view;
    }

    public void getUsers()
    {
        reference=FirebaseDatabase.getInstance().getReference("Users");

        arrayList=new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                arrayList.clear();

                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    User user=ds.getValue(User.class);

                    arrayList.add(user);

                }

                allFriendsAdapter=new AllFriendsAdapter(getContext(),arrayList,current_user);
                recyclerView.setAdapter(allFriendsAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void screenStatus()
    {
        DatabaseReference rootRef=FirebaseDatabase.getInstance().getReference();
        DatabaseReference onlineRef=rootRef.child("Users").child(current_user).child("screnn_status");
        String time = new SimpleDateFormat("hh : mm a", Locale.getDefault()).format(Calendar.getInstance().getTime());
        onlineRef.onDisconnect().setValue("last seen "+time);
    }
}
