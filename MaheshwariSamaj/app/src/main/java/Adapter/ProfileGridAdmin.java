package Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import teqvirtual.deep.boranasamaj.AdminViewAdvertise;
import teqvirtual.deep.boranasamaj.AdminViewEvent;
import teqvirtual.deep.boranasamaj.AdminViewGallery;
import teqvirtual.deep.boranasamaj.AdminViewMember;
import teqvirtual.deep.boranasamaj.AdminViewNews;
import teqvirtual.deep.boranasamaj.Adminprofile;
import teqvirtual.deep.boranasamaj.R;

public class ProfileGridAdmin extends RecyclerView.Adapter<ProfileGridAdmin.MyViewHolder>{

    Context context;
    String[] items;
    int[] items_images;
    LayoutInflater layoutInflater;

    public ProfileGridAdmin(Adminprofile adminprofile, String[] items, int[] items_imges) {
        this.context=adminprofile;
        this.items=items;
        this.items_images=items_imges;
        layoutInflater=layoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.profile_item_layout,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.imageView.setImageResource(items_images[position]);
        holder.textView.setText(items[position]);
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public MyViewHolder(View itemView) {
            super(itemView);

            imageView=(ImageView)itemView.findViewById(R.id.profiile_item_image);
            textView=(TextView)itemView.findViewById(R.id.profile_item_text);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=getAdapterPosition();
                    if (pos==0)
                    {
                        context.startActivity(new Intent(context, AdminViewMember.class));
                    }
                    else if (pos==3)
                    {
                        context.startActivity(new Intent(context, AdminViewGallery.class));
                    }
                    else if (pos==4)
                    {
                        context.startActivity(new Intent(context, AdminViewAdvertise.class));
                    }
                    else if(pos==2)
                    {
                        context.startActivity(new Intent(context, AdminViewEvent.class));
                    }
                    else if (pos==1)
                    {
                        context.startActivity(new Intent(context, AdminViewNews.class));
                    }
                }
            });
        }
    }
}
