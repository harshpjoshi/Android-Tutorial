package teqvirtual.deep.firebasechatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import teqvirtual.deep.firebasechatapp.Holder.MessageAdapter;
import teqvirtual.deep.firebasechatapp.Model.Chat;
import teqvirtual.deep.firebasechatapp.Model.UserData;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessagingActivity extends AppCompatActivity {

    CircleImageView imageView;
    TextView textView;

    MessageAdapter messageAdapter;
    List<Chat> chatList;
    RecyclerView recyclerView;

    String s_Status;

    FirebaseUser fuser;
    DatabaseReference reference;
    String userId;
    ImageButton send_btn;
    EditText send_text;

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

        recyclerView=(RecyclerView)findViewById(R.id.recyclerview_msg);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        imageView=(CircleImageView)findViewById(R.id.profile_image);
        textView=(TextView)findViewById(R.id.username);

        send_text=(EditText)findViewById(R.id.textsend);
        send_btn=(ImageButton)findViewById(R.id.btn_send);

        intent=getIntent();

        userId=intent.getStringExtra("userId");

        fuser= FirebaseAuth.getInstance().getCurrentUser();

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
                    SendMessage(fuser.getUid(),userId,msg);
                }

                send_text.setText("");
            }
        });





        reference= FirebaseDatabase.getInstance().getReference("Users").child(userId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserData user=dataSnapshot.getValue(UserData.class);

                textView.setText(user.getUsername()+"\n"+"("+s_Status+")");

                if (user.getImage().equals("default"))
                {
                    imageView.setImageResource(R.mipmap.ic_launcher);
                }
                else
                {
                    Picasso.get().load(user.getImage()).into(imageView);
                }

                readMessage(fuser.getUid());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void SendMessage(String sender,String receiver,String message)
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);

        reference.child("Chats").push().setValue(hashMap);
    }


    private void readMessage(final String mid)
    {
        chatList=new ArrayList<>();

        reference=FirebaseDatabase.getInstance().getReference("Chats");
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


    public String screenStatus(String sc_status)
    {
        reference=FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("screnn_status",sc_status);

        reference.updateChildren(hashMap);

        return sc_status;
    }

    @Override
    protected void onResume() {
        super.onResume();

        s_Status=screenStatus("Online");
    }

    @Override
    protected void onPause() {
        super.onPause();

        s_Status=screenStatus("Offline");
    }
}
