package teqvirtual.deep.fashionbook.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import teqvirtual.deep.fashionbook.MainProfileActivity;
import teqvirtual.deep.fashionbook.R;

public class CatAdapter extends RecyclerView.Adapter<CatAdapter.MyViewHolder> {

    int cat_img[];
    LayoutInflater layoutInflater;
    Context context;

    public CatAdapter(MainProfileActivity mainProfileActivity, int[] cat_img) {

        this.context=mainProfileActivity;
        this.cat_img=cat_img;
        this.layoutInflater=layoutInflater.from(context);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=layoutInflater.inflate(R.layout.cat_item,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.cat_img.setImageResource(cat_img[i]);

    }

    @Override
    public int getItemCount() {
        return cat_img.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView cat_img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cat_img=(ImageView)itemView.findViewById(R.id.cat_img);
        }
    }
}
