package teqvirtual.deep.boranasamaj;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder>{

    Context context;
    ArrayList<GalleryData> galleryData;
    LayoutInflater layoutInflater;
    public GalleryAdapter(Gallery gallery, ArrayList<GalleryData> arrayList) {
        this.context=gallery;
        this.galleryData=arrayList;
        layoutInflater=layoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=layoutInflater.inflate(R.layout.gallerylay,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(galleryData.get(position).getImage()).into(holder.gallery_img);
    }

    @Override
    public int getItemCount() {
        return galleryData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        ImageView gallery_img;
        public MyViewHolder(View itemView) {
            super(itemView);
            gallery_img=(ImageView)itemView.findViewById(R.id.gallery_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog=new Dialog(context);
                    dialog.setContentView(R.layout.subgallery_lay);
                    ImageView imageView=(ImageView)dialog.findViewById(R.id.subgal_img);

                    Picasso.get().load(galleryData.get(getAdapterPosition()).getImage()).into(imageView);
                    dialog.show();
                }
            });

        }
    }
}
