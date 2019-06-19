package teqvirtual.deep.healthcare.Adapter;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


import androidx.recyclerview.widget.RecyclerView;

import teqvirtual.deep.healthcare.MedicineGrid;
import teqvirtual.deep.healthcare.Model.SubCategoryModel;
import teqvirtual.deep.healthcare.R;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyViewHolder> {

    Context context;
    ArrayList<SubCategoryModel> arrayList;
    LayoutInflater layoutInflater;
    String userid;

    public SubCategoryAdapter(Context subMedicine, ArrayList<SubCategoryModel> arrayList) {
        this.context=subMedicine;
        this.arrayList=arrayList;
        layoutInflater=layoutInflater.from(context);
        this.userid=userid;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.sub_medicine_item,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(context, MedicineGrid.class);
                    intent.putExtra("id",arrayList.get(getAdapterPosition()).getId());
                    intent.putExtra("name",arrayList.get(getAdapterPosition()).getName());
                    intent.putExtra("userid",userid);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }
            });

        }
    }
}
