package teqvirtual.deep.firebasechatapp.Holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import teqvirtual.deep.firebasechatapp.Model.Chat;
import teqvirtual.deep.firebasechatapp.Model.UserData;
import teqvirtual.deep.firebasechatapp.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    int MSG_TYPE_LEFT=0,MSG_TYPE_RIGHT=1;

    Context context;
    List<Chat> mUser;

    FirebaseUser fuser;

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
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView showMessage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            showMessage=(TextView)itemView.findViewById(R.id.show_message);
        }
    }

    @Override
    public int getItemViewType(int position) {

        fuser= FirebaseAuth.getInstance().getCurrentUser();

        if (mUser.get(position).getSender().equals(fuser.getUid()))
        {
            return MSG_TYPE_RIGHT;
        }
        else
        {
            return MSG_TYPE_LEFT;
        }

    }
}
