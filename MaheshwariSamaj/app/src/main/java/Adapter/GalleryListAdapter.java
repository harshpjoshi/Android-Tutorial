package Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Model.GalleryModel;
import teqvirtual.deep.boranasamaj.Gallery;
import teqvirtual.deep.boranasamaj.R;
import teqvirtual.deep.boranasamaj.ViewAddMain;

public class GalleryListAdapter extends RecyclerView.Adapter<GalleryListAdapter.MyViewHolder> {

    Context context;
    ArrayList<GalleryModel> arrayList;
    LayoutInflater layoutInflater;
    public GalleryListAdapter(Gallery gallery, ArrayList<GalleryModel> arrayList) {

        this.context=gallery;
        this.arrayList=arrayList;
        layoutInflater=layoutInflater.from(context);

    }

    @NonNull
    @Override
    public GalleryListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.gallery_item,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryListAdapter.MyViewHolder holder, int position) {
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

            imageView=(ImageView)itemView.findViewById(R.id.gallery_image_item);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Dialog dialog=new Dialog(context);
//                    dialog.setContentView(R.layout.galery_imagge);
//                    ImageView profile_pic=(ImageView)dialog.findViewById(R.id.gallery_pic_dialog);
//                    Picasso.get().load(arrayList.get(getAdapterPosition()).getImage()).into(profile_pic);
//                    dialog.show();

                    Intent intent=new Intent(context, ViewAddMain.class);
                    intent.putExtra("advertise_image",arrayList.get(getAdapterPosition()).getImage());
                    ActivityOptions activityOptions= null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        activityOptions = ActivityOptions.makeSceneTransitionAnimation((Activity) context,imageView,"profile");
                        context.startActivity(intent,activityOptions.toBundle());
                    }else
                    {
                        Toast.makeText(context, "Version Not Supported", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
