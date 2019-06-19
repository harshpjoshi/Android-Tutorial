package teqvirtual.deep.fireinsertselect;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String image;
    Button button;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference personref;
    ArrayList<User> arrayList;

    StorageReference storageReference;
    ImageView imageView;
    Uri imguri;

    FirebaseHelper firebaseHelper;
    CustomAdapter customAdapter;

    String storage_path="image/";
    String database_path="image";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();

        storageReference= FirebaseStorage.getInstance().getReference();

        final EditText text=(EditText)findViewById(R.id.text);
        final EditText text2=(EditText)findViewById(R.id.text2);

        arrayList=new ArrayList<>();



        firebaseHelper=new FirebaseHelper(databaseReference);

        button=(Button)findViewById(R.id.add);

        imageView=(ImageView)findViewById(R.id.image);

       final ListView listView=(ListView)findViewById(R.id.list);

       final DatabaseReference person_ref=databaseReference.child("Person");

      person_ref.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              arrayList.clear();
              for (DataSnapshot ds:dataSnapshot.getChildren())
              {
                  User user=ds.getValue(User.class);
                  arrayList.add(user);
              }
              customAdapter=new CustomAdapter(MainActivity.this,arrayList);
              listView.setAdapter(customAdapter);
          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
      });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              final String name=text.getText().toString();
              final String address=text2.getText().toString();

                final User user=new User();
              final StorageReference ref=storageReference.child(storage_path+System.currentTimeMillis()+"."+getImageExt(imguri));
              ref.putFile(imguri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                  @Override
                  public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                      return ref.getDownloadUrl();
                  }
              }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                  @Override
                  public void onComplete(@NonNull Task<Uri> task) {

                      if (task.isSuccessful()) {
                          Uri downloadUri = task.getResult();
                          image=downloadUri.toString();

                          Log.d("mainact","valis:"+image);
                          user.setImage(image);
                          user.setName(name);
                          user.setAddress(address);
                          person_ref.push().setValue(user);

                          Toast.makeText(MainActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                      } else {
                          Toast.makeText(MainActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                      }

                  }
              });





            }
        });

        Button browse=(Button)findViewById(R.id.browse);

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select image"),1234);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode==1234 && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            imguri=data.getData();

            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imguri);
                imageView.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getImageExt(Uri uri)
    {
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


}
