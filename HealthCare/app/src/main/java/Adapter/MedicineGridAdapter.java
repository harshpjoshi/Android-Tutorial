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
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Model.MedicineModel;
import teqvirtual.deep.healthcare.MainMedicine;
import teqvirtual.deep.healthcare.MedicineGrid;
import teqvirtual.deep.healthcare.R;

public class MedicineGridAdapter extends RecyclerView.Adapter<MedicineGridAdapter.MyViewHolder> {

    Context context;
    ArrayList<MedicineModel> arrayList;
    LayoutInflater layoutInflater;
    String userid;

    public MedicineGridAdapter(MedicineGrid medicineGrid, ArrayList<MedicineModel> arrayList, String userid) {

        this.context=medicineGrid;
        this.arrayList=arrayList;
        layoutInflater=layoutInflater.from(context);
        this.userid=userid;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.medicinegrid_item,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(arrayList.get(position).getImage()).into(holder.imageView);
        holder.name.setText(arrayList.get(position).getName());
        holder.price.setText("Rs:"+arrayList.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name,price;

        public MyViewHolder(View itemView) {
            super(itemView);

            imageView=(ImageView)itemView.findViewById(R.id.medicine_image);
            name=(TextView)itemView.findViewById(R.id.medicine_name);
            price=(TextView)itemView.findViewById(R.id.medicine_price);

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
