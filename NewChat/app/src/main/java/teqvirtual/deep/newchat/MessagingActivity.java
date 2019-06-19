package teqvirtual.deep.newchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import teqvirtual.deep.newchat.Adapter.MessageAdapter;
import teqvirtual.deep.newchat.Notification.SendNotification;

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

    SendNotification sendNotification;

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

        recyclerView=(RecyclerView)findViewById(R.id.recyclerview_msg);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        imageView=(CircleImageView)findViewById(R.id.profile_image);
        textView=(TextView)findViewById(R.id.username);
        status=(TextView)findViewById(R.id.status);

        send_text=(EditText)findViewById(R.id.textsend);
        send_btn=(ImageButton)findViewById(R.id.btn_send);

        intent=getIntent();

        userId=intent.getStringExtra("userId");


        //fuser= FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences settings = getSharedPreferences("settings", MODE_PRIVATE);
        String mobile = settings.getString("mobile_key", "");
        fuser=settings.getString("c_userid","");

        DatabaseReference rootRef=FirebaseDatabase.getInstance().getReference();
        DatabaseReference onlineRef=rootRef.child("Users").child(fuser).child("screnn_status");
        onlineRef.setValue("Online");

        //screen status

        screenStatus();

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

                    SendMessage(fuser,userId,msg,time);
                }

                send_text.setText("");
            }
        });





        reference= FirebaseDatabase.getInstance().getReference("Users").child(userId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user=dataSnapshot.getValue(User.class);

                textView.setText(user.getName());
                status.setText(user.getScrenn_status());

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

    }

    public void SendMessage(String sender,String receiver,String message,String time)
    {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("time",time);

        reference.child("Chats").push().setValue(hashMap);

        reference.child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String token = dataSnapshot.child("token").getValue().toString();

                reference.child("Users").child(fuser).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String name=dataSnapshot.child("name").getValue().toString();

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

                    if (chat.getReceiver().equals(mid) && chat.getSender().equals(userId)  ||
                            chat.getReceiver().equals(userId) && chat.getSender().equals(mid))
                    {
                        chatList.add(chat);
                    }

                    messageAdapter=new MessageAdapter(MessagingActivity.this,chatList);
                    recyclerView.setAdapter(messageAdapter);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void screenStatus()
    {
        DatabaseReference rootRef=FirebaseDatabase.getInstance().getReference();
        DatabaseReference onlineRef=rootRef.child("Users").child(fuser).child("screnn_status");
        String time = new SimpleDateFormat("hh : mm a", Locale.getDefault()).format(Calendar.getInstance().getTime());
        onlineRef.onDisconnect().setValue("last seen "+time);
    }

}
