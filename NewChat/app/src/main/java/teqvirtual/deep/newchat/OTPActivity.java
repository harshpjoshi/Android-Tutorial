package teqvirtual.deep.newchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import androidx.appcompat.app.AppCompatActivity;
import teqvirtual.deep.newchat.Utils.FirebaseMethod;

public class OTPActivity extends AppCompatActivity {

    Button getOtp;
    EditText otpString;

    FirebaseAuth auth;

    String code_send,userid,mobile;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference;


    FirebaseMethod firebaseMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        auth = FirebaseAuth.getInstance();

        Intent intent = getIntent();

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();

        firebaseMethod=new FirebaseMethod(this);

        code_send = intent.getStringExtra("code");
        mobile=intent.getStringExtra("mobile");

        Log.d("otpact","mobile"+mobile);

        Log.d("main", "jth" + code_send);

        getOtp = (Button) findViewById(R.id.btn);

        otpString = (EditText) findViewById(R.id.otp_text);

        getOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verifyOTP();

            }
        });
    }

    private void verifyOTP() {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(code_send, otpString.getText().toString());

        signInWithPhoneAuthCredential(credential);

    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("otpact", "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();

                            mAuth=FirebaseAuth.getInstance();


                            if (user!=null)
                            {
                                userid=mAuth.getCurrentUser().getUid();

                                Log.d("otpact","USer id"+userid);

                                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        SharedPreferences settings = getSharedPreferences("settings", MODE_PRIVATE);

                                        // Writing data to SharedPreferences
                                        SharedPreferences.Editor editor = settings.edit();
                                        editor.putString("mobile_key", mobile);
                                        editor.putString("c_userid",userid);
                                        editor.commit();

                                        ///Log.d("otpact","new Profile image"+image_firebase_uri);
                                        firebaseMethod.send_new_userData(userid,"name","email",mobile,"image","status", FirebaseInstanceId.getInstance().getToken(),"");

                                        //select_image();

                                        Toast.makeText(OTPActivity.this, "Registred Success", Toast.LENGTH_SHORT).show();
                                        mAuth.signOut();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                finish();
                            }

                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("otpact", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
}

