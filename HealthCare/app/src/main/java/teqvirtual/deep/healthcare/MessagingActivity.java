package teqvirtual.deep.healthcare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import teqvirtual.deep.healthcare.Adapter.MessageAdapter;
import teqvirtual.deep.healthcare.Firebase.SendNotification;
import teqvirtual.deep.healthcare.Model.Chat;
import teqvirtual.deep.healthcare.Model.DoctorModel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MessagingActivity extends AppCompatActivity {

    CircleImageView imageView;
    TextView textView,status;

    MessageAdapter messageAdapter;
    List<Chat> chatList;
    RecyclerView recyclerView;

    String s_Status;

    String fuser;
    DatabaseReference reference;
    String userId;
    ImageButton send_btn;
    EditText send_text;
    ImageView uploadImage;
    SendNotification sendNotification;
    FirebaseStorage storage;
    StorageReference storageReference;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);


        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sendNotification=new SendNotification();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        recyclerView=(RecyclerView)findViewById(R.id.recyclerview_msg);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        imageView=(CircleImageView)findViewById(R.id.profile_image);
        textView=(TextView)findViewById(R.id.username);
        status=(TextView)findViewById(R.id.status);
        uploadImage=(ImageView)findViewById(R.id.uploadimage);

        send_text=(EditText)findViewById(R.id.textsend);
        send_btn=(ImageButton)findViewById(R.id.btn_send);

        intent=getIntent();

        userId=intent.getStringExtra("userId");

        Log.d("hello","mid "+FirebaseAuth.getInstance().getCurrentUser().getUid());
        Log.d("hello","fid "+userId);

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg=send_text.getText().toString();

                if (msg.isEmpty())
                {
                    Toast.makeText(MessagingActivity.this, "You Can Not Send Empty Message", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String time = new SimpleDateFormat("hh : mm a", Locale.getDefault()).format(Calendar.getInstance().getTime());

                    SendMessage(FirebaseAuth.getInstance().getCurrentUser().getUid(),userId,msg,time);
                }

                send_text.setText("");
            }
        });


        reference= FirebaseDatabase.getInstance().getReference("Subscribers").child(userId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                DoctorModel user=dataSnapshot.getValue(DoctorModel.class);

                textView.setText(user.getFirstname());


                if (user.getImage().equals("default"))
                {
                    imageView.setImageResource(R.mipmap.ic_launcher);
                }
                else
                {
                    Picasso.get().load(user.getImage()).into(imageView);
                }

                readMessage(fuser);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"SELECT IMAGE"),1);
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK)
        {
            Uri imageUri = data.getData();

            DatabaseReference reference=FirebaseDatabase.getInstance().getReference();

            String time = new SimpleDateFormat("hh : mm a", Locale.getDefault()).format(Calendar.getInstance().getTime());


            StorageReference filePath=storageReference.child("Message_images").child("img"+time+".jpg");

            filePath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if (task.isSuccessful())
                    {
                        String downloadURL=task.getResult().getDownloadUrl().toString();

                        String time = new SimpleDateFormat("hh : mm a", Locale.getDefault()).format(Calendar.getInstance().getTime());

                        DatabaseReference ref=reference.child("Chats").push();

                        Map<String,Object> map=new HashMap<>();
                        map.put("chat_id",ref.getKey());
                        map.put("message",downloadURL);
                        map.put("sender",FirebaseAuth.getInstance().getCurrentUser().getUid());
                        map.put("receiver",userId);
                        map.put("time",time);
                        map.put("type","image");

                       ref.setValue(map);

                    }

                }
            });

        }
    }


    public void SendMessage(String sender,String receiver,String message,String time)
    {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();

        DatabaseReference ref=reference.child("Chats").push();

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("chat_id",ref.getKey());
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("type","text");
        hashMap.put("time",time);

        ref.setValue(hashMap);


        reference.child("Subscribers").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String token = dataSnapshot.child("token").getValue().toString();

                reference.child("Doctors").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String name=dataSnapshot.child("firstname").getValue().toString();

                        sendNotification.sendNotification(token,message,name);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void readMessage(final String mid)
    {
        chatList=new ArrayList<>();

        reference= FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                chatList.clear();


                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Chat chat=snapshot.getValue(Chat.class);



                    if (chat.getReceiver().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && chat.getSender().equals(userId)  ||
                            chat.getReceiver().equals(userId) && chat.getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                    {
                        chatList.add(chat);
                    }

                    messageAdapter=new MessageAdapter(MessagingActivity.this,chatList);
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {

                            if(messageAdapter.getItemCount()!=0)
                            {
                                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount()-1);
                            }

                        }
                    });
                    recyclerView.setAdapter(messageAdapter);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
