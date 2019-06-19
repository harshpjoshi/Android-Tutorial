package teqvirtual.deep.healthcare.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import teqvirtual.deep.healthcare.Model.Chat;
import teqvirtual.deep.healthcare.R;


import static android.content.Context.MODE_PRIVATE;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    int MSG_TYPE_LEFT=0,MSG_TYPE_RIGHT=1;

    Context context;
    List<Chat> mUser;

    String text;

    DatabaseReference reference;

    public MessageAdapter(Context context, List<Chat> mUser)
    {
        this.context=context;
        this.mUser=mUser;
        reference=FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       if (viewType==MSG_TYPE_RIGHT)
       {
           View view=LayoutInflater.from(context).inflate(R.layout.chat_item_right,null);
           return new MyViewHolder(view);
       }
       else
       {
           View view=LayoutInflater.from(context).inflate(R.layout.chat_item_left,null);
           return new MyViewHolder(view);
       }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Chat chat=mUser.get(position);

        Log.d("welcome333","oooo "+chat.getType());

        if (chat.getType().equals("text") || chat.getType()==null)
        {
            holder.showMessage.setText(chat.getMessage());
            holder.time.setText(chat.getTime());
            holder.sendimg.setVisibility(View.GONE);
        }
        else {
            holder.showMessage.setVisibility(View.GONE);
            Picasso.get().load(chat.getMessage()).placeholder(R.drawable.clinic).into(holder.sendimg);
        }
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView showMessage,time;
        ImageView sendimg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            showMessage=(TextView)itemView.findViewById(R.id.show_message);
            time=(TextView)itemView.findViewById(R.id.time);
            sendimg=(ImageView)itemView.findViewById(R.id.sendimg);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {

                            AlertDialog.Builder builder=new AlertDialog.Builder(context);

                            builder.setMessage("Are You Sure Want To Delete");
                            builder.setTitle("Message");

                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();

                                    databaseReference.child("Chats").child(mUser.get(getAdapterPosition()).getChat_id()).removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful())
                                                    {
                                                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            });

                                }
                            });

                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.cancel();

                                }
                            });

                            AlertDialog dialog=builder.create();
                            dialog.show();


                            return true;
                        }
                    });

                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {


        if (mUser.get(position).getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
        {
            return MSG_TYPE_RIGHT;
        }
        else
        {
            return MSG_TYPE_LEFT;
        }

    }
}
