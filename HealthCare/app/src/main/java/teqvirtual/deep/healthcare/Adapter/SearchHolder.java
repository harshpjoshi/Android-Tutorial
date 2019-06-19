package teqvirtual.deep.healthcare.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import teqvirtual.deep.healthcare.R;

public class SearchHolder extends RecyclerView.ViewHolder {

    View mView;

    public SearchHolder(@NonNull View itemView) {
        super(itemView);

        mView=itemView;
    }

    public void setFirstname(String name)
    {
        TextView textView=(TextView)mView.findViewById(R.id.req_subscriber_name);
        textView.setText(name);
    }


    public void setImage(Context context,String image)
    {
        CircleImageView circleImageView=(CircleImageView)mView.findViewById(R.id.req_subscriber_img);

        Picasso.get().load(image).placeholder(R.drawable.logo).into(circleImageView);
    }

    public View getAccept()
    {
        TextView textView=(TextView)mView.findViewById(R.id.accept_text);

        return textView;
    }

    public View getDecline()
    {
        TextView textView=(TextView)mView.findViewById(R.id.decline_text);

        return textView;
    }
}
