package teqvirtual.deep.newchat.Adapter;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;
import teqvirtual.deep.newchat.FriendProfileActivity;
import teqvirtual.deep.newchat.Notification.SendNotification;
import teqvirtual.deep.newchat.ProfileActivity;
import teqvirtual.deep.newchat.R;
import teqvirtual.deep.newchat.User;

public class AllFriendsAdapter extends RecyclerView.Adapter<AllFriendsAdapter.MyViewHolder> {

    Context context;
    LayoutInflater layoutInflater;
    String current_user;
    ArrayList<User> arrayList;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference;
    int current=0;

    public AllFriendsAdapter(Context context, ArrayList<User> arrayList,String current_user) {

        this.context=context;
        this.arrayList=arrayList;
        layoutInflater=layoutInflater.from(context);
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();
        this.current_user=current_user;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=layoutInflater.inflate(R.layout.user_item,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Picasso.get().load(arrayList.get(position).getImage()).placeholder(R.drawable.ic_launcher_background).into(holder.imageView);
        holder.textView.setText(arrayList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView,sendRequest;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=(ImageView)itemView.findViewById(R.id.friend_image);
            textView=(TextView)itemView.findViewById(R.id.friend_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   Intent intent=new Intent(context,FriendProfileActivity.class);
                   intent.putExtra("key",arrayList.get(getAdapterPosition()).getMyid());
                   context.startActivity(intent);

                }
            });

        }

    }
}
