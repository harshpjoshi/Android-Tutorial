package teqvirtual.deep.fireinsertselect;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class FirebaseHelper {

    DatabaseReference databaseReference;
    Boolean saved;
    ArrayList<User> arrayList=new ArrayList<>();

    public FirebaseHelper(DatabaseReference databaseReference)
    {
        this.databaseReference=databaseReference;
    }

    public Boolean save(User user)
    {
        if (user==null)
        {
            saved=false;
        }
        else
        {
            databaseReference.child("Person").push().setValue(user);
            saved=true;
        }

        return saved;
    }

    private void fetchData(DataSnapshot dataSnapshot)
    {
        arrayList.clear();

        for (DataSnapshot ds:dataSnapshot.getChildren())
        {
            User user=dataSnapshot.getValue(User.class);
            arrayList.add(user);
        }
    }

    public ArrayList<User> retrive()
    {

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                fetchData(dataSnapshot);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                fetchData(dataSnapshot);

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return arrayList;
    }
}
