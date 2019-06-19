package teqvirtual.deep.healthcare.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import teqvirtual.deep.healthcare.R;

public class SubscriberHolder extends RecyclerView.ViewHolder {

    View mView;

    public SubscriberHolder(@NonNull View itemView) {
        super(itemView);

        mView=itemView;
    }

    public void setFirstname(String name)
    {
        TextView textView=(TextView)mView.findViewById(R.id.friend_subscriber_name);
        textView.setText(name);
    }


    public void setImage(Context context,String image)
    {
        CircleImageView circleImageView=(CircleImageView)mView.findViewById(R.id.friend_subscriber_img);

        Picasso.get().load(image).placeholder(R.drawable.logo).into(circleImageView);
    }

}
