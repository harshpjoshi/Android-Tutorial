package teqvirtual.deep.fashionbook.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import teqvirtual.deep.fashionbook.MainProfileActivity;
import teqvirtual.deep.fashionbook.R;

public class ProdCategoryAdapter extends RecyclerView.Adapter<ProdCategoryAdapter.MyViewHolder> {

    Context context;
    LayoutInflater layoutInflater;
    int[] prod_cat_img;
    String[] prod_cat_name;

    public ProdCategoryAdapter(MainProfileActivity mainProfileActivity, int[] prod_cat_img, String[] prod_cat_name) {

        this.context=mainProfileActivity;
        this.prod_cat_img=prod_cat_img;
        this.prod_cat_name=prod_cat_name;
        layoutInflater=layoutInflater.from(context);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=layoutInflater.inflate(R.layout.prodcat_item,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.imageView.setImageResource(prod_cat_img[i]);
        myViewHolder.textView.setText(prod_cat_name[i]);

    }

    @Override
    public int getItemCount() {
        return prod_cat_img.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textView=(TextView)itemView.findViewById(R.id.prodcat_text);
            imageView=(ImageView)itemView.findViewById(R.id.prodcat_img);

        }
    }
}
