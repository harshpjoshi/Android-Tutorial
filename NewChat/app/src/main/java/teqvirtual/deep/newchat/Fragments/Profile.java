package teqvirtual.deep.newchat.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
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
import androidx.fragment.app.Fragment;
import teqvirtual.deep.newchat.R;

import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.widget.Constraints.TAG;


public class Profile extends Fragment {


    ImageView imageView;
    EditText name,email,mobile;
    TextView pname,pmobile;
    Uri imageUri;
    Button save_profile;

    String userid;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference;
    StorageReference mStorage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FirebaseApp.initializeApp(getContext());

        View view=inflater.inflate(R.layout.fragment_profile, container, false);

        mDatabase=FirebaseDatabase.getInstance();
        mDatabaseReference=mDatabase.getReference();


        mStorage= FirebaseStorage.getInstance().getReference();

        Bundle bundle=getArguments();
        userid=bundle.getString("c_user");
        Log.d("prof","iddddddd"+userid);

        pname=(TextView)view.findViewById(R.id.profile_name);
        pmobile=(TextView)view.findViewById(R.id.profile_mobile);
        imageView=(ImageView)view.findViewById(R.id.profile_image);
        name=(EditText)view.findViewById(R.id.profile_name_2);
        email=(EditText)view.findViewById(R.id.profile_email);
        mobile=(EditText)view.findViewById(R.id.profile_mobile_2);
        save_profile=(Button)view.findViewById(R.id.save_profile);

        save_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setUserProfileData();

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                Intent chooser = Intent.createChooser(intent, "Choose a Picture");
                startActivityForResult(chooser,5);

            }
        });

        select_image();

        getUserProfileData();

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 5 && resultCode==RESULT_OK)
        {
            //for copy image path
            Uri path=data.getData();
            CropImage.activity(path).setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(getContext(),Profile.this);
        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result= CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri=result.getUri();
                Log.d(TAG,"image uri"+imageUri.toString());
                Picasso.get().load(imageUri).into(imageView);

                StorageReference imagePath=mStorage.child("Users").child(userid).child("Profile.jpg");

                imagePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        String image_firebase_uri=taskSnapshot.getDownloadUrl().toString();
                        Log.d(TAG,"Profile image:"+image_firebase_uri);
                        mDatabaseReference.child("Users").child(userid).child("image").setValue(image_firebase_uri);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Profile Image Not Upload", Toast.LENGTH_SHORT).show();
                    }
                });

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

            StorageReference imagePath=mStorage.child("Users").child(userid).child(imageUri.getLastPathSegment());

            imagePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    String image_firebase_uri = taskSnapshot.getDownloadUrl().toString();
                    Log.d(TAG,"Profile image:"+image_firebase_uri);
                    mDatabaseReference.child("Users").child(userid).child("image").setValue(image_firebase_uri);

                }
            });

        }
    }



    public void getUserProfileData()
    {
        mDatabaseReference.child("Users").child(userid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {



                        String name_str,image_str,mobileno_str,email_str;


                        name_str=dataSnapshot.child("name").getValue().toString();
                        image_str=dataSnapshot.child("image").getValue().toString();
                        mobileno_str=dataSnapshot.child("phonenumber").getValue().toString();
                        email_str=dataSnapshot.child("email").getValue().toString();

                        pname.setText(name_str);
                        pmobile.setText(mobileno_str);

                        name.setText(name_str);
                        mobile.setText(mobileno_str);
                        email.setText(email_str);

                        Picasso.get().load(image_str).placeholder(R.drawable.ic_launcher_foreground).into(imageView);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void setUserProfileData() {

        final String email_str,name_str,mobile_str;

        name_str=name.getText().toString();
        email_str=email.getText().toString();
        mobile_str=mobile.getText().toString();

        mDatabaseReference.child("Users").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mDatabaseReference.child("Users").child(userid).child("name").setValue(name_str);
                mDatabaseReference.child("Users").child(userid).child("email").setValue(email_str);
                mDatabaseReference.child("Users").child(userid).child("phonenumber").setValue(mobile_str);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
