package teqvirtual.deep.newchat;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button getOtp;
    EditText otpString;

    FirebaseAuth auth;

    String code_send;
    String otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        auth=FirebaseAuth.getInstance();

        getOtp=(Button)findViewById(R.id.button);

        otpString=(EditText)findViewById(R.id.mobile);

        getOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                otp=otpString.getText().toString();
                sendOTP(otp);

            }
        });

    }

    private void sendOTP(String otp) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                otp,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks



    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            Log.d("mseesage","msg"+phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

            Log.d("mseesage","msg"+e.getMessage());

        }


        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);


            code_send=s;
            Log.d("message","msg"+s);

            Intent intent=new Intent(MainActivity.this,OTPActivity.class);
            intent.putExtra("code",code_send);
            intent.putExtra("mobile",otp);
            startActivity(intent);
        }
    };
}
