package teqvirtual.deep.firebasechatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.libizo.CustomEditText;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class Account_Setting_Activity extends AppCompatActivity {

    String TAG="settings";

    Uri imageUri;

    ImageView back,update;
    CircleImageView profile;
    CustomEditText mstatus,mname,mmobile,musername;
    String userId;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference;
    StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account__setting_);

        FirebaseApp.initializeApp(this);
        mAuth=FirebaseAuth.getInstance();
        mDatabase=FirebaseDatabase.getInstance();
        mDatabaseReference=mDatabase.getReference();
        mStorage= FirebaseStorage.getInstance().getReference();


        back=(ImageView)findViewById(R.id.back_arrow);
        update=(ImageView)findViewById(R.id.updatedone);
        profile=(CircleImageView)findViewById(R.id.changeprofile);
        mstatus=(CustomEditText)findViewById(R.id.change_status);
        mname=(CustomEditText)findViewById(R.id.change_name);
        mmobile=(CustomEditText)findViewById(R.id.change_mobile);
        musername=(CustomEditText)findViewById(R.id.change_username);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setUserProfileData();

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                Intent chooser = Intent.createChooser(intent, "Choose a Picture");
                startActivityForResult(chooser,5);

            }
        });

        getUserProfileData();

    }

    private void setUserProfileData() {

        final String status_str,name_str,username_str;

        name_str=mname.getText().toString();
        username_str=musername.getText().toString();
        status_str=mstatus.getText().toString();

        mDatabaseReference.child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mDatabaseReference.child(getString(R.string.user)).child(userId).child("name").setValue(name_str);
                mDatabaseReference.child(getString(R.string.user)).child(userId).child("status").setValue(status_str);
                mDatabaseReference.child(getString(R.string.user)).child(userId).child("username").setValue(username_str);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void getUserProfileData()
    {
        mDatabaseReference.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Log.d(TAG,"On User"+FirebaseAuth.getInstance().getCurrentUser().getUid());

                        String name,username,image,status;

                        userId=FirebaseAuth.getInstance().getCurrentUser().getUid();

                        name=dataSnapshot.child("name").getValue().toString();
                        username=dataSnapshot.child("username").getValue().toString();
                        image=dataSnapshot.child("image").getValue().toString();
                        status=dataSnapshot.child("status").getValue().toString();

                        mname.setText(name);
                        musername.setText(username);
                        mstatus.setText(status);

                        Picasso.get().load(image).placeholder(R.drawable.com_facebook_profile_picture_blank_square).into(profile);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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
                    .start(Account_Setting_Activity.this);
        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result= CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri=result.getUri();
                Log.d(TAG,"image uri"+imageUri.toString());
                Picasso.get().load(imageUri).into(profile);

                StorageReference imagePath=mStorage.child(getString(R.string.user)).child(mAuth.getUid()).child("Profile.jpg");

                imagePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        String image_firebase_uri=taskSnapshot.getDownloadUrl().toString();
                        Log.d(TAG,"Profile image:"+image_firebase_uri);
                        mDatabaseReference.child(getString(R.string.user)).child(userId).child("image").setValue(image_firebase_uri);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Account_Setting_Activity.this, "Profile Image Not Upload", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }
}
