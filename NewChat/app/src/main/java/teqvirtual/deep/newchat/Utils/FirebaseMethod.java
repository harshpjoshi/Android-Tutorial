package teqvirtual.deep.newchat.Utils;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import teqvirtual.deep.newchat.User;

public class FirebaseMethod {


    FirebaseAuth mAuth;
    Context mContext;
    String TAG="firmethod";
    String userid;
    FirebaseDatabase mDataBase;
    DatabaseReference mReference;

    public FirebaseMethod(Context context)
    {
        mAuth=FirebaseAuth.getInstance();
        mContext=context;
        mDataBase=FirebaseDatabase.getInstance();
        mReference=mDataBase.getReference();
    }


    public void send_new_userData(String myid,String name, String email, String phonenumber, String image,String status,String token,String s_status) {

        User user=new User(myid,name,email,phonenumber,image,status,token,s_status);

        mReference.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(user);

    }
}
