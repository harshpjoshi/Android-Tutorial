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

import java.util.ArrayList;

import Model.SubCategoryModel;
import teqvirtual.deep.healthcare.MedicineGrid;
import teqvirtual.deep.healthcare.R;
import teqvirtual.deep.healthcare.SubMedicine;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyViewHolder> {

    Context context;
    ArrayList<SubCategoryModel> arrayList;
    LayoutInflater layoutInflater;
    String userid;

    public SubCategoryAdapter(SubMedicine subMedicine, ArrayList<SubCategoryModel> arrayList, String userid) {
        this.context=subMedicine;
        this.arrayList=arrayList;
        layoutInflater=layoutInflater.from(context);
        this.userid=userid;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.sub_medicine_item,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView.setText(arrayList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;
        public MyViewHolder(View itemView) {
            super(itemView);

            textView=(TextView)itemView.findViewById(R.id.subcat_name);
            imageView=(ImageView)itemView.findViewById(R.id.subcat_right);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(context,MedicineGrid.class);
                    intent.putExtra("id",arrayList.get(getAdapterPosition()).getId());
                    intent.putExtra("name",arrayList.get(getAdapterPosition()).getName());
                    intent.putExtra("userid",userid);
                    context.startActivity(intent);

                }
            });

        }
    }
}
