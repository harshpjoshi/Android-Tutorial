package teqvirtual.deep.newchat.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import teqvirtual.deep.newchat.Chat;
import teqvirtual.deep.newchat.R;

import static android.content.Context.MODE_PRIVATE;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    int MSG_TYPE_LEFT=0,MSG_TYPE_RIGHT=1;

    Context context;
    List<Chat> mUser;

    String fuser;

    public MessageAdapter(Context context, List<Chat> mUser)
    {
        this.context=context;
        this.mUser=mUser;
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
        holder.showMessage.setText(chat.getMessage());
        holder.time.setText(chat.getTime());
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView showMessage,time;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            showMessage=(TextView)itemView.findViewById(R.id.show_message);
            time=(TextView)itemView.findViewById(R.id.time);
        }
    }

    @Override
    public int getItemViewType(int position) {

        SharedPreferences settings = context.getSharedPreferences("settings", MODE_PRIVATE);
        String mobile = settings.getString("mobile_key", "");
        fuser =settings.getString("c_userid","");

        if (mUser.get(position).getSender().equals(fuser))
        {
            return MSG_TYPE_RIGHT;
        }
        else
        {
            return MSG_TYPE_LEFT;
        }

    }
}
