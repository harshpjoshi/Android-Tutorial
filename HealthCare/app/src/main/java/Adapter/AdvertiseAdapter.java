package Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import teqvirtual.deep.healthcare.Profile;
import teqvirtual.deep.healthcare.R;

public class AdvertiseAdapter extends RecyclerView.Adapter<AdvertiseAdapter.MyviewHolder> {

    Context context;
    int[] ads_img;
    LayoutInflater layoutInflater;

    public AdvertiseAdapter(Context profile, int[] ads_img) {
        this.context=profile;
        this.ads_img=ads_img;
        layoutInflater=layoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.advertise_item,null);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
        Picasso.get().load(ads_img[position]).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return ads_img.length;
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        public MyviewHolder(View itemView) {
            super(itemView);

            imageView=(ImageView)itemView.findViewById(R.id.banner_image);

        }
    }
}
