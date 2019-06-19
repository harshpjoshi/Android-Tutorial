package teqvirtual.deep.healthcare.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import teqvirtual.deep.healthcare.Model.TipsModel;

import teqvirtual.deep.healthcare.R;
import teqvirtual.deep.healthcare.TipsDetail;

public class TipsAdapter extends RecyclerView.Adapter<TipsAdapter.MyViewHolder> {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<TipsModel> arrayList;

    public TipsAdapter(Context profile, ArrayList<TipsModel> arrayList) {

        this.context=profile;
        this.arrayList=arrayList;
        layoutInflater=layoutInflater.from(context);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.tips_item,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(arrayList.get(position).getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);

            imageView=(ImageView)itemView.findViewById(R.id.tips_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(context, TipsDetail.class);
                    intent.putExtra("tipsid",arrayList.get(getAdapterPosition()).getId());
                    intent.putExtra("docid",arrayList.get(getAdapterPosition()).getDoctor_id());
                    context.startActivity(intent);

                }
            });
        }
    }
}
