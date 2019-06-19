package teqvirtual.deep.healthcare.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import teqvirtual.deep.healthcare.Model.SubscriberModel;
import teqvirtual.deep.healthcare.R;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyClassViewHolder> {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<SubscriberModel> arrayList;

    public RequestAdapter(Context context, ArrayList<SubscriberModel> arrayList)
    {
        this.context=context;
        this.arrayList=arrayList;
        layoutInflater=layoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=layoutInflater.inflate(R.layout.request_list_item,null);

        return new MyClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyClassViewHolder holder, int position) {

        Picasso.get().load(arrayList.get(position).getImage()).into(holder.imageView);
        holder.textView.setText(arrayList.get(position).getFirstname());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyClassViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public MyClassViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=(ImageView)itemView.findViewById(R.id.req_subscriber_img);
            textView=(TextView)itemView.findViewById(R.id.req_subscriber_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                }
            });
        }
    }
}
