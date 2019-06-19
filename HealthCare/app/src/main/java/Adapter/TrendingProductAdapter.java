package Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Model.MedicineModel;
import teqvirtual.deep.healthcare.MainMedicine;
import teqvirtual.deep.healthcare.Profile;
import teqvirtual.deep.healthcare.R;

public class TrendingProductAdapter extends RecyclerView.Adapter<TrendingProductAdapter.MyViewHolder> {

    Context context;
    ArrayList<MedicineModel> arrayList;
    LayoutInflater layoutInflater;
    String userid;
    public TrendingProductAdapter(Context profile, ArrayList<MedicineModel> arrayList, String userid) {
        this.context=profile;
        layoutInflater=layoutInflater.from(context);
        this.arrayList=arrayList;
        this.userid=userid;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.trending_item,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Picasso.get().load(arrayList.get(position).getImage()).into(holder.imageView);
        holder.textView.setText(arrayList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);

            imageView=(ImageView)itemView.findViewById(R.id.trendingprod_image);
            textView=(TextView)itemView.findViewById(R.id.trendingprod_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(context,MainMedicine.class);
                    intent.putExtra("id",arrayList.get(getAdapterPosition()).getId());
                    intent.putExtra("userid",userid);
                    ActivityOptions activityOptions= null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        activityOptions = ActivityOptions.makeSceneTransitionAnimation((Activity) context,imageView,"medicine");
                    }
                    context.startActivity(intent,activityOptions.toBundle());
                }
            });
        }
    }
}
