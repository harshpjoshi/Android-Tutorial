package teqvirtual.deep.healthcare.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
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
import teqvirtual.deep.healthcare.Adapter.DoctorsAdapter;
import teqvirtual.deep.healthcare.Model.DoctorModel;
import teqvirtual.deep.healthcare.Model.SubscriberModel;
import teqvirtual.deep.healthcare.R;


public class Doctor extends Fragment {

    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference,mFriendRef;
    RecyclerView subscriberList;
    String mid;
    ArrayList<DoctorModel> mUsers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_doctor, container, false);

        mAuth=FirebaseAuth.getInstance();

        mid=FirebaseAuth.getInstance().getCurrentUser().getUid();

        subscriberList=(RecyclerView)view.findViewById(R.id.doctorlist);

        mUsers=new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("Doctors");

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mUsers.clear();

                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    DoctorModel user=ds.getValue(DoctorModel.class);

                    mUsers.add(user);

                }

                DoctorsAdapter doctorsAdapter=new DoctorsAdapter(getContext(),mUsers);
                subscriberList.setAdapter(doctorsAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }


}
