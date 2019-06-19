package teqvirtual.deep.firebasechatapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import teqvirtual.deep.firebasechatapp.Utilities.Firebase_method;

public class RegisterActivity extends AppCompatActivity {

    ImageView back_button,profile_image;
    EditText mName,mEmail,mUsername,mPassword;
    Button mRegister;
    Uri imageUri;

    String str_email,str_password,TAG="activity",str_name,str_username,str_image,userid;
    String image_firebase_uri;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference;
    StorageReference mStorage;

    Firebase_method firebase_method;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FirebaseApp.initializeApp(this);

        firebase_method=new Firebase_method(this);

       mAuth=FirebaseAuth.getInstance();
       mDatabase=FirebaseDatabase.getInstance();
       mDatabaseReference=mDatabase.getReference();

       mStorage= FirebaseStorage.getInstance().getReference();

        back_button=(ImageView)findViewById(R.id.back_activity);
        profile_image=(ImageView)findViewById(R.id.friendprofileimg);
        mName=(EditText)findViewById(R.id.name_reg);
        mEmail=(EditText)findViewById(R.id.email_reg);
        mUsername=(EditText)findViewById(R.id.username_reg);
        mPassword=(EditText)findViewById(R.id.password_reg);
        mRegister=(Button)findViewById(R.id.register_user);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                register_new_user();

            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                Intent chooser = Intent.createChooser(intent, "Choose a Picture");
                startActivityForResult(chooser,5);

            }
        });

        setUpFirebaseAuthntication();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 5 && resultCode==RESULT_OK)
        {
            //for copy image path
            Uri path=data.getData();
            CropImage.activity(path).setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(RegisterActivity.this);
        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result= CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri=result.getUri();
                Log.d(TAG,"image uri"+imageUri.toString());
                Picasso.get().load(imageUri).into(profile_image);
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

    private void select_image()
    {
        if (imageUri!=null)
        {

            StorageReference imagePath=mStorage.child(getString(R.string.user)).child(mAuth.getUid()).child(imageUri.getLastPathSegment());

            imagePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                  image_firebase_uri=taskSnapshot.getDownloadUrl().toString();
                  Log.d(TAG,"Profile image:"+image_firebase_uri);
                  mDatabaseReference.child(getString(R.string.user)).child(userid).child("image").setValue(image_firebase_uri);

                }
            });

        }
    }

    private void register_new_user() {
        str_name=mName.getText().toString();
        str_username=mUsername.getText().toString();
        str_email=mEmail.getText().toString().trim();
        str_password=mPassword.getText().toString().trim();

        if (check_input(str_email,str_password,str_name))
        {
            firebase_method.register_new_email(str_email,str_password);
        }

    }

    private boolean check_input(String email,String password,String name)
    {
        if (email.isEmpty())
        {
            Toast.makeText(this, "EMail not Empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (password.isEmpty())
        {
            Toast.makeText(this, "Password not empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (name.isEmpty())
        {
            Toast.makeText(this, "Name Not Empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            return true;
        }
    }

    private void setUpFirebaseAuthntication()
    {
        mAuth=FirebaseAuth.getInstance();

        Log.d(TAG,"setUpFirebaseAuthnetication: ready for sent data");

        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user=mAuth.getCurrentUser();

                if (user!=null)
                {
                   userid=mAuth.getCurrentUser().getUid();

                    Log.d(TAG,"USer id"+userid);

                    mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Log.d(TAG,"new Profile image"+image_firebase_uri);
                            firebase_method.send_new_userData(str_name,str_email,str_username,str_password,"def","Available");

                            select_image();

                            Toast.makeText(RegisterActivity.this, "Registred Success", Toast.LENGTH_SHORT).show();
                            mAuth.signOut();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    finish();
                }

            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mAuthListener != null)
        {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
