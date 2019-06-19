package teqvirtual.deep.fashionbook.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import teqvirtual.deep.fashionbook.MainProfileActivity;
import teqvirtual.deep.fashionbook.R;

public class ShoesAdapter extends RecyclerView.Adapter<ShoesAdapter.MyViewHolder> {

    Context context;
    LayoutInflater layoutInflater;
    int[] sho_img;

    public ShoesAdapter(MainProfileActivity mainProfileActivity, int[] sho_img) {
        this.context=mainProfileActivity;
        this.sho_img=sho_img;
        layoutInflater=layoutInflater.from(context);
    }

    @NonNull
    @Override
    public ShoesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view=layoutInflater.inflate(R.layout.shoes_item,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoesAdapter.MyViewHolder myViewHolder, int i) {

        myViewHolder.imageView.setImageResource(sho_img[i]);

    }

    @Override
    public int getItemCount() {
        return sho_img.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=(ImageView)itemView.findViewById(R.id.shoe_img);
        }
    }
}
