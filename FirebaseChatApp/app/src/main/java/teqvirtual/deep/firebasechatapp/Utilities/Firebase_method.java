package teqvirtual.deep.firebasechatapp.Utilities;

import android.content.Context;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.Executor;

import androidx.annotation.NonNull;
import teqvirtual.deep.firebasechatapp.Model.UserData;

public class Firebase_method {

    FirebaseAuth mAuth;
    Context mContext;
    String TAG="firmethod";
    String userid;
    FirebaseDatabase mDataBase;
    DatabaseReference mReference;
    public Firebase_method(Context context)
    {
        mAuth=FirebaseAuth.getInstance();
        mContext=context;
        mDataBase=FirebaseDatabase.getInstance();
        mReference=mDataBase.getReference();
    }

    public void register_new_email(String email,String password)
    {
       mAuth.createUserWithEmailAndPassword(email,password)
               .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {

                       if (!task.isSuccessful())
                       {
                           Log.d(TAG,"createUserWithEmail:failure", task.getException());
                           Toast.makeText(mContext, "Registration Failed", Toast.LENGTH_SHORT).show();
                       }
                       else
                       {
                           userid=mAuth.getCurrentUser().getUid();
                       }

                   }
               });
    }

    public void send_new_userData( String name,String email,String username,String password,String image,String status)
    {
        UserData userData=new UserData(name,email,username,password,image,status,"");
        mReference.child("Users").child(mAuth.getUid()).setValue(userData);
    }
}
