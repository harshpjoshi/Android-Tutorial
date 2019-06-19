package teqvirtual.deep.firebasechatapp.Holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import teqvirtual.deep.firebasechatapp.R;

public class SearchHolder extends RecyclerView.ViewHolder {

    View mView;

    public SearchHolder(@NonNull View itemView) {
        super(itemView);

        mView=itemView;
    }

    public void set_Name(String name)
    {
        TextView textView=(TextView)mView.findViewById(R.id.friendname);
        textView.setText(name);
    }

    public void set_Status(String status)
    {
        TextView textView=(TextView)mView.findViewById(R.id.friendstatus);
        textView.setText(status);
    }

    public void set_Profile(Context context,String image)
    {
        CircleImageView circleImageView=(CircleImageView)mView.findViewById(R.id.friendprofileimg);

        Picasso.get().load(image).placeholder(R.drawable.profile).into(circleImageView);
    }
}
